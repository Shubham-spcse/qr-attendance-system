<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QR Code Display - ${session.courseName}</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .qr-container {
            background: white;
            border-radius: 30px;
            padding: 50px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            max-width: 800px;
            width: 100%;
            text-align: center;
        }

        .header {
            margin-bottom: 30px;
        }

        .course-name {
            font-size: 2.5rem;
            font-weight: 800;
            color: #2d3748;
            margin-bottom: 10px;
        }

        .course-code {
            font-size: 1.2rem;
            color: #718096;
            font-weight: 600;
        }

        .session-info {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px;
            border-radius: 20px;
            margin: 30px 0;
        }

        .info-row {
            display: flex;
            justify-content: space-around;
            flex-wrap: wrap;
            gap: 20px;
        }

        .info-item {
            text-align: center;
        }

        .info-label {
            font-size: 0.9rem;
            opacity: 0.9;
            margin-bottom: 5px;
        }

        .info-value {
            font-size: 1.3rem;
            font-weight: 700;
        }

        .qr-code-wrapper {
            background: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);
            padding: 40px;
            border-radius: 25px;
            margin: 30px 0;
            box-shadow: inset 0 2px 10px rgba(0,0,0,0.05);
        }

        .qr-code-image {
            max-width: 400px;
            width: 100%;
            height: auto;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }

        .session-code-display {
            background: #2d3748;
            color: white;
            padding: 20px;
            border-radius: 15px;
            margin: 20px 0;
            font-family: 'Courier New', monospace;
        }

        .session-code-label {
            font-size: 0.9rem;
            opacity: 0.8;
            margin-bottom: 10px;
        }

        .session-code-value {
            font-size: 1.8rem;
            font-weight: 700;
            letter-spacing: 2px;
        }

        .instructions {
            background: #fff5f5;
            border-left: 4px solid #fc8181;
            padding: 20px;
            border-radius: 10px;
            text-align: left;
            margin: 30px 0;
        }

        .instructions h5 {
            color: #c53030;
            margin-bottom: 15px;
            font-weight: 700;
        }

        .instructions ol {
            margin: 0;
            padding-left: 20px;
        }

        .instructions li {
            margin-bottom: 10px;
            color: #2d3748;
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
            margin-top: 30px;
        }

        .btn-custom {
            padding: 12px 30px;
            border-radius: 12px;
            font-weight: 600;
            font-size: 1rem;
            transition: all 0.3s ease;
            border: none;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }

        .btn-primary-custom {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary-custom:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
            color: white;
        }

        .btn-secondary-custom {
            background: #e2e8f0;
            color: #2d3748;
        }

        .btn-secondary-custom:hover {
            background: #cbd5e0;
            transform: translateY(-2px);
        }

        .btn-success-custom {
            background: linear-gradient(135deg, #10b981 0%, #34d399 100%);
            color: white;
        }

        .btn-success-custom:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(16, 185, 129, 0.4);
        }

        .status-badge {
            display: inline-block;
            padding: 8px 20px;
            border-radius: 20px;
            font-weight: 600;
            font-size: 0.9rem;
            margin-bottom: 20px;
        }

        .status-active {
            background: linear-gradient(135deg, #10b981 0%, #34d399 100%);
            color: white;
        }

        .timer {
            font-size: 1.5rem;
            font-weight: 700;
            color: #667eea;
            margin: 20px 0;
        }

        .error-message {
            background: #fff5f5;
            border: 2px solid #fc8181;
            color: #c53030;
            padding: 20px;
            border-radius: 15px;
            margin: 20px 0;
        }

        .refresh-notice {
            background: #ebf8ff;
            border-left: 4px solid #4299e1;
            padding: 15px;
            border-radius: 10px;
            margin: 20px 0;
            text-align: left;
            color: #2c5282;
        }

        @media (max-width: 768px) {
            .qr-container {
                padding: 30px 20px;
            }

            .course-name {
                font-size: 1.8rem;
            }

            .qr-code-image {
                max-width: 300px;
            }

            .session-code-value {
                font-size: 1.3rem;
            }

            .info-row {
                flex-direction: column;
            }
        }

        /* Fullscreen Mode */
        .fullscreen-wrapper {
            position: fixed;
            top: 0;
            left: 0;
            width: 100vw;
            height: 100vh;
            background: white;
            z-index: 9999;
            display: none;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .fullscreen-wrapper.active {
            display: flex;
        }

        .fullscreen-content {
            text-align: center;
        }

        .fullscreen-qr {
            max-width: 600px;
            width: 90vw;
            height: auto;
        }

        .close-fullscreen {
            position: absolute;
            top: 20px;
            right: 20px;
            background: #e2e8f0;
            border: none;
            width: 50px;
            height: 50px;
            border-radius: 50%;
            font-size: 1.5rem;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .close-fullscreen:hover {
            background: #cbd5e0;
            transform: scale(1.1);
        }
    </style>
</head>
<body>
    <div class="qr-container">
        <c:if test="${not empty error}">
            <div class="error-message">
                <i class="fas fa-exclamation-triangle me-2"></i>
                <strong>Error:</strong> ${error}
            </div>
            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/staff/dashboard" class="btn-custom btn-secondary-custom">
                    <i class="fas fa-arrow-left"></i> Back to Dashboard
                </a>
            </div>
        </c:if>

        <c:if test="${empty error and not empty session}">
            <!-- Header -->
            <div class="header">
                <span class="status-badge status-active">
                    <i class="fas fa-circle me-2"></i> ACTIVE SESSION
                </span>
                <h1 class="course-name">${session.courseName}</h1>
                <p class="course-code">${session.courseCode}</p>
            </div>

            <!-- Session Information -->
            <div class="session-info">
                <div class="info-row">
                    <div class="info-item">
                        <div class="info-label">
                            <i class="fas fa-clock me-1"></i> Time
                        </div>
                        <div class="info-value">
                            <fmt:formatDate value="${session.startTime}" pattern="HH:mm"/> -
                            <fmt:formatDate value="${session.endTime}" pattern="HH:mm"/>
                        </div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">
                            <i class="fas fa-map-marker-alt me-1"></i> Location
                        </div>
                        <div class="info-value">${session.location}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">
                            <i class="fas fa-calendar-day me-1"></i> Date
                        </div>
                        <div class="info-value">
                            <fmt:formatDate value="${session.sessionDate}" pattern="dd MMM yyyy"/>
                        </div>
                    </div>
                </div>
            </div>

            <!-- QR Code Display -->
            <div class="qr-code-wrapper">
                <c:choose>
                    <c:when test="${not empty qrCodeImage}">
                        <img src="data:image/png;base64,${qrCodeImage}"
                             alt="QR Code"
                             class="qr-code-image"
                             id="qrCodeImg"/>
                    </c:when>
                    <c:otherwise>
                        <div class="error-message">
                            <i class="fas fa-qrcode fa-3x mb-3"></i>
                            <p>QR Code could not be generated. Please try again.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Session Code -->
            <div class="session-code-display">
                <div class="session-code-label">Session Code</div>
                <div class="session-code-value">${sessionCode}</div>
            </div>

            <!-- Auto-refresh Notice -->
            <div class="refresh-notice">
                <i class="fas fa-info-circle me-2"></i>
                <strong>Security Notice:</strong> This QR code refreshes every 5 minutes for security.
                The page will auto-reload to generate a new code.
            </div>

            <!-- Instructions -->
            <div class="instructions">
                <h5><i class="fas fa-mobile-alt me-2"></i> Instructions for Students</h5>
                <ol>
                    <li>Open the QR Attendance mobile app or website</li>
                    <li>Navigate to "Mark Attendance" or "Scan QR"</li>
                    <li>Point your camera at the QR code above</li>
                    <li>Wait for automatic scanning and confirmation</li>
                    <li>Verify your attendance is marked successfully</li>
                </ol>
            </div>

            <!-- Action Buttons -->
            <div class="action-buttons">
                <button onclick="enterFullscreen()" class="btn-custom btn-primary-custom">
                    <i class="fas fa-expand"></i> Fullscreen Mode
                </button>
                <button onclick="window.print()" class="btn-custom btn-secondary-custom">
                    <i class="fas fa-print"></i> Print QR Code
                </button>
                <a href="${pageContext.request.contextPath}/staff/generate-qr?sessionId=${session.sessionId}"
                   class="btn-custom btn-success-custom">
                    <i class="fas fa-sync-alt"></i> Refresh QR Code
                </a>
                <a href="${pageContext.request.contextPath}/staff/dashboard"
                   class="btn-custom btn-secondary-custom">
                    <i class="fas fa-arrow-left"></i> Back to Dashboard
                </a>
            </div>

            <!-- Auto-refresh Timer -->
            <div class="timer" id="timer">
                Auto-refresh in: <span id="countdown">5:00</span>
            </div>
        </c:if>
    </div>

    <!-- Fullscreen Wrapper -->
    <div class="fullscreen-wrapper" id="fullscreenWrapper">
        <button onclick="exitFullscreen()" class="close-fullscreen">
            <i class="fas fa-times"></i>
        </button>
        <div class="fullscreen-content">
            <h2 class="mb-4">${session.courseName} - ${session.courseCode}</h2>
            <img src="data:image/png;base64,${qrCodeImage}"
                 alt="QR Code Fullscreen"
                 class="fullscreen-qr"/>
            <h3 class="mt-4">Session Code: ${sessionCode}</h3>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- JavaScript -->
    <script>
        // Fullscreen Mode
        function enterFullscreen() {
            document.getElementById('fullscreenWrapper').classList.add('active');
        }

        function exitFullscreen() {
            document.getElementById('fullscreenWrapper').classList.remove('active');
        }

        // ESC key to exit fullscreen
        document.addEventListener('keydown', function(event) {
            if (event.key === 'Escape') {
                exitFullscreen();
            }
        });

        // Auto-refresh countdown (5 minutes = 300 seconds)
        let timeLeft = 300;

        function updateCountdown() {
            const minutes = Math.floor(timeLeft / 60);
            const seconds = timeLeft % 60;
            document.getElementById('countdown').textContent =
                minutes + ':' + (seconds < 10 ? '0' : '') + seconds;

            if (timeLeft <= 0) {
                // Auto-refresh page
                location.reload();
            } else {
                timeLeft--;
            }
        }

        // Update countdown every second
        setInterval(updateCountdown, 1000);

        // Print-friendly styles
        window.onbeforeprint = function() {
            document.body.style.background = 'white';
        };
    </script>
</body>
</html>

