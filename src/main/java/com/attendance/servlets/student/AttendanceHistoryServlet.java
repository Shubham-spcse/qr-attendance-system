package com.attendance.servlets.student;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 * AttendanceHistoryServlet - Shows student's complete attendance history
 * URL: /student/history
 */
@WebServlet("/student/history")
public class AttendanceHistoryServlet extends HttpServlet {

    private AttendanceDAO attendanceDAO;
    private EnrollmentDAO enrollmentDAO;

    @Override
    public void init() throws ServletException {
        attendanceDAO = new AttendanceDAO();
        enrollmentDAO = new EnrollmentDAO();
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
            String courseIdParam = request.getParameter("courseId");

            if (courseIdParam != null && !courseIdParam.isEmpty()) {
                // Show attendance for specific course
                int courseId = Integer.parseInt(courseIdParam);
                List<Attendance> attendanceList = attendanceDAO.findByStudentAndCourse(
                    student.getStudentId(), courseId);

                // Calculate statistics
                int total = attendanceList.size();
                int present = 0;
                int late = 0;
                int absent = 0;

                for (Attendance att : attendanceList) {
                    if (att.isPresent()) present++;
                    else if (att.isLate()) late++;
                    else if (att.isAbsent()) absent++;
                }

                double percentage = total > 0 ? ((double)(present + late) / total) * 100.0 : 0.0;

                request.setAttribute("attendanceList", attendanceList);
                request.setAttribute("totalSessions", total);
                request.setAttribute("presentCount", present);
                request.setAttribute("lateCount", late);
                request.setAttribute("absentCount", absent);
                request.setAttribute("percentage", String.format("%.2f", percentage));
                request.setAttribute("courseId", courseId);

            } else {
                // Show all attendance grouped by course
                List<Enrollment> enrollments = enrollmentDAO.findByStudent(student.getStudentId());
                Map<String, List<Attendance>> attendanceByCoursepublic = new HashMap<>();
                Map<String, Map<String, Integer>> statisticsByCourse = new HashMap<>();

                for (Enrollment enrollment : enrollments) {
                    List<Attendance> courseAttendance = attendanceDAO.findByStudentAndCourse(
                        student.getStudentId(), enrollment.getCourseId());

                    String courseKey = enrollment.getCourseCode() + " - " + enrollment.getCourseName();
                    attendanceByCoursepublic.put(courseKey, courseAttendance);

                    // Calculate stats
                    int total = courseAttendance.size();
                    int present = 0;
                    int late = 0;
                    int absent = 0;

                    for (Attendance att : courseAttendance) {
                        if (att.isPresent()) present++;
                        else if (att.isLate()) late++;
                        else if (att.isAbsent()) absent++;
                    }

                    Map<String, Integer> stats = new HashMap<>();
                    stats.put("total", total);
                    stats.put("present", present);
                    stats.put("late", late);
                    stats.put("absent", absent);
                    stats.put("percentage", total > 0 ? (present + late) * 100 / total : 0);

                    statisticsByCourse.put(courseKey, stats);
                }

                request.setAttribute("enrollments", enrollments);
                request.setAttribute("attendanceByCoursepublic", attendanceByCoursepublic);
                request.setAttribute("statisticsByCourse", statisticsByCourse);
            }

            request.getRequestDispatcher("/views/student/history.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading attendance history: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error loading attendance history");
            request.getRequestDispatcher("/views/student/history.jsp").forward(request, response);
        }
    }
}
