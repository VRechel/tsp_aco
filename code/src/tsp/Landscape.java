package tsp;

/**
 * @author Viktor
 */
public class Landscape {
    private static double[][] neighbours;
    private City startingCity;
    private boolean initialized = false;

    private void initNeighbours(int size){
        neighbours = new double[size][size];
        initialized = true;
    }

    public int addNeighbour(City a, City b, double distance){
        if(!initialized)
            initNeighbours(2);
        if(startingCity == null)
            startingCity = a;
        if(neighbours[a.getId()][b.getId()]!=0)
            return -1;
        neighbours[a.getId()][b.getId()] = distance;
        return 1;
    }

    public double getDistance(City city, City neighbour){
        return neighbours[city.getId()][neighbour.getId()];
    }

    public int getNeighboursSize(){ return neighbours.length;}
    public void reset() {
        neighbours = new double[neighbours.length][neighbours.length];
    }

    public double[][] getNeighbours() {
        return neighbours;
    }

    public double[] getSpecifiedNeighbours(City currentCity) {
        return neighbours[currentCity.getId()];
    }

    public City getStartingCity() {
        return startingCity;
    }
}
