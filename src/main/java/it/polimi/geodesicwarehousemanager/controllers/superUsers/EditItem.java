package it.polimi.geodesicwarehousemanager.controllers.superUsers;

import it.polimi.geodesicwarehousemanager.daos.ItemDAO;
import it.polimi.geodesicwarehousemanager.enums.Location;
import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;
import it.polimi.geodesicwarehousemanager.utils.FileHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@MultipartConfig
@WebServlet(name = "EditItem", value = "/SuperUser/EditItem")
public class EditItem extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id;
        String name = "";
        String description = "";
        String location = "";
        int itemLocation = -1;

        ItemDAO itemDAO = new ItemDAO(connection);

        try {
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                throw new Exception();
            }
            name = request.getParameter("name");
            description = request.getParameter("description");
            location = request.getParameter("location");
            try {
                itemLocation = Integer.parseInt(location);
            } catch (NumberFormatException e) {
                itemLocation = -1;
            }
            if (id < 0 || itemDAO.selectItemById(id) == null) {
                throw new Exception();
            }
            if (Location.getLocationFromInt(itemLocation) == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
            return;
        }


        String filePath = null;
        String uploadFolder = "Uploads";

        try {
            Part part = request.getPart("file");
            String fileName = part.getSubmittedFileName();
            if (fileName!= null && !fileName.isEmpty()) {
                byte[] fileContent = part.getInputStream().readAllBytes();
                filePath = FileHandler.addFile(fileName, fileContent, uploadFolder);
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

        try {
            itemDAO.updateItemById(id, name, description, itemLocation, filePath);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }


        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}