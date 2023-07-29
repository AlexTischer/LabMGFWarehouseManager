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
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        ArrayList<ItemBean> tools;
        String startString = request.getParameter("start");
        String endString = request.getParameter("end");
        Timestamp start = null;
        Timestamp end = null;

        ItemDAO itemDAO = new ItemDAO(connection);

        if(startString != null && endString != null) {
            try {
                startString = startString.concat(" 00:00:00.1");
                start = Timestamp.valueOf(startString);
                endString = (LocalDate.parse(endString, DateTimeFormatter.ISO_DATE).minusDays(1)).toString();
                endString = endString.concat(" 23:59:59.0");
                end = Timestamp.valueOf(endString);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
                return;
            }
        }

        try {
            tools = itemDAO.getAvailableToolsByTimeRange(start, end);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(tools);
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