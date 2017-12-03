package aco;

import main.Configuration;
import tsp.City;
import tsp.CityPair;

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
            Map<CityPair, Double> neighbours = Configuration.instance.landscape.getSpecifiedNeighbours(this.getCurrentCity());
            ArrayList<CityPair> cities = new ArrayList<>();
            //Adding all neighbours of the current city to a temporary local list
            for (Map.Entry<CityPair, Double> entry : neighbours.entrySet()) {
                cities.add(entry.getKey());
            }

            //Checks which of the neighbours can be reached (weren't visited before)
            for(int i = 0; i < cities.size(); i++){
                if(this.route.contains(cities.get(i).getCityB()))
                    cities.remove(i--);
            }

            //Adds all reachable neighbours to a local list for easier comparison
            this.availableCities.clear();
            for (CityPair pair: cities) {
                this.availableCities.add(pair.getCityB());
            }

            //If all cities have been reached the ant has to travel back to the colony (the colony equals the first city)
            if(availableCities.size()==0) {
                setFinished();
                City start = this.route.get(0);
                availableCities.add(start);
                visitCity(start);
            } else {
                Map<CityPair, Double> lambdas = calculateLambdas(cities);
                Map<CityPair, Double> probabilities = calculateProbabilities(cities, lambdas);

                double rand = Configuration.instance.randomNumberGenerator.nextDouble();

                boolean trip = false;
                for (Map.Entry<CityPair, Double> probability :
                        probabilities.entrySet()) {
                    if (probability.getValue() > rand) {
                        this.visitCity(probability.getKey().getCityB());
//                    System.out.println("Probability: " + probability.getValue());
//                    System.out.println("Random: " + rand);
//                    System.out.println("Ant " + this.id  + " went from " + temp + " to " + currentCity);
                        runs++;
                        trip = true;
                    }
                }
                if (!trip) {
                    double sum = 0;
                    for (Map.Entry<CityPair, Double> probability :
                            probabilities.entrySet()) {
                        sum += probability.getValue();
                        if (sum > rand) {
                            this.visitCity(probability.getKey().getCityB());
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
            CityPair key = new CityPair(route.get(x), route.get(x+1));
            this.colony.updatePheromones(key
                    , 1+(1./Configuration.instance.landscape.getDistance(route.get(x),route.get(x+1))));
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

    public void setAvailableCities(ArrayList<City> availableCities) {
        this.availableCities = availableCities;
    }

    private void setFinished() {
        this.finished = true;
    }
}