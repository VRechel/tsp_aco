package main;

import parser.Parser;
import parser.XMLParser;
import util.DBInitializationException;

/**
 * @author Viktor
 */
public class Application {
    public Application(){
        try {
            initDB();
        } catch (DBInitializationException e) {
            System.out.println("DB manager already initialized!");
            Configuration.instance.dbManager.dropTable();
        }

        Parser parser = new XMLParser();
    }

    public void initDB() throws DBInitializationException {
        try {
            Configuration.instance.dbManager.startup();
        } catch (DBInitializationException e) {
            throw new DBInitializationException();
        }
        try {
            Configuration.instance.dbManager.init();
        } catch (DBInitializationException e) {
            throw new DBInitializationException();
        }
    }
}
