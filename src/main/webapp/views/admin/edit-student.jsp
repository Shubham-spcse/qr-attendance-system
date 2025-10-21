<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Student - QR Attendance</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px 0;
        }

        .edit-container {
            max-width: 800px;
            margin: 0 auto;
        }

        .edit-card {
            background: white;
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }

        .edit-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .edit-header h2 {
            color: #2d3748;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .edit-header p {
            color: #718096;
        }

        .form-label {
            font-weight: 600;
            color: #2d3748;
            margin-bottom: 8px;
        }

        .form-control, .form-select {
            border-radius: 10px;
            border: 2px solid #e2e8f0;
            padding: 12px 15px;
            transition: all 0.3s ease;
        }

        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }

        .btn-group-custom {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }

        .btn-custom {
            flex: 1;
            padding: 12px;
            border-radius: 10px;
            font-weight: 600;
            border: none;
            transition: all 0.3s ease;
        }

        .btn-update {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-update:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-cancel {
            background: #e2e8f0;
            color: #2d3748;
        }

        .btn-cancel:hover {
            background: #cbd5e0;
        }

        .required {
            color: #e53e3e;
        }
    </style>
</head>
<body>
    <div class="edit-container">
        <div class="edit-card">
            <div class="edit-header">
                <h2><i class="fas fa-user-edit me-2"></i> Edit Student</h2>
                <p>Update student information</p>
            </div>

            <form action="${pageContext.request.contextPath}/admin/manage-students" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="studentId" value="${student.studentId}">

                <div class="row">
                    <!-- Roll Number -->
                    <div class="col-md-6 mb-3">
                        <label for="rollNumber" class="form-label">
                            Roll Number <span class="required">*</span>
                        </label>
                        <input type="text"
                               class="form-control"
                               id="rollNumber"
                               name="rollNumber"
                               value="${student.rollNumber}"
                               required>
                    </div>

                    <!-- Name -->
                    <div class="col-md-6 mb-3">
                        <label for="name" class="form-label">
                            Full Name <span class="required">*</span>
                        </label>
                        <input type="text"
                               class="form-control"
                               id="name"
                               name="name"
                               value="${student.name}"
                               required>
                    </div>

                    <!-- Email -->
                    <div class="col-md-6 mb-3">
                        <label for="email" class="form-label">
                            Email <span class="required">*</span>
                        </label>
                        <input type="email"
                               class="form-control"
                               id="email"
                               name="email"
                               value="${student.email}"
                               required>
                    </div>

                    <!-- Phone -->
                    <div class="col-md-6 mb-3">
                        <label for="phone" class="form-label">Phone</label>
                        <input type="text"
                               class="form-control"
                               id="phone"
                               name="phone"
                               value="${student.phone}">
                    </div>

                    <!-- Department -->
                    <div class="col-md-6 mb-3">
                        <label for="departmentId" class="form-label">
                            Department <span class="required">*</span>
                        </label>
                        <select class="form-select"
                                id="departmentId"
                                name="departmentId"
                                required>
                            <option value="">Select Department</option>
                            <c:forEach items="${departments}" var="dept">
                                <option value="${dept.departmentId}"
                                    ${student.departmentId == dept.departmentId ? 'selected' : ''}>
                                    ${dept.departmentName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Year -->
                    <div class="col-md-6 mb-3">
                        <label for="year" class="form-label">
                            Year <span class="required">*</span>
                        </label>
                        <select class="form-select" id="year" name="year" required>
                            <option value="1" ${student.year == 1 ? 'selected' : ''}>Year 1</option>
                            <option value="2" ${student.year == 2 ? 'selected' : ''}>Year 2</option>
                            <option value="3" ${student.year == 3 ? 'selected' : ''}>Year 3</option>
                            <option value="4" ${student.year == 4 ? 'selected' : ''}>Year 4</option>
                        </select>
                    </div>

                    <!-- Semester -->
                    <div class="col-md-6 mb-3">
                        <label for="semester" class="form-label">
                            Semester <span class="required">*</span>
                        </label>
                        <select class="form-select" id="semester" name="semester" required>
                            <option value="1" ${student.semester == 1 ? 'selected' : ''}>Semester 1</option>
                            <option value="2" ${student.semester == 2 ? 'selected' : ''}>Semester 2</option>
                        </select>
                    </div>

                    <!-- Status -->
                    <div class="col-md-6 mb-3">
                        <label for="status" class="form-label">
                            Status <span class="required">*</span>
                        </label>
                        <select class="form-select" id="status" name="status" required>
                            <option value="ACTIVE" ${student.status == 'ACTIVE' ? 'selected' : ''}>Active</option>
                            <option value="INACTIVE" ${student.status == 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                            <option value="SUSPENDED" ${student.status == 'SUSPENDED' ? 'selected' : ''}>Suspended</option>
                            <option value="GRADUATED" ${student.status == 'GRADUATED' ? 'selected' : ''}>Graduated</option>
                        </select>
                    </div>

                    <!-- Password (optional) -->
                    <div class="col-12 mb-3">
                        <label for="password" class="form-label">
                            New Password <small class="text-muted">(Leave blank to keep current password)</small>
                        </label>
                        <input type="password"
                               class="form-control"
                               id="password"
                               name="password"
                               placeholder="Enter new password to change">
                    </div>
                </div>

                <!-- Buttons -->
                <div class="btn-group-custom">
                    <button type="submit" class="btn-custom btn-update">
                        <i class="fas fa-save me-2"></i> Update Student
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/manage-students"
                       class="btn-custom btn-cancel">
                        <i class="fas fa-times me-2"></i> Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
