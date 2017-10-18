package tspTest;

import main.Configuration;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Viktor on 18.10.2017.
 */
public class LandscapeTest {
    @Test
    public void getDistancesTest(){
        final String cityName = "A";
        final String neighbourName = "B";
        final int distance = 5;

        Configuration.instance.landscape.addNeighbour(cityName, neighbourName, distance);
        Assert.assertEquals(5, Configuration.instance.landscape.getDistance(cityName, neighbourName));
    }
}
