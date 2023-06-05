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
@WebServlet(name = "GetAvTools", value = "/GetAvTools")
public class GetAvTools extends HttpServlet {

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
        String timeRangeJson = request.getParameter("timeRange");
        ArrayList<ItemBean> tools = null;

        if(timeRangeJson != null && !timeRangeJson.isEmpty()){
            try {
                timeRange = new Gson().fromJson(timeRangeJson, Map.class);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
                return;
            }
            //TODO: get tools by time range
        } else {
            ItemDAO itemDAO = new ItemDAO(connection);
            try {
                tools = itemDAO.selectAllItemsByTypeAndLocation(0,-1);
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
        String json = gson.toJson(tools);
        response.getWriter().write(json);
    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}