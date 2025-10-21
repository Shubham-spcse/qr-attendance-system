package com.attendance.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AuthorizationFilter - Checks if user has correct role for the resource
 * Ensures students can't access admin/staff pages, etc.
 */
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthorizationFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("userRole") == null) {
            // No session or role, redirect to login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");
        String requestURI = httpRequest.getRequestURI();

        // Check role-based access
        boolean authorized = false;

        if (requestURI.contains("/admin/")) {
            authorized = "ADMIN".equals(userRole);
        } else if (requestURI.contains("/staff/")) {
            authorized = "STAFF".equals(userRole);
        } else if (requestURI.contains("/student/")) {
            authorized = "STUDENT".equals(userRole);
        } else {
            authorized = true; // Allow access to common resources
        }

        if (authorized) {
            // User has correct role, proceed
            chain.doFilter(request, response);
        } else {
            // Unauthorized access attempt
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, 
                "You don't have permission to access this resource");
        }
    }

    @Override
    public void destroy() {
        System.out.println("AuthorizationFilter destroyed");
    }
}
