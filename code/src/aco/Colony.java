package aco;

import javafx.util.Pair;
import main.Configuration;
import tsp.City;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class Colony {
    private boolean initialized;
    private Map<Pair<City,City>, Integer> pheromone = new HashMap<>();
    private ArrayList<Ant> ants = new ArrayList<>();
    private final CyclicBarrier barrier;

    public Colony(){
        initAnts();
        barrier = new CyclicBarrier(ants.size(), this::notifyColony);
    }

    private void notifyColony() {
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
            throw new PheromoneInitializationException("Colony already initialized!");
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
            ants.add(new Ant());
    }

    public void killAnt(Ant a) {
        ants.remove(a);
        System.out.println("ERROR: Ant " + a.id + " was killed!");
    }
}
