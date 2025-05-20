 <style>
        :root {
            --bg-primary: #ffffff;
            --primary-color: #007bff;
            --text-primary: #333333;
            --shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            --radius: 6px;
        }

        .navbar {
            background-color: var(--bg-primary);
            box-shadow: var(--shadow);
            position: sticky;
            top: 0;
            z-index: 1000;
            width: 100%;
        }

        .navbar-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1rem;
            max-width: 1280px;
            margin: 0 auto;
        }

        .navbar-logo {
            display: flex;
            align-items: center;
            text-decoration: none;
        }

        .logo-text {
            font-size: 1.5rem;
            font-weight: bold;
            color: var(--primary-color);
            margin-left: 0.5rem;
        }

        .nav-menu {
            display: flex;
            list-style: none;
            align-items: center;
        }

        .nav-item {
            margin-left: 1.5rem;
        }

        .nav-link {
            color: var(--text-primary);
            font-weight: 500;
            text-decoration: none;
            padding: 0.5rem 1rem;
            border-radius: var(--radius);
            transition: all 0.2s ease-in-out;
            background: transparent;
            border: none;
            cursor: pointer;
        }

        .nav-link:hover {
            color: var(--primary-color);
            background-color: rgba(0, 123, 255, 0.1);
            text-decoration: none;
        }

        .logout-btn {
            background: none;
            border: none;
            font-family: inherit;
            font-size: 1rem;
            color: var(--text-primary);
            cursor: pointer;
        }

        .logout-btn:hover {
            color: var(--primary-color);
            background-color: rgba(0, 123, 255, 0.1);
            border-radius: var(--radius);
        }
    </style>

<nav class="navbar">
    <div class="navbar-container">

        <!-- Logo -->
        <a href="${pageContext.request.contextPath}/AdminServlet" class="navbar-logo">
            <img src="${pageContext.request.contextPath}" alt="" height="30" />
            <span class="logo-text">NetruDoc</span>
        </a>

        <!-- Navigation Links -->
        <ul class="nav-menu" id="navMenu">
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/AdminServlet" class="nav-link">Dashboard</a>
            </li>
            <li class="nav-item">
                <a href= "${pageContext.request.contextPath}/auth/logout" class="nav-link">Logout</a>
            </li>
        </ul>

    </div>
</nav>

<script>
    function toggleMenu() {
        const navMenu = document.getElementById("navMenu");
        navMenu.classList.toggle("active");
    }
</script>
