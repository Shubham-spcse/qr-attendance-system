package com.attendance.servlets.admin;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * SystemAnalyticsServlet - Advanced analytics and system-wide reports
 * URL: /admin/analytics
 */
@WebServlet("/admin/analytics")
public class SystemAnalyticsServlet extends HttpServlet {

    private StudentDAO studentDAO;
    private StaffDAO staffDAO;
    private CourseDAO courseDAO;
    private AttendanceSessionDAO sessionDAO;
    private AttendanceDAO attendanceDAO;
    private DepartmentDAO departmentDAO;
    private EnrollmentDAO enrollmentDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
        staffDAO = new StaffDAO();
        courseDAO = new CourseDAO();
        sessionDAO = new AttendanceSessionDAO();
        attendanceDAO = new AttendanceDAO();
        departmentDAO = new DepartmentDAO();
        enrollmentDAO = new EnrollmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Admin admin = (Admin) request.getSession().getAttribute("user");
        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String reportType = request.getParameter("type");
        String action = request.getParameter("action");

        try {
            if ("download".equals(action)) {
                downloadReport(request, response, reportType);
                return;
            }

            if ("attendance".equals(reportType)) {
                generateAttendanceAnalytics(request, response);
            } else if ("department".equals(reportType)) {
                generateDepartmentAnalytics(request, response);
            } else if ("course".equals(reportType)) {
                generateCourseAnalytics(request, response);
            } else {
                showAnalyticsDashboard(request, response);
            }

        } catch (Exception e) {
            System.err.println("Error generating analytics: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error generating analytics");
            showAnalyticsDashboard(request, response);
        }
    }

    private void showAnalyticsDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Overall system metrics
        List<Student> students = studentDAO.findAll();
        List<Staff> staff = staffDAO.findAll();
        List<Department> departments = departmentDAO.findAll();

        request.setAttribute("totalStudents", students.size());
        request.setAttribute("totalStaff", staff.size());
        request.setAttribute("totalDepartments", departments.size());

        request.getRequestDispatcher("/views/admin/analytics.jsp").forward(request, response);
    }

    private void generateAttendanceAnalytics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Department> departments = departmentDAO.findAll();
        Map<String, Map<String, Object>> deptAttendance = new HashMap<>();

        for (Department dept : departments) {
            List<Student> deptStudents = studentDAO.findByDepartment(dept.getDepartmentId());

            int totalSessions = 0;
            int totalPresent = 0;
            int totalLate = 0;
            int totalAbsent = 0;

            for (Student student : deptStudents) {
                List<Attendance> studentAttendance = attendanceDAO.findByStudent(student.getStudentId());
                totalSessions += studentAttendance.size();

                for (Attendance att : studentAttendance) {
                    if (att.isPresent()) totalPresent++;
                    else if (att.isLate()) totalLate++;
                    else if (att.isAbsent()) totalAbsent++;
                }
            }

            double avgAttendance = totalSessions > 0 ? 
                ((double)(totalPresent + totalLate) / totalSessions) * 100.0 : 0.0;

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalSessions", totalSessions);
            stats.put("present", totalPresent);
            stats.put("late", totalLate);
            stats.put("absent", totalAbsent);
            stats.put("percentage", avgAttendance);
            stats.put("studentCount", deptStudents.size());

            deptAttendance.put(dept.getDepartmentName(), stats);
        }

        request.setAttribute("departmentAttendance", deptAttendance);
        request.setAttribute("reportType", "attendance");
        request.getRequestDispatcher("/views/admin/attendance-analytics.jsp").forward(request, response);
    }

    private void generateDepartmentAnalytics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Department> departments = departmentDAO.findAll();
        List<Map<String, Object>> deptStats = new ArrayList<>();

        for (Department dept : departments) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("department", dept);
            stats.put("studentCount", studentDAO.findByDepartment(dept.getDepartmentId()).size());
            stats.put("staffCount", staffDAO.findByDepartment(dept.getDepartmentId()).size());
            stats.put("courseCount", courseDAO.findByDepartment(dept.getDepartmentId()).size());

            deptStats.add(stats);
        }

        request.setAttribute("departmentStats", deptStats);
        request.setAttribute("reportType", "department");
        request.getRequestDispatcher("/views/admin/department-analytics.jsp").forward(request, response);
    }

    private void generateCourseAnalytics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Course> courses = courseDAO.findAll();
        List<Map<String, Object>> courseStats = new ArrayList<>();

        for (Course course : courses) {
            int enrolledStudents = enrollmentDAO.countByCourse(course.getCourseId());

            Map<String, Object> stats = new HashMap<>();
            stats.put("course", course);
            stats.put("enrolledStudents", enrolledStudents);

            courseStats.add(stats);
        }

        request.setAttribute("courseStats", courseStats);
        request.setAttribute("reportType", "course");
        request.getRequestDispatcher("/views/admin/course-analytics.jsp").forward(request, response);
    }

    private void downloadReport(HttpServletRequest request, HttpServletResponse response, String reportType)
            throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", 
            "attachment; filename=system_report_" + reportType + ".csv");

        PrintWriter writer = response.getWriter();

        if ("attendance".equals(reportType)) {
            downloadAttendanceReport(writer);
        } else if ("department".equals(reportType)) {
            downloadDepartmentReport(writer);
        } else if ("student".equals(reportType)) {
            downloadStudentReport(writer);
        }

        writer.flush();
    }

    private void downloadAttendanceReport(PrintWriter writer) {
        writer.println("System-Wide Attendance Report");
        writer.println();
        writer.println("Department,Total Students,Total Sessions,Present,Late,Absent,Attendance %");

        List<Department> departments = departmentDAO.findAll();

        for (Department dept : departments) {
            List<Student> students = studentDAO.findByDepartment(dept.getDepartmentId());

            int totalSessions = 0;
            int present = 0;
            int late = 0;
            int absent = 0;

            for (Student student : students) {
                List<Attendance> attendance = attendanceDAO.findByStudent(student.getStudentId());
                totalSessions += attendance.size();

                for (Attendance att : attendance) {
                    if (att.isPresent()) present++;
                    else if (att.isLate()) late++;
                    else if (att.isAbsent()) absent++;
                }
            }

            double percentage = totalSessions > 0 ? 
                ((double)(present + late) / totalSessions) * 100.0 : 0.0;

            writer.printf("%s,%d,%d,%d,%d,%d,%.2f%%%n",
                dept.getDepartmentName(),
                students.size(),
                totalSessions,
                present,
                late,
                absent,
                percentage
            );
        }
    }

    private void downloadDepartmentReport(PrintWriter writer) {
        writer.println("Department Statistics Report");
        writer.println();
        writer.println("Department Code,Department Name,Students,Staff,Courses,Status,HOD");

        List<Department> departments = departmentDAO.findAll();

        for (Department dept : departments) {
            int students = studentDAO.findByDepartment(dept.getDepartmentId()).size();
            int staff = staffDAO.findByDepartment(dept.getDepartmentId()).size();
            int courses = courseDAO.findByDepartment(dept.getDepartmentId()).size();

            writer.printf("%s,%s,%d,%d,%d,%s,%s%n",
                dept.getDepartmentCode(),
                dept.getDepartmentName(),
                students,
                staff,
                courses,
                dept.getStatus(),
                dept.getHeadOfDepartment() != null ? dept.getHeadOfDepartment() : "N/A"
            );
        }
    }

    private void downloadStudentReport(PrintWriter writer) {
        writer.println("Complete Student Report");
        writer.println();
        writer.println("Roll Number,Name,Email,Department,Year,Section,Semester,Status");

        List<Student> students = studentDAO.findAll();

        for (Student student : students) {
            writer.printf("%s,%s,%s,%s,%d,%s,%d,%s%n",
                student.getRollNumber(),
                student.getName(),
                student.getEmail(),
                student.getDepartmentName() != null ? student.getDepartmentName() : "N/A",
                student.getYear(),
                student.getSection() != null ? student.getSection() : "N/A",
                student.getSemester(),
                student.getStatus()
            );
        }
    }
}
