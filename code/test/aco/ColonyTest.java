package aco;

import main.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tsp.City;
import tsp.Landscape;

import java.util.ArrayList;

/**
 * @author Viktor
 */
public class ColonyTest {
    @Test
    public void updatePheromoneTest(){
        final City a = new City(1);
        final City b = new City(2);
        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException pe) {
            pe.printStackTrace();
        }
        colony.updatePheromones(a,b, 5);
        Assert.assertEquals(6, colony.getPheromone(a, b),0.);
    }

    @Test
    public void initInitializedPheromonesTest(){
        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException pe) {
            pe.printStackTrace();
        }
        try{
            colony.initPheromone();
        }
        catch(PheromoneInitializationException e){
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }

    @Test
    public void initPheromonesTest(){
        final City a = new City(1);
        final City b = new City(2);
        Colony colony = new Colony();
        Configuration.instance.landscape.addNeighbour(a,b,2);
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException pe) {
            pe.printStackTrace();
        }
        Assert.assertEquals(2,colony.getPheromones().length);
        Assert.assertTrue(colony.getInitialized());
        Assert.assertEquals(1, colony.getPheromones()[a.getId()][b.getId()],0.);
    }
    
    @Test
    public void initAntsTest(){
        Colony colony = new Colony();
        for(int i = 0; i < colony.getAnts().size(); i++){
            colony.getAnts().remove(i);
            i--;
        }
        if(colony.getAnts().size()!=0)
            Assert.fail();
        else
            colony.initAnts();
        Assert.assertEquals(Configuration.numberAnts, colony.getAnts().size());
    }

    @Test
    public void killAntTest(){
        Colony colony = new Colony();
        if(colony.getAnts().size() == 0)
            Assert.fail();
        Ant a = colony.getAnts().get(0);
        colony.killAnt(a);
        Assert.assertTrue(!colony.getAnts().contains(a));
    }

    @Test
    public void printPheromonesTest(){
        final City a = new City(1);
        final City b = new City(2);
        Configuration.instance.landscape.addNeighbour(a,b,2);
        Configuration.instance.landscape.addNeighbour(b,a,2);

        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void notifyColonyTest(){
        final City a = new City(1);
        final City b = new City(2);

        Configuration.instance.landscape.addNeighbour(a,b,2);
        Configuration.instance.landscape.addNeighbour(b,a,2);

        Colony colony = new Colony();
        colony.debug = true;
        ArrayList<City> temp = new ArrayList<>();
        temp.add(a);
        temp.add(b);
        colony.updateRoute(temp);

        colony.notifyColony();

        Assert.assertTrue(!colony.started);
        Assert.assertTrue(colony.getAnts().size()>0);
        Assert.assertTrue(colony.currentGeneration == 2);
    }

    @Before
    @After
    public void resetLandscape(){
        Configuration.instance.landscape = new Landscape();
    }
}
