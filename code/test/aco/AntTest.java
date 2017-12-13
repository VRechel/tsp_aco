package aco;

import main.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import tsp.City;
import tsp.Landscape;

import java.math.BigDecimal;
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

        BigDecimal expected = new BigDecimal((1./3.));
        expected = expected.pow(colony.getBeta());

        Assert.assertEquals(0,expected.compareTo(ant.calculateLambda(3.,1.)));
    }

    @Test
    public void calculateLambdasTest() {
        final City a = new City(1);
        final City b = new City(2);
        final City c = new City(3);
        Configuration.instance.landscape.initNeighbours(3);

        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException e) {
            e.printStackTrace();
        }
        Ant ant = new Ant(1, a, colony);
        ArrayList<City> cities = new ArrayList<>();

        BigDecimal expected = new BigDecimal((1./3.));
        expected = expected.pow(colony.getBeta());

        cities.add(b);
        cities.add(c);
        for (City city: cities) {
            Configuration.instance.landscape.addNeighbour(ant.getCurrentCity() ,city,3);
        }
        Map<City, BigDecimal> lambdas = ant.calculateLambdas(cities);

        Assert.assertTrue(lambdas.size() > 0);
        for (Map.Entry<City, BigDecimal> entry: lambdas.entrySet()) {
            Assert.assertEquals(0, expected.compareTo(entry.getValue()));
        }
    }

    @Test
    public void calculateProbabilityTest(){
        final City a = new City(1);
        Colony colony = new Colony();
        Ant ant = new Ant(1, a, colony);

        BigDecimal lambda = BigDecimal.valueOf(1./3.);
        BigDecimal sum = BigDecimal.valueOf(3.);

        BigDecimal p = ant.calculateProbability(lambda,sum);
        Assert.assertEquals(0, p.compareTo(BigDecimal.valueOf((1./3.)/3.)));
    }

    @Test
    public void calculateProbabilitiesTest() {
        final City a = new City(1);
        final City b = new City(2);
        final City c = new City(3);
        Configuration.instance.landscape.initNeighbours(3);

        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException e) {
            e.printStackTrace();
        }
        Ant ant = new Ant(1, a, colony);
        Map<City, BigDecimal> probabilities;
        Map<City, BigDecimal> lambdas = new HashMap<>();
        ArrayList<City> cities = new ArrayList<>();

        cities.add(b);
        cities.add(c);
        for (City city: cities) {
            Configuration.instance.landscape.addNeighbour(ant.getCurrentCity(), city,3);
        }
        for (City city: cities) {
            lambdas.put(city, BigDecimal.valueOf(1./3.));
        }

        probabilities = ant.calculateProbabilities(cities, lambdas);
        Assert.assertTrue(probabilities.size() != 0);
        for (Map.Entry<City, BigDecimal> entry:
             probabilities.entrySet()) {
            Assert.assertEquals(0, entry.getValue().compareTo(BigDecimal.valueOf(0.5)));
        }
    }

    @Test
    public void runTest(){
        final City a = new City(1);
        final City b = new City(2);
        final City c = new City(3);
        Configuration.instance.landscape.initNeighbours(3);

        Colony colony = new Colony();

        Ant ant = new Ant(1, a, colony);
        Configuration.instance.landscape.addNeighbour(a,b, 2);
        Configuration.instance.landscape.addNeighbour(a,c, 2);
        Configuration.instance.landscape.addNeighbour(b,a, 2);
        Configuration.instance.landscape.addNeighbour(b,c, 2);
        Configuration.instance.landscape.addNeighbour(c,a, 2);
        Configuration.instance.landscape.addNeighbour(c,b, 2);
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException e) {
            e.printStackTrace();
        }

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
        Configuration.instance.landscape.initNeighbours(3);

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
    public void visitNonAvailableCityTest(){
        final City a = new City(1);
        final City b = new City(2);
        final City c = new City(3);
        Configuration.instance.landscape.initNeighbours(3);

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
        Configuration.instance.landscape = new Landscape();
    }
}