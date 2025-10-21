package com.attendance.servlets.admin;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * ManageCoursesServlet - Admin CRUD operations for courses
 */
@WebServlet("/admin/manage-courses")
public class ManageCoursesServlet extends HttpServlet {

    private CourseDAO courseDAO;
    private DepartmentDAO departmentDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
        departmentDAO = new DepartmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        System.out.println("ManageCoursesServlet - Action: " + action);

        try {
            if ("delete".equals(action)) {
                handleDelete(request, response);
            } else if ("edit".equals(action)) {
                handleEditForm(request, response);
            } else {
                handleList(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in ManageCoursesServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            handleList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        System.out.println("ManageCoursesServlet POST - Action: " + action);

        try {
            if ("add".equals(action)) {
                handleAdd(request, response);
            } else if ("update".equals(action)) {
                handleUpdate(request, response);
            } else {
                handleList(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in ManageCoursesServlet POST: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            handleList(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Course> courses = courseDAO.findAll();
            List<Department> departments = departmentDAO.findAll();

            System.out.println("Loading courses list - Count: " +
                    (courses != null ? courses.size() : 0));

            request.setAttribute("courses", courses);
            request.setAttribute("departments", departments);

            request.getRequestDispatcher("/views/admin/manage-courses.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading courses list: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Failed to load courses", e);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String courseIdParam = request.getParameter("courseId");
        if (courseIdParam == null) {
            courseIdParam = request.getParameter("id");
        }

        System.out.println("Delete request - courseId parameter: " + courseIdParam);

        if (courseIdParam == null || courseIdParam.trim().isEmpty()) {
            System.err.println("CourseId is null or empty");
            request.getSession().setAttribute("error", "Course ID is required for deletion");
            response.sendRedirect(request.getContextPath() + "/admin/manage-courses");
            return;
        }

        try {
            int courseId = Integer.parseInt(courseIdParam.trim());
            System.out.println("Attempting to delete course with ID: " + courseId);

            Course course = courseDAO.findById(courseId);

            if (course == null) {
                System.err.println("Course not found: " + courseId);
                request.getSession().setAttribute("error", "Course not found");
                response.sendRedirect(request.getContextPath() + "/admin/manage-courses");
                return;
            }

            boolean deleted = courseDAO.delete(courseId);

            if (deleted) {
                System.out.println("Course deleted successfully: " + course.getCourseName());
                request.getSession().setAttribute("success",
                        "Course '" + course.getCourseName() + "' deleted successfully!");
            } else {
                System.err.println("Failed to delete course: " + courseId);
                request.getSession().setAttribute("error", "Failed to delete course");
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid course ID format: " + courseIdParam);
            e.printStackTrace();
            request.getSession().setAttribute("error", "Invalid course ID format");
        } catch (Exception e) {
            System.err.println("Error deleting course: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error deleting course: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-courses");
    }

    private void handleEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String courseIdParam = request.getParameter("courseId");
        if (courseIdParam == null) {
            courseIdParam = request.getParameter("id");
        }

        System.out.println("Edit request - courseId parameter: " + courseIdParam);

        if (courseIdParam == null || courseIdParam.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Course ID is required");
            response.sendRedirect(request.getContextPath() + "/admin/manage-courses");
            return;
        }

        try {
            int courseId = Integer.parseInt(courseIdParam);
            Course course = courseDAO.findById(courseId);

            if (course == null) {
                request.getSession().setAttribute("error", "Course not found");
                response.sendRedirect(request.getContextPath() + "/admin/manage-courses");
                return;
            }

            List<Department> departments = departmentDAO.findAll();

            request.setAttribute("course", course);
            request.setAttribute("departments", departments);
            request.setAttribute("editMode", true);

            System.out.println("Loading edit form for course: " + course.getCourseName());

            request.getRequestDispatcher("/views/admin/edit-course.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            System.err.println("Invalid course ID: " + courseIdParam);
            request.getSession().setAttribute("error", "Invalid course ID");
            response.sendRedirect(request.getContextPath() + "/admin/manage-courses");
        } catch (Exception e) {
            System.err.println("Error loading course for edit: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error loading course");
            response.sendRedirect(request.getContextPath() + "/admin/manage-courses");
        }
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Course course = new Course();
            course.setCourseCode(request.getParameter("courseCode"));
            course.setCourseName(request.getParameter("courseName"));
            course.setDepartmentId(Integer.parseInt(request.getParameter("departmentId")));
            course.setCredits(Integer.parseInt(request.getParameter("credits")));
            course.setSemester(Integer.parseInt(request.getParameter("semester")));
            course.setAcademicYear(request.getParameter("academicYear"));
            course.setCourseType(request.getParameter("courseType"));
            course.setDescription(request.getParameter("description"));
            course.setStatus("ACTIVE");

            boolean created = courseDAO.create(course);

            if (created) {
                System.out.println("Course created successfully: " + course.getCourseName());
                request.getSession().setAttribute("success", "Course added successfully!");
            } else {
                System.err.println("Failed to create course");
                request.getSession().setAttribute("error", "Failed to add course");
            }

        } catch (Exception e) {
            System.err.println("Error adding course: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error adding course: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-courses");
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int courseId = Integer.parseInt(request.getParameter("courseId"));

            Course course = courseDAO.findById(courseId);

            if (course == null) {
                request.getSession().setAttribute("error", "Course not found");
                response.sendRedirect(request.getContextPath() + "/admin/manage-courses");
                return;
            }

            course.setCourseCode(request.getParameter("courseCode"));
            course.setCourseName(request.getParameter("courseName"));
            course.setDepartmentId(Integer.parseInt(request.getParameter("departmentId")));
            course.setCredits(Integer.parseInt(request.getParameter("credits")));
            course.setSemester(Integer.parseInt(request.getParameter("semester")));
            course.setAcademicYear(request.getParameter("academicYear"));
            course.setCourseType(request.getParameter("courseType"));
            course.setDescription(request.getParameter("description"));
            course.setStatus(request.getParameter("status"));

            boolean updated = courseDAO.update(course);

            if (updated) {
                System.out.println("Course updated successfully: " + course.getCourseName());
                request.getSession().setAttribute("success", "Course updated successfully!");
            } else {
                System.err.println("Failed to update course");
                request.getSession().setAttribute("error", "Failed to update course");
            }

        } catch (Exception e) {
            System.err.println("Error updating course: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error updating course: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-courses");
    }
}
