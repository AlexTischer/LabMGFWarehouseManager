package it.polimi.geodesicwarehousemanager.controllers.users;

import com.google.gson.Gson;
import it.polimi.geodesicwarehousemanager.beans.NotificationBean;
import it.polimi.geodesicwarehousemanager.beans.UserBean;
import it.polimi.geodesicwarehousemanager.daos.ItemDAO;
import it.polimi.geodesicwarehousemanager.daos.NotificationDAO;
import it.polimi.geodesicwarehousemanager.enums.Location;
import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

@MultipartConfig
@WebServlet(name = "GetNotifications", value = "/User/GetNotifications")
public class GetNotifications extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UserBean user = (UserBean) request.getSession().getAttribute("user");
            int userId = user.getId();
            NotificationDAO notificationDAO = new NotificationDAO(connection);
            ArrayList<NotificationBean> notifications = notificationDAO.getNotifications(userId);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(notifications));
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error in database connection");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error in session");
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