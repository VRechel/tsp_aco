package parser;

import main.Configuration;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @author Viktor
 */
public class TSPParserTest {
    @Test
    public void getDistanceTest(){
        Parser parser = new TSPParser();

        double distance = parser.getDistance(10,15,32,35);
        Assert.assertEquals(Math.sqrt(22^2+20^2),distance,0.);
    }

    @Test
    public void parserTest(){
        Configuration.instance.landscape.reset();

        File file = new File("tspProblems/tspTest.tsp");
        Parser parser = new TSPParser();
        parser.parse(file);

        Assert.assertTrue(Configuration.instance.landscape.getNeighboursSize() > 0);
    }
}


