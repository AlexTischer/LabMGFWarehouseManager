package it.polimi.LabMGFwarehousemanager.utils;

import jakarta.servlet.UnavailableException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class ConnectionHandler {
    /**Returns a connection to the database created using the parameters in the web.xml file*/
    public static Connection getConnection() throws UnavailableException {
        String driver_bk = "com.mysql.cj.jdbc.Driver";
        String url_bk = "jdbc:mysql://localhost:3306/labmgf?serverTimezone=UTC";
        String user_bk = "java";
        String password_bk = "password";

        String driver;
        String url;
        String user;
        String password;

        Connection connection = null;
        Map<String, String> dbConfig = FileHandler.getConfig(Constants.DB_CONFIG_FILE_PATH);
        if(dbConfig != null){
            driver = dbConfig.get("dbDriver");
            url = dbConfig.get("dbUrl");
            user = dbConfig.get("dbUser");
            password = dbConfig.get("dbPassword");

        } else {
            driver = driver_bk;
            url = url_bk;
            user = user_bk;
            password = password_bk;
        }

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        }
        return connection;
    }

    /**Closes the connection to the database*/
    public static void closeConnection(Connection connection) throws SQLException {
        if(connection!=null){
            connection.close();
        }
    }
}
