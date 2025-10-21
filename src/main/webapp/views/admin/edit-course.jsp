<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Course - QR Attendance</title>

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

        textarea.form-control {
            resize: vertical;
            min-height: 100px;
        }
    </style>
</head>
<body>
    <div class="edit-container">
        <div class="edit-card">
            <div class="edit-header">
                <h2><i class="fas fa-book me-2"></i> Edit Course</h2>
                <p>Update course information</p>
            </div>

            <form action="${pageContext.request.contextPath}/admin/manage-courses" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="courseId" value="${course.courseId}">

                <div class="row">
                    <!-- Course Code -->
                    <div class="col-md-6 mb-3">
                        <label for="courseCode" class="form-label">
                            Course Code <span class="required">*</span>
                        </label>
                        <input type="text"
                               class="form-control"
                               id="courseCode"
                               name="courseCode"
                               value="${course.courseCode}"
                               placeholder="e.g., CS101"
                               required>
                    </div>

                    <!-- Course Name -->
                    <div class="col-md-6 mb-3">
                        <label for="courseName" class="form-label">
                            Course Name <span class="required">*</span>
                        </label>
                        <input type="text"
                               class="form-control"
                               id="courseName"
                               name="courseName"
                               value="${course.courseName}"
                               placeholder="e.g., Data Structures"
                               required>
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
                                    ${course.departmentId == dept.departmentId ? 'selected' : ''}>
                                    ${dept.departmentName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Credits -->
                    <div class="col-md-6 mb-3">
                        <label for="credits" class="form-label">
                            Credits <span class="required">*</span>
                        </label>
                        <input type="number"
                               class="form-control"
                               id="credits"
                               name="credits"
                               value="${course.credits}"
                               min="1"
                               max="10"
                               required>
                    </div>

                    <!-- Semester -->
                    <div class="col-md-6 mb-3">
                        <label for="semester" class="form-label">
                            Semester <span class="required">*</span>
                        </label>
                        <select class="form-select" id="semester" name="semester" required>
                            <option value="1" ${course.semester == 1 ? 'selected' : ''}>Semester 1</option>
                            <option value="2" ${course.semester == 2 ? 'selected' : ''}>Semester 2</option>
                            <option value="3" ${course.semester == 3 ? 'selected' : ''}>Semester 3</option>
                            <option value="4" ${course.semester == 4 ? 'selected' : ''}>Semester 4</option>
                            <option value="5" ${course.semester == 5 ? 'selected' : ''}>Semester 5</option>
                            <option value="6" ${course.semester == 6 ? 'selected' : ''}>Semester 6</option>
                            <option value="7" ${course.semester == 7 ? 'selected' : ''}>Semester 7</option>
                            <option value="8" ${course.semester == 8 ? 'selected' : ''}>Semester 8</option>
                        </select>
                    </div>

                    <!-- Academic Year -->
                    <div class="col-md-6 mb-3">
                        <label for="academicYear" class="form-label">
                            Academic Year <span class="required">*</span>
                        </label>
                        <input type="text"
                               class="form-control"
                               id="academicYear"
                               name="academicYear"
                               value="${course.academicYear}"
                               placeholder="e.g., 2024-2025"
                               required>
                    </div>

                    <!-- Status -->
                    <div class="col-md-6 mb-3">
                        <label for="status" class="form-label">
                            Status <span class="required">*</span>
                        </label>
                        <select class="form-select" id="status" name="status" required>
                            <option value="ACTIVE" ${course.status == 'ACTIVE' ? 'selected' : ''}>Active</option>
                            <option value="INACTIVE" ${course.status == 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                            <option value="ARCHIVED" ${course.status == 'ARCHIVED' ? 'selected' : ''}>Archived</option>
                        </select>
                    </div>

                    <!-- Course Type -->
                    <div class="col-md-6 mb-3">
                        <label for="courseType" class="form-label">Course Type</label>
                        <select class="form-select" id="courseType" name="courseType">
                            <option value="THEORY" ${course.courseType == 'THEORY' ? 'selected' : ''}>Theory</option>
                            <option value="PRACTICAL" ${course.courseType == 'PRACTICAL' ? 'selected' : ''}>Practical</option>
                            <option value="THEORY_PRACTICAL" ${course.courseType == 'THEORY_PRACTICAL' ? 'selected' : ''}>Theory + Practical</option>
                        </select>
                    </div>

                    <!-- Description -->
                    <div class="col-12 mb-3">
                        <label for="description" class="form-label">Description</label>
                        <textarea class="form-control"
                                  id="description"
                                  name="description"
                                  rows="4"
                                  placeholder="Enter course description...">${course.description}</textarea>
                    </div>
                </div>

                <!-- Buttons -->
                <div class="btn-group-custom">
                    <button type="submit" class="btn-custom btn-update">
                        <i class="fas fa-save me-2"></i> Update Course
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/manage-courses"
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
