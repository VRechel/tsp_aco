package parser;

import main.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tsp.City;
import util.CityPair;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Viktor
 */
public class XMLParser implements Parser {
    public void parse(File file) {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document doc = null;

        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc     = builder.parse(file);
        }catch(ParserConfigurationException | SAXException | IOException ex){
            System.out.println("XML File " + file.getPath() + " could not be opened!");
        }

        if(doc != null) {
            NodeList nList = doc.getElementsByTagName("entry");

            for (int x = 0; x < nList.getLength(); x++) {
                Node node = nList.item(x);

                Element element = (Element) node;

                City a = new City(element.getElementsByTagName("cityA").item(0).getTextContent());
                City b = new City(element.getElementsByTagName("cityB").item(0).getTextContent());
                Double distance = Double.parseDouble(element.getElementsByTagName("distance").item(0).getTextContent());
                Configuration.instance.landscape.addNeighbour(new CityPair(a,b), distance);
            }
        }
    }
}
