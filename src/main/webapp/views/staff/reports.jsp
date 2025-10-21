<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attendance Reports - Staff</title>

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
            max-width: 1400px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .page-title {
            font-size: 2rem;
            font-weight: 700;
            color: white;
            margin-bottom: 30px;
        }

        .report-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .report-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e2e8f0;
        }

        .report-title {
            font-size: 1.5rem;
            font-weight: 700;
            color: #2d3748;
            margin: 0;
        }

        .btn-download {
            background: linear-gradient(135deg, #10b981 0%, #34d399 100%);
            color: white;
            padding: 10px 25px;
            border-radius: 10px;
            font-weight: 600;
            border: none;
            transition: all 0.3s ease;
        }

        .btn-download:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(16, 185, 129, 0.4);
        }

        .table {
            margin: 0;
        }

        .table thead {
            background: #f8f9fa;
        }

        .table thead th {
            font-weight: 700;
            color: #2d3748;
            padding: 15px;
            border-bottom: 2px solid #dee2e6;
        }

        .table tbody td {
            padding: 15px;
            vertical-align: middle;
        }

        .badge-present {
            background: linear-gradient(135deg, #10b981 0%, #34d399 100%);
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
        }

        .badge-late {
            background: linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%);
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

        .btn-back {
            background: white;
            color: #2d3748;
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
            color: #2d3748;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
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
            <i class="fas fa-chart-bar me-3"></i> Attendance Reports
        </h1>

        <!-- Course-wise Reports -->
        <c:choose>
            <c:when test="${not empty courseReports}">
                <c:forEach items="${courseReports}" var="report">
                    <div class="report-card">
                        <div class="report-header">
                            <div>
                                <h3 class="report-title">${report.courseName}</h3>
                                <p class="text-muted mb-0">${report.courseCode}</p>
                            </div>
                            <button class="btn-download" onclick="downloadReport('${report.courseId}')">
                                <i class="fas fa-download me-2"></i> Download CSV
                            </button>
                        </div>

                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Session</th>
                                        <th>Total Students</th>
                                        <th>Present</th>
                                        <th>Late</th>
                                        <th>Absent</th>
                                        <th>Attendance %</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${report.sessions}" var="session">
                                        <tr>
                                            <td><fmt:formatDate value="${session.sessionDate}" pattern="dd MMM yyyy"/></td>
                                            <td>${session.sessionCode}</td>
                                            <td>${session.totalStudents}</td>
                                            <td><span class="badge-present">${session.presentCount}</span></td>
                                            <td><span class="badge-late">${session.lateCount}</span></td>
                                            <td><span class="badge-absent">${session.absentCount}</span></td>
                                            <td><strong>${session.attendancePercentage}%</strong></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="report-card">
                    <div class="empty-state">
                        <i class="fas fa-file-alt"></i>
                        <h3>No Reports Available</h3>
                        <p>Attendance reports will appear here once sessions are completed</p>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function downloadReport(courseId) {
            alert('Downloading report for course ID: ' + courseId);
            // Implement actual download logic
        }
    </script>
</body>
</html>

