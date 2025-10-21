<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Dashboard - QR Attendance</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
            --secondary-gradient: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%);
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
            background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #ec4899 100%);
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

        /* Quick Actions */
        .quick-actions {
            margin: 30px 0;
        }

        .action-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            text-align: center;
            box-shadow: 0 4px 6px rgba(0,0,0,0.07);
            transition: all 0.3s ease;
            border: 1px solid #f0f0f0;
            height: 100%;
        }

        .action-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(99, 102, 241, 0.15);
        }

        .action-icon {
            width: 70px;
            height: 70px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
            font-size: 1.8rem;
            color: white;
        }

        .action-icon.primary { background: var(--primary-gradient); }
        .action-icon.secondary { background: var(--secondary-gradient); }
        .action-icon.success { background: var(--success-gradient); }
        .action-icon.warning { background: var(--warning-gradient); }

        .action-title {
            font-size: 1.1rem;
            font-weight: 700;
            color: #1f2937;
            margin-bottom: 10px;
        }

        .action-desc {
            font-size: 0.9rem;
            color: #6b7280;
            margin-bottom: 20px;
        }

        .btn-action {
            background: var(--primary-gradient);
            border: none;
            color: white;
            padding: 10px 25px;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }

        .btn-action:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 12px rgba(99, 102, 241, 0.4);
            color: white;
        }

        /* Stats Cards */
        .stats-section {
            margin: 30px 0;
        }

        .stat-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.07);
            border: 1px solid #f0f0f0;
            transition: all 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 15px rgba(99, 102, 241, 0.15);
        }

        .stat-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }

        .stat-label {
            font-size: 0.9rem;
            color: #6b7280;
            font-weight: 600;
        }

        .stat-icon-small {
            width: 40px;
            height: 40px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.2rem;
            color: white;
        }

        .stat-number {
            font-size: 2.5rem;
            font-weight: 800;
            color: #1f2937;
        }

        /* Section Headers */
        .section-header {
            margin: 40px 0 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #f0f0f0;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .section-title {
            font-size: 1.5rem;
            font-weight: 700;
            background: var(--primary-gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        /* Session Cards */
        .session-card {
            background: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.07);
            border-left: 4px solid transparent;
            transition: all 0.3s ease;
        }

        .session-card.active {
            border-left-color: #10b981;
            background: linear-gradient(90deg, rgba(16, 185, 129, 0.05) 0%, white 100%);
        }

        .session-card:hover {
            transform: translateX(5px);
            box-shadow: 0 6px 12px rgba(0,0,0,0.1);
        }

        .session-header {
            display: flex;
            justify-content: space-between;
            align-items: start;
            margin-bottom: 15px;
        }

        .session-course {
            font-size: 1.1rem;
            font-weight: 700;
            color: #1f2937;
        }

        .session-code {
            font-size: 0.8rem;
            color: #6b7280;
        }

        .session-status {
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: 600;
        }

        .status-active {
            background: var(--success-gradient);
            color: white;
        }

        .status-scheduled {
            background: var(--warning-gradient);
            color: white;
        }

        .status-completed {
            background: #e5e7eb;
            color: #6b7280;
        }

        .session-info {
            display: flex;
            gap: 20px;
            margin-bottom: 15px;
            font-size: 0.9rem;
            color: #6b7280;
        }

        .session-actions {
            display: flex;
            gap: 10px;
        }

        .btn-sm-action {
            padding: 8px 20px;
            border-radius: 8px;
            font-size: 0.85rem;
            font-weight: 600;
            border: none;
            transition: all 0.3s ease;
        }

        .btn-primary-gradient {
            background: var(--primary-gradient);
            color: white;
        }

        .btn-primary-gradient:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(99, 102, 241, 0.3);
        }

        .btn-outline-gradient {
            background: white;
            border: 2px solid #6366f1;
            color: #6366f1;
        }

        .btn-outline-gradient:hover {
            background: var(--primary-gradient);
            color: white;
            border-color: transparent;
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
    </style>
</head>
<body>
    <!-- Top Header -->
    <div class="top-header">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h1><i class="fas fa-chalkboard-teacher me-2"></i> Staff Dashboard</h1>
                </div>
                <div class="col-md-6 user-info">
                    <div class="user-name">${sessionScope.user.name}</div>
                    <div class="user-role">
                        <i class="fas fa-user-tie me-1"></i> Staff | ${sessionScope.user.staffCode}
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

        <!-- Quick Actions -->
        <div class="quick-actions">
            <div class="row g-4">
                <div class="col-md-3 col-sm-6">
                    <div class="action-card">
                        <div class="action-icon primary">
                            <i class="fas fa-plus-circle"></i>
                        </div>
                        <div class="action-title">Create Session</div>
                        <div class="action-desc">Start a new attendance session</div>
                        <a href="${pageContext.request.contextPath}/staff/create-session" class="btn-action">
                            Create Now
                        </a>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6">
                    <div class="action-card">
                        <div class="action-icon success">
                            <i class="fas fa-qrcode"></i>
                        </div>
                        <div class="action-title">Active Sessions</div>
                        <div class="action-desc">Monitor ongoing sessions</div>
                        <a href="${pageContext.request.contextPath}/staff/active-sessions" class="btn-action">
                            View Sessions
                        </a>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6">
                    <div class="action-card">
                        <div class="action-icon secondary">
                            <i class="fas fa-chart-bar"></i>
                        </div>
                        <div class="action-title">Reports</div>
                        <div class="action-desc">View attendance reports</div>
                        <a href="${pageContext.request.contextPath}/staff/reports" class="btn-action">
                            View Reports
                        </a>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6">
                    <div class="action-card">
                        <div class="action-icon warning">
                            <i class="fas fa-history"></i>
                        </div>
                        <div class="action-title">History</div>
                        <div class="action-desc">Past session records</div>
                        <a href="${pageContext.request.contextPath}/staff/session-history" class="btn-action">
                            View History
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Statistics -->
        <div class="stats-section">
            <div class="row g-4">
                <div class="col-md-3 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-header">
                            <span class="stat-label">Total Sessions</span>
                            <div class="stat-icon-small primary">
                                <i class="fas fa-calendar"></i>
                            </div>
                        </div>
                        <div class="stat-number">${totalSessions != null ? totalSessions : 0}</div>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-header">
                            <span class="stat-label">Active Today</span>
                            <div class="stat-icon-small success">
                                <i class="fas fa-wifi"></i>
                            </div>
                        </div>
                        <div class="stat-number">${activeSessions != null ? activeSessions.size() : 0}</div>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-header">
                            <span class="stat-label">My Courses</span>
                            <div class="stat-icon-small secondary">
                                <i class="fas fa-book"></i>
                            </div>
                        </div>
                        <div class="stat-number">${courses != null ? courses.size() : 0}</div>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6">
                    <div class="stat-card">
                        <div class="stat-header">
                            <span class="stat-label">Avg Attendance</span>
                            <div class="stat-icon-small warning">
                                <i class="fas fa-percentage"></i>
                            </div>
                        </div>
                        <div class="stat-number">${averageAttendance != null ? averageAttendance : 0}%</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Active Sessions -->
        <div class="section-header">
            <h2 class="section-title">
                <i class="fas fa-wifi me-2"></i> Active Sessions
            </h2>
            <a href="${pageContext.request.contextPath}/staff/create-session" class="btn btn-sm btn-primary-gradient">
                <i class="fas fa-plus me-1"></i> New Session
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty activeSessions}">
                <c:forEach items="${activeSessions}" var="session">
                    <div class="session-card active">
                        <div class="session-header">
                            <div>
                                <div class="session-course">${session.courseName}</div>
                                <div class="session-code">${session.courseCode}</div>
                            </div>
                            <span class="session-status status-active">
                                <i class="fas fa-circle me-1"></i> ACTIVE
                            </span>
                        </div>
                        <div class="session-info">
                            <span><i class="fas fa-clock me-1"></i>
                                <fmt:formatDate value="${session.startTime}" pattern="HH:mm"/> -
                                <fmt:formatDate value="${session.endTime}" pattern="HH:mm"/>
                            </span>
                            <span><i class="fas fa-map-marker-alt me-1"></i> ${session.location}</span>
                            <span><i class="fas fa-users me-1"></i> ${session.presentCount}/${session.totalStudents} Present</span>
                        </div>
                        <div class="session-actions">
                            <a href="${pageContext.request.contextPath}/staff/monitor-session?id=${session.sessionId}"
                               class="btn btn-sm-action btn-primary-gradient">
                                <i class="fas fa-eye me-1"></i> Monitor
                            </a>
                            <a href="${pageContext.request.contextPath}/staff/qr-display?id=${session.sessionId}"
                               class="btn btn-sm-action btn-outline-gradient">
                                <i class="fas fa-qrcode me-1"></i> Show QR
                            </a>
                            <a href="${pageContext.request.contextPath}/staff/end-session?id=${session.sessionId}"
                               class="btn btn-sm-action btn-outline-gradient">
                                <i class="fas fa-stop-circle me-1"></i> End
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-calendar-times"></i>
                    <h4>No Active Sessions</h4>
                    <p>Create a new session to start taking attendance</p>
                    <a href="${pageContext.request.contextPath}/staff/create-session" class="btn-action mt-3">
                        <i class="fas fa-plus me-2"></i> Create Session
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Recent Sessions -->
        <div class="section-header">
            <h2 class="section-title">
                <i class="fas fa-history me-2"></i> Recent Sessions
            </h2>
            <a href="${pageContext.request.contextPath}/staff/session-history" class="btn btn-sm btn-outline-gradient">
                View All <i class="fas fa-arrow-right ms-1"></i>
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty recentSessions}">
                <c:forEach items="${recentSessions}" var="session">
                    <div class="session-card">
                        <div class="session-header">
                            <div>
                                <div class="session-course">${session.courseName}</div>
                                <div class="session-code">${session.courseCode}</div>
                            </div>
                            <span class="session-status status-completed">
                                <i class="fas fa-check me-1"></i> COMPLETED
                            </span>
                        </div>
                        <div class="session-info">
                            <span><i class="fas fa-calendar me-1"></i>
                                <fmt:formatDate value="${session.sessionDate}" pattern="dd MMM yyyy"/>
                            </span>
                            <span><i class="fas fa-clock me-1"></i>
                                <fmt:formatDate value="${session.startTime}" pattern="HH:mm"/>
                            </span>
                            <span><i class="fas fa-users me-1"></i> ${session.presentCount}/${session.totalStudents}</span>
                        </div>
                        <div class="session-actions">
                            <a href="${pageContext.request.contextPath}/staff/session-report?id=${session.sessionId}"
                               class="btn btn-sm-action btn-outline-gradient">
                                <i class="fas fa-file-alt me-1"></i> View Report
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-inbox"></i>
                    <h4>No Recent Sessions</h4>
                    <p>Your completed sessions will appear here</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

