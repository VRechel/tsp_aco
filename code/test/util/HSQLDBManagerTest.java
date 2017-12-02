package util;

import main.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Viktor
 */
public class HSQLDBManagerTest {

    @Test
    public void initTest() {
        try{
            Configuration.instance.dbManager.init(2.);
        }
        catch(DBInitializationException e){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void updateTest() {
        Assert.fail();
    }

    @Test
    public void dropTableTest() {
        Assert.fail();
    }

    @Test
    public void createTableTest() {
        Configuration.instance.dbManager.createTable(2.);
        Assert.fail();
    }

    @Test
    public void updateTableTest() {
        Assert.fail();
    }

    @Before
    public void init(){
        try{
            Configuration.instance.dbManager.startup();
            Configuration.instance.dbManager.init(2.);
        }
        catch(DBInitializationException e){
            System.out.println(e.getMessage());
        }
    }

    @After
    public void shutdown(){
        Configuration.instance.dbManager.shutdown();
    }
}