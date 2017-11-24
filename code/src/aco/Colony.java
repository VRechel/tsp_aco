package aco;

import javafx.util.Pair;
import mainTest.Configuration;
import tsp.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class Colony {
    private boolean initialized;
    private Map<Pair<City,City>, Integer> pheromone = new HashMap<>();
    private ArrayList<Ant> ants = new ArrayList<>();
    private final CyclicBarrier barrier;
    private static ArrayList<City> bestRoute;
    private static int bestDistance;
    private static final double alpha = 2;  //Pheromongewichtung
    private static final double beta = 1;   //Distanzgewichtung

    public Colony(){
        initAnts();
        barrier = new CyclicBarrier(ants.size(), this::notifyColony);
    }

    double getBeta() {
        return beta;
    }

    public void notifyColony() {
        for (Ant a:
             getAnts()) {
            a.updatePheromones();
        }
    }

    public boolean getInitialized() {
        return initialized;
    }

    public void initPheromone() throws PheromoneInitializationException {
        if(initialized){
            throw new PheromoneInitializationException();
        }
        else{
            initialized = true;
        }
    }

    public void updatePheromone(City city, City target, int plevel) {
        pheromone.put(new Pair<>(city, target), plevel);
    }

    public int getPheromones(City a, City b) {
        return pheromone.get(new Pair<>(a, b));
    }

    public ArrayList<Ant> getAnts() {
        return ants;
    }

    public void initAnts() {
        for(int i = 0; i < Configuration.numberAnts; i++)
            ants.add(new Ant(i, new City("A"),this));
    }

    public void killAnt(Ant a) {
        ants.remove(a);
        System.out.println("ERROR: Ant " + a.id + " was killed!");
    }

    public void iteration() {
        for (Ant a:
             ants) {
            a.iteration();
        }
    }

    public Result solve() {
        return new Result(bestRoute,bestDistance);
    }

    double getAlpha() {
        return alpha;
    }

    public class Result{
        private final ArrayList<City> bestRoute;
        private final int bestDistance;

        Result(ArrayList<City> route, int distance){
            this.bestRoute = route;
            this.bestDistance = distance;
        }

        public ArrayList<City> getBestRoute() {
            return bestRoute;
        }
        public int getBestDistance() {
            return bestDistance;
        }
    }
}