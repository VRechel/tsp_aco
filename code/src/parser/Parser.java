package parser;

import main.Configuration;
import tsp.City;

import java.io.File;
import java.util.logging.Level;

/**
 * @author Viktor
 */
public interface Parser {
    void parse(File file);

    default double getDistance(int xSource, int ySource, int xDestination, int yDestination){
        return -1.;
    }

    default void logEntry(City a, City b, double distance){

        Configuration.instance.logger.log(Level.CONFIG, "Added new Neighbours: " + " City " + a + " City " + b + " with distance " + distance);
    }
}
