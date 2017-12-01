package aco;

import main.Configuration;
import tsp.City;
import util.CityPair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Viktor
 */
public class Ant extends Thread {
    final int id;
    private final Colony colony;
    private City currentCity;
    private ArrayList<City> route;

    public Ant(int i, City start, Colony colony) {
        this.id = i;
        this.colony = colony;
        this.currentCity = start;
        this.route = new ArrayList<>();
        this.route.add(currentCity);
    }

    public void run(){
        Map<CityPair, Double> neighbours = Configuration.instance.landscape.getSpecifiedNeighbours(this.getCurrentCity());
        ArrayList<CityPair> cities = new ArrayList<>();
        for (Map.Entry<CityPair, Double> entry:
             neighbours.entrySet()) {
            cities.add(entry.getKey());
        }

        Map<CityPair, Double> lambdas = calculateLambdas(cities);

    }

    public void updatePheromones() {
        for(int x = 0; x < route.size()-1; x++){
            CityPair key = new CityPair(route.get(x), route.get(x+1));
            this.colony.updatePheromones(key
                    , 1+(1./Configuration.instance.landscape.getDistance(route.get(x),route.get(x+1))));
        }
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void visitCity(City b) {
        this.currentCity = b;
        this.route.add(b);
    }

    public ArrayList<City> getRoute() {
        return route;
    }

    public Map<CityPair, Double> calculateProbabilities(ArrayList<CityPair> cities, Map<CityPair, Double> lambdas) {
        Map<CityPair, Double> probabilities = new HashMap<>();
        double sum = 0;
        for (Map.Entry<CityPair, Double> lambda:
                lambdas.entrySet()) {
            sum += lambda.getValue();
        }
        for (CityPair c:
             cities) {
            probabilities.put(c,
                    calculateProbability(lambdas.get(c), sum));
        }
        return probabilities;
    }

    public double calculateProbability(double lambda, double sum) {
        return lambda / sum;
    }

    public Map<CityPair, Double> calculateLambdas(ArrayList<CityPair> cities) {
        Map<CityPair, Double> lambdas = new HashMap<>();
        for (CityPair pair:
             cities) {
            lambdas.put(pair, calculateLambda(
                                    Configuration.instance.landscape.getDistance(pair.getCityA(), pair.getCityB())
                                    ,this.getColony().getPheromone(pair)));
        }
        return lambdas;
    }

    public double calculateLambda(double distance, double pheromone) {
        double tau = Math.pow(pheromone, colony.getAlpha());
        double eta = Math.pow((1/distance), colony.getBeta());
        return tau * eta;
    }

    public Colony getColony() {
        return colony;
    }

    public void setRoute(ArrayList<City> route) {
        this.route = route;
    }
}