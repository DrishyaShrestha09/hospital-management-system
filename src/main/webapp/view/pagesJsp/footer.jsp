<%@ page import="java.util.Calendar" %>
<footer class="footer">
    <div class="footer-container">
        <div class="footer-section">
            <h3>HospitalSys</h3>
            <p>Connecting patients with healthcare professionals for better care.</p>
        </div>

        <div class="footer-section">
            <h4>Quick Links</h4>
            <ul>
                <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/view/pagesJsp/login.jsp">Login</a></li>
                <li><a href="${pageContext.request.contextPath}/view/pagesJsp/signup.jsp">Sign Up</a></li>
            </ul>
        </div>

        <div class="footer-section">
            <h4>Services</h4>
            <ul>
                <li>Online Consultations</li>
                <li>Appointment Booking</li>
                <li>Health Records</li>
                <li>Medical Advice</li>
            </ul>
        </div>

        <div class="footer-section">
            <h4>Contact Us</h4>
            <p>Email: support@netrudoc.com</p>
            <p>Phone: +1 (555) 123-4567</p>
            <div class="social-icons">
                <a href="#" class="social-icon"><i class="fab fa-facebook-f"></i></a>
                <a href="#" class="social-icon"><i class="fab fa-twitter"></i></a>
                <a href="#" class="social-icon"><i class="fab fa-instagram"></i></a>
                <a href="#" class="social-icon"><i class="fab fa-linkedin-in"></i></a>
            </div>
        </div>
    </div>

    <div class="footer-bottom">
        <p>&copy; <%= Calendar.getInstance().get(Calendar.YEAR) %> HospitalSys. All rights reserved.</p>
    </div>
</footer>

