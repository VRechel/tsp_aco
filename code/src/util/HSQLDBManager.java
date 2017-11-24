package util;

import mainTest.Configuration;

import java.sql.*;

public enum HSQLDBManager {
    instance;

    private Connection connection;
    private String driverName = "jdbc:hsqldb:";
    private String username = "sa";
    private String password = "";
    private boolean initialized = false;

    public void startup() throws DBInitializationException {
        if(initialized) throw new DBInitializationException();
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String databaseURL = driverName + Configuration.instance.databaseFile;
            connection = DriverManager.getConnection(databaseURL, username, password);
            initialized = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void init() throws DBInitializationException {
        if(initialized) throw new DBInitializationException();
        dropTable();
        createTable();
    }

    public synchronized void update(String sqlStatement) {
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

    public synchronized ResultSet query(String sqlStatement) {
        Statement st;
        ResultSet rs = null;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sqlStatement);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void dropTable() {
        System.out.println("--- dropTable");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE s01");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
        sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE s02");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTable() {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("CREATE TABLE s01 ").append(" ( ");
        sqlStringBuilder.append("id BIGINT NOT NULL").append(",");
        sqlStringBuilder.append("test VARCHAR(20) NOT NULL").append(",");
        sqlStringBuilder.append("PRIMARY KEY (id)");
        sqlStringBuilder.append(" )");
        update(sqlStringBuilder.toString());

        sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("CREATE TABLE s02 ").append(" ( ");
        sqlStringBuilder.append("id BIGINT NOT NULL").append(",");
        sqlStringBuilder.append("test VARCHAR(20) NOT NULL").append(",");
        sqlStringBuilder.append("PRIMARY KEY (id)");
        sqlStringBuilder.append(" )");
        update(sqlStringBuilder.toString());
    }

    public String buildSQLStatement(long id, String test) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            stringBuilder.append("INSERT INTO s01 (id,test) VALUES (");
            stringBuilder.append(i).append(",");
            stringBuilder.append("'").append((int) (Math.random()*10)).append("'");
            stringBuilder.append(")");
            stringBuilder.append("INSERT INTO s02 (id,test) VALUES (");
            stringBuilder.append(i).append(",");
            stringBuilder.append("'").append((int) (Math.random()*20)).append("'");
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }

    public void insert(String test) {
        update(buildSQLStatement(System.nanoTime(), test));
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