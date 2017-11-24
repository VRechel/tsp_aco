package mainTest;

import util.DBInitializationException;

/**
 * @author Viktor
 */
class Application {
    Application(){
        try {
            initDB();
        } catch (DBInitializationException e) {
            System.out.println("DB manager already initialized!");
            Configuration.instance.dbManager.dropTable();
        }
    }

    void initDB() throws DBInitializationException {
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
