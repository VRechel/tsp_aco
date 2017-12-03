package tsp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Viktor
 */
public class Landscape {
    private final Map<CityPair, Double> neighbours = new HashMap<>();
    private City startingCity;

    public int addNeighbour(CityPair cities, double distance){
        if(startingCity == null)
            startingCity = cities.getCityA();
        if(neighbours.containsKey(cities))
            return -1;
        neighbours.put(cities,distance);
        return 1;
    }

    public double getDistance(City city, City neighbour){
        return neighbours.get(new CityPair(city, neighbour));
    }

    public double getNeighboursSize(){ return neighbours.size();}
    public void reset() {
        neighbours.clear();
    }

    public Map<CityPair,Double> getNeighbours() {
        return neighbours;
    }

    public Map<CityPair,Double> getSpecifiedNeighbours(City currentCity) {
        Map<CityPair, Double> temp = new HashMap<>();
        for (Map.Entry<CityPair, Double> entry : neighbours.entrySet()) {
            if(entry.getKey().getCityA().equals(currentCity)){
//                System.out.println(entry.getKey().getCityA());
//                System.out.println(entry.getKey().getCityB());
//                System.out.println(currentCity);
                temp.put(entry.getKey(),entry.getValue());
            }
        }
        return temp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Landscape: \n");
        for (Map.Entry<CityPair, Double> neighbour:
             neighbours.entrySet()) {
            sb.append(neighbour.getKey().getCityA()).append(" ");
            sb.append(neighbour.getKey().getCityB()).append(" ");
            sb.append("\n");
        }
        return sb.toString();
    }

    public City getStartingCity() {
        return startingCity;
    }
}
