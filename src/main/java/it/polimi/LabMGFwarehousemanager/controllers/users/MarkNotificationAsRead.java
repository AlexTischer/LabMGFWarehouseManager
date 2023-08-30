package it.polimi.LabMGFwarehousemanager.controllers.users;

import it.polimi.LabMGFwarehousemanager.daos.NotificationDAO;
import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@MultipartConfig
@WebServlet(name = "MarkNotificationAsRead", value = "/User/MarkNotificationAsRead")
public class MarkNotificationAsRead extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection();
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int notificationId = Integer.parseInt(request.getParameter("id"));
            NotificationDAO notificationDAO = new NotificationDAO(connection);
            notificationDAO.markNotificationAsRead(notificationId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid notification id");
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error, retry later");
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