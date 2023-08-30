package it.polimi.LabMGFwarehousemanager.filters;

import it.polimi.LabMGFwarehousemanager.beans.UserBean;
import it.polimi.LabMGFwarehousemanager.daos.TokenDAO;
import it.polimi.LabMGFwarehousemanager.daos.UserDAO;
import it.polimi.LabMGFwarehousemanager.utils.ConnectionHandler;
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

import static it.polimi.LabMGFwarehousemanager.utils.Constants.*;

@WebFilter(filterName = "UserChecker")
public class UserChecker extends HttpFilter {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection();
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

    /**Checks if the user is logged in, if not allows to navigate to log in, register, index or error pages, any other request gets redirected to index page.
     * If the user is logged in, checks if he is trying to access the login or register page, if so redirects to homepage*/
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        UserBean user = (UserBean) request.getSession().getAttribute("user");
        boolean hasUser = user!=null || hasValidRememberMeToken(request, response);
        user = (UserBean) request.getSession().getAttribute("user");
        boolean isConfirmed = (hasUser && user.getRole().getValue()>=0);
        boolean hasRole = (hasUser && (user.getRole().getValue()>0));

        boolean isWelcome = requestURI.equals(contextPath + "/");
        boolean isPublic = requestURI.startsWith(contextPath + "/public/") || (!requestURI.contains("/User/") && !requestURI.contains("/SuperUser/") && !requestURI.contains("/Admin/") && !requestURI.endsWith(".html") && !requestURI.endsWith(".js"));
        boolean isChangePassword = requestURI.equals(contextPath + "/User/ChangePassword") || requestURI.startsWith(contextPath + CHANGE_PASSWORD_PATH);
        boolean isLogin = requestURI.startsWith(contextPath + "/Login") || requestURI.startsWith(contextPath + LOGIN_PATH);
        boolean isRegister = requestURI.startsWith(contextPath + "/Register") || requestURI.startsWith(contextPath + REGISTER_PATH);
        boolean isIndex = isPublic && !isLogin && !isRegister;
        boolean isConfirm = requestURI.startsWith(contextPath + "/ConfirmRegistration") || requestURI.startsWith(contextPath + CONFIRM_PATH);
        boolean isLogout = requestURI.equals(contextPath + "/Logout");
        boolean isWaitForRole = requestURI.startsWith(contextPath + WAIT_FOR_ROLE_PATH);

        if(isWelcome){
            response.sendRedirect(contextPath + INDEX_PATH + "index.html");
        } else if(!hasUser){
            if(isPublic){
                chain.doFilter(request, response);
            } else if(isChangePassword) {
                if (hasValidChangePwdToken(request, response)) {
                    chain.doFilter(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                }
            } else {
                response.sendRedirect(contextPath + INDEX_PATH + "index.html");
            }
        } else if (!isConfirmed){
            if(isConfirm) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(contextPath + CONFIRM_PATH + "confirmRegistration.html");
            }
        } else if (!hasRole){
            if(isWaitForRole || isLogout || isChangePassword || isIndex) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(contextPath + WAIT_FOR_ROLE_PATH + "waitForRole.html");
            }
        } else {
            if(isLogin || isRegister || isConfirm || isWaitForRole){
                response.sendRedirect(contextPath + HOMEPAGE_PATH + "homePage.html");
            } else {
                chain.doFilter(request, response);
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
                     user = userDAO.getUserById(id, false);
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
                            user = userDAO.getUserById(id, false);
                        } catch (SQLException e) {
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            return false;
                        }
                        if (user != null) {
                            c.setMaxAge(60 * 60 * 24 * 30);
                            response.addCookie(c);
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
