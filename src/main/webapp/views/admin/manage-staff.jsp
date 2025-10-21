<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Manage Staff - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<div class="container-fluid mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="fas fa-chalkboard-teacher me-2"></i> Manage Staff</h2>
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addStaffModal">
            <i class="fas fa-plus me-2"></i> Add Staff
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
                            <th>Staff Code</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Department</th>
                            <th>Designation</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${staffList}" var="staff">
                            <tr>
                                <td>${staff.staffCode}</td>
                                <td>${staff.name}</td>
                                <td>${staff.email}</td>
                                <td>${staff.departmentName}</td>
                                <td>${staff.designation}</td>
                                <td><span class="badge bg-${staff.status == 'ACTIVE' ? 'success' : 'secondary'}">${staff.status}</span></td>
                                <td>
                                    <a href="?action=edit&id=${staff.staffId}" class="btn btn-sm btn-warning">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <button onclick="confirmDelete(${staff.staffId})" class="btn btn-sm btn-danger">
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

<!-- Add Staff Modal -->
<div class="modal fade" id="addStaffModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add New Staff</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/manage-staff" method="post">
                <input type="hidden" name="action" value="create">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Staff Code *</label>
                        <input type="text" class="form-control" name="staffCode" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Full Name *</label>
                        <input type="text" class="form-control" name="name" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email *</label>
                        <input type="email" class="form-control" name="email" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Phone</label>
                        <input type="text" class="form-control" name="phone">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Department *</label>
                        <select class="form-select" name="departmentId" required>
                            <c:forEach items="${departments}" var="dept">
                                <option value="${dept.departmentId}">${dept.departmentName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Designation *</label>
                        <input type="text" class="form-control" name="designation" placeholder="e.g., Professor" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Password *</label>
                        <input type="password" class="form-control" name="password" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Add Staff</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function confirmDelete(id) {
    if(confirm('Are you sure you want to delete this staff member?')) {
        window.location.href = '?action=delete&staffId=' + id;
    }
}
</script>

<%@ include file="../common/footer.jsp" %>
