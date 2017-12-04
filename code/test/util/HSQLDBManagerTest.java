package util;

import main.Configuration;
import org.junit.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Viktor
 */
public class HSQLDBManagerTest {

    @Test
    public void initTest() {
        Configuration.instance.dbManager.dropTable();
        try{
            Configuration.instance.dbManager.init();
            try {
                Assert.assertEquals(0,Configuration.instance.dbManager.getTable("GENERATION").getRow());
            } catch (SQLException e) {
                e.printStackTrace();
                Assert.fail();
            }
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
    public void selectTest() {
        Assert.fail();
    }

    @Test
    public void getTableTest() {
        Configuration.instance.dbManager.dropTable();
        Configuration.instance.dbManager.createTable();
        try {
            Assert.assertEquals(3,Configuration.instance.dbManager.getTable("GENERATIONS").getMetaData().getColumnCount());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Configuration.instance.dbManager.updateTable("GENERATIONS", "0,1,2,0", 10);
        try {
            ResultSet result = Configuration.instance.dbManager.getTable("GENERATIONS");
            Assert.assertEquals("0,1,2,0",result.getString(1));
            Assert.assertEquals(10, result.getInt(2));
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }


    @Test
    public void dropTableTest() {
        Configuration.instance.dbManager.dropTable();
        Configuration.instance.dbManager.createTable();
        try {
            Assert.assertEquals(3,Configuration.instance.dbManager.getTable("GENERATIONS").getMetaData().getColumnCount());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Configuration.instance.dbManager.dropTable();
        try {
            Assert.assertEquals(null,Configuration.instance.dbManager.getTable("GENERATIONS").getMetaData().getColumnCount());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (NullPointerException ex){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void createTableTest() {
        Configuration.instance.dbManager.dropTable();
        Configuration.instance.dbManager.createTable();
        try {
            Assert.assertEquals(3,Configuration.instance.dbManager.getTable("GENERATIONS").getMetaData().getColumnCount());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
        try {
            Assert.assertEquals(3,Configuration.instance.dbManager.getTable("HISTORY").getMetaData().getColumnCount());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
        try {
            Assert.assertEquals(null,Configuration.instance.dbManager.getTable("TEST").getMetaData().getColumnCount());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (NullPointerException ex){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void updateTableTest() {
        Assert.fail();
    }

    @BeforeClass
    public static void init(){
        try{
            Configuration.instance.dbManager.startup();
            Configuration.instance.dbManager.init();
        }
        catch(DBInitializationException e){
            System.out.println(e.getMessage());
        }
    }

    @AfterClass
    public static void shutdown(){
        Configuration.instance.dbManager.shutdown();
    }
}