<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Active Sessions - Staff</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }

        .header {
            background: rgba(255, 255, 255, 0.95);
            padding: 20px 0;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .container-main {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .page-title {
            font-size: 2rem;
            font-weight: 700;
            color: #2d3748;
            margin-bottom: 30px;
        }

        .session-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 20px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            transition: all 0.3s ease;
        }

        .session-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(0,0,0,0.2);
        }

        .session-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e2e8f0;
        }

        .session-title {
            font-size: 1.5rem;
            font-weight: 700;
            color: #2d3748;
            margin: 0;
        }

        .status-badge {
            padding: 8px 20px;
            border-radius: 20px;
            font-weight: 600;
            font-size: 0.9rem;
        }

        .status-active {
            background: linear-gradient(135deg, #10b981 0%, #34d399 100%);
            color: white;
        }

        .session-info {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
            margin-bottom: 20px;
        }

        .info-item {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .info-icon {
            width: 40px;
            height: 40px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .info-content {
            flex: 1;
        }

        .info-label {
            font-size: 0.85rem;
            color: #6b7280;
            margin: 0;
        }

        .info-value {
            font-size: 1rem;
            font-weight: 600;
            color: #2d3748;
            margin: 0;
        }

        .session-actions {
            display: flex;
            gap: 15px;
        }

        .btn-custom {
            flex: 1;
            padding: 12px;
            border-radius: 10px;
            font-weight: 600;
            border: none;
            transition: all 0.3s ease;
        }

        .btn-end {
            background: linear-gradient(135deg, #ef4444 0%, #f87171 100%);
            color: white;
        }

        .btn-end:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(239, 68, 68, 0.4);
        }

        .btn-view {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-view:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-back {
            background: white;
            color: #667eea;
            padding: 10px 25px;
            border-radius: 10px;
            font-weight: 600;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 10px;
            transition: all 0.3s ease;
        }

        .btn-back:hover {
            background: #f3f4f6;
            color: #667eea;
        }

        .empty-state {
            background: white;
            border-radius: 15px;
            padding: 60px;
            text-align: center;
        }

        .empty-state i {
            font-size: 4rem;
            color: #d1d5db;
            margin-bottom: 20px;
        }

        .empty-state h3 {
            color: #2d3748;
            margin-bottom: 10px;
        }

        .empty-state p {
            color: #6b7280;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="container-main">
            <a href="${pageContext.request.contextPath}/staff/dashboard" class="btn-back">
                <i class="fas fa-arrow-left"></i> Back to Dashboard
            </a>
        </div>
    </div>

    <div class="container-main">
        <h1 class="page-title">
            <i class="fas fa-broadcast-tower me-3"></i> Active Sessions
        </h1>

        <c:choose>
            <c:when test="${not empty activeSessions}">
                <c:forEach items="${activeSessions}" var="session">
                    <div class="session-card">
                        <div class="session-header">
                            <h3 class="session-title">${session.courseName}</h3>
                            <span class="status-badge status-active">
                                <i class="fas fa-circle me-2"></i> Active
                            </span>
                        </div>

                        <div class="session-info">
                            <div class="info-item">
                                <div class="info-icon">
                                    <i class="fas fa-code"></i>
                                </div>
                                <div class="info-content">
                                    <p class="info-label">Session Code</p>
                                    <p class="info-value">${session.sessionCode}</p>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-icon">
                                    <i class="fas fa-clock"></i>
                                </div>
                                <div class="info-content">
                                    <p class="info-label">Time</p>
                                    <p class="info-value">
                                        <fmt:formatDate value="${session.startTime}" pattern="HH:mm"/> -
                                        <fmt:formatDate value="${session.endTime}" pattern="HH:mm"/>
                                    </p>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-icon">
                                    <i class="fas fa-map-marker-alt"></i>
                                </div>
                                <div class="info-content">
                                    <p class="info-label">Location</p>
                                    <p class="info-value">${session.location}</p>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-icon">
                                    <i class="fas fa-users"></i>
                                </div>
                                <div class="info-content">
                                    <p class="info-label">Students Marked</p>
                                    <p class="info-value">${session.attendanceCount} students</p>
                                </div>
                            </div>
                        </div>

                        <div class="session-actions">
                            <a href="${pageContext.request.contextPath}/staff/view-qr?sessionId=${session.sessionId}"
                               class="btn-custom btn-view">
                                <i class="fas fa-qrcode me-2"></i> View QR Code
                            </a>
                            <a href="${pageContext.request.contextPath}/staff/end-session?sessionId=${session.sessionId}"
                               class="btn-custom btn-end"
                               onclick="return confirm('Are you sure you want to end this session?')">
                                <i class="fas fa-stop-circle me-2"></i> End Session
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-calendar-times"></i>
                    <h3>No Active Sessions</h3>
                    <p>You don't have any active attendance sessions at the moment</p>
                    <a href="${pageContext.request.contextPath}/staff/create-session" class="btn-custom btn-view mt-3" style="max-width: 300px; margin: 20px auto 0;">
                        <i class="fas fa-plus me-2"></i> Create New Session
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
