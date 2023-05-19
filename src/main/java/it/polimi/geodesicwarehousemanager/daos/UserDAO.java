package it.polimi.geodesicwarehousemanager.daos;

import it.polimi.geodesicwarehousemanager.beans.UserBean;
import it.polimi.geodesicwarehousemanager.enums.UserRole;
import jakarta.servlet.UnavailableException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection){
        this.connection = connection;
    }

    private static final String INSERT_USER = "INSERT INTO users (name, surname, email, password, role) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String UPDATE_USER_ROLE = "UPDATE users SET role = ? WHERE id = ?";
    private static final String UPDATE_USER_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";
    private static final String SELECT_ADMINS_EMAILS = "SELECT email FROM users WHERE role = 3";

    /**Inserts User in DB.*/
    public void insertUser(UserBean user) throws UnavailableException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setInt(5, user.getRole().getValue());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new UnavailableException("Error with SQL");
        }
    }

    /**Selects a User based on its email*/
    public UserBean getUserByEmail(String email) throws UnavailableException {
        UserBean user = new UserBean();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = createUserBean(resultSet, true);
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            throw new UnavailableException("Error with SQL");
        }
        return user;
    }

    /**Selects a User based on its id*/
    public UserBean getUserById(int id) throws UnavailableException {
        UserBean user = new UserBean();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = createUserBean(resultSet, true);
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            throw new UnavailableException("Error with SQL");
        }
        return user;
    }

    /**Selects all Users in DB*/
    public ArrayList<UserBean> getAllUsers() throws UnavailableException {
        ArrayList<UserBean> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(createUserBean(resultSet, false));
            }
        } catch (SQLException e) {
            throw new UnavailableException("Error with SQL");
        }
        return users;
    }

    public void assignRole(int id, UserRole role) throws UnavailableException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_ROLE)) {
            preparedStatement.setInt(1, role.getValue());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new UnavailableException("Error with SQL");
        }
    }

    public ArrayList<String> getAdminsEmails() throws UnavailableException {
        ArrayList<String> adminsEmails = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ADMINS_EMAILS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                adminsEmails.add(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            throw new UnavailableException("Error with SQL");
        }
        return adminsEmails;
    }

    UserBean createUserBean(ResultSet resultSet, boolean addPassword) throws SQLException {
        UserBean user = new UserBean();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setEmail(resultSet.getString("email"));
        if(addPassword){
            user.setPassword(resultSet.getString("password"));
        }
        user.setRole(UserRole.getUserRoleFromInt(resultSet.getInt("role")));
        return user;
    }

    public void changeUserPassword(int id, String password) throws UnavailableException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_PASSWORD)) {
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new UnavailableException("Error with SQL");
        }
    }
}
