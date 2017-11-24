package acoTest;

import aco.Ant;
import aco.Colony;
import org.junit.Assert;
import org.junit.Test;
import tsp.City;

import java.util.ArrayList;

/**
 * @author Viktor
 */
public class AntTest {
    @Test
    public void calculateProbabilities() throws Exception {
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        ArrayList<Float> probabilites = a.calculateProbabilities();
        Assert.fail();
    }

    @Test
    public void calculateProbability() throws Exception {
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        //a.calculateProbability();
        Assert.fail();
    }

    @Test
    public void calculateLambdas() throws Exception {
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        a.calculateLambdas();
        Assert.fail();
    }

    @Test
    public void calculateLambda() throws Exception {
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        //a.calculateLambda();
        Assert.fail();
    }

    @Test
    public void iterationTest(){
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        a.iteration();
        Assert.fail();
    }

    @Test
    public void runTest(){
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        a.run();
        Assert.fail();
    }

    @Test
    public void updatePheromonesTest(){
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        a.updatePheromones();
        Assert.fail();
    }

    @Test
    public void visitCityTest(){
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        ArrayList<City> route = a.getRoute();
        City target = new City("B");
        a.visitCity(target);
        if (a.getRoute().get(a.getRoute().size()-1) != target)
            Assert.fail();
        Assert.assertTrue(a.getCurrentCity()==target);
    }

    @Test
    public void calculateProbabilityTest(){
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        City target = new City("B");
        float p = a.calculateProbability(target, a.getColony().getPheromones(a.getCurrentCity(),target));
        Assert.assertEquals(2, p);
        Assert.fail();
    }
}