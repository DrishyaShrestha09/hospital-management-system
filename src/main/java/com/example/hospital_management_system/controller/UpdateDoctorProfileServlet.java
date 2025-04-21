package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.dao.DoctorDAO;
import com.example.hospital_management_system.model.Doctor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/updateDoctorProfile")  // This maps the servlet to /updateDoctorProfile URL
public class UpdateDoctorProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve form data from the request
        int doctorId = Integer.parseInt(request.getParameter("doctorId"));
        String specialty = request.getParameter("specialty");
        int experience = Integer.parseInt(request.getParameter("experience"));
        int departmentId = Integer.parseInt(request.getParameter("departmentId"));

        // Create a new Doctor object
        Doctor doctor = new Doctor();
        doctor.setDoctorId(doctorId);
        doctor.setSpecialty(specialty);
        doctor.setExperience(experience);
        doctor.setDepartmentId(departmentId);

        // Create an instance of DoctorDAO to interact with the database
        DoctorDAO dao = new DoctorDAO();

        // Update the doctor profile using DAO
        boolean updated = dao.updateDoctorProfile(doctor);

        // Set success or failure message as a request attribute
        if (updated) {
            request.setAttribute("successMessage", "Profile updated successfully.");
        } else {
            request.setAttribute("errorMessage", "Failed to update profile.");
        }

        // Pass the updated doctor object to the JSP for rendering
        request.setAttribute("doctor", doctor);

        // Forward the request to the doctor profile page
        request.getRequestDispatcher("/view/pagesJsp/doctor/doctorProfile.jsp").forward(request, response);
    }
}
