package acoTest;

import aco.Ant;
import aco.Colony;
import aco.PheromoneInitializationException;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import main.Configuration;
import org.junit.Assert;
import org.junit.Test;
import tsp.City;

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
    public void initAnts(){
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
}
