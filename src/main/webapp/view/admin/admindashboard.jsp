<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.example.hospital_management_system.services.AuthService" %>
<%@ page import="com.example.hospital_management_system.model.Users" %>
<jsp:include page="/view/admin/adminNav.jsp" />
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        /* General Styles */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
        }

        /* Dashboard container */
        .dashboard-container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        /* Dashboard Header */
        .dashboard-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .dashboard-header h1 {
            font-size: 32px;
            margin: 0;
        }

        .welcome-text {
            font-size: 18px;
            color: #555;
        }

        /* Alerts */
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
        }

        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
        }

        /* Cards */
        .card {
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            padding: 20px;
            margin-bottom: 80px;
        }

        .card-header {
            font-size: 20px;
            margin-bottom: 15px;
            color: #333;
            font-weight: bold;
        }

        /* Table */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 15px;
        }

        table th, table td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: left;
        }

        table th {
            background-color: #f1f1f1;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input[type="text"], input[type="email"], input[type="password"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        input[type="submit"] {
            background-color: #3498db;
            color: white;
            padding: 10px 18px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
<%
    if (!AuthService.isAdmin(request)) {
        response.sendRedirect(request.getContextPath() + "/LoginServlet");
        return;
    }

    Users currentUser = AuthService.getCurrentUser(request);
    List<Map<String, Object>> patientsWithAppointments = (List<Map<String, Object>>) request.getAttribute("patientsWithAppointments");
    List<Map<String, Object>> doctors = (List<Map<String, Object>>) request.getAttribute("doctors");
%>

<div class="dashboard-container">

    <div class="dashboard-header">
        <h1>Admin Dashboard</h1>
        <p class="welcome-text">Welcome, <%= currentUser.getName() %></p>
    </div>

    <% if (request.getAttribute("successMessage") != null) { %>
    <div class="alert alert-success">
        <%= request.getAttribute("successMessage") %>
    </div>
    <% } %>

    <% if (request.getAttribute("errorMessage") != null) { %>
    <div class="alert alert-danger">
        <%= request.getAttribute("errorMessage") %>
    </div>
    <% } %>

    <div class="card">
        <div class="card-header">Patients and Appointments</div>
        <table>
            <thead>
            <tr>
                <th>Patient ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Appointment Count</th>
            </tr>
            </thead>
            <tbody>
            <% if (patientsWithAppointments != null && !patientsWithAppointments.isEmpty()) {
                for (Map<String, Object> patient : patientsWithAppointments) { %>
            <tr>
                <td><%= patient.get("patient_id") %></td>
                <td><%= patient.get("user_name") %></td>
                <td><%= patient.get("user_email") %></td>
                <td><%= patient.get("appointment_count") %></td>
            </tr>
            <% }} else { %>
            <tr><td colspan="4" style="text-align:center;">No patients found</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <div class="card">
        <div class="card-header">Doctors Management</div>
        <table>
            <thead>
            <tr>
                <th>Doctor ID</th>
                <th>Name</th>
                <th>Specialty</th>
                <th>Email</th>
            </tr>
            </thead>
            <tbody>
            <% if (doctors != null && !doctors.isEmpty()) {
                for (Map<String, Object> doctor : doctors) { %>
            <tr>
                <td><%= doctor.get("doctor_id") %></td>
                <td><%= doctor.get("user_name") %></td>
                <td><%= doctor.get("doctor_specialty") %></td>
                <td><%= doctor.get("user_email") %></td>
            </tr>
            <% }} else { %>
            <tr><td colspan="4" style="text-align:center;">No doctors found</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>

</div>

</body>
</html>
