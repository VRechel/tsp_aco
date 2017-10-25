package aco;

import javafx.util.Pair;
import tsp.City;

import java.util.HashMap;
import java.util.Map;

public class Colony {
    private boolean initialized;
    private Map<Pair<City,City>, Integer> pheromone = new HashMap<>();


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
}
