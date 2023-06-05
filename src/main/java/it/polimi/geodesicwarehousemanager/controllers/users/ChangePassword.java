package it.polimi.geodesicwarehousemanager.controllers.users;

import it.polimi.geodesicwarehousemanager.beans.UserBean;
import it.polimi.geodesicwarehousemanager.daos.TokenDAO;
import it.polimi.geodesicwarehousemanager.daos.UserDAO;
import it.polimi.geodesicwarehousemanager.utils.PasswordHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;

//todo: /User to apply filter
@MultipartConfig
@WebServlet(name = "ChangePassword", value = "/ChangePassword")
public class ChangePassword extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        String oldPassword = request.getParameter("old-password");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeat-password");
        String changePasswordToken = request.getParameter("changePasswordToken");
        UserDAO userDAO = new UserDAO(connection);
        TokenDAO tokenDAO = new TokenDAO(connection);

        if (((oldPassword == null || oldPassword.isBlank()) && (changePasswordToken == null || changePasswordToken.isBlank()))
                || password == null || repeatPassword == null || password.isBlank() || repeatPassword.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Some fields are empty");
        } else if(oldPassword != null && !oldPassword.isBlank() && !PasswordHandler.checkPassword(oldPassword, user.getPassword())){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Old password is wrong");
        } else if(!password.equals(repeatPassword)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Passwords do not match");
        } else if (changePasswordToken != null && !changePasswordToken.isBlank() && tokenDAO.getUserIdByChangePwdToken(changePasswordToken) != user.getId()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid token");
        } else {
            String hashedPassword = PasswordHandler.encryptPassword(password);
            try {
                userDAO.changeUserPassword(user.getId(), hashedPassword);
                user.setPassword(hashedPassword);
                tokenDAO.removeChangePwdToken(user.getId());
            } catch (UnavailableException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Internal error creating the user or saving it into the database");
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Password changed successfully");
        }

    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}