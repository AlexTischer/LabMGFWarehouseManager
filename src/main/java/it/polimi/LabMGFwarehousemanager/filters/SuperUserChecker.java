package it.polimi.LabMGFwarehousemanager.filters;

import it.polimi.LabMGFwarehousemanager.beans.UserBean;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "SuperUserChecker")
public class SuperUserChecker extends HttpFilter {

    /**Checks if the user is a SuperUser, if not sends a 403 error, if yes allows to navigate to any page*/
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        if (requestURI.startsWith(contextPath + "/superusers") || requestURI.startsWith(contextPath + "/SuperUser/")) {
            try {
                if (((UserBean) (request.getSession().getAttribute("user"))).getRole().getValue() > 1) {
                    chain.doFilter(request, response);
                } else {
                    response.sendError(403);
                }
            } catch (ClassCastException e) {
                request.setAttribute("error", "Error with Session User attribute");
                request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
