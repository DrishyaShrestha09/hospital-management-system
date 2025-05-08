package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (AuthService.isAuthenticated(request) && AuthService.isAdmin(request)) {
            Users user = AuthService.getCurrentUser(request);

            if (user != null) {
                request.setAttribute("currentUser", user);

                request.getRequestDispatcher("/view/admin/admindashboard.jsp").forward(request, response);
            } else {
                response.sendRedirect("LoginServlet");
            }
        }else{
            response.sendRedirect("LoginServlet");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}