package it.polimi.LabMGFwarehousemanager.controllers.superUsers;

import it.polimi.LabMGFwarehousemanager.daos.ReportDAO;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.SQLException;

import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;


@MultipartConfig
@WebServlet(name = "UpdateReport", value = "/SuperUser/UpdateReport")
public class UpdateReport extends HttpServlet {

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
            int reportId = Integer.parseInt(request.getParameter("reportId"));
            int reportStatus = Integer.parseInt(request.getParameter("reportStatus"));
            ReportDAO reportDAO = new ReportDAO(connection);
            reportDAO.updateReport(reportId, reportStatus);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
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