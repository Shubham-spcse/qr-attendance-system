package com.attendance.servlets.admin;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * ManageStaffServlet - Admin CRUD operations for staff
 */
@WebServlet("/admin/manage-staff")
public class ManageStaffServlet extends HttpServlet {

    private StaffDAO staffDAO;
    private DepartmentDAO departmentDAO;

    @Override
    public void init() throws ServletException {
        staffDAO = new StaffDAO();
        departmentDAO = new DepartmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        System.out.println("ManageStaffServlet - Action: " + action);

        try {
            if ("delete".equals(action)) {
                handleDelete(request, response);
            } else if ("edit".equals(action)) {
                handleEditForm(request, response);
            } else {
                handleList(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in ManageStaffServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            handleList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        System.out.println("ManageStaffServlet POST - Action: " + action);

        try {
            if ("add".equals(action)) {
                handleAdd(request, response);
            } else if ("update".equals(action)) {
                handleUpdate(request, response);
            } else {
                handleList(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in ManageStaffServlet POST: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            handleList(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Staff> staffList = staffDAO.findAll();
            List<Department> departments = departmentDAO.findAll();

            System.out.println("Loading staff list - Count: " +
                    (staffList != null ? staffList.size() : 0));

            request.setAttribute("staffList", staffList);
            request.setAttribute("departments", departments);

            request.getRequestDispatcher("/views/admin/manage-staff.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading staff list: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Failed to load staff", e);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String staffIdParam = request.getParameter("staffId");
        if (staffIdParam == null) {
            staffIdParam = request.getParameter("id");
        }

        System.out.println("Delete request - staffId parameter: " + staffIdParam);

        if (staffIdParam == null || staffIdParam.trim().isEmpty()) {
            System.err.println("StaffId is null or empty");
            request.getSession().setAttribute("error", "Staff ID is required for deletion");
            response.sendRedirect(request.getContextPath() + "/admin/manage-staff");
            return;
        }

        try {
            int staffId = Integer.parseInt(staffIdParam.trim());
            System.out.println("Attempting to delete staff with ID: " + staffId);

            Staff staff = staffDAO.findById(staffId);

            if (staff == null) {
                System.err.println("Staff not found: " + staffId);
                request.getSession().setAttribute("error", "Staff not found");
                response.sendRedirect(request.getContextPath() + "/admin/manage-staff");
                return;
            }

            boolean deleted = staffDAO.delete(staffId);

            if (deleted) {
                System.out.println("Staff deleted successfully: " + staff.getName());
                request.getSession().setAttribute("success",
                        "Staff member '" + staff.getName() + "' deleted successfully!");
            } else {
                System.err.println("Failed to delete staff: " + staffId);
                request.getSession().setAttribute("error", "Failed to delete staff");
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid staff ID format: " + staffIdParam);
            e.printStackTrace();
            request.getSession().setAttribute("error", "Invalid staff ID format");
        } catch (Exception e) {
            System.err.println("Error deleting staff: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error deleting staff: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-staff");
    }

    private void handleEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String staffIdParam = request.getParameter("staffId");
        if (staffIdParam == null) {
            staffIdParam = request.getParameter("id");
        }

        System.out.println("Edit request - staffId parameter: " + staffIdParam);

        if (staffIdParam == null || staffIdParam.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Staff ID is required");
            response.sendRedirect(request.getContextPath() + "/admin/manage-staff");
            return;
        }

        try {
            int staffId = Integer.parseInt(staffIdParam);
            Staff staff = staffDAO.findById(staffId);

            if (staff == null) {
                request.getSession().setAttribute("error", "Staff not found");
                response.sendRedirect(request.getContextPath() + "/admin/manage-staff");
                return;
            }

            List<Department> departments = departmentDAO.findAll();

            request.setAttribute("staff", staff);
            request.setAttribute("departments", departments);
            request.setAttribute("editMode", true);

            System.out.println("Loading edit form for staff: " + staff.getName());

            request.getRequestDispatcher("/views/admin/edit-staff.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            System.err.println("Invalid staff ID: " + staffIdParam);
            request.getSession().setAttribute("error", "Invalid staff ID");
            response.sendRedirect(request.getContextPath() + "/admin/manage-staff");
        } catch (Exception e) {
            System.err.println("Error loading staff for edit: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error loading staff");
            response.sendRedirect(request.getContextPath() + "/admin/manage-staff");
        }
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Staff staff = new Staff();
            staff.setEmployeeId(request.getParameter("employeeId"));
            staff.setName(request.getParameter("name"));
            staff.setEmail(request.getParameter("email"));
            staff.setPasswordHash(request.getParameter("password"));
            staff.setDepartmentId(Integer.parseInt(request.getParameter("departmentId")));
            staff.setDesignation(request.getParameter("designation"));
            staff.setPhone(request.getParameter("phone"));
            staff.setQualification(request.getParameter("qualification"));
            staff.setOfficeLocation(request.getParameter("officeLocation"));
            staff.setStatus("ACTIVE");

            boolean created = staffDAO.create(staff);

            if (created) {
                System.out.println("Staff created successfully: " + staff.getName());
                request.getSession().setAttribute("success", "Staff added successfully!");
            } else {
                System.err.println("Failed to create staff");
                request.getSession().setAttribute("error", "Failed to add staff");
            }

        } catch (Exception e) {
            System.err.println("Error adding staff: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error adding staff: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-staff");
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int staffId = Integer.parseInt(request.getParameter("staffId"));

            Staff staff = staffDAO.findById(staffId);

            if (staff == null) {
                request.getSession().setAttribute("error", "Staff not found");
                response.sendRedirect(request.getContextPath() + "/admin/manage-staff");
                return;
            }

            staff.setEmployeeId(request.getParameter("employeeId"));
            staff.setName(request.getParameter("name"));
            staff.setEmail(request.getParameter("email"));

            String newPassword = request.getParameter("password");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                staff.setPasswordHash(newPassword);
            }

            staff.setDepartmentId(Integer.parseInt(request.getParameter("departmentId")));
            staff.setDesignation(request.getParameter("designation"));
            staff.setPhone(request.getParameter("phone"));
            staff.setQualification(request.getParameter("qualification"));
            staff.setOfficeLocation(request.getParameter("officeLocation"));
            staff.setStatus(request.getParameter("status"));

            boolean updated = staffDAO.update(staff);

            if (updated) {
                System.out.println("Staff updated successfully: " + staff.getName());
                request.getSession().setAttribute("success", "Staff updated successfully!");
            } else {
                System.err.println("Failed to update staff");
                request.getSession().setAttribute("error", "Failed to update staff");
            }

        } catch (Exception e) {
            System.err.println("Error updating staff: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error updating staff: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-staff");
    }
}
