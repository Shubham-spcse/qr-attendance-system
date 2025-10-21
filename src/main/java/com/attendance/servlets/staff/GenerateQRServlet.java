package com.attendance.servlets.staff;

import com.attendance.dao.*;
import com.attendance.models.*;
import com.attendance.utils.QRCodeUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * GenerateQRServlet - Generates QR code for attendance session
 * Handles both /generate-qr and /qr-display routes
 */
@WebServlet({"/staff/generate-qr", "/staff/qr-display"})
public class GenerateQRServlet extends HttpServlet {

    private AttendanceSessionDAO sessionDAO;

    @Override
    public void init() throws ServletException {
        sessionDAO = new AttendanceSessionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check staff authentication
        Staff staff = (Staff) request.getSession().getAttribute("user");
        if (staff == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get session ID - support both 'sessionId' and 'id' parameters
        String sessionIdParam = request.getParameter("sessionId");
        if (sessionIdParam == null || sessionIdParam.trim().isEmpty()) {
            sessionIdParam = request.getParameter("id"); // Try 'id' parameter
        }

        System.out.println("GenerateQRServlet called with sessionId: " + sessionIdParam);

        if (sessionIdParam == null || sessionIdParam.trim().isEmpty()) {
            System.err.println("No session ID provided");
            request.setAttribute("error", "Session ID is required");
            request.getRequestDispatcher("/views/staff/qr-display.jsp").forward(request, response);
            return;
        }

        try {
            int sessionId = Integer.parseInt(sessionIdParam);
            System.out.println("Parsed session ID: " + sessionId);

            // Get session details
            AttendanceSession session = sessionDAO.findById(sessionId);

            if (session == null) {
                System.err.println("Session not found: " + sessionId);
                request.setAttribute("error", "Session not found");
                request.getRequestDispatcher("/views/staff/qr-display.jsp").forward(request, response);
                return;
            }

            System.out.println("Session found: " + session.getSessionCode());

            // Verify staff owns this session
            if (session.getStaffId() != staff.getStaffId()) {
                System.err.println("Unauthorized access - Session staff: " + session.getStaffId() +
                        ", Current staff: " + staff.getStaffId());
                request.setAttribute("error", "Unauthorized access to session");
                request.getRequestDispatcher("/views/staff/qr-display.jsp").forward(request, response);
                return;
            }

            // Generate QR code data
            String sessionCode = session.getSessionCode();
            long timestamp = System.currentTimeMillis();

            // Create checksum for security
            String checksum = generateChecksum(sessionCode + timestamp);

            // Format QR data: sessionCode|checksum|timestamp
            String qrData = QRCodeUtil.formatQRData(sessionCode, checksum, timestamp);

            System.out.println("Generating QR code...");
            System.out.println("QR Data: " + qrData);

            // Generate QR code as Base64 image
            String qrCodeBase64 = QRCodeUtil.generateQRCodeBase64(qrData, 400, 400);

            System.out.println("QR Code generated successfully. Size: " + qrCodeBase64.length() + " characters");

            // Update session with QR code data in database
            session.setQrCodeData(qrData);
            session.setQrCodeChecksum(checksum);
            boolean updated = sessionDAO.updateQRCode(sessionId, qrData, checksum);
            System.out.println("Database updated: " + updated);

            // Set attributes for JSP
            request.setAttribute("session", session);
            request.setAttribute("qrCodeImage", qrCodeBase64);
            request.setAttribute("qrData", qrData);
            request.setAttribute("sessionCode", sessionCode);

            System.out.println("Forwarding to JSP...");

            // Forward to display page
            request.getRequestDispatcher("/views/staff/qr-display.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            System.err.println("Invalid session ID format: " + sessionIdParam);
            e.printStackTrace();
            request.setAttribute("error", "Invalid session ID format");
            request.getRequestDispatcher("/views/staff/qr-display.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error generating QR code: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Failed to generate QR code: " + e.getMessage());
            request.getRequestDispatcher("/views/staff/qr-display.jsp").forward(request, response);
        }
    }

    /**
     * Generate QR code image as downloadable PNG
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sessionIdParam = request.getParameter("sessionId");
        if (sessionIdParam == null) {
            sessionIdParam = request.getParameter("id");
        }

        if (sessionIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Session ID required");
            return;
        }

        try {
            int sessionId = Integer.parseInt(sessionIdParam);
            AttendanceSession session = sessionDAO.findById(sessionId);

            if (session == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Session not found");
                return;
            }

            // Generate QR code data
            String sessionCode = session.getSessionCode();
            long timestamp = System.currentTimeMillis();
            String checksum = generateChecksum(sessionCode + timestamp);
            String qrData = QRCodeUtil.formatQRData(sessionCode, checksum, timestamp);

            // Generate QR code as byte array
            byte[] qrImageBytes = QRCodeUtil.generateQRCodeImage(qrData, 400, 400);

            // Set response headers for image download
            response.setContentType("image/png");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"QR_" + sessionCode + ".png\"");
            response.setContentLength(qrImageBytes.length);

            // Write image to response
            response.getOutputStream().write(qrImageBytes);
            response.getOutputStream().flush();

        } catch (Exception e) {
            System.err.println("Error generating QR image: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to generate QR code");
        }
    }

    /**
     * Generate SHA-256 checksum for security
     */
    private String generateChecksum(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            return Base64.getEncoder().encodeToString(hash).substring(0, 16);
        } catch (Exception e) {
            System.err.println("Error generating checksum: " + e.getMessage());
            return String.valueOf(data.hashCode());
        }
    }
}
