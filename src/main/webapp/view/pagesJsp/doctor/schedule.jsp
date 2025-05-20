<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Doctor Schedule</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/schedule.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;

        }
    </style>

    <script>
        const daysOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
        const timeSlots = [
            "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM",
            "12:00 PM", "12:30 PM", "01:00 PM", "01:30 PM", "02:00 PM", "02:30 PM",
            "03:00 PM", "03:30 PM", "04:00 PM", "04:30 PM", "05:00 PM", "05:30 PM"
        ];

        let schedule = {};

        function initSchedule() {
            const saved = localStorage.getItem("doctor_schedule");
            if (saved) {
                schedule = JSON.parse(saved);
            } else {
                daysOfWeek.forEach(day => {
                    schedule[day] = { isAvailable: false, slots: [] };
                });
            }
            renderSchedule();
        }

        function toggleDayAvailability(day) {
            schedule[day].isAvailable = !schedule[day].isAvailable;
            if (!schedule[day].isAvailable) {
                schedule[day].slots = [];
            }
            renderSchedule();
        }

        function toggleTimeSlot(day, slot) {
            const slots = schedule[day].slots;
            const index = slots.indexOf(slot);
            if (index > -1) {
                slots.splice(index, 1);
            } else {
                slots.push(slot);
            }
            renderSchedule();
        }

        function saveSchedule() {
            localStorage.setItem("doctor_schedule", JSON.stringify(schedule));
            document.getElementById("success-alert").style.display = "block";
            setTimeout(() => {
                document.getElementById("success-alert").style.display = "none";
            }, 3000);
        }

        function renderSchedule() {
            const container = document.getElementById("schedule-grid");
            container.innerHTML = "";
            daysOfWeek.forEach(day => {
                const dayDiv = document.createElement("div");
                dayDiv.className = "day-schedule";

                const headerDiv = document.createElement("div");
                headerDiv.className = "day-header";

                const checkbox = document.createElement("input");
                checkbox.type = "checkbox";
                checkbox.checked = schedule[day].isAvailable;
                checkbox.onchange = () => toggleDayAvailability(day);

                const label = document.createElement("label");
                label.className = "day-toggle";
                label.appendChild(checkbox);
                label.appendChild(document.createTextNode(day));

                headerDiv.appendChild(label);
                dayDiv.appendChild(headerDiv);

                if (schedule[day].isAvailable) {
                    const slotContainer = document.createElement("div");
                    slotContainer.className = "time-slots";

                    timeSlots.forEach(slot => {
                        const slotLabel = document.createElement("label");
                        slotLabel.className = "time-slot-toggle";

                        const slotCheckbox = document.createElement("input");
                        slotCheckbox.type = "checkbox";
                        slotCheckbox.checked = schedule[day].slots.includes(slot);
                        slotCheckbox.onchange = () => toggleTimeSlot(day, slot);

                        slotLabel.appendChild(slotCheckbox);
                        slotLabel.appendChild(document.createTextNode(slot));
                        slotContainer.appendChild(slotLabel);
                    });

                    dayDiv.appendChild(slotContainer);
                }

                container.appendChild(dayDiv);
            });
        }

        window.onload = initSchedule;
    </script>
</head>
<body class="schedule-page">

<%-- Navbar --%>
<jsp:include page="/view/pagesJsp/doctor/doctorNav.jsp" />


<div class="schedule-container">
    <div class="schedule-header">
        <h1>Manage Your Schedule</h1>
        <p>Set your availability and working hours</p>
    </div>

    <div id="success-alert" class="alert success" style="display: none;">
        <span>Schedule saved successfully!</span>
        <button onclick="document.getElementById('success-alert').style.display='none'">×</button>
    </div>

    <div class="card schedule-card">
        <div class="schedule-instructions">
            <h3>Instructions</h3>
            <ul>
                <li>Select the days you are available for appointments</li>
                <li>For each available day, select the time slots when you can see patients</li>
                <li>Click "Save Schedule" when you're done</li>
            </ul>
        </div>

        <div id="schedule-grid" class="schedule-grid">
        </div>

        <div class="schedule-actions">
            <button class="btn" onclick="saveSchedule()">Save Schedule</button>
        </div>
    </div>
</div>

<%-- Footer Include --%>

<jsp:include page="/view/pagesJsp/footer.jsp" />


</body>
</html>
