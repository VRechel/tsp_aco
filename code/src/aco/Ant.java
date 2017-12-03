package aco;

import main.Configuration;
import tsp.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Viktor
 */
class Ant extends Thread {
    final int id;
    private final Colony colony;
    private CyclicBarrier barrier;
    private City currentCity;
    private ArrayList<City> route;
    private ArrayList<City> availableCities = new ArrayList<>();
    private int runs = 0;
    private boolean finished = false;

    /*
        Constructor for testing purposes. It is used to minimize the amount of variables to be initialized for testing.
     */
    public Ant(int i, City start, Colony colony) {
        this.id = i;
        this.colony = colony;
        this.currentCity = start;
        this.route = new ArrayList<>();
        this.route.add(currentCity);
    }

    /*
        Constructor for productive use. All ants used by the program have to be initialized by using this constructor.

        @param i        An ID used to identify it by the application.
        @param start    The City from which the colony will start. All ants have to start at the same city.
        @param colony   The Colony the ant belongs to.
        @param barrier  A CyclicBarrier used to synchronize the threads (ants)
     */
    public Ant(int i, City start, Colony colony, CyclicBarrier barrier) {
        this.id = i;
        this.colony = colony;
        this.currentCity = start;
        this.route = new ArrayList<>();
        this.route.add(currentCity);
        this.barrier = barrier;
    }

    public void run(){
        while(!finished && Configuration.maxIterations > runs && this.colony.getSolutionQuality() < Configuration.instance.getQuality()) {
            double[] neighbours = Configuration.instance.landscape.getSpecifiedNeighbours(this.getCurrentCity());

            ArrayList<City> neighboursList = new ArrayList<>();
            //Adding all neighbours of the current city to a temporary local list
            for (int x = 0; x < neighbours.length; x++) {
                neighboursList.add(new City(x));
            }

            //Checks which of the neighbours can be reached (weren't visited before)
            for (int x = 0; x < neighboursList.size(); x++) {
                if(this.route.contains(neighboursList.get(x)))
                    neighboursList.remove(x--);
            }

            //Adds all reachable neighbours to a local list for easier comparison
            this.availableCities.clear();
            this.availableCities.addAll(neighboursList);

            //If all cities have been reached the ant has to travel back to the colony (the colony equals the first city)
            if(availableCities.size()==0) {
                setFinished();
                City start = this.route.get(0);
                availableCities.add(start);
                visitCity(start);
            } else {
                Map<City, Double> lambdas = calculateLambdas(this.availableCities);
                Map<City, Double> probabilities = calculateProbabilities(this.availableCities, lambdas);

                double rand = Configuration.instance.randomNumberGenerator.nextDouble();

                boolean trip = false;
                for (Map.Entry<City, Double> probability :
                        probabilities.entrySet()) {
                    if (probability.getValue() > rand) {
                        this.visitCity(probability.getKey());
//                    System.out.println("Probability: " + probability.getValue());
//                    System.out.println("Random: " + rand);
//                    System.out.println("Ant " + this.id  + " went from " + temp + " to " + currentCity);
                        runs++;
                        trip = true;
                    }
                }
                if (!trip) {
                    double sum = 0;
                    for (Map.Entry<City, Double> probability :
                            probabilities.entrySet()) {
                        sum += probability.getValue();
                        if (sum > rand) {
                            this.visitCity(probability.getKey());
//                        System.out.println("Ant " + this.id  + " went from " + temp + " to " + currentCity);
                            runs++;
                            trip = true;
                        }
                    }
                }
                if (!trip){
                    this.colony.killAnt(this);
                    return;
                }
            }
        }
        try {
            this.printRoute();
            this.updateColony();
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            System.out.println("Ant " + this.id + " has encountered a problem with the barrier!");
        }   catch (NullPointerException ex) {
            System.out.println("Barrier is not existing! If this was not a test, check barrier initialization in Colony!");
        }
    }

    private void updateColony() {
        this.updatePheromones();
        this.colony.updateRoute(this.route);
    }

    private void printRoute() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ant ").append(this.id).append(" Route: ");
        for (City city:
             this.route) {
            sb.append(city).append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));

        sb.append(" Distance: ").append(this.colony.getDistance(this.route));
        System.out.println(sb.toString());
    }

    public void updatePheromones() {
        for(int x = 0; x < route.size()-1; x++){
            this.colony.updatePheromones(route.get(x),route.get(x+1)
                    , (1./Configuration.instance.landscape.getDistance(route.get(x),route.get(x+1))));
        }
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void visitCity(City b){
        if(!availableCities.contains(b))
            return;
        this.currentCity = b;
        this.route.add(b);
//        printRoute();
    }

    public ArrayList<City> getRoute() {
        return route;
    }

    public Map<City, Double> calculateProbabilities(ArrayList<City> cities, Map<City, Double> lambdas) {
        Map<City, Double> probabilities = new HashMap<>();
        double sum = 0;
        for (Map.Entry<City, Double> lambda:
                lambdas.entrySet()) {
            sum += lambda.getValue();
        }
        for (City city:
             cities) {
            probabilities.put(city, calculateProbability(lambdas.get(city), sum));
        }
        return probabilities;
    }

    public double calculateProbability(double lambda, double sum) {
        return lambda / sum;
    }

    public Map<City, Double> calculateLambdas(ArrayList<City> cities) {
        Map<City, Double> lambdas = new HashMap<>();
        for (City city:
             cities) {
            lambdas.put(city, calculateLambda(
                                    Configuration.instance.landscape.getDistance(this.currentCity, city)
                                    ,this.getColony().getPheromone(this.currentCity, city)));
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

    public void setAvailableCities(ArrayList<City> availableCities) {
        this.availableCities = availableCities;
    }

    private void setFinished() {
        this.finished = true;
    }
}