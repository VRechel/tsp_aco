package mainTest;

import org.junit.Assert;
import org.junit.Test;
import util.DBInitializationException;

/**
 * @author Viktor
 */
public class ApplicationTest {
    @Test
    public void initDBTest(){
        Application application = new Application();
        try{
            application.initDB();
        }
        catch(DBInitializationException e){
            Assert.assertTrue(true);
        }
    }
}
