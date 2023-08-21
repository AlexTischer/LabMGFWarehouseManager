package it.polimi.LabMGFwarehousemanager.controllers;

import it.polimi.LabMGFwarehousemanager.beans.UserBean;
import it.polimi.LabMGFwarehousemanager.daos.TokenDAO;
import it.polimi.LabMGFwarehousemanager.daos.UserDAO;
import it.polimi.LabMGFwarehousemanager.utils.MailHandler;
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

import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;

import static it.polimi.LabMGFwarehousemanager.utils.Constants.SERVER_URL;

@MultipartConfig
@WebServlet(name = "RecoverPassword", value = "/RecoverPassword")
public class RecoverPassword extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        if (email == null || email.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Email is empty");
            return;
        } else {
            UserDAO userDAO = new UserDAO(connection);
            UserBean user = userDAO.getUserByEmail(email);
            if (user != null) {
                TokenDAO tokenDAO = new TokenDAO(connection);
                String token = tokenDAO.insertChangePwdToken(user);
                MailHandler.sendMail(email,
                        "Recover password",
                        "Click on the following link to recover your password: " + SERVER_URL + "/ChangePassword?changePasswordToken=" + token);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("An email has been sent to " + email + " with instructions to recover your password");
                return;
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Email not found");
                return;
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