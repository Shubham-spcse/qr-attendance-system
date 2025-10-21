package com.attendance.servlets.student;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.List;

/**
 * DownloadReportServlet - Generates and downloads attendance report
 * URL: /student/download-report
 */
@WebServlet("/student/download-report")
public class DownloadReportServlet extends HttpServlet {

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

        String format = request.getParameter("format");
        String courseIdParam = request.getParameter("courseId");

        if (format == null || format.isEmpty()) {
            format = "CSV";
        }

        try {
            if ("CSV".equalsIgnoreCase(format)) {
                generateCSVReport(student, courseIdParam, response);
            } else if ("PDF".equalsIgnoreCase(format)) {
                // PDF generation can be implemented later with iText or similar
                response.setContentType("text/plain");
                response.getWriter().println("PDF generation coming soon!");
            }

        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage());
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().println("Error generating report: " + e.getMessage());
        }
    }

    private void generateCSVReport(Student student, String courseIdParam, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", 
            "attachment; filename=attendance_report_" + student.getRollNumber() + ".csv");

        PrintWriter writer = response.getWriter();

        // CSV Header
        writer.println("Course Code,Course Name,Session Date,Session Time,Status,Marked At");

        if (courseIdParam != null && !courseIdParam.isEmpty()) {
            // Single course report
            int courseId = Integer.parseInt(courseIdParam);
            List<Attendance> attendanceList = attendanceDAO.findByStudentAndCourse(
                student.getStudentId(), courseId);

            for (Attendance att : attendanceList) {
                writer.printf("%s,%s,%s,%s,%s,%s%n",
                    escapeCSV(att.getCourseCode()),
                    escapeCSV(att.getCourseName()),
                    att.getMarkedAt() != null ? att.getMarkedAt().toString().split(" ")[0] : "N/A",
                    att.getMarkedAt() != null ? att.getMarkedAt().toString().split(" ")[1] : "N/A",
                    att.getStatus().getDisplayName(),
                    att.getMarkedAt() != null ? att.getMarkedAt() : "Not Marked"
                );
            }
        } else {
            // All courses report
            List<Enrollment> enrollments = enrollmentDAO.findByStudent(student.getStudentId());

            for (Enrollment enrollment : enrollments) {
                List<Attendance> attendanceList = attendanceDAO.findByStudentAndCourse(
                    student.getStudentId(), enrollment.getCourseId());

                for (Attendance att : attendanceList) {
                    writer.printf("%s,%s,%s,%s,%s,%s%n",
                        escapeCSV(enrollment.getCourseCode()),
                        escapeCSV(enrollment.getCourseName()),
                        att.getMarkedAt() != null ? att.getMarkedAt().toString().split(" ")[0] : "N/A",
                        att.getMarkedAt() != null ? att.getMarkedAt().toString().split(" ")[1] : "N/A",
                        att.getStatus().getDisplayName(),
                        att.getMarkedAt() != null ? att.getMarkedAt() : "Not Marked"
                    );
                }
            }
        }

        writer.flush();
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
