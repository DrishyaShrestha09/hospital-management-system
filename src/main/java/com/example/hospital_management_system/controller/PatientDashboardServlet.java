package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AppointmentService;
import com.example.hospital_management_system.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/PatientDashboardServlet")
public class PatientDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (AuthService.isAuthenticated(request) && AuthService.isPatient(request)) {
            Users user = AuthService.getCurrentUser(request);
            request.setAttribute("currentUser", user);

            // Fetch appointments for patient
            List<Map<String, String>> appointments = AppointmentService.getInstance().getAppointmentsForPatient(user.getUserId());
            request.setAttribute("appointments", appointments);

            request.getRequestDispatcher("/view/pagesJsp/patient/patientDashboard.jsp").forward(request, response);
        } else {
            response.sendRedirect("LoginServlet");
        }
    }
}
