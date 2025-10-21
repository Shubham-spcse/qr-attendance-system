/**
 * QR Code Scanner JavaScript
 * Handles QR code scanning using device camera
 */

let html5QrCode = null;
let scannerStarted = false;

// Initialize scanner on page load
document.addEventListener('DOMContentLoaded', function() {
    console.log('QR Scanner.js initialized');

    // Check if we're on the QR scanner page
    if (document.getElementById('qr-reader')) {
        initializeQRScanner();
    }
});

/**
 * Initialize QR Code Scanner
 */
function initializeQRScanner() {
    const qrReaderDiv = document.getElementById('qr-reader');
    if (!qrReaderDiv) return;

    // Get session ID from URL
    const urlParams = new URLSearchParams(window.location.search);
    const sessionId = urlParams.get('sessionId');

    if (!sessionId) {
        showErrorMessage('No session ID provided');
        return;
    }

    // Initialize Html5Qrcode scanner
    html5QrCode = new Html5Qrcode("qr-reader");

    // Add start scanner button handler
    const startButton = document.getElementById('start-scanner-btn');
    if (startButton) {
        startButton.addEventListener('click', startScanning);
    }

    // Add manual entry handler
    const manualForm = document.getElementById('manual-entry-form');
    if (manualForm) {
        manualForm.addEventListener('submit', handleManualEntry);
    }
}

/**
 * Start QR code scanning
 */
function startScanning() {
    if (scannerStarted) {
        console.log('Scanner already started');
        return;
    }

    const config = {
        fps: 10,
        qrbox: { width: 250, height: 250 },
        aspectRatio: 1.0
    };

    html5QrCode.start(
        { facingMode: "environment" }, // Use back camera
        config,
        onScanSuccess,
        onScanFailure
    ).then(() => {
        scannerStarted = true;
        updateScannerUI(true);
        console.log('QR Scanner started successfully');
    }).catch(err => {
        console.error('Failed to start scanner:', err);
        showErrorMessage('Failed to start camera. Please check permissions.');
    });
}

/**
 * Stop QR code scanning
 */
function stopScanning() {
    if (!scannerStarted) return;

    html5QrCode.stop().then(() => {
        scannerStarted = false;
        updateScannerUI(false);
        console.log('QR Scanner stopped');
    }).catch(err => {
        console.error('Failed to stop scanner:', err);
    });
}

/**
 * Handle successful QR scan
 */
function onScanSuccess(decodedText, decodedResult) {
    console.log('QR Code scanned:', decodedText);

    // Stop scanner
    stopScanning();

    // Show loading
    showLoadingMessage('Verifying QR code...');

    // Send QR code to server
    submitQRCode(decodedText);
}

/**
 * Handle QR scan failure (called continuously)
 */
function onScanFailure(error) {
    // Don't log every failure, just ignore
    // console.warn('QR scan error:', error);
}

/**
 * Submit QR code to server
 */
function submitQRCode(qrData) {
    const urlParams = new URLSearchParams(window.location.search);
    const sessionId = urlParams.get('sessionId');

    const formData = new FormData();
    formData.append('sessionId', sessionId);
    formData.append('qrCode', qrData);

    fetch(contextPath + '/student/mark-attendance', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showSuccessMessage('Attendance marked successfully!');
            setTimeout(() => {
                window.location.href = contextPath + '/student/dashboard';
            }, 2000);
        } else {
            showErrorMessage(data.message || 'Failed to mark attendance');
            // Restart scanner after error
            setTimeout(startScanning, 2000);
        }
    })
    .catch(error => {
        console.error('Error submitting QR code:', error);
        showErrorMessage('Network error. Please try again.');
        setTimeout(startScanning, 2000);
    });
}

/**
 * Handle manual QR code entry
 */
function handleManualEntry(event) {
    event.preventDefault();

    const qrCode = document.getElementById('manual-qr-input').value.trim();

    if (!qrCode) {
        showErrorMessage('Please enter a QR code');
        return;
    }

    submitQRCode(qrCode);
}

/**
 * Update scanner UI
 */
function updateScannerUI(isScanning) {
    const startButton = document.getElementById('start-scanner-btn');
    const stopButton = document.getElementById('stop-scanner-btn');
    const statusText = document.getElementById('scanner-status');

    if (startButton) {
        startButton.style.display = isScanning ? 'none' : 'block';
    }

    if (stopButton) {
        stopButton.style.display = isScanning ? 'block' : 'none';
        stopButton.onclick = stopScanning;
    }

    if (statusText) {
        statusText.textContent = isScanning ?
            'Scanning... Point camera at QR code' :
            'Scanner stopped. Click Start to scan.';
        statusText.className = isScanning ? 'text-success' : 'text-muted';
    }
}

/**
 * Show loading message
 */
function showLoadingMessage(message) {
    const statusDiv = document.getElementById('scan-status');
    if (statusDiv) {
        statusDiv.innerHTML = `
            <div class="alert alert-info">
                <i class="fas fa-spinner fa-spin me-2"></i>${message}
            </div>
        `;
    }
}

/**
 * Show success message
 */
function showSuccessMessage(message) {
    const statusDiv = document.getElementById('scan-status');
    if (statusDiv) {
        statusDiv.innerHTML = `
            <div class="alert alert-success">
                <i class="fas fa-check-circle me-2"></i>${message}
            </div>
        `;
    }
}

/**
 * Show error message
 */
function showErrorMessage(message) {
    const statusDiv = document.getElementById('scan-status');
    if (statusDiv) {
        statusDiv.innerHTML = `
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-circle me-2"></i>${message}
            </div>
        `;
    }
}

// Export functions
window.QRScannerJS = {
    startScanning,
    stopScanning,
    submitQRCode
};
