package com.attendance.servlets.staff;

import com.attendance.dao.*;
import com.attendance.models.*;
import com.attendance.enums.AttendanceStatus;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * ManualAttendanceServlet - Staff can manually mark/update attendance
 * URL: /staff/manual-attendance
 * Useful for proxy attendance, corrections, or offline sessions
 */
@WebServlet("/staff/manual-attendance")
public class ManualAttendanceServlet extends HttpServlet {

    private AttendanceSessionDAO sessionDAO;
    private AttendanceDAO attendanceDAO;
    private EnrollmentDAO enrollmentDAO;

    @Override
    public void init() throws ServletException {
        sessionDAO = new AttendanceSessionDAO();
        attendanceDAO = new AttendanceDAO();
        enrollmentDAO = new EnrollmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Staff staff = (Staff) request.getSession().getAttribute("user");
        if (staff == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String sessionIdParam = request.getParameter("sessionId");
        if (sessionIdParam == null) {
            // Show session selection page
            List<AttendanceSession> sessions = sessionDAO.findByStaff(staff.getStaffId());
            request.setAttribute("sessions", sessions);
            request.getRequestDispatcher("/views/staff/select-session.jsp").forward(request, response);
            return;
        }

        try {
            int sessionId = Integer.parseInt(sessionIdParam);
            AttendanceSession session = sessionDAO.findById(sessionId);

            if (session == null || session.getStaffId() != staff.getStaffId()) {
                request.setAttribute("error", "Session not found or unauthorized");
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
                return;
            }

            // Get enrolled students
            List<Enrollment> enrollments = enrollmentDAO.findByCourse(session.getCourseId());

            // Get existing attendance records
            List<Attendance> existingAttendance = attendanceDAO.findBySession(sessionId);
            Map<Integer, Attendance> attendanceMap = new HashMap<>();
            for (Attendance att : existingAttendance) {
                attendanceMap.put(att.getStudentId(), att);
            }

            // Create student-attendance pairs
            List<Map<String, Object>> studentAttendanceList = new ArrayList<>();
            for (Enrollment enrollment : enrollments) {
                Map<String, Object> pair = new HashMap<>();
                pair.put("enrollment", enrollment);
                pair.put("attendance", attendanceMap.get(enrollment.getStudentId()));
                studentAttendanceList.add(pair);
            }

            request.setAttribute("session", session);
            request.setAttribute("studentAttendanceList", studentAttendanceList);
            request.setAttribute("attendanceStatuses", AttendanceStatus.values());

            request.getRequestDispatcher("/views/staff/manual-attendance.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading manual attendance: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error loading attendance data");
            response.sendRedirect(request.getContextPath() + "/staff/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Staff staff = (Staff) request.getSession().getAttribute("user");
        if (staff == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int sessionId = Integer.parseInt(request.getParameter("sessionId"));
            AttendanceSession session = sessionDAO.findById(sessionId);

            if (session == null || session.getStaffId() != staff.getStaffId()) {
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
                return;
            }

            String action = request.getParameter("action");

            if ("markAll".equals(action)) {
                // Bulk mark all students
                markBulkAttendance(request, session, staff);
                request.setAttribute("success", "Bulk attendance marked successfully");

            } else if ("markIndividual".equals(action)) {
                // Mark individual student
                markIndividualAttendance(request, session, staff);
                request.setAttribute("success", "Attendance updated successfully");

            } else if ("updateStatus".equals(action)) {
                // Update existing attendance status
                updateAttendanceStatus(request);
                request.setAttribute("success", "Attendance status updated");
            }

            // Redirect back to manual attendance page
            response.sendRedirect(request.getContextPath() + 
                "/staff/manual-attendance?sessionId=" + sessionId + "&success=true");

        } catch (Exception e) {
            System.err.println("Error marking attendance: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error marking attendance");
            doGet(request, response);
        }
    }

    /**
     * Mark attendance for all enrolled students at once
     */
    private void markBulkAttendance(HttpServletRequest request, 
                                   AttendanceSession session, Staff staff) {

        String bulkStatus = request.getParameter("bulkStatus");
        AttendanceStatus status = AttendanceStatus.valueOf(bulkStatus);

        List<Enrollment> enrollments = enrollmentDAO.findByCourse(session.getCourseId());
        Timestamp now = new Timestamp(System.currentTimeMillis());

        int marked = 0;
        for (Enrollment enrollment : enrollments) {
            // Check if already marked
            if (!attendanceDAO.hasMarkedAttendance(session.getSessionId(), 
                                                   enrollment.getStudentId())) {
                Attendance attendance = new Attendance();
                attendance.setSessionId(session.getSessionId());
                attendance.setStudentId(enrollment.getStudentId());
                attendance.setStatus(status);
                attendance.setMarkedAt(now);
                attendance.setMarkedBy("STAFF");
                attendance.setRemarks("Bulk marked by staff");

                if (attendanceDAO.create(attendance)) {
                    marked++;
                }
            }
        }

        System.out.println("Bulk attendance: " + marked + " students marked as " + 
                         status + " by staff " + staff.getStaffCode());
    }

    /**
     * Mark attendance for a single student
     */
    private void markIndividualAttendance(HttpServletRequest request,
                                         AttendanceSession session, Staff staff) {

        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String statusStr = request.getParameter("status");
        String remarks = request.getParameter("remarks");

        AttendanceStatus status = AttendanceStatus.valueOf(statusStr);

        if (!attendanceDAO.hasMarkedAttendance(session.getSessionId(), studentId)) {
            Attendance attendance = new Attendance();
            attendance.setSessionId(session.getSessionId());
            attendance.setStudentId(studentId);
            attendance.setStatus(status);
            attendance.setMarkedAt(new Timestamp(System.currentTimeMillis()));
            attendance.setMarkedBy("STAFF");
            attendance.setRemarks(remarks);

            attendanceDAO.create(attendance);

            System.out.println("Manual attendance: Student " + studentId + 
                             " marked as " + status + " by staff " + staff.getStaffCode());
        }
    }

    /**
     * Update existing attendance record status
     */
    private void updateAttendanceStatus(HttpServletRequest request) {
        int attendanceId = Integer.parseInt(request.getParameter("attendanceId"));
        String statusStr = request.getParameter("newStatus");

        AttendanceStatus newStatus = AttendanceStatus.valueOf(statusStr);
        attendanceDAO.updateStatus(attendanceId, newStatus);

        System.out.println("Attendance updated: ID " + attendanceId + 
                         " changed to " + newStatus);
    }
}
