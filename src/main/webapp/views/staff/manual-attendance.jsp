<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Manual Attendance - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<div class="container mt-4">
    <h2 class="mb-4"><i class="fas fa-edit me-2"></i> Manual Attendance</h2>

    <div class="card">
        <div class="card-header"><h5>Session: ${session.courseName} - ${session.sessionCode}</h5></div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/staff/manual-attendance" method="post">
                <input type="hidden" name="sessionId" value="${session.sessionId}">
                <input type="hidden" name="action" value="markAll">

                <div class="mb-3">
                    <label class="form-label">Mark all unmarked as:</label>
                    <select class="form-select" name="bulkStatus">
                        <option value="PRESENT">Present</option>
                        <option value="ABSENT">Absent</option>
                        <option value="LATE">Late</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Apply to All</button>
            </form>

            <hr>

            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Roll No</th>
                            <th>Name</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${studentAttendanceList}" var="item">
                            <tr>
                                <td>${item.enrollment.rollNumber}</td>
                                <td>${item.enrollment.studentName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.attendance != null}">
                                            <span class="badge bg-success">${item.attendance.status}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">Not Marked</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/staff/manual-attendance" 
                                          method="post" class="d-inline">
                                        <input type="hidden" name="sessionId" value="${session.sessionId}">
                                        <input type="hidden" name="studentId" value="${item.enrollment.studentId}">
                                        <input type="hidden" name="action" value="markIndividual">
                                        <select name="status" class="form-select form-select-sm d-inline w-auto" onchange="this.form.submit()">
                                            <option value="">Change...</option>
                                            <option value="PRESENT">Present</option>
                                            <option value="LATE">Late</option>
                                            <option value="ABSENT">Absent</option>
                                        </select>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
