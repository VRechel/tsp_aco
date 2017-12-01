package tsp;

import util.CityPair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Viktor
 */
public class Landscape {
    private Map<CityPair, Double> neighbours = new HashMap<>();

    public void addNeighbour(CityPair cities, double distance){
        neighbours.put(cities,distance);
    }

    public double getDistance(City city, City neighbour){
        return neighbours.get(new CityPair(city, neighbour));
    }

    public double getNeighboursSize(){ return neighbours.size();}
    public void reset() {
        neighbours = new HashMap<>();
    }

    public Map<CityPair,Double> getNeighbours() {
        return neighbours;
    }

    public Map<CityPair,Double> getSpecifiedNeighbours(City currentCity) {
        Map<CityPair, Double> temp = new HashMap<>();
        for (Map.Entry<CityPair, Double> entry:
             neighbours.entrySet()) {
            if(entry.getKey().getCityA() == currentCity)
                temp.put(entry.getKey(),entry.getValue());
        }
        return temp;
    }
}
