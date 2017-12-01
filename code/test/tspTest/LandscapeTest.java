package tspTest;

import main.Configuration;
import org.junit.Assert;
import org.junit.Test;
import tsp.City;
import util.CityPair;

/**
 * @author Viktor
 */
public class LandscapeTest {
    @Test
    public void getDistancesTest(){
        final City city = new City("A");
        final City neighbour = new City("B");
        final int distance = 5;

        Configuration.instance.landscape.addNeighbour(new CityPair(city, neighbour), distance);
        Assert.assertEquals(5, Configuration.instance.landscape.getDistance(city, neighbour),0.);
    }
}
