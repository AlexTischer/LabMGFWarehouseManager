package it.polimi.LabMGFwarehousemanager.controllers.superUsers;

import com.google.gson.Gson;
import it.polimi.LabMGFwarehousemanager.beans.DocumentBean;
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
import java.util.ArrayList;

import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;


@MultipartConfig
@WebServlet(name = "GetDocuments", value = "/SuperUser/GetDocuments")
public class GetDocuments extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ItemDAO itemDAO = new ItemDAO(connection);
        ArrayList<DocumentBean> documents;
        try {
            documents = itemDAO.getAllDocuments();
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        Gson gson = new Gson();
        String json = gson.toJson(documents);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}