package it.polimi.geodesicwarehousemanager.controllers.admins;

import com.google.gson.Gson;
import it.polimi.geodesicwarehousemanager.daos.UserDAO;
import it.polimi.geodesicwarehousemanager.enums.UserRole;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;


@MultipartConfig
@WebServlet(name = "SetUsersRoles", value = "/Admin/SetUsersRoles")
public class SetUsersRoles extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> RolesMap;
        RolesMap = new Gson().fromJson(request.getReader(), Map.class);
        int userId;
        int userRole;
        for (Map.Entry entry : RolesMap.entrySet()) {
            try {
                userId = Integer.parseInt((String) entry.getKey());
                userRole = Integer.parseInt(entry.getValue().toString());
                if (UserRole.getUserRoleFromInt(userRole) == null) {
                    System.out.println("Invalid role");
                    throw new ClassCastException("Invalid role");
                } else {
                    UserDAO userDAO = new UserDAO(connection);
                    userDAO.assignRole(userId, UserRole.getUserRoleFromInt(userRole));
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Invalid userId");
                return;
            } catch (UnavailableException e) {
                response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                response.getWriter().println("Failure in database access while assigning roles");
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("Roles set successfully");
    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}