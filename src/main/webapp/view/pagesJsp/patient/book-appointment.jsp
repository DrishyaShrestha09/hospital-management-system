<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.example.hospital_management_system.model.Doctor" %>
<!DOCTYPE html>
<html>
<head>
    <title>Book Appointment</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/appointment.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;

        }
        .doctor-card.selected {
            border: 2px solid #007bff;
            background-color: #f0f8ff;
        }
        .time-slot.selected {
            background-color: #007bff;
            color: white;
        }
        .message {
            padding: 10px;
            margin: 15px 0;
            border-radius: 4px;
            font-weight: bold;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
        }
        .hidden {
            display: none;
        }
        /* Added styles for doctor name */
        .doctor-name {
            font-weight: bold;
            margin-bottom: 5px;
            color: #333;
        }
        .doctor-specialty {
            color: #555;
            font-style: italic;
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
<jsp:include page="/view/pagesJsp/patient/patientNav.jsp"/>
<div class="book-appointment-page">
    <div class="book-appointment-container">
        <div class="book-appointment-header">
            <h1>Book an Appointment</h1>
            <p>Schedule a visit with one of our healthcare professionals</p>
        </div>
        <div class="card book-appointment-card">
            <!-- Show success or error messages -->
            <% String successMsg = (String) request.getAttribute("message");
                String errorMsg = (String) request.getAttribute("error");
                // For session messages after redirect (optional)
                if (successMsg == null && session.getAttribute("message") != null) {
                    successMsg = (String) session.getAttribute("message");
                    session.removeAttribute("message");
                }
                if (errorMsg == null && session.getAttribute("error") != null) {
                    errorMsg = (String) session.getAttribute("error");
                    session.removeAttribute("error");
                }
                if (successMsg != null) { %>
            <div class="message success"><%= successMsg %></div>
            <% } else if (errorMsg != null) { %>
            <div class="message error"><%= errorMsg %></div>
            <% } %>
            <form id="appointment-form" method="post" action="${pageContext.request.contextPath}/BookAppointmentServlet">
                <!-- Step 1 -->
                <div class="appointment-step-content" id="form-step-1">
                    <h3>Select a Doctor</h3>
                    <div class="doctors-grid">
                        <% List<Map<String, Object>> doctors = (List<Map<String, Object>>) request.getAttribute("doctorsWithNames");
                            if (doctors != null && !doctors.isEmpty()) {
                                for (Map<String, Object> doctor : doctors) { %>
                        <div class="doctor-card" data-id="<%= doctor.get("doctorId") %>" data-name="<%= doctor.get("name") %>">
                            <h4 class="doctor-name">Dr. <%= doctor.get("name") %></h4>
                            <p class="doctor-specialty"><%= doctor.get("specialty") %></p>
                            <p>Experience: <%= doctor.get("experience") %> years</p>
                        </div>
                        <% }
                        } else { %>
                        <p>No doctors available.</p>
                        <% } %>
                    </div>
                    <label for="date">Select Date</label>
                    <input type="date" name="date" id="date" required min="<%= java.time.LocalDate.now() %>"/>
                    <div class="step-actions">
                        <button type="button" onclick="nextStep()">Next</button>
                    </div>
                </div>
                <!-- Step 2 -->
                <div class="appointment-step-content hidden" id="form-step-2">
                    <h3>Select a Time Slot</h3>
                    <div class="time-slots-grid" id="time-slots-container">
                        <button type="button" class="time-slot" data-time="9:00 AM">9:00 AM</button>
                        <button type="button" class="time-slot" data-time="10:00 AM">10:00 AM</button>
                        <button type="button" class="time-slot" data-time="11:00 AM">11:00 AM</button>
                        <button type="button" class="time-slot" data-time="2:00 PM">2:00 PM</button>
                        <button type="button" class="time-slot" data-time="3:00 PM">3:00 PM</button>
                    </div>
                    <div class="step-actions">
                        <button type="button" onclick="prevStep()">Back</button>
                        <button type="button" onclick="nextStep()">Next</button>
                    </div>
                </div>
                <!-- Step 3 -->
                <div class="appointment-step-content hidden" id="form-step-3">
                    <h3>Reason for Appointment</h3>
                    <textarea name="reason" required placeholder="Why do you need to see the doctor?"></textarea>
                    <div class="step-actions">
                        <button type="button" onclick="prevStep()">Back</button>
                        <button type="submit">Confirm Appointment</button>
                    </div>
                </div>
                <!-- Hidden Inputs -->
                <input type="hidden" name="doctorId" id="doctorId"/>
                <input type="hidden" name="timeSlot" id="timeSlot"/>
                <input type="hidden" name="doctorName" id="doctorName"/>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/view/pagesJsp/footer.jsp"/>
<script>
    let currentStep = 1;
    function nextStep() {
        if (currentStep === 1) {
            const selectedDoctor = document.querySelector('.doctor-card.selected');
            if (selectedDoctor) {
                document.getElementById('doctorId').value = selectedDoctor.getAttribute('data-id');
                document.getElementById('doctorName').value = selectedDoctor.getAttribute('data-name');
                document.getElementById('form-step-1').classList.add('hidden');
                document.getElementById('form-step-2').classList.remove('hidden');
                currentStep++;
            } else {
                alert("Please select a doctor.");
            }
        } else if (currentStep === 2) {
            const selectedTimeSlot = document.querySelector('.time-slot.selected');
            if (selectedTimeSlot) {
                document.getElementById('timeSlot').value = selectedTimeSlot.getAttribute('data-time');
                document.getElementById('form-step-2').classList.add('hidden');
                document.getElementById('form-step-3').classList.remove('hidden');
                currentStep++;
            } else {
                alert("Please select a time slot.");
            }
        }
    }
    function prevStep() {
        if (currentStep === 2) {
            document.getElementById('form-step-2').classList.add('hidden');
            document.getElementById('form-step-1').classList.remove('hidden');
            currentStep--;
        } else if (currentStep === 3) {
            document.getElementById('form-step-3').classList.add('hidden');
            document.getElementById('form-step-2').classList.remove('hidden');
            currentStep--;
        }
    }
    window.onload = function () {
        // Doctor selection
        document.querySelectorAll('.doctor-card').forEach(card => {
            card.addEventListener('click', function () {
                document.querySelectorAll('.doctor-card').forEach(c => c.classList.remove('selected'));
                this.classList.add('selected');
            });
        });
        // Time slot selection
        document.querySelectorAll('.time-slot').forEach(slot => {
            slot.addEventListener('click', function () {
                document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('selected'));
                this.classList.add('selected');
            });
        });
    }
</script>
</body>
</html>
