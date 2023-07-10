package it.polimi.geodesicwarehousemanager.controllers.users;

import com.google.gson.Gson;
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
@WebServlet(name = "GetUnAvDates", value = "/User/GetUnAvDates")
public class GetUnAvDates extends HttpServlet {

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
        ArrayList<Integer> accessories = new ArrayList<>();
        ArrayList<Integer> tools = new ArrayList<>();
        ArrayList<Timestamp[]> dates = new ArrayList<>();
        Location location = null;

        ItemDAO itemDAO = new ItemDAO(connection);

        if(dataJson != null && !dataJson.isEmpty()){
            try {
                Map<String, Object> data = new Gson().fromJson(dataJson, Map.class);
                location = Location.getLocationFromInt(Integer.parseInt(data.get("location").toString()));
                if(data.get("tools") != null) {
                    for (Object s : (ArrayList) data.get("tools")) {
                        tools.add((int) Double.parseDouble(s.toString()));
                    }
                }
                if(data.get("accessories") != null) {
                    for (Object s : (ArrayList) data.get("accessories")) {
                        accessories.add((int) Double.parseDouble(s.toString()));
                    }
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            dates = itemDAO.getUnavailablePeriods(tools, accessories);

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }

        //todo: complete this servlet.

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(dates);
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