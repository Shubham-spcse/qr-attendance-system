package com.attendance.servlets.student;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {

    private StudentDAO studentDAO;
    private EnrollmentDAO enrollmentDAO;
    private AttendanceSessionDAO sessionDAO;
    private AttendanceDAO attendanceDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
        enrollmentDAO = new EnrollmentDAO();
        sessionDAO = new AttendanceSessionDAO();
        attendanceDAO = new AttendanceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Student student = (Student) request.getSession().getAttribute("user");
        if (student == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Initialize all lists to avoid null pointer exceptions
            List<Enrollment> enrollments = new ArrayList<>();
            List<AttendanceSession> activeSessions = new ArrayList<>();
            List<Attendance> recentAttendance = new ArrayList<>();

            // Get student's enrollments
            enrollments = enrollmentDAO.findByStudent(student.getStudentId());
            if (enrollments == null) {
                enrollments = new ArrayList<>();
            }

            // Get active sessions for student's courses
            List<AttendanceSession> allActiveSessions = sessionDAO.findActiveSessions();
            if (allActiveSessions != null) {
                for (AttendanceSession session : allActiveSessions) {
                    boolean enrolled = enrollments.stream()
                            .anyMatch(e -> e.getCourseId() == session.getCourseId());
                    if (enrolled) {
                        activeSessions.add(session);
                    }
                }
            }

            // Get student's attendance records
            List<Attendance> allAttendance = attendanceDAO.findByStudent(student.getStudentId());
            if (allAttendance == null) {
                allAttendance = new ArrayList<>();
            }

            // Get recent attendance (last 10)
            if (allAttendance.size() > 10) {
                recentAttendance = allAttendance.subList(0, 10);
            } else {
                recentAttendance = allAttendance;
            }

            // Calculate statistics
            int totalSessions = allAttendance.size();
            int presentCount = 0;
            int lateCount = 0;
            int absentCount = 0;

            for (Attendance att : allAttendance) {
                if (att.isPresent()) presentCount++;
                else if (att.isLate()) lateCount++;
                else if (att.isAbsent()) absentCount++;
            }

            double overallPercentage = totalSessions > 0 ?
                    ((double)(presentCount + lateCount) / totalSessions) * 100.0 : 0.0;

            // Set all attributes (with safe defaults)
            request.setAttribute("enrollments", enrollments);
            request.setAttribute("activeSessions", activeSessions);
            request.setAttribute("recentAttendance", recentAttendance);
            request.setAttribute("totalSessions", totalSessions);
            request.setAttribute("presentCount", presentCount);
            request.setAttribute("lateCount", lateCount);
            request.setAttribute("absentCount", absentCount);
            request.setAttribute("overallPercentage", String.format("%.1f", overallPercentage));

            System.out.println("StudentDashboard loaded for: " + student.getRollNumber() +
                    " | Enrollments: " + enrollments.size() +
                    " | Active Sessions: " + activeSessions.size() +
                    " | Total Attendance: " + totalSessions);

            request.getRequestDispatcher("/views/student/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading student dashboard: " + e.getMessage());
            e.printStackTrace();

            // Set safe defaults to prevent blank page
            request.setAttribute("enrollments", new ArrayList<>());
            request.setAttribute("activeSessions", new ArrayList<>());
            request.setAttribute("recentAttendance", new ArrayList<>());
            request.setAttribute("totalSessions", 0);
            request.setAttribute("presentCount", 0);
            request.setAttribute("lateCount", 0);
            request.setAttribute("absentCount", 0);
            request.setAttribute("overallPercentage", "0.0");
            request.setAttribute("error", "Unable to load dashboard data. Please try again.");

            request.getRequestDispatcher("/views/student/dashboard.jsp").forward(request, response);
        }
    }
}
