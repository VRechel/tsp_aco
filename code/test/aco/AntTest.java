package aco;

import main.Configuration;
import org.junit.After;
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
public class AntTest {
    @Test
    public void calculateLambdaTest() {
        final City a = new City("A");
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);

        double expected = ((double) 1) / ((double)3);
        Assert.assertEquals(expected,ant.calculateLambda(3,1),0.0);
    }

    @Test
    public void calculateLambdasTest() {
        final City a = new City("A");
        final City b = new City("B");
        final City c = new City("C");
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);
        ArrayList<CityPair> cities = new ArrayList<>();

        double expected = ((double) 1) / ((double)3);

        cities.add(new CityPair(a, b));
        cities.add(new CityPair(a, c));
        for (CityPair pair: cities) {
            Configuration.instance.landscape.addNeighbour(pair,3);
        }
        for (CityPair pair: cities){
            ant.getColony().updatePheromones(pair, 1);
        }
        Map<CityPair, Double> lambdas = ant.calculateLambdas(cities);

        Assert.assertTrue(lambdas.size() > 0);
        for (Map.Entry<CityPair, Double> entry:
             lambdas.entrySet()) {
            Assert.assertEquals(expected, entry.getValue(), 0.);
        }
    }

    @Test
    public void calculateProbabilityTest(){
        final City a = new City("A");
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);

        double lambda = 1./3.;
        double sum = 3.;

        double p = ant.calculateProbability(lambda,sum);
        Assert.assertEquals((1./3.)/3., p,0.0);
    }

    @Test
    public void calculateProbabilitiesTest() {
        final City a = new City("A");
        final City b = new City("B");
        final City c = new City("C");

        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);
        Map<CityPair, Double> probabilities;
        Map<CityPair, Double> lambdas = new HashMap<>();
        ArrayList<CityPair> cities = new ArrayList<>();

        cities.add(new CityPair(a, b));
        cities.add(new CityPair(a, c));
        for (CityPair pair: cities) {
            Configuration.instance.landscape.addNeighbour(pair,3);
        }
        for (CityPair pair: cities){
            ant.getColony().updatePheromones(pair, 1);
        }
        for (CityPair pair: cities) {
            lambdas.put(pair,1./3.);
        }

        probabilities = ant.calculateProbabilities(cities, lambdas);
        Assert.assertTrue(probabilities.size() != 0);
        for (Map.Entry<CityPair, Double> entry:
             probabilities.entrySet()) {
            Assert.assertEquals(0.5, entry.getValue(),0.);
        }
    }

    @Test
    public void runTest(){
        final City a = new City("A");
        final City b = new City("B");
        final City c = new City("C");

        Configuration.instance.setMaxIterations(1);
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);
        Configuration.instance.landscape.addNeighbour(new CityPair(a,b), 2);
        Configuration.instance.landscape.addNeighbour(new CityPair(a,c), 2);
        Configuration.instance.landscape.addNeighbour(new CityPair(b,a), 2);

        ant.getColony().updatePheromones(new CityPair(a,b),1.5);
        ant.getColony().updatePheromones(new CityPair(a,c),1.5);

        ant.run();
        Assert.assertTrue(ant.getRoute().size()!=1);
        Assert.assertTrue(ant.getCurrentCity()!=a);
    }

    @Test
    public void updatePheromonesTest(){
        final City a = new City("A");
        final City b = new City("B");
        final City c = new City("C");
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);
        ArrayList<City> route = new ArrayList<>();
        Map<CityPair, Double> expected = new HashMap<>();

        for (Map.Entry<CityPair, Double> entry:
            colony.getPheromones().entrySet()) {
            expected.put(entry.getKey(),entry.getValue());
        }

        route.add(b);
        route.add(c);
        route.add(a);
        ant.setRoute(route);

        for(int x = 0; x < route.size()-1;x++){
            CityPair key = new CityPair(route.get(x), route.get(x+1));
            expected.put(key, (1+(1./3.)));
        }
        for(int x = 0; x < route.size()-1;x++){
            Configuration.instance.landscape.addNeighbour(new CityPair(route.get(x), route.get(x+1)), 3);
        }

        ant.updatePheromones();
        Assert.assertEquals(expected,ant.getColony().getPheromones());
    }

    @Test
    public void visitCityTest(){
        final City a = new City("A");
        final City b = new City("B");
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);
        ArrayList<City> temp = new ArrayList<>();
        temp.add(b);
        ant.setAvailableCities(temp);
        ant.visitCity(b);

        if (ant.getRoute().get(ant.getRoute().size()-1) != b)
            Assert.fail();
        Assert.assertTrue(ant.getCurrentCity()==b);
    }

    @Test
    public void visitVisitedCityTest(){
        final City a = new City("A");
        final City b = new City("B");
        final City c = new City("C");
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);

        ArrayList<City> temp = new ArrayList<>();
        temp.add(b);
        temp.add(c);
        ant.setAvailableCities(temp);
        ant.visitCity(b);

        temp.clear();
        temp.add(a);
        temp.add(c);
        ant.setAvailableCities(temp);
        ant.visitCity(c);
        if (ant.getRoute().get(ant.getRoute().size()-1) != c)
            Assert.fail();
        Assert.assertTrue(ant.getCurrentCity()==c);

        temp.clear();
        temp.add(a);
        ant.setAvailableCities(temp);
        ant.visitCity(b);
        if (ant.getRoute().get(ant.getRoute().size()-1) != c && ant.getCurrentCity()==c)
            Assert.fail();
        Assert.assertTrue(ant.getCurrentCity()==c);
    }


    @After
    public void reset(){
        Configuration.instance.landscape.reset();
    }
}