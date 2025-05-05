<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/profiles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
    <style>
        .error-message {
            color: #dc3545;
            font-size: 0.9em;
            margin-top: 5px;
            display: none;
        }
        .alert {
            padding: 10px;
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
    </style>
</head>
<body>
<jsp:include page="/view/pagesJsp/patient/patientNav.jsp" />

<div class="profile-page">
    <div class="profile-container">
        <div class="profile-header animate-fade-in">
            <h1>Patient Profile</h1>
            <p>Manage your personal and medical information</p>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                    ${successMessage}
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                    ${errorMessage}
            </div>
        </c:if>

        <div class="profile-content animate-slide-up">
            <div class="profile-card">
                <div class="profile-avatar">
                    <img src="/placeholder.svg?height=150&width=150" alt="Profile" class="avatar-image" />
                    <button type="button" class="btn-outline" id="change-avatar-btn">Change Photo</button>
                </div>

                <div class="profile-details">
                    <form action="${pageContext.request.contextPath}/PatientProfileServlet" method="POST" id="patientProfileForm" onsubmit="return validateForm()">
                        <div class="form-row">
                            <label for="name">Full Name</label>
                            <input type="text" id="name" name="name" value="${patient.name}" required>
                            <div class="error-message" id="name-error">Name is required and must be between 2-50 characters</div>

                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" value="${patient.email}" disabled required>
                        </div>

                        <div class="form-row">
                            <label for="phone">Phone Number</label>
                            <input type="text" id="phone" name="phone" value="${patient.phone}">
                            <div class="error-message" id="phone-error">Phone number must be 10-15 digits</div>

                            <label for="dateOfBirth">Date of Birth</label>
                            <input type="date" id="dateOfBirth" name="dateOfBirth" value="${medicalInfo.dateOfBirth}">
                        </div>

                        <div class="form-row">
                            <label for="gender">Gender</label>
                            <select id="gender" name="gender">
                                <option value="male" ${patient.gender == 'male' ? 'selected' : ''}>Male</option>
                                <option value="female" ${patient.gender == 'female' ? 'selected' : ''}>Female</option>
                                <option value="other" ${patient.gender == 'other' ? 'selected' : ''}>Other</option>
                                <option value="prefer-not-to-say" ${patient.gender == 'prefer-not-to-say' ? 'selected' : ''}>Prefer not to say</option>
                            </select>

                            <label for="bloodGroup">Blood Group</label>
                            <select id="bloodGroup" name="bloodGroup">
                                <option value="A+" ${medicalInfo.bloodGroup == 'A+' ? 'selected' : ''}>A+</option>
                                <option value="A-" ${medicalInfo.bloodGroup == 'A-' ? 'selected' : ''}>A-</option>
                                <option value="B+" ${medicalInfo.bloodGroup == 'B+' ? 'selected' : ''}>B+</option>
                                <option value="B-" ${medicalInfo.bloodGroup == 'B-' ? 'selected' : ''}>B-</option>
                                <option value="AB+" ${medicalInfo.bloodGroup == 'AB+' ? 'selected' : ''}>AB+</option>
                                <option value="AB-" ${medicalInfo.bloodGroup == 'AB-' ? 'selected' : ''}>AB-</option>
                                <option value="O+" ${medicalInfo.bloodGroup == 'O+' ? 'selected' : ''}>O+</option>
                                <option value="O-" ${medicalInfo.bloodGroup == 'O-' ? 'selected' : ''}>O-</option>
                            </select>
                        </div>

                        <div class="form-row full-width">
                            <label for="address">Address</label>
                            <input type="text" id="address" name="address" value="${patient.address}">
                        </div>

                        <div class="form-row">
                            <label for="emergencyContact">Emergency Contact</label>
                            <input type="text" id="emergencyContact" name="emergencyContact" value="${medicalInfo.emergencyContact}" placeholder="Name and phone number">
                        </div>

                        <div class="form-row full-width">
                            <label for="allergies">Allergies</label>
                            <textarea id="allergies" name="allergies">${medicalInfo.allergies}</textarea>
                        </div>

                        <div class="form-row full-width">
                            <label for="medicalConditions">Medical Conditions</label>
                            <textarea id="medicalConditions" name="medicalConditions">${medicalInfo.medicalConditions}</textarea>
                        </div>

                        <div class="profile-actions">
                            <button type="submit" class="btn-primary">Save Changes</button>
                            <button type="button" class="btn-outline" id="cancel-btn" onclick="window.location.href='${pageContext.request.contextPath}/PatientProfileServlet'">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/view/pagesJsp/footer.jsp" />
</div>

<script>
    function validateForm() {
        let isValid = true;

        // Reset error messages
        document.querySelectorAll('.error-message').forEach(el => {
            el.style.display = 'none';
        });

        // Validate name
        const name = document.getElementById('name').value.trim();
        if (!name || name.length < 2 || name.length > 50) {
            document.getElementById('name-error').style.display = 'block';
            isValid = false;
        }

        // Validate phone if provided
        const phone = document.getElementById('phone').value.trim();
        if (phone && !phone.match(/^\d{10,15}$/)) {
            document.getElementById('phone-error').style.display = 'block';
            isValid = false;
        }

        return isValid;
    }

    // Handle change photo button
    document.getElementById('change-avatar-btn').addEventListener('click', function() {
        alert('Photo upload functionality will be implemented soon.');
    });
</script>

</body>
</html>