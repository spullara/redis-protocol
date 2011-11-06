package redis.clientgen;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import com.sampullara.mustache.Mustache;
import com.sampullara.mustache.MustacheBuilder;
import com.sampullara.mustache.MustacheException;
import com.sampullara.mustache.Scope;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate client code for redis based on the protocol.
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 9:10 PM
 */
public class Main {

  @Argument(alias = "d", required = true)
  private static File dest;

  public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, MustacheException {
    try {
      Args.parse(Main.class, args);
    } catch (IllegalArgumentException e) {
      Args.usage(Main.class);
      System.exit(1);
    }

    MustacheBuilder mb = new MustacheBuilder("templates");
    mb.setSuperclass(NoEncodingMustache.class.getName());
    Mustache mustache = mb.parseFile("client.txt");

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    final DocumentBuilder db = dbf.newDocumentBuilder();
    Document redis = db.parse("http://query.yahooapis.com/v1/public/yql/javarants/redis");
    XPathFactory xpf = XPathFactory.newInstance();
    XPath xPath = xpf.newXPath();
    NodeList commandNodes = (NodeList) xPath.evaluate("//li", redis, XPathConstants.NODESET);
    final XPathExpression commandX = xpf.newXPath().compile("span/a/text()");
    final XPathExpression argumentsX = xpf.newXPath().compile("span/span[@class='args']/text()");
    final XPathExpression summaryX = xpf.newXPath().compile("span[@class='summary']/text()");
    final XPathExpression replyX = xpf.newXPath().compile("//a");
    List<Object> commands = new ArrayList<Object>();
    for (int i = 0; i < commandNodes.getLength(); i++) {
      final Node node = commandNodes.item(i);
      final String command = commandX.evaluate(node).replace(" ", "_");
      final String commandArguments = argumentsX.evaluate(node);
      final String commandSummary = summaryX.evaluate(node);
      final Document detail = db.parse("http://query.yahooapis.com/v1/public/yql/javarants/redisreply?url=" + URLEncoder.encode("http://redis.io/commands/" + command.toLowerCase(), "utf-8"));
      final String finalReply = replyX.evaluate(detail).replaceAll("[- ]", "").replaceAll("reply", "Reply").replaceAll("bulk", "Bulk");
      if (!commandArguments.contains("[") && !commandArguments.contains("|")) {
        commands.add(new Object() {
          String name = command;
          String comment = commandSummary;
          String reply = finalReply;
          List<Object> arguments = new ArrayList<Object>();
          {
            final String[] split = commandArguments.split(" ");
            for (int i = 0; split[0].length() > 0 && i < split.length; i++) {
              final int finalI = i;
              arguments.add(new Object() {
                String typename = "Object";
                String name = split[finalI].toLowerCase();
                boolean notlast = finalI != split.length - 1;
              });
            }
          }

          String methodname = command.toLowerCase();
        });
      }
    }
    Scope ctx = new Scope();
    ctx.put("commands", commands);
    Writer writer = new FileWriter(new File(dest, "redis/client/RedisClient.java"));
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
