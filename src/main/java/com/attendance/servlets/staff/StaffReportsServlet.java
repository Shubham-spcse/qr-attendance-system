package com.attendance.servlets.staff;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/staff/reports")
public class StaffReportsServlet extends HttpServlet {

    private AttendanceSessionDAO sessionDAO;

    @Override
    public void init() throws ServletException {
        sessionDAO = new AttendanceSessionDAO();
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
            System.out.println("Loading reports for staff: " + staff.getName() + " (ID: " + staff.getStaffId() + ")");

            // Get all sessions for this staff member
            List<AttendanceSession> sessions = sessionDAO.findByStaff(staff.getStaffId());

            System.out.println("Found " + (sessions != null ? sessions.size() : 0) + " sessions");

            // Group sessions by course
            Map<Integer, List<AttendanceSession>> courseSessionsMap = new HashMap<>();

            if (sessions != null && !sessions.isEmpty()) {
                for (AttendanceSession session : sessions) {
                    int courseId = session.getCourseId();
                    courseSessionsMap.computeIfAbsent(courseId, k -> new ArrayList<>()).add(session);
                }
            }

            // Create report objects
            List<CourseReport> courseReports = new ArrayList<>();

            for (Map.Entry<Integer, List<AttendanceSession>> entry : courseSessionsMap.entrySet()) {
                List<AttendanceSession> courseSessions = entry.getValue();
                if (!courseSessions.isEmpty()) {
                    AttendanceSession firstSession = courseSessions.get(0);

                    CourseReport report = new CourseReport();
                    report.setCourseId(firstSession.getCourseId());
                    report.setCourseName(firstSession.getCourseName());
                    report.setCourseCode(firstSession.getCourseCode());
                    report.setSessions(courseSessions);

                    courseReports.add(report);

                    System.out.println("Created report for: " + firstSession.getCourseName() +
                            " with " + courseSessions.size() + " sessions");
                }
            }

            System.out.println("Total course reports created: " + courseReports.size());

            request.setAttribute("courseReports", courseReports);
            request.getRequestDispatcher("/views/staff/reports.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading reports: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error loading reports: " + e.getMessage());
            request.getRequestDispatcher("/views/staff/reports.jsp").forward(request, response);
        }
    }
}
