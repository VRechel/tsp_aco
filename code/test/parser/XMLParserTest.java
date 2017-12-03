package parser;

import main.Configuration;
import org.junit.Assert;
import org.junit.Test;
import tsp.Landscape;

import java.io.File;

/**
 * @author Viktor
 */
public class XMLParserTest {
    @Test
    public void parserTest(){
        Configuration.instance.landscape = new Landscape();

        File file = new File("tspProblems/xmlTest.xml");
        Parser parser = new XMLParser();
        parser.parse(file);

        Assert.assertTrue(Configuration.instance.landscape.getNeighboursSize() > 0);
    }
}
