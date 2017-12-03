package aco;

import main.Configuration;
import org.junit.Assert;
import org.junit.Test;
import tsp.City;
import tsp.CityPair;

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
        final City a = new City("A");
        final City b = new City("B");
        Colony colony = new Colony();
        Configuration.instance.landscape.addNeighbour(new CityPair(a,b),2);
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException pe) {
            pe.printStackTrace();
        }
        Assert.assertEquals(1,colony.getPheromones().size());
        Assert.assertEquals(1, colony.getPheromones().get(new CityPair(a,b)),0.);
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
    public void printPheromonesTest(){
        final City a = new City("A");
        final City b = new City("B");
        Configuration.instance.landscape.addNeighbour(new CityPair(a,b),2);
        Configuration.instance.landscape.addNeighbour(new CityPair(b,a),2);

        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void notifyColonyTest(){
        Colony colony = new Colony();
        colony.debug = true;

        colony.notifyColony();

        Assert.assertTrue(!colony.started);
        Assert.assertTrue(colony.getAnts().size()>0);
        Assert.assertTrue(colony.currentGeneration == 2);


//        final City a = new City("A");
//        final City b = new City("B");
//        Colony colony = new Colony();
//        colony.debug = true;
//        Configuration.instance.landscape.addNeighbour(new CityPair(a,b), 3);
//        Configuration.instance.landscape.addNeighbour(new CityPair(b,a), 3);
//        colony.updatePheromones(new CityPair(a,b), 1);
//        colony.updatePheromones(new CityPair(b,a), 1);
//        Map<CityPair, Double> pheromones = new HashMap<>();

//        for (Map.Entry<CityPair, Double> entry:
//             colony.getPheromones().entrySet()) {
//            pheromones.put(entry.getKey(),entry.getValue());
//        }
//
//        colony.getAnts().add(new Ant(1,a,colony));
//        colony.getAnts().add(new Ant(2,a,colony));
//        ArrayList<City> route = new ArrayList<>();
//        route.add(a);
//        route.add(b);
//        for (Ant ant:
//             colony.getAnts()) {
//            ant.setRoute(route);
//        }
//
//        colony.notifyColony();
//
//        boolean change = false;
//        for (Map.Entry<CityPair, Double> entry:
//                pheromones.entrySet()) {
//            if(!entry.getValue().equals(colony.getPheromone(entry.getKey())))
//                change = true;
//        }
//        Assert.assertTrue(change);
    }
}
