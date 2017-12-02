package main;

import tsp.Landscape;
import util.HSQLDBManager;
import util.MersenneTwisterFast;

import java.util.logging.Logger;

/**
 * @author Viktor
 */
public enum Configuration {
    instance;
    //Init
    public final MersenneTwisterFast randomNumberGenerator = new MersenneTwisterFast(System.currentTimeMillis());
    public final HSQLDBManager dbManager = HSQLDBManager.instance;
    public final Landscape landscape = new Landscape();
    public final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    //Parameters
    final String filePath = "tspProblems/tspProblem.xml";
    private static final double quality = 0.95;
    //public static final int maxDistance = 2708;
    public static final int numberAnts = 4;
    public static int maxIterations = 100;

    //DB parameters
    private final String fileSeparator = System.getProperty("file.separator");
    private final String userDirectory = System.getProperty("user.dir");

    private final String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    public final String dataFilePath = dataDirectory + "tsp_antColony.csv";

    public final String dataRDirectory = userDirectory;

    public final String databaseFile = dataDirectory + "datastore.db";

    public double getQuality() {
        return quality;
    }

    public void setMaxIterations(int maxIterations) {
        Configuration.maxIterations = maxIterations;
    }

    public String getFilePath() {
        return filePath;
    }
}