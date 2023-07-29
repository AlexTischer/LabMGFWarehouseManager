package it.polimi.geodesicwarehousemanager.controllers.users;

import com.google.gson.Gson;
import it.polimi.geodesicwarehousemanager.beans.UserBean;
import it.polimi.geodesicwarehousemanager.daos.RequestDAO;
import jakarta.servlet.ServletException;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;

@MultipartConfig
@WebServlet(name = "CreateRequest", value = "/User/CreateRequest")
public class CreateRequest extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        UserBean user = (UserBean) request.getSession().getAttribute("user");
        int userId = user.getId();

        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String dataJson = sb.toString();

        Map<String, Object> requestData;

        if(dataJson.isEmpty()){
            System.out.println("dataJson is null or empty");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            try{
                requestData = new Gson().fromJson(dataJson, Map.class);
            } catch (Exception e){
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            RequestDAO requestDAO = new RequestDAO(connection);
            String startString = (String) requestData.get("start");
            String endString = (String) requestData.get("end");
            startString = startString.concat(" 00:00:00.1");
            Timestamp start = Timestamp.valueOf(startString);
            endString = (LocalDate.parse(endString, DateTimeFormatter.ISO_DATE).minusDays(1)).toString();
            endString = endString.concat(" 23:59:59.0");
            Timestamp end = Timestamp.valueOf(endString);
            ArrayList<Double> doubleItems = (ArrayList<Double>) requestData.get("items");
            ArrayList<Integer> items = new ArrayList<>();
            for (Double doubleItem : doubleItems) {
                items.add(doubleItem.intValue());
            }
            String reason = (String) requestData.get("reason");

            try {
                requestDAO.createRequest(userId, start, end, items, reason);
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Report created successfully");

        }



    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}