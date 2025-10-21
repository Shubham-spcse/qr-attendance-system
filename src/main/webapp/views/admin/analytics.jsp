<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="System Analytics - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<div class="container-fluid mt-4">
    <h2 class="mb-4"><i class="fas fa-chart-bar me-2"></i> System Analytics</h2>

    <!-- Report Type Selection -->
    <div class="row mb-4">
        <div class="col-md-12">
            <div class="btn-group" role="group">
                <a href="?type=attendance" class="btn btn-${param.type == 'attendance' || empty param.type ? 'primary' : 'outline-primary'}">
                    Attendance Analytics
                </a>
                <a href="?type=department" class="btn btn-${param.type == 'department' ? 'primary' : 'outline-primary'}">
                    Department Analytics
                </a>
                <a href="?type=course" class="btn btn-${param.type == 'course' ? 'primary' : 'outline-primary'}">
                    Course Analytics
                </a>
            </div>
        </div>
    </div>

    <!-- Attendance Analytics -->
    <c:if test="${param.type == 'attendance' || empty param.type}">
        <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Department-wise Attendance Report</h5>
                <a href="?type=attendance&action=download" class="btn btn-sm btn-success">
                    <i class="fas fa-download me-1"></i> Download CSV
                </a>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Department</th>
                                <th>Total Students</th>
                                <th>Total Sessions</th>
                                <th>Present</th>
                                <th>Late</th>
                                <th>Absent</th>
                                <th>Attendance %</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${departmentAttendance}" var="entry">
                                <tr>
                                    <td><strong>${entry.key}</strong></td>
                                    <td>${entry.value.studentCount}</td>
                                    <td>${entry.value.totalSessions}</td>
                                    <td><span class="badge bg-success">${entry.value.present}</span></td>
                                    <td><span class="badge bg-warning">${entry.value.late}</span></td>
                                    <td><span class="badge bg-danger">${entry.value.absent}</span></td>
                                    <td>
                                        <strong class="${entry.value.percentage >= 75 ? 'text-success' : 'text-danger'}">
                                            ${entry.value.percentage}%
                                        </strong>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Department Analytics -->
    <c:if test="${param.type == 'department'}">
        <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Department Statistics</h5>
                <a href="?type=department&action=download" class="btn btn-sm btn-success">
                    <i class="fas fa-download me-1"></i> Download CSV
                </a>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Department Code</th>
                                <th>Department Name</th>
                                <th>Total Students</th>
                                <th>Total Staff</th>
                                <th>Total Courses</th>
                                <th>HOD</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${departmentStats}" var="dept">
                                <tr>
                                    <td><strong>${dept.department.departmentCode}</strong></td>
                                    <td>${dept.department.departmentName}</td>
                                    <td>${dept.studentCount}</td>
                                    <td>${dept.staffCount}</td>
                                    <td>${dept.courseCount}</td>
                                    <td>${dept.department.headOfDepartment}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Course Analytics -->
    <c:if test="${param.type == 'course'}">
        <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Course Enrollment Statistics</h5>
                <a href="?type=course&action=download" class="btn btn-sm btn-success">
                    <i class="fas fa-download me-1"></i> Download CSV
                </a>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Course Code</th>
                                <th>Course Name</th>
                                <th>Department</th>
                                <th>Semester</th>
                                <th>Enrolled Students</th>
                                <th>Min Attendance</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${courseStats}" var="stat">
                                <tr>
                                    <td><strong>${stat.course.courseCode}</strong></td>
                                    <td>${stat.course.courseName}</td>
                                    <td>${stat.course.departmentName}</td>
                                    <td>${stat.course.semester}</td>
                                    <td><span class="badge bg-primary">${stat.enrolledStudents}</span></td>
                                    <td>${stat.course.minAttendancePercentage}%</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>

    <!-- System Overview -->
    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header bg-info text-white">
                    <h5 class="mb-0">System Overview</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-3 text-center mb-3">
                            <h2 class="text-primary">${totalDepartments}</h2>
                            <p class="mb-0">Total Departments</p>
                        </div>
                        <div class="col-md-3 text-center mb-3">
                            <h2 class="text-success">${totalStudents}</h2>
                            <p class="mb-0">Total Students</p>
                        </div>
                        <div class="col-md-3 text-center mb-3">
                            <h2 class="text-warning">${totalStaff}</h2>
                            <p class="mb-0">Total Staff</p>
                        </div>
                        <div class="col-md-3 text-center mb-3">
                            <h2 class="text-info">${totalCourses}</h2>
                            <p class="mb-0">Total Courses</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
