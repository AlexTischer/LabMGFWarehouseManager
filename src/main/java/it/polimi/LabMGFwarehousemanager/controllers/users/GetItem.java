package it.polimi.LabMGFwarehousemanager.controllers.users;

import com.google.gson.Gson;
import it.polimi.LabMGFwarehousemanager.beans.ItemBean;
import it.polimi.LabMGFwarehousemanager.daos.ItemDAO;
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
@WebServlet(name = "GetItem", value = "/User/GetItem")
public class GetItem extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String idString = request.getParameter("id");
        int id;

        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
            return;
        }

        ItemDAO itemDAO = new ItemDAO(connection);

        try {
            ItemBean item = itemDAO.getItemById(id);
            if (item == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(new Gson().toJson(item));
        } catch (SQLException | IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
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