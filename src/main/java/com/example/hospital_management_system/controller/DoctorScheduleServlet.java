package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "DoctorScheduleServlet", value = "/DoctorScheduleServlet")
public class DoctorScheduleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is authenticated and is a doctor
        if (AuthService.isAuthenticated(request) && AuthService.isDoctor(request)) {
            // Get the user from the session
            Users user = AuthService.getCurrentUser(request);

            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/pagesJsp/doctor/schedule.jsp").forward(request, response);
        } else {
            response.sendRedirect("LoginServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (AuthService.isAuthenticated(request) && AuthService.isDoctor(request)) {
            Users user = AuthService.getCurrentUser(request);

            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/pagesJsp/doctor/schedule.jsp").forward(request, response);
        } else {
            response.sendRedirect("LoginServlet");
        }
    }
}
