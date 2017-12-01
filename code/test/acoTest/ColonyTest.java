package acoTest;

import aco.Ant;
import aco.Colony;
import aco.PheromoneInitializationException;
import main.Configuration;
import org.junit.Assert;
import org.junit.Test;
import tsp.City;
import util.CityPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        colony.updatePheromones(new CityPair(new City("A"), new City("B")), 5);
        Assert.assertEquals(5, colony.getPheromone(new CityPair(new City("A"), new City("B"))),0.);
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
    public void solveTest(){
        Colony colony = new Colony();
        Colony.Result result = colony.solve();
        Assert.assertTrue(result.getClass()== Colony.Result.class);
    }

    @Test
    public void notifyColonyTest(){
        Colony colony = new Colony();
        Configuration.instance.landscape.addNeighbour(new CityPair(new City("A"), new City("B")), 3);
        colony.updatePheromones(new CityPair(new City("A"), new City("B")), 1);
        Map<CityPair, Double> pheromones = new HashMap<>();
        for (Map.Entry<CityPair, Double> entry:
             colony.getPheromones().entrySet()) {
            pheromones.put(entry.getKey(),entry.getValue());
        }

        colony.getAnts().add(new Ant(1,new City("A"),colony));
        colony.getAnts().add(new Ant(2,new City("A"),colony));
        ArrayList<City> route = new ArrayList<>();
        route.add(new City("A"));
        route.add(new City("B"));
        for (Ant a:
             colony.getAnts()) {
            a.setRoute(route);
        }

        colony.notifyColony();

        for (Map.Entry<CityPair, Double> entry:
                pheromones.entrySet()) {
            Assert.assertTrue(!entry.getValue().equals(colony.getPheromone(entry.getKey())));
        }
    }
}
