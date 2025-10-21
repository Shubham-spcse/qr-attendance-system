package com.attendance.servlets.admin;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * ManageDepartmentsServlet - Complete department management (CRUD)
 * URL: /admin/manage-departments
 */
@WebServlet("/admin/manage-departments")
public class ManageDepartmentsServlet extends HttpServlet {

    private DepartmentDAO departmentDAO;

    @Override
    public void init() throws ServletException {
        departmentDAO = new DepartmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Admin admin = (Admin) request.getSession().getAttribute("user");
        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("edit".equals(action)) {
                showEditForm(request, response);
            } else {
                showDepartmentList(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in manage departments: " + e.getMessage());
            e.printStackTrace();
            showDepartmentList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Admin admin = (Admin) request.getSession().getAttribute("user");
        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                createDepartment(request, response);
            } else if ("update".equals(action)) {
                updateDepartment(request, response);
            } else if ("delete".equals(action)) {
                deleteDepartment(request, response);
            } else if ("updateStatus".equals(action)) {
                updateStatus(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error processing department action: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            showDepartmentList(request, response);
        }
    }

    private void showDepartmentList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Department> departments = departmentDAO.findAll();
        int totalDepartments = departmentDAO.count();

        request.setAttribute("departments", departments);
        request.setAttribute("totalDepartments", totalDepartments);
        request.getRequestDispatcher("/views/admin/manage-departments.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int departmentId = Integer.parseInt(request.getParameter("id"));
        Department department = departmentDAO.findById(departmentId);

        request.setAttribute("department", department);
        request.setAttribute("editMode", true);
        request.getRequestDispatcher("/views/admin/department-form.jsp").forward(request, response);
    }

    private void createDepartment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Department department = new Department();
        department.setDepartmentCode(request.getParameter("departmentCode"));
        department.setDepartmentName(request.getParameter("departmentName"));
        department.setDescription(request.getParameter("description"));
        department.setHeadOfDepartment(request.getParameter("headOfDepartment"));
        department.setStatus("ACTIVE");

        if (departmentDAO.create(department)) {
            request.setAttribute("success", "Department created successfully");
        } else {
            request.setAttribute("error", "Failed to create department");
        }

        showDepartmentList(request, response);
    }

    private void updateDepartment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Department department = departmentDAO.findById(
            Integer.parseInt(request.getParameter("departmentId")));

        department.setDepartmentCode(request.getParameter("departmentCode"));
        department.setDepartmentName(request.getParameter("departmentName"));
        department.setDescription(request.getParameter("description"));
        department.setHeadOfDepartment(request.getParameter("headOfDepartment"));
        department.setStatus(request.getParameter("status"));

        if (departmentDAO.update(department)) {
            request.setAttribute("success", "Department updated successfully");
        } else {
            request.setAttribute("error", "Failed to update department");
        }

        showDepartmentList(request, response);
    }

    private void deleteDepartment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int departmentId = Integer.parseInt(request.getParameter("departmentId"));

        if (departmentDAO.delete(departmentId)) {
            request.setAttribute("success", "Department deleted successfully");
        } else {
            request.setAttribute("error", "Failed to delete department. May have dependent records.");
        }

        showDepartmentList(request, response);
    }

    private void updateStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int departmentId = Integer.parseInt(request.getParameter("departmentId"));
        String status = request.getParameter("status");

        if (departmentDAO.updateStatus(departmentId, status)) {
            request.setAttribute("success", "Department status updated");
        } else {
            request.setAttribute("error", "Failed to update status");
        }

        showDepartmentList(request, response);
    }
}
