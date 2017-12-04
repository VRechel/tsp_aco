package parser;

import javafx.util.Pair;
import main.Configuration;
import tsp.City;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Viktor
 */
public class TSPParser implements Parser {
    /*
        The parser will take a .tsp file and parse it to the landscape.
        The format is as follows:   <city ID> <x coordinate> <y coordinate>

        @param  File    The file which will be parsed
     */
    public void parse(File file) {
        HashMap<City, Pair<Integer, Integer>> cities = new HashMap<>();

        //A buffered reader will be used to parse the file. If no file can be found the exception will be caught.
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            System.out.println("Specified .tsp file was not found!");
            return;
        }

        try {
            String currentLine;
            String[] split;

            //The first three lines specify the problem but are not useful for the application.
            for (int i = 0; i < 3; i++) {
                br.readLine();
            }

            //Read in the dimension of the problem
            currentLine = br.readLine();
            split = currentLine.trim().split("\\s+");
            int dimension = Integer.parseInt(split[1]);
            //The landscape has to set up by naming the dimension
            Configuration.instance.landscape.initNeighbours(dimension);

            //The next two lines further name parameters of the problem but are not applicable to this use.
            for (int i = 0; i < 2; i++) {
                br.readLine();
            }

            //Read in all lines which name the cities
            //If EOF has been reached the parsing will end
            while ((currentLine = br.readLine()) != null) {
                if(currentLine.equals("EOF"))
                    break;
                split = currentLine.trim().split("\\s+");
                int id = Integer.parseInt(split[0]);
                int x = Integer.parseInt(split[1]);
                int y = Integer.parseInt(split[2]);

                cities.put(new City(id), new Pair<>(x,y));
            }
        }catch(IOException ioe){
            System.out.println("Error while reading the file!");
        }finally {
            try {
                br.close();
            }catch(IOException iex){
                System.out.println("Error while closing the file readers!");
            }
        }

        //With the HashMap cities the landscape will be initialized while adding every city to the landscape
        HashMap<City, Pair<Integer, Integer>> temp;
        for (Map.Entry<City, Pair<Integer, Integer>> city:
             cities.entrySet()) {
            temp = new HashMap<>(cities);
            temp.remove(city.getKey());

            for (Map.Entry<City, Pair<Integer, Integer>> neighbour:
                 temp.entrySet()) {
                double distance = getDistance(city.getValue().getKey(), city.getValue().getValue()
                        , neighbour.getValue().getKey(), neighbour.getValue().getValue());
                if(Configuration.instance.landscape.addNeighbour(city.getKey(), neighbour.getKey(), distance)==1)
                    logEntry(city.getKey(), neighbour.getKey(), distance);

                if(Configuration.instance.landscape.addNeighbour(neighbour.getKey(), city.getKey(), distance)==1)
                    logEntry(neighbour.getKey(), city.getKey(), distance);
            }
        }
    }

    /*
        The parser will calculate the distance between both cities with the Pythagorean theorem

        @param  int     The x coordinate of the first city
        @param  int     Y coordinate of the first city
        @param  int     The x coordinate of the second city
        @param  int     Second y coordinate
        @return double  The distance between both cities
     */
    public double getDistance(int xSource, int ySource, int xDestination, int yDestination){
        double tempX = Math.pow((xDestination - xSource),2);
        double tempY = Math.pow((yDestination - ySource),2);
        return Math.sqrt(tempX + tempY);
    }
}
