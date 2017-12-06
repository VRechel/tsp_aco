package util;

import main.Configuration;

import java.sql.*;

public enum HSQLDBManager {
    instance;

    private Connection connection;
    private boolean initialized = false;

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

    public void init() throws DBInitializationException {
        if(initialized) throw new DBInitializationException();
        else{
            dropTable();
            createTable();
        }
    }

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

    public void updateTable(String table, String route, int distance){
        String sqlStringBuilder = "INSERT INTO " + table +
                " ( " + "id " + "," + "route" + "," +  "distance" + " ) " +
                " VALUES " +  " ( " +
                Configuration.instance.randomNumberGenerator.nextInt() +
                "," + route + "," + distance + " ) ";
        update(sqlStringBuilder);
    }

    private synchronized ResultSet select(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sqlStatement);
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            return null;
        }
    }

    public ResultSet getTable(String table){
        String sqlStringBuilder = "SELECT * FROM " + table;
        return select(sqlStringBuilder);
    }

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