package it.polimi.LabMGFwarehousemanager.controllers.users;

import com.google.gson.Gson;
import it.polimi.LabMGFwarehousemanager.beans.UserBean;
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
@WebServlet(name = "GetMyUser", value = "/User/GetMyUser")
public class GetMyUser extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection();
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        response.setContentType("application/json");
        try {
            response.getWriter().println(new Gson().toJson(user));
        } catch (IOException e) {
            e.printStackTrace();
        }
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