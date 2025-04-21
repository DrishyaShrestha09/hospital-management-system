<nav class="navbar">
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
                <a href="${pageContext.request.contextPath}/view/pagesJsp/login.jsp" class="nav-link">Login</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/view/pagesJsp/signup.jsp" class="nav-link">Signup</a>
            </li>
            <%--Remove this from here and add in doctor and patient nav bar--%>
<%--            <li class="nav-item">--%>
<%--                <a href= "${pageContext.request.contextPath}/auth/logout" class="nav-link">Logout</a>--%>
<%--            </li>--%>
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
