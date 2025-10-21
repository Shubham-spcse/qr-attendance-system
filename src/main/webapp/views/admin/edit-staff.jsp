<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Staff - QR Attendance</title>

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
                <h2><i class="fas fa-chalkboard-teacher me-2"></i> Edit Staff Member</h2>
                <p>Update staff information</p>
            </div>

            <form action="${pageContext.request.contextPath}/admin/manage-staff" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="staffId" value="${staff.staffId}">

                <div class="row">
                    <!-- Employee ID -->
                    <div class="col-md-6 mb-3">
                        <label for="employeeId" class="form-label">
                            Employee ID <span class="required">*</span>
                        </label>
                        <input type="text"
                               class="form-control"
                               id="employeeId"
                               name="employeeId"
                               value="${staff.employeeId}"
                               required>
                    </div>

                    <!-- Full Name -->
                    <div class="col-md-6 mb-3">
                        <label for="name" class="form-label">
                            Full Name <span class="required">*</span>
                        </label>
                        <input type="text"
                               class="form-control"
                               id="name"
                               name="name"
                               value="${staff.name}"
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
                               value="${staff.email}"
                               required>
                    </div>

                    <!-- Phone -->
                    <div class="col-md-6 mb-3">
                        <label for="phone" class="form-label">Phone</label>
                        <input type="text"
                               class="form-control"
                               id="phone"
                               name="phone"
                               value="${staff.phone}">
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
                                    ${staff.departmentId == dept.departmentId ? 'selected' : ''}>
                                    ${dept.departmentName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Designation -->
                    <div class="col-md-6 mb-3">
                        <label for="designation" class="form-label">
                            Designation <span class="required">*</span>
                        </label>
                        <select class="form-select" id="designation" name="designation" required>
                            <option value="Professor" ${staff.designation == 'Professor' ? 'selected' : ''}>Professor</option>
                            <option value="Associate Professor" ${staff.designation == 'Associate Professor' ? 'selected' : ''}>Associate Professor</option>
                            <option value="Assistant Professor" ${staff.designation == 'Assistant Professor' ? 'selected' : ''}>Assistant Professor</option>
                            <option value="Lecturer" ${staff.designation == 'Lecturer' ? 'selected' : ''}>Lecturer</option>
                            <option value="Teaching Assistant" ${staff.designation == 'Teaching Assistant' ? 'selected' : ''}>Teaching Assistant</option>
                        </select>
                    </div>

                    <!-- Qualification -->
                    <div class="col-md-6 mb-3">
                        <label for="qualification" class="form-label">Qualification</label>
                        <input type="text"
                               class="form-control"
                               id="qualification"
                               name="qualification"
                               value="${staff.qualification}"
                               placeholder="e.g., Ph.D., M.Tech">
                    </div>

                    <!-- Status -->
                    <div class="col-md-6 mb-3">
                        <label for="status" class="form-label">
                            Status <span class="required">*</span>
                        </label>
                        <select class="form-select" id="status" name="status" required>
                            <option value="ACTIVE" ${staff.status == 'ACTIVE' ? 'selected' : ''}>Active</option>
                            <option value="INACTIVE" ${staff.status == 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                            <option value="ON_LEAVE" ${staff.status == 'ON_LEAVE' ? 'selected' : ''}>On Leave</option>
                            <option value="RETIRED" ${staff.status == 'RETIRED' ? 'selected' : ''}>Retired</option>
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

                    <!-- Office Location -->
                    <div class="col-12 mb-3">
                        <label for="officeLocation" class="form-label">Office Location</label>
                        <input type="text"
                               class="form-control"
                               id="officeLocation"
                               name="officeLocation"
                               value="${staff.officeLocation}"
                               placeholder="e.g., Room 301, Building A">
                    </div>
                </div>

                <!-- Buttons -->
                <div class="btn-group-custom">
                    <button type="submit" class="btn-custom btn-update">
                        <i class="fas fa-save me-2"></i> Update Staff
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/manage-staff"
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
