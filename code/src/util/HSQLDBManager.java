package util;

import main.Configuration;

import java.sql.*;

public enum HSQLDBManager {
    instance;

    private Connection connection;
    private boolean initialized = false;

    /**
     * The database has to be started at the start of the application. If this method will be called a second time it
     * will throw an exception.
     *
     * @throws DBInitializationException    The exception which will be thrown if the database should start a second time
     */
    public void startup() throws DBInitializationException {
        if(initialized) throw new DBInitializationException();
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String driverName = "jdbc:hsqldb:";
            String databaseURL = driverName + Configuration.instance.databaseFile;
            String username = "SA";
            String password = "";
            connection = DriverManager.getConnection(databaseURL, username, password);
            initialized = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * The database can be initiliazed by dropping all existing tables and creating them again.
     */
    public void init() throws DBInitializationException {
        dropTable();
        createTable();
    }

    /**
     * Data can be written to the database with calling one of the create or update methods. They will create the
     * statement and will call this method with it.
     *
     * @param sqlStatement  The statement which will be executed on the database
     */
    private synchronized void update(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sqlStatement);
            if (result == -1)
                System.out.println("error executing " + sqlStatement);
            statement.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    /**
     * This method will drop both tables without any check. So it should only be called if the data is not needed anymore.
     */
    public void dropTable() {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE HISTORY");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());

        sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE GENERATIONS");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    /**
     * Similar to dropping the tables this method can be called to get clean tables. This method will create the two
     * needed tables again.
     */
    public void createTable() {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("CREATE TABLE HISTORY ")
                .append(" ( ")
                .append("id BIGINT NOT NULL")
                .append(",")
                .append("generation BIGINT NOT NULL")
                .append(",")
                .append("route VARCHAR(")
                .append(255)
                .append(") NOT NULL")
                .append(",")
                .append("distance DOUBLE NOT NULL")
                .append(",")
                .append("PRIMARY KEY (id)")
                .append(" )");
        update(sqlStringBuilder.toString());

        sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("CREATE TABLE GENERATIONS ")
                .append(" ( ")
                .append("id BIGINT NOT NULL")
                .append(",")
                .append("route VARCHAR(")
                .append(255)
                .append(") NOT NULL")
                .append(",")
                .append("distance DOUBLE NOT NULL")
                .append(",")
                .append("PRIMARY KEY (id)")
                .append(" )");
        update(sqlStringBuilder.toString());
    }

    /**
     * This method will generate a SQL statement to insert the given data into the given table.
     *
     * @param table     The table the data will be inserted to
     * @param route     The route which will be saved
     * @param distance  The distance of the given route which will also be saved
     */
    public void updateTable(String table, String route, int distance){
        String sqlStringBuilder = "INSERT INTO " + table +
                " ( " + "id " + "," + "route" + "," +  "distance" + " ) " +
                " VALUES " +  " ( " +
                Configuration.instance.randomNumberGenerator.nextInt() +
                "," + route + "," + distance + " ) ";
        update(sqlStringBuilder);
    }

    /**
     * This method can be called to select data from a table. It has to called with a finished statement which can be executed.
     *
     * @param   sqlStatement    The SQL statement which will be excuted on the database
     * @return  ResultSet       The results of the statement
     */
    private synchronized ResultSet select(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sqlStatement);
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            return null;
        }
    }

    /**
     * This method takes a given table and genereates a SQL statement which gets every data from that table.
     * The statement will then be executed on the database.
     *
     * @param   table       The table on which a select will be executed
     * @return  ResultSet   The results of the statement
     */
    public ResultSet getTable(String table){
        String sqlStringBuilder = "SELECT * FROM " + table;
        return select(sqlStringBuilder);
    }

    /**
     *  The database has to be shutdown at the end of the application.
     */
    public void shutdown() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
            initialized = false;
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }
}