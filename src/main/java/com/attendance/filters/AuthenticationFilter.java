package com.attendance.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AuthenticationFilter - Checks if user is logged in
 * Redirects to login page if not authenticated
 */
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthenticationFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Check if user is logged in
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (loggedIn) {
            // User is authenticated, proceed
            chain.doFilter(request, response);
        } else {
            // Not authenticated, redirect to login
            String contextPath = httpRequest.getContextPath();
            httpResponse.sendRedirect(contextPath + "/login");
        }
    }

    @Override
    public void destroy() {
        System.out.println("AuthenticationFilter destroyed");
    }
}
