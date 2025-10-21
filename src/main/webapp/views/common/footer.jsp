<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

    <!-- Footer -->
    <footer class="footer bg-light border-top mt-auto py-3">
        <div class="container-fluid">
            <div class="row align-items-center">
                <div class="col-md-6 text-center text-md-start">
                    <span class="text-muted">
                        © 2025 <strong>QR Attendance System</strong>. All rights reserved.
                    </span>
                </div>
                <div class="col-md-6 text-center text-md-end">
                    <span class="text-muted">
                        Powered by <i class="fas fa-qrcode text-primary"></i> Technology
                    </span>
                </div>
            </div>
        </div>
    </footer>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!-- Custom JavaScript -->
<!-- Custom JavaScript -->
<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/assets/js/validation.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/attendance.js"></script>

<!-- Only include QR scanner on scan-qr.jsp page -->
<!-- <script src="${pageContext.request.contextPath}/assets/js/qr-scanner.js"></script> -->
</body>
</html>
