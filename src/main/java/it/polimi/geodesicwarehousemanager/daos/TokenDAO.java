package it.polimi.geodesicwarehousemanager.daos;

import it.polimi.geodesicwarehousemanager.beans.UserBean;
import it.polimi.geodesicwarehousemanager.utils.TokenHandler;
import jakarta.servlet.UnavailableException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class TokenDAO {
    private final Connection connection;

    private static final String SELECT_REMEMBERME_TOKEN_BY_ID = "SELECT * FROM rememberme_tokens WHERE id = ?";
    private static final String REMOVE_REMEMBERME_TOKEN_BY_ID = "DELETE FROM rememberme_tokens WHERE id = ?";
    private static final String INSERT_REMEMBERME_TOKEN = "INSERT INTO rememberme_tokens (id, token) VALUES (?, ?)";
    private static final String SELECT_ID_BY_REMEMBERME_TOKEN = "SELECT id FROM rememberme_tokens WHERE token = ?";

    private static final String SELECT_REGISTRATION_TOKEN_BY_ID = "SELECT * FROM registration_tokens WHERE id = ?";
    private static final String REMOVE_REGISTRATION_TOKEN_BY_ID = "DELETE FROM registration_tokens WHERE id = ?";
    private static final String INSERT_REGISTRATTION_TOKEN = "INSERT INTO registration_tokens (id, token) VALUES (?, ?)";

    private static final String SELECT_ID_BY_CHANGEPWD_TOKEN = "SELECT id FROM changepwd_tokens WHERE token = ?";
    private static final String REMOVE_CHANGEPWD_TOKEN_BY_ID = "DELETE FROM changepwd_tokens WHERE id = ?";
    private static final String INSERT_CHANGEPWD_TOKEN = "INSERT INTO changepwd_tokens (id, token) VALUES (?, ?)";

    public TokenDAO(Connection connection){
        this.connection = connection;
    }
    public String insertRememberMeToken(UserBean user) throws UnavailableException {
        int id = user.getId();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REMEMBERME_TOKEN_BY_ID);
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeQuery().next()){
                preparedStatement = connection.prepareStatement(REMOVE_REMEMBERME_TOKEN_BY_ID);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                return insertRememberMeToken(id);
            }
            else{
                return insertRememberMeToken(id);
            }
        }
        catch (SQLException e){
            throw new UnavailableException("Error with SQL");
        }
    }
    private String insertRememberMeToken(int id) throws UnavailableException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_REMEMBERME_TOKEN);
            preparedStatement.setInt(1, id);
            String token = TokenHandler.generateToken();
            preparedStatement.setString(2, token);
            preparedStatement.executeUpdate();
            return token;
        }
        catch (SQLException e){
            throw new UnavailableException("Error with SQL");
        }
    }
    public int getUserIdByRememberMeToken(String value) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_BY_REMEMBERME_TOKEN);
            preparedStatement.setString(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("id");
            }
            else{
                return -1;
            }
        }
        catch (SQLException e){
            throw new RuntimeException("Error with SQL");
        }

    }



    public int insertRegistrationToken(UserBean user) throws UnavailableException {
        int id = user.getId();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REGISTRATION_TOKEN_BY_ID);
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeQuery().next()){
                removeRegistrationToken(id);
                return insertRegistrationToken(id);
            }
            else{
                return insertRegistrationToken(id);
            }
        }
        catch (SQLException e){
            throw new UnavailableException("Error with SQL");
        }
    }
    private int insertRegistrationToken(int id) throws UnavailableException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_REGISTRATTION_TOKEN);
            preparedStatement.setInt(1, id);
            Random random = new Random();
            int token = random.nextInt(8999) + 1000;
            preparedStatement.setInt(2, token);
            preparedStatement.executeUpdate();
            return token;
        }
        catch (SQLException e){
            throw new UnavailableException("Error with SQL");
        }
    }
    public boolean checkRegistrationToken(int id, int token) throws UnavailableException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REGISTRATION_TOKEN_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return token == resultSet.getInt("token");
            }
            else{
                return false;
            }
        }
        catch (SQLException e){
            throw new UnavailableException("Error with SQL");
        }
    }
    public void removeRegistrationToken(int id) throws UnavailableException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_REGISTRATION_TOKEN_BY_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            throw new UnavailableException("Error with SQL");
        }
    }



    public int getUserIdByChangePwdToken(String value) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_BY_CHANGEPWD_TOKEN);
            preparedStatement.setString(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("id");
            }
            else{
                return -1;
            }
        }
        catch (SQLException e){
            throw new RuntimeException("Error with SQL");
        }

    }
    public String insertChangePwdToken(UserBean user) throws UnavailableException {
        int id = user.getId();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_BY_CHANGEPWD_TOKEN);
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeQuery().next()){
                removeChangePwdToken(id);
                return insertChangePwdToken(id);
            }
            else{
                return insertChangePwdToken(id);
            }
        }
        catch (SQLException e){
            throw new UnavailableException("Error with SQL");
        }
    }
    private String insertChangePwdToken(int id) throws UnavailableException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CHANGEPWD_TOKEN);
            preparedStatement.setInt(1, id);
            String token = TokenHandler.generateToken();
            preparedStatement.setString(2, token);
            preparedStatement.executeUpdate();
            return token;
        }
        catch (SQLException e){
            throw new UnavailableException("Error with SQL");
        }
    }
    public void removeChangePwdToken(int id) throws UnavailableException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_CHANGEPWD_TOKEN_BY_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new UnavailableException("Error with SQL");
        }
    }

}
