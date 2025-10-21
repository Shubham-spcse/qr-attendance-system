<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - QR Attendance System</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            height: 100vh;
            overflow: hidden;
        }

        .login-wrapper {
            display: flex;
            height: 100vh;
        }

        /* Left Side - Gradient & Info */
        .left-side {
            flex: 1;
            background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #d946ef 100%);
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            padding: 60px;
            position: relative;
            overflow: hidden;
        }

        .left-side::before {
            content: '';
            position: absolute;
            width: 500px;
            height: 500px;
            background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
            border-radius: 50%;
            top: -100px;
            right: -100px;
        }

        .left-side::after {
            content: '';
            position: absolute;
            width: 400px;
            height: 400px;
            background: radial-gradient(circle, rgba(255,255,255,0.08) 0%, transparent 70%);
            border-radius: 50%;
            bottom: -150px;
            left: -100px;
        }

        .brand-section {
            position: relative;
            z-index: 10;
            text-align: center;
            color: white;
        }

        .brand-icon {
            font-size: 5rem;
            margin-bottom: 30px;
            animation: float 3s ease-in-out infinite;
        }

        @keyframes float {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-20px); }
        }

        .brand-section h1 {
            font-size: 3rem;
            font-weight: 800;
            margin-bottom: 20px;
            text-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }

        .brand-section p {
            font-size: 1.2rem;
            opacity: 0.95;
            max-width: 400px;
            line-height: 1.6;
        }

        .features-list {
            margin-top: 50px;
            text-align: left;
        }

        .feature-item {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
            font-size: 1rem;
        }

        .feature-item i {
            width: 40px;
            height: 40px;
            background: rgba(255,255,255,0.2);
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
        }

        /* Right Side - Login Form */
        .right-side {
            flex: 1;
            background: white;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px;
        }

        .login-container {
            width: 100%;
            max-width: 450px;
        }

        .login-header {
            margin-bottom: 40px;
        }

        .login-header h2 {
            font-size: 2rem;
            font-weight: 700;
            color: #1f2937;
            margin-bottom: 10px;
        }

        .login-header p {
            color: #6b7280;
            font-size: 0.95rem;
        }

        .role-selector {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 12px;
            margin-bottom: 30px;
        }

        .role-option {
            position: relative;
        }

        .role-option input {
            position: absolute;
            opacity: 0;
        }

        .role-label {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 20px 10px;
            background: #f9fafb;
            border: 2px solid #e5e7eb;
            border-radius: 12px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-align: center;
        }

        .role-label i {
            font-size: 1.8rem;
            color: #6b7280;
            margin-bottom: 8px;
            transition: all 0.3s ease;
        }

        .role-label span {
            font-size: 0.85rem;
            font-weight: 600;
            color: #374151;
        }

        .role-option input:checked + .role-label {
            background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
            border-color: #6366f1;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
        }

        .role-option input:checked + .role-label i,
        .role-option input:checked + .role-label span {
            color: white;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-label {
            display: block;
            font-size: 0.9rem;
            font-weight: 600;
            color: #374151;
            margin-bottom: 8px;
        }

        .form-control {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e5e7eb;
            border-radius: 10px;
            font-size: 0.95rem;
            transition: all 0.3s ease;
            font-family: 'Inter', sans-serif;
        }

        .form-control:focus {
            outline: none;
            border-color: #6366f1;
            box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.1);
        }

        .password-wrapper {
            position: relative;
        }

        .password-toggle {
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            color: #6b7280;
            cursor: pointer;
            padding: 8px;
        }

        .password-toggle:hover {
            color: #6366f1;
        }

        .form-options {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            font-size: 0.9rem;
        }

        .checkbox-label {
            display: flex;
            align-items: center;
            cursor: pointer;
            color: #374151;
        }

        .checkbox-label input {
            margin-right: 8px;
            cursor: pointer;
        }

        .forgot-link {
            color: #6366f1;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s ease;
        }

        .forgot-link:hover {
            color: #8b5cf6;
        }

        .btn-login {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
            border: none;
            border-radius: 10px;
            color: white;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(99, 102, 241, 0.4);
        }

        .divider {
            text-align: center;
            margin: 30px 0;
            position: relative;
        }

        .divider span {
            background: white;
            padding: 0 15px;
            color: #6b7280;
            font-size: 0.85rem;
            position: relative;
            z-index: 1;
        }

        .divider::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            height: 1px;
            background: #e5e7eb;
        }

        .register-link {
            text-align: center;
            font-size: 0.9rem;
            color: #6b7280;
        }

        .register-link a {
            color: #6366f1;
            text-decoration: none;
            font-weight: 600;
        }

        .register-link a:hover {
            color: #8b5cf6;
        }

        .demo-box {
            background: #f9fafb;
            border: 1px solid #e5e7eb;
            border-radius: 10px;
            padding: 15px;
            margin-top: 20px;
        }

        .demo-box h6 {
            font-size: 0.85rem;
            font-weight: 700;
            color: #374151;
            margin-bottom: 10px;
        }

        .demo-box p {
            font-size: 0.8rem;
            color: #6b7280;
            margin: 5px 0;
            font-family: 'Courier New', monospace;
        }

        .alert {
            padding: 12px 16px;
            border-radius: 10px;
            margin-bottom: 20px;
            font-size: 0.9rem;
        }

        @media (max-width: 768px) {
            .login-wrapper {
                flex-direction: column;
            }
            .left-side {
                display: none;
            }
            .right-side {
                padding: 30px 20px;
            }
        }
    </style>
