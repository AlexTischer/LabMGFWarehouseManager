package it.polimi.geodesicwarehousemanager.filters;

import it.polimi.geodesicwarehousemanager.beans.UserBean;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "AdminChecker")
public class AdminChecker extends HttpFilter {

    /**Checks if the user is an Admin, if not sends a 403 error, if yes allows to navigate to any page*/
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        //todo: remove this (just for testing)
        chain.doFilter(request, response);

        try {
            if (((UserBean) (request.getSession().getAttribute("user"))).getRole().getValue() > 2) {
                chain.doFilter(request, response);
            }
            else {
                response.sendError(403);
            }
        }
        catch (ClassCastException e){
            request.setAttribute("error", "Error with Session User attribute");
            request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
        }
    }
}