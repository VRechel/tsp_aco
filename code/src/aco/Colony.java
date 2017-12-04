package aco;

import main.Configuration;
import tsp.City;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class Colony {
    private boolean initialized;
    boolean started;
    //private double solutionQuality;
    int currentGeneration = 1;

    private final City start = Configuration.instance.landscape.getStartingCity();
    private static double[][] pheromones;
    private final ArrayList<Ant> ants = new ArrayList<>();
    private ArrayList<City> bestRoute = null;
    private static final int alpha = 3;  //Pheromongewichtung
    private static final int beta = 2;   //Distanzgewichtung
    boolean debug = false;

    public Colony(){
        initAnts();
    }

    /*
        If the colony has not already been started all threads (ants) will be started
     */
    public void start(){
        if(!started){
            started = true;
            for (Ant a:
                 ants) {
                a.start();
            }
        }
        else{
            System.out.println("Colony already started!");
        }
    }

    /*
        If a generation finished an the colony is already updated it will generate a new generation of ants.
        The debug parameter is only for testing purposes and will prevent any automatic start of the ants.
     */
    private void newGeneration() {
        started = false;
        ants.clear();
        initAnts();
        if(!debug)
            start();
        currentGeneration++;
    }

    /*
        The colony will create a number of ants according to the value of the configuration parameter
        Every ant will be synchronized with the same CyclicBarrier.
     */
    void initAnts() {
        CyclicBarrier barrier = new CyclicBarrier(Configuration.numberAnts, this::notifyColony);
        for(int i = 1; i < Configuration.numberAnts+1; i++)
            ants.add(new Ant(i, start,this, barrier));
    }

    /*
        The colony will initialize the 2D matrix for the pheromone values according to the neighbourhood matrix from
        the landscape.
     */
    public void initPheromone() throws PheromoneInitializationException {
        if(initialized){
            throw new PheromoneInitializationException();
        }
        else{
            int size = Configuration.instance.landscape.getNeighboursSize();
            pheromones = new double[size][size];

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    pheromones[x][y] = 1;
                }
            }
            initialized = true;
        }
    }

    /*
        The method which will be called by every ant caught by the barrier.
        The colony will print the best route to the console and then generate a new generation of ants.
     */
    void notifyColony() {
        System.out.println("Generation " + currentGeneration + " finished!");
        printRoute(bestRoute);
        newGeneration();
    }

    /*
        For every traveled path within the generation the value has to be updated.

        @param  City    The source city
        @param  City    The target city
        @param  double  The pheromone value which will be added to the current value in the pheromone matrix
     */
    synchronized void updatePheromones(City a, City b, double plevel) {
        pheromones[a.getId()][b.getId()] += plevel;
    }

    /*
        Every ant checks if their route is better than the current best solution. If it is the route will be saved as
        the new best route.

        @param  ArrayList<City> The route of cities
     */
    synchronized void updateRoute(ArrayList<City> route) {
        if(bestRoute == null)
            bestRoute = route;
        double current = getDistance(bestRoute);
        double new_ = getDistance(route);

        if(new_ < current){
            bestRoute = route;
            //The following code could be used to weigh the new best route additionally
            //It is not used at the moment as pheromone weights should be kept low
            //for (int x = 0; x < route.size()-1; x++) {
            //  City a = route.get(x);
            //  City b = route.get(x+1);
            //  updatePheromones(a, b,(1./Configuration.instance.landscape.getDistance(a,b)));
            //}
        }
    }

    /*
        This method is only called by an ant which has to suicide. It will stop itself and tell the colony to remove it.

        @param  Ant The ant which has to be killed
     */
    void killAnt(Ant a) {
        ants.remove(a);
        System.out.println("ERROR: Ant " + a.id + " was killed!");
    }

    /*
        The distance of a route is the fitness factor of this implementation. This method will sum up every traveled path.

        @param  ArrayList<City> The route of which the whole distance will be calculated
        @return double          The distance value of the given route
     */
    double getDistance(ArrayList<City> route) {
        double sum = 0;
        for (int i = 0; i < route.size()-1; i++) {
            sum += Configuration.instance.landscape.getDistance(route.get(i), route.get(i+1));
        }
        return sum;
    }

    /*
        For given and "solved" TSP problems it is possible to provide a approximately solution.
        To get the current solution quality of the colony the both values have to be divided.

        @return double  The solution quality
     */
    double getSolutionQuality() {
        if(bestRoute == null){return 0;}
        return Configuration.maxDistance / getDistance(bestRoute);
    }

    /*
        The current pheromone matrix can be printed by going through the matrix and printing every value with 2 decimals.
     */
    @SuppressWarnings("unused")
    public void printPheromones(){
        System.out.println("Pheromones:");
        for (double[] pheromone : pheromones) {
            for (int y = 0; y < pheromones.length; y++) {
                System.out.printf("%.2f", pheromone[y]);
                System.out.print(" \t");
            }
            System.out.print("\n");
        }
    }

    /*
        A given route can be printed by printing out the distance followed by the cities in order of visit.

        @param  ArrayList<City> The given route
     */
    private void printRoute(ArrayList<City> route) {
        StringBuilder sb = new StringBuilder();
        sb.append(" Distance: ").append(getDistance(route));
        sb.append(" Best Route: ");
        for (City city: route) {
            sb.append(city).append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        System.out.println(sb.toString());
    }

    double[][] getPheromones(){
        return pheromones;
    }
    boolean getInitialized() {
        return initialized;
    }
    int getAlpha() {
        return alpha;
    }
    int getBeta() {
        return beta;
    }
    double getPheromone(City a, City b) {
        return pheromones[a.getId()][b.getId()];
    }
    ArrayList<Ant> getAnts() {
        return ants;
    }
    ArrayList<City> getBestRoute() {
        return bestRoute;
    }
}