package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AdminService;
import com.example.hospital_management_system.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (AuthService.isAuthenticated(request) && AuthService.isAdmin(request)) {
            Users user = AuthService.getCurrentUser(request);

            if (user != null) {
                request.setAttribute("currentUser", user);

                // Fetch patient and doctor data
                List<Map<String, Object>> patients = AdminService.getPatientsWithAppointmentCount();
                List<Map<String, Object>> doctors = AdminService.getAllDoctors();

                request.setAttribute("patientsWithAppointments", patients);
                request.setAttribute("doctors", doctors);

                // Forward to admin dashboard
                request.getRequestDispatcher("/view/admin/admindashboard.jsp").forward(request, response);
                return;
            }
        }
        response.sendRedirect("LoginServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // For now, delegate POST to GET
        doGet(request, response);
    }
}
