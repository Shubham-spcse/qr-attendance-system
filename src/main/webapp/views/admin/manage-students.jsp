<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Manage Students - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<div class="container-fluid mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="fas fa-user-graduate me-2"></i> Manage Students</h2>
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addStudentModal">
            <i class="fas fa-plus me-2"></i> Add Student
        </button>
    </div>

    <!-- Success/Error Messages -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="fas fa-check-circle me-2"></i> ${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="fas fa-exclamation-circle me-2"></i> ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <!-- Students Table -->
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Roll Number</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Department</th>
                            <th>Year</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${students}" var="student">
                            <tr>
                                <td>${student.rollNumber}</td>
                                <td>${student.name}</td>
                                <td>${student.email}</td>
                                <td>${student.departmentName}</td>
                                <td>Year ${student.year}, Sem ${student.semester}</td>
                                <td><span class="badge bg-${student.status == 'ACTIVE' ? 'success' : 'secondary'}">${student.status}</span></td>
                                <td>
                                    <a href="?action=edit&studentId=${student.studentId}" class="btn btn-sm btn-warning">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <button onclick="confirmDelete(${student.studentId})" class="btn btn-sm btn-danger">
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

<!-- Add Student Modal -->
<div class="modal fade" id="addStudentModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add New Student</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/manage-students" method="post">
                <input type="hidden" name="action" value="add">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Roll Number *</label>
                            <input type="text" class="form-control" name="rollNumber" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Full Name *</label>
                            <input type="text" class="form-control" name="name" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Email *</label>
                            <input type="email" class="form-control" name="email" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Phone</label>
                            <input type="text" class="form-control" name="phone">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Department *</label>
                            <select class="form-select" name="departmentId" required>
                                <c:forEach items="${departments}" var="dept">
                                    <option value="${dept.departmentId}">${dept.departmentName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2 mb-3">
                            <label class="form-label">Year *</label>
                            <select class="form-select" name="year" required>
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                            </select>
                        </div>
                        <div class="col-md-2 mb-3">
                            <label class="form-label">Semester *</label>
                            <select class="form-select" name="semester" required>
                                <c:forEach begin="1" end="8" var="i">
                                    <option value="${i}">${i}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Password *</label>
                            <input type="password" class="form-control" name="password" required>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Add Student</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function confirmDelete(id) {
    if(confirm('Are you sure you want to delete this student?')) {
        window.location.href = '?action=delete&studentId=' + id;
    }
}
</script>

<%@ include file="../common/footer.jsp" %>
