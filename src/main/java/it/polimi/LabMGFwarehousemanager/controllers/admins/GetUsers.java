package it.polimi.LabMGFwarehousemanager.controllers.admins;

import com.google.gson.Gson;
import it.polimi.LabMGFwarehousemanager.beans.UserBean;
import it.polimi.LabMGFwarehousemanager.daos.UserDAO;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;


@MultipartConfig
@WebServlet(name = "GetUsers", value = "/Admin/GetUsers")
public class GetUsers extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection();
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnavailableException, IOException {
        UserDAO userDAO = new UserDAO(connection);
        ArrayList<UserBean> users;
        try {
            users = userDAO.getAllUsers();
        } catch (UnavailableException e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            response.getWriter().println("Failure in database access while retrieving users");
            return;
        }
        users.removeIf(user -> user.getId() == ( (UserBean) request.getSession().getAttribute("user")).getId());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(users);
        response.getWriter().println(json);
    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}