package parserTest;

import main.Configuration;
import org.junit.Assert;
import org.junit.Test;
import parser.Parser;
import parser.XMLParser;

import java.io.File;

/**
 * Created by Viktor on 01.12.2017.
 */
public class XMLParserTest {
    @Test
    public void parserTest(){
        Configuration.instance.landscape.reset();

        File file = new File("tspProblems/tspProblem.xml");
        System.out.println(file.getAbsolutePath());
        Parser parser = new XMLParser();
        parser.parse(file);

        Assert.assertTrue(Configuration.instance.landscape.getNeighboursSize() > 0);
    }
}
