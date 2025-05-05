package com.example.hospital_management_system.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter"
//    urlPatterns = {"/AdminDashboardServlet", "view/admindashboard.jsp}
)
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code
    }
}