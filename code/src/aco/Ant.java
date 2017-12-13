package aco;

import main.Configuration;
import tsp.City;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;

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

    /**
        Constructor for testing purposes. It is used to minimize the amount of variables to be initialized for testing.
     */
    Ant(int i, City start, Colony colony) {
        this.id = i;
        this.colony = colony;
        this.currentCity = start;
        this.route = new ArrayList<>();
        this.route.add(currentCity);
    }

    /**
        Constructor for productive use. All ants used by the program have to be initialized by using this constructor.

        @param i        An ID used to identify it by the application.
        @param start    The City from which the colony will start. All ants have to start at the same city.
        @param colony   The Colony the ant belongs to.
        @param barrier  A CyclicBarrier used to synchronize the threads (ants)
     */
    Ant(int i, City start, Colony colony, CyclicBarrier barrier) {
        this.id = i;
        this.colony = colony;
        this.currentCity = start;
        this.route = new ArrayList<>();
        this.route.add(currentCity);
        this.barrier = barrier;
    }

    /**
        The routine every ant has to run through. It will do a travel between every city while only visiting every city
        just once.
        For every city all reachable neighbours have to be accounted as a potential target.
     */
    public void run(){
        while(!finished /*&& Configuration.maxIterations > runs/* /*&& this.colony.getSolutionQuality() < 0.95*/) {
            double[] neighbours = Configuration.instance.landscape.getSpecifiedNeighbours(this.getCurrentCity());

            ArrayList<City> neighboursList = new ArrayList<>();
            //Adding all neighbours of the current city to a temporary local list
            for (int x = 0; x < neighbours.length; x++) {
                neighboursList.add(new City(x+1));
            }

            //Checks which of the neighbours can be reached (weren't visited before)
            for (int x = 0; x < neighboursList.size(); x++) {
                if(this.route.contains(neighboursList.get(x))) {
                    neighboursList.remove(x);
                    x -= 1;
                }
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
                boolean trip = false;
                BigDecimal idiocrazy = BigDecimal.valueOf(Configuration.instance.randomNumberGenerator.nextDouble());

                //Idiocrazy(tm) filter
                //A ant has a very low probability to just use a random city instead of checking the best path
                //This is used to make sure that a single path will have too much weight
                if(idiocrazy.compareTo(Configuration.instance.getIdiocrazyFilter()) < 1) {
                    int city = Configuration.instance.randomNumberGenerator.nextInt(availableCities.size());
                    visitCity(availableCities.get(city));
                    runs++;
                    trip = true;
                }

                Map<City, BigDecimal> lambdas = calculateLambdas(this.availableCities);
                Map<City, BigDecimal> probabilities = calculateProbabilities(this.availableCities, lambdas);

                BigDecimal rand = BigDecimal.valueOf(Configuration.instance.randomNumberGenerator.nextDouble());

                //If one city has a probability higher than the random number it will be targeted
                if(!trip){
                    for (Map.Entry<City, BigDecimal> probability :
                            probabilities.entrySet()) {
                        if (probability.getValue().compareTo(rand) > 0) {
                            this.visitCity(probability.getKey());
                            runs++;
                            trip = true;
                            break;
                        }
                    }
                }
                //If no city can be found which has a higher probability than rand than the probabilities will be added
                // up until the sum is higher than rand.
                //The city at which sum gets bigger than rand will be targeted.
                if (!trip) {
                    BigDecimal sum = BigDecimal.valueOf(0.);
                    for (Map.Entry<City, BigDecimal> probability :
                            probabilities.entrySet()) {
                        sum = sum.add(probability.getValue());
                        if (sum.compareTo(rand) > 0) {
                            this.visitCity(probability.getKey());
                            runs++;
                            trip = true;
                            break;
                        }
                    }
                }
                //This should never be achievable
                //If the sum (which should be 1 after the adding up) is still smaller than the random number (smaller
                // than 1) than the ant has to be killed.
                if (!trip){
                    this.colony.killAnt(this);
                    return;
                }
            }
        }
        //After the ants have arrived at the start again they have to wait for the others
        //When all ants finished they will update the colony
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

    /**
        An ant updates the colony by updating the pheromones and checking if its own route is better than the one from
        the colony.
    */
    private void updateColony() {
        this.updatePheromones();
        this.colony.updateRoute(this.route);
    }

    /**
        The pheromones are updated by adding eta (1/distance) to every path taken by the ant
     */
    public void updatePheromones() {
        for(int x = 0; x < route.size()-1; x++){
            this.colony.updatePheromones(route.get(x),route.get(x+1)
                    , (1./Configuration.instance.landscape.getDistance(route.get(x),route.get(x+1))));
        }
    }

    /**
        An ant travels between two cities. If the ant has the given city not as a reachable target she won't leave

        @param  b   The city the ant has to go to
     */
    public void visitCity(City b){
        if(!availableCities.contains(b))
            return;
        this.currentCity = b;
        this.route.add(b);
    }

    /**
        For every reachable city the probability has to be calculated which will be used to decide the target

        @param  cities                  A list of the cities which can be reached
        @param  lambdas                 The list of lambdas which has to generated before calculating the probabilities
        @return Map<City, BigDecimal>   The list of probabilities
     */
    public Map<City, BigDecimal> calculateProbabilities(ArrayList<City> cities, Map<City, BigDecimal> lambdas) {
        Map<City, BigDecimal> probabilities = new HashMap<>();
        BigDecimal sum = BigDecimal.valueOf(0);
        for (Map.Entry<City, BigDecimal> lambda: lambdas.entrySet()) {
            sum = sum.add(lambda.getValue());
        }
        for (City city: cities) {
            probabilities.put(city, calculateProbability(lambdas.get(city), sum));
        }
        return probabilities;
    }

    /**
        A probability to travel to a singe city is defined through the lambda and the sum of all lambdas

        @param  lambda  The lambda for a single city
        @param  sum  The sum of the lambdas of all reachable cities
        @return BigDecimal  The probability
     */
    public BigDecimal calculateProbability(BigDecimal lambda, BigDecimal sum) {
        return lambda.divide(sum, 20, BigDecimal.ROUND_CEILING);
    }

    /**
        From the list of reachable cities a HashMap of the cities and their lambda has to be created

        @param  cities       The list of all reachable cities
        @return Map<City, BigDecimal>   A mapping between the cities and the corresponding lambda
     */
    public Map<City, BigDecimal> calculateLambdas(ArrayList<City> cities) {
        Map<City, BigDecimal> lambdas = new HashMap<>();
        for (City city:
             cities) {
            lambdas.put(city, calculateLambda(
                                    Configuration.instance.landscape.getDistance(this.currentCity, city)
                                    ,this.getColony().getPheromone(this.currentCity, city)));
        }
        return lambdas;
    }

    /**
        A single lambda for a single city is calculated by multiplying tau (pheromone level of the path) and eta
        (inverse of the distance). Eta and tau are weighted with alpha and beta (from the colony=

        @param distance       The distance between the current city and the target
        @param pheromone       The pheromone level between both cities
        @return BigDecimal  Lambda
     */
    public BigDecimal calculateLambda(double distance, double pheromone) {
        if(distance==0)
            return BigDecimal.valueOf(1);
        BigDecimal tau = new BigDecimal(pheromone);
        tau = tau.pow(colony.getAlpha());
        BigDecimal eta = new BigDecimal((1./distance));
        eta = eta.pow(colony.getBeta());
        return tau.multiply(eta);
    }

    /**
        Every ant will log their route to the central log file.
     */
    private void printRoute() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ant ").append(this.id).append(" Route: ");
        for (City city:
                this.route) {
            sb.append(city).append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));

        sb.append(" Distance: ").append(this.colony.getDistance(this.route));
        Configuration.logger.log(Level.CONFIG, sb.toString());
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

    public City getCurrentCity() {return currentCity;}

    public ArrayList<City> getRoute() {
        return route;
    }
}