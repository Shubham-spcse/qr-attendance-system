package com.attendance.servlets.auth;
import com.attendance.dao.StudentDAO;
import com.attendance.models.Student;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            Student student = new Student();
            student.setRollNumber(req.getParameter("rollNumber"));
            student.setName(req.getParameter("name"));
            student.setEmail(req.getParameter("email"));
            student.setPhone(req.getParameter("phone"));
            student.setDepartmentId(Integer.parseInt(req.getParameter("departmentId")));
            student.setYear(Integer.parseInt(req.getParameter("year")));
            student.setSection(req.getParameter("section"));
            student.setSemester(Integer.parseInt(req.getParameter("semester")));
            student.setPasswordHash(req.getParameter("password"));

            if (studentDAO.create(student)) {
                resp.sendRedirect(req.getContextPath() + "/login?registered=true");
            } else {
                req.setAttribute("error", "Registration failed");
                req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
        }
    }
}
