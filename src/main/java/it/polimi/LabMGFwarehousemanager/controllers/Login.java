package it.polimi.LabMGFwarehousemanager.controllers;

import com.google.gson.Gson;
import it.polimi.LabMGFwarehousemanager.beans.UserBean;
import it.polimi.LabMGFwarehousemanager.daos.TokenDAO;
import it.polimi.LabMGFwarehousemanager.daos.UserDAO;
import it.polimi.LabMGFwarehousemanager.utils.PasswordHandler;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;

@MultipartConfig
@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    /**Checks if login parameters are valid, otherwise shows login page with error message. If parameters are valid, proceeds with login*/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(email == null || password == null || email.isBlank() || password.isBlank()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Email or password are empty");
            return;
        }
        else{
            try {
                UserDAO userDAO = new UserDAO(connection);
                UserBean user = userDAO.getUserByEmail(email);

                if (user != null && PasswordHandler.checkPassword(password, user.getPassword())){

                    UserBean userCopy = user.clone();
                    userCopy.setPassword(null);

                    if(request.getParameter("rememberMe") != null && request.getParameter("rememberMe").equals("on")) {
                        TokenDAO tokenDAO = new TokenDAO(connection);
                        String token = tokenDAO.insertRememberMeToken(user);
                        Cookie rememberMeCookie = new Cookie("RememberMe", token);
                        rememberMeCookie.setMaxAge(60 * 60 * 24 * 30);
                        response.addCookie(rememberMeCookie);
                    }
                    request.getSession().setAttribute("user", user);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    Gson gson = new Gson();
                    String json = gson.toJson(userCopy);
                    response.getWriter().println(json);
                }
                else{
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().println("Email or password are wrong");
                    return;
                }
            } catch (UnavailableException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Internal error logging in");
                return;
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