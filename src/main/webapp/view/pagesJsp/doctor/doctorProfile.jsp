<%@ page import="com.example.hospital_management_system.model.Doctor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Doctor Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/profiles.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
</head>
<body class="profile-page">

<jsp:include page="/view/pagesJsp/doctor/doctorNav.jsp" />

<%
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");

    Doctor doctor = (Doctor) request.getAttribute("doctor");
    if (doctor == null) {
        doctor = new Doctor();
    }
%>

<div class="profile-container">
    <div class="profile-header">
        <h1>Doctor Profile</h1>
        <p>Update your professional information</p>
    </div>

    <% if (successMessage != null) { %>
    <div class="alert alert-success"><%= successMessage %></div>
    <% } %>
    <% if (errorMessage != null) { %>
    <div class="alert alert-error"><%= errorMessage %></div>
    <% } %>

    <div class="profile-content">
        <div class="profile-card">
            <div class="profile-avatar">
                <img src="/placeholder.svg?height=150&width=150" alt="Profile" class="avatar-image" />
            </div>

            <div class="profile-details">
                <form method="post" action="${pageContext.request.contextPath}/updateDoctorProfile">

                    <input type="hidden" name="doctorId" value="<%= doctor.getDoctorId() %>" />

                    <div class="form-row">
                        <div class="input-group">
                            <label for="specialty">Specialty</label>
                            <select name="specialty" id="specialty" required>
                                <%
                                    String[] specValues = {"cardiology", "dermatology", "neurology", "orthopedics", "pediatrics", "psychiatry", "general"};
                                    String[] specLabels = {"Cardiology", "Dermatology", "Neurology", "Orthopedics", "Pediatrics", "Psychiatry", "General Medicine"};
                                    for (int i = 0; i < specValues.length; i++) {
                                %>
                                <option value="<%= specValues[i] %>" <%= specValues[i].equals(doctor.getSpecialty()) ? "selected" : "" %>>
                                    <%= specLabels[i] %>
                                </option>
                                <% } %>
                            </select>
                        </div>

                        <div class="input-group">
                            <label for="experience">Experience (in years)</label>
                            <input type="number" name="experience" id="experience" value="<%= doctor.getExperience() %>" min="0" required />
                        </div>

                    <div class="form-row">
                        <div class="input-group">
                            <label for="departmentId">Department ID</label>
                            <input type="number" name="departmentId" id="departmentId" value="<%= doctor.getDepartmentId() %>" required />
                        </div>
                    </div>

                    <div class="profile-actions">
                        <button type="submit" class="btn">Save Changes</button>
                        <a href="${pageContext.request.contextPath}/doctorProfile" class="btn btn-outline">Cancel</a>
                    </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/view/pagesJsp/footer.jsp" />

</body>
</html>
