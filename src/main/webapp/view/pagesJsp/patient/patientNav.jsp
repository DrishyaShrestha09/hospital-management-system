<nav class="navbar" style="font-family: 'Inter', sans-serif;">
    <div class="navbar-container">

        <!-- Logo -->
        <a href="${pageContext.request.contextPath}/PatientDashboardServlet" class="navbar-logo">
            <img src="${pageContext.request.contextPath}" alt="" height="30" />
            <span class="logo-text">NetruDoc</span>
        </a>

        <!-- Navigation Links -->
        <ul class="nav-menu" id="navMenu">
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/PatientDashboardServlet" class="nav-link">User Dashboard</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/BookAppointmentServlet" class="nav-link">Book Appointment</a>
            </li><li class="nav-item">
            <a href="${pageContext.request.contextPath}/PatientProfileServlet" class="nav-link">Profile</a>
        </li>
            <li class="nav-item">
                <a href= "${pageContext.request.contextPath}/auth/logout" class="nav-link">Logout</a>
            </li>
        </ul>

    </div>
</nav>

<!-- Responsive toggle script -->
<script>
    function toggleMenu() {
        const navMenu = document.getElementById("navMenu");
        navMenu.classList.toggle("active");
    }
</script>
