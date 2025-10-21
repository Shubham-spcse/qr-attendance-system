package com.attendance.servlets.staff;

import com.attendance.dao.*;
import com.attendance.models.*;
import com.attendance.enums.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * CreateSessionServlet - Create new attendance session
 * URL: /staff/create-session
 */
@WebServlet("/staff/create-session")
public class CreateSessionServlet extends HttpServlet {

    private AttendanceSessionDAO sessionDAO;
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;

    @Override
    public void init() throws ServletException {
        sessionDAO = new AttendanceSessionDAO();
        courseDAO = new CourseDAO();
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

        // Load courses for staff's department
        List<Course> courses = courseDAO.findByDepartment(staff.getDepartmentId());
        request.setAttribute("courses", courses);
        request.setAttribute("sessionTypes", SessionType.values());

        request.getRequestDispatcher("/views/staff/create-session.jsp").forward(request, response);
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
            // Get form parameters
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            String sessionDate = request.getParameter("sessionDate");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            String location = request.getParameter("location");
            String sessionType = request.getParameter("sessionType");

            // Validate inputs
            if (sessionDate == null || startTime == null || endTime == null || location == null) {
                request.setAttribute("error", "All fields are required");
                doGet(request, response);
                return;
            }

            // Generate unique session code
            String sessionCode = generateSessionCode(courseId, sessionDate);

            // Count enrolled students
            int totalStudents = enrollmentDAO.countByCourse(courseId);

            // Create session object
            AttendanceSession session = new AttendanceSession();
            session.setSessionCode(sessionCode);
            session.setCourseId(courseId);
            session.setStaffId(staff.getStaffId());
            session.setSessionDate(Date.valueOf(sessionDate));
            session.setStartTime(Time.valueOf(startTime + ":00"));
            session.setEndTime(Time.valueOf(endTime + ":00"));
            session.setLocation(location);
            session.setSessionType(SessionType.valueOf(sessionType));
            session.setStatus(SessionStatus.SCHEDULED);
            session.setTotalStudents(totalStudents);

            // Save to database
            if (sessionDAO.create(session)) {
                System.out.println("Session created: " + sessionCode + " by staff " + staff.getStaffCode());
                response.sendRedirect(request.getContextPath() + 
                    "/staff/generate-qr?sessionId=" + session.getSessionId());
            } else {
                request.setAttribute("error", "Failed to create session");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input format");
            doGet(request, response);
        } catch (Exception e) {
            System.err.println("Error creating session: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "System error: " + e.getMessage());
            doGet(request, response);
        }
    }

    /**
     * Generate unique session code
     * Format: ATT_YYYYMMDD_COURSEID_XXX
     */
    private String generateSessionCode(int courseId, String dateStr) {
        String datePart = dateStr.replace("-", "");
        long timestamp = System.currentTimeMillis() % 1000;
        return String.format("ATT_%s_%d_%03d", datePart, courseId, timestamp);
    }
}
