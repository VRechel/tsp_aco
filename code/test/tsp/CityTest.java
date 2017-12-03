package tsp;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Viktor
 */
public class CityTest {
    @Test
    public void travelToCity(){
        City city = new City(1);
        try {
            city.visit();
        } catch (VisitationException ve){
            ve.printStackTrace();
        }

        Assert.assertTrue(city.getVisited());
    }

    @Test
    public void travelToVisitedCity(){
        City city = new City(1);
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
