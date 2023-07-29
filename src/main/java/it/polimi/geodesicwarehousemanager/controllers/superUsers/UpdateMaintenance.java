package it.polimi.geodesicwarehousemanager.controllers.superUsers;

import it.polimi.geodesicwarehousemanager.daos.ItemDAO;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;

import static java.time.LocalTime.now;


@MultipartConfig
@WebServlet(name = "UpdateMaintenance", value = "/SuperUser/UpdateMaintenance")
public class UpdateMaintenance extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println(now() + " - UpdateMaintenance");
            int itemId = Integer.parseInt(request.getParameter("id"));
            int maintenanceStatus = Integer.parseInt(request.getParameter("status"));
            if (maintenanceStatus < 0 || maintenanceStatus > 2) {
                throw new IllegalArgumentException();
            }
            ItemDAO itemDAO = new ItemDAO(connection);
            if (maintenanceStatus != 0) {
                String reason = request.getParameter("reason");
                Timestamp start = Timestamp.valueOf(LocalDate.parse(request.getParameter("start")).atStartOfDay());
                Timestamp end = Timestamp.valueOf(LocalDate.parse(request.getParameter("end")).atStartOfDay());
                itemDAO.updateMaintenance(itemId, maintenanceStatus, reason, start, end);
            } else {
                itemDAO.updateMaintenance(itemId, maintenanceStatus, null, null, null);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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