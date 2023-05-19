package it.polimi.geodesicwarehousemanager.controllers;

import com.google.gson.Gson;
import it.polimi.geodesicwarehousemanager.beans.UserBean;
import it.polimi.geodesicwarehousemanager.daos.TokenDAO;
import it.polimi.geodesicwarehousemanager.daos.UserDAO;
import it.polimi.geodesicwarehousemanager.enums.UserRole;
import it.polimi.geodesicwarehousemanager.utils.MailHandler;
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
@WebServlet(name = "ConfirmRegistration", value = "/ConfirmRegistration")
public class ConfirmRegistration extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int verificationCode = 0;

        try{
            verificationCode = Integer.parseInt(request.getParameter("verificationCode"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid verification code format");
            return;
        }

        UserBean user = (UserBean) request.getSession().getAttribute("user");
        TokenDAO tokenDAO = new TokenDAO(connection);
        if(tokenDAO.checkRegistrationToken(user.getId(), verificationCode)){

            tokenDAO.removeRegistrationToken(user.getId());

            MailHandler.sendMail(user.getEmail(),
                    "Welcome to Geodesic Warehouse Manager",
                    "Registration completed successfully"
            );
            user.setRole(UserRole.NONE);

            UserDAO userDAO = new UserDAO(connection);
            userDAO.assignRole(user.getId(), user.getRole());

            MailHandler.sendMail(userDAO.getAdminsEmails(),
                    "New user registered to Geodesic Warehouse Manager",
                    "A new user has registered to Geodesic Warehouse Manager with the following data:\n" +
                            "Name: " + user.getName() + "\n" +
                            "Email: " + user.getEmail() + ".\n" +
                    "Login to the application to assign a role to the new User."
            );
            UserBean userCopy = user.clone();
            response.setContentType("application/json");
            Gson gson = new Gson();
            String json = gson.toJson(userCopy);
            response.getWriter().println(json);
            response.setStatus(HttpServletResponse.SC_OK);

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Wrong verification code");
            return;
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