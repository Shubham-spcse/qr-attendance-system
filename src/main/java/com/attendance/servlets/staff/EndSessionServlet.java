package com.attendance.servlets.staff;

import com.attendance.dao.*;
import com.attendance.models.*;
import com.attendance.enums.SessionStatus;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * EndSessionServlet - Ends an active attendance session
 */
@WebServlet("/staff/end-session")
public class EndSessionServlet extends HttpServlet {

    private AttendanceSessionDAO sessionDAO;

    @Override
    public void init() throws ServletException {
        sessionDAO = new AttendanceSessionDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check staff authentication
        Staff staff = (Staff) request.getSession().getAttribute("user");
        if (staff == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get session ID from request
        String sessionIdParam = request.getParameter("sessionId");
        if (sessionIdParam == null) {
            sessionIdParam = request.getParameter("id");
        }

        System.out.println("EndSessionServlet called with sessionId: " + sessionIdParam);

        if (sessionIdParam == null || sessionIdParam.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Session ID is required");
            response.sendRedirect(request.getContextPath() + "/staff/dashboard");
            return;
        }

        try {
            int sessionId = Integer.parseInt(sessionIdParam);
            System.out.println("Parsed session ID: " + sessionId);

            // Get session details
            AttendanceSession session = sessionDAO.findById(sessionId);

            if (session == null) {
                System.err.println("Session not found: " + sessionId);
                request.getSession().setAttribute("error", "Session not found");
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
                return;
            }

            System.out.println("Session found: " + session.getSessionCode() +
                    ", Status: " + session.getStatus());

            // Verify staff owns this session
            if (session.getStaffId() != staff.getStaffId()) {
                System.err.println("Unauthorized access - Session staff: " +
                        session.getStaffId() + ", Current staff: " + staff.getStaffId());
                request.getSession().setAttribute("error", "Unauthorized access to session");
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
                return;
            }

            // Check if session is already ended
            if (session.getStatus() == SessionStatus.EXPIRED ||
                    session.getStatus() == SessionStatus.CANCELLED) {
                System.out.println("Session already ended: " + session.getStatus());
                request.getSession().setAttribute("info",
                        "Session is already ended with status: " + session.getStatus());
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
                return;
            }

            // End the session by marking it as EXPIRED
            boolean updated = sessionDAO.updateStatus(sessionId, SessionStatus.EXPIRED);

            if (updated) {
                System.out.println("Session ended successfully: " + sessionId);
                request.getSession().setAttribute("success",
                        "Session '" + session.getSessionCode() + "' ended successfully!");
            } else {
                System.err.println("Failed to end session: " + sessionId);
                request.getSession().setAttribute("error", "Failed to end session");
            }

            // Redirect back to dashboard
            response.sendRedirect(request.getContextPath() + "/staff/dashboard");

        } catch (NumberFormatException e) {
            System.err.println("Invalid session ID format: " + sessionIdParam);
            e.printStackTrace();
            request.getSession().setAttribute("error", "Invalid session ID format");
            response.sendRedirect(request.getContextPath() + "/staff/dashboard");
        } catch (Exception e) {
            System.err.println("Error ending session: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error",
                    "Failed to end session: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/staff/dashboard");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to POST
        doPost(request, response);
    }
}
