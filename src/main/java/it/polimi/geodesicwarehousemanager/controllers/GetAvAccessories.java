package it.polimi.geodesicwarehousemanager.controllers;

import com.google.gson.Gson;
import it.polimi.geodesicwarehousemanager.beans.ItemBean;
import it.polimi.geodesicwarehousemanager.daos.ItemDAO;
import it.polimi.geodesicwarehousemanager.enums.Location;
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
        String dataJson = request.getParameter("dataJson");
        ArrayList<ItemBean> accessories = null;
        Timestamp start = null;
        Timestamp end = null;
        Location location = null;

        ItemDAO itemDAO = new ItemDAO(connection);

        if(dataJson != null && !dataJson.isEmpty()){
            try {
                Map<String, Object> data = new Gson().fromJson(dataJson, Map.class);
                start = Timestamp.valueOf(data.get("start").toString());
                end = Timestamp.valueOf(data.get("end").toString());
                location = Location.getLocationFromInt(Integer.parseInt(data.get("location").toString()));
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
                return;
            }
            try {
                accessories = itemDAO.selectAvailableItemsByTipeLocationAndTimeRange(1,location==null ? -1 : location.getValue(),start, end);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
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