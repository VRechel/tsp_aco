package parser;

import main.Configuration;
import tsp.City;

import java.io.File;
import java.util.logging.Level;

/**
 * @author Viktor
 */
public interface Parser {
    /*
        A parser has to be able to parse a given file. In most cases tsp problems are formulated after the standard.
        If another format will be used the parser has to be able to apply it to the class structure.

        @param  File    The file which has to be parsed
     */
    void parse(File file);

    /*
        For parsing a problem a parser has to be able to report the distance between two cities.
        In the standard tsp format it is described with coordinates.
        In other formats it may vary.

        @param  int     The x coordinate of the first city
        @param  int     Y coordinate of the first city
        @param  int     The x coordinate of the second city
        @param  int     Second y coordinate
        @return double  The distance between both cities
     */
    default double getDistance(int xSource, int ySource, int xDestination, int yDestination){
        return -1.;
    }

    /*
        Every parser has to be able to parse it's steps to the central log file.
        A default implementation is used as every parser will print it the same.

        @param  City    The first city
        @param  City    The second city
        @param  double  The distance between city a and city b
     */
    default void logEntry(City a, City b, double distance){
        Configuration.instance.logger.log(Level.CONFIG, "Added new Neighbours: " + " City " + a + " City " + b + " with distance " + distance);
    }
}
