package com.attendance.dao;

import com.attendance.models.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CourseDAO - Data Access Object for Course operations
 * Handles all database operations for courses table
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class CourseDAO {

    /**
     * Create a new course
     * @param course Course object to insert
     * @return true if insert successful
     */
    public boolean create(Course course) {
        String sql = "INSERT INTO courses (course_code, course_name, department_id, credits, " +
                    "semester, academic_year, description, min_attendance_percentage, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getDepartmentId());
            pstmt.setInt(4, course.getCredits());
            pstmt.setInt(5, course.getSemester());
            pstmt.setString(6, course.getAcademicYear());
            pstmt.setString(7, course.getDescription());
            pstmt.setBigDecimal(8, course.getMinAttendancePercentage());
            pstmt.setString(9, course.getStatus());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    course.setCourseId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating course: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    /**
     * Find course by ID
     * @param courseId Course ID
     * @return Course object or null
     */
    public Course findById(int courseId) {
        String sql = "SELECT c.*, d.department_name, d.department_code " +
                    "FROM courses c " +
                    "LEFT JOIN departments d ON c.department_id = d.department_id " +
                    "WHERE c.course_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCourseFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding course by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    /**
     * Find course by course code
     * @param courseCode Course code
     * @return Course object or null
     */
    public Course findByCode(String courseCode) {
        String sql = "SELECT c.*, d.department_name, d.department_code " +
                    "FROM courses c " +
                    "LEFT JOIN departments d ON c.department_id = d.department_id " +
                    "WHERE c.course_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, courseCode);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCourseFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding course by code: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    /**
     * Get all courses
     * @return List of all courses with department info
     */
    public List<Course> findAll() {
        String sql = "SELECT c.*, d.department_name, d.department_code " +
                    "FROM courses c " +
                    "LEFT JOIN departments d ON c.department_id = d.department_id " +
                    "ORDER BY c.course_code";

        List<Course> courses = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error getting all courses: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return courses;
    }

    /**
     * Get courses by department
     * @param departmentId Department ID
     * @return List of courses in the department
     */
    public List<Course> findByDepartment(int departmentId) {
        String sql = "SELECT c.*, d.department_name, d.department_code " +
                    "FROM courses c " +
                    "LEFT JOIN departments d ON c.department_id = d.department_id " +
                    "WHERE c.department_id = ? AND c.status = 'ACTIVE' " +
                    "ORDER BY c.course_code";

        List<Course> courses = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, departmentId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error getting courses by department: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return courses;
    }

    /**
     * Get courses by semester
     * @param semester Semester number
     * @return List of courses for the semester
     */
    public List<Course> findBySemester(int semester) {
        String sql = "SELECT c.*, d.department_name, d.department_code " +
                    "FROM courses c " +
                    "LEFT JOIN departments d ON c.department_id = d.department_id " +
                    "WHERE c.semester = ? AND c.status = 'ACTIVE' " +
                    "ORDER BY c.course_code";

        List<Course> courses = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, semester);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error getting courses by semester: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return courses;
    }

    /**
     * Update course
     * @param course Course object with updated values
     * @return true if update successful
     */
    public boolean update(Course course) {
        String sql = "UPDATE courses SET course_code = ?, course_name = ?, department_id = ?, " +
                    "credits = ?, semester = ?, academic_year = ?, description = ?, " +
                    "min_attendance_percentage = ?, status = ? WHERE course_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getDepartmentId());
            pstmt.setInt(4, course.getCredits());
            pstmt.setInt(5, course.getSemester());
            pstmt.setString(6, course.getAcademicYear());
            pstmt.setString(7, course.getDescription());
            pstmt.setBigDecimal(8, course.getMinAttendancePercentage());
            pstmt.setString(9, course.getStatus());
            pstmt.setInt(10, course.getCourseId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    /**
     * Delete course
     * @param courseId Course ID
     * @return true if delete successful
     */
    public boolean delete(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    /**
     * Extract Course object from ResultSet
     */
    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseId(rs.getInt("course_id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseName(rs.getString("course_name"));
        course.setDepartmentId(rs.getInt("department_id"));
        course.setCredits(rs.getInt("credits"));
        course.setSemester(rs.getInt("semester"));
        course.setAcademicYear(rs.getString("academic_year"));
        course.setDescription(rs.getString("description"));
        course.setMinAttendancePercentage(rs.getBigDecimal("min_attendance_percentage"));
        course.setStatus(rs.getString("status"));
        course.setCreatedAt(rs.getTimestamp("created_at"));
        course.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Joined fields
        course.setDepartmentName(rs.getString("department_name"));
        course.setDepartmentCode(rs.getString("department_code"));

        return course;
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
