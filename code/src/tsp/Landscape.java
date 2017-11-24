package tsp;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Viktor
 */
public class Landscape {
    private Map<Pair<City,City>, Integer> neighbours = new HashMap<>();

    public void addNeighbour(City city,City neighbour, int distance){
        neighbours.put(new Pair<>(city,neighbour),distance);
    }

    public int getDistance(City city, City neighbour){
        return neighbours.get(new Pair<>(city, neighbour));
    }
}
