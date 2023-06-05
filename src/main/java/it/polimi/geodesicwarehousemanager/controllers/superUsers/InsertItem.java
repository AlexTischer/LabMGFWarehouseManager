package it.polimi.geodesicwarehousemanager.controllers.superUsers;

import com.google.gson.Gson;
import it.polimi.geodesicwarehousemanager.beans.ItemBean;
import it.polimi.geodesicwarehousemanager.daos.ItemDAO;
import it.polimi.geodesicwarehousemanager.enums.ItemType;
import it.polimi.geodesicwarehousemanager.enums.Location;
import it.polimi.geodesicwarehousemanager.utils.FileHandler;
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
import java.util.HashMap;
import java.util.Map;

import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;
import jakarta.servlet.http.Part;


@MultipartConfig
@WebServlet(name = "InsertItem", value = "/SuperUser/InsertItem")
public class InsertItem extends HttpServlet {

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
        String description = "";
        String type = "";
        int itemType = 0;
        String serialNumber = "";
        String inventoryNumber = "";
        String location = "";
        int itemLocation = 0;

        try {

            name = request.getParameter("name");
            description = request.getParameter("description");
            type = request.getParameter("type");
            itemType = Integer.parseInt(type);
            serialNumber = request.getParameter("serialNumber");
            inventoryNumber = request.getParameter("inventoryNumber");
            location = request.getParameter("location");
            itemLocation = Integer.parseInt(location);
            if (name.isBlank() || description.isBlank() || type.isBlank() || serialNumber.isBlank() || inventoryNumber.isBlank() || location.isBlank()) {
                throw new Exception();
            }
            if (ItemType.getItemTypeFromInt(itemType) == null || Location.getLocationFromInt(itemLocation) == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
            return;
        }


        String filePath = null;
        String uploadFolder = "Uploads";
        //+ File.separator + ItemType.getItemTypeFromInt(itemType).toString()

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

        ItemDAO itemDAO = new ItemDAO(connection);

        try {
            if (itemDAO.selectItemByInventoryNumber(inventoryNumber) != null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                System.out.println("item already exists");
                return;
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }

        try {
            itemDAO.insertItem(name, description, itemType, serialNumber, inventoryNumber, itemLocation, filePath);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }

        ItemBean itemBean = null;
        try {
            itemBean = itemDAO.selectItemByInventoryNumber(inventoryNumber);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }


        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        Gson gson = new Gson();
        String json = gson.toJson(itemBean);
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