<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Mark Attendance - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<!-- QR Code Scanner Library -->
<script src="https://unpkg.com/html5-qrcode"></script>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-10">
            <!-- Page Header -->
            <div class="mb-4">
                <h2 class="fw-bold">
                    <i class="fas fa-qrcode me-2"></i> Mark Attendance
                </h2>
                <p class="text-muted">Scan the QR code displayed by your instructor</p>
            </div>

            <!-- Success Message -->
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>
                    <strong>Success!</strong> ${success}
                    <hr>
                    <p class="mb-1"><strong>Session:</strong> ${sessionCode}</p>
                    <p class="mb-1"><strong>Course:</strong> ${courseName}</p>
                    <p class="mb-1"><strong>Status:</strong> <span class="badge bg-success">${status}</span></p>
                    <p class="mb-0"><strong>Time:</strong> ${markedAt}</p>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    <strong>Error!</strong> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Main Scanner Card -->
            <div class="card shadow-lg">
                <div class="card-body p-4">
                    <ul class="nav nav-tabs mb-4" id="scannerTabs" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link active" id="camera-tab" data-bs-toggle="tab" 
                                    data-bs-target="#camera" type="button" role="tab">
                                <i class="fas fa-camera me-2"></i> Camera Scan
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="manual-tab" data-bs-toggle="tab" 
                                    data-bs-target="#manual" type="button" role="tab">
                                <i class="fas fa-keyboard me-2"></i> Manual Entry
                            </button>
                        </li>
                    </ul>

                    <div class="tab-content" id="scannerTabContent">
                        <!-- Camera Scanner Tab -->
                        <div class="tab-pane fade show active" id="camera" role="tabpanel">
                            <div class="text-center mb-3">
                                <h5>Position the QR code in the camera view</h5>
                                <p class="text-muted">The scan will happen automatically</p>
                            </div>

                            <!-- QR Scanner Video -->
                            <div id="qr-reader" style="width: 100%; max-width: 500px; margin: 0 auto;"></div>

                            <!-- Scanner Status -->
                            <div class="text-center mt-3">
                                <button id="startScanBtn" class="btn btn-primary btn-lg">
                                    <i class="fas fa-camera me-2"></i> Start Scanner
                                </button>
                                <button id="stopScanBtn" class="btn btn-danger btn-lg" style="display:none;">
                                    <i class="fas fa-stop me-2"></i> Stop Scanner
                                </button>
                            </div>

                            <div class="alert alert-info mt-3" role="alert">
                                <i class="fas fa-info-circle me-2"></i>
                                <strong>Tip:</strong> Make sure the QR code is clearly visible and well-lit
                            </div>
                        </div>

                        <!-- Manual Entry Tab -->
                        <div class="tab-pane fade" id="manual" role="tabpanel">
                            <form action="${pageContext.request.contextPath}/student/mark-attendance" 
                                  method="post" id="manualForm">
                                <div class="mb-3">
                                    <label for="sessionCode" class="form-label fw-semibold">
                                        <i class="fas fa-code me-1"></i> Session Code
                                    </label>
                                    <input type="text" class="form-control form-control-lg" 
                                           id="sessionCode" name="sessionCode" 
                                           placeholder="Enter session code (e.g., ATT_20251017_5_123)" 
                                           required>
                                    <small class="text-muted">
                                        Ask your instructor for the session code if QR scan doesn't work
                                    </small>
                                </div>

                                <div class="mb-3">
                                    <label for="qrCodeData" class="form-label fw-semibold">
                                        <i class="fas fa-key me-1"></i> QR Code Data (Optional)
                                    </label>
                                    <input type="text" class="form-control" 
                                           id="qrCodeData" name="qrCodeData" 
                                           placeholder="QR code checksum (optional)">
                                </div>

                                <button type="submit" class="btn btn-primary btn-lg w-100">
                                    <i class="fas fa-check me-2"></i> Submit Attendance
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Instructions Card -->
            <div class="card mt-4">
                <div class="card-header">
                    <h6 class="mb-0"><i class="fas fa-question-circle me-2"></i> How to Mark Attendance</h6>
                </div>
                <div class="card-body">
                    <ol class="mb-0">
                        <li class="mb-2">Click <strong>"Start Scanner"</strong> button</li>
                        <li class="mb-2">Allow camera access when prompted by your browser</li>
                        <li class="mb-2">Point your camera at the QR code displayed by the instructor</li>
                        <li class="mb-2">Wait for automatic scan (you'll hear a beep)</li>
                        <li class="mb-0">Your attendance will be marked automatically!</li>
                    </ol>
                    <hr>
                    <p class="mb-0 text-muted">
                        <strong>Alternative:</strong> If camera doesn't work, switch to "Manual Entry" tab 
                        and enter the session code provided by your instructor.
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- QR Scanner JavaScript -->
<script>
let html5QrCode = null;

document.getElementById('startScanBtn').addEventListener('click', function() {
    startScanner();
});

document.getElementById('stopScanBtn').addEventListener('click', function() {
    stopScanner();
});

function startScanner() {
    html5QrCode = new Html5Qrcode("qr-reader");

    const config = { fps: 10, qrbox: 250 };

    html5QrCode.start(
        { facingMode: "environment" },
        config,
        onScanSuccess,
        onScanError
    ).then(() => {
        document.getElementById('startScanBtn').style.display = 'none';
        document.getElementById('stopScanBtn').style.display = 'inline-block';
    }).catch((err) => {
        alert('Unable to start camera. Please check permissions or use manual entry.');
        console.error(err);
    });
}

function stopScanner() {
    if (html5QrCode) {
        html5QrCode.stop().then(() => {
            document.getElementById('startScanBtn').style.display = 'inline-block';
            document.getElementById('stopScanBtn').style.display = 'none';
        }).catch((err) => {
            console.error(err);
        });
    }
}

function onScanSuccess(decodedText, decodedResult) {
    // Play beep sound (browser will play default)
    const audio = new Audio('data:audio/wav;base64,UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBjWb3fHNeysFGGi87+OYQQ4KS6Xj8LVhHAQ3pOPyvmohBjiP1vLOeysFI3fH8N6UQQoPXLPp66ZUEwpFoe/PwmMoCy9vmN9M');
    audio.play().catch(() => {});

    // Stop scanner
    stopScanner();

    // Parse QR data (format: sessionCode|checksum)
    const parts = decodedText.split('|');
    const sessionCode = parts[0];
    const checksum = parts.length > 1 ? parts[1] : '';

    // Auto-submit form
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '${pageContext.request.contextPath}/student/mark-attendance';

    const sessionInput = document.createElement('input');
    sessionInput.type = 'hidden';
    sessionInput.name = 'sessionCode';
    sessionInput.value = sessionCode;

    const checksumInput = document.createElement('input');
    checksumInput.type = 'hidden';
    checksumInput.name = 'qrCodeData';
    checksumInput.value = checksum;

    form.appendChild(sessionInput);
    form.appendChild(checksumInput);
    document.body.appendChild(form);
    form.submit();
}

function onScanError(errorMessage) {
    // Ignore errors - they're common during scanning
}

// Clean up on page unload
window.addEventListener('beforeunload', function() {
    if (html5QrCode && html5QrCode.isScanning) {
        html5QrCode.stop();
    }
});
</script>

<%@ include file="../common/footer.jsp" %>
