<%@ page import="com.example.hospital_management_system.model.Doctor" %>
<%@ page import="com.example.hospital_management_system.model.Users" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Doctor Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/profiles.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;

        }
        .profile-container {
            max-width: 1000px;
            margin: 40px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .profile-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .profile-header h1 {
            color: #333;
            margin-bottom: 5px;
        }
        .profile-header p {
            color: #666;
        }
        .profile-content {
            display: flex;
            flex-direction: column;
        }
        .profile-card {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }
        @media (min-width: 768px) {
            .profile-card {
                flex-direction: row;
            }
        }
        .profile-avatar {
            flex: 1;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 8px;
        }
        .avatar-image {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            object-fit: cover;
            margin-bottom: 15px;
        }
        .profile-details {
            flex: 2;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 8px;
        }
        .form-row {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            margin-bottom: 15px;
        }
        .input-group {
            flex: 1;
            min-width: 250px;
            margin-bottom: 15px;
        }
        .input-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            color: #333;
        }
        .input-group input, .input-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
        }
        .profile-actions {
            display: flex;
            justify-content: flex-start;
            gap: 10px;
            margin-top: 20px;
        }
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: 600;
            transition: background-color 0.3s;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
        .btn-outline {
            background-color: transparent;
            border: 1px solid #007bff;
            color: #007bff;
        }
        .btn-outline:hover {
            background-color: #f0f8ff;
        }
        .error-message {
            color: #dc3545;
            font-size: 0.9em;
            margin-top: 5px;
            display: none;
        }
        .alert {
            padding: 15px;
            margin: 15px 0;
            border-radius: 4px;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
        }
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
        }
        .tabs {
            display: flex;
            margin-bottom: 20px;
            border-bottom: 1px solid #ddd;
        }
        .tab {
            padding: 10px 20px;
            cursor: pointer;
            border-bottom: 2px solid transparent;
        }
        .tab.active {
            border-bottom: 2px solid #007bff;
            color: #007bff;
            font-weight: 600;
        }
        .tab-content {
            display: none;
        }
        .tab-content.active {
            display: block;
        }
        .department-link {
            display: block;
            margin-top: 5px;
            color: #007bff;
            text-decoration: none;
        }
        .department-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body class="profile-page">

<jsp:include page="/view/pagesJsp/doctor/doctorNav.jsp" />

<%
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");

    Users user = (Users) request.getAttribute("user");
    Doctor doctor = (Doctor) request.getAttribute("doctor");
    List<Map<String, Object>> departments = (List<Map<String, Object>>) request.getAttribute("departments");

    if (doctor == null) {
        doctor = new Doctor();
    }

    String profileImage = "";
    if (user != null && user.getProfile() != null && user.getProfile().length > 0) {
        profileImage = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getProfile());
    }
%>

<div class="profile-container">
    <div class="profile-header">
        <h1>Doctor Profile</h1>
        <p>Update your personal and professional information</p>
    </div>

    <% if (successMessage != null) { %>
    <div class="alert alert-success"><%= successMessage %></div>
    <% } %>
    <% if (errorMessage != null) { %>
    <div class="alert alert-error"><%= errorMessage %></div>
    <% } %>

    <div class="tabs">
        <div class="tab active" onclick="openTab(event, 'personal')">Personal Information</div>
        <div class="tab" onclick="openTab(event, 'professional')">Professional Information</div>
    </div>

    <div class="profile-content">
        <form method="post" action="${pageContext.request.contextPath}/DoctorProfile" id="doctorProfileForm" enctype="multipart/form-data" onsubmit="return validateForm()">
            <input type="hidden" name="doctorId" value="<%= doctor.getDoctorId() %>" />
            <input type="hidden" name="userId" value="<%= user != null ? user.getUserId() : "" %>" />

            <div id="personal" class="tab-content active">
                <div class="profile-card">
                    <div class="profile-avatar">
                        <% if (!profileImage.isEmpty()) { %>
                        <img src="<%= profileImage %>" alt="Profile" class="avatar-image" id="profile-preview" />
                        <% } else { %>
                        <img src="${pageContext.request.contextPath}/view/images/default-profile.jpg" alt="Profile" class="avatar-image" id="profile-preview" />
                        <% } %>
                        <input type="file" name="profilePicture" id="profilePicture" accept="image/*" onchange="previewImage(this)" />
                        <small>Max size: 5MB. Formats: JPG, PNG</small>
                    </div>

                    <div class="profile-details">
                        <div class="form-row">
                            <div class="input-group">
                                <label for="name">Full Name</label>
                                <input type="text" name="name" id="name" value="<%= user != null ? user.getName() : "" %>" required />
                                <div class="error-message" id="name-error">Please enter your full name</div>
                            </div>

                            <div class="input-group">
                                <label for="email">Email Address</label>
                                <input type="email" name="email" id="email" value="<%= user != null ? user.getEmail() : "" %>" required />
                                <div class="error-message" id="email-error">Please enter a valid email address</div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="input-group">
                                <label for="phone">Phone Number</label>
                                <input type="tel" name="phone" id="phone" value="<%= user != null ? (user.getPhone() != null ? user.getPhone() : "") : "" %>" />
                                <div class="error-message" id="phone-error">Please enter a valid phone number</div>
                            </div>

                            <div class="input-group">
                                <label for="gender">Gender</label>
                                <select name="gender" id="gender">
                                    <option value="">Select Gender</option>
                                    <option value="Male" <%= user != null && "Male".equals(user.getGender()) ? "selected" : "" %>>Male</option>
                                    <option value="Female" <%= user != null && "Female".equals(user.getGender()) ? "selected" : "" %>>Female</option>
                                    <option value="Other" <%= user != null && "Other".equals(user.getGender()) ? "selected" : "" %>>Other</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="input-group">
                                <label for="address">Address</label>
                                <input type="text" name="address" id="address" value="<%= user != null ? (user.getAddress() != null ? user.getAddress() : "") : "" %>" />
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="input-group">
                                <label for="currentPassword">Current Password (required to update email or password)</label>
                                <input type="password" name="currentPassword" id="currentPassword" />
                                <div class="error-message" id="currentPassword-error">Current password is required to update email or password</div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="input-group">
                                <label for="newPassword">New Password (leave blank to keep current)</label>
                                <input type="password" name="newPassword" id="newPassword" />
                                <div class="error-message" id="newPassword-error">Password must be at least 8 characters</div>
                            </div>

                            <div class="input-group">
                                <label for="confirmPassword">Confirm New Password</label>
                                <input type="password" name="confirmPassword" id="confirmPassword" />
                                <div class="error-message" id="confirmPassword-error">Passwords do not match</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="professional" class="tab-content">
                <div class="profile-card">
                    <div class="profile-details">
                        <div class="form-row">
                            <div class="input-group">
                                <label for="specialty">Specialty</label>
                                <select name="specialty" id="specialty" required>
                                    <option value="">Select Specialty</option>
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
                                <div class="error-message" id="specialty-error">Please select a specialty</div>
                            </div>

                            <div class="input-group">
                                <label for="experience">Experience (in years)</label>
                                <input type="number" name="experience" id="experience" value="<%= doctor.getExperience() %>" min="0" required />
                                <div class="error-message" id="experience-error">Experience must be a positive number</div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="input-group">
                                <label for="departmentId">Department</label>
                                <select name="departmentId" id="departmentId" required>
                                    <option value="">Select Department</option>
                                    <% if (departments != null) {
                                        for (Map<String, Object> dept : departments) {
                                            int deptId = (Integer) dept.get("department_id");
                                            String deptName = (String) dept.get("department_name");
                                    %>
                                    <option value="<%= deptId %>" <%= deptId == doctor.getDepartmentId() ? "selected" : "" %>><%= deptName %></option>
                                    <% } } %>
                                </select>
                                <div class="error-message" id="departmentId-error">Please select a department</div>
                                <a href="${pageContext.request.contextPath}/ManageDepartments" class="department-link">Manage Departments</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="profile-actions">
                <button type="submit" class="btn btn-primary">Save Changes</button>
                <a href="${pageContext.request.contextPath}/DocHome" class="btn btn-outline">Cancel</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/view/pagesJsp/footer.jsp" />

