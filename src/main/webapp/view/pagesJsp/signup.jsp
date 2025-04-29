<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign Up - Hospital Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/authPages.css">
</head>
<body>

<jsp:include page="/view/pagesJsp/navbar.jsp" />

<div class="auth-page">
    <div class="auth-container">
        <div class="auth-content">
            <form action="${pageContext.request.contextPath}/RegisterServlet" method="post" class="auth-form">
                <h2>Sign Up</h2>

                <label for="firstName">First Name:</label>
                <input type="text" id="firstName" name="firstName" required />

                <label for="lastName">Last Name:</label>
                <input type="text" id="lastName" name="lastName" required />

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required />

                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required />

                <label for="userType">Register As:</label>
                <select id="userType" name="userType" required onchange="toggleSpecialization()">
                    <option value="">-- Select Role --</option>
                    <option value="admin">Admin</option>
                    <option value="doctor">Doctor</option>
                    <option value="patient">Patient</option>
                </select>

                <!-- Hidden Specialization Field -->
                <div id="specializationField" style="display: none;">
                    <label for="specialization">Specialization:</label>
                    <input type="text" id="specialization" name="specialization" />
                </div>

                <button type="submit">Sign Up</button>

                <p>Already have an account? <a href="LoginServlet">Login</a></p>
            </form>
        </div>

        <div class="auth-image">
            <img src="https://img.freepik.com/free-vector/account-concept-illustration_114360-399.jpg?w=826&h=600"
                 alt="Sign Up" width="700" height="850" />
        </div>
    </div>
</div>

<%@ include file="/view/pagesJsp/footer.jsp" %>

<script>
    function toggleSpecialization() {
        const userType = document.getElementById("userType").value;
        const specializationField = document.getElementById("specializationField");

        if (userType === "doctor") {
            specializationField.style.display = "block";
        } else {
            specializationField.style.display = "none";
        }
    }
</script>

</body>
</html>
