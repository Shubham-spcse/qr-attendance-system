<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Session History - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<div class="container-fluid mt-4">
    <h2 class="mb-4"><i class="fas fa-history me-2"></i> Session History</h2>

    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Course</th>
                            <th>Session Code</th>
                            <th>Status</th>
                            <th>Present</th>
                            <th>Late</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${allSessions}" var="session">
                            <tr>
                                <td><fmt:formatDate value="${session.sessionDate}" pattern="dd MMM yyyy"/></td>
                                <td>${session.courseName}</td>
                                <td><code>${session.sessionCode}</code></td>
                                <td><span class="badge bg-${session.status == 'ACTIVE' ? 'success' : 'secondary'}">${session.status}</span></td>
                                <td>${session.presentCount}</td>
                                <td>${session.lateCount}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/staff/session-report?sessionId=${session.sessionId}" 
                                       class="btn btn-sm btn-primary">View</a>
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
