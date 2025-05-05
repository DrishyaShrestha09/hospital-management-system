<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - Hospital Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/authPages.css">

    <style>
        .error-border {
            border: 1.5px solid red !important;
        }

        .error-message {
            color: red !important;
            font-size: 0.9em;
            margin: 5px 0 10px 0;
        }

        /* Toast Styles */
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


        .remember-me {
            display: flex;
            align-items: center;
            margin: 8px 0;
            cursor: pointer;
            user-select: none;
        }

        .remember-me input[type="checkbox"] {
            appearance: none;
            -webkit-appearance: none;
            background-color: #fff;
            border: 2px solid #ccc;
            padding: 8px;
            border-radius: 4px;
            display: inline-block;
            position: relative;
            cursor: pointer;
            transition: all 0.2s ease-in-out;
            width: 18px;
            height: 18px;
            margin-right: 10px;
        }

        .remember-me input[type="checkbox"]:checked {
            background-color: #4CAF50;
            border-color: #4CAF50;
        }

        .remember-me input[type="checkbox"]::after {
            content: '';
            position: absolute;
            display: none;
        }

        .remember-me input[type="checkbox"]:checked::after {
            display: block;
            left: 5px;
            top: 1px;
            width: 5px;
            height: 10px;
            border: solid white;
            border-width: 0 2px 2px 0;
            transform: rotate(45deg);
        }

        .remember-me label {
            font-size: 0.95em;
            color: #333;
            cursor: pointer;
        }

    </style>

</head>
<body>

<jsp:include page="/view/pagesJsp/navbar.jsp" />

<div class="auth-page">
    <div class="auth-container">
        <div class="auth-content">

            <form action="${pageContext.request.contextPath}/LoginServlet" method="post" class="auth-form">
                <h2>Login</h2>

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required
                       value="<%= request.getAttribute("rememberedEmail") != null ? request.getAttribute("rememberedEmail") : (request.getAttribute("email") != null ? request.getAttribute("email") : "") %>"
                       class="form-input <%= (request.getAttribute("emailError") != null) ? "error-border" : "" %>" />

                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required
                       class="form-input <%= (request.getAttribute("passwordError") != null) ? "error-border" : "" %>" />

                <% if (request.getAttribute("emailError") != null) { %>
                <p class="error-message"><%= request.getAttribute("emailError") %></p>
                <% } %>

                <% if (request.getAttribute("passwordError") != null) { %>
                <p class="error-message"><%= request.getAttribute("passwordError") %></p>
                <% } %>

                <div class="remember-me">
                    <input type="checkbox" id="remember" name="remember" value="true">
                    <label for="remember">Remember me</label>
                </div>

                <button type="submit">Login</button>

                <p>Don't have an account? <a href="RegisterServlet">Sign up</a></p>
            </form>
        </div>

        <div class="auth-image">
            <img src="https://img.freepik.com/free-vector/login-concept-illustration_114360-739.jpg?height=700&width=500" alt="Login">
        </div>
    </div>
</div>

<!-- Toast Notification -->
<div id="toast" class="toast"></div>

<script>
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");

    emailInput.addEventListener("input", function () {
        emailInput.classList.remove("error-border");
        const emailErrorMsg = document.querySelectorAll(".error-message")[0];
        if (emailErrorMsg) emailErrorMsg.style.display = "none";
    });

    passwordInput.addEventListener("input", function () {
        passwordInput.classList.remove("error-border");
        const passwordErrorMsg = document.querySelectorAll(".error-message")[1];
        if (passwordErrorMsg) passwordErrorMsg.style.display = "none";
    });

    const params = new URLSearchParams(window.location.search);
    if (params.get("login") === "success") {
        const toast = document.getElementById("toast");
        toast.textContent = "Login successful!";
        toast.className = "toast show";

        setTimeout(function () {
            toast.className = toast.className.replace("show", "");
        }, 3000);
    }
</script>


<%@ include file="/view/pagesJsp/footer.jsp" %>

</body>
</html>