<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/view/pagesJsp/navbar.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <title>NetruDoc - Hospital Management</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/navigation.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/footer.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<div class="home-page">
  <section class="hero-section">
    <div class="hero-content">
      <h1 class="animate-slide-up">Healthcare Made Simple</h1>
      <p class="animate-slide-up-delay-1">
        Connect with trusted doctors and book appointments online with ease.
      </p>
      <div class="hero-buttons animate-slide-up-delay-2">
        <a href="${pageContext.request.contextPath}/view/pagesJsp/login.jsp" class="btn btn-outline">Login</a>
        <a href="${pageContext.request.contextPath}/view/pagesJsp/signup.jsp" class="btn btn-primary">Get Started</a>
      </div>
    </div>
    <div class="hero-image animate-fade-in">
      <img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-q9fGeJb9m7jUz2fyFHi2W0mo6cO4QK.png"
           alt="Doctor using healthcare platform"/>
    </div>
  </section>

  <section class="features-section">
    <h2 class="animate-slide-up">Why Choose NetruDoc?</h2>
    <div class="features-grid">
      <div class="feature-card animate-slide-up-delay-1">
        <div class="feature-icon"><i class="fas fa-calendar-check"></i></div>
        <h3>Easy Appointment Booking</h3>
        <p>Book appointments with your preferred doctors in just a few clicks.</p>
      </div>
      <div class="feature-card animate-slide-up-delay-2">
        <div class="feature-icon"><i class="fas fa-user-md"></i></div>
        <h3>Qualified Doctors</h3>
        <p>Connect with experienced and qualified healthcare professionals.</p>
      </div>
      <div class="feature-card animate-slide-up-delay-3">
        <div class="feature-icon"><i class="fas fa-file-medical"></i></div>
        <h3>Digital Health Records</h3>
        <p>Access your medical history and prescriptions anytime, anywhere.</p>
      </div>
      <div class="feature-card animate-slide-up-delay-4">
        <div class="feature-icon"><i class="fas fa-comments"></i></div>
        <h3>Pre-Consultation Forms</h3>
        <p>Fill out pre-consultation forms to help doctors understand your condition better.</p>
      </div>
    </div>
  </section>

  <section class="featured-doctors">
    <h2 class="animate-slide-up">Meet Our Specialists</h2>
    <div class="doctors-grid">
      <div class="doctor-card animate-slide-up-delay-1">
        <img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-eMiDGZGZMrwlJK1lUugchOUJgyQyB2.png" alt="Dr. Rajesh Sharma"/>
        <h3>Dr. Rajesh Sharma</h3>
        <p>Cardiologist</p>
        <span class="experience">15+ years experience</span>
      </div>
      <div class="doctor-card animate-slide-up-delay-2">
        <img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-zE1HVN5pEE1NRUDzEfE5ewgXwRewHy.png" alt="Dr. Sunil Poudel"/>
        <h3>Dr. Sunil Poudel</h3>
        <p>Neurologist</p>
        <span class="experience">12+ years experience</span>
      </div>
      <div class="doctor-card animate-slide-up-delay-3">
        <img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-8RbQauD3KH8jBIhtbrAtqmK6xbKnPG.png" alt="Dr. Binod Adhikari"/>
        <h3>Dr. Binod Adhikari</h3>
        <p>General Physician</p>
        <span class="experience">18+ years experience</span>
      </div>
      <div class="doctor-card animate-slide-up-delay-4">
        <img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-kPmZLYcJDPzFPnN09Sg53n1Jx5LUXI.png" alt="Dr. Priya Shrestha"/>
        <h3>Dr. Priya Shrestha</h3>
        <p>Pediatrician</p>
        <span class="experience">10+ years experience</span>
      </div>
    </div>
  </section>

  <section class="how-it-works-section">
    <h2 class="animate-slide-up">How It Works</h2>
    <div class="steps-container">
      <div class="step animate-slide-up-delay-1">
        <div class="step-number">1</div>
        <h3>Create an Account</h3>
        <p>Sign up as a patient or doctor to get started with NetruDoc.</p>
      </div>
      <div class="step animate-slide-up-delay-2">
        <div class="step-number">2</div>
        <h3>Find a Doctor</h3>
        <p>Browse through our network of qualified healthcare professionals.</p>
      </div>
      <div class="step animate-slide-up-delay-3">
        <div class="step-number">3</div>
        <h3>Book an Appointment</h3>
        <p>Select a convenient time slot and book your appointment.</p>
      </div>
      <div class="step animate-slide-up-delay-4">
        <div class="step-number">4</div>
        <h3>Get Quality Care</h3>
        <p>Visit the doctor at the scheduled time and receive quality healthcare.</p>
      </div>
    </div>
  </section>

  <section class="cta-section animate-fade-in">
    <div class="cta-content">
      <h2>Ready to Get Started?</h2>
      <p>Join thousands of users who have simplified their healthcare experience with NetruDoc.</p>
      <a href="${pageContext.request.contextPath}/view/pagesJsp/signup.jsp" style ="color: white"  >Create an Account</a>
    </div>
  </section>
</div>

<%@ include file="/view/pagesJsp/footer.jsp" %>

</body>
</html>