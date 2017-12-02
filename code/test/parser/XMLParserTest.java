package parser;

import main.Configuration;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @author Viktor
 */
public class XMLParserTest {
    @Test
    public void parserTest(){
        Configuration.instance.landscape.reset();

        File file = new File("tspProblems/tspProblem.xml");
        Parser parser = new XMLParser();
        parser.parse(file);

        Assert.assertTrue(Configuration.instance.landscape.getNeighboursSize() > 0);
    }
}
