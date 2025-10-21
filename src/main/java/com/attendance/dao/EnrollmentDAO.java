package com.attendance.dao;

import com.attendance.models.Enrollment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EnrollmentDAO - Data Access Object for Enrollment operations
 */
public class EnrollmentDAO {

    public boolean create(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, course_id, academic_year, semester, " +
                    "enrollment_date, status) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setString(3, enrollment.getAcademicYear());
            pstmt.setInt(4, enrollment.getSemester());
            pstmt.setDate(5, enrollment.getEnrollmentDate());
            pstmt.setString(6, enrollment.getStatus());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    enrollment.setEnrollmentId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating enrollment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public Enrollment findById(int enrollmentId) {
        String sql = "SELECT e.*, s.name as student_name, s.roll_number, " +
                    "c.course_name, c.course_code " +
                    "FROM enrollments e " +
                    "INNER JOIN students s ON e.student_id = s.student_id " +
                    "INNER JOIN courses c ON e.course_id = c.course_id " +
                    "WHERE e.enrollment_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, enrollmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractEnrollmentFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding enrollment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public List<Enrollment> findByStudent(int studentId) {
        String sql = "SELECT e.*, s.name as student_name, s.roll_number, " +
                    "c.course_name, c.course_code " +
                    "FROM enrollments e " +
                    "INNER JOIN students s ON e.student_id = s.student_id " +
                    "INNER JOIN courses c ON e.course_id = c.course_id " +
                    "WHERE e.student_id = ? AND e.status = 'ACTIVE' " +
                    "ORDER BY c.course_code";

        List<Enrollment> enrollments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                enrollments.add(extractEnrollmentFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding enrollments by student: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return enrollments;
    }

    public List<Enrollment> findByCourse(int courseId) {
        String sql = "SELECT e.*, s.name as student_name, s.roll_number, " +
                    "c.course_name, c.course_code " +
                    "FROM enrollments e " +
                    "INNER JOIN students s ON e.student_id = s.student_id " +
                    "INNER JOIN courses c ON e.course_id = c.course_id " +
                    "WHERE e.course_id = ? AND e.status = 'ACTIVE' " +
                    "ORDER BY s.roll_number";

        List<Enrollment> enrollments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                enrollments.add(extractEnrollmentFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding enrollments by course: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return enrollments;
    }

    public boolean isEnrolled(int studentId, int courseId) {
        String sql = "SELECT COUNT(*) FROM enrollments " +
                    "WHERE student_id = ? AND course_id = ? AND status = 'ACTIVE'";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking enrollment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    /**
     * Alias for isEnrolled - used by MarkAttendanceServlet
     */
    public boolean isStudentEnrolled(int studentId, int courseId) {
        return isEnrolled(studentId, courseId);
    }


    public boolean update(Enrollment enrollment) {
        String sql = "UPDATE enrollments SET student_id = ?, course_id = ?, " +
                    "academic_year = ?, semester = ?, status = ? WHERE enrollment_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setString(3, enrollment.getAcademicYear());
            pstmt.setInt(4, enrollment.getSemester());
            pstmt.setString(5, enrollment.getStatus());
            pstmt.setInt(6, enrollment.getEnrollmentId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating enrollment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean delete(int enrollmentId) {
        String sql = "DELETE FROM enrollments WHERE enrollment_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, enrollmentId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting enrollment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public int countByCourse(int courseId) {
        String sql = "SELECT COUNT(*) FROM enrollments " +
                    "WHERE course_id = ? AND status = 'ACTIVE'";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error counting enrollments: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return 0;
    }

    private Enrollment extractEnrollmentFromResultSet(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
        enrollment.setStudentId(rs.getInt("student_id"));
        enrollment.setCourseId(rs.getInt("course_id"));
        enrollment.setAcademicYear(rs.getString("academic_year"));
        enrollment.setSemester(rs.getInt("semester"));
        enrollment.setEnrollmentDate(rs.getDate("enrollment_date"));
        enrollment.setStatus(rs.getString("status"));

        enrollment.setStudentName(rs.getString("student_name"));
        enrollment.setRollNumber(rs.getString("roll_number"));
        enrollment.setCourseName(rs.getString("course_name"));
        enrollment.setCourseCode(rs.getString("course_code"));

        return enrollment;
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
