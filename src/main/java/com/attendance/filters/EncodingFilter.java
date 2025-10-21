package com.attendance.filters;

import jakarta.servlet.*;
import java.io.IOException;

/**
 * EncodingFilter - Ensures UTF-8 encoding for all requests/responses
 */
public class EncodingFilter implements Filter {

    private static final String ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("EncodingFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Set UTF-8 encoding for request and response
        request.setCharacterEncoding(ENCODING);
        response.setCharacterEncoding(ENCODING);

        // Continue with the request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("EncodingFilter destroyed");
    }
}
