package it.polimi.LabMGFwarehousemanager.controllers;

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
import java.sql.Timestamp;
import java.util.ArrayList;

import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;

@MultipartConfig
@WebServlet(name = "GetAvAccessories", value = "/GetAvAccessories")
public class GetAvAccessories extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<ItemBean> accessories = null;
        String idString = request.getParameter("id");
        String startString = request.getParameter("start");
        String endString = request.getParameter("end");
        int toolId = -1;
        Timestamp start = null;
        Timestamp end = null;

        ItemDAO itemDAO = new ItemDAO(connection);

        if (startString != null && endString != null) {
            try {
                startString = startString.concat(" 00:00:00.1");
                start = Timestamp.valueOf(startString);
                endString = endString.concat(" 23:59:59.0");
                end = Timestamp.valueOf(endString);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
                return;
            }
        }
        if(idString != null) {
            try {
                toolId = Integer.parseInt(idString);
                accessories = itemDAO.getAvailableAccessoriesByToolAndTimeRange(toolId, start, end);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
                return;
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
                return;
            }
        } else {
            try {
                accessories = itemDAO.getAccessories();
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
                return;
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(accessories);
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