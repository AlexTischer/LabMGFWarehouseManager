package it.polimi.LabMGFwarehousemanager.controllers.superUsers;

import it.polimi.LabMGFwarehousemanager.daos.ItemDAO;
import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.SQLException;


@MultipartConfig
@WebServlet(name = "RemoveItem", value = "/SuperUser/RemoveItem")
public class RemoveItem extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection();
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
            return;
        }
        ItemDAO itemDAO = new ItemDAO(connection);
        try{
            itemDAO.removeItemById(id);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
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