package com.attendance.dao;

import com.attendance.models.Attendance;
import com.attendance.enums.AttendanceStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AttendanceDAO - MOST CRITICAL DAO for attendance marking
 */
public class AttendanceDAO {

    public boolean create(Attendance attendance) {
        String sql = "INSERT INTO attendance (session_id, student_id, status, marked_at, " +
                    "qr_code_used, location_latitude, location_longitude, ip_address, " +
                    "device_info, marked_by, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, attendance.getSessionId());
            pstmt.setInt(2, attendance.getStudentId());
            pstmt.setString(3, attendance.getStatus().name());
            pstmt.setTimestamp(4, attendance.getMarkedAt());
            pstmt.setString(5, attendance.getQrCodeUsed());
            pstmt.setBigDecimal(6, attendance.getLocationLatitude());
            pstmt.setBigDecimal(7, attendance.getLocationLongitude());
            pstmt.setString(8, attendance.getIpAddress());
            pstmt.setString(9, attendance.getDeviceInfo());
            pstmt.setString(10, attendance.getMarkedBy());
            pstmt.setString(11, attendance.getRemarks());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    attendance.setAttendanceId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating attendance: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }
    /**
     * Alias for create - used by MarkAttendanceServlet
     */
    public boolean insert(Attendance attendance) {
        return create(attendance);
    }




