package redis.clientgen;

import com.sampullara.cli.Args;
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
import java.io.IOException;

/**
 * Generate client code for redis based on the protocol.
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 9:10 PM
 */
public class Main {
  public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
    try {
      Args.parse(Main.class, args);
    } catch (IllegalArgumentException e) {
      Args.usage(Main.class);
      System.exit(1);
    }

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document redis = db.parse("http://query.yahooapis.com/v1/public/yql/javarants/redis");
    XPathFactory xpf = XPathFactory.newInstance();
    XPath xPath = xpf.newXPath();
    NodeList commands = (NodeList) xPath.evaluate("//li", redis, XPathConstants.NODESET);
    XPathExpression commandX = xpf.newXPath().compile("span/a/text()");
    XPathExpression argumentsX = xpf.newXPath().compile("span/span[@class='args']/text()");
    XPathExpression summaryX = xpf.newXPath().compile("span[@class='summary']/text()");
    for (int i = 0; i < commands.getLength(); i++) {
      Node node = commands.item(i);
      String command = commandX.evaluate(node);
      String arguments = argumentsX.evaluate(node);
      String summary = summaryX.evaluate(node);
      System.out.println(command + " " + arguments + " " + summary);
    }
  }
}
