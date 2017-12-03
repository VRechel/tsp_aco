package aco;

import main.Configuration;
import tsp.City;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class Colony {
    private boolean initialized;
    boolean started;
    private double solutionQuality;
    int currentGeneration = 1;

    private final City start = Configuration.instance.landscape.getStartingCity();
    private static double[][] pheromones;
    private final ArrayList<Ant> ants = new ArrayList<>();
    private static ArrayList<City> bestRoute;
    private static double bestDistance;
    private static final double alpha = 2;  //Pheromongewichtung
    private static final double beta = 1;   //Distanzgewichtung
    boolean debug = false;

    public Colony(){
        initAnts();
    }

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

    double getBeta() {
        return beta;
    }

    public void notifyColony() {
        System.out.println("Generation " + currentGeneration + " finished!");
        newGeneration();
    }

    private void newGeneration() {
//        System.out.println("Initializing new generation!");
        started = false;
        ants.clear();
        initAnts();
        if(!debug)
            start();
        currentGeneration++;
//        System.out.println("New generation started!");
    }

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

    public synchronized void updatePheromones(City a, City b, double plevel) {
        pheromones[a.getId()][b.getId()] += plevel;
    }

    public double[][] getPheromones(){
        return pheromones;
    }

    public double getPheromone(City a, City b) {
        return pheromones[a.getId()][b.getId()];
    }

    public ArrayList<Ant> getAnts() {
        return ants;
    }

    public void initAnts() {
        CyclicBarrier barrier = new CyclicBarrier(Configuration.numberAnts, this::notifyColony);
        for(int i = 1; i < Configuration.numberAnts+1; i++)
            ants.add(new Ant(i, start,this, barrier));
    }

    public void killAnt(Ant a) {
        ants.remove(a);
        System.out.println("ERROR: Ant " + a.id + " was killed!");
    }

    public Result solve() {
        return new Result(bestRoute,bestDistance);
    }

    double getAlpha() {
        return alpha;
    }

    public double getSolutionQuality() {
        return solutionQuality;
    }

    public synchronized void updateRoute(ArrayList<City> route) {
        if(bestRoute == null)
            bestRoute=route;
        double current = getDistance(bestRoute);
        double new_ = getDistance(route);

        if(new_ < current){
            bestRoute = route;
            bestDistance = new_;
        }
    }

    public double getDistance(ArrayList<City> route) {
        double sum = 0;
        for (int i = 0; i < route.size()-1; i++) {
            sum += Configuration.instance.landscape.getDistance(route.get(i), route.get(i+1));
        }
        return sum;
    }

    public boolean getInitialized() {
        return initialized;
    }

    public class Result{
        private final ArrayList<City> bestRoute;
        private final double bestDistance;

        Result(ArrayList<City> route, double distance){
            this.bestRoute = route;
            this.bestDistance = distance;
        }

        public ArrayList<City> getBestRoute() {
            return bestRoute;
        }
        public double getBestDistance() {
            return bestDistance;
        }
    }
}