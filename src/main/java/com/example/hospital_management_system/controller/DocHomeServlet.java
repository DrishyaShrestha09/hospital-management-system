package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AppointmentService;
import com.example.hospital_management_system.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/DocHome")
public class DocHomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (AuthService.isAuthenticated(request) && AuthService.isDoctor(request)) {
            Users user = AuthService.getCurrentUser(request);

            // Null check for user before proceeding
            if (user != null) {
                request.setAttribute("currentUser", user);

                // Fetch appointments for the doctor
                List<Map<String, String>> appointments = AppointmentService.getInstance().getAppointmentsForDoctor(user.getUserId());

                if (appointments == null || appointments.isEmpty()) {
                    System.out.println("No appointments found for doctor with ID: " + user.getUserId());
                } else {
                    System.out.println("Appointments for doctor: " + appointments.size());
                }

                // Set the appointments list to request attributes
                request.setAttribute("appointments", appointments);

                // Forward to the doctor dashboard
                request.getRequestDispatcher("/view/pagesJsp/doctor/doctorDashboard.jsp").forward(request, response);
            } else {
                // Handle the case where user is null
                response.sendRedirect("LoginServlet");
            }
        } else {
            response.sendRedirect("LoginServlet");
        }
    }
}
