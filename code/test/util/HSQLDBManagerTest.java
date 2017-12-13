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
        Configuration.instance.dbManager.init();
        try {
            Assert.assertEquals(0,Configuration.instance.dbManager.getTable("GENERATIONS").getRow());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void updateTest() {
        String sqlStringBuilder = "INSERT INTO GENERATIONS ( id , generation, route , distance ) "+
                "VALUES ( "+ 1 + "," + 1 + "," + "'0,1,0'" + "," + 2 + " ) ";
        Configuration.instance.dbManager.update(sqlStringBuilder);

        ResultSet rs = Configuration.instance.dbManager.getTable("GENERATIONS");
        try {
            rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
        try {
            Assert.assertEquals(1,rs.getRow());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void selectTest() {
        String sqlSelect = "SELECT * FROM GENERATIONS";

        String sqlUpdate = "INSERT INTO GENERATIONS ( id , generation, route , distance ) "+
                "VALUES ( "+ 1 + "," + 1 + "," + "'0,1,0'" + "," + 2 + " ) ";
        Configuration.instance.dbManager.update(sqlUpdate);


        ResultSet rs = Configuration.instance.dbManager.select(sqlSelect);
        try {
            if (rs != null) {
                rs.next();
                Assert.assertEquals(1, rs.getInt("generation"));
                Assert.assertEquals("0,1,0", rs.getString("route"));
                Assert.assertEquals(2, rs.getInt("distance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void getTableTest() {
        Configuration.instance.dbManager.init();
        try {
            Assert.assertEquals(4,Configuration.instance.dbManager.getTable("GENERATIONS").getMetaData().getColumnCount());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Configuration.instance.dbManager.updateTable("GENERATIONS", 1, "0,1,2,0", 10);
        ResultSet result = Configuration.instance.dbManager.getTable("GENERATIONS");
        try {
            result.next();
            Assert.assertEquals(1, result.getRow());
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
            Assert.assertEquals(4,Configuration.instance.dbManager.getTable("GENERATIONS").getMetaData().getColumnCount());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Configuration.instance.dbManager.dropTable();
        try {
            Assert.assertEquals(0,Configuration.instance.dbManager.getTable("GENERATIONS").getMetaData().getColumnCount());
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
            Assert.assertEquals(4,Configuration.instance.dbManager.getTable("GENERATIONS").getMetaData().getColumnCount());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
        try {
            Assert.assertEquals(4,Configuration.instance.dbManager.getTable("HISTORY").getMetaData().getColumnCount());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
        try {
            Assert.assertEquals(0,Configuration.instance.dbManager.getTable("TEST").getMetaData().getColumnCount());
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (NullPointerException ex){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void updateTableTest() {
        Configuration.instance.dbManager.updateTable("GENERATIONS", 1, "0,1,0", 2);

        ResultSet rs = Configuration.instance.dbManager.getTable("GENERATIONS");
        try {
            rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(1,rs.getRow());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @After
    public void reset(){
        Configuration.instance.dbManager.init();
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