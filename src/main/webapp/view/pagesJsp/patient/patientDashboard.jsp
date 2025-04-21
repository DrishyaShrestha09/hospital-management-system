<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>

<html>
<head>
    <title>Patient Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/appointment.css">
    <style>
        .toast {
            visibility: hidden;
            min-width: 250px;
            margin-left: -125px;
            background-color: #4CAF50;
            color: white;
            text-align: center;
            border-radius: 2px;
            padding: 16px;
            position: fixed;
            z-index: 1;
            left: 50%;
            bottom: 30px;
            font-size: 17px;
        }
        .toast.show {
            visibility: visible;
            animation: fadein 0.5s, fadeout 0.5s 2.5s;
        }
        @keyframes fadein {
            from {bottom: 0; opacity: 0;}
            to {bottom: 30px; opacity: 1;}
        }
        @keyframes fadeout {
            from {bottom: 30px; opacity: 1;}
            to {bottom: 0; opacity: 0;}
        }

    </style>
</head>
<body>

<jsp:include page="/view/pagesJsp/patient/patientNav.jsp" />

<%
    String userName = (String) request.getAttribute("user_name");
    List<Map<String, String>> appointments = (List<Map<String, String>>) request.getAttribute("appointments");

    if (appointments == null) {
        appointments = new ArrayList<>();
    }

    int upcoming = 0, completed = 0, cancelled = 0;
    Date today = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    for (Map<String, String> appt : appointments) {
        Date apptDate = sdf.parse(appt.get("date"));
        String status = appt.get("status");

        if ("cancelled".equals(status)) {
            cancelled++;
        } else if ("completed".equals(status)) {
            completed++;
        } else if (apptDate.compareTo(today) >= 0) {
            upcoming++;
        }
    }
%>

<div class="dashboard-container">
    <div class="dashboard-header">
        <h1>Welcome, <%= userName %></h1>
        <p>Manage your appointments and health records</p>
    </div>

    <div class="dashboard-actions">
        <a href="book-appointment.jsp" class="btn">Book New Appointment</a>
        <a href="medicalHistory.jsp" class="btn-outline">View Medical History</a>
    </div>

    <div class="dashboard-stats">
        <div class="stat-card">
            <div class="stat-icon"><i class="fas fa-calendar-check"></i></div>
            <h3>Upcoming Appointments</h3>
            <p class="stat-number"><%= upcoming %></p>
        </div>
        <div class="stat-card">
            <div class="stat-icon"><i class="fas fa-check-circle"></i></div>
            <h3>Completed Appointments</h3>
            <p class="stat-number"><%= completed %></p>
        </div>
        <div class="stat-card">
            <div class="stat-icon"><i class="fas fa-times-circle"></i></div>
            <h3>Cancelled Appointments</h3>
            <p class="stat-number"><%= cancelled %></p>
        </div>
    </div>

    <div class="appointments-list">
        <% for (Map<String, String> appt : appointments) {
            String id = appt.get("id");
            String doctor = appt.get("doctorName");
            String date = appt.get("date");
            String time = appt.get("timeSlot");
            String status = appt.get("status");
            String reason = appt.get("reason");
        %>
        <div class="appointment-item">
            <div class="appointment-info">
                <h4>Dr. <%= doctor %></h4>
                <p>Date: <%= date %></p>
                <p>Time: <%= time %></p>
                <p>Status: <span class="status-<%= status %>"><%= status %></span></p>
                <% if (reason != null && !reason.isEmpty()) { %>
                <p>Reason: <%= reason %></p>
                <% } %>
            </div>
            <% if ("pending".equals(status) || "confirmed".equals(status)) { %>
            <div class="appointment-actions">
                <a href="preConsultation.jsp?id=<%= id %>" class="btn-outline btn-small">Pre-Consultation Form</a>
                <a href="cancelAppointment.jsp?id=<%= id %>" class="btn-danger btn-small">Cancel</a>
            </div>
            <% } else if ("cancelled".equals(status)) { %>
            <div class="appointment-actions">
                <a href="book-appointment.jsp" class="btn-outline btn-small">Book Again</a>
            </div>
            <% } %>
        </div>
        <% } %>
    </div>
</div>

<!-- Toast Notification -->
<div id="toast" class="toast"></div>

<jsp:include page="/view/pagesJsp/footer.jsp" />
<script>
    const params = new URLSearchParams(window.location.search);
    if (params.get("login") === "success") {
        const toast = document.getElementById("toast");
        toast.textContent = "Login successful!";
        toast.className = "toast show";
        setTimeout(function () {
            toast.className = toast.className.replace("show", "");
        }, 3000);
    }
</script>
</body>
</html>
