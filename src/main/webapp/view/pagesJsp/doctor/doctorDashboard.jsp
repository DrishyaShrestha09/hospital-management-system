<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/view/pagesJsp/doctor/doctorNav.jsp" />
<html>
<head>
    <title>Doctor Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
    <script src="https://kit.fontawesome.com/a076d05399.js"></script>
    <style>
        .dashboard-page {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            min-height: 100vh;
        }
        .dashboard-container {
            max-width: 1200px;
            margin: auto;
            padding: 20px;
        }
        .dashboard-header h1 {
            font-size: 2em;
            margin-bottom: 0.3em;
        }
        .dashboard-stats {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
            margin: 20px 0;
        }
        .stat-card {
            flex: 1;
            background: white;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.05);
            transition: transform 0.3s;
        }
        .stat-icon i {
            font-size: 1.5em;
            margin-bottom: 0.5em;
        }
        .appointments-list {
            margin-top: 20px;
        }
        .appointment-item {
            background: white;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .status-pending { color: orange; }
        .status-confirmed { color: green; }
        .status-cancelled { color: red; }
        .appointment-actions button {
            margin-right: 8px;
        }
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

<body class="dashboard-page">
<div class="dashboard-container">
    <div class="dashboard-header">
        <h1>Welcome, Dr. ${currentUser.name}</h1>
        <p>Manage your appointments and schedule</p>
    </div>

    <div class="dashboard-stats">
        <div class="stat-card">
            <div class="stat-icon"><i class="fas fa-calendar-day"></i></div>
            <h3>Today's Appointments</h3>
            <p class="stat-number">${todayCount}</p>
        </div>
        <div class="stat-card">
            <div class="stat-icon"><i class="fas fa-hourglass-half"></i></div>
            <h3>Pending Appointments</h3>
            <p class="stat-number">${pendingCount}</p>
        </div>
        <div class="stat-card">
            <div class="stat-icon"><i class="fas fa-users"></i></div>
            <h3>Total Patients</h3>
            <p class="stat-number">${uniquePatientCount}</p>
        </div>
        <div class="stat-card">
            <div class="stat-icon"><i class="fas fa-check-circle"></i></div>
            <h3>Completed Appointments</h3>
            <p class="stat-number">${completedCount}</p>
        </div>
    </div>

    <div class="dashboard-appointments">
        <h2>Your Appointments</h2>
        <div class="appointments-list">
            <c:forEach var="appointment" items="${appointments}">
                <div class="appointment-item">
                    <h4>Patient: ${appointment.patientName}</h4>
                    <p>Date: ${appointment.date}</p>
                    <p>Time: ${appointment.timeSlot}</p>
                    <p>Status:
                        <span class="status-${appointment.status}">
                                ${appointment.status}
                        </span>
                    </p>
                    <div class="appointment-actions">
                        <button onclick="viewDetails('${appointment.id}')">View</button>
                        <c:if test="${appointment.status == 'pending'}">
                            <button onclick="updateStatus('${appointment.id}', 'confirmed')">Confirm</button>
                            <button onclick="updateStatus('${appointment.id}', 'cancelled')">Cancel</button>
                        </c:if>
                        <c:if test="${appointment.status == 'confirmed'}">
                            <button onclick="updateStatus('${appointment.id}', 'completed')">Complete</button>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="/view/pagesJsp/footer.jsp" />
<!-- Toast Notification -->
<div id="toast" class="toast"></div>
<script>
    function updateStatus(appointmentId, newStatus) {
        // Call a backend endpoint with AJAX or form
        console.log("Updating", appointmentId, "to", newStatus);
        // You'd use fetch() or an AJAX call here to hit the servlet
    }

    function viewDetails(id) {
        // Open a modal or redirect to details page
        alert("Open details modal for appointment ID: " + id);
    }

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


