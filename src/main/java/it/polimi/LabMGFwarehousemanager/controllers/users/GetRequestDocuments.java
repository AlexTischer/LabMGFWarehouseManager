package it.polimi.LabMGFwarehousemanager.controllers.users;

import it.polimi.LabMGFwarehousemanager.beans.RequestBean;
import it.polimi.LabMGFwarehousemanager.daos.RequestDAO;
import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;
import it.polimi.LabMGFwarehousemanager.utils.FileHandler;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


@MultipartConfig
@WebServlet(name = "GetRequestDocuments", value = "/User/GetRequestDocuments")
public class GetRequestDocuments extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String idString = request.getParameter("id");
        int id;

        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
            return;
        }

        RequestDAO requestDAO = new RequestDAO(connection);

        try {
            RequestBean userRequest = requestDAO.getRequestById(id);
            if (userRequest == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String zipFileName = "request_" + userRequest.getStart().replace('/','-') + "_" + "documents" + ".zip";
            ArrayList<String> documentsPaths = userRequest.getDocumentsPaths();

            File zip = FileHandler.createZipFile(zipFileName, documentsPaths);

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "inline; filename=\"" + zip.getName() + "\"");

            try (InputStream fileInputStream = new FileInputStream(zip);
                 OutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (FileNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                e.printStackTrace();
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
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