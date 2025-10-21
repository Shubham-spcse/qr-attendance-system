<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="Modern QR-based Attendance Management System - Secure, Fast, and Efficient">
    <meta name="author" content="QR Attendance System">

    <title>QR Attendance System - Smart Attendance Management</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <!-- AOS Animation Library -->
    <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">

    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%);
            --secondary-gradient: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
            --success-gradient: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            --dark-gradient: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            overflow-x: hidden;
            background: #f8f9fa;
        }

        /* Hero Section */
        .hero-section {
            min-height: 100vh;
            background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 50%, #6366f1 100%);
            position: relative;
            overflow: hidden;
            display: flex;
            align-items: center;
        }

        .hero-section::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: url('data:image/svg+xml,<svg width="100" height="100" xmlns="http://www.w3.org/2000/svg"><defs><pattern id="grid" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="50" cy="50" r="2" fill="rgba(255,255,255,0.1)"/></pattern></defs><rect width="100%" height="100%" fill="url(%23grid)"/></svg>');
            opacity: 0.4;
        }

        .floating-qr {
            position: absolute;
            animation: float 6s ease-in-out infinite;
            opacity: 0.15;
            color: white;
        }

        .floating-qr:nth-child(1) { top: 10%; left: 10%; font-size: 100px; animation-delay: 0s; }
        .floating-qr:nth-child(2) { top: 60%; right: 10%; font-size: 120px; animation-delay: 2s; }
        .floating-qr:nth-child(3) { bottom: 10%; left: 50%; font-size: 80px; animation-delay: 4s; }

        @keyframes float {
            0%, 100% { transform: translateY(0) rotate(0deg); }
            50% { transform: translateY(-20px) rotate(5deg); }
        }

        .hero-content {
            position: relative;
            z-index: 10;
            color: white;
        }

        .hero-title {
            font-size: 4rem;
            font-weight: 800;
            line-height: 1.2;
            margin-bottom: 1.5rem;
            text-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }

        .hero-subtitle {
            font-size: 1.5rem;
            font-weight: 300;
            margin-bottom: 2rem;
            opacity: 0.95;
        }

        .hero-buttons .btn {
            padding: 15px 40px;
            font-size: 1.1rem;
            font-weight: 600;
            border-radius: 50px;
            margin: 10px;
            transition: all 0.3s ease;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        }

        .hero-buttons .btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 35px rgba(0,0,0,0.3);
        }

        .btn-light-custom {
            background: white;
            color: #ec4899;
            border: none;
        }

        .btn-outline-light-custom {
            background: transparent;
            color: white;
            border: 2px solid white;
        }

        .btn-outline-light-custom:hover {
            background: white;
            color: #ec4899;
        }

        /* Features Section */
        .features-section {
            padding: 100px 0;
            background: white;
        }

        .section-title {
            font-size: 3rem;
            font-weight: 700;
            text-align: center;
            margin-bottom: 1rem;
            background: var(--primary-gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .section-subtitle {
            text-align: center;
            font-size: 1.2rem;
            color: #6b7280;
            margin-bottom: 4rem;
        }

        .feature-card {
            background: white;
            border-radius: 20px;
            padding: 40px;
            text-align: center;
            transition: all 0.3s ease;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            height: 100%;
            border: 1px solid #f0f0f0;
        }

        .feature-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 40px rgba(236, 72, 153, 0.2);
        }

        .feature-icon {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 25px;
            font-size: 2rem;
            color: white;
        }

        .feature-icon.primary { background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%); }
        .feature-icon.secondary { background: linear-gradient(135deg, #8b5cf6 0%, #6366f1 100%); }
        .feature-icon.success { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
        .feature-icon.dark { background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%); }

        .feature-card h3 {
            font-size: 1.5rem;
            font-weight: 600;
            margin-bottom: 15px;
            color: #1f2937;
        }

        .feature-card p {
            color: #6b7280;
            line-height: 1.8;
        }

        /* Stats Section */
        .stats-section {
            padding: 80px 0;
            background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 50%, #6366f1 100%);
            color: white;
        }

        .stat-card {
            text-align: center;
            padding: 30px;
        }

        .stat-number {
            font-size: 3.5rem;
            font-weight: 800;
            display: block;
            margin-bottom: 10px;
        }

        .stat-label {
            font-size: 1.2rem;
            font-weight: 300;
            opacity: 0.9;
        }

        /* How It Works Section */
        .how-it-works-section {
            padding: 100px 0;
            background: #f8f9fa;
        }

        .step-card {
            text-align: center;
            padding: 30px;
            position: relative;
        }

        .step-number {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            font-weight: 700;
            margin: 0 auto 20px;
            box-shadow: 0 5px 15px rgba(236, 72, 153, 0.4);
        }

        .step-card h4 {
            font-size: 1.3rem;
            font-weight: 600;
            margin-bottom: 15px;
            color: #1f2937;
        }

        .step-card p {
            color: #6b7280;
            line-height: 1.8;
        }

        /* CTA Section */
        .cta-section {
            padding: 100px 0;
            background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #ec4899 100%);
            color: white;
            text-align: center;
        }

        .cta-section h2 {
            font-size: 3rem;
            font-weight: 700;
            margin-bottom: 30px;
        }

        .cta-section p {
            font-size: 1.3rem;
            margin-bottom: 40px;
            opacity: 0.9;
        }

        /* Footer */
        .footer {
            background: #1f2937;
            color: white;
            padding: 60px 0 30px;
        }

        .footer h5 {
            font-weight: 600;
            margin-bottom: 20px;
            color: #ec4899;
        }

        .footer a {
            color: rgba(255,255,255,0.7);
            text-decoration: none;
            display: block;
            margin-bottom: 10px;
            transition: all 0.3s ease;
        }

        .footer a:hover {
            color: #ec4899;
            padding-left: 5px;
        }

        .social-icons a {
            display: inline-block;
            width: 40px;
            height: 40px;
            background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%);
            border-radius: 50%;
            text-align: center;
            line-height: 40px;
            margin-right: 10px;
            transition: all 0.3s ease;
            color: white;
        }

        .social-icons a:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(236, 72, 153, 0.4);
        }

        /* Responsive */
        @media (max-width: 768px) {
            .hero-title { font-size: 2.5rem; }
            .hero-subtitle { font-size: 1.2rem; }
            .section-title { font-size: 2rem; }
            .stat-number { font-size: 2.5rem; }
        }

        /* Animations */
        .fade-in {
            animation: fadeIn 1s ease-in;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(30px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body>
    <!-- Hero Section -->
    <section class="hero-section">
        <!-- Floating QR Codes -->
        <div class="floating-qr"><i class="fas fa-qrcode"></i></div>
        <div class="floating-qr"><i class="fas fa-qrcode"></i></div>
        <div class="floating-qr"><i class="fas fa-qrcode"></i></div>

        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-6 hero-content" data-aos="fade-right">
                    <h1 class="hero-title">Smart Attendance with QR Technology</h1>
                    <p class="hero-subtitle">
                        Modern, secure, and efficient attendance management system powered by QR codes
                    </p>
                    <div class="hero-buttons">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-light-custom btn-lg">
                            <i class="fas fa-sign-in-alt me-2"></i> Get Started
                        </a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-light-custom btn-lg">
                            <i class="fas fa-user-plus me-2"></i> Register Now
                        </a>
                    </div>
                </div>
                <div class="col-lg-6 text-center" data-aos="fade-left">
                    <i class="fas fa-qrcode" style="font-size: 20rem; color: rgba(255,255,255,0.2);"></i>
                </div>
            </div>
        </div>
    </section>

    <!-- Features Section -->
    <section class="features-section">
        <div class="container">
            <h2 class="section-title" data-aos="fade-up">Powerful Features</h2>
            <p class="section-subtitle" data-aos="fade-up" data-aos-delay="100">
                Everything you need for modern attendance management
            </p>

            <div class="row g-4">
                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="200">
                    <div class="feature-card">
                        <div class="feature-icon primary">
                            <i class="fas fa-qrcode"></i>
                        </div>
                        <h3>QR Code Scanning</h3>
                        <p>Instant attendance marking with secure QR codes generated in real-time</p>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="300">
                    <div class="feature-card">
                        <div class="feature-icon secondary">
                            <i class="fas fa-mobile-alt"></i>
                        </div>
                        <h3>Mobile Friendly</h3>
                        <p>Access from any device - desktop, tablet, or smartphone</p>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="400">
                    <div class="feature-card">
                        <div class="feature-icon success">
                            <i class="fas fa-chart-line"></i>
                        </div>
                        <h3>Real-time Analytics</h3>
                        <p>Monitor attendance in real-time with comprehensive reports</p>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="500">
                    <div class="feature-card">
                        <div class="feature-icon dark">
                            <i class="fas fa-shield-alt"></i>
                        </div>
                        <h3>Secure & Private</h3>
                        <p>Bank-level encryption with BCrypt password security</p>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="600">
                    <div class="feature-card">
                        <div class="feature-icon success">
                            <i class="fas fa-users"></i>
                        </div>
                        <h3>Multi-Role Access</h3>
                        <p>Separate dashboards for Admin, Staff, and Students</p>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="700">
                    <div class="feature-card">
                        <div class="feature-icon primary">
                            <i class="fas fa-download"></i>
                        </div>
                        <h3>CSV Export</h3>
                        <p>Download attendance reports in CSV format instantly</p>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="800">
                    <div class="feature-card">
                        <div class="feature-icon secondary">
                            <i class="fas fa-edit"></i>
                        </div>
                        <h3>Manual Override</h3>
                        <p>Flexibility to manually mark or correct attendance</p>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="900">
                    <div class="feature-card">
                        <div class="feature-icon dark">
                            <i class="fas fa-bell"></i>
                        </div>
                        <h3>Notifications</h3>
                        <p>Real-time alerts for attendance status updates</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Stats Section -->
    <section class="stats-section">
        <div class="container">
            <div class="row">
                <div class="col-md-3 col-6" data-aos="zoom-in" data-aos-delay="100">
                    <div class="stat-card">
                        <span class="stat-number"><i class="fas fa-infinity"></i></span>
                        <span class="stat-label">Unlimited Sessions</span>
                    </div>
                </div>
                <div class="col-md-3 col-6" data-aos="zoom-in" data-aos-delay="200">
                    <div class="stat-card">
                        <span class="stat-number">99.9%</span>
                        <span class="stat-label">Uptime</span>
                    </div>
                </div>
                <div class="col-md-3 col-6" data-aos="zoom-in" data-aos-delay="300">
                    <div class="stat-card">
                        <span class="stat-number"><2s</span>
                        <span class="stat-label">Scan Time</span>
                    </div>
                </div>
                <div class="col-md-3 col-6" data-aos="zoom-in" data-aos-delay="400">
                    <div class="stat-card">
                        <span class="stat-number">100%</span>
                        <span class="stat-label">Secure</span>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- How It Works Section -->
    <section class="how-it-works-section">
        <div class="container">
            <h2 class="section-title" data-aos="fade-up">How It Works</h2>
            <p class="section-subtitle" data-aos="fade-up" data-aos-delay="100">
                Simple 4-step process to manage attendance
            </p>

            <div class="row g-4 mt-4">
                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="200">
                    <div class="step-card">
                        <div class="step-number">1</div>
                        <h4>Staff Creates Session</h4>
                        <p>Teacher creates attendance session for their class with course details</p>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="300">
                    <div class="step-card">
                        <div class="step-number">2</div>
                        <h4>QR Code Generated</h4>
                        <p>System generates secure QR code displayed to students</p>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="400">
                    <div class="step-card">
                        <div class="step-number">3</div>
                        <h4>Students Scan</h4>
                        <p>Students scan QR code using their mobile device camera</p>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3" data-aos="fade-up" data-aos-delay="500">
                    <div class="step-card">
                        <div class="step-number">4</div>
                        <h4>Attendance Marked</h4>
                        <p>Attendance is recorded instantly with timestamp</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section">
        <div class="container" data-aos="fade-up">
            <h2>Ready to Modernize Your Attendance?</h2>
            <p>Join thousands of institutions using QR-based attendance</p>
            <div class="hero-buttons">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-light-custom btn-lg">
                    <i class="fas fa-rocket me-2"></i> Get Started Now
                </a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-light-custom btn-lg">
                    <i class="fas fa-user-plus me-2"></i> Create Account
                </a>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="footer">
        <div class="container">
            <div class="row">
                <div class="col-md-4 mb-4">
                    <h5><i class="fas fa-qrcode me-2"></i> QR Attendance</h5>
                    <p style="color: rgba(255,255,255,0.7);">
                        Modern, secure, and efficient attendance management system powered by QR technology.
                    </p>
                    <div class="social-icons mt-3">
                        <a href="#"><i class="fab fa-facebook-f"></i></a>
                        <a href="#"><i class="fab fa-twitter"></i></a>
                        <a href="#"><i class="fab fa-linkedin-in"></i></a>
                        <a href="#"><i class="fab fa-instagram"></i></a>
                    </div>
                </div>

                <div class="col-md-4 mb-4">
                    <h5>Quick Links</h5>
                    <a href="${pageContext.request.contextPath}/login">Login</a>
                    <a href="${pageContext.request.contextPath}/register">Register</a>
                    <a href="#">Features</a>
                    <a href="#">How It Works</a>
                </div>

                <div class="col-md-4 mb-4">
                    <h5>Contact</h5>
                    <p style="color: rgba(255,255,255,0.7);">
                        <i class="fas fa-envelope me-2"></i> spcse900@gmail.com<br>
                        <i class="fas fa-phone me-2"></i> +91 9044231865<br>
                        <i class="fas fa-map-marker-alt me-2"></i> Civil Line Road Gorakhpur, 273009
                    </p>
                </div>
            </div>

            <hr style="border-color: rgba(255,255,255,0.1); margin: 30px 0;">

            <div class="text-center" style="color: rgba(255,255,255,0.7);">
                <p class="mb-0">© 2025 QR Attendance System. All rights reserved. | Powered by <i class="fas fa-qrcode"></i> Technology</p>
            </div>
        </div>
    </footer>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- AOS Animation Library -->
    <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>

    <script>
        // Initialize AOS animations
        AOS.init({
            duration: 1000,
            once: true,
            offset: 100
        });

        // Smooth scroll for anchor links
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if(target) {
                    target.scrollIntoView({
                        behavior: 'smooth'
                    });
                }
            });
        });
    </script>
</body>
</html>
