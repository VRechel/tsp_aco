package main;

import tsp.Landscape;
import util.HSQLDBManager;
import util.MersenneTwisterFast;

import java.io.IOException;
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
    public final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final Level loggingLevel = Level.CONFIG;

    //Parameters
    final String filePath = "tspProblems/a280.tsp";
    public static final int maxDistance = 2708;
    public static final int numberAnts = 2;
    private static int maxIterations = 1;

    //DB parameters
    private final String fileSeparator = System.getProperty("file.separator");
    private final String userDirectory = System.getProperty("user.dir");

    private final String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    public final String dataFilePath = dataDirectory + "tsp_antColony.csv";
    public final String dataRDirectory = userDirectory;
    public final String databaseFile = dataDirectory + "datastore.db";

    {
        try {
            FileHandler fh = new FileHandler("Logging.txt");
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setLevel(loggingLevel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMaxIterations(int maxIterations) {
        Configuration.maxIterations = maxIterations;
    }

    public String getFilePath() {
        return filePath;
    }
}