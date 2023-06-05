package it.polimi.geodesicwarehousemanager.controllers;

import com.google.gson.Gson;
import it.polimi.geodesicwarehousemanager.beans.ItemBean;
import it.polimi.geodesicwarehousemanager.daos.ItemDAO;
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

import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;


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
        Map<String, Timestamp> timeRange = null;
        String timeRangejson = request.getParameter("timeRange");
        ArrayList<ItemBean> accessories = null;

        if(timeRangejson != null && !timeRangejson.isEmpty()){
            try {
                timeRange = new Gson().fromJson(timeRangejson, Map.class);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
                return;
            }
            //TODO: get accessories by time range
        } else {
            ItemDAO itemDAO = new ItemDAO(connection);
            try {
                accessories = itemDAO.selectAllItemsByTypeAndLocation(1,-1);
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