package tsp;

/**
 * @author Viktor
 */
public class Landscape {
    private static double[][] neighbours;
    private City startingCity;
    private boolean initialized = false;

    /*
        The neighbourhood matrix will be initialized by a given size. In the most cases it will be initialized by the
        parser which knows the dimension of the landscape.

        @param  int dimension of the problem
     */
    public void initNeighbours(int size){
        neighbours = new double[size][size];
        initialized = true;
    }

    /*
        If the given cities don't know each other (don't have a distance between them) the given distance will be saved
        to the matrix. If a distance is already saved a error code (-1) will be returned.

        @param  City    The source city
        @param  City    The new neighbour
        @param  double  The distance between the two cities
        @return int     A error code
     */
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

    /*
        For a given city the landscape can name every city which can reach the given city.

        @param  City        A given city
        @return double[]    An array of distances (the indexes of the array are the cities)
     */
    public double[] getSpecifiedNeighbours(City currentCity) {
        return neighbours[currentCity.getId()];
    }

    /*
        This method will return the distance between the given source city and the given target.

        @param  City    The source
        @param  City    The target
        @return double  The distance
     */
    public double getDistance(City city, City neighbour){
        return neighbours[city.getId()][neighbour.getId()];
    }


    public int getNeighboursSize(){ return neighbours.length;}
    public double[][] getNeighbours() {
        return neighbours;
    }
    public City getStartingCity() {
        return startingCity;
    }
}
