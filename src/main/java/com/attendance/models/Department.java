package com.attendance.models;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Department - Represents a department in the institution
 * Maps to: departments table in database
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    // Primary fields from database
    private int departmentId;
    private String departmentCode;
    private String departmentName;
    private String description;
    private String headOfDepartment;
    private String status; // ACTIVE, INACTIVE
    private Timestamp createdAt;
    private Timestamp updatedAt;

    /**
     * Default constructor
     */
    public Department() {
        this.status = "ACTIVE"; // Default status
    }

    /**
     * Constructor with essential fields
     * @param departmentCode Unique department code
     * @param departmentName Department name
     */
    public Department(String departmentCode, String departmentName) {
        this();
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
    }

    /**
     * Full constructor
     * @param departmentCode Unique department code
     * @param departmentName Department name
     * @param description Department description
     * @param headOfDepartment Head of department name
     */
    public Department(String departmentCode, String departmentName, 
                     String description, String headOfDepartment) {
        this(departmentCode, departmentName);
        this.description = description;
        this.headOfDepartment = headOfDepartment;
    }

    // Getters and Setters

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode != null ? departmentCode.toUpperCase() : null;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
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

    // Business logic methods

    /**
     * Check if department is active
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    /**
     * Activate the department
     */
    public void activate() {
        this.status = "ACTIVE";
    }

    /**
     * Deactivate the department
     */
    public void deactivate() {
        this.status = "INACTIVE";
    }

    /**
     * Validate department data
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return departmentCode != null && !departmentCode.trim().isEmpty() &&
               departmentName != null && !departmentName.trim().isEmpty();
    }

    // Object methods

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Department that = (Department) obj;
        return departmentId == that.departmentId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(departmentId);
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentCode='" + departmentCode + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", headOfDepartment='" + headOfDepartment + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
