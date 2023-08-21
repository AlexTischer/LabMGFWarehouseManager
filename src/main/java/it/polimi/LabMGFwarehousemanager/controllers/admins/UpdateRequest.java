package it.polimi.LabMGFwarehousemanager.controllers.admins;

import com.google.gson.Gson;
import it.polimi.LabMGFwarehousemanager.beans.UserBean;
import it.polimi.LabMGFwarehousemanager.daos.NotificationDAO;
import it.polimi.LabMGFwarehousemanager.daos.RequestDAO;
import it.polimi.LabMGFwarehousemanager.enums.NotificationType;
import it.polimi.LabMGFwarehousemanager.enums.RequestStatus;
import it.polimi.LabMGFwarehousemanager.utils.MailHandler;
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

import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;


@MultipartConfig
@WebServlet(name = "UpdateRequest", value = "/Admin/UpdateRequest")
public class UpdateRequest extends HttpServlet {

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
        int id;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String dataJson = sb.toString();

        if(dataJson.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            Map<String, Object> data;
            try {
                data = new Gson().fromJson(dataJson, Map.class);
                Double doubleId =  (Double) data.get("id");
                id = doubleId.intValue();
                Double doubleStatus = (Double) data.get("status");
                RequestStatus status = RequestStatus.getRequestStatusFromInt(doubleStatus.intValue());
                if(status == null){
                    throw new Exception("Status is not valid");
                }

                String adminNotes = (String) data.get("adminNotes");

                ArrayList<Double> doubleDocuments;
                ArrayList<Integer> documents = new ArrayList<>();
                if(data.get("documents") != null){
                    doubleDocuments = (ArrayList<Double>) data.get("documents");
                    for (Double doubleDocument : doubleDocuments) {
                        documents.add(doubleDocument.intValue());
                    }
                }
                RequestDAO requestDAO = new RequestDAO(connection);
                requestDAO.updateRequest(id, status, adminNotes, documents);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("Request updated successfully");
            } catch (NumberFormatException e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
                return;
            }
        }

        RequestDAO requestDAO = new RequestDAO(connection);
        try {
            UserBean user = requestDAO.getRequestById(id).getRequestingUser();
            MailHandler.sendMail(user.getEmail(),
                    "Your request on Lab MGF Warehouse Manager has been updated",
                    "Your request on Lab MGF Warehouse Manager has been updated. " +
                            "Please check the website to see the changes and take actions."
            );

            NotificationDAO notificationDAO = new NotificationDAO(connection);

            ArrayList<Integer> userId = new ArrayList<>();
            userId.add(user.getId());
            notificationDAO.addNotification(userId, NotificationType.NEW_REQUEST_UPDATE,
                    "Your request has been updated" + "<br/>" +
                    "Go to Manage User Reports page to take actions");

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error, retry later");
            return;
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