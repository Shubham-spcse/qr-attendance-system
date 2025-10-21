package com.attendance.dao;

import com.attendance.models.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminDAO - Data Access Object for Admin operations
 */
public class AdminDAO {

    public boolean create(Admin admin) {
        String sql = "INSERT INTO admin (admin_code, name, email, phone, password_hash, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, admin.getAdminCode());
            pstmt.setString(2, admin.getName());
            pstmt.setString(3, admin.getEmail());
            pstmt.setString(4, admin.getPhone());
            pstmt.setString(5, admin.getPasswordHash());
            pstmt.setString(6, admin.getStatus());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    admin.setAdminId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating admin: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public Admin findById(int adminId) {
        String sql = "SELECT * FROM admin WHERE admin_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, adminId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractAdminFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding admin: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public Admin findByEmail(String email) {
        String sql = "SELECT * FROM admin WHERE email = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractAdminFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding admin by email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return null;
    }

    public List<Admin> findAll() {
        String sql = "SELECT * FROM admin ORDER BY name";

        List<Admin> admins = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                admins.add(extractAdminFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error getting all admins: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return admins;
    }

    public boolean update(Admin admin) {
        String sql = "UPDATE admin SET admin_code = ?, name = ?, email = ?, phone = ?, " +
                    "password_hash = ?, status = ? WHERE admin_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, admin.getAdminCode());
            pstmt.setString(2, admin.getName());
            pstmt.setString(3, admin.getEmail());
            pstmt.setString(4, admin.getPhone());
            pstmt.setString(5, admin.getPasswordHash());
            pstmt.setString(6, admin.getStatus());
            pstmt.setInt(7, admin.getAdminId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating admin: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    public boolean updateLastLogin(int adminId) {
        String sql = "UPDATE admin SET last_login = CURRENT_TIMESTAMP WHERE admin_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, adminId);

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

    public boolean delete(int adminId) {
        String sql = "DELETE FROM admin WHERE admin_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, adminId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting admin: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }

        return false;
    }

    private Admin extractAdminFromResultSet(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setAdminId(rs.getInt("admin_id"));
        admin.setAdminCode(rs.getString("admin_code"));
        admin.setName(rs.getString("name"));
        admin.setEmail(rs.getString("email"));
        admin.setPhone(rs.getString("phone"));
        admin.setPasswordHash(rs.getString("password_hash"));
        admin.setStatus(rs.getString("status"));
        admin.setCreatedAt(rs.getTimestamp("created_at"));
        admin.setUpdatedAt(rs.getTimestamp("updated_at"));
        admin.setLastLogin(rs.getTimestamp("last_login"));
        return admin;
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
