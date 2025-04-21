<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%@ page import="javax.servlet.http., javax.servlet." %>--%>
<%--<%--%>
<%--    HttpSession session = request.getSession(false);--%>
<%--    String userType = null;--%>
<%--    if (session != null && session.getAttribute("currentUser") != null) {--%>
<%--        // Assuming "currentUser" object has a getUserType() method--%>
<%--        userType = (String) session.getAttribute("userType");--%>
<%--        r esponse.sendRedirect(request.getContextPath() + "/" + userType + "/dashboard");--%>
<%--        return;--%>
<%--    }--%>
<%--%>--%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - Hospital Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/authPages.css">
</head>
<body>

<jsp:include page="/view/pagesJsp/navbar.jsp" />

<div class="auth-page">
    <div class="auth-container">
        <div class="auth-content">
            <form action="${pageContext.request.contextPath}/LoginServlet" method="post" class="auth-form">
                <h2>Login</h2>

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required />

                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required />

                <button type="submit">Login</button>

                <p>Don't have an account? <a href="signup.jsp">Sign up</a></p>
            </form>
        </div>

        <div class="auth-image">
            <img src="https://img.freepik.com/free-vector/login-concept-illustration_114360-739.jpg?height=700&width=500" alt="Login">
        </div>
    </div>
</div>

<%@ include file="/view/pagesJsp/footer.jsp" %>

</body>
</html>
