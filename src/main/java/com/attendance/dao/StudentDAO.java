package com.attendance.dao;

import com.attendance.models.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentDAO - Data Access Object for Student operations
 * Handles all database operations for students table
 */
public class StudentDAO {

    public boolean create(Student student) {
        String sql = "INSERT INTO students (roll_number, name, email, phone, department_id, " +
                    "year, section, semester, admission_date, password_hash, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, student.getRollNumber());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getPhone());
            pstmt.setInt(5, student.getDepartmentId());
            pstmt.setInt(6, student.getYear());
            pstmt.setString(7, student.getSection());
            pstmt.setInt(8, student.getSemester());
            pstmt.setDate(9, student.getAdmissionDate());
            pstmt.setString(10, student.getPasswordHash());
            pstmt.setString(11, student.getStatus());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    student.setStudentId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating student: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public Student findById(int studentId) {
        String sql = "SELECT s.*, d.department_name, d.department_code " +
                    "FROM students s " +
                    "LEFT JOIN departments d ON s.department_id = d.department_id " +
                    "WHERE s.student_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding student: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public Student findByEmail(String email) {
        String sql = "SELECT s.*, d.department_name, d.department_code " +
                    "FROM students s " +
                    "LEFT JOIN departments d ON s.department_id = d.department_id " +
                    "WHERE s.email = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding student by email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public Student findByRollNumber(String rollNumber) {
        String sql = "SELECT s.*, d.department_name, d.department_code " +
                    "FROM students s " +
                    "LEFT JOIN departments d ON s.department_id = d.department_id " +
                    "WHERE s.roll_number = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, rollNumber);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding student by roll number: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public List<Student> findAll() {
        String sql = "SELECT s.*, d.department_name, d.department_code " +
                    "FROM students s " +
                    "LEFT JOIN departments d ON s.department_id = d.department_id " +
                    "ORDER BY s.roll_number";

        return executeQuery(sql);
    }

    public List<Student> findByDepartment(int departmentId) {
        String sql = "SELECT s.*, d.department_name, d.department_code " +
                    "FROM students s " +
                    "LEFT JOIN departments d ON s.department_id = d.department_id " +
                    "WHERE s.department_id = ? AND s.status = 'ACTIVE' " +
                    "ORDER BY s.roll_number";

        List<Student> students = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, departmentId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding students by department: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return students;
    }

    public List<Student> findByYearAndSection(int year, String section) {
        String sql = "SELECT s.*, d.department_name, d.department_code " +
                    "FROM students s " +
                    "LEFT JOIN departments d ON s.department_id = d.department_id " +
                    "WHERE s.year = ? AND s.section = ? AND s.status = 'ACTIVE' " +
                    "ORDER BY s.roll_number";

        List<Student> students = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, year);
            pstmt.setString(2, section);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding students by year and section: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return students;
    }

    public boolean update(Student student) {
        String sql = "UPDATE students SET roll_number = ?, name = ?, email = ?, phone = ?, " +
                    "department_id = ?, year = ?, section = ?, semester = ?, " +
                    "admission_date = ?, password_hash = ?, status = ? WHERE student_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, student.getRollNumber());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getPhone());
            pstmt.setInt(5, student.getDepartmentId());
            pstmt.setInt(6, student.getYear());
            pstmt.setString(7, student.getSection());
            pstmt.setInt(8, student.getSemester());
            pstmt.setDate(9, student.getAdmissionDate());
            pstmt.setString(10, student.getPasswordHash());
            pstmt.setString(11, student.getStatus());
            pstmt.setInt(12, student.getStudentId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean updateLastLogin(int studentId) {
        String sql = "UPDATE students SET last_login = CURRENT_TIMESTAMP WHERE student_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean delete(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    private List<Student> executeQuery(String sql) {
        List<Student> students = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return students;
    }

    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setRollNumber(rs.getString("roll_number"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        student.setDepartmentId(rs.getInt("department_id"));
        student.setYear(rs.getInt("year"));
        student.setSection(rs.getString("section"));
        student.setSemester(rs.getInt("semester"));
        student.setAdmissionDate(rs.getDate("admission_date"));
        student.setPasswordHash(rs.getString("password_hash"));
        student.setStatus(rs.getString("status"));
        student.setCreatedAt(rs.getTimestamp("created_at"));
        student.setUpdatedAt(rs.getTimestamp("updated_at"));
        student.setLastLogin(rs.getTimestamp("last_login"));

        student.setDepartmentName(rs.getString("department_name"));
        student.setDepartmentCode(rs.getString("department_code"));

        return student;
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
