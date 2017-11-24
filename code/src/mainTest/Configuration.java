package mainTest;

import util.HSQLDBManager;
import util.MersenneTwisterFast;
import tsp.Landscape;

public enum Configuration {
    instance;
    //Init
    public final MersenneTwisterFast randomNumberGenerator = new MersenneTwisterFast(System.currentTimeMillis());
    public final HSQLDBManager dbManager = HSQLDBManager.instance;
    public final Landscape landscape = new Landscape();

    //Parameters
    public static final double quality = 0.95;
    public static final int maxDistance = 2708;
    public static final int numberAnts = 8;

    //DB parameters
    private final String fileSeparator = System.getProperty("file.separator");
    private final String userDirectory = System.getProperty("user.dir");

    private final String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    public final String dataFilePath = dataDirectory + "tsp_antColony.csv";

    public final String dataRDirectory = userDirectory;

    public final String databaseFile = dataDirectory + "datastore.db";
}