package com.attendance.dao;

import com.attendance.models.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DepartmentDAO - Data Access Object for Department operations
 * Handles all database operations for departments table
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class DepartmentDAO {

    /**
     * Create a new department
     * @param department Department object to insert
     * @return true if insert successful, false otherwise
     */
    public boolean create(Department department) {
        String sql = "INSERT INTO departments (department_code, department_name, description, " +
                    "head_of_department, status) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, department.getDepartmentCode());
            pstmt.setString(2, department.getDepartmentName());
            pstmt.setString(3, department.getDescription());
            pstmt.setString(4, department.getHeadOfDepartment());
            pstmt.setString(5, department.getStatus());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    department.setDepartmentId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating department: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    /**
     * Find department by ID
     * @param departmentId Department ID
     * @return Department object or null if not found
     */
    public Department findById(int departmentId) {
        String sql = "SELECT * FROM departments WHERE department_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, departmentId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractDepartmentFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding department by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    /**
     * Find department by department code
     * @param departmentCode Department code
     * @return Department object or null if not found
     */
    public Department findByCode(String departmentCode) {
        String sql = "SELECT * FROM departments WHERE department_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, departmentCode);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractDepartmentFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding department by code: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    /**
     * Get all departments
     * @return List of all departments
     */
    public List<Department> findAll() {
        String sql = "SELECT * FROM departments ORDER BY department_name";

        List<Department> departments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                departments.add(extractDepartmentFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error getting all departments: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return departments;
    }

    /**
     * Get all active departments
     * @return List of active departments
     */
    public List<Department> findAllActive() {
        String sql = "SELECT * FROM departments WHERE status = 'ACTIVE' ORDER BY department_name";

        List<Department> departments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                departments.add(extractDepartmentFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error getting active departments: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return departments;
    }

    /**
     * Update department
     * @param department Department object with updated values
     * @return true if update successful, false otherwise
     */
    public boolean update(Department department) {
        String sql = "UPDATE departments SET department_code = ?, department_name = ?, " +
                    "description = ?, head_of_department = ?, status = ? " +
                    "WHERE department_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, department.getDepartmentCode());
            pstmt.setString(2, department.getDepartmentName());
            pstmt.setString(3, department.getDescription());
            pstmt.setString(4, department.getHeadOfDepartment());
            pstmt.setString(5, department.getStatus());
            pstmt.setInt(6, department.getDepartmentId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating department: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    /**
     * Delete department (hard delete)
     * @param departmentId Department ID
     * @return true if delete successful, false otherwise
     */
    public boolean delete(int departmentId) {
        String sql = "DELETE FROM departments WHERE department_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, departmentId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting department: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    /**
     * Update department status
     * @param departmentId Department ID
     * @param status New status (ACTIVE/INACTIVE)
     * @return true if update successful
     */
    public boolean updateStatus(int departmentId, String status) {
        String sql = "UPDATE departments SET status = ? WHERE department_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, departmentId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating department status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    /**
     * Count total departments
     * @return Total number of departments
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM departments";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error counting departments: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return 0;
    }

    /**
     * Extract Department object from ResultSet
     * @param rs ResultSet
     * @return Department object
     * @throws SQLException if error occurs
     */
    private Department extractDepartmentFromResultSet(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setDepartmentId(rs.getInt("department_id"));
        department.setDepartmentCode(rs.getString("department_code"));
        department.setDepartmentName(rs.getString("department_name"));
        department.setDescription(rs.getString("description"));
        department.setHeadOfDepartment(rs.getString("head_of_department"));
        department.setStatus(rs.getString("status"));
        department.setCreatedAt(rs.getTimestamp("created_at"));
        department.setUpdatedAt(rs.getTimestamp("updated_at"));
        return department;
    }

    /**
     * Close ResultSet safely
     */
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
    }

    /**
     * Close PreparedStatement safely
     */
    private void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }
}
