package acoTest;

import aco.Colony;
import aco.PheromoneInitializationError;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Viktor on 18.10.2017.
 */
public class ColonyTest {
    @Test
    public void updatePheromoneTest(){
        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationError pheromoneInitializationError) {
            pheromoneInitializationError.printStackTrace();
        }
        colony.updatePheromones("A", "B", 5);
        Assert.assertEquals(5, colony.getPheromones("A", "B"));
    }

    @Test
    public void initInitializedPheromonesTest(){
        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationError pheromoneInitializationError) {
            pheromoneInitializationError.printStackTrace();
        }
        try{
            colony.initPheromone();
        }
        catch(PheromoneInitializationError e){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void initPheromonesTest(){
        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationError pheromoneInitializationError) {
            pheromoneInitializationError.printStackTrace();
        }
        Assert.assertTrue(colony.getInitialized());
    }
}
