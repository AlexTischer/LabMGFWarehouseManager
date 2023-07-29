package it.polimi.geodesicwarehousemanager.controllers.users;

import it.polimi.geodesicwarehousemanager.beans.UserBean;
import it.polimi.geodesicwarehousemanager.daos.ItemDAO;
import it.polimi.geodesicwarehousemanager.daos.NotificationDAO;
import it.polimi.geodesicwarehousemanager.daos.ReportDAO;
import it.polimi.geodesicwarehousemanager.daos.UserDAO;
import it.polimi.geodesicwarehousemanager.enums.NotificationType;
import it.polimi.geodesicwarehousemanager.utils.MailHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;


@MultipartConfig
@WebServlet(name = "CreateReport", value = "/User/CreateReport")
public class CreateReport extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String reportSubject;
        String reportBody;
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        int reportUser = user.getId();
        ArrayList<Integer> items;

        try {
            reportSubject = request.getParameter("reportSubject");
            reportBody = request.getParameter("reportBody");
            items = (ArrayList<Integer>) Arrays.stream(request.getParameter("items").split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());


            if (reportSubject == null || reportSubject.isBlank() || reportBody == null || reportBody.isBlank()) {
                throw new Exception();
            }
            if (items == null || items.isEmpty()) {
                throw new Exception();
            }

            ReportDAO reportDAO = new ReportDAO(connection);
            reportDAO.createReport(reportUser, reportSubject, reportBody, items);

        } catch (SQLException e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error, retry later");
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid request");
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("Report created successfully");
        System.out.println("Report created successfully. Report subject: " + reportSubject + ", report body: " + reportBody + ", items: " + items);

        UserDAO userDAO = new UserDAO(connection);
        ItemDAO itemDAO = new ItemDAO(connection);
        String itemsNamesString = "";
        try {
            for(Integer item : items){
                itemsNamesString += "    " + itemDAO.getItemById(item).getName() + "\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error, retry later");
            return;
        }

        MailHandler.sendMail(userDAO.getAdminsEmails(),
                "New report sent to Geodesic Warehouse Manager",
                user.getName() + " has sent a new report:\n" +
                        "Reported Items: " + "\n" + itemsNamesString + "\n" +
                        "Subject: " + reportSubject + ".\n" +
                        "Body: " + reportBody + "\n" +
                        "Please check the report section of the website to take actions."
        );

        NotificationDAO notificationDAO = new NotificationDAO(connection);
        try {
            notificationDAO.addNotification(userDAO.getAdminsId(), NotificationType.NEW_REPORT, "New report received from " + user.getName() + " " + user.getSurname() + "\n" +
                    "Subject: " + reportSubject + "\n" +
                    "Go to Manage Reports form Users page to take actions");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error, retry later");
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("Report created successfully");

    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}