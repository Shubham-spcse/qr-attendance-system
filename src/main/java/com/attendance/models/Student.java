package com.attendance.models;

import com.attendance.enums.UserRole;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Student - Represents a student in the system
 * Maps to: students table in database
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    // Primary fields from database
    private int studentId;
    private String rollNumber;
    private String name;
    private String email;
    private String phone;
    private int departmentId;
    private int year;
    private String section;
    private int semester;
    private Date admissionDate;
    private String passwordHash;
    private String status; // ACTIVE, INACTIVE, GRADUATED, SUSPENDED
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLogin;

    // Joined fields (not in database)
    private String departmentName;
    private String departmentCode;

    /**
     * Default constructor
     */
    public Student() {
        this.status = "ACTIVE";
    }

    /**
     * Constructor with essential fields
     * @param rollNumber Unique roll number
     * @param name Student name
     * @param email Student email
     * @param departmentId Department ID
     * @param year Year of study
     */
    public Student(String rollNumber, String name, String email, int departmentId, int year) {
        this();
        this.rollNumber = rollNumber;
        this.name = name;
        this.email = email;
        this.departmentId = departmentId;
        this.year = year;
    }

    /**
     * Full constructor
     * @param rollNumber Unique roll number
     * @param name Student name
     * @param email Student email
     * @param phone Phone number
     * @param departmentId Department ID
     * @param year Year of study
     * @param section Section
     * @param semester Current semester
     * @param passwordHash Hashed password
     */
    public Student(String rollNumber, String name, String email, String phone,
                   int departmentId, int year, String section, int semester, String passwordHash) {
        this(rollNumber, name, email, departmentId, year);
        this.phone = phone;
        this.section = section;
        this.semester = semester;
        this.passwordHash = passwordHash;
    }

    // Getters and Setters

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber != null ? rollNumber.toUpperCase() : null;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section != null ? section.toUpperCase() : null;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
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
     * Check if student is active
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    /**
     * Check if student is graduated
     * @return true if status is GRADUATED
     */
    public boolean isGraduated() {
        return "GRADUATED".equalsIgnoreCase(this.status);
    }

    /**
     * Check if student is suspended
     * @return true if status is SUSPENDED
     */
    public boolean isSuspended() {
        return "SUSPENDED".equalsIgnoreCase(this.status);
    }

    /**
     * Get user role for this student
     * @return UserRole.STUDENT
     */
    public UserRole getUserRole() {
        return UserRole.STUDENT;
    }

    /**
     * Update last login timestamp to current time
     */
    public void updateLastLogin() {
        this.lastLogin = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Activate the student account
     */
    public void activate() {
        this.status = "ACTIVE";
    }

    /**
     * Suspend the student account
     */
    public void suspend() {
        this.status = "SUSPENDED";
    }

    /**
     * Mark student as graduated
     */
    public void graduate() {
        this.status = "GRADUATED";
    }

    /**
     * Validate student data
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return rollNumber != null && !rollNumber.trim().isEmpty() &&
               name != null && !name.trim().isEmpty() &&
               email != null && email.contains("@") &&
               departmentId > 0 &&
               year > 0 && year <= 5 &&
               passwordHash != null && !passwordHash.trim().isEmpty();
    }

    /**
     * Get display name for UI
     * @return Student name with roll number
     */
    public String getDisplayName() {
        return name + " (" + rollNumber + ")";
    }

    /**
     * Get year name (First Year, Second Year, etc.)
     * @return Year as text
     */
    public String getYearName() {
        switch (year) {
            case 1: return "First Year";
            case 2: return "Second Year";
            case 3: return "Third Year";
            case 4: return "Fourth Year";
            case 5: return "Fifth Year";
            default: return "Year " + year;
        }
    }

    /**
     * Get full student identifier with section
     * @return Roll number, Year, Section
     */
    public String getFullIdentifier() {
        StringBuilder sb = new StringBuilder(rollNumber);
        if (section != null && !section.isEmpty()) {
            sb.append(" - ").append(getYearName()).append(" (Section ").append(section).append(")");
        } else {
            sb.append(" - ").append(getYearName());
        }
        return sb.toString();
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
        Student student = (Student) obj;
        return studentId == student.studentId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(studentId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", rollNumber='" + rollNumber + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", year=" + year +
                ", section='" + section + '\'' +
                ", semester=" + semester +
                ", status='" + status + '\'' +
                '}';
    }
    /**
     * Convenience method for setting password
     * Alias for setPasswordHash for backward compatibility
     * @param password Password to set
     */
    public void setPassword(String password) {
        this.passwordHash = password;
    }

    /**
     * Convenience method for getting password
     * Alias for getPasswordHash for backward compatibility
     * @return The password hash
     */
    public String getPassword() {
        return this.passwordHash;
    }

}
