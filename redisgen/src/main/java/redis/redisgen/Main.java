package redis.redisgen;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
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
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Generate client code for redis based on the protocol.
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 9:10 PM
 */
@SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
public class Main {

  @Argument(alias = "l")
  private static String language = "java";

  @Argument(alias = "p")
  private static String pkg = "redis.client";

  @Argument(alias = "d", required = true)
  private static File dest;

  @Argument(alias = "t")
  private static String template = "client";

  @Argument(alias = "n")
  private static String className = "RedisClient";

  private static Set<String> keywords = new HashSet<String>() {{
    add("type");
    add("object");
  }};

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, MustacheException {
    try {
      Args.parse(Main.class, args);
    } catch (IllegalArgumentException e) {
      Args.usage(Main.class);
      System.exit(1);
    }

    MustacheFactory mb = new DefaultMustacheFactory("templates/" + language) {
      @Override
      public void encode(String value, Writer writer) {
        try {
          writer.write(value);
        } catch (IOException e) {
          throw new MustacheException();
        }
      }
    };
    Mustache mustache = mb.compile(template + ".txt");

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
                    "PSUBSCRIBE", "SUBSCRIBE", "UNSUBSCRIBE", "PUNSUBSCRIBE", // subscriptions
                    "AUTH" // authentication
            )
    );
    final Set<String> genericReply = new HashSet<String>(Arrays.asList(
            "SORT", // Can return an integer reply
            "ZRANK", "ZREVRANK", // Two different return values
            "SRANDMEMBER", // Can return a bulk or multibulk reply depending on count
            "BRPOPLPUSH" // If the timeout occurs it returns a Nil multi-bulk reply instead of a bulk reply
    ));
    final Set<String> multiples = new HashSet<String>(Arrays.asList(
            "ZADD"
    ));
    JsonFactory jf = new MappingJsonFactory();
    JsonParser jsonParser = jf.createJsonParser(new URL("https://raw.github.com/antirez/redis-doc/master/commands.json"));
    final JsonNode commandNodes = jsonParser.readValueAsTree();
    Iterator<String> fieldNames = commandNodes.getFieldNames();
    ImmutableListMultimap<String,String> group = Multimaps.index(fieldNames,
            new Function<String, String>() {
              @Override
              public String apply(String s) {
                return commandNodes.get(s).get("group").asText();
              }
            });
    List<Object> commands = new ArrayList<Object>();
    for (Map.Entry<String, String> entry : group.entries()) {
      String key = entry.getKey();
      final String groupName = key.substring(0, 1).toUpperCase() + key.substring(1);
      final String command = entry.getValue();
      if (ungenerated.contains(command)) continue;
      final boolean splitCommand = command.contains(" ");
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
        String group = groupName;
        boolean split_command = splitCommand;
        String name = safeCommand;
        String name1 = splitCommand ? name.substring(0, name.indexOf("_")) : name;
        String name2 = splitCommand ? name.substring(name.indexOf("_") + 1) : "";
        String comment = commandNode.get("summary").getTextValue();
        boolean generic = finalReply.equals("") || genericReply.contains(name);
        String reply = generic ? "Reply" : finalReply;
        String version = commandNode.get("since").getTextValue();
        boolean hasOptional = false;
        boolean hasMultiple = false;
        List<String> skipped = new ArrayList<String>();
        boolean varargs() {
          return (hasMultiple || hasOptional);
        }

        boolean usearray = false;
        List<Object> arguments = new ArrayList<Object>();

        int base_length() {
          return arguments.size() - (hasMultiple ? 1 : 0);
        }

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
                final boolean isMultiple = argumentNode.get("multiple") != null || argumentNode.get("command") != null;
                final boolean isOptional = argumentNode.get("optional") != null;
                if (isOptional) hasOptional = true;
                if (isMultiple) hasMultiple = true;
                final int finalArgNum1 = argNum;
                arguments.add(new Object() {
                  int arg_num = finalArgNum1;
                  boolean first = finalFirst;
                  boolean multiple = isMultiple;
                  String typename = "Object";
                  String name() {
                    String name = (argName + finalArgNum).replaceAll("[- :]", "_");
                    for (String s : skipped) {
                      name = s + "_" + name;
                    }
                    return name;
                  }
                  Boolean optional = isOptional;
                  boolean skip() {
                    boolean skip = hasMultiple && isOptional && !isMultiple;
                    if (skip) {
                      skipped.add(argName);
                    }
                    return skip;
                  }
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

    Map<String, Object> ctx = new HashMap<String, Object>();
    ctx.put("commands", commands);
    File base = new File(dest, pkg.replace(".", "/"));
    base.mkdirs();
    mustache.execute(new FileWriter(new File(base, className + "." + language)), ctx).close();
  }
}
