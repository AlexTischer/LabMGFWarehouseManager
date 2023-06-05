package it.polimi.geodesicwarehousemanager.controllers.superUsers;

import com.google.gson.Gson;
import it.polimi.geodesicwarehousemanager.daos.ItemDAO;
import it.polimi.geodesicwarehousemanager.enums.ItemType;
import jakarta.servlet.ServletException;
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
import java.util.Map;

import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;


@MultipartConfig
@WebServlet(name = "LinkItems", value = "/SuperUser/LinkItems")
public class LinkItems extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = 0;
        int type = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
            type = Integer.parseInt(request.getParameter("type"));
            if (ItemType.getItemTypeFromInt(type) == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
            return;
        }
        ItemDAO itemDAO = new ItemDAO(connection);
        Map<String, ArrayList<Object>> links = null;
        try {
            switch (type) {
                case 0:
                    links = itemDAO.getToolLinks(id);
                    break;
                case 1:
                    links = itemDAO.getAccessoryLinks(id);
                    break;
                case 2:
                    links = itemDAO.getDocumentLinks(id);
                    break;
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(links);
        response.getWriter().println(json);
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