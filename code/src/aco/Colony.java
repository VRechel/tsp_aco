package aco;

import main.Configuration;
import tsp.City;
import tsp.CityPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class Colony {
    private boolean initialized;
    boolean started;
    private double solutionQuality;
    int currentGeneration = 1;

    private final City start = Configuration.instance.landscape.getStartingCity();
    private final Map<CityPair, Double> pheromones = new HashMap<>();
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
            for(Map.Entry<CityPair, Double> entry : Configuration.instance.landscape.getNeighbours().entrySet()){
                pheromones.put(entry.getKey(),1.);
            }
            initialized = true;
        }
    }

    public synchronized void updatePheromones(CityPair cities, double plevel) {
        pheromones.put(cities, plevel);
    }

    public Map<CityPair, Double> getPheromones(){
        return pheromones;
    }

    public double getPheromone(CityPair c) {
        return pheromones.get(c);
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