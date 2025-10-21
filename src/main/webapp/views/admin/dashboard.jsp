<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - QR Attendance</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>

    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #ec4899 0%, #8b5cf6 100%);
            --secondary-gradient: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
            --success-gradient: linear-gradient(135deg, #10b981 0%, #34d399 100%);
            --warning-gradient: linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%);
            --danger-gradient: linear-gradient(135deg, #ef4444 0%, #f87171 100%);
            --info-gradient: linear-gradient(135deg, #3b82f6 0%, #60a5fa 100%);
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

        /* Stats Cards */
        .stats-section {
            margin: 30px 0;
        }

        .stat-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.07);
            transition: all 0.3s ease;
            border: 1px solid #f0f0f0;
            position: relative;
            overflow: hidden;
        }

        .stat-card::before {
            content: '';
            position: absolute;
            top: 0;
            right: 0;
            width: 100px;
            height: 100px;
            opacity: 0.1;
            border-radius: 50%;
        }

        .stat-card.primary::before {
            background: linear-gradient(135deg, #ec4899, #8b5cf6);
            transform: translate(30px, -30px);
        }

        .stat-card.success::before {
            background: linear-gradient(135deg, #10b981, #34d399);
            transform: translate(30px, -30px);
        }

        .stat-card.warning::before {
            background: linear-gradient(135deg, #f59e0b, #fbbf24);
            transform: translate(30px, -30px);
        }

        .stat-card.info::before {
            background: linear-gradient(135deg, #3b82f6, #60a5fa);
            transform: translate(30px, -30px);
        }

        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(236, 72, 153, 0.15);
        }

        .stat-icon {
            width: 60px;
            height: 60px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            color: white;
            margin-bottom: 15px;
        }

        .stat-icon.primary { background: var(--primary-gradient); }
        .stat-icon.success { background: var(--success-gradient); }
        .stat-icon.warning { background: var(--warning-gradient); }
        .stat-icon.info { background: var(--info-gradient); }

        .stat-number {
            font-size: 2.5rem;
            font-weight: 800;
            color: #1f2937;
            display: block;
            margin-bottom: 5px;
        }

        .stat-label {
            font-size: 0.9rem;
            color: #6b7280;
            font-weight: 600;
        }

        .stat-change {
            font-size: 0.8rem;
            margin-top: 10px;
        }

        .stat-change.positive {
            color: #10b981;
        }

        .stat-change.negative {
            color: #ef4444;
        }

        /* Quick Actions */
        .quick-actions {
            margin: 40px 0;
        }

        .action-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            text-align: center;
            box-shadow: 0 4px 6px rgba(0,0,0,0.07);
            transition: all 0.3s ease;
            border: 1px solid #f0f0f0;
            height: 100%;
        }

        .action-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(236, 72, 153, 0.15);
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
        .action-icon.info { background: var(--info-gradient); }

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
            box-shadow: 0 6px 12px rgba(236, 72, 153, 0.4);
            color: white;
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
        }

        /* Chart Cards */
        .chart-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.07);
            border: 1px solid #f0f0f0;
            margin-bottom: 20px;
        }

        .chart-title {
            font-size: 1.1rem;
            font-weight: 700;
            color: #1f2937;
            margin-bottom: 20px;
        }

        /* Recent Activity */
        .activity-item {
            display: flex;
            align-items: center;
            padding: 15px;
            border-bottom: 1px solid #f0f0f0;
            transition: all 0.3s ease;
        }

        .activity-item:last-child {
            border-bottom: none;
        }

        .activity-item:hover {
            background: #f9fafb;
        }

        .activity-icon {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
            font-size: 1rem;
            color: white;
        }

        .activity-content {
            flex: 1;
        }

        .activity-title {
            font-size: 0.9rem;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 5px;
        }

        .activity-time {
            font-size: 0.8rem;
            color: #6b7280;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .top-header h1 {
                font-size: 1.5rem;
            }
            .stat-number {
                font-size: 2rem;
            }
        }
    </style>
