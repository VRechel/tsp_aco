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
    /**
        First of all the application will start the corresponding parser to the file specified in the Configuration.
        The application will then startup the database which will be used by the colony to save the achievements of each
        generation.
        After that the colony will be initialized. The colony will handle the rest.
     */
    public static void main(String args[]){
        Configuration.initLogger();

        initTSP();
        try {
            initDB();
        } catch (DBInitializationException e) {
            System.out.println("DB manager already initialized!");
            Configuration.instance.dbManager.dropTable();
        }
        Configuration.initLogger();
        initColony();

        // TODO: 05.12.2017 DB darf erst nach Ende sich schließen
        //At the end of the program the db has to be shutdown!
        //Configuration.instance.dbManager.shutdown();
    }

    /**
        The application will decide which parser is needed and initialize it. Then the file will be read and the problem
        will be created.
     */
    private static void initTSP() {
        Parser parser;
        String path = Configuration.instance.getFilePath();
        switch (path.substring(path.lastIndexOf("."))) {
            case ".xml":
                parser = new XMLParser();
                parser.parse(new File(path));
                break;
            case ".tsp":
                parser = new TSPParser();
                parser.parse(new File(path));
                break;
            default:
                System.out.println("File format not supported by now! Please use XML!");
                break;
        }
    }

    /**
        The HSQLDB has to be initialized at the first time.
     */
    private static void initDB() throws DBInitializationException {
        try {
            Configuration.instance.dbManager.startup();
        } catch (DBInitializationException e) {
            throw new DBInitializationException();
        }
        Configuration.instance.dbManager.init();
    }

    /**
        A new colony will be created. To function the colony has to initialize its pheromone matrix.
    */
    private static void initColony() {
        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException e) {
            e.printStackTrace();
        }
        colony.start();
    }
}
