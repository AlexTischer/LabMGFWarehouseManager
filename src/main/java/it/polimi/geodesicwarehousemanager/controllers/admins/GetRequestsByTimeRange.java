package it.polimi.geodesicwarehousemanager.controllers.admins;

import com.google.gson.Gson;
import it.polimi.geodesicwarehousemanager.beans.RequestBean;
import it.polimi.geodesicwarehousemanager.daos.RequestDAO;
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

import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;


@MultipartConfig
@WebServlet(name = "GetRequestsByTimeRange", value = "/Admin/GetRequestsByTimeRange")
public class GetRequestsByTimeRange extends HttpServlet {
    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String startString = request.getParameter("start");
        String endString = request.getParameter("end");
        Timestamp start;
        Timestamp end;
        if (startString == null || endString == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            try {
                startString = startString.substring(0, 10);
                startString = startString.concat(" 00:00:00.1");
                start = Timestamp.valueOf(startString);
                endString = endString.substring(0, 10);
                endString = endString.concat(" 23:59:59.0");
                end = Timestamp.valueOf(endString);

                RequestDAO requestDAO = new RequestDAO(connection);
                ArrayList<RequestBean> requests = requestDAO.getRequestsByTimeRange(start, end);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(new Gson().toJson(requests));
                response.setStatus(HttpServletResponse.SC_OK);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
                return;
            }
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