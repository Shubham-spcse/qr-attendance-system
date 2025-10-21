package com.attendance.models;

import com.attendance.enums.UserRole;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Staff - Represents a staff/faculty member
 * Maps to: staff table in database
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    // Primary fields from database
    private int staffId;
    private String staffCode;
    private String name;
    private String email;
    private String phone;
    private int departmentId;
    private String designation;
    private String passwordHash;
    private String status; // ACTIVE, INACTIVE
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLogin;

    // Joined fields (not in database)
    private String departmentName;
    private String departmentCode;

    /**
     * Default constructor
     */
    public Staff() {
        this.status = "ACTIVE";
    }

    /**
     * Constructor with essential fields
     * @param staffCode Unique staff code
     * @param name Staff name
     * @param email Staff email
     * @param departmentId Department ID
     */
    public Staff(String staffCode, String name, String email, int departmentId) {
        this();
        this.staffCode = staffCode;
        this.name = name;
        this.email = email;
        this.departmentId = departmentId;
    }

    /**
     * Full constructor
     * @param staffCode Unique staff code
     * @param name Staff name
     * @param email Staff email
     * @param phone Phone number
     * @param departmentId Department ID
     * @param designation Staff designation
     * @param passwordHash Hashed password
     */
    public Staff(String staffCode, String name, String email, String phone,
                int departmentId, String designation, String passwordHash) {
        this(staffCode, name, email, departmentId);
        this.phone = phone;
        this.designation = designation;
        this.passwordHash = passwordHash;
    }

    // Getters and Setters

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode != null ? staffCode.toUpperCase() : null;
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

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    // Business logic methods

    /**
     * Check if staff is active
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    /**
     * Get user role for this staff
     * @return UserRole.STAFF
     */
    public UserRole getUserRole() {
        return UserRole.STAFF;
    }

    /**
     * Update last login timestamp to current time
     */
    public void updateLastLogin() {
        this.lastLogin = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Activate the staff account
     */
    public void activate() {
        this.status = "ACTIVE";
    }

    /**
     * Deactivate the staff account
     */
    public void deactivate() {
        this.status = "INACTIVE";
    }

    /**
     * Validate staff data
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return staffCode != null && !staffCode.trim().isEmpty() &&
               name != null && !name.trim().isEmpty() &&
               email != null && email.contains("@") &&
               departmentId > 0 &&
               passwordHash != null && !passwordHash.trim().isEmpty();
    }

    /**
     * Get display name for UI
     * @return Staff name with designation
     */
    public String getDisplayName() {
        if (designation != null && !designation.isEmpty()) {
            return designation + " " + name + " (" + staffCode + ")";
        }
        return name + " (" + staffCode + ")";
    }

    /**
     * Get full department info
     * @return Department code - Department name
     */
    public String getFullDepartmentName() {
        if (departmentCode != null && departmentName != null) {
            return departmentCode + " - " + departmentName;
        }
        return departmentName != null ? departmentName : "N/A";
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
        Staff staff = (Staff) obj;
        return staffId == staff.staffId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(staffId);
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", staffCode='" + staffCode + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", designation='" + designation + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    /**
     * Get employee ID (alias for staff code)
     * @return Employee ID
     */
    public String getEmployeeId() {
        return this.staffCode;
    }

    /**
     * Set employee ID (alias for staff code)
     * @param employeeId Employee ID to set
     */
    public void setEmployeeId(String employeeId) {
        this.staffCode = employeeId != null ? employeeId.toUpperCase() : null;
    }

    /**
     * Get qualification
     * @return Staff qualification
     */
    public String getQualification() {
        return this.designation; // Temporary - reuse designation field
    }

    /**
     * Set qualification
     * @param qualification Staff qualification
     */
    public void setQualification(String qualification) {
        // Store in designation for now, or add new field if needed
    }

    /**
     * Get office location
     * @return Office location
     */
    public String getOfficeLocation() {
        return ""; // Return empty or add field to database
    }

    /**
     * Set office location
     * @param officeLocation Office location
     */
    public void setOfficeLocation(String officeLocation) {
        // Add field to database if needed
    }

    /**
     * Convenience method for password (alias for passwordHash)
     */
    public void setPassword(String password) {
        this.passwordHash = password;
    }

    /**
     * Get password (alias for passwordHash)
     */
    public String getPassword() {
        return this.passwordHash;
    }

}
