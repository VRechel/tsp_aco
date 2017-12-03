package main;

import aco.Colony;
import aco.PheromoneInitializationException;
import parser.Parser;
import parser.TSPParser;
import parser.XMLParser;
import util.DBInitializationException;

import java.io.File;

/**
 * @author Viktor
 */
class Application {
    public static void main(String args[]){
        initTSP();
        try {
            initDB();
        } catch (DBInitializationException e) {
            System.out.println("DB manager already initialized!");
            Configuration.instance.dbManager.dropTable();
        }

        initColony();

        //At the end of the program the db has to be shutdown!
        Configuration.instance.dbManager.shutdown();
    }

    private static void initColony() {
        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException e) {
            e.printStackTrace();
        }
        colony.start();
    }

    private static void initTSP() {
        Parser parser;
        String path = Configuration.instance.getFilePath();
        if(path.substring(path.lastIndexOf(".")).equals(".xml")){
            parser = new XMLParser();
            parser.parse(new File(path));
        }
        else if(path.substring(path.lastIndexOf(".")).equals(".tsp")){
            parser = new TSPParser();
            parser.parse(new File(path));
        } else{
            System.out.println("File format not supported by now! Please use XML!");
        }
        System.out.println("Paths: " + Configuration.instance.landscape.getNeighbours().size());
    }

    private static void initDB() throws DBInitializationException {
        try {
            Configuration.instance.dbManager.startup();
        } catch (DBInitializationException e) {
            throw new DBInitializationException();
        }
        try {
            Configuration.instance.dbManager.init(Configuration.instance.landscape.getNeighboursSize());
        } catch (DBInitializationException e) {
            throw new DBInitializationException();
        }
    }
}