</head>
<body>
    <!-- Top Header -->
    <div class="top-header">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h1><i class="fas fa-user-shield me-2"></i> Admin Dashboard</h1>
                </div>
                <div class="col-md-6 user-info">
                    <div class="user-name">${sessionScope.user.name}</div>
                    <div class="user-role">
                        <i class="fas fa-crown me-1"></i> Administrator
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

        <!-- Statistics Cards -->
        <div class="stats-section">
            <div class="row g-4">
                <div class="col-lg-3 col-md-6">
                    <div class="stat-card primary">
                        <div class="stat-icon primary">
                            <i class="fas fa-users"></i>
                        </div>
                        <span class="stat-number">${totalStudents != null ? totalStudents : 0}</span>
                        <div class="stat-label">Total Students</div>
                        <div class="stat-change positive">
                            <i class="fas fa-arrow-up me-1"></i> +12% from last month
                        </div>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="stat-card success">
                        <div class="stat-icon success">
                            <i class="fas fa-user-tie"></i>
                        </div>
                        <span class="stat-number">${totalStaff != null ? totalStaff : 0}</span>
                        <div class="stat-label">Total Staff</div>
                        <div class="stat-change positive">
                            <i class="fas fa-arrow-up me-1"></i> +5% from last month
                        </div>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="stat-card warning">
                        <div class="stat-icon warning">
                            <i class="fas fa-book"></i>
                        </div>
                        <span class="stat-number">${totalCourses != null ? totalCourses : 0}</span>
                        <div class="stat-label">Active Courses</div>
                        <div class="stat-change positive">
                            <i class="fas fa-arrow-up me-1"></i> +8% from last month
                        </div>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="stat-card info">
                        <div class="stat-icon info">
                            <i class="fas fa-calendar-check"></i>
                        </div>
                        <span class="stat-number">${activeSessionsCount != null ? activeSessionsCount : 0}</span>
                        <div class="stat-label">Active Sessions</div>
                        <jsp:useBean id="currentDate" class="java.util.Date"/>
                        <div class="stat-change">
                            <i class="fas fa-calendar me-1"></i> <fmt:formatDate value="${currentDate}" pattern="dd MMM yyyy"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="quick-actions">
            <div class="row g-4">
                <div class="col-lg-3 col-md-6">
                    <div class="action-card">
                        <div class="action-icon primary">
                            <i class="fas fa-user-graduate"></i>
                        </div>
                        <div class="action-title">Manage Students</div>
                        <div class="action-desc">Add, edit, or remove students</div>
                        <a href="${pageContext.request.contextPath}/admin/manage-students" class="btn-action">
                            Manage
                        </a>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="action-card">
                        <div class="action-icon success">
                            <i class="fas fa-chalkboard-teacher"></i>
                        </div>
                        <div class="action-title">Manage Staff</div>
                        <div class="action-desc">Add, edit, or remove staff</div>
                        <a href="${pageContext.request.contextPath}/admin/manage-staff" class="btn-action">
                            Manage
                        </a>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="action-card">
                        <div class="action-icon secondary">
                            <i class="fas fa-book-open"></i>
                        </div>
                        <div class="action-title">Manage Courses</div>
                        <div class="action-desc">Create and manage courses</div>
                        <a href="${pageContext.request.contextPath}/admin/manage-courses" class="btn-action">
                            Manage
                        </a>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="action-card">
                        <div class="action-icon info">
                            <i class="fas fa-chart-bar"></i>
                        </div>
                        <div class="action-title">Analytics</div>
                        <div class="action-desc">View system analytics</div>
                        <a href="${pageContext.request.contextPath}/admin/analytics" class="btn-action">
                            View Analytics
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Charts Section -->
        <div class="row">
            <div class="col-lg-8">
                <div class="section-header">
                    <h2 class="section-title">
                        <i class="fas fa-chart-line me-2"></i> Attendance Trends
                    </h2>
                </div>

                <div class="chart-card">
                    <div class="chart-title">Monthly Attendance Overview</div>
                    <canvas id="attendanceChart" height="80"></canvas>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="section-header">
                    <h2 class="section-title">
                        <i class="fas fa-bell me-2"></i> Recent Activity
                    </h2>
                </div>

                <div class="chart-card">
                    <div class="activity-item">
                        <div class="activity-icon primary">
                            <i class="fas fa-user-plus"></i>
                        </div>
                        <div class="activity-content">
                            <div class="activity-title">New student registered</div>
                            <div class="activity-time">2 hours ago</div>
                        </div>
                    </div>

                    <div class="activity-item">
                        <div class="activity-icon success">
                            <i class="fas fa-qrcode"></i>
                        </div>
                        <div class="activity-content">
                            <div class="activity-title">Session created</div>
                            <div class="activity-time">5 hours ago</div>
                        </div>
                    </div>

                    <div class="activity-item">
                        <div class="activity-icon info">
                            <i class="fas fa-book"></i>
                        </div>
                        <div class="activity-content">
                            <div class="activity-title">New course added</div>
                            <div class="activity-time">1 day ago</div>
                        </div>
                    </div>

                    <div class="activity-item">
                        <div class="activity-icon warning">
                            <i class="fas fa-user-tie"></i>
                        </div>
                        <div class="activity-content">
                            <div class="activity-title">Staff member updated</div>
                            <div class="activity-time">2 days ago</div>
                        </div>
                    </div>

                    <div class="activity-item">
                        <div class="activity-icon primary">
                            <i class="fas fa-chart-bar"></i>
                        </div>
                        <div class="activity-content">
                            <div class="activity-title">Report generated</div>
                            <div class="activity-time">3 days ago</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Chart.js Configuration -->
    <script>
        // Attendance Trend Chart
        const ctx = document.getElementById('attendanceChart');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                datasets: [{
                    label: 'Attendance Rate (%)',
                    data: [85, 87, 90, 88, 92, 89, 91, 93, 90, 94, 92, 95],
                    borderColor: '#ec4899',
                    backgroundColor: 'rgba(236, 72, 153, 0.1)',
                    borderWidth: 3,
                    tension: 0.4,
                    fill: true,
                    pointBackgroundColor: '#ec4899',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 5,
                    pointHoverRadius: 7
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: false,
                        min: 80,
                        max: 100,
                        grid: {
                            color: '#f0f0f0'
                        }
                    },
                    x: {
                        grid: {
                            display: false
                        }
                    }
                }
            }
        });
    </script>
</body>
</html>
