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

        /* Buttons */
        .btn {
            padding: 8px 14px;
            text-decoration: none;
            border-radius: 4px;
            font-size: 14px;
            cursor: pointer;
            border: none;
            display: inline-block;
            margin-right: 5px;
        }

        .btn-danger {
            background-color: #e74c3c;
            color: white;
        }

        .btn-danger:hover {
            background-color: #c0392b;
        }

        .btn-primary {
            background-color: #3498db;
            color: white;
        }

        .btn-primary:hover {
            background-color: #2980b9;
        }

        /* Modal */
        .modal {
            display: none;
            position: fixed;
            z-index: 999;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }

        .modal-content {
            background-color: #fff;
            margin: 10% auto;
            padding: 20px;
            border-radius: 5px;
            width: 400px;
            position: relative;
        }

        .close {
            color: #aaa;
            position: absolute;
            right: 15px;
            top: 10px;
            font-size: 24px;
            cursor: pointer;
        }

        .close:hover {
            color: #000;
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
            margin-bottom: 15px;
            box-sizing: border-box;
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

        .form-group {
            margin-bottom: 15px;
        }

        .button-group {
            display: flex;
            gap: 10px;
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

    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");

    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");
%>

<div class="dashboard-container">

    <div class="dashboard-header">
        <h1>Admin Dashboard</h1>
        <p class="welcome-text">Welcome, <%= currentUser.getName() %></p>
    </div>

    <% if (successMessage != null && !successMessage.isEmpty()) { %>
    <div class="alert alert-success">
        <%= successMessage %>
    </div>
    <% } %>

    <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
    <div class="alert alert-danger">
        <%= errorMessage %>
    </div>
    <% } %>

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
                <th>Actions</th>
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
                <td class="button-group">
                    <button class="btn btn-primary" onclick="openEditModal(<%= doctor.get("doctor_id") %>, '<%= doctor.get("user_name") %>', '<%= doctor.get("doctor_specialty") %>', '<%= doctor.get("user_email") %>')">Edit</button>
                    <button class="btn btn-danger" onclick="confirmDelete(<%= doctor.get("doctor_id") %>, '<%= doctor.get("user_name") %>')">Delete</button>
                </td>
            </tr>
            <% }} else { %>
            <tr><td colspan="5" style="text-align:center;">No doctors found</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div id="deleteModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <h2>Confirm Delete</h2>
        <p id="deleteConfirmMessage">Are you sure you want to delete this doctor?</p>
        <p><strong>WARNING:</strong> This will completely remove the doctor from the system. The doctor will no longer be able to log in, and all their appointments will be deleted.</p>
        <div style="display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px;">
            <button class="btn" style="background-color: #ccc;" onclick="closeModal()">Cancel</button>
            <form id="deleteForm" action="<%= request.getContextPath() %>/DeleteDoctorServlet" method="get">
                <input type="hidden" id="doctorIdInput" name="doctorId" value="">
                <button type="submit" class="btn btn-danger">Delete Permanently</button>
            </form>
        </div>
    </div>
</div>

<!-- Edit Doctor Modal -->
<div id="editModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeEditModal()">&times;</span>
        <h2>Edit Doctor</h2>
        <form id="editForm" action="<%= request.getContextPath() %>/EditDoctorServlet" method="post">
            <input type="hidden" id="editDoctorId" name="doctorId" value="">

            <div class="form-group">
                <label for="editName">Name:</label>
                <input type="text" id="editName" name="name" required>
            </div>

            <div class="form-group">
                <label for="editSpecialty">Specialty:</label>
                <input type="text" id="editSpecialty" name="specialty" required>
            </div>

            <div class="form-group">
                <label for="editEmail">Email:</label>
                <input type="email" id="editEmail" name="email" required>
            </div>

            <div style="display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px;">
                <button type="button" class="btn" style="background-color: #ccc;" onclick="closeEditModal()">Cancel</button>
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </div>
        </form>
    </div>
</div>

<script>
    // Delete Modal functions
    const modal = document.getElementById('deleteModal');

    function confirmDelete(doctorId, doctorName) {
        document.getElementById('doctorIdInput').value = doctorId;
        document.getElementById('deleteConfirmMessage').textContent =
            "Are you sure you want to delete Dr. " + doctorName + "?";
        modal.style.display = 'block';
    }

    function closeModal() {
        modal.style.display = 'none';
    }

    // Edit Modal functions
    const editModal = document.getElementById('editModal');
    const editForm = document.getElementById('editForm');
    const editDoctorId = document.getElementById('editDoctorId');
    const editName = document.getElementById('editName');
    const editSpecialty = document.getElementById('editSpecialty');
    const editEmail = document.getElementById('editEmail');

    function openEditModal(doctorId, doctorName, doctorSpecialty, doctorEmail) {
        editDoctorId.value = doctorId;
        editName.value = doctorName;
        editSpecialty.value = doctorSpecialty;
        editEmail.value = doctorEmail;
        editModal.style.display = 'block';
    }

    function closeEditModal() {
        editModal.style.display = 'none';
    }

    // Close modals when clicking outside
    window.onclick = function(event) {
        if (event.target === modal) {
            closeModal();
        }
        if (event.target === editModal) {
            closeEditModal();
        }
    }
</script>

</body>
</html>