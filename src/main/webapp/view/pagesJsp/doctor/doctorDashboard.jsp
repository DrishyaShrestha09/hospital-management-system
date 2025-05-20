<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <title>Doctor Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
    <script src="https://kit.fontawesome.com/a076d05399.js"></script>
    <style>

        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;

        }
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
            padding: 6px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            background-color: #007bff;
            color: white;
        }
        .appointment-actions button:hover {
            background-color: #0056b3;
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

        /* Modal Styles */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }

        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            width: 50%;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            animation: modalopen 0.4s;
        }

        @keyframes modalopen {
            from {opacity: 0; transform: scale(0.8);}
            to {opacity: 1; transform: scale(1);}
        }

        .close-modal {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }

        .close-modal:hover,
        .close-modal:focus {
            color: black;
            text-decoration: none;
        }

        .modal-header {
            padding-bottom: 10px;
            border-bottom: 1px solid #ddd;
            margin-bottom: 15px;
        }

        .modal-header h2 {
            margin: 0;
            color: #333;
        }

        .modal-body {
            margin-bottom: 20px;
        }

        .modal-body p {
            margin: 8px 0;
            line-height: 1.5;
        }

        .modal-body .label {
            font-weight: bold;
            color: #555;
            width: 120px;
            display: inline-block;
        }

        .modal-footer {
            padding-top: 10px;
            border-top: 1px solid #ddd;
            text-align: right;
        }

        .modal-footer button {
            padding: 8px 16px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .modal-footer button:hover {
            background-color: #0056b3;
        }
    </style>
</head>

<body class="dashboard-page">
<jsp:include page="/view/pagesJsp/doctor/doctorNav.jsp" />
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
        <c:if test="${not empty appointments}">
            <div class="appointments-list">
                <c:forEach var="appointment" items="${appointments}">
                    <div class="appointment-item">
                        <h4>Patient: ${appointment.patientName}</h4>
                        <p>Date: ${appointment.date}</p>
                        <p>Status:
                            <span class="status-${appointment.status}">
                                    ${appointment.status}
                            </span>
                        </p>
                        <div class="appointment-actions">
                            <button onclick="viewDetails('${appointment.id}', '${appointment.patientName}', '${appointment.date}', '${appointment.timeSlot}', '${appointment.cause}')">View</button>
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
        </c:if>
        <c:if test="${empty appointments}">
            <p>No appointments found.</p>
        </c:if>
    </div>
</div>

<!-- Appointment Details Modal -->
<div id="appointmentModal" class="modal">
    <div class="modal-content">
        <span class="close-modal" onclick="closeModal()">&times;</span>
        <div class="modal-header">
            <h2>Appointment Details</h2>
        </div>
        <div class="modal-body">
            <p><span class="label">Appointment ID:</span> <span id="modal-appointment-id"></span></p>
            <p><span class="label">Patient Name:</span> <span id="modal-patient-name"></span></p>
            <p><span class="label">Date:</span> <span id="modal-date"></span></p>
            <p><span class="label">Time:</span> <span id="modal-time"></span></p>
            <p><span class="label">Reason:</span> <span id="modal-reason"></span></p>
        </div>
        <div class="modal-footer">
            <button onclick="closeModal()">Close</button>
        </div>
    </div>
</div>

<jsp:include page="/view/pagesJsp/footer.jsp" />

<!-- Toast Notification -->
<div id="toast" class="toast"></div>
<script>
    function updateStatus(appointmentId, newStatus) {
        // Confirm before updating status
        let confirmMessage = `Are you sure you want to ${newStatus} this appointment?`;
        if (!confirm(confirmMessage)) {
            return;
        }

        // Create form data
        const formData = new FormData();
        formData.append('appointmentId', appointmentId);
        formData.append('status', newStatus);

        // Send the request
        fetch('${pageContext.request.contextPath}/updateAppointmentStatus', {
            method: 'POST',
            body: new URLSearchParams({
                'appointmentId': appointmentId,
                'status': newStatus
            }),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    // Show success message
                    showToast(`Appointment ${newStatus} successfully!`);
                    // Reload the page after a short delay
                    setTimeout(() => {
                        location.reload();
                    }, 1500);
                } else {
                    showToast(`Failed to update status: ${data.message}`);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showToast('An error occurred while updating the appointment status');
            });
    }

    // Get the modal
    const modal = document.getElementById("appointmentModal");

    // Function to display appointment details in the modal
    function viewDetails(id, patientName, date, timeSlot, cause) {
        document.getElementById("modal-appointment-id").textContent = id;
        document.getElementById("modal-patient-name").textContent = patientName;
        document.getElementById("modal-date").textContent = date;
        document.getElementById("modal-time").textContent = timeSlot;
        document.getElementById("modal-reason").textContent = cause;

        // Display the modal
        modal.style.display = "block";
    }

    // Function to close the modal
    function closeModal() {
        modal.style.display = "none";
    }

    // Close the modal when clicking outside of it
    window.onclick = function(event) {
        if (event.target == modal) {
            closeModal();
        }
    }

    // Function to show toast notification
    function showToast(message) {
        const toast = document.getElementById("toast");
        toast.textContent = message;
        toast.className = "toast show";
        setTimeout(function () {
            toast.className = toast.className.replace("show", "");
        }, 3000);
    }

    // Check for login success message
    const params = new URLSearchParams(window.location.search);
    if (params.get("login") === "success") {
        showToast("Login successful!");
    }
</script>
</body>
</html>