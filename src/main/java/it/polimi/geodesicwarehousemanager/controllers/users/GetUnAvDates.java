package it.polimi.geodesicwarehousemanager.controllers.users;

import com.google.gson.Gson;
import it.polimi.geodesicwarehousemanager.daos.ItemDAO;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String dataJson = sb.toString();

        ArrayList<Double> doubleItems;
        ArrayList<Map<String, String>> dates;

        ItemDAO itemDAO = new ItemDAO(connection);

        if(!dataJson.isEmpty()){
            try {
                doubleItems = new Gson().fromJson(dataJson, ArrayList.class);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
                return;
            }
        } else {
            System.out.println("dataJson is null or empty");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if(!doubleItems.isEmpty()) {
            try {
                ArrayList<Integer> items = new ArrayList<>();
                for (Double d : doubleItems) {
                    items.add(d.intValue());
                }
                dates = itemDAO.getUnavailablePeriods(items);

            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
                return;
            }
        } else {
            dates = new ArrayList<>();
        }

        response.setStatus(HttpServletResponse.SC_OK);
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