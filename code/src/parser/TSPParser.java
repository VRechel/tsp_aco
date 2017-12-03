package parser;

import javafx.util.Pair;
import main.Configuration;
import sun.security.krb5.Config;
import tsp.City;
import tsp.CityPair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author Viktor
 */
public class TSPParser implements Parser {
    @Override
    public void parse(File file) {
        HashMap<City, Pair<Integer, Integer>> cities = new HashMap<>();

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            System.out.println("Specified .tsp file was not found!");
            return;
        }

        try {
            for (int i = 0; i < 6; i++) {
                br.readLine();
            }
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if(currentLine.equals("EOF"))
                    break;
                String[] split = currentLine.trim().split("\\s+");
                int id = Integer.parseInt(split[0]);
                int x = Integer.parseInt(split[1]);
                int y = Integer.parseInt(split[2]);

                cities.put(new City(id), new Pair<>(x,y));
            }
        }catch(IOException ioe){
            System.out.println("Error while reading the file!");
        }finally {
            try {
                if (br != null)
                    br.close();
            }catch(IOException iex){
                System.out.println("Error while closing the file readers!");
            }
        }

        HashMap<City, Pair<Integer, Integer>> temp;
        for (Map.Entry<City, Pair<Integer, Integer>> city:
             cities.entrySet()) {
            temp = new HashMap<>(cities);
            temp.remove(city.getKey());

            for (Map.Entry<City, Pair<Integer, Integer>> neighbour:
                 temp.entrySet()) {
                double distance = getDistance(city.getValue().getKey(), city.getValue().getValue()
                        , neighbour.getValue().getKey(), neighbour.getValue().getValue());
                if(Configuration.instance.landscape.addNeighbour(new CityPair(city.getKey(), neighbour.getKey())
                        , distance)==1)
                    logEntry(city.getKey(), neighbour.getKey(), distance);

                if(Configuration.instance.landscape.addNeighbour(new CityPair(neighbour.getKey(), city.getKey())
                        , distance)==1)
                    logEntry(neighbour.getKey(), city.getKey(), distance);
            }
        }
    }

    public double getDistance(int xSource, int ySource, int xDestination, int yDestination){
        double tempX = Math.pow((xDestination - xSource),2);
        double tempY = Math.pow((yDestination - ySource),2);
        return Math.sqrt(tempX + tempY);
    }
}