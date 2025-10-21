package com.attendance.servlets.admin;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 * AdminDashboardServlet - Admin dashboard with system analytics
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private AdminDAO adminDAO;
    private StudentDAO studentDAO;
    private StaffDAO staffDAO;
    private CourseDAO courseDAO;
    private DepartmentDAO departmentDAO;
    private AttendanceSessionDAO sessionDAO;
    private AttendanceDAO attendanceDAO;

    @Override
    public void init() throws ServletException {
        adminDAO = new AdminDAO();
        studentDAO = new StudentDAO();
        staffDAO = new StaffDAO();
        courseDAO = new CourseDAO();
        departmentDAO = new DepartmentDAO();
        sessionDAO = new AttendanceSessionDAO();
        attendanceDAO = new AttendanceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check admin authentication
        Admin admin = (Admin) request.getSession().getAttribute("user");
        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Gather dashboard statistics
            int totalStudents = studentDAO.findAll().size();
            int totalStaff = staffDAO.findAll().size();
            int totalCourses = courseDAO.findAll().size();
            int totalDepartments = departmentDAO.findAll().size();

            // Get active sessions
            List<AttendanceSession> activeSessions = sessionDAO.findActiveSessions();
            int activeSessionsCount = activeSessions != null ? activeSessions.size() : 0;

            // Get today's sessions count (approximate)
            int todaySessionsCount = activeSessionsCount; // Simplified

            // Calculate overall attendance percentage (last 30 days)
            double overallAttendancePercentage = calculateOverallAttendance();

            // Set attributes
            request.setAttribute("totalStudents", totalStudents);
            request.setAttribute("totalStaff", totalStaff);
            request.setAttribute("totalCourses", totalCourses);
            request.setAttribute("totalDepartments", totalDepartments);
            request.setAttribute("activeSessionsCount", activeSessionsCount);
            request.setAttribute("todaySessionsCount", todaySessionsCount);
            request.setAttribute("overallAttendancePercentage",
                    String.format("%.1f", overallAttendancePercentage));
            request.setAttribute("activeSessions", activeSessions);

            // Get recent activities (last 10 sessions)
            List<AttendanceSession> recentSessions = getRecentSessions(10);
            request.setAttribute("recentSessions", recentSessions);

            System.out.println("Admin Dashboard loaded successfully for: " + admin.getName());
            System.out.println("Stats - Students: " + totalStudents + ", Staff: " + totalStaff +
                    ", Courses: " + totalCourses + ", Active Sessions: " + activeSessionsCount);

            // Forward to JSP
            request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading admin dashboard: " + e.getMessage());
            e.printStackTrace();

            // Set default values to prevent JSP errors
            request.setAttribute("totalStudents", 0);
            request.setAttribute("totalStaff", 0);
            request.setAttribute("totalCourses", 0);
            request.setAttribute("totalDepartments", 0);
            request.setAttribute("activeSessionsCount", 0);
            request.setAttribute("todaySessionsCount", 0);
            request.setAttribute("overallAttendancePercentage", "0.0");
            request.setAttribute("activeSessions", new ArrayList<>());
            request.setAttribute("recentSessions", new ArrayList<>());
            request.setAttribute("error", "Unable to load dashboard data: " + e.getMessage());

            request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response);
        }
    }

    /**
     * Calculate overall attendance percentage (simplified)
     */
    private double calculateOverallAttendance() {
        try {
            // Get all students
            List<Student> students = studentDAO.findAll();
            if (students == null || students.isEmpty()) {
                return 0.0;
            }

            int totalPresentCount = 0;
            int totalSessionsCount = 0;

            // For each student, count their present/late attendance
            for (Student student : students) {
                List<Attendance> attendances = attendanceDAO.findByStudent(student.getStudentId());
                if (attendances != null) {
                    totalSessionsCount += attendances.size();
                    for (Attendance att : attendances) {
                        if (att.isPresent() || att.isLate()) {
                            totalPresentCount++;
                        }
                    }
                }
            }

            if (totalSessionsCount == 0) {
                return 0.0;
            }

            return ((double) totalPresentCount / totalSessionsCount) * 100.0;

        } catch (Exception e) {
            System.err.println("Error calculating attendance: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Get recent sessions for activity feed
     */
    private List<AttendanceSession> getRecentSessions(int limit) {
        try {
            List<AttendanceSession> allSessions = new ArrayList<>();

            // Get sessions from all staff (simplified approach)
            List<Staff> staffList = staffDAO.findAll();
            for (Staff staff : staffList) {
                List<AttendanceSession> staffSessions = sessionDAO.findByStaff(staff.getStaffId());
                if (staffSessions != null) {
                    allSessions.addAll(staffSessions);
                }
            }

            // Sort by date (most recent first)
            allSessions.sort((s1, s2) -> s2.getSessionDate().compareTo(s1.getSessionDate()));

            // Return top N
            return allSessions.size() > limit ?
                    allSessions.subList(0, limit) : allSessions;

        } catch (Exception e) {
            System.err.println("Error fetching recent sessions: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
