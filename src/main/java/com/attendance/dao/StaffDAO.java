package com.attendance.dao;

import com.attendance.models.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StaffDAO - Data Access Object for Staff operations
 */
public class StaffDAO {

    public boolean create(Staff staff) {
        String sql = "INSERT INTO staff (staff_code, name, email, phone, department_id, " +
                    "designation, password_hash, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, staff.getStaffCode());
            pstmt.setString(2, staff.getName());
            pstmt.setString(3, staff.getEmail());
            pstmt.setString(4, staff.getPhone());
            pstmt.setInt(5, staff.getDepartmentId());
            pstmt.setString(6, staff.getDesignation());
            pstmt.setString(7, staff.getPasswordHash());
            pstmt.setString(8, staff.getStatus());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    staff.setStaffId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating staff: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public Staff findById(int staffId) {
        String sql = "SELECT s.*, d.department_name, d.department_code " +
                    "FROM staff s " +
                    "LEFT JOIN departments d ON s.department_id = d.department_id " +
                    "WHERE s.staff_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, staffId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractStaffFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding staff: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public Staff findByEmail(String email) {
        String sql = "SELECT s.*, d.department_name, d.department_code " +
                    "FROM staff s " +
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
                return extractStaffFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding staff by email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public List<Staff> findAll() {
        String sql = "SELECT s.*, d.department_name, d.department_code " +
                    "FROM staff s " +
                    "LEFT JOIN departments d ON s.department_id = d.department_id " +
                    "ORDER BY s.name";

        List<Staff> staffList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                staffList.add(extractStaffFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error getting all staff: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return staffList;
    }

    public List<Staff> findByDepartment(int departmentId) {
        String sql = "SELECT s.*, d.department_name, d.department_code " +
                    "FROM staff s " +
                    "LEFT JOIN departments d ON s.department_id = d.department_id " +
                    "WHERE s.department_id = ? AND s.status = 'ACTIVE' " +
                    "ORDER BY s.name";

        List<Staff> staffList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, departmentId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                staffList.add(extractStaffFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error getting staff by department: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return staffList;
    }

    public boolean update(Staff staff) {
        String sql = "UPDATE staff SET staff_code = ?, name = ?, email = ?, phone = ?, " +
                    "department_id = ?, designation = ?, password_hash = ?, status = ? " +
                    "WHERE staff_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, staff.getStaffCode());
            pstmt.setString(2, staff.getName());
            pstmt.setString(3, staff.getEmail());
            pstmt.setString(4, staff.getPhone());
            pstmt.setInt(5, staff.getDepartmentId());
            pstmt.setString(6, staff.getDesignation());
            pstmt.setString(7, staff.getPasswordHash());
            pstmt.setString(8, staff.getStatus());
            pstmt.setInt(9, staff.getStaffId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating staff: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean updateLastLogin(int staffId) {
        String sql = "UPDATE staff SET last_login = CURRENT_TIMESTAMP WHERE staff_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, staffId);

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

    public boolean delete(int staffId) {
        String sql = "DELETE FROM staff WHERE staff_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, staffId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting staff: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    private Staff extractStaffFromResultSet(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setStaffId(rs.getInt("staff_id"));
        staff.setStaffCode(rs.getString("staff_code"));
        staff.setName(rs.getString("name"));
        staff.setEmail(rs.getString("email"));
        staff.setPhone(rs.getString("phone"));
        staff.setDepartmentId(rs.getInt("department_id"));
        staff.setDesignation(rs.getString("designation"));
        staff.setPasswordHash(rs.getString("password_hash"));
        staff.setStatus(rs.getString("status"));
        staff.setCreatedAt(rs.getTimestamp("created_at"));
        staff.setUpdatedAt(rs.getTimestamp("updated_at"));
        staff.setLastLogin(rs.getTimestamp("last_login"));

        // Joined fields
        staff.setDepartmentName(rs.getString("department_name"));
        staff.setDepartmentCode(rs.getString("department_code"));

        return staff;
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
