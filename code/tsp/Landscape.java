package tsp;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Viktor on 18.10.2017.
 */
public class Landscape {
    private Map<Pair<String,String>, Integer> neighbours = new HashMap<>();

    public void addNeighbour(String city,String neighbour, int distance){
        neighbours.put(new Pair<>(city,neighbour),distance);
    }

    public int getDistance(String city, String neighbour){
        return neighbours.get(new Pair<>(city, neighbour));
    }
}
