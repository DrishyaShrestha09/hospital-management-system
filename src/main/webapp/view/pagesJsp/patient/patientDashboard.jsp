<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParseException" %>

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

        .appointment-btn {
            display: inline-block;
            background-color: #007BFF;
            color: #ffffff;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 6px;
            font-weight: 600;
            transition: background-color 0.3s ease;
        }

        .appointment-btn:hover {
            background-color: #0056b3;
        }

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

        .status-pending { color: orange; }
        .status-confirmed { color: green; }
        .status-cancelled { color: red; }
        .status-completed { color: blue; }

        .appointment-item {
            background: white;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .appointment-info {
            flex: 1;
        }

        .appointment-actions {
            display: flex;
            gap: 10px;
        }

        .btn-outline {
            padding: 6px 12px;
            background-color: transparent;
            color: #007bff;
            border: 1px solid #007bff;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }

        .btn-outline:hover {
            background-color: #f0f8ff;
        }

        .btn-danger {
            padding: 6px 12px;
            background-color: #dc3545;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }

        .btn-danger:hover {
            background-color: #c82333;
        }

        .btn-small {
            font-size: 0.9em;
        }

        /* Confirmation Modal */
        #confirmationModal .modal-content {
            width: 400px;
        }

        #confirmationModal .modal-footer {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
        }

        #confirmationModal .btn-secondary {
            background-color: #6c757d;
        }

        #confirmationModal .btn-secondary:hover {
            background-color: #5a6268;
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
        Date apptDate = null;
        String dateStr = appt.get("date");
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                apptDate = sdf.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Handle null or empty date (you could choose to skip or assign a default value)
            continue; // This skips the current appointment if the date is invalid
        }

        String status = appt.get("status");

        if ("cancelled".equals(status)) {
            cancelled++;
        } else if ("completed".equals(status)) {
            completed++;
        } else if (apptDate.compareTo(today) >= 0) {
            upcoming++;
        }
    }

    // Check for cancel success message
    String cancelMessage = (String) request.getAttribute("cancelMessage");
    if (cancelMessage == null) {
        cancelMessage = (String) session.getAttribute("cancelMessage");
        if (cancelMessage != null) {
            session.removeAttribute("cancelMessage");
        }
    }
%>

<div class="dashboard-container">
    <div class="dashboard-header">
        <h1>Welcome, ${currentUser.name}</h1>
        <p>Manage your appointments and health records</p>
    </div>

    <div class="dashboard-actions">
        <a href="BookAppointmentServlet" class="appointment-btn">Book New Appointment</a>
    </div>

    <% if (cancelMessage != null) { %>
    <div class="message success" style="background-color: #d4edda; color: #155724; padding: 10px; margin: 15px 0; border-radius: 4px;">
        <%= cancelMessage %>
    </div>
    <% } %>

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

    <h2>Your Appointments</h2>
    <div class="appointments-list">
        <% for (Map<String, String> appt : appointments) {
            String id = appt.get("id");
            String doctor = appt.get("doctorName");
            String date = appt.get("date");
            String time = appt.get("timeSlot");
            String status = appt.get("status");
            String cause = appt.get("cause");
        %>
        <div class="appointment-item">
            <div class="appointment-info">
                <h4>Dr. <%= doctor %></h4>
                <p>Date: <%= date %></p>
                <p>Time: <%= time %></p>
                <p>Status: <span class="status-<%= status %>"><%= status %></span></p>
            </div>
            <div class="appointment-actions">
                <button onclick="viewDetails('<%= id %>', '<%= doctor %>', '<%= date %>', '<%= time %>', '<%= cause %>')">View</button>
                <% if ("pending".equals(status) || "confirmed".equals(status)) { %>
                <button class="btn-danger btn-small" onclick="confirmCancel('<%= id %>')">Cancel</button>
                <% } else if ("cancelled".equals(status)) { %>
                <a href="BookAppointmentServlet" class="btn-outline btn-small">Book Again</a>
                <% } %>
            </div>
        </div>
        <% } %>
    </div>
</div>