<script>
    function openTab(evt, tabName) {
        // Hide all tab content
        var tabcontent = document.getElementsByClassName("tab-content");
        for (var i = 0; i < tabcontent.length; i++) {
            tabcontent[i].classList.remove("active");
        }

        // Remove "active" class from all tabs
        var tabs = document.getElementsByClassName("tab");
        for (var i = 0; i < tabs.length; i++) {
            tabs[i].classList.remove("active");
        }

        // Show the current tab and add "active" class to the button that opened the tab
        document.getElementById(tabName).classList.add("active");
        evt.currentTarget.classList.add("active");
    }

    function previewImage(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function(e) {
                document.getElementById('profile-preview').src = e.target.result;
            }

            reader.readAsDataURL(input.files[0]);
        }
    }

    function validateForm() {
        let isValid = true;
        let emailChanged = false;
        let passwordChanged = false;

        // Reset error messages
        document.querySelectorAll('.error-message').forEach(el => {
            el.style.display = 'none';
        });

        // Validate name
        const name = document.getElementById('name').value;
        if (!name || name.trim().length < 2) {
            document.getElementById('name-error').style.display = 'block';
            isValid = false;
        }

        // Check if email has changed
        const email = document.getElementById('email').value;
        const originalEmail = "<%= user != null ? user.getEmail() : "" %>";
        if (email !== originalEmail) {
            emailChanged = true;
        }

        // Validate email format
        if (email) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                document.getElementById('email-error').style.display = 'block';
                isValid = false;
            }
        } else {
            document.getElementById('email-error').style.display = 'block';
            isValid = false;
        }

        // Validate phone if provided
        const phone = document.getElementById('phone').value;
        if (phone && !(/^\d{10,15}$/.test(phone.replace(/[\s-]/g, '')))) {
            document.getElementById('phone-error').style.display = 'block';
            isValid = false;
        }

        // Check if password fields are filled
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        if (newPassword || confirmPassword) {
            passwordChanged = true;

            // Validate password length
            if (newPassword.length < 8) {
                document.getElementById('newPassword-error').style.display = 'block';
                isValid = false;
            }

            // Validate password match
            if (newPassword !== confirmPassword) {
                document.getElementById('confirmPassword-error').style.display = 'block';
                isValid = false;
            }
        }

        // Require current password if email or password is changed
        const currentPassword = document.getElementById('currentPassword').value;
        if ((emailChanged || passwordChanged) && !currentPassword) {
            document.getElementById('currentPassword-error').style.display = 'block';
            isValid = false;
        }

        // Validate specialty
        const specialty = document.getElementById('specialty').value;
        if (!specialty) {
            document.getElementById('specialty-error').style.display = 'block';
            isValid = false;
        }

        // Validate experience
        const experience = document.getElementById('experience').value;
        if (!experience || isNaN(experience) || parseInt(experience) < 0) {
            document.getElementById('experience-error').style.display = 'block';
            isValid = false;
        }

        // Validate department
        const departmentId = document.getElementById('departmentId').value;
        if (!departmentId) {
            document.getElementById('departmentId-error').style.display = 'block';
            isValid = false;
        }

        return isValid;
    }
</script>

</body>
</html>