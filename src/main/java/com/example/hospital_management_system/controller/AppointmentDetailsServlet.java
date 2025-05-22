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
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/AppointmentDetailsServlet")
public class AppointmentDetailsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AppointmentDetailsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        if (AuthService.isAuthenticated(request) && AuthService.isDoctor(request)) {
            try {
                int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

                Map<String, String> appointmentDetails = AppointmentService.getInstance().getAppointmentDetails(appointmentId);

                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("{");
                boolean first = true;
                for (Map.Entry<String, String> entry : appointmentDetails.entrySet()) {
                    if (!first) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("\"").append(entry.getKey()).append("\":\"")
                            .append(entry.getValue() != null ? entry.getValue().replace("\"", "\\\"") : "")
                            .append("\"");
                    first = false;
                }
                jsonBuilder.append("}");

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(jsonBuilder.toString());
                out.flush();

            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid appointment ID provided", e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid appointment ID");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error retrieving appointment details", e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error retrieving appointment details");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized access");
        }
    }
}