<!-- Appointment Details Modal -->
<div id="appointmentModal" class="modal">
    <div class="modal-content">
        <span class="close-modal" onclick="closeModal('appointmentModal')">&times;</span>
        <div class="modal-header">
            <h2>Appointment Details</h2>
        </div>
        <div class="modal-body">
            <p><span class="label">Appointment ID:</span> <span id="modal-appointment-id"></span></p>
            <p><span class="label">Doctor:</span> <span id="modal-doctor-name"></span></p>
            <p><span class="label">Date:</span> <span id="modal-date"></span></p>
            <p><span class="label">Time:</span> <span id="modal-time"></span></p>
            <p><span class="label">Reason:</span> <span id="modal-reason"></span></p>
        </div>
        <div class="modal-footer">
            <button onclick="closeModal('appointmentModal')">Close</button>
        </div>
    </div>
</div>

<!-- Confirmation Modal -->
<div id="confirmationModal" class="modal">
    <div class="modal-content">
        <span class="close-modal" onclick="closeModal('confirmationModal')">&times;</span>
        <div class="modal-header">
            <h2>Confirm Cancellation</h2>
        </div>
        <div class="modal-body">
            <p>Are you sure you want to cancel this appointment?</p>
            <p>This action cannot be undone.</p>
        </div>
        <div class="modal-footer">
            <button class="btn-secondary" onclick="closeModal('confirmationModal')">No, Keep It</button>
            <button class="btn-danger" id="confirmCancelButton">Yes, Cancel Appointment</button>
        </div>
    </div>
</div>

<!-- Toast Notification -->
<div id="toast" class="toast"></div>

<jsp:include page="/view/pagesJsp/footer.jsp" />
<script>
    // Get the modals
    const appointmentModal = document.getElementById("appointmentModal");
    const confirmationModal = document.getElementById("confirmationModal");

    // Function to display appointment details in the modal
    function viewDetails(id, doctorName, date, timeSlot, cause) {
        document.getElementById("modal-appointment-id").textContent = id;
        document.getElementById("modal-doctor-name").textContent = "Dr. " + doctorName;
        document.getElementById("modal-date").textContent = date;
        document.getElementById("modal-time").textContent = timeSlot;
        document.getElementById("modal-reason").textContent = cause;

        // Display the modal
        appointmentModal.style.display = "block";
    }

    // Function to confirm appointment cancellation
    function confirmCancel(appointmentId) {
        // Set up the confirmation button to call cancelAppointment with the correct ID
        document.getElementById("confirmCancelButton").onclick = function() {
            cancelAppointment(appointmentId);
        };

        // Display the confirmation modal
        confirmationModal.style.display = "block";
    }

    // Function to cancel an appointment
    function cancelAppointment(appointmentId) {
        // Close the confirmation modal
        closeModal('confirmationModal');

        // Show a loading message
        showToast("Cancelling appointment...");

        // Send the cancellation request to the server
        fetch("${pageContext.request.contextPath}/CancelAppointmentServlet?appointmentId=" + appointmentId, {
            method: 'POST'
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Network response was not ok.');
            })
            .then(data => {
                if (data.success) {
                    showToast("Appointment cancelled successfully!");
                    // Reload the page after a short delay to show the updated list
                    setTimeout(() => {
                        window.location.reload();
                    }, 1500);
                } else {
                    showToast("Failed to cancel appointment: " + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showToast("An error occurred. Please try again.");
            });
    }

    // Function to close a modal by ID
    function closeModal(modalId) {
        document.getElementById(modalId).style.display = "none";
    }

    // Function to show a toast message
    function showToast(message) {
        const toast = document.getElementById("toast");
        toast.textContent = message;
        toast.className = "toast show";
        setTimeout(function () {
            toast.className = toast.className.replace("show", "");
        }, 3000);
    }

    // Close the modals when clicking outside of them
    window.onclick = function(event) {
        if (event.target == appointmentModal) {
            closeModal('appointmentModal');
        } else if (event.target == confirmationModal) {
            closeModal('confirmationModal');
        }
    }

    // Check for login success message
    const params = new URLSearchParams(window.location.search);
    if (params.get("login") === "success") {
        showToast("Login successful!");
    }

    // Check for cancellation success message
    <% if (cancelMessage != null) { %>
    // Scroll to the success message
    window.onload = function() {
        document.querySelector('.message.success').scrollIntoView({ behavior: 'smooth', block: 'center' });
    };
    <% } %>
</script>
</body>
</html>
