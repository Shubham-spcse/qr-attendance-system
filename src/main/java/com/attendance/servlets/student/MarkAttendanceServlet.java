package com.attendance.servlets.student;

import com.attendance.dao.*;
import com.attendance.models.*;
import com.attendance.enums.AttendanceStatus;
import com.attendance.enums.SessionStatus;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * MarkAttendanceServlet - Student marks attendance by scanning QR code
 * CRITICAL: Fixes Issue #3
 */
@WebServlet("/student/mark-attendance")
public class MarkAttendanceServlet extends HttpServlet {

    private AttendanceDAO attendanceDAO;
    private AttendanceSessionDAO sessionDAO;
    private StudentDAO studentDAO;
    private EnrollmentDAO enrollmentDAO;

    @Override
    public void init() throws ServletException {
        attendanceDAO = new AttendanceDAO();
        sessionDAO = new AttendanceSessionDAO();
        studentDAO = new StudentDAO();
        enrollmentDAO = new EnrollmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Display QR scanner page
        request.getRequestDispatcher("/views/student/mark-attendance.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check student authentication
        Student student = (Student) request.getSession().getAttribute("user");
        if (student == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get QR data from request
        String sessionCode = request.getParameter("sessionCode");
        String qrCodeData = request.getParameter("qrCodeData");

        System.out.println("MarkAttendanceServlet called");
        System.out.println("Student: " + student.getName() + " (ID: " + student.getStudentId() + ")");
        System.out.println("Session Code: " + sessionCode);
        System.out.println("QR Data: " + qrCodeData);

        if (sessionCode == null || sessionCode.trim().isEmpty()) {
            request.setAttribute("error", "Session code is required");
            request.getRequestDispatcher("/views/student/mark-attendance.jsp")
                    .forward(request, response);
            return;
        }

        try {
            // Find session by code
            AttendanceSession session = sessionDAO.findBySessionCode(sessionCode);

            if (session == null) {
                System.err.println("Session not found: " + sessionCode);
                request.setAttribute("error", "Invalid session code");
                request.getRequestDispatcher("/views/student/mark-attendance.jsp")
                        .forward(request, response);
                return;
            }

            System.out.println("Session found: " + session.getSessionCode() +
                    ", Status: " + session.getStatus());

            // Validate session is active
            if (session.getStatus() != SessionStatus.ACTIVE) {
                System.err.println("Session not active: " + session.getStatus());
                request.setAttribute("error", "Session is not active. Status: " +
                        session.getStatus().getDisplayName());
                request.getRequestDispatcher("/views/student/mark-attendance.jsp")
                        .forward(request, response);
                return;
            }

            // Check if student is enrolled in this course
            boolean isEnrolled = enrollmentDAO.isStudentEnrolled(
                    student.getStudentId(),
                    session.getCourseId()
            );

            if (!isEnrolled) {
                System.err.println("Student not enrolled in course: " + session.getCourseId());
                request.setAttribute("error", "You are not enrolled in this course");
                request.getRequestDispatcher("/views/student/mark-attendance.jsp")
                        .forward(request, response);
                return;
            }

            // Check for duplicate attendance
            boolean alreadyMarked = attendanceDAO.hasMarkedAttendance(
                    student.getStudentId(),
                    session.getSessionId()
            );

            if (alreadyMarked) {
                System.out.println("Attendance already marked for this session");
                request.setAttribute("info", "You have already marked attendance for this session");
                request.getRequestDispatcher("/views/student/mark-attendance.jsp")
                        .forward(request, response);
                return;
            }

            // Determine attendance status (PRESENT or LATE)
            AttendanceStatus status = determineAttendanceStatus(session);

            // Create attendance record
            Attendance attendance = new Attendance();
            attendance.setSessionId(session.getSessionId());
            attendance.setStudentId(student.getStudentId());
            attendance.setStatus(status);
            attendance.setMarkedAt(new Timestamp(System.currentTimeMillis()));
            attendance.setQrCodeUsed(qrCodeData != null ? qrCodeData : sessionCode);

            // Save attendance
            boolean saved = attendanceDAO.insert(attendance);

            if (saved) {
                System.out.println("Attendance marked successfully!");
                System.out.println("Student: " + student.getName());
                System.out.println("Session: " + session.getSessionCode());
                System.out.println("Status: " + status);

                // Set success attributes
                request.setAttribute("success", "Attendance marked successfully!");
                request.setAttribute("sessionCode", sessionCode);
                request.setAttribute("courseName", session.getCourseName());
                request.setAttribute("status", status.getDisplayName());
                request.setAttribute("markedAt", attendance.getMarkedAt());

            } else {
                System.err.println("Failed to save attendance");
                request.setAttribute("error", "Failed to mark attendance. Please try again.");
            }

            request.getRequestDispatcher("/views/student/mark-attendance.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            System.err.println("Error marking attendance: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error marking attendance: " + e.getMessage());
            request.getRequestDispatcher("/views/student/mark-attendance.jsp")
                    .forward(request, response);
        }
    }

    /**
     * Determine if student is PRESENT or LATE based on session timing
     */
    private AttendanceStatus determineAttendanceStatus(AttendanceSession session) {
        try {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Time startTime = session.getStartTime();

            // If no start time, default to PRESENT
            if (startTime == null) {
                return AttendanceStatus.PRESENT;
            }

            // Grace period: 10 minutes after start time
            long gracePeriodMillis = 10 * 60 * 1000; // 10 minutes
            long timeDiff = now.getTime() - startTime.getTime();

            if (timeDiff <= gracePeriodMillis) {
                return AttendanceStatus.PRESENT;
            } else {
                return AttendanceStatus.LATE;
            }

        } catch (Exception e) {
            System.err.println("Error determining status: " + e.getMessage());
            return AttendanceStatus.PRESENT; // Default
        }
    }
}
