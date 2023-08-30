package it.polimi.LabMGFwarehousemanager.controllers.users;

import com.google.gson.Gson;
import it.polimi.LabMGFwarehousemanager.beans.RequestBean;
import it.polimi.LabMGFwarehousemanager.beans.UserBean;
import it.polimi.LabMGFwarehousemanager.daos.RequestDAO;
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
@WebServlet(name = "GetUserRequests", value = "/User/GetUserRequests")
public class GetUserRequests extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection();
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        ArrayList<RequestBean> requests;
        RequestDAO requestDAO = new RequestDAO(connection);
        try {
            requests = requestDAO.getRequestsByUserId(user.getId());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(requests);
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