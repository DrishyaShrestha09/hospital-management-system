<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Book Appointment</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/appointment.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
    <script defer src="scripts/book-appointment.js"></script>
</head>
<body>

<jsp:include page="/view/pagesJsp/navbar.jsp" />

<div class="book-appointment-page">
    <div class="book-appointment-container">
        <div class="book-appointment-header">
            <h1>Book an Appointment</h1>
            <p>Schedule a visit with one of our healthcare professionals</p>
        </div>

        <div id="alert-container"></div>

        <div class="card book-appointment-card">
            <div class="appointment-steps">
                <div class="step active" id="step-1">
                    <div class="step-number">1</div>
                    <div class="step-text">Select Doctor & Date</div>
                </div>
                <div class="step-connector"></div>
                <div class="step" id="step-2">
                    <div class="step-number">2</div>
                    <div class="step-text">Choose Time Slot</div>
                </div>
                <div class="step-connector"></div>
                <div class="step" id="step-3">
                    <div class="step-number">3</div>
                    <div class="step-text">Appointment Details</div>
                </div>
            </div>

            <form id="appointment-form">
                <%-- Step 1 --%>
                <div class="appointment-step-content" id="form-step-1">
                    <h3>Select a Doctor</h3>
                    <div class="doctors-grid">
                        <c:forEach var="doctor" items="${doctors}">
                            <div class="doctor-card" data-id="${doctor.id}" data-name="${doctor.name}">
                                <div class="doctor-image">
                                    <img src="${doctor.image}" alt="${doctor.name}" />
                                </div>
                                <h4>${doctor.name}</h4>
                                <p>${doctor.specialty}</p>
                            </div>
                        </c:forEach>
                    </div>

                    <label for="date">Select Date</label>
                    <input type="date" name="date" id="date" required min="<%= java.time.LocalDate.now() %>"/>

                    <div class="step-actions">
                        <button type="button" onclick="nextStep()">Next</button>
                    </div>
                </div>

                <%-- Step 2 (hidden initially) --%>
                <div class="appointment-step-content hidden" id="form-step-2">
                    <h3>Select a Time Slot</h3>
                    <p id="time-slot-info"></p>
                    <div class="time-slots-grid" id="time-slots-container"></div>

                    <div class="step-actions">
                        <button type="button" onclick="prevStep()">Back</button>
                        <button type="button" onclick="nextStep()">Next</button>
                    </div>
                </div>

                <%-- Step 3 (hidden initially) --%>
                <div class="appointment-step-content hidden" id="form-step-3">
                    <h3>Appointment Summary</h3>
                    <div id="appointment-summary"></div>

                    <label for="reason">Reason for Visit <span class="required">*</span></label>
                    <textarea id="reason" name="reason" required rows="4"
                              placeholder="Please describe your symptoms or reason for the appointment in detail"></textarea>
                    <p class="input-help">Minimum 10 characters required</p>

                    <div class="step-actions">
                        <button type="button" onclick="prevStep()">Back</button>
                        <button type="button" onclick="confirmBooking()">Confirm Booking</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>


<jsp:include page="/view/pagesJsp/footer.jsp" />
</body>
</html>
