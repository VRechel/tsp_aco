package aco;

import main.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import tsp.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Viktor
 */
public class AntTest {
    @Test
    public void calculateLambdaTest() {
        final City a = new City(1);
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);

        double expected = ((double) 1) / ((double)3);
        Assert.assertEquals(expected,ant.calculateLambda(3,1),0.0);
    }

    @Test
    public void calculateLambdasTest() {
        final City a = new City(1);
        final City b = new City(2);
        final City c = new City(3);
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);
        ArrayList<City> cities = new ArrayList<>();

        double expected = ((double) 1) / ((double)3);

        cities.add(b);
        cities.add(c);
        for (City city: cities) {
            Configuration.instance.landscape.addNeighbour(ant.getCurrentCity() ,city,3);
        }
        for (City city: cities){
            ant.getColony().updatePheromones(ant.getCurrentCity(), city, 1);
        }
        Map<City, Double> lambdas = ant.calculateLambdas(cities);

        Assert.assertTrue(lambdas.size() > 0);
        for (Map.Entry<City, Double> entry: lambdas.entrySet()) {
            Assert.assertEquals(expected, entry.getValue(), 0.);
        }
    }

    @Test
    public void calculateProbabilityTest(){
        final City a = new City(1);
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);

        double lambda = 1./3.;
        double sum = 3.;

        double p = ant.calculateProbability(lambda,sum);
        Assert.assertEquals((1./3.)/3., p,0.0);
    }

    @Test
    public void calculateProbabilitiesTest() {
        final City a = new City(1);
        final City b = new City(2);
        final City c = new City(3);

        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);
        Map<City, Double> probabilities;
        Map<City, Double> lambdas = new HashMap<>();
        ArrayList<City> cities = new ArrayList<>();

        cities.add(b);
        cities.add(c);
        for (City city: cities) {
            Configuration.instance.landscape.addNeighbour(ant.getCurrentCity(), city,3);
        }
        for (City city: cities){
            ant.getColony().updatePheromones(ant.getCurrentCity(), city, 1);
        }
        for (City city: cities) {
            lambdas.put(city, 1./3.);
        }

        probabilities = ant.calculateProbabilities(cities, lambdas);
        Assert.assertTrue(probabilities.size() != 0);
        for (Map.Entry<City, Double> entry:
             probabilities.entrySet()) {
            Assert.assertEquals(0.5, entry.getValue(),0.);
        }
    }

    @Test
    public void runTest(){
        final City a = new City(1);
        final City b = new City(2);
        final City c = new City(3);

        Configuration.instance.setMaxIterations(10);
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);
        Configuration.instance.landscape.addNeighbour(a,b, 2);
        Configuration.instance.landscape.addNeighbour(a,c, 2);
        Configuration.instance.landscape.addNeighbour(b,a, 2);
        Configuration.instance.landscape.addNeighbour(b,c, 2);
        Configuration.instance.landscape.addNeighbour(c,a, 2);
        Configuration.instance.landscape.addNeighbour(c,b, 2);

        ant.getColony().updatePheromones(a,b,1.5);
        ant.getColony().updatePheromones(a,c,1.5);
        ant.getColony().updatePheromones(b,a,1.5);
        ant.getColony().updatePheromones(b,c,1.5);
        ant.getColony().updatePheromones(c,a,1.5);
        ant.getColony().updatePheromones(c,b,1.5);

        ant.run();
        Assert.assertTrue(ant.getRoute().size()!=1);
        Assert.assertTrue(ant.getCurrentCity()==a);
    }

    @Test
    public void updatePheromonesTest(){
        final City a = new City(1);
        final City b = new City(2);
        final City c = new City(3);

        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);
        ArrayList<City> route = new ArrayList<>();
        double[][] tempPheromones = ant.getColony().getPheromones().clone();

        route.add(b);
        route.add(c);
        route.add(a);
        ant.setRoute(route);

        for(int x = 0; x < route.size()-1;x++){
            tempPheromones[x][x+1] = (1+(1./3.));
        }
        for(int x = 0; x < route.size()-1;x++){
            Configuration.instance.landscape.addNeighbour(route.get(x), route.get(x+1), 3);
        }

        ant.updatePheromones();
        Assert.assertArrayEquals(tempPheromones,ant.getColony().getPheromones());
    }

    @Test
    public void visitCityTest(){
        final City a = new City(1);
        final City b = new City(2);

        Configuration.instance.landscape.addNeighbour(a,b,2);
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
        final City a = new City(1);
        final City b = new City(2);
        final City c = new City(3);

        Configuration.instance.landscape.addNeighbour(a,b, 2);
        Configuration.instance.landscape.addNeighbour(a,c, 2);
        Configuration.instance.landscape.addNeighbour(b,a, 2);
        Configuration.instance.landscape.addNeighbour(b,c, 2);
        Configuration.instance.landscape.addNeighbour(c,a, 2);
        Configuration.instance.landscape.addNeighbour(c,b, 2);
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