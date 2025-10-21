<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard - QR Attendance</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%);
            --secondary-gradient: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
            --success-gradient: linear-gradient(135deg, #10b981 0%, #34d399 100%);
            --warning-gradient: linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: #f8f9fa;
        }

        /* Top Header */
        .top-header {
            background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 50%, #6366f1 100%);
            color: white;
            padding: 20px 0;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }

        .top-header h1 {
            font-size: 1.8rem;
            font-weight: 700;
            margin: 0;
        }

        .user-info {
            text-align: right;
        }

        .user-name {
            font-size: 1.1rem;
            font-weight: 600;
        }

        .user-role {
            font-size: 0.85rem;
            opacity: 0.9;
        }

        /* QR SCANNER HIGHLIGHT CARD - NEW */
        .qr-scanner-highlight {
            background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%);
            border-radius: 20px;
            padding: 40px;
            text-align: center;
            box-shadow: 0 10px 30px rgba(236, 72, 153, 0.3);
            margin: 30px 0;
            position: relative;
            overflow: hidden;
        }

        .qr-scanner-highlight::before {
            content: '';
            position: absolute;
            top: -50%;
            right: -50%;
            width: 200%;
            height: 200%;
            background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
            animation: pulse-bg 3s ease-in-out infinite;
        }

        @keyframes pulse-bg {
            0%, 100% { transform: scale(1) rotate(0deg); }
            50% { transform: scale(1.1) rotate(5deg); }
        }

        .qr-icon-large {
            width: 100px;
            height: 100px;
            background: white;
            border-radius: 20px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 20px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            animation: float 3s ease-in-out infinite;
            position: relative;
            z-index: 1;
        }

        @keyframes float {
            0%, 100% { transform: translateY(0px); }
            50% { transform: translateY(-10px); }
        }

        .qr-icon-large i {
            font-size: 3.5rem;
            background: var(--primary-gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .qr-scanner-highlight h2 {
            color: white;
            font-size: 2rem;
            font-weight: 800;
            margin-bottom: 10px;
            position: relative;
            z-index: 1;
        }

        .qr-scanner-highlight p {
            color: rgba(255,255,255,0.9);
            font-size: 1.1rem;
            margin-bottom: 25px;
            position: relative;
            z-index: 1;
        }

        .btn-scan-main {
            background: white;
            color: #ec4899;
            border: none;
            padding: 18px 50px;
            border-radius: 15px;
            font-size: 1.2rem;
            font-weight: 700;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            transition: all 0.3s ease;
            position: relative;
            z-index: 1;
            text-decoration: none;
            display: inline-block;
        }

        .btn-scan-main:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 35px rgba(0,0,0,0.3);
            color: #ec4899;
        }

        .btn-scan-main i {
            margin-right: 10px;
            font-size: 1.3rem;
        }

        /* Stats Cards */
        .stats-section {
            margin: 30px 0;
        }

        .stat-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            text-align: center;
            box-shadow: 0 4px 6px rgba(0,0,0,0.07);
            transition: all 0.3s ease;
            border: 1px solid #f0f0f0;
        }

        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(236, 72, 153, 0.15);
        }

        .stat-icon {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 15px;
            font-size: 1.5rem;
            color: white;
        }

        .stat-icon.primary { background: var(--primary-gradient); }
        .stat-icon.success { background: var(--success-gradient); }
        .stat-icon.warning { background: var(--warning-gradient); }
        .stat-icon.secondary { background: var(--secondary-gradient); }

        .stat-number {
            font-size: 2.5rem;
            font-weight: 800;
            color: #1f2937;
            display: block;
            margin-bottom: 5px;
        }

        .stat-label {
            font-size: 0.95rem;
            color: #6b7280;
            font-weight: 500;
        }

        /* Section Headers */
        .section-header {
            margin: 40px 0 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #f0f0f0;
        }

        .section-title {
            font-size: 1.5rem;
            font-weight: 700;
            background: var(--primary-gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            display: inline-block;
        }

        /* Session Card */
        .session-card {
            background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%);
            color: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 15px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }

        .session-time {
            font-size: 1.2rem;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .session-info {
            font-size: 0.9rem;
            opacity: 0.95;
            margin-bottom: 15px;
        }

        .btn-scan {
            background: white;
            color: #ec4899;
            border: none;
            padding: 10px 25px;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }

        .btn-scan:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(255,255,255,0.3);
            color: #ec4899;
        }

        /* Attendance Table */
        .attendance-table {
            background: white;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 4px 6px rgba(0,0,0,0.07);
        }

        .table {
            margin: 0;
        }

        .table thead {
            background: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%);
            color: white;
        }

        .table thead th {
            border: none;
            padding: 15px;
            font-weight: 600;
        }

        .table tbody td {
            padding: 15px;
            vertical-align: middle;
        }

        .badge-present {
            background: var(--success-gradient);
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
        }

        .badge-late {
            background: var(--warning-gradient);
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
        }

        .badge-absent {
            background: linear-gradient(135deg, #ef4444 0%, #f87171 100%);
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
        }

        /* Empty State */
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #6b7280;
        }

        .empty-state i {
            font-size: 4rem;
            color: #d1d5db;
            margin-bottom: 20px;
        }

        .empty-state h4 {
            font-size: 1.3rem;
            color: #1f2937;
            margin-bottom: 10px;
        }

        .empty-state p {
            font-size: 0.95rem;
        }
    </style>
