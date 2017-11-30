package aco;

import javafx.util.Pair;
import mainTest.Configuration;
import tsp.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public Map<Pair<City, City>, Float> calculateProbabilities() {
        Map<Pair<City, City>, Float> probabilites = new HashMap<>();
        return probabilites;
    }

    public double calculateProbability(City b, int pheromone) {
        return 0f;
    }

    public double calculateLambdas(ArrayList<City> cities) {
        return 0f;
    }

    public double calculateLambda(double distance, int pheromone) {
        double tau = Math.pow(pheromone, colony.getAlpha());
        double eta = Math.pow((1/distance), colony.getBeta());
        return tau * eta;
    }

    public Colony getColony() {
        return colony;
    }
}