package com.attendance.servlets.staff;

import com.attendance.dao.*;
import com.attendance.models.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/staff/active-sessions")
public class StaffSessionsServlet extends HttpServlet {

    private AttendanceSessionDAO sessionDAO;

    @Override
    public void init() throws ServletException {
        sessionDAO = new AttendanceSessionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Staff staff = (Staff) request.getSession().getAttribute("user");

        if (staff == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get active sessions for this staff member
            List<AttendanceSession> activeSessions = sessionDAO.findByStaffAndStatus(staff.getStaffId(), "Active");

            request.setAttribute("activeSessions", activeSessions);
            request.getRequestDispatcher("/views/staff/active-sessions.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading sessions");
            request.getRequestDispatcher("/views/staff/active-sessions.jsp").forward(request, response);
        }
    }
}