</head>
<body>
    <!-- Top Header -->
    <div class="top-header">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h1><i class="fas fa-qrcode me-2"></i> QR Attendance</h1>
                </div>
                <div class="col-md-6 user-info">
                    <div class="user-name">${sessionScope.user.name}</div>
                    <div class="user-role">
                        <i class="fas fa-user-graduate me-1"></i> Student | ${sessionScope.user.rollNumber}
                    </div>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-light mt-2">
                        <i class="fas fa-sign-out-alt me-1"></i> Logout
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Main Content -->
    <div class="container my-4">

        <!-- 🔥 QR SCANNER HIGHLIGHT CARD - NEW! -->
        <div class="qr-scanner-highlight">
            <div class="qr-icon-large">
                <i class="fas fa-qrcode"></i>
            </div>
            <h2>Mark Your Attendance</h2>
            <p>Scan QR code to mark your attendance for active sessions</p>
            <a href="${pageContext.request.contextPath}/student/mark-attendance" class="btn-scan-main">
                <i class="fas fa-camera"></i> Scan QR Code Now
            </a>
        </div>

        <!-- Statistics Cards -->
        <div class="stats-section">
            <div class="row g-4">
                <div class="col-md-3 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-icon primary">
                            <i class="fas fa-book"></i>
                        </div>
                        <span class="stat-number">${enrollments != null ? enrollments.size() : 0}</span>
                        <div class="stat-label">Enrolled Courses</div>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-icon success">
                            <i class="fas fa-check-circle"></i>
                        </div>
                        <span class="stat-number">${presentCount != null ? presentCount : 0}</span>
                        <div class="stat-label">Present</div>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-icon warning">
                            <i class="fas fa-clock"></i>
                        </div>
                        <span class="stat-number">${lateCount != null ? lateCount : 0}</span>
                        <div class="stat-label">Late</div>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-icon secondary">
                            <i class="fas fa-percentage"></i>
                        </div>
                        <span class="stat-number">${overallPercentage != null ? overallPercentage : 0}%</span>
                        <div class="stat-label">Overall Attendance</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Active Sessions -->
        <div class="section-header">
            <h2 class="section-title">
                <i class="fas fa-wifi me-2"></i> Active Sessions
            </h2>
        </div>

        <div class="row">
            <c:choose>
                <c:when test="${not empty activeSessions}">
                    <c:forEach items="${activeSessions}" var="session">
                        <div class="col-md-6">
                            <div class="session-card">
                                <div class="session-time">
                                    <i class="fas fa-clock me-2"></i>
                                    <fmt:formatDate value="${session.startTime}" pattern="HH:mm"/> -
                                    <fmt:formatDate value="${session.endTime}" pattern="HH:mm"/>
                                </div>
                                <div class="session-info">
                                    <strong>${session.courseName}</strong> (${session.courseCode})<br>
                                    <i class="fas fa-map-marker-alt me-1"></i> ${session.location}
                                </div>
                                <a href="${pageContext.request.contextPath}/student/mark-attendance"
                                   class="btn btn-scan">
                                    <i class="fas fa-qrcode me-2"></i> Scan QR Code
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12">
                        <div class="empty-state">
                            <i class="fas fa-calendar-times"></i>
                            <h4>No Active Sessions</h4>
                            <p>There are no active attendance sessions at the moment</p>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Recent Attendance -->
        <div class="section-header">
            <h2 class="section-title">
                <i class="fas fa-history me-2"></i> Recent Attendance
            </h2>
        </div>

        <div class="attendance-table">
            <c:choose>
                <c:when test="${not empty recentAttendance}">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Course</th>
                                <th>Time</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentAttendance}" var="attendance">
                                <tr>
                                    <td>
                                        <fmt:formatDate value="${attendance.markedAt}" pattern="dd MMM yyyy"/>
                                    </td>
                                    <td>
                                        <strong>${attendance.courseName}</strong><br>
                                        <small class="text-muted">${attendance.courseCode}</small>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${attendance.markedAt}" pattern="HH:mm"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${attendance.status == 'PRESENT'}">
                                                <span class="badge-present">
                                                    <i class="fas fa-check me-1"></i> Present
                                                </span>
                                            </c:when>
                                            <c:when test="${attendance.status == 'LATE'}">
                                                <span class="badge-late">
                                                    <i class="fas fa-clock me-1"></i> Late
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge-absent">
                                                    <i class="fas fa-times me-1"></i> Absent
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <i class="fas fa-clipboard-list"></i>
                        <h4>No Attendance Records</h4>
                        <p>Your attendance history will appear here once you start attending classes</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
