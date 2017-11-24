package aco;

import mainTest.Configuration;
import tsp.City;

import java.util.ArrayList;

/**
 * @author Viktor
 */
public class Ant extends Thread {
    int id;
    private Colony colony;
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

    }

    public void updatePheromones() {
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void iteration() {
    }

    public void visitCity(City b) {
        this.currentCity = b;
        this.route.add(b);
    }

    public ArrayList<City> getRoute() {
        return route;
    }

    public ArrayList<Float> calculateProbabilities() {
        return null;
    }

    public float calculateProbability(City b, int pheromone) {
        return 0f;
    }

    public float calculateLambdas() {
        return 0f;
    }

    public float calculateLambda(City b, int pheromone) {
        int distance = Configuration.instance.landscape.getDistance(currentCity,b);
        float tau = (float)((Math.pow(pheromone, colony.getAlpha())));
        float eta = (float)((Math.pow((1/distance), colony.getBeta())));
        return tau * eta;
    }

    public Colony getColony() {
        return colony;
    }
}