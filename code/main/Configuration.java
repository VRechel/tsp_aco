package main;

import random.MersenneTwisterFast;

public enum Configuration {
    instance;
    //Init
    public MersenneTwisterFast randomNumberGenerator = new MersenneTwisterFast(System.currentTimeMillis());

    //Parameters
    public static final double quality = 0.95;
    public static int maxDistance = 2708;

    //DB parameters
    public String fileSeparator = System.getProperty("file.separator");
    public String userDirectory = System.getProperty("user.dir");

    public String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    public String dataFilePath = dataDirectory + "tsp_antColony.csv";

    public String dataRDirectory = userDirectory;

    public String databaseFile = dataDirectory + "datastore.db";
}