package com.attendance.servlets.admin;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * ManageStudentsServlet - Admin CRUD operations for students
 */
@WebServlet("/admin/manage-students")
public class ManageStudentsServlet extends HttpServlet {

    private StudentDAO studentDAO;
    private DepartmentDAO departmentDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
        departmentDAO = new DepartmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        System.out.println("ManageStudentsServlet - Action: " + action);

        try {
            if ("delete".equals(action)) {
                handleDelete(request, response);
            } else if ("edit".equals(action)) {
                handleEditForm(request, response);
            } else {
                handleList(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in ManageStudentsServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            handleList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        System.out.println("ManageStudentsServlet POST - Action: " + action);

        try {
            if ("add".equals(action)) {
                handleAdd(request, response);
            } else if ("update".equals(action)) {
                handleUpdate(request, response);
            } else {
                handleList(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in ManageStudentsServlet POST: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            handleList(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get all students
            List<Student> students = studentDAO.findAll();

            // Get all departments for dropdown
            List<Department> departments = departmentDAO.findAll();

            System.out.println("Loading students list - Count: " +
                    (students != null ? students.size() : 0));

            request.setAttribute("students", students);
            request.setAttribute("departments", departments);

            request.getRequestDispatcher("/views/admin/manage-students.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading students list: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Failed to load students", e);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentIdParam = request.getParameter("studentId");

        System.out.println("Delete request - studentId parameter: " + studentIdParam);

        if (studentIdParam == null || studentIdParam.trim().isEmpty()) {
            System.err.println("StudentId is null or empty");
            request.getSession().setAttribute("error", "Student ID is required for deletion");
            response.sendRedirect(request.getContextPath() + "/admin/manage-students");
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdParam.trim());
            System.out.println("Attempting to delete student with ID: " + studentId);

            // Get student details before deletion
            Student student = studentDAO.findById(studentId);

            if (student == null) {
                System.err.println("Student not found: " + studentId);
                request.getSession().setAttribute("error", "Student not found");
                response.sendRedirect(request.getContextPath() + "/admin/manage-students");
                return;
            }

            // Delete the student
            boolean deleted = studentDAO.delete(studentId);

            if (deleted) {
                System.out.println("Student deleted successfully: " + student.getName());
                request.getSession().setAttribute("success",
                        "Student '" + student.getName() + "' deleted successfully!");
            } else {
                System.err.println("Failed to delete student: " + studentId);
                request.getSession().setAttribute("error", "Failed to delete student");
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid student ID format: " + studentIdParam);
            e.printStackTrace();
            request.getSession().setAttribute("error", "Invalid student ID format");
        } catch (Exception e) {
            System.err.println("Error deleting student: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error deleting student: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-students");
    }

    private void handleEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentIdParam = request.getParameter("studentId");

        if (studentIdParam == null || studentIdParam.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Student ID is required");
            response.sendRedirect(request.getContextPath() + "/admin/manage-students");
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdParam);
            Student student = studentDAO.findById(studentId);

            if (student == null) {
                request.getSession().setAttribute("error", "Student not found");
                response.sendRedirect(request.getContextPath() + "/admin/manage-students");
                return;
            }

            // Get departments for dropdown
            List<Department> departments = departmentDAO.findAll();

            request.setAttribute("student", student);
            request.setAttribute("departments", departments);
            request.setAttribute("editMode", true);

            request.getRequestDispatcher("/views/admin/edit-student.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            System.err.println("Invalid student ID: " + studentIdParam);
            request.getSession().setAttribute("error", "Invalid student ID");
            response.sendRedirect(request.getContextPath() + "/admin/manage-students");
        } catch (Exception e) {
            System.err.println("Error loading student for edit: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error loading student");
            response.sendRedirect(request.getContextPath() + "/admin/manage-students");
        }
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Create new student from form data
            Student student = new Student();
            student.setRollNumber(request.getParameter("rollNumber"));
            student.setName(request.getParameter("name"));
            student.setEmail(request.getParameter("email"));
            student.setPassword(request.getParameter("password")); // Should be hashed in production
            student.setDepartmentId(Integer.parseInt(request.getParameter("departmentId")));
            student.setYear(Integer.parseInt(request.getParameter("year")));
            student.setSemester(Integer.parseInt(request.getParameter("semester")));
            student.setPhone(request.getParameter("phone"));
            student.setStatus("ACTIVE");

            boolean created = studentDAO.create(student);

            if (created) {
                System.out.println("Student created successfully: " + student.getName());
                request.getSession().setAttribute("success", "Student added successfully!");
            } else {
                System.err.println("Failed to create student");
                request.getSession().setAttribute("error", "Failed to add student");
            }

        } catch (Exception e) {
            System.err.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error adding student: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-students");
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));

            Student student = studentDAO.findById(studentId);

            if (student == null) {
                request.getSession().setAttribute("error", "Student not found");
                response.sendRedirect(request.getContextPath() + "/admin/manage-students");
                return;
            }

            // Update student fields
            student.setRollNumber(request.getParameter("rollNumber"));
            student.setName(request.getParameter("name"));
            student.setEmail(request.getParameter("email"));

            // Only update password if provided
            String newPassword = request.getParameter("password");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                student.setPasswordHash(newPassword); // FIXED
            }

            student.setDepartmentId(Integer.parseInt(request.getParameter("departmentId")));
            student.setYear(Integer.parseInt(request.getParameter("year")));
            student.setSemester(Integer.parseInt(request.getParameter("semester")));
            student.setPhone(request.getParameter("phone"));
            student.setStatus(request.getParameter("status"));

            boolean updated = studentDAO.update(student);

            if (updated) {
                System.out.println("Student updated successfully: " + student.getName());
                request.getSession().setAttribute("success", "Student updated successfully!");
            } else {
                System.err.println("Failed to update student");
                request.getSession().setAttribute("error", "Failed to update student");
            }

        } catch (Exception e) {
            System.err.println("Error updating student: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error updating student: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-students");
    }

}
