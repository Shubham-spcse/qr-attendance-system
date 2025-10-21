package com.attendance.servlets.staff;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * StaffDashboardServlet - Staff home page
 * URL: /staff/dashboard
 */
@WebServlet("/staff/dashboard")
public class StaffDashboardServlet extends HttpServlet {

    private AttendanceSessionDAO sessionDAO;
    private CourseDAO courseDAO;

    @Override
    public void init() throws ServletException {
        sessionDAO = new AttendanceSessionDAO();
        courseDAO = new CourseDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Staff staff = (Staff) request.getSession().getAttribute("user");
        if (staff == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get staff's courses
            List<Course> courses = courseDAO.findByDepartment(staff.getDepartmentId());
            request.setAttribute("courses", courses);

            // Get staff's sessions (recent 20)
            List<AttendanceSession> sessions = sessionDAO.findByStaff(staff.getStaffId());
            if (sessions.size() > 20) {
                sessions = sessions.subList(0, 20);
            }
            request.setAttribute("sessions", sessions);

            // Get active sessions
            List<AttendanceSession> activeSessions = sessionDAO.findActiveSessions();
            List<AttendanceSession> staffActiveSessions = activeSessions.stream()
                .filter(s -> s.getStaffId() == staff.getStaffId())
                .toList();
            request.setAttribute("activeSessions", staffActiveSessions);

            // Statistics
            long totalSessions = sessions.size();
            long activeCount = staffActiveSessions.size();
            long scheduledCount = sessions.stream()
                .filter(s -> s.isScheduled())
                .count();
            long completedCount = sessions.stream()
                .filter(s -> s.isExpired())
                .count();

            request.setAttribute("totalSessions", totalSessions);
            request.setAttribute("activeCount", activeCount);
            request.setAttribute("scheduledCount", scheduledCount);
            request.setAttribute("completedCount", completedCount);

            request.getRequestDispatcher("/views/staff/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading staff dashboard: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error loading dashboard");
            request.getRequestDispatcher("/views/staff/dashboard.jsp").forward(request, response);
        }
    }
}
