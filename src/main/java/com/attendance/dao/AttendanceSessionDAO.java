package com.attendance.dao;

import com.attendance.models.AttendanceSession;
import com.attendance.enums.SessionStatus;
import com.attendance.enums.SessionType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AttendanceSessionDAO - Critical DAO for session management
 */
public class AttendanceSessionDAO {

    public boolean create(AttendanceSession session) {
        String sql = "INSERT INTO attendance_sessions (session_code, course_id, staff_id, " +
                    "session_date, start_time, end_time, location, session_type, status, " +
                    "total_students) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, session.getSessionCode());
            pstmt.setInt(2, session.getCourseId());
            pstmt.setInt(3, session.getStaffId());
            pstmt.setDate(4, session.getSessionDate());
            pstmt.setTime(5, session.getStartTime());
            pstmt.setTime(6, session.getEndTime());
            pstmt.setString(7, session.getLocation());
            pstmt.setString(8, session.getSessionType().name());
            pstmt.setString(9, session.getStatus().name());
            pstmt.setInt(10, session.getTotalStudents());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    session.setSessionId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating session: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public AttendanceSession findById(int sessionId) {
        String sql = "SELECT ats.*, c.course_name, c.course_code, s.name as staff_name " +
                    "FROM attendance_sessions ats " +
                    "INNER JOIN courses c ON ats.course_id = c.course_id " +
                    "INNER JOIN staff s ON ats.staff_id = s.staff_id " +
                    "WHERE ats.session_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sessionId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractSessionFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding session: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public AttendanceSession findByCode(String sessionCode) {
        String sql = "SELECT ats.*, c.course_name, c.course_code, s.name as staff_name " +
                    "FROM attendance_sessions ats " +
                    "INNER JOIN courses c ON ats.course_id = c.course_id " +
                    "INNER JOIN staff s ON ats.staff_id = s.staff_id " +
                    "WHERE ats.session_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sessionCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractSessionFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding session by code: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    /**
     * Alias for findByCode - used by MarkAttendanceServlet
     */
    public AttendanceSession findBySessionCode(String sessionCode) {
        return findByCode(sessionCode);
    }


    public List<AttendanceSession> findByStaff(int staffId) {
        String sql = "SELECT ats.*, c.course_name, c.course_code, s.name as staff_name " +
                    "FROM attendance_sessions ats " +
                    "INNER JOIN courses c ON ats.course_id = c.course_id " +
                    "INNER JOIN staff s ON ats.staff_id = s.staff_id " +
                    "WHERE ats.staff_id = ? " +
                    "ORDER BY ats.session_date DESC, ats.start_time DESC";

        return executeSessionQuery(sql, staffId);
    }

    public List<AttendanceSession> findByCourse(int courseId) {
        String sql = "SELECT ats.*, c.course_name, c.course_code, s.name as staff_name " +
                    "FROM attendance_sessions ats " +
                    "INNER JOIN courses c ON ats.course_id = c.course_id " +
                    "INNER JOIN staff s ON ats.staff_id = s.staff_id " +
                    "WHERE ats.course_id = ? " +
                    "ORDER BY ats.session_date DESC";

        return executeSessionQuery(sql, courseId);
    }

    public List<AttendanceSession> findActiveSessions() {
        String sql = "SELECT ats.*, c.course_name, c.course_code, s.name as staff_name " +
                    "FROM attendance_sessions ats " +
                    "INNER JOIN courses c ON ats.course_id = c.course_id " +
                    "INNER JOIN staff s ON ats.staff_id = s.staff_id " +
                    "WHERE ats.status = 'ACTIVE' " +
                    "ORDER BY ats.session_date, ats.start_time";

        List<AttendanceSession> sessions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                sessions.add(extractSessionFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding active sessions: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return sessions;
    }

    /**
     * Find sessions by staff ID and status
     * @param staffId Staff ID
     * @param status Status string (e.g., "ACTIVE", "EXPIRED")
     * @return List of matching sessions
     */
    public List<AttendanceSession> findByStaffAndStatus(int staffId, String status) {
        String sql = "SELECT ats.*, c.course_name, c.course_code, s.name as staff_name " +
                "FROM attendance_sessions ats " +
                "INNER JOIN courses c ON ats.course_id = c.course_id " +
                "INNER JOIN staff s ON ats.staff_id = s.staff_id " +
                "WHERE ats.staff_id = ? AND ats.status = ? " +
                "ORDER BY ats.session_date DESC, ats.start_time DESC";

        List<AttendanceSession> sessions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, staffId);
            pstmt.setString(2, status.toUpperCase());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                sessions.add(extractSessionFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding sessions by staff and status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return sessions;
    }


    public boolean update(AttendanceSession session) {
        String sql = "UPDATE attendance_sessions SET session_code = ?, course_id = ?, " +
                    "staff_id = ?, session_date = ?, start_time = ?, end_time = ?, " +
                    "location = ?, session_type = ?, status = ?, qr_code_data = ?, " +
                    "qr_code_checksum = ?, total_students = ?, present_count = ?, " +
                    "late_count = ?, absent_count = ? WHERE session_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, session.getSessionCode());
            pstmt.setInt(2, session.getCourseId());
            pstmt.setInt(3, session.getStaffId());
            pstmt.setDate(4, session.getSessionDate());
            pstmt.setTime(5, session.getStartTime());
            pstmt.setTime(6, session.getEndTime());
            pstmt.setString(7, session.getLocation());
            pstmt.setString(8, session.getSessionType().name());
            pstmt.setString(9, session.getStatus().name());
            pstmt.setString(10, session.getQrCodeData());
            pstmt.setString(11, session.getQrCodeChecksum());
            pstmt.setInt(12, session.getTotalStudents());
            pstmt.setInt(13, session.getPresentCount());
            pstmt.setInt(14, session.getLateCount());
            pstmt.setInt(15, session.getAbsentCount());
            pstmt.setInt(16, session.getSessionId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating session: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean updateStatus(int sessionId, SessionStatus status) {
        String sql = "UPDATE attendance_sessions SET status = ? WHERE session_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.name());
            pstmt.setInt(2, sessionId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating session status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean updateQRCode(int sessionId, String qrData, String checksum) {
        String sql = "UPDATE attendance_sessions SET qr_code_data = ?, qr_code_checksum = ? " +
                    "WHERE session_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, qrData);
            pstmt.setString(2, checksum);
            pstmt.setInt(3, sessionId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating QR code: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean delete(int sessionId) {
        String sql = "DELETE FROM attendance_sessions WHERE session_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sessionId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting session: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    private List<AttendanceSession> executeSessionQuery(String sql, int id) {
        List<AttendanceSession> sessions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                sessions.add(extractSessionFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error executing session query: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return sessions;
    }

    private AttendanceSession extractSessionFromResultSet(ResultSet rs) throws SQLException {
        AttendanceSession session = new AttendanceSession();
        session.setSessionId(rs.getInt("session_id"));
        session.setSessionCode(rs.getString("session_code"));
        session.setCourseId(rs.getInt("course_id"));
        session.setStaffId(rs.getInt("staff_id"));
        session.setSessionDate(rs.getDate("session_date"));
        session.setStartTime(rs.getTime("start_time"));
        session.setEndTime(rs.getTime("end_time"));
        session.setLocation(rs.getString("location"));
        session.setSessionType(SessionType.valueOf(rs.getString("session_type")));
        session.setStatus(SessionStatus.valueOf(rs.getString("status")));
        session.setQrCodeData(rs.getString("qr_code_data"));
        session.setQrCodeChecksum(rs.getString("qr_code_checksum"));
        session.setTotalStudents(rs.getInt("total_students"));
        session.setPresentCount(rs.getInt("present_count"));
        session.setLateCount(rs.getInt("late_count"));
        session.setAbsentCount(rs.getInt("absent_count"));
        session.setCreatedAt(rs.getTimestamp("created_at"));
        session.setUpdatedAt(rs.getTimestamp("updated_at"));
        session.setActivatedAt(rs.getTimestamp("activated_at"));
        session.setEndedAt(rs.getTimestamp("ended_at"));

        session.setCourseName(rs.getString("course_name"));
        session.setCourseCode(rs.getString("course_code"));
        session.setStaffName(rs.getString("staff_name"));

        return session;
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
