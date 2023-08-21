package it.polimi.LabMGFwarehousemanager.controllers.superUsers;

import com.google.gson.Gson;
import it.polimi.LabMGFwarehousemanager.beans.DocumentBean;
import it.polimi.LabMGFwarehousemanager.daos.ItemDAO;
import it.polimi.LabMGFwarehousemanager.enums.DocumentLanguage;
import it.polimi.LabMGFwarehousemanager.enums.DocumentType;
import it.polimi.LabMGFwarehousemanager.utils.FileHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;
import jakarta.servlet.http.Part;


@MultipartConfig
@WebServlet(name = "InsertDocument", value = "/SuperUser/InsertDocument")
public class InsertDocument extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = "";
        String documentTypeString = "";
        int documentType = 0;
        String language = "";
        int documentLanguage = 0;

        try {
            name = request.getParameter("name");
            language = request.getParameter("language");
            documentLanguage = Integer.parseInt(language);
            documentTypeString = request.getParameter("documentType");
            documentType = Integer.parseInt(documentTypeString);
            if (documentTypeString.isBlank() || language.isBlank()) {
                throw new Exception();
            }
            if (DocumentType.getDocumentTypeFromInt(documentType) == null || DocumentLanguage.getDocumentLanguageFromInt(documentLanguage) == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
            return;
        }

        System.out.println("name: " + name);
        System.out.println("documentType: " + documentType);
        System.out.println("documentLanguage: " + documentLanguage);


        String filePath = null;
        String uploadFolder = "Uploads" + File.separator + DocumentType.getDocumentTypeFromInt(documentType).toString();


        try {
            Part part = request.getPart("file");
            String fileName = part.getSubmittedFileName();
            if (fileName!= null && !fileName.isEmpty()) {
                byte[] fileContent = part.getInputStream().readAllBytes();
                filePath = FileHandler.addFile(fileName, fileContent, uploadFolder);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
            return;
        } catch (IOException | ServletException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }

        System.out.println("filePath: " + filePath);

        ItemDAO documentDAO = new ItemDAO(connection);
        DocumentBean document;

        try {
            document = documentDAO.insertDocument(name, filePath, documentType, documentLanguage);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        Gson gson = new Gson();
        String json = gson.toJson(document);
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