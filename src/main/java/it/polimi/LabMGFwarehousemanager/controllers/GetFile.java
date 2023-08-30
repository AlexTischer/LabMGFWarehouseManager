package it.polimi.LabMGFwarehousemanager.controllers;

import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;
import it.polimi.LabMGFwarehousemanager.utils.FileHandler;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;


@MultipartConfig
@WebServlet(name = "GetFile", value = "/GetFile")
public class GetFile extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection();
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path;

        try {
            String encodedPath = request.getParameter("path");
            path = URLDecoder.decode(encodedPath, "UTF-8");
        } catch (NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Missing path");
            return;
        }

        if(path == null || path.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Missing path");
            return;
        }

        File file = FileHandler.getFile(path);

        if (file == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("File not found");
            return;
        } else {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

            try (InputStream fileInputStream = new FileInputStream(file);
                 OutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
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