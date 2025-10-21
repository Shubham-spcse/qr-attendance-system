<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Top Navigation Bar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top">
    <div class="container-fluid">
        <!-- Brand/Logo -->
        <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/${userRole != null ? userRole.toLowerCase() : ''}/dashboard">
            <i class="fas fa-qrcode me-2"></i>
            <span class="fw-bold">QR Attendance</span>
        </a>

        <!-- Mobile Toggle Button -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Navbar Content -->
        <div class="collapse navbar-collapse" id="navbarContent">
            <!-- Left Side Menu (Role-based) -->
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <c:if test="${sessionScope.userRole == 'STUDENT'}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/student/dashboard">
                            <i class="fas fa-home me-1"></i> Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/student/mark-attendance">
                            <i class="fas fa-qrcode me-1"></i> Mark Attendance
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/student/history">
                            <i class="fas fa-history me-1"></i> History
                        </a>
                    </li>
                </c:if>

                <c:if test="${sessionScope.userRole == 'STAFF'}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/staff/dashboard">
                            <i class="fas fa-home me-1"></i> Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/staff/create-session">
                            <i class="fas fa-plus-circle me-1"></i> Create Session
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/staff/session-history">
                            <i class="fas fa-list me-1"></i> Sessions
                        </a>
                    </li>
                </c:if>

                <c:if test="${sessionScope.userRole == 'ADMIN'}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">
                            <i class="fas fa-home me-1"></i> Dashboard
                        </a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="manageDropdown" role="button" 
                           data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fas fa-cog me-1"></i> Manage
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="manageDropdown">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/manage-students">
                                <i class="fas fa-user-graduate me-2"></i> Students
                            </a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/manage-staff">
                                <i class="fas fa-chalkboard-teacher me-2"></i> Staff
                            </a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/manage-courses">
                                <i class="fas fa-book me-2"></i> Courses
                            </a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/manage-departments">
                                <i class="fas fa-building me-2"></i> Departments
                            </a></li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/analytics">
                            <i class="fas fa-chart-bar me-1"></i> Analytics
                        </a>
                    </li>
                </c:if>
            </ul>

            <!-- Right Side Menu (User Info & Logout) -->
            <ul class="navbar-nav ms-auto">
                <c:if test="${sessionScope.user != null}">
                    <!-- User Info Dropdown -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="userDropdown" 
                           role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <div class="user-avatar me-2">
                                <i class="fas fa-user-circle fa-lg"></i>
                            </div>
                            <div class="user-info d-none d-md-block">
                                <small class="d-block text-light opacity-75">${sessionScope.userRole}</small>
                                <span class="fw-semibold">
                                    <c:choose>
                                        <c:when test="${sessionScope.userRole == 'STUDENT'}">
                                            ${sessionScope.user.name}
                                        </c:when>
                                        <c:when test="${sessionScope.userRole == 'STAFF'}">
                                            ${sessionScope.user.name}
                                        </c:when>
                                        <c:when test="${sessionScope.userRole == 'ADMIN'}">
                                            ${sessionScope.user.name}
                                        </c:when>
                                    </c:choose>
                                </span>
                            </div>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                            <li>
                                <div class="dropdown-header">
                                    <strong>
                                        <c:choose>
                                            <c:when test="${sessionScope.userRole == 'STUDENT'}">
                                                ${sessionScope.user.rollNumber}
                                            </c:when>
                                            <c:when test="${sessionScope.userRole == 'STAFF'}">
                                                ${sessionScope.user.staffCode}
                                            </c:when>
                                            <c:when test="${sessionScope.userRole == 'ADMIN'}">
                                                ${sessionScope.user.adminCode}
                                            </c:when>
                                        </c:choose>
                                    </strong><br>
                                    <small class="text-muted">${sessionScope.user.email}</small>
                                </div>
                            </li>
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                    <i class="fas fa-sign-out-alt me-2"></i> Logout
                                </a>
                            </li>
                        </ul>
                    </li>
                </c:if>

                <c:if test="${sessionScope.user == null}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">
                            <i class="fas fa-sign-in-alt me-1"></i> Login
                        </a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>
