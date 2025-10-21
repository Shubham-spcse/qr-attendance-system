/**
 * Attendance Management JavaScript
 * Handles attendance marking, validation, and UI updates
 */

// Global variables
let currentSessionId = null;
let studentData = null;

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    console.log('Attendance.js initialized');
    initializeAttendanceFunctions();
});

/**
 * Initialize all attendance-related functions
 */
function initializeAttendanceFunctions() {
    // Auto-refresh active sessions every 30 seconds
    if (document.getElementById('active-sessions-container')) {
        setInterval(refreshActiveSessions, 30000);
    }

    // Initialize attendance marking buttons
    initializeMarkAttendanceButtons();

    // Initialize manual attendance forms
    initializeManualAttendanceForms();
}

/**
 * Refresh active sessions list
 */
function refreshActiveSessions() {
    const container = document.getElementById('active-sessions-container');
    if (!container) return;

    fetch(contextPath + '/student/active-sessions')
        .then(response => response.json())
        .then(data => {
            updateActiveSessionsUI(data);
        })
        .catch(error => {
            console.error('Error refreshing sessions:', error);
        });
}

/**
 * Update active sessions UI
 */
function updateActiveSessionsUI(sessions) {
    const container = document.getElementById('active-sessions-container');
    if (!container) return;

    if (sessions.length === 0) {
        container.innerHTML = '<div class="alert alert-info">No active sessions at the moment</div>';
        return;
    }

    let html = '<div class="row g-3">';
    sessions.forEach(session => {
        html += `
            <div class="col-md-6">
                <div class="card border-primary">
                    <div class="card-body">
                        <h5 class="card-title">${session.courseName}</h5>
                        <p class="card-text">
                            <strong>Time:</strong> ${session.startTime} - ${session.endTime}<br>
                            <strong>Location:</strong> ${session.location}
                        </p>
                        <button onclick="markAttendance(${session.sessionId})"
                                class="btn btn-primary btn-sm">
                            <i class="fas fa-qrcode me-1"></i> Mark Attendance
                        </button>
                    </div>
                </div>
            </div>
        `;
    });
    html += '</div>';
    container.innerHTML = html;
}

/**
 * Mark attendance for a session
 */
function markAttendance(sessionId) {
    currentSessionId = sessionId;
    // Redirect to QR scanner page
    window.location.href = contextPath + '/student/scan-qr?sessionId=' + sessionId;
}

/**
 * Initialize mark attendance buttons
 */
function initializeMarkAttendanceButtons() {
    const buttons = document.querySelectorAll('.btn-mark-attendance');
    buttons.forEach(button => {
        button.addEventListener('click', function() {
            const sessionId = this.getAttribute('data-session-id');
            markAttendance(sessionId);
        });
    });
}

/**
 * Initialize manual attendance forms
 */
function initializeManualAttendanceForms() {
    const forms = document.querySelectorAll('.manual-attendance-form');
    forms.forEach(form => {
        form.addEventListener('submit', handleManualAttendance);
    });
}

/**
 * Handle manual attendance submission
 */
function handleManualAttendance(event) {
    event.preventDefault();

    const formData = new FormData(event.target);

    fetch(event.target.action, {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showSuccessMessage('Attendance marked successfully');
            setTimeout(() => {
                location.reload();
            }, 1500);
        } else {
            showErrorMessage(data.message || 'Failed to mark attendance');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showErrorMessage('An error occurred while marking attendance');
    });
}

/**
 * Show success message
 */
function showSuccessMessage(message) {
    showMessage(message, 'success');
}

/**
 * Show error message
 */
function showErrorMessage(message) {
    showMessage(message, 'danger');
}

/**
 * Show message alert
 */
function showMessage(message, type) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    const container = document.querySelector('.container');
    if (container) {
        container.insertBefore(alertDiv, container.firstChild);

        // Auto-dismiss after 5 seconds
        setTimeout(() => {
            alertDiv.remove();
        }, 5000);
    }
}

/**
 * Download attendance report
 */
function downloadReport(format) {
    const url = contextPath + '/student/download-report?format=' + format;
    window.location.href = url;
}

/**
 * Filter attendance history
 */
function filterAttendanceHistory() {
    const courseFilter = document.getElementById('course-filter')?.value;
    const dateFrom = document.getElementById('date-from')?.value;
    const dateTo = document.getElementById('date-to')?.value;

    let url = contextPath + '/student/history?';

    if (courseFilter) url += 'courseId=' + courseFilter + '&';
    if (dateFrom) url += 'dateFrom=' + dateFrom + '&';
    if (dateTo) url += 'dateTo=' + dateTo;

    window.location.href = url;
}

/**
 * Calculate attendance percentage
 */
function calculateAttendancePercentage(present, total) {
    if (total === 0) return 0;
    return ((present / total) * 100).toFixed(1);
}

/**
 * Get attendance status badge class
 */
function getStatusBadgeClass(status) {
    switch(status.toUpperCase()) {
        case 'PRESENT':
            return 'bg-success';
        case 'LATE':
            return 'bg-warning';
        case 'ABSENT':
            return 'bg-danger';
        default:
            return 'bg-secondary';
    }
}

// Export functions for use in other scripts
window.AttendanceJS = {
    markAttendance,
    downloadReport,
    filterAttendanceHistory,
    calculateAttendancePercentage,
    getStatusBadgeClass,
    showSuccessMessage,
    showErrorMessage
};

