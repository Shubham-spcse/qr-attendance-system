<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="End Session - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header bg-warning">
                    <h4 class="mb-0"><i class="fas fa-exclamation-triangle me-2"></i> End Session</h4>
                </div>
                <div class="card-body">
                    <h5>Are you sure you want to end this session?</h5>
                    <p class="text-muted">Session: ${session.courseName} (${session.sessionCode})</p>

                    <div class="alert alert-info">
                        <p><strong>Total Students:</strong> ${totalStudents}</p>
                        <p><strong>Marked:</strong> ${markedCount}</p>
                        <p><strong>Unmarked:</strong> ${unmarkedCount}</p>
                    </div>

                    <form action="${pageContext.request.contextPath}/staff/end-session" method="post">
                        <input type="hidden" name="sessionId" value="${session.sessionId}">

                        <div class="mb-3">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="markAbsentFor" 
                                       value="unmarked" id="markAbsent" checked>
                                <label class="form-check-label" for="markAbsent">
                                    Mark all unmarked students as ABSENT
                                </label>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-danger w-100">
                            <i class="fas fa-stop-circle me-2"></i> End Session
                        </button>
                    </form>

                    <a href="${pageContext.request.contextPath}/staff/monitor-session?sessionId=${session.sessionId}" 
                       class="btn btn-secondary w-100 mt-2">Cancel</a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
