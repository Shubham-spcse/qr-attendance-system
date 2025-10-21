<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Sidebar (Optional - can be toggled on larger screens) -->
<div class="sidebar bg-light border-end" id="sidebar">
    <div class="sidebar-header p-3 border-bottom">
        <h6 class="mb-0 fw-bold text-primary">
            <c:choose>
                <c:when test="${sessionScope.userRole == 'STUDENT'}">
                    <i class="fas fa-user-graduate me-2"></i> Student Portal
                </c:when>
                <c:when test="${sessionScope.userRole == 'STAFF'}">
                    <i class="fas fa-chalkboard-teacher me-2"></i> Staff Portal
                </c:when>
                <c:when test="${sessionScope.userRole == 'ADMIN'}">
                    <i class="fas fa-user-shield me-2"></i> Admin Portal
                </c:when>
            </c:choose>
        </h6>
    </div>

    <div class="sidebar-menu p-3">
        <c:if test="${sessionScope.userRole == 'STUDENT'}">
            <ul class="nav flex-column">
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('dashboard') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/student/dashboard">
                        <i class="fas fa-home me-2"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('mark-attendance') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/student/mark-attendance">
                        <i class="fas fa-qrcode me-2"></i> Mark Attendance
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('history') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/student/history">
                        <i class="fas fa-history me-2"></i> Attendance History
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a class="nav-link" href="${pageContext.request.contextPath}/student/download-report">
                        <i class="fas fa-download me-2"></i> Download Report
                    </a>
                </li>
            </ul>
        </c:if>

        <c:if test="${sessionScope.userRole == 'STAFF'}">
            <ul class="nav flex-column">
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('dashboard') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/staff/dashboard">
                        <i class="fas fa-home me-2"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('create-session') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/staff/create-session">
                        <i class="fas fa-plus-circle me-2"></i> Create Session
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('session-history') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/staff/session-history">
                        <i class="fas fa-list me-2"></i> Session History
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('manual-attendance') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/staff/manual-attendance">
                        <i class="fas fa-edit me-2"></i> Manual Attendance
                    </a>
                </li>
            </ul>
        </c:if>

        <c:if test="${sessionScope.userRole == 'ADMIN'}">
            <ul class="nav flex-column">
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('dashboard') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/admin/dashboard">
                        <i class="fas fa-home me-2"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item mb-3">
                    <h6 class="text-muted small text-uppercase">Manage</h6>
                </li>
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('manage-students') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/admin/manage-students">
                        <i class="fas fa-user-graduate me-2"></i> Students
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('manage-staff') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/admin/manage-staff">
                        <i class="fas fa-chalkboard-teacher me-2"></i> Staff
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('manage-courses') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/admin/manage-courses">
                        <i class="fas fa-book me-2"></i> Courses
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('manage-departments') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/admin/manage-departments">
                        <i class="fas fa-building me-2"></i> Departments
                    </a>
                </li>
                <li class="nav-item mb-3">
                    <h6 class="text-muted small text-uppercase mt-3">Reports</h6>
                </li>
                <li class="nav-item mb-2">
                    <a class="nav-link ${pageContext.request.requestURI.contains('analytics') ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/admin/analytics">
                        <i class="fas fa-chart-bar me-2"></i> Analytics
                    </a>
                </li>
            </ul>
        </c:if>
    </div>
</div>
