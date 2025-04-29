package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "BookAppointmentServlet", value = "/BookAppointmentServlet")
public class BookAppointmentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is authenticated and is a doctor
        if (AuthService.isAuthenticated(request) && AuthService.isPatient(request)) {
            // Get the user from the session
            Users user = AuthService.getCurrentUser(request);

            // Set user attribute and forward to doctor's dashboard
            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/pagesJsp/patient/book-appointment.jsp").forward(request, response);
        } else {
            // Redirect unauthenticated users or non-doctors to login
            response.sendRedirect("LoginServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is authenticated and is a doctor
        if (AuthService.isAuthenticated(request) && AuthService.isPatient(request)) {
            // Get the user from the session
            Users user = AuthService.getCurrentUser(request);

            // Set user attribute and forward to doctor's dashboard
            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/pagesJsp/patient/book-appointment.jsp").forward(request, response);

        } else {
            // Redirect unauthenticated users or non-doctors to login
            response.sendRedirect("LoginServlet");
        }
    }

}