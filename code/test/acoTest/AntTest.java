package acoTest;

import aco.Ant;
import aco.Colony;
import main.Configuration;
import org.junit.After;
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
public class AntTest {
    @Test
    public void calculateLambdaTest() throws Exception {
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        double expected = ((double) 1) / ((double)3);
        Assert.assertEquals(expected,a.calculateLambda(3,1),0.0);
    }

    @Test
    public void calculateLambdasTest() throws Exception {
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        ArrayList<CityPair> cities = new ArrayList<>();
        double expected = ((double) 1) / ((double)3);

        cities.add(new CityPair(new City("A"), new City("B")));
        cities.add(new CityPair(new City("A"), new City("C")));
        for (CityPair c: cities) {
            Configuration.instance.landscape.addNeighbour(c,3);
        }
        for (CityPair c: cities){
            a.getColony().updatePheromones(c, 1);
        }
        Map<CityPair, Double> lambdas = a.calculateLambdas(cities);

        Assert.assertTrue(lambdas.size() > 0);
        for (Map.Entry<CityPair, Double> entry:
             lambdas.entrySet()) {
            Assert.assertEquals(expected, entry.getValue(), 0.);
        }
    }

    @Test
    public void calculateProbabilityTest(){
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);

        double lambda = 1./3.;
        double sum = 3.;

        double p = a.calculateProbability(lambda,sum);
        Assert.assertEquals((1./3.)/3., p,0.0);
    }

    @Test
    public void calculateProbabilitiesTest() throws Exception {
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        Map<CityPair, Double> probabilities;
        Map<CityPair, Double> lambdas = new HashMap<>();
        ArrayList<CityPair> cities = new ArrayList<>();

        cities.add(new CityPair(new City("A"), new City("B")));
        cities.add(new CityPair(new City("A"), new City("C")));
        for (CityPair c: cities) {
            Configuration.instance.landscape.addNeighbour(c,3);
        }
        for (CityPair c: cities){
            a.getColony().updatePheromones(c, 1);
        }
        for (CityPair c: cities) {
            lambdas.put(c,1./3.);
        }

        probabilities = a.calculateProbabilities(cities, lambdas);
        Assert.assertTrue(probabilities.size() != 0);
        for (Map.Entry<CityPair, Double> entry:
             probabilities.entrySet()) {
            Assert.assertEquals(0.5, entry.getValue(),0.);
        }
    }

    @Test
    public void runTest(){
        Configuration.instance.setMaxIterations(1);
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        Configuration.instance.landscape.addNeighbour(new CityPair(new City("A"), new City( "B")), 2);
        Configuration.instance.landscape.addNeighbour(new CityPair(new City("A"), new City( "C")), 2);
        Configuration.instance.landscape.addNeighbour(new CityPair(new City("B"), new City( "A")), 2);
        Configuration.instance.landscape.addNeighbour(new CityPair(new City("B"), new City( "C")), 2);
        Configuration.instance.landscape.addNeighbour(new CityPair(new City("C"), new City( "A")), 2);
        Configuration.instance.landscape.addNeighbour(new CityPair(new City("C"), new City( "B")), 2);

        a.run();
        Assert.assertTrue(a.getRoute().size()!=1);
        Assert.assertTrue(a.getCurrentCity()==new City("A"));
    }

    @Test
    public void updatePheromonesTest(){
        Colony colony = new Colony();
        Ant a = new Ant(1, new City("A"), colony);
        ArrayList<City> route = new ArrayList<>();
        Map<CityPair, Double> expected = new HashMap<>();
        for (Map.Entry<CityPair, Double> entry:
            colony.getPheromones().entrySet()) {
            expected.put(entry.getKey(),entry.getValue());
        }

        route.add(new City("B"));
        route.add(new City("C"));
        route.add(new City("A"));
        a.setRoute(route);

        for(int x = 0; x < route.size()-1;x++){
            CityPair key = new CityPair(route.get(x), route.get(x+1));
            expected.put(key, (1+(1./3.)));
        }
        for(int x = 0; x < route.size()-1;x++){
            Configuration.instance.landscape.addNeighbour(new CityPair(route.get(x), route.get(x+1)), 3);
        }

        a.updatePheromones();
        Assert.assertEquals(expected,a.getColony().getPheromones());
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


    @After
    public void reset(){
        Configuration.instance.landscape.reset();
    }
}