<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Manage Courses - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<div class="container-fluid mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="fas fa-book me-2"></i> Manage Courses</h2>
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addCourseModal">
            <i class="fas fa-plus me-2"></i> Add Course
        </button>
    </div>

    <c:if test="${not empty success}">
        <div class="alert alert-success alert-dismissible fade show">
            ${success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Course Code</th>
                            <th>Course Name</th>
                            <th>Department</th>
                            <th>Semester</th>
                            <th>Credits</th>
                            <th>Min Attendance</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${courses}" var="course">
                            <tr>
                                <td><strong>${course.courseCode}</strong></td>
                                <td>${course.courseName}</td>
                                <td>${course.departmentName}</td>
                                <td>${course.semester}</td>
                                <td>${course.credits}</td>
                                <td>${course.minAttendancePercentage}%</td>
                                <td><span class="badge bg-${course.status == 'ACTIVE' ? 'success' : 'secondary'}">${course.status}</span></td>
                                <td>
                                    <a href="?action=edit&id=${course.courseId}" class="btn btn-sm btn-warning">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <button onclick="confirmDelete(${course.courseId})" class="btn btn-sm btn-danger">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Add Course Modal -->
<div class="modal fade" id="addCourseModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add New Course</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/manage-courses" method="post">
                <input type="hidden" name="action" value="create">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Course Code *</label>
                            <input type="text" class="form-control" name="courseCode" placeholder="e.g., CS101" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Course Name *</label>
                            <input type="text" class="form-control" name="courseName" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Department *</label>
                            <select class="form-select" name="departmentId" required>
                                <c:forEach items="${departments}" var="dept">
                                    <option value="${dept.departmentId}">${dept.departmentName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3 mb-3">
                            <label class="form-label">Semester *</label>
                            <select class="form-select" name="semester" required>
                                <c:forEach begin="1" end="8" var="i">
                                    <option value="${i}">${i}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3 mb-3">
                            <label class="form-label">Credits *</label>
                            <input type="number" class="form-control" name="credits" min="1" max="6" value="3" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Academic Year *</label>
                            <input type="text" class="form-control" name="academicYear" placeholder="2025-26" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Min Attendance % *</label>
                            <input type="number" class="form-control" name="minAttendance" min="0" max="100" value="75" required>
                        </div>
                        <div class="col-md-12 mb-3">
                            <label class="form-label">Description</label>
                            <textarea class="form-control" name="description" rows="2"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Add Course</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function confirmDelete(id) {
    if(confirm('Are you sure you want to delete this course?')) {
        window.location.href = '?action=delete&courseId=' + id;
    }
}
</script>

<%@ include file="../common/footer.jsp" %>
