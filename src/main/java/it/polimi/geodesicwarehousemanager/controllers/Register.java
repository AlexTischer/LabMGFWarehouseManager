package it.polimi.geodesicwarehousemanager.controllers;

import it.polimi.geodesicwarehousemanager.beans.UserBean;
import it.polimi.geodesicwarehousemanager.daos.TokenDAO;
import it.polimi.geodesicwarehousemanager.daos.UserDAO;
import it.polimi.geodesicwarehousemanager.enums.UserRole;
import it.polimi.geodesicwarehousemanager.utils.MailHandler;
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

@MultipartConfig
@WebServlet(name = "Register", value = "/Register")
public class Register extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    /**Checks if the user is already registered with the same email,
     * if not, creates a new user saves it into the database and sends an email to the user in order to confirm registration */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeat-password");
        UserDAO userDAO = new UserDAO(connection);

        if(name == null || surname == null || email == null || password == null || repeatPassword == null || name.isBlank() || surname.isBlank() || email.isBlank() || password.isBlank() || repeatPassword.isBlank()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Some fields are empty");
        }
        else if(!password.equals(repeatPassword)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Passwords do not match");
        }
        else if(userDAO.getUserByEmail(email) != null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Email already in use");
        }
        else{
            String hashedPassword = PasswordHandler.encryptPassword(password);
            try {
                UserBean user = new UserBean();
                user.setName(name);
                user.setSurname(surname);
                user.setEmail(email);
                user.setPassword(hashedPassword);
                user.setRole(UserRole.UNREGISTERED);
                response.setStatus(HttpServletResponse.SC_OK);
                userDAO.insertUser(user);
                user = userDAO.getUserByEmail(email);
                TokenDAO tokenDAO = new TokenDAO(connection);
                int code = tokenDAO.insertRegistrationToken(user);
                MailHandler.sendMail(email,
                        "Welcome to Geodesic Warehouse Manager",
                        "Insert the following code to complete registration: " + code
                );
            } catch (UnavailableException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Internal error creating the user or saving it into the database");
            }
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