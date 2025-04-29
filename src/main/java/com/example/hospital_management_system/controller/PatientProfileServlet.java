package com.example.hospital_management_system.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "PatientProfileServlet", value = "/PatientProfileServlet")
public class PatientProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/view/pagesJsp/patient/patientProfile.jsp").forward(request, response);
    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
}