package acoTest;

import aco.Ant;
import aco.Colony;
import aco.PheromoneInitializationException;
import mainTest.Configuration;
import org.junit.Assert;
import org.junit.Test;
import tsp.City;

import java.util.ArrayList;

/**
 * @author Viktor
 */
public class ColonyTest {
    @Test
    public void updatePheromoneTest(){
        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException pe) {
            pe.printStackTrace();
        }
        colony.updatePheromone(new City("A"), new City("B"), 5);
        Assert.assertEquals(5, colony.getPheromones(new City("A"), new City("B")));
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
        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException pe) {
            pe.printStackTrace();
        }
        Assert.assertTrue(colony.getInitialized());
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
    public void iterationTest(){
        Colony colony = new Colony();
        ArrayList<Ant> ants = colony.getAnts();
        ArrayList<City> cities = new ArrayList<>();
        for (Ant a :
             ants) {
            cities.add(a.getCurrentCity());
        }
        colony.iteration();
        for (Ant a :
                ants) {
            if(cities.get(ants.indexOf(a))==a.getCurrentCity())
                Assert.fail();
        }
    }

    @Test
    public void solveTest(){
        Colony colony = new Colony();
        Colony.Result result = colony.solve();
        Assert.assertTrue(result.getClass()== Colony.Result.class);
    }

    @Test
    public void notifyColonyTest(){
        Colony colony = new Colony();
        colony.notifyColony();
        Assert.fail();
    }
}
