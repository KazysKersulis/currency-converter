package lt.kazys.currencyconverterapi.xmlparser;

import lt.kazys.currencyconverterapi.model.ExchangeData;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Component
public class XmlParser {

    private static final Logger LOG = LoggerFactory.getLogger(XmlParser.class);

    @Value("${openexchangeapi.root_uri}")
    private String uri;

    public ExchangeData parseXml() throws RuntimeException {
        ExchangeData exchangeData;
        Map<String, Double> rates = new HashMap<>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            String refDate = null;
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            URL url = new URL(uri);
            URLConnection urlConnection = url.openConnection();
            Document doc = dBuilder.parse(urlConnection.getInputStream());
            doc.getDocumentElement().normalize();
            NodeList cubeNodes = doc.getElementsByTagName("Cube");
            for (int nodeCt = 0; nodeCt < cubeNodes.getLength(); nodeCt++) {
                Node cubeNode = cubeNodes.item(nodeCt);
                NamedNodeMap attributeMap = cubeNode.getAttributes();
                if (attributeMap.getNamedItem("currency") != null && attributeMap.getNamedItem("rate") != null) {
                    Attr attrCcy = (Attr) attributeMap.getNamedItem("currency");
                    Attr attrRate = (Attr) attributeMap.getNamedItem("rate");
                    rates.put(attrCcy.getValue(), Double.parseDouble(attrRate.getValue()));
                } else if (attributeMap.getNamedItem("time") != null) {
                    Attr attrTime = (Attr) attributeMap.getNamedItem("time");
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                    refDate = LocalDate.parse(attrTime.getValue()).toString(fmt);
                }
            }
            exchangeData = new ExchangeData(rates, "EUR", refDate);
        } catch (Exception e) {
            LOG.error("Error parsing xml file from "+ uri);
            throw new RuntimeException();
        }
        return exchangeData;
    }

}
