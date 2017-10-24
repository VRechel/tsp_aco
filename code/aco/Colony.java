package aco;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Colony {
    private boolean initialized;
    private Map<Pair<String,String>, Integer> pheromone = new HashMap<>();


    public boolean getInitialized() {
        return initialized;
    }

    public void initPheromone() throws PheromoneInitializationError{
        if(initialized){
            throw new PheromoneInitializationError("Colony already initialized!");
        }
        else{
            initialized = true;
        }
        //TODO
    }

    public void updatePheromones(String city, String target, int plevel) {
        pheromone.put(new Pair<>(city, target), plevel);
    }

    public int getPheromones(String a, String b) {
        return pheromone.get(new Pair<>(a, b));
    }
}
