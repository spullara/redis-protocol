package redis.clientgen;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import com.sampullara.mustache.Mustache;
import com.sampullara.mustache.MustacheBuilder;
import com.sampullara.mustache.MustacheException;
import com.sampullara.mustache.Scope;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Generate client code for redis based on the protocol.
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 9:10 PM
 */
public class Main {

  @Argument(alias = "l")
  private static String language = "java";

  @Argument(alias = "p")
  private static String pkg = "redis.client";

  @Argument(alias = "d", required = true)
  private static File dest;

  private static Set<String> keywords = new HashSet<String>() {{
    add("type");
    add("object");
  }};

  public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, MustacheException {
    try {
      Args.parse(Main.class, args);
    } catch (IllegalArgumentException e) {
      Args.usage(Main.class);
      System.exit(1);
    }

    MustacheBuilder mb = new MustacheBuilder("templates/" + language + "client");
    mb.setSuperclass(NoEncodingMustache.class.getName());
    Mustache mustache = mb.parseFile("client.txt");

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    XPathFactory xpf = XPathFactory.newInstance();
    final DocumentBuilder db = dbf.newDocumentBuilder();
    final XPathExpression replyX = xpf.newXPath().compile("//a");
    final Properties cache = new Properties();
    File cacheFile = new File("cache");
    if (cacheFile.exists()) {
      cache.load(new FileInputStream(cacheFile));
    }

    Set<String> ungenerated = new HashSet<String>(
            Arrays.asList(
                    "MULTI", "EXEC", "DISCARD", // Transactions
                    "PSUBSCRIBE", "SUBSCRIBE", "UNSUBSCRIBE", "PUNSUBSCRIBE" // subscriptions
            )
    );
    final Set<String> genericReply = new HashSet<String>(Arrays.asList(
            "ZRANK", "ZREVRANK" // Two different return values
    ));
    final Set<String> multiples = new HashSet<String>(Arrays.asList(
            "ZADD"
    ));
    JsonFactory jf = new MappingJsonFactory();
    JsonParser jsonParser = jf.createJsonParser(new URL("https://raw.github.com/antirez/redis-doc/master/commands.json"));
    final JsonNode commandNodes = jsonParser.readValueAsTree();
    Iterator<String> fieldNames = commandNodes.getFieldNames();
    List<Object> commands = new ArrayList<Object>();
    while (fieldNames.hasNext()) {
      final String command = fieldNames.next();
      if (ungenerated.contains(command)) continue;
      final String safeCommand = command.replace(" ", "_");
      String cacheReply = cache.getProperty(command.toLowerCase());
      if (cacheReply == null) {
        final Document detail = db.parse("http://query.yahooapis.com/v1/public/yql/javarants/redisreply?url=" + URLEncoder.encode("http://redis.io/commands/" + safeCommand.toLowerCase(), "utf-8"));
        cacheReply = replyX.evaluate(detail).replaceAll("[- ]", "").replaceAll("reply", "Reply").replaceAll("bulk", "Bulk").replaceAll("Statuscode", "Status");
        cache.setProperty(safeCommand.toLowerCase(), cacheReply);
        cache.store(new FileWriter(cacheFile), "# Updated " + new Date());
      }
      final String finalReply = cacheReply;
      final JsonNode commandNode = commandNodes.get(command);
      commands.add(new Object() {
        String name = safeCommand;
        String comment = commandNode.get("summary").getTextValue();
        String reply = (finalReply.equals("") || genericReply.contains(name)) ? "Reply" : finalReply;
        String version = commandNode.get("since").getTextValue();
        boolean usearray = false;
        List<Object> arguments = new ArrayList<Object>();
        {
          JsonNode argumentArray = commandNode.get("arguments");
          if (argumentArray != null) {
            if (argumentArray.size() > 3) {
              usearray = true;
            }
            boolean first = true;
            int argNum = 0;
            if (multiples.contains(name)) {
              arguments.add(new Object() {
                boolean first = true;
                boolean multiple = true;
                String typename = "Object";
                String name = "args";
              });
            } else {
              for (final JsonNode argumentNode : argumentArray) {
                JsonNode nameNodes = argumentNode.get("name");
                final String argName;
                if (nameNodes.isArray()) {
                  boolean f = true;
                  StringBuilder sb = new StringBuilder();
                  for (JsonNode nameNode : nameNodes) {
                    if (!f) {
                      sb.append("_or_");
                    }
                    f = false;
                    String textValue = nameNode.getTextValue();
                    sb.append(textValue);
                  }
                  argName = sb.toString();
                } else {
                  argName = nameNodes.getTextValue();
                }
                final boolean finalFirst = first;
                final int finalArgNum = argNum;
                final boolean isMultiple = argumentNode.get("multiple") != null ;
                arguments.add(new Object() {
                  boolean first = finalFirst;
                  boolean multiple = isMultiple;
                  String typename = "Object";
                  String name = (argName + finalArgNum).replace(" ", "_");
                });
                if (isMultiple) {
                  usearray = true;
                }
                first = false;
                argNum++;
                if (isMultiple) break;
              }
            }
          }
        }

        String methodname = safeCommand.toLowerCase();
        String quote = keywords.contains(methodname) ? "`" : "";
      });
    }

    Scope ctx = new Scope();
    ctx.put("commands", commands);
    File base = new File(dest, pkg.replace(".", "/"));
    base.mkdirs();
    Writer writer = new FileWriter(new File(base, "RedisClient." + language));
    mustache.execute(writer, ctx);
    writer.flush();
  }

  public static class NoEncodingMustache extends Mustache {
    @Override
    public String encode(String value) {
      return value;
    }
  }
}
