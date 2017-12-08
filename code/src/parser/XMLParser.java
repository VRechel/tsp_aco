package parser;

import main.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tsp.City;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Viktor
 */
public class XMLParser implements Parser {
    /**
        The XML parser will read in a xml file and set up the landscape with the information from it.
        Until now only one format will be accepted:
        <entry>
            <CityA>ID</CityA>
            <CityB>ID</CityB>
            <distance>DISTANCE</distance>
        </entry>

        @param  file    The file has to be parsed
     */
    public void parse(File file) {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document doc = null;

        //If the document builder can't find/open the specified file it will return a error message
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc     = builder.parse(file);
        }catch(ParserConfigurationException | SAXException | IOException ex){
            System.out.println("XML File " + file.getPath() + " could not be opened!");
        }

        if(doc != null) {
            NodeList nList = doc.getElementsByTagName("entry");

            Node node = nList.item(0);

            Element element = (Element) node;

            Configuration.instance.landscape.initNeighbours(Integer.valueOf(element.getElementsByTagName("dimension").item(0).getTextContent()));

            //Go through every node in the xml file
            for (int x = 1; x < nList.getLength(); x++) {
                node = nList.item(x);

                element = (Element) node;

                //Get the information from the different elements
                City a = new City(Integer.valueOf(element.getElementsByTagName("cityA").item(0).getTextContent()));
                City b = new City(Integer.valueOf(element.getElementsByTagName("cityB").item(0).getTextContent()));
                Double distance = Double.parseDouble(element.getElementsByTagName("distance").item(0).getTextContent());
                Configuration.instance.landscape.addNeighbour(a,b, distance);
                logEntry(a, b, distance);
            }
        }
    }
}
