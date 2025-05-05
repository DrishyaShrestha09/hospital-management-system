package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.utils.DBConnectionUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/ManageDepartments")
public class ManageDepartmentsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ManageDepartmentsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Manage Departments</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println("h1, h2 { color: #333; }");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("tr:nth-child(even) { background-color: #f9f9f9; }");
        out.println("form { margin: 20px 0; padding: 15px; border: 1px solid #ddd; background-color: #f9f9f9; }");
        out.println("input[type=text] { padding: 8px; width: 300px; }");
        out.println("input[type=submit] { padding: 8px 15px; background-color: #4CAF50; color: white; border: none; cursor: pointer; }");
        out.println("input[type=submit]:hover { background-color: #45a049; }");
        out.println(".success { color: green; }");
        out.println(".error { color: red; }");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<h1>Manage Departments</h1>");

        // Display success or error messages
        String success = request.getParameter("success");
        String error = request.getParameter("error");

        if ("true".equals(success)) {
            out.println("<p class='success'>Department added successfully!</p>");
        } else if (error != null && !error.isEmpty()) {
            out.println("<p class='error'>Error: " + error + "</p>");
        }

        // Add department form
        out.println("<h2>Add New Department</h2>");
        out.println("<form method='post' action='ManageDepartments'>");
        out.println("<label for='departmentName'>Department Name:</label><br>");
        out.println("<input type='text' id='departmentName' name='departmentName' required><br><br>");
        out.println("<input type='submit' value='Add Department'>");
        out.println("</form>");

        // Display existing departments
        out.println("<h2>Existing Departments</h2>");

        try (Connection conn = DBConnectionUtils.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM department ORDER BY department_name");
                     ResultSet rs = stmt.executeQuery()) {

                    out.println("<table>");
                    out.println("<tr><th>ID</th><th>Department Name</th></tr>");
                    boolean hasDepartments = false;

                    while (rs.next()) {
                        hasDepartments = true;
                        out.println("<tr>");
                        out.println("<td>" + rs.getInt("department_id") + "</td>");
                        out.println("<td>" + rs.getString("department_name") + "</td>");
                        out.println("</tr>");
                    }

                    out.println("</table>");

                    if (!hasDepartments) {
                        out.println("<p class='error'>No departments found in the database!</p>");
                        out.println("<p>You need to add at least one department before updating doctor profiles.</p>");
                    }
                }
            } else {
                out.println("<p class='error'>Failed to establish database connection!</p>");
            }
        } catch (SQLException e) {
            out.println("<p class='error'>Database error: " + e.getMessage() + "</p>");
            LOGGER.log(Level.SEVERE, "Database error", e);
        }

        out.println("<p><a href='DoctorProfile'>Return to Doctor Profile</a></p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String departmentName = request.getParameter("departmentName");

        if (departmentName != null && !departmentName.trim().isEmpty()) {
            try (Connection conn = DBConnectionUtils.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO department (department_name) VALUES (?)")) {

                stmt.setString(1, departmentName);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    response.sendRedirect("ManageDepartments?success=true");
                } else {
                    response.sendRedirect("ManageDepartments?error=insert-failed");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error creating department", e);
                response.sendRedirect("ManageDepartments?error=" + e.getMessage());
            }
        } else {
            response.sendRedirect("ManageDepartments?error=invalid-name");
        }
    }
}