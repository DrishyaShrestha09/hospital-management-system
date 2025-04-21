<%@ page contentType="text/html; charset=UTF-8" language="java" %>


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
            <div class="alert success">
                    ${successMessage}
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert error">
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
                    <form action="updateProfile" method="POST">
                        <div class="form-row">
                            <label for="name">Full Name</label>
                            <input type="text" id="name" name="name" value="${patient.name}" required>

                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" value="${patient.email}" disabled required>
                        </div>

                        <div class="form-row">
                            <label for="phone">Phone Number</label>
                            <input type="text" id="phone" name="phone" value="${patient.phone}">

                            <label for="dateOfBirth">Date of Birth</label>
                            <input type="date" id="dateOfBirth" name="dateOfBirth" value="${patient.dateOfBirth}">
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
                                <option value="A+" ${patient.bloodGroup == 'A+' ? 'selected' : ''}>A+</option>
                                <option value="A-" ${patient.bloodGroup == 'A-' ? 'selected' : ''}>A-</option>
                                <option value="B+" ${patient.bloodGroup == 'B+' ? 'selected' : ''}>B+</option>
                                <option value="B-" ${patient.bloodGroup == 'B-' ? 'selected' : ''}>B-</option>
                                <option value="AB+" ${patient.bloodGroup == 'AB+' ? 'selected' : ''}>AB+</option>
                                <option value="AB-" ${patient.bloodGroup == 'AB-' ? 'selected' : ''}>AB-</option>
                                <option value="O+" ${patient.bloodGroup == 'O+' ? 'selected' : ''}>O+</option>
                                <option value="O-" ${patient.bloodGroup == 'O-' ? 'selected' : ''}>O-</option>
                            </select>
                        </div>

                        <div class="form-row full-width">
                            <label for="address">Address</label>
                            <input type="text" id="address" name="address" value="${patient.address}">
                        </div>

                        <div class="form-row">
                            <label for="emergencyContact">Emergency Contact</label>
                            <input type="text" id="emergencyContact" name="emergencyContact" value="${patient.emergencyContact}" placeholder="Name and phone number">
                        </div>

                        <div class="form-row full-width">
                            <label for="allergies">Allergies</label>
                            <textarea id="allergies" name="allergies">${patient.allergies}</textarea>
                        </div>

                        <div class="form-row full-width">
                            <label for="medicalConditions">Medical Conditions</label>
                            <textarea id="medicalConditions" name="medicalConditions">${patient.medicalConditions}</textarea>
                        </div>

                        <div class="profile-actions">
                            <button type="submit" class="btn-primary">Save Changes</button>
                            <button type="button" class="btn-outline" id="cancel-btn">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/view/pagesJsp/footer.jsp" />
</div>

</body>
</html>
