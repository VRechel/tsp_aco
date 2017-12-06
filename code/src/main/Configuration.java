package main;

import tsp.Landscape;
import util.HSQLDBManager;
import util.MersenneTwisterFast;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Viktor
 */
public enum Configuration {
    instance;
    //Init
    public final MersenneTwisterFast randomNumberGenerator = new MersenneTwisterFast(System.currentTimeMillis());
    public final HSQLDBManager dbManager = HSQLDBManager.instance;
    public Landscape landscape = new Landscape();
    public static final Logger logger = Logger.getLogger("GLOBAL");
    static final Level loggingLevel = Level.INFO;

    //Parameters
    public static final int alpha = 3;  //Pheromongewichtung
    public static final int beta = 2;   //Distanzgewichtung
    final String filePath = "tspProblems/xmlTest.xml";
    public static final int maxDistance = 2708;
    public static final int numberAnts = 1;
    private static final BigDecimal idiocrazyFilter = BigDecimal.valueOf(0.005);

    //DB parameters
    private final String fileSeparator = System.getProperty("file.separator");
    private final String userDirectory = System.getProperty("user.dir");

    private final String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    public final String dataFilePath = dataDirectory + "tsp_antColony.csv";
    public final String dataRDirectory = userDirectory;
    public final String databaseFile = dataDirectory + "datastore.db";

    private static Logger getLogger() {
        if(logger == null){
            initLogger();
        }
        return logger;
    }

    public String getFilePath() {
        return filePath;
    }

    public BigDecimal getIdiocrazyFilter() {
        return idiocrazyFilter;
    }

    public static void log(Level level, String msg) {
        getLogger().log(level, msg);
        System.out.println(msg);
    }

    public static void initLogger(){
        try {
            FileHandler fh = new FileHandler("Logging.txt", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setLevel(loggingLevel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}