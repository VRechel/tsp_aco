package tsp;

import main.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Viktor
 */
public class LandscapeTest {
    @Test
    public void getDistancesTest(){
        final City city = new City(1);
        final City neighbour = new City(2);
        final int distance = 5;

        Configuration.instance.landscape.addNeighbour(city, neighbour, distance);
        Assert.assertEquals(5, Configuration.instance.landscape.getDistance(city, neighbour),0.);
    }

    @Test
    public void getNeighboursSizeTest() {
        final City city = new City(1);
        final City neighbour = new City(2);
        final int distance = 5;

        Configuration.instance.landscape.addNeighbour(city, neighbour, distance);
        Assert.assertEquals(1,Configuration.instance.landscape.getNeighboursSize() ,0.);
    }

    @Test
    public void getSpecifiedNeighboursTest() {
        final City a = new City(1);
        final City b = new City(2);
        final City c = new City(3);

        Configuration.instance.landscape.addNeighbour(a,b,1);
        Configuration.instance.landscape.addNeighbour(a,c,1);
        Configuration.instance.landscape.addNeighbour(b,c,1);

        Assert.assertEquals(2, Configuration.instance.landscape.getSpecifiedNeighbours(new City(1)).length);
    }

    @Test
    public void resetTest() {
        final City a = new City(1);
        final City b = new City(2);

        Configuration.instance.landscape.addNeighbour(a,b,1);
        if(Configuration.instance.landscape.getNeighbours().length==0)
            Assert.fail();
        Configuration.instance.landscape.reset();
        Assert.assertTrue(Configuration.instance.landscape.getNeighbours().length==0);
    }

    @Test
    public void getNeighboursTest() {
        final City a = new City(1);
        final City b = new City(2);
        final City c = new City(3);

        Configuration.instance.landscape.addNeighbour(a,b,1);
        Configuration.instance.landscape.addNeighbour(a,c,1);
        Configuration.instance.landscape.addNeighbour(b,c,1);

        Assert.assertEquals(3, Configuration.instance.landscape.getNeighbours().length);
    }

    @Test
    public void addNeighbourTest() {
        final City a = new City(1);
        final City b = new City(2);

        if(Configuration.instance.landscape.getNeighbours().length!=0){
            Assert.fail();
        }
        Configuration.instance.landscape.addNeighbour(a,b,1);
        Assert.assertEquals(1, Configuration.instance.landscape.getNeighbours().length);
    }

    @Test
    public void addExistingNeighbourTest() {
        final City a = new City(1);
        final City b = new City(2);

        if(Configuration.instance.landscape.getNeighbours().length!=0){
            Assert.fail();
        }

        Configuration.instance.landscape.addNeighbour(a,b,1);
        Assert.assertEquals(-1,Configuration.instance.landscape.addNeighbour(a,b,1));
        Assert.assertEquals(1, Configuration.instance.landscape.getNeighbours().length);
    }

    @Test
    public void toStringTest() {
        final City a = new City(1);
        final City b = new City(2);
        Configuration.instance.landscape.addNeighbour(a,b,1);

        String sb = "Landscape: \n" +
                a + " " +
                b + " " +
                "\n";

        Assert.assertEquals(sb, Configuration.instance.landscape.toString());
    }

    @After
    public void resetAfter(){
        Configuration.instance.landscape.reset();
    }

    @Before
    public void resetBefore(){
        Configuration.instance.landscape.reset();
    }
}
