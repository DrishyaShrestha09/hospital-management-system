<nav class="navbar" style="font-family: 'Inter', sans-serif;">
    <div class="navbar-container">

        <!-- Logo -->
        <a href="${pageContext.request.contextPath}/index.jsp" class="navbar-logo">
            <img src="${pageContext.request.contextPath}" alt="" height="30" />
            <span class="logo-text">NetruDoc</span>
        </a>

        <!-- Navigation Links -->
        <ul class="nav-menu" id="navMenu">
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/index.jsp" class="nav-link">Home</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/LoginServlet" class="nav-link">Login</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/RegisterServlet" class="nav-link">Signup</a>
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
