package aco;

import main.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parser.Parser;
import parser.TSPParser;
import tsp.City;
import tsp.Landscape;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Viktor
 */
public class ColonyTest {
    @Test
    public void updatePheromoneTest(){
        final City a = new City(1);
        final City b = new City(2);
        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException pe) {
            pe.printStackTrace();
        }
        colony.updatePheromones(a,b, 5);
        Assert.assertEquals(6, colony.getPheromone(a, b),0.);
    }

    @Test
    public void initInitializedPheromonesTest(){
        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException pe) {
            pe.printStackTrace();
        }
        try{
            colony.initPheromone();
        }
        catch(PheromoneInitializationException e){
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }

    @Test
    public void initPheromonesTest(){
        final City a = new City(1);
        final City b = new City(2);
        Colony colony = new Colony();
        Configuration.instance.landscape.addNeighbour(a,b,2);
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException pe) {
            pe.printStackTrace();
        }
        Assert.assertEquals(2,colony.getPheromones().length);
        Assert.assertTrue(colony.getInitialized());
        Assert.assertEquals(1, colony.getPheromones()[a.getId()][b.getId()],0.);
    }
    
    @Test
    public void initAntsTest(){
        Colony colony = new Colony();
        for(int i = 0; i < colony.getAnts().size(); i++){
            colony.getAnts().remove(i);
            i--;
        }
        if(colony.getAnts().size()!=0)
            Assert.fail();
        else
            colony.initAnts();
        Assert.assertEquals(Configuration.numberAnts, colony.getAnts().size());
    }

    @Test
    public void killAntTest(){
        Colony colony = new Colony();
        if(colony.getAnts().size() == 0)
            Assert.fail();
        Ant a = colony.getAnts().get(0);
        colony.killAnt(a);
        Assert.assertTrue(!colony.getAnts().contains(a));
    }

    @Test
    public void printPheromonesTest(){
        final City a = new City(1);
        final City b = new City(2);
        Configuration.instance.landscape.addNeighbour(a,b,2);
        Configuration.instance.landscape.addNeighbour(b,a,2);

        Colony colony = new Colony();
        try {
            colony.initPheromone();
        } catch (PheromoneInitializationException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void notifyColonyTest(){
        final City a = new City(1);
        final City b = new City(2);

        Configuration.instance.landscape.addNeighbour(a,b,2);
        Configuration.instance.landscape.addNeighbour(b,a,2);

        Colony colony = new Colony();
        colony.debug = true;
        ArrayList<City> temp = new ArrayList<>();
        temp.add(a);
        temp.add(b);
        System.out.println(colony.getBestRoute());
        colony.updateRoute(temp);

        colony.notifyColony();

        Assert.assertTrue(!colony.started);
        Assert.assertTrue(colony.getAnts().size()>0);
        Assert.assertTrue(colony.currentGeneration == 2);
    }

    @Before
    @After
    public void resetLandscape(){
        Configuration.instance.landscape = new Landscape();
    }

    @Test
    public void distanceCheck(){
        Parser parser = new TSPParser();
        parser.parse(new File(Configuration.instance.getFilePath()));
        Colony colony = new Colony();
        int[] temp = {
                1,
                2,
                242,
                243,
                244,
                241,
                240,
                239,
                238,
                237,
                236,
                235,
                234,
                233,
                232,
                231,
                246,
                245,
                247,
                250,
                251,
                230,
                229,
                228,
                227,
                226,
                225,
                224,
                223,
                222,
                221,
                220,
                219,
                218,
                217,
                216,
                215,
                214,
                213,
                212,
                211,
                210,
                207,
                206,
                205,
                204,
                203,
                202,
                201,
                198,
                197,
                196,
                195,
                194,
                193,
                192,
                191,
                190,
                189,
                188,
                187,
                186,
                185,
                184,
                183,
                182,
                181,
                176,
                180,
                179,
                150,
                178,
                177,
                151,
                152,
                156,
                153,
                155,
                154,
                129,
                130,
                131,
                20,
                21,
                128,
                127,
                126,
                125,
                124,
                123,
                122,
                121,
                120,
                119,
                157,
                158,
                159,
                160,
                175,
                161,
                162,
                163,
                164,
                165,
                166,
                167,
                168,
                169,
                170,
                172,
                171,
                173,
                174,
                107,
                106,
                105,
                104,
                103,
                102,
                101,
                100,
                99,
                98,
                97,
                96,
                95,
                94,
                93,
                92,
                91,
                90,
                89,
                109,
                108,
                110,
                111,
                112,
                88,
                87,
                113,
                114,
                115,
                117,
                116,
                86,
                85,
                84,
                83,
                82,
                81,
                80,
                79,
                78,
                77,
                76,
                75,
                74,
                73,
                72,
                71,
                70,
                69,
                68,
                67,
                66,
                65,
                64,
                58,
                57,
                56,
                55,
                54,
                53,
                52,
                51,
                50,
                49,
                48,
                47,
                46,
                45,
                44,
                59,
                63,
                62,
                118,
                61,
                60,
                43,
                42,
                41,
                40,
                39,
                38,
                37,
                36,
                35,
                34,
                33,
                32,
                31,
                30,
                29,
                28,
                27,
                26,
                22,
                25,
                23,
                24,
                14,
                15,
                13,
                12,
                11,
                10,
                9,
                8,
                7,
                6,
                5,
                4,
                277,
                276,
                275,
                274,
                273,
                272,
                271,
                16,
                17,
                18,
                19,
                132,
                133,
                134,
                270,
                269,
                135,
                136,
                268,
                267,
                137,
                138,
                139,
                149,
                148,
                147,
                146,
                145,
                199,
                200,
                144,
                143,
                142,
                141,
                140,
                266,
                265,
                264,
                263,
                262,
                261,
                260,
                259,
                258,
                257,
                254,
                253,
                208,
                209,
                252,
                255,
                256,
                249,
                248,
                278,
                279,
                3,
                280,
                1};

        ArrayList<City> tempRoute = new ArrayList<>();
        for (Integer i:
                temp) {
            tempRoute.add(new City(i));
        }

        Assert.assertEquals(281,tempRoute.size());
        //Assert.assertEquals(2708, colony.getDistance(tempRoute),0.);
    }
}
