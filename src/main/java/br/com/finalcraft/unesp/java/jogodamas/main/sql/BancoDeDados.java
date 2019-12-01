package br.com.finalcraft.unesp.java.jogodamas.main.sql;


import br.com.finalcraft.unesp.java.jogodamas.common.SmartLogger;
import br.com.finalcraft.unesp.java.jogodamas.main.sql.data.SQLBattleLog;
import br.com.finalcraft.unesp.java.jogodamas.main.sql.data.SQLPlayer;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BancoDeDados {

    private static String serverName = "localhost";
    private static int serverPort = 3306;
    private static String userName = "root";
    private static String userPassword = "";
    private static String databaseName = "jogodamas";

    private static Connection connection = null;

    public static void myOwnDataLocal(){
        setUpProperties("localhost",3306,"root","","");
    }

    public static void setUpProperties(String serverName, int serverPort, String userName, String userPassword, String databaseName) {
        BancoDeDados.serverName = serverName;
        BancoDeDados.serverPort = serverPort;
        BancoDeDados.userName = userName;
        BancoDeDados.userPassword = userPassword;
        BancoDeDados.databaseName = databaseName.isEmpty() ? "jogodamas" : databaseName.toLowerCase();
        if (connection!=null) connection = null;
    }

    public static Connection getConnection() throws SQLException{
        if (connection != null && connection.isValid(0)){
            return connection;
        }
        connection = DriverManager.getConnection("jdbc:mysql://" + serverName + ":" + serverPort + "/?user=" + userName + "&password=" + userPassword);
        checkForDatabaseCreation();
        return connection;
    }

    public static boolean tableExists(String tableName) throws SQLException{
        DatabaseMetaData dbm = getConnection().getMetaData();
        ResultSet tables = dbm.getTables(null, null, tableName, null);
        return tables.next();
    }

    public static boolean databaseExists(String databaseName) throws SQLException{
        ResultSet rs = getConnection().createStatement().executeQuery("SHOW DATABASES;");
        while (rs.next()){
            if (rs.getString("SCHEMA_NAME").equals(databaseName)){
                return true;
            }
        }
        return false;
    }

    private static BufferedReader getBufferedAsset(String assetPath){
        InputStream inputStream = BancoDeDados.class.getResourceAsStream(assetPath);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    public static boolean checkForDatabaseCreation(){
        try {
            if (!databaseExists(databaseName)) {
                getConnection().createStatement().executeQuery("CREATE DATABASE " + databaseName + ";");
            }

            getConnection().createStatement().executeQuery("USE " + databaseName + ";");

            if (!tableExists("players")){
                ScriptRunner scriptRunner = new ScriptRunner(getConnection());
                Reader bufCreateDatabase = getBufferedAsset("/assets/sql/create_tables.sql");
                scriptRunner.runScript(bufCreateDatabase);
            }

            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static List<SQLPlayer> getAllPlayers() throws SQLException{
        List<SQLBattleLog> allBattleLogs = getAllBattleLogs();
        ResultSet rs = getConnection().createStatement().executeQuery("SELECT * FROM players;");
        List<SQLPlayer> sqlPlayerList = new ArrayList<>();
        while (rs.next()){
            sqlPlayerList.add(new SQLPlayer(rs,allBattleLogs));
        }
        return sqlPlayerList;
    }

    public static List<SQLBattleLog> getAllBattleLogs() throws SQLException{
        ResultSet rs = getConnection().createStatement().executeQuery("SELECT * FROM battlelogs;");
        List<SQLBattleLog> sqlBattleLogs = new ArrayList<>();
        while (rs.next()){
            sqlBattleLogs.add(new SQLBattleLog(rs));
        }
        return sqlBattleLogs;
    }

    public static void computarBatalha(String playerOne, String playerTwo, int damasPlayerOne, int damasPlayerTwo, String winner){
        try {
            String sqlCommand = "insert into battlelogs values('" + playerOne + "', '" + playerTwo + "'," + damasPlayerOne + "," + damasPlayerTwo + ",'" + winner + "', " + System.currentTimeMillis() + ");";
            SmartLogger.info("Executando: \n" + sqlCommand);
            getConnection().createStatement().executeQuery(sqlCommand);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    public static void main(String[] args) throws Exception {

        myOwnDataLocal();

        ResultSet rs = getConnection().createStatement().executeQuery("SHOW DATABASES;");
        System.out.println("Connection Accept");

        ResultSetMetaData rsMetaData = rs.getMetaData();

        for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
            String columName = rsMetaData.getColumnName(i);
            if (columName.isEmpty()){
                System.out.println("IsEmpty");
            }
            System.out.println(columName);
            rs.beforeFirst();
            while (rs.next()){
                System.out.println(rs.getString(columName));
            }
        }

        checkForDatabaseCreation();


        System.out.println("All Players:");
        for (SQLPlayer sqlPlayer : getAllPlayers()) {
            System.out.println(sqlPlayer.toString() + "\n");
        }

        for (SQLBattleLog sqlBattleLog : getAllBattleLogs()) {
            System.out.println(sqlBattleLog.toString() + "\n");
        }

    }
    */
}
