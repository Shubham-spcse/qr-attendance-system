package com.attendance.servlets.auth;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * LoginServlet - Handles user authentication (Admin, Staff, Student)
 * Uses plain-text password comparison
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AdminDAO adminDAO;
    private StaffDAO staffDAO;
    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        adminDAO = new AdminDAO();
        staffDAO = new StaffDAO();
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            String userRole = (String) session.getAttribute("userRole");
            redirectToDashboard(response, userRole, request.getContextPath());
            return;
        }

        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userType = request.getParameter("userType");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("Login attempt - Type: " + userType + ", Email: " + email);

        // Validation
        if (userType == null || email == null || password == null ||
                userType.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
            return;
        }

        boolean authenticated = false;
        Object user = null;

        try {
            switch (userType.toUpperCase()) {
                case "ADMIN":
                    user = authenticateAdmin(email, password);
                    if (user != null) {
                        authenticated = true;
                        adminDAO.updateLastLogin(((Admin) user).getAdminId());
                    }
                    break;

                case "STAFF":
                    user = authenticateStaff(email, password);
                    if (user != null) {
                        authenticated = true;
                        staffDAO.updateLastLogin(((Staff) user).getStaffId());
                    }
                    break;

                case "STUDENT":
                    user = authenticateStudent(email, password);
                    if (user != null) {
                        authenticated = true;
                        studentDAO.updateLastLogin(((Student) user).getStudentId());
                    }
                    break;

                default:
                    System.out.println("Invalid user type: " + userType);
                    break;
            }

            if (authenticated) {
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userRole", userType.toUpperCase());
                session.setAttribute("userId", getUserId(user, userType));
                session.setAttribute("userName", getUserName(user, userType));
                session.setMaxInactiveInterval(1800); // 30 minutes

                System.out.println("Session created for user: " + getUserName(user, userType));

                // Redirect to dashboard
                redirectToDashboard(response, userType.toUpperCase(), request.getContextPath());

            } else {
                // Authentication failed
                System.out.println("Authentication failed for: " + email);
                request.setAttribute("error", "Invalid email or password");
                request.setAttribute("userType", userType);
                request.setAttribute("email", email);
                request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "System error. Please try again later.");
            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
        }
    }

    /**
     * Authenticate Admin with plain-text password
     */
    private Admin authenticateAdmin(String email, String password) {
        Admin admin = adminDAO.findByEmail(email.toLowerCase());

        if (admin != null) {
            System.out.println("Admin found: " + admin.getEmail());

            // Plain-text password comparison
            String storedPassword = admin.getPasswordHash();

            System.out.println("=== Password Verification ===");
            System.out.println("Plain password: [" + password + "]");
            System.out.println("Stored hash: [" + storedPassword + "]");
            System.out.println("Hash length: " + storedPassword.length());

            // Direct string comparison
            if (password.equals(storedPassword)) {
                System.out.println("Admin authentication successful");
                return admin;
            } else {
                System.out.println("Admin password mismatch");
            }
        } else {
            System.out.println("Admin not found for email: " + email);
        }

        return null;
    }

    /**
     * Authenticate Staff with plain-text password
     */
    private Staff authenticateStaff(String email, String password) {
        Staff staff = staffDAO.findByEmail(email.toLowerCase());

        if (staff != null) {
            System.out.println("Staff found: " + staff.getEmail());

            // Plain-text password comparison
            String storedPassword = staff.getPasswordHash();

            System.out.println("=== Password Verification ===");
            System.out.println("Plain password: [" + password + "]");
            System.out.println("Stored hash: [" + storedPassword + "]");
            System.out.println("Hash length: " + storedPassword.length());
            System.out.println("Using plain text comparison");

            // Direct string comparison
            if (password.equals(storedPassword)) {
                System.out.println("Plain text match: true");
                System.out.println("Staff authentication successful");
                return staff;
            } else {
                System.out.println("Plain text match: false");
                System.out.println("Staff password mismatch");
            }
        } else {
            System.out.println("Staff not found for email: " + email);
        }

        return null;
    }

    /**
     * Authenticate Student with plain-text password
     */
    private Student authenticateStudent(String email, String password) {
        Student student = studentDAO.findByEmail(email.toLowerCase());

        if (student != null) {
            System.out.println("Student found: " + student.getEmail());

            // Plain-text password comparison
            String storedPassword = student.getPasswordHash();

            System.out.println("=== Password Verification ===");
            System.out.println("Plain password: [" + password + "]");
            System.out.println("Stored hash: [" + storedPassword + "]");

            // Direct string comparison
            if (password.equals(storedPassword)) {
                System.out.println("Student authentication successful");
                return student;
            } else {
                System.out.println("Student password mismatch");
            }
        } else {
            System.out.println("Student not found for email: " + email);
        }

        return null;
    }

    /**
     * Redirect user to appropriate dashboard
     */
    private void redirectToDashboard(HttpServletResponse response, String role, String contextPath)
            throws IOException {
        String dashboardUrl = contextPath + "/" + role.toLowerCase() + "/dashboard";
        System.out.println("Redirecting to: " + dashboardUrl);
        response.sendRedirect(dashboardUrl);
    }

    /**
     * Get user ID based on user type
     */
    private int getUserId(Object user, String userType) {
        switch (userType.toUpperCase()) {
            case "ADMIN":
                return ((Admin) user).getAdminId();
            case "STAFF":
                return ((Staff) user).getStaffId();
            case "STUDENT":
                return ((Student) user).getStudentId();
            default:
                return 0;
        }
    }

    /**
     * Get user name based on user type
     */
    private String getUserName(Object user, String userType) {
        switch (userType.toUpperCase()) {
            case "ADMIN":
                return ((Admin) user).getName();
            case "STAFF":
                return ((Staff) user).getName();
            case "STUDENT":
                return ((Student) user).getName();
            default:
                return "Unknown";
        }
    }
}
