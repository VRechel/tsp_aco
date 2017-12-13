package parser;

import main.Configuration;
import org.junit.Assert;
import org.junit.Test;
import tsp.Landscape;

import java.io.File;

/**
 * @author Viktor
 */
public class TSPParserTest {
    @Test
    public void getDistanceTest(){
        Parser parser = new TSPParser();

        double distance = parser.getDistance(10,15,32,35);
        Assert.assertEquals(29.7321,distance,0.00009);
    }

    @Test
    public void parseTest(){
        Configuration.instance.landscape = new Landscape();

        File file = new File("tspProblems/tspTest.tsp");
        Parser parser = new TSPParser();
        parser.parse(file);

        Assert.assertTrue(Configuration.instance.landscape.getNeighboursSize() > 0);
    }
}


