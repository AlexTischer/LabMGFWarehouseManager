package it.polimi.geodesicwarehousemanager;

import it.polimi.geodesicwarehousemanager.utils.MailHandler;
import jakarta.servlet.UnavailableException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.LocalTime.now;

public class MainTest {
    public static void main(String[] args) throws SQLException {

        Connection connection = null;
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/database?serverTimezone=UTC";
            String user = "java";
            String password = "password";
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't load database driver");
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't get db connection");
        }

        Timestamp testStart = Timestamp.valueOf(LocalDate.parse("2023-07-27").atStartOfDay());

        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO maintenance (item_id, reason, start, end) VALUES (52, 'test', ?, NOW())");
        preparedStatement.setTimestamp(1, testStart);
        //preparedStatement.executeUpdate();

        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM maintenance WHERE item_id = 54");
        ResultSet resultSet = preparedStatement1.executeQuery();

        while (resultSet.next()) {
            System.out.println("start: " + resultSet.getTimestamp("start"));
            System.out.println("end: " + resultSet.getTimestamp("end"));
        }

    }
}
