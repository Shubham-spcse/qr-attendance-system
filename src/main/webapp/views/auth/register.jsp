<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Registration - QR Attendance System</title>

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
            min-height: 100vh;
            background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 50%, #6366f1 100%);
            padding: 40px 20px;
        }

        .register-container {
            max-width: 900px;
            margin: 0 auto;
        }

        .register-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.2);
            overflow: hidden;
        }

        .register-header {
            background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%);
            padding: 40px;
            text-align: center;
            color: white;
        }

        .register-header i {
            font-size: 3.5rem;
            margin-bottom: 15px;
        }

        .register-header h1 {
            font-size: 2.2rem;
            font-weight: 800;
            margin-bottom: 10px;
        }

        .register-header p {
            font-size: 1rem;
            opacity: 0.95;
        }

        .register-body {
            padding: 40px;
        }

        .section-title {
            font-size: 1.1rem;
            font-weight: 700;
            color: #1f2937;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #f3f4f6;
            display: flex;
            align-items: center;
        }

        .section-title i {
            margin-right: 10px;
            color: #ec4899;
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

        .form-control, .form-select {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e5e7eb;
            border-radius: 10px;
            font-size: 0.95rem;
            transition: all 0.3s ease;
            font-family: 'Inter', sans-serif;
        }

        .form-control:focus, .form-select:focus {
            outline: none;
            border-color: #ec4899;
            box-shadow: 0 0 0 4px rgba(236, 72, 153, 0.1);
        }

        .form-text {
            font-size: 0.8rem;
            color: #6b7280;
            margin-top: 5px;
            display: block;
        }

        .input-icon {
            color: #ec4899;
            margin-right: 5px;
        }

        .password-strength {
            height: 4px;
            background: #e5e7eb;
            border-radius: 4px;
            margin-top: 8px;
            overflow: hidden;
        }

        .strength-bar {
            height: 100%;
            width: 0;
            transition: all 0.3s ease;
        }

        .strength-weak { width: 33%; background: #ef4444; }
        .strength-medium { width: 66%; background: #f59e0b; }
        .strength-strong { width: 100%; background: #10b981; }

        .btn-register {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%);
            border: none;
            border-radius: 10px;
            color: white;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 12px rgba(236, 72, 153, 0.3);
        }

        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(236, 72, 153, 0.4);
        }

        .login-link {
            text-align: center;
            margin-top: 25px;
            font-size: 0.9rem;
            color: #6b7280;
        }

        .login-link a {
            color: #ec4899;
            text-decoration: none;
            font-weight: 600;
        }

        .login-link a:hover {
            color: #8b5cf6;
        }

        .checkbox-label {
            display: flex;
            align-items: center;
            font-size: 0.9rem;
            color: #374151;
            margin-top: 20px;
        }

        .checkbox-label input {
            margin-right: 8px;
        }

        .checkbox-label a {
            color: #ec4899;
            text-decoration: none;
            font-weight: 600;
        }

        .alert {
            padding: 12px 16px;
            border-radius: 10px;
            margin-bottom: 20px;
            font-size: 0.9rem;
        }

        @media (max-width: 768px) {
            .register-body {
                padding: 30px 20px;
            }
            .register-header {
                padding: 30px 20px;
            }
        }
    </style>
</head>
<body>
    <div class="register-container">
        <div class="register-card">
            <!-- Header -->
            <div class="register-header">
                <i class="fas fa-user-plus"></i>
                <h1>Create Your Account</h1>
                <p>Join QR Attendance System today</p>
            </div>

            <!-- Body -->
            <div class="register-body">
                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm">
                    <!-- Personal Information -->
                    <div class="section-title">
                        <i class="fas fa-user-circle"></i>
                        <span>Personal Information</span>
                    </div>

                    <div class="row g-3">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-id-card input-icon"></i>Roll Number *
                                </label>
                                <input type="text" name="rollNumber" id="rollNumber" class="form-control"
                                       placeholder="2023CSE001" pattern="[0-9]{4}[A-Z]{3}[0-9]{3}" required>
                                <span class="form-text">Format: YYYYDDDNNN</span>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-user input-icon"></i>Full Name *
                                </label>
                                <input type="text" name="name" class="form-control" placeholder="Shubh Prajapati" required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-envelope input-icon"></i>Email Address *
                                </label>
                                <input type="email" name="email" class="form-control"
                                       placeholder="subh@gmail.com" required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-phone input-icon"></i>Phone Number
                                </label>
                                <input type="tel" name="phone" class="form-control"
                                       placeholder="9876543210" pattern="[0-9]{10}">
                            </div>
                        </div>
                    </div>

                    <!-- Academic Information -->
                    <div class="section-title mt-4">
                        <i class="fas fa-graduation-cap"></i>
                        <span>Academic Details</span>
                    </div>

                    <div class="row g-3">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-building input-icon"></i>Department *
                                </label>
                                <select name="departmentId" class="form-select" required>
                                    <option value="">Select Department</option>
                                    <option value="1">Computer Science Engineering</option>
                                    <option value="2">Electronics & Communication</option>
                                    <option value="3">Mechanical Engineering</option>
                                    <option value="4">Civil Engineering</option>
                                    <option value="5">Electrical Engineering</option>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <div class="form-group">
                                <label class="form-label">Year *</label>
                                <select name="year" class="form-select" required>
                                    <option value="">Select</option>
                                    <option value="1">1st Year</option>
                                    <option value="2">2nd Year</option>
                                    <option value="3">3rd Year</option>
                                    <option value="4">4th Year</option>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <div class="form-group">
                                <label class="form-label">Section *</label>
                                <select name="section" class="form-select" required>
                                    <option value="">Select</option>
                                    <option value="A">A</option>
                                    <option value="B">B</option>
                                    <option value="C">C</option>
                                    <option value="D">D</option>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <div class="form-group">
                                <label class="form-label">Semester *</label>
                                <select name="semester" class="form-select" required>
                                    <option value="">Select</option>
                                    <c:forEach begin="1" end="8" var="i">
                                        <option value="${i}">${i}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-12">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-calendar-alt input-icon"></i>Admission Date *
                                </label>
                                <input type="date" name="admissionDate" id="admissionDate" class="form-control" required>
                            </div>
                        </div>
                    </div>

                    <!-- Security -->
                    <div class="section-title mt-4">
                        <i class="fas fa-lock"></i>
                        <span>Security</span>
                    </div>

                    <div class="row g-3">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Password *</label>
                                <input type="password" name="password" id="password" class="form-control"
                                       placeholder="Min 8 characters" minlength="8" required>
                                <div class="password-strength">
                                    <div class="strength-bar" id="strengthBar"></div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Confirm Password *</label>
                                <input type="password" name="confirmPassword" id="confirmPassword"
                                       class="form-control" placeholder="Re-enter password" required>
                            </div>
                        </div>
                    </div>

                    <!-- Terms -->
                    <label class="checkbox-label">
                        <input type="checkbox" required>
                        <span>I agree to the <a href="#">Terms & Conditions</a> and <a href="#">Privacy Policy</a></span>
                    </label>

                    <!-- Submit -->
                    <button type="submit" class="btn-register mt-4">
                        <i class="fas fa-user-plus me-2"></i>Create Account
                    </button>
                </form>

                <div class="login-link">
                    Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Password Strength
        document.getElementById('password').addEventListener('input', function() {
            const password = this.value;
            const bar = document.getElementById('strengthBar');
            bar.className = 'strength-bar';

            if (password.length >= 8) {
                if (password.match(/[a-z]/) && password.match(/[A-Z]/) &&
                    password.match(/[0-9]/) && password.match(/[^a-zA-Z0-9]/)) {
                    bar.classList.add('strength-strong');
                } else if (password.match(/[a-zA-Z]/) && password.match(/[0-9]/)) {
                    bar.classList.add('strength-medium');
                } else {
                    bar.classList.add('strength-weak');
                }
            }
        });

        // Form Validation
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirm = document.getElementById('confirmPassword').value;

            if (password !== confirm) {
                e.preventDefault();
                alert('Passwords do not match!');
                return false;
            }

            const roll = document.getElementById('rollNumber').value;
            if (!/^[0-9]{4}[A-Z]{3}[0-9]{3}$/.test(roll)) {
                e.preventDefault();
                alert('Invalid roll number format! Use: YYYYDDDNNN');
                return false;
            }
        });

        document.getElementById('admissionDate').max = new Date().toISOString().split('T')[0];
    </script>
</body>
</html>
