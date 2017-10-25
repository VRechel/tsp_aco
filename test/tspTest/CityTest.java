package tspTest;

import org.junit.Assert;
import org.junit.Test;
import tsp.City;
import tsp.VisitationException;

/**
 * @author Viktor
 */
public class CityTest {
    @Test
    public void travelToCity(){
        City city = new City("A");
        try {
            city.visit();
        } catch (VisitationException ve){
            ve.printStackTrace();
        }

        Assert.assertTrue(city.getVisited());
    }

    @Test
    public void travelToVisitedCity(){
        City city = new City("A");
        try {
            city.visit();
        }catch(VisitationException ve){
            ve.printStackTrace();
        }
        try {
            city.visit();
        }catch(VisitationException ve){
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }
}
