<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Generate QR Code - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-lg">
                <div class="card-header bg-success text-white">
                    <h4 class="mb-0"><i class="fas fa-qrcode me-2"></i> QR Code - Attendance Session</h4>
                </div>
                <div class="card-body p-4">
                    <!-- Session Info -->
                    <div class="alert alert-info">
                        <h5 class="mb-3"><i class="fas fa-info-circle me-2"></i> Session Details</h5>
                        <p class="mb-1"><strong>Course:</strong> ${session.courseName}</p>
                        <p class="mb-1"><strong>Session Code:</strong> <code>${session.sessionCode}</code></p>
                        <p class="mb-1"><strong>Location:</strong> ${session.location}</p>
                        <p class="mb-1"><strong>Time:</strong> ${session.startTime} - ${session.endTime}</p>
                        <p class="mb-0"><strong>Total Students:</strong> ${session.totalStudents}</p>
                    </div>

                    <!-- QR Code Display -->
                    <div class="qr-code-container text-center py-4">
                        <h5 class="mb-3">Students, scan this QR code to mark attendance</h5>
                        <c:choose>
                            <c:when test="${not empty qrCodeBase64}">
                                <img src="data:image/png;base64,${qrCodeBase64}" 
                                     alt="QR Code" class="img-fluid" style="max-width: 400px;">
                            </c:when>
                            <c:otherwise>
                                <div id="qr-display"></div>
                            </c:otherwise>
                        </c:choose>

                        <div class="mt-4">
                            <p class="text-muted">Session Code: <strong>${session.sessionCode}</strong></p>
                            <p class="text-muted small">QR code refreshes every 5 minutes for security</p>
                        </div>
                    </div>

                    <!-- Action Buttons -->
                    <div class="row mt-4">
                        <div class="col-md-6 mb-2">
                            <a href="${pageContext.request.contextPath}/staff/monitor-session?sessionId=${session.sessionId}" 
                               class="btn btn-primary btn-lg w-100">
                                <i class="fas fa-eye me-2"></i> Monitor Session
                            </a>
                        </div>
                        <div class="col-md-6 mb-2">
                            <button onclick="refreshQR()" class="btn btn-outline-success btn-lg w-100">
                                <i class="fas fa-sync me-2"></i> Refresh QR
                            </button>
                        </div>
                    </div>

                    <div class="mt-3">
                        <form action="${pageContext.request.contextPath}/staff/end-session" method="post" 
                              onsubmit="return confirm('Are you sure you want to end this session?');">
                            <input type="hidden" name="sessionId" value="${session.sessionId}">
                            <input type="hidden" name="markAbsentFor" value="unmarked">
                            <button type="submit" class="btn btn-danger w-100">
                                <i class="fas fa-stop-circle me-2"></i> End Session
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Instructions -->
            <div class="card mt-3">
                <div class="card-body">
                    <h6><i class="fas fa-lightbulb me-2"></i> Instructions:</h6>
                    <ol class="mb-0 small">
                        <li>Display this QR code to students using a projector or large screen</li>
                        <li>Students will scan using their mobile cameras</li>
                        <li>Monitor real-time attendance in "Monitor Session"</li>
                        <li>Click "End Session" when class is over</li>
                        <li>QR code automatically refreshes for security</li>
                    </ol>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function refreshQR() {
    location.reload();
}
</script>

<%@ include file="../common/footer.jsp" %>