    public Attendance findById(int attendanceId) {
        String sql = "SELECT a.*, s.name as student_name, s.roll_number, " +
                    "ats.session_code, c.course_name, c.course_code " +
                    "FROM attendance a " +
                    "INNER JOIN students s ON a.student_id = s.student_id " +
                    "INNER JOIN attendance_sessions ats ON a.session_id = ats.session_id " +
                    "INNER JOIN courses c ON ats.course_id = c.course_id " +
                    "WHERE a.attendance_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attendanceId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractAttendanceFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding attendance: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public Attendance findBySessionAndStudent(int sessionId, int studentId) {
        String sql = "SELECT a.*, s.name as student_name, s.roll_number, " +
                    "ats.session_code, c.course_name, c.course_code " +
                    "FROM attendance a " +
                    "INNER JOIN students s ON a.student_id = s.student_id " +
                    "INNER JOIN attendance_sessions ats ON a.session_id = ats.session_id " +
                    "INNER JOIN courses c ON ats.course_id = c.course_id " +
                    "WHERE a.session_id = ? AND a.student_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sessionId);
            pstmt.setInt(2, studentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractAttendanceFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding attendance by session and student: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public List<Attendance> findBySession(int sessionId) {
        String sql = "SELECT a.*, s.name as student_name, s.roll_number, " +
                    "ats.session_code, c.course_name, c.course_code " +
                    "FROM attendance a " +
                    "INNER JOIN students s ON a.student_id = s.student_id " +
                    "INNER JOIN attendance_sessions ats ON a.session_id = ats.session_id " +
                    "INNER JOIN courses c ON ats.course_id = c.course_id " +
                    "WHERE a.session_id = ? " +
                    "ORDER BY s.roll_number";

        return executeAttendanceQuery(sql, sessionId);
    }

    public List<Attendance> findByStudent(int studentId) {
        String sql = "SELECT a.*, s.name as student_name, s.roll_number, " +
                    "ats.session_code, c.course_name, c.course_code " +
                    "FROM attendance a " +
                    "INNER JOIN students s ON a.student_id = s.student_id " +
                    "INNER JOIN attendance_sessions ats ON a.session_id = ats.session_id " +
                    "INNER JOIN courses c ON ats.course_id = c.course_id " +
                    "WHERE a.student_id = ? " +
                    "ORDER BY ats.session_date DESC";

        return executeAttendanceQuery(sql, studentId);
    }

    public List<Attendance> findByStudentAndCourse(int studentId, int courseId) {
        String sql = "SELECT a.*, s.name as student_name, s.roll_number, " +
                    "ats.session_code, c.course_name, c.course_code " +
                    "FROM attendance a " +
                    "INNER JOIN students s ON a.student_id = s.student_id " +
                    "INNER JOIN attendance_sessions ats ON a.session_id = ats.session_id " +
                    "INNER JOIN courses c ON ats.course_id = c.course_id " +
                    "WHERE a.student_id = ? AND ats.course_id = ? " +
                    "ORDER BY ats.session_date DESC";

        List<Attendance> attendances = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                attendances.add(extractAttendanceFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding attendance by student and course: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return attendances;
    }

    public boolean hasMarkedAttendance(int sessionId, int studentId) {
        String sql = "SELECT COUNT(*) FROM attendance " +
                    "WHERE session_id = ? AND student_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sessionId);
            pstmt.setInt(2, studentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking attendance: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean update(Attendance attendance) {
        String sql = "UPDATE attendance SET session_id = ?, student_id = ?, status = ?, " +
                    "marked_at = ?, qr_code_used = ?, location_latitude = ?, " +
                    "location_longitude = ?, ip_address = ?, device_info = ?, " +
                    "marked_by = ?, remarks = ? WHERE attendance_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, attendance.getSessionId());
            pstmt.setInt(2, attendance.getStudentId());
            pstmt.setString(3, attendance.getStatus().name());
            pstmt.setTimestamp(4, attendance.getMarkedAt());
            pstmt.setString(5, attendance.getQrCodeUsed());
            pstmt.setBigDecimal(6, attendance.getLocationLatitude());
            pstmt.setBigDecimal(7, attendance.getLocationLongitude());
            pstmt.setString(8, attendance.getIpAddress());
            pstmt.setString(9, attendance.getDeviceInfo());
            pstmt.setString(10, attendance.getMarkedBy());
            pstmt.setString(11, attendance.getRemarks());
            pstmt.setInt(12, attendance.getAttendanceId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating attendance: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean updateStatus(int attendanceId, AttendanceStatus status) {
        String sql = "UPDATE attendance SET status = ?, marked_at = CURRENT_TIMESTAMP " +
                    "WHERE attendance_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.name());
            pstmt.setInt(2, attendanceId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating attendance status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean delete(int attendanceId) {
        String sql = "DELETE FROM attendance WHERE attendance_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attendanceId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting attendance: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    private List<Attendance> executeAttendanceQuery(String sql, int id) {
        List<Attendance> attendances = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                attendances.add(extractAttendanceFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error executing attendance query: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return attendances;
    }

    private Attendance extractAttendanceFromResultSet(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(rs.getInt("attendance_id"));
        attendance.setSessionId(rs.getInt("session_id"));
        attendance.setStudentId(rs.getInt("student_id"));
        attendance.setStatus(AttendanceStatus.valueOf(rs.getString("status")));
        attendance.setMarkedAt(rs.getTimestamp("marked_at"));
        attendance.setQrCodeUsed(rs.getString("qr_code_used"));
        attendance.setLocationLatitude(rs.getBigDecimal("location_latitude"));
        attendance.setLocationLongitude(rs.getBigDecimal("location_longitude"));
        attendance.setIpAddress(rs.getString("ip_address"));
        attendance.setDeviceInfo(rs.getString("device_info"));
        attendance.setMarkedBy(rs.getString("marked_by"));
        attendance.setRemarks(rs.getString("remarks"));
        attendance.setCreatedAt(rs.getTimestamp("created_at"));
        attendance.setUpdatedAt(rs.getTimestamp("updated_at"));

        attendance.setStudentName(rs.getString("student_name"));
        attendance.setRollNumber(rs.getString("roll_number"));
        attendance.setSessionCode(rs.getString("session_code"));
        attendance.setCourseName(rs.getString("course_name"));
        attendance.setCourseCode(rs.getString("course_code"));

        return attendance;
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
