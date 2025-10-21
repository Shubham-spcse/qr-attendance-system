package com.attendance.models;

import com.attendance.enums.UserRole;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Admin - Represents an administrative user
 * Maps to: admin table in database
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    // Primary fields from database
    private int adminId;
    private String adminCode;
    private String name;
    private String email;
    private String phone;
    private String passwordHash;
    private String status; // ACTIVE, INACTIVE
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLogin;

    /**
     * Default constructor
     */
    public Admin() {
        this.status = "ACTIVE";
    }

    /**
     * Constructor with essential fields
     * @param adminCode Unique admin code
     * @param name Admin name
     * @param email Admin email
     */
    public Admin(String adminCode, String name, String email) {
        this();
        this.adminCode = adminCode;
        this.name = name;
        this.email = email;
    }

    /**
     * Full constructor
     * @param adminCode Unique admin code
     * @param name Admin name
     * @param email Admin email
     * @param phone Phone number
     * @param passwordHash Hashed password
     */
    public Admin(String adminCode, String name, String email, 
                String phone, String passwordHash) {
        this(adminCode, name, email);
        this.phone = phone;
        this.passwordHash = passwordHash;
    }

    // Getters and Setters

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode != null ? adminCode.toUpperCase() : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.toLowerCase() : null;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    // Business logic methods

    /**
     * Check if admin is active
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    /**
     * Get user role for this admin
     * @return UserRole.ADMIN
     */
    public UserRole getUserRole() {
        return UserRole.ADMIN;
    }

    /**
     * Update last login timestamp to current time
     */
    public void updateLastLogin() {
        this.lastLogin = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Activate the admin account
     */
    public void activate() {
        this.status = "ACTIVE";
    }

    /**
     * Deactivate the admin account
     */
    public void deactivate() {
        this.status = "INACTIVE";
    }

    /**
     * Validate admin data
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return adminCode != null && !adminCode.trim().isEmpty() &&
               name != null && !name.trim().isEmpty() &&
               email != null && email.contains("@") &&
               passwordHash != null && !passwordHash.trim().isEmpty();
    }

    /**
     * Get display name for UI
     * @return Admin name with code
     */
    public String getDisplayName() {
        return name + " (" + adminCode + ")";
    }

    /**
     * Check if email is valid format
     * @return true if email contains @ and .
     */
    public boolean hasValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }

    // Object methods

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Admin admin = (Admin) obj;
        return adminId == admin.adminId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(adminId);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", adminCode='" + adminCode + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