</head>
<body>
    <div class="login-wrapper">
        <!-- Left Side - Brand & Info -->
        <div class="left-side">
            <div class="brand-section">
                <div class="brand-icon">
                    <i class="fas fa-qrcode"></i>
                </div>
                <h1>QR Attendance</h1>
                <p>Smart, Secure, and Simple attendance management for modern institutions</p>

                <div class="features-list">
                    <div class="feature-item">
                        <i class="fas fa-bolt"></i>
                        <span>Instant QR code scanning</span>
                    </div>
                    <div class="feature-item">
                        <i class="fas fa-shield-alt"></i>
                        <span>Bank-level security</span>
                    </div>
                    <div class="feature-item">
                        <i class="fas fa-chart-line"></i>
                        <span>Real-time analytics</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- Right Side - Login Form -->
        <div class="right-side">
            <div class="login-container">
                <div class="login-header">
                    <h2>Welcome back</h2>
                    <p>Enter your credentials to access your account</p>
                </div>

                <!-- Error/Success Messages -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                    </div>
                </c:if>

                <c:if test="${param.registered == 'true'}">
                    <div class="alert alert-success">
                        <i class="fas fa-check-circle me-2"></i>Registration successful! Please login.
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <!-- Role Selector -->
                    <div class="role-selector">
                        <div class="role-option">
                            <input type="radio" name="userType" id="student" value="STUDENT" checked>
                            <label for="student" class="role-label">
                                <i class="fas fa-user-graduate"></i>
                                <span>Student</span>
                            </label>
                        </div>
                        <div class="role-option">
                            <input type="radio" name="userType" id="staff" value="STAFF">
                            <label for="staff" class="role-label">
                                <i class="fas fa-chalkboard-teacher"></i>
                                <span>Staff</span>
                            </label>
                        </div>
                        <div class="role-option">
                            <input type="radio" name="userType" id="admin" value="ADMIN">
                            <label for="admin" class="role-label">
                                <i class="fas fa-user-shield"></i>
                                <span>Admin</span>
                            </label>
                        </div>
                    </div>

                    <!-- Email -->
                    <div class="form-group">
                        <label class="form-label">Email address</label>
                        <input type="email" name="email" class="form-control" placeholder="you@example.com" required autofocus>
                    </div>

                    <!-- Password -->
                    <div class="form-group">
                        <label class="form-label">Password</label>
                        <div class="password-wrapper">
                            <input type="password" name="password" id="password" class="form-control" placeholder="Enter your password" required>
                            <button type="button" class="password-toggle" onclick="togglePassword()">
                                <i class="fas fa-eye" id="toggleIcon"></i>
                            </button>
                        </div>
                    </div>

                    <!-- Options -->
                    <div class="form-options">
                        <label class="checkbox-label">
                            <input type="checkbox" name="rememberMe">
                            <span>Remember me</span>
                        </label>
                        <a href="#" class="forgot-link">Forgot password?</a>
                    </div>

                    <!-- Submit -->
                    <button type="submit" class="btn-login">
                        <i class="fas fa-sign-in-alt me-2"></i>Sign in
                    </button>
                </form>

                <div class="divider">
                    <span>or</span>
                </div>

                <div class="register-link">
                    Don't have an account? <a href="${pageContext.request.contextPath}/register">Create one</a>
                </div>

                <!-- Demo Credentials -->
                <div class="demo-box">
                    <h6><i class="fas fa-info-circle me-1"></i> Demo Accounts</h6>
                    <p>Student: rahul.2023cse001@student.edu / Student@123</p>
                    <p>Staff: ramesh.verma@college.edu / Staff@123</p>
                    <p>Admin: admin@attendance.com / Admin@123</p>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function togglePassword() {
            const password = document.getElementById('password');
            const icon = document.getElementById('toggleIcon');
            if (password.type === 'password') {
                password.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                password.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        }
    </script>
</body>
</html>
