package it.polimi.geodesicwarehousemanager.filters;

import it.polimi.geodesicwarehousemanager.beans.UserBean;
import it.polimi.geodesicwarehousemanager.daos.TokenDAO;
import it.polimi.geodesicwarehousemanager.daos.UserDAO;
import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebFilter(filterName = "UserChecker")
public class UserChecker extends HttpFilter {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**Checks if the user is logged in, if not allows to navigate to login, register, index or error pages, any other request gets redirected to index page.
     * If the user is logged in, checks if he is trying to access the login or register page, if so redirects to homepage*/
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        //todo: remove this (just for testing)
        chain.doFilter(request, response);

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        //todo: update paths
        boolean isLogin = requestURI.equals(contextPath +"/Login");
        boolean isRegister = requestURI.equals(contextPath +"/Register");
        boolean isLogout = requestURI.equals(contextPath +"/Logout");
        boolean isIndex = requestURI.equals(contextPath +"/Index");
        boolean isError = requestURI.contains(contextPath +"/ErrorPages/");
        boolean isChangePassword = requestURI.equals(contextPath +"/ChangePassword");

        boolean hasUser = request.getSession().getAttribute("user")!=null || hasValidRememberMeToken(request, response);

        //todo: update sendRedirects

        if( ((isLogin || isRegister || isIndex) && !hasUser) || isError) {
            chain.doFilter(request, response);
        }
        else if(!hasUser){
            if(isChangePassword){
                if(hasValidChangePwdToken(request, response)) {
                    chain.doFilter(request, response);
                    return;
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
            response.sendRedirect(contextPath + "/Index");
        }
        else if (isLogin || isRegister){
            response.sendRedirect(contextPath + "/HomePage");
        }
        else{
            boolean hasRole = false;
            try{
                hasRole = ((UserBean) request.getSession().getAttribute("user")).getRole().getValue()>0;
            } catch (ClassCastException e){
                e.printStackTrace();
            }
            if(hasRole){
                chain.doFilter(request, response);
            }
            else {
                if(isLogout){
                    chain.doFilter(request, response);
                }
                else {
                    response.sendError(402);
                }
            }
        }
    }

    private boolean hasValidChangePwdToken(HttpServletRequest request, HttpServletResponse response) throws UnavailableException {
        String token = request.getParameter("changePasswordToken");
        if(token!=null){
            TokenDAO tokenDAO = new TokenDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
            int id = tokenDAO.getUserIdByChangePwdToken(token);
            if(id!=-1) {
                UserBean user;
                try {
                     user = userDAO.getUserById(id);
                } catch (SQLException e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return false;
                }
                if (user != null) {
                    request.getSession().setAttribute("user", user);
                    return true;
                }
                return false;
            }
            return false;
        } else {
            return false;
        }
    }

    /**Checks if the user has a remember me cookie and logs him in if it is valid*/
    private boolean hasValidRememberMeToken(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie c : cookies){
                if(c.getName().equals("RememberMe")){
                    TokenDAO tokenDAO = new TokenDAO(connection);
                    UserDAO userDAO = new UserDAO(connection);
                    int id = tokenDAO.getUserIdByRememberMeToken(c.getValue());
                    if(id!=-1) {
                        UserBean user;
                        try {
                            user = userDAO.getUserById(id);
                        } catch (SQLException e) {
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            return false;
                        }
                        if (user != null) {
                            /* TODO decide if to refresh token
                            c.setMaxAge(60 * 60 * 24 * 30);
                            response.addCookie(c);
                            */
                            request.getSession().setAttribute("user", user);
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
            }
        }
        return false;
    }
}
