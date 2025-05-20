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
        /* Clean button style — Discord inspired */
        button {
            background-color: white;
            color: black;
            border: none;
            padding: 12px 28px;
            border-radius: 6px;
            font-weight: normal;
            font-size: 16px;
            cursor: pointer;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            user-select: none;
            transition: background-color 0.3s ease, color 0.3s ease, box-shadow 0.3s ease;
        }

        /* Hover effect: Discord blurple bg, white text, remove shadow */
        button:hover {
            background-color: #5865F2;
            color: white;
            box-shadow: none;
        }

        button:focus {
            outline: 2px solid #5865F2;
            outline-offset: 2px;
        }

        button:disabled,
        button[disabled] {
            background-color: #a9a9a9;
            color: #555;
            cursor: not-allowed;
            box-shadow: none;
        }



        .time-slot {
            background-color: white;
            color: black;
            padding: 12px 30px;
            margin: 5px;
            border-radius: 6px;
            font-weight: normal;
            font-size: 16px;
            min-width: 110px;
            cursor: pointer;
            user-select: none;
            transition: background-color 0.3s ease, color 0.3s ease, box-shadow 0.3s ease;
            box-shadow: 0 0 5px 0 rgba(0, 0, 0, 0.1);
        }

        .time-slot:hover {
            background-color: #5865F2;
            color: white;
            box-shadow: none;
        }

        .time-slot.selected {
            background-color: #5865F2;
            color: white;
            box-shadow: 0 0 8px 2px rgba(30, 30, 30, 0.4);
        }
        /* Label style */
        label {
            display: block;
            margin-bottom: 8px;
            font-size: 16px;
            color: #000;
            font-weight: 600;
        }

        /* Date input styling */
        input[type="date"] {
            padding: 12px 16px;
            border: none;
            border-radius: 6px;
            background-color: white;
            color: black;
            font-size: 16px;
            box-shadow: 0 0 6px rgba(0,0,0,0.15);
            transition: box-shadow 0.3s ease, background-color 0.3s ease;
            cursor: pointer;
            appearance: none;
            -webkit-appearance: none;
            -moz-appearance: none;
        }

        /* On focus */
        input[type="date"]:focus {
            outline: 2px solid #5865F2;
            box-shadow: 0 0 10px rgba(88, 101, 242, 0.4);
        }

        /* When disabled */
        input[type="date"]:disabled {
            background-color: #f0f0f0;
            color: #999;
            cursor: not-allowed;
            box-shadow: none;
        }

        #form-step-3 {
            margin-top: 20px;
        }

        #form-step-3 h3 {
            font-weight: 600;
            color: #2c2f33;
            margin-bottom: 12px;
        }

        #form-step-3 textarea {
            width: 100%;
            min-height: 120px;
            padding: 12px 15px;
            border-radius: 6px;
            border: 1px solid #ccc;
            font-size: 16px;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            resize: vertical;
            box-shadow: 0 0 5px rgba(0,0,0,0.05);
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        #form-step-3 textarea:focus {
            border-color: #5865F2; /* Discord blurple */
            box-shadow: 0 0 8px rgba(88, 101, 242, 0.6);
            outline: none;
        }

        #form-step-3 .step-actions {
            margin-top: 15px;
            display: flex;
            gap: 10px;
            justify-content: flex-start;
        }

        #form-step-3 .step-actions button {
            background-color: white;
            color: black;
            border: none;
            padding: 12px 28px;
            border-radius: 6px;
            font-weight: normal;
            font-size: 16px;
            cursor: pointer;
            box-shadow: 0 0 5px rgba(0,0,0,0.1);
            user-select: none;
            transition: background-color 0.3s ease, color 0.3s ease, box-shadow 0.3s ease;
        }

        #form-step-3 .step-actions button:hover {
            background-color: #5865F2;
            color: white;
            box-shadow: none;
        }

        #form-step-3 .step-actions button:focus {
            outline: 2px solid #5865F2;
            outline-offset: 2px;
        }

        #form-step-3 .step-actions button:disabled {
            background-color: #ccc;
            color: #666;
            cursor: not-allowed;
            box-shadow: none;
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
