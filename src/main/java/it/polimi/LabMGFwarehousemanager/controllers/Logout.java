package it.polimi.LabMGFwarehousemanager.controllers;

import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.SQLException;

import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;

@WebServlet(name = "Logout", value = "/Logout")
public class Logout extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection();
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("RememberMe")){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
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