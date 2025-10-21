<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Attendance History - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<div class="container-fluid mt-4">
    <div class="row">
        <div class="col-md-12">
            <!-- Page Header -->
            <div class="mb-4">
                <h2 class="fw-bold">
                    <i class="fas fa-history me-2"></i> Attendance History
                </h2>
                <p class="text-muted">View your complete attendance records</p>
            </div>

            <!-- Filter Section -->
            <div class="card mb-4">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/student/history" method="get" class="row g-3">
                        <div class="col-md-4">
                            <label for="courseId" class="form-label">Filter by Course</label>
                            <select class="form-select" id="courseId" name="courseId" onchange="this.form.submit()">
                                <option value="">All Courses</option>
                                <c:forEach items="${enrollments}" var="enrollment">
                                    <option value="${enrollment.courseId}" 
                                            ${param.courseId == enrollment.courseId ? 'selected' : ''}>
                                        ${enrollment.courseCode} - ${enrollment.courseName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-8 d-flex align-items-end">
                            <a href="${pageContext.request.contextPath}/student/history" class="btn btn-secondary">
                                <i class="fas fa-redo me-1"></i> Reset Filter
                            </a>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Statistics for Selected Course -->
            <c:if test="${not empty param.courseId}">
                <div class="row mb-4">
                    <div class="col-md-3">
                        <div class="card text-center">
                            <div class="card-body">
                                <h3 class="text-primary">${totalSessions}</h3>
                                <p class="mb-0 text-muted">Total Sessions</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-center">
                            <div class="card-body">
                                <h3 class="text-success">${presentCount}</h3>
                                <p class="mb-0 text-muted">Present</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-center">
                            <div class="card-body">
                                <h3 class="text-warning">${lateCount}</h3>
                                <p class="mb-0 text-muted">Late</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-center">
                            <div class="card-body">
                                <h3 class="text-info">${percentage}%</h3>
                                <p class="mb-0 text-muted">Attendance</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Progress Bar -->
                <div class="card mb-4">
                    <div class="card-body">
                        <h6 class="mb-3">Attendance Progress</h6>
                        <div class="progress" style="height: 30px;">
                            <div class="progress-bar bg-success" role="progressbar" 
                                 style="width: ${(presentCount * 100) / totalSessions}%">
                                Present: ${presentCount}
                            </div>
                            <div class="progress-bar bg-warning" role="progressbar" 
                                 style="width: ${(lateCount * 100) / totalSessions}%">
                                Late: ${lateCount}
                            </div>
                            <div class="progress-bar bg-danger" role="progressbar" 
                                 style="width: ${(absentCount * 100) / totalSessions}%">
                                Absent: ${absentCount}
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Attendance Table -->
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Attendance Records</h5>
                    <a href="${pageContext.request.contextPath}/student/download-report${not empty param.courseId ? '?courseId='.concat(param.courseId) : ''}" 
                       class="btn btn-sm btn-success">
                        <i class="fas fa-download me-1"></i> Download CSV
                    </a>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty attendanceList}">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Date</th>
                                            <th>Course</th>
                                            <th>Session Code</th>
                                            <th>Time</th>
                                            <th>Status</th>
                                            <th>Marked At</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${attendanceList}" var="att" varStatus="status">
                                            <tr>
                                                <td>${status.index + 1}</td>
                                                <td>
                                                    <fmt:formatDate value="${att.markedAt}" pattern="dd MMM yyyy"/>
                                                </td>
                                                <td>
                                                    <strong>${att.courseName}</strong><br>
                                                    <small class="text-muted">${att.courseCode}</small>
                                                </td>
                                                <td><code>${att.sessionCode}</code></td>
                                                <td>
                                                    <small>
                                                        <fmt:formatDate value="${att.markedAt}" pattern="hh:mm a"/>
                                                    </small>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${att.status == 'PRESENT'}">
                                                            <span class="badge bg-success">
                                                                <i class="fas fa-check-circle"></i> Present
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${att.status == 'LATE'}">
                                                            <span class="badge bg-warning">
                                                                <i class="fas fa-clock"></i> Late
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-danger">
                                                                <i class="fas fa-times-circle"></i> Absent
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${att.markedAt}" pattern="dd MMM yyyy, hh:mm a"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-5">
                                <i class="fas fa-clipboard-list fa-4x text-muted mb-3"></i>
                                <h5 class="text-muted">No attendance records found</h5>
                                <p class="text-muted">
                                    <c:choose>
                                        <c:when test="${not empty param.courseId}">
                                            No records for the selected course
                                        </c:when>
                                        <c:otherwise>
                                            Start marking attendance to see your history here
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Course-wise Summary (All Courses View) -->
            <c:if test="${empty param.courseId && not empty statisticsByCourse}">
                <div class="card mt-4">
                    <div class="card-header">
                        <h5 class="mb-0">Course-wise Summary</h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Course</th>
                                        <th>Total Sessions</th>
                                        <th>Present</th>
                                        <th>Late</th>
                                        <th>Absent</th>
                                        <th>Percentage</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${enrollments}" var="enrollment">
                                        <c:set var="stats" value="${statisticsByCourse[enrollment.courseCode.concat(' - ').concat(enrollment.courseName)]}" />
                                        <tr>
                                            <td>
                                                <strong>${enrollment.courseName}</strong><br>
                                                <small class="text-muted">${enrollment.courseCode}</small>
                                            </td>
                                            <td>${stats.total}</td>
                                            <td><span class="badge bg-success">${stats.present}</span></td>
                                            <td><span class="badge bg-warning">${stats.late}</span></td>
                                            <td><span class="badge bg-danger">${stats.absent}</span></td>
                                            <td>
                                                <strong class="${stats.percentage >= 75 ? 'text-success' : 'text-danger'}">
                                                    ${stats.percentage}%
                                                </strong>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/student/history?courseId=${enrollment.courseId}" 
                                                   class="btn btn-sm btn-outline-primary">
                                                    View Details
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
