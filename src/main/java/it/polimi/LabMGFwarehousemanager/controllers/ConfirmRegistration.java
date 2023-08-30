package it.polimi.LabMGFwarehousemanager.controllers;

import com.google.gson.Gson;
import it.polimi.LabMGFwarehousemanager.beans.UserBean;
import it.polimi.LabMGFwarehousemanager.daos.NotificationDAO;
import it.polimi.LabMGFwarehousemanager.daos.TokenDAO;
import it.polimi.LabMGFwarehousemanager.daos.UserDAO;
import it.polimi.LabMGFwarehousemanager.enums.NotificationType;
import it.polimi.LabMGFwarehousemanager.enums.UserRole;
import it.polimi.LabMGFwarehousemanager.utils.MailHandler;
import jakarta.mail.MessagingException;
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

@MultipartConfig
@WebServlet(name = "ConfirmRegistration", value = "/ConfirmRegistration")
public class ConfirmRegistration extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection();
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int verificationCode = 0;

        try {
            verificationCode = Integer.parseInt(request.getParameter("verificationCode"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid verification code format");
            return;
        }

        UserBean user = (UserBean) request.getSession().getAttribute("user");
        TokenDAO tokenDAO = new TokenDAO(connection);
        try {
            if (tokenDAO.checkRegistrationToken(user.getId(), verificationCode)) {

                tokenDAO.removeRegistrationToken(user.getId());

                MailHandler.sendMail(user.getEmail(),
                        "Welcome to Lab MGF Warehouse Manager",
                        "Registration completed successfully"
                );
                user.setRole(UserRole.NONE);

                UserDAO userDAO = new UserDAO(connection);
                userDAO.assignRole(user.getId(), user.getRole());

                MailHandler.sendMail(userDAO.getAdminsEmails(),
                        "New user registered to Lab MGF Warehouse Manager",
                        "A new user has registered to Lab MGF Warehouse Manager with the following data:\n" +
                        "Name: " + user.getName() + "\n" +
                        "Email: " + user.getEmail() + ".\n" +
                        "Login to the application to assign a role to the new User."
                );

                NotificationDAO notificationDAO = new NotificationDAO(connection);

                notificationDAO.addNotification(userDAO.getAdminsId(), NotificationType.NEW_USER, "A new user has registered to Lab MGF Warehouse Manager with the following data:\n" +
                        "Name: " + user.getName() + "\n" +
                        "Email: " + user.getEmail() + ".\n" +
                        "Go to Manage Users Roles page to assign a role to the new User.");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("Wrong verification code");
                return;
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Not possible to confirm registration");
            return;
        } catch (MessagingException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Not possible to send email");
            return;
        }
            UserBean userCopy = user.clone();
            response.setContentType("application/json");
            Gson gson = new Gson();
            String json = gson.toJson(userCopy);
            response.getWriter().println(json);
            response.setStatus(HttpServletResponse.SC_OK);

    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}