<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Manage Departments - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<div class="container-fluid mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="fas fa-building me-2"></i> Manage Departments</h2>
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addDeptModal">
            <i class="fas fa-plus me-2"></i> Add Department
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
                            <th>Department Code</th>
                            <th>Department Name</th>
                            <th>HOD</th>
                            <th>Description</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${departments}" var="dept">
                            <tr>
                                <td><strong>${dept.departmentCode}</strong></td>
                                <td>${dept.departmentName}</td>
                                <td>${dept.headOfDepartment}</td>
                                <td>${dept.description}</td>
                                <td><span class="badge bg-${dept.status == 'ACTIVE' ? 'success' : 'secondary'}">${dept.status}</span></td>
                                <td>
                                    <a href="?action=edit&id=${dept.departmentId}" class="btn btn-sm btn-warning">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <button onclick="confirmDelete(${dept.departmentId})" class="btn btn-sm btn-danger">
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

<!-- Add Department Modal -->
<div class="modal fade" id="addDeptModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add New Department</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/manage-departments" method="post">
                <input type="hidden" name="action" value="create">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Department Code *</label>
                        <input type="text" class="form-control" name="departmentCode" placeholder="e.g., CSE" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Department Name *</label>
                        <input type="text" class="form-control" name="departmentName" placeholder="e.g., Computer Science Engineering" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Head of Department</label>
                        <input type="text" class="form-control" name="headOfDepartment">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Description</label>
                        <textarea class="form-control" name="description" rows="3"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Add Department</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function confirmDelete(id) {
    if(confirm('Are you sure you want to delete this department?')) {
        window.location.href = '?action=delete&departmentId=' + id;
    }
}
</script>

<%@ include file="../common/footer.jsp" %>
