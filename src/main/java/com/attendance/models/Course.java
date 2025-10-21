package com.attendance.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Course - Represents a course/subject in the system
 * Maps to: courses table in database
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    // Primary fields from database
    private int courseId;
    private String courseCode;
    private String courseName;
    private int departmentId;
    private int credits;
    private int semester;
    private String academicYear;
    private String description;
    private BigDecimal minAttendancePercentage;
    private String status; // ACTIVE, INACTIVE
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Joined fields (not in database)
    private String departmentName;
    private String departmentCode;

    /**
     * Default constructor
     */
    public Course() {
        this.status = "ACTIVE";
        this.minAttendancePercentage = new BigDecimal("75.00"); // Default 75%
        this.credits = 3; // Default credits
    }

    /**
     * Constructor with essential fields
     * @param courseCode Unique course code
     * @param courseName Course name
     * @param departmentId Department ID
     */
    public Course(String courseCode, String courseName, int departmentId) {
        this();
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.departmentId = departmentId;
    }

    /**
     * Full constructor
     * @param courseCode Unique course code
     * @param courseName Course name
     * @param departmentId Department ID
     * @param credits Course credits
     * @param semester Semester number
     * @param academicYear Academic year
     */
    public Course(String courseCode, String courseName, int departmentId,
                 int credits, int semester, String academicYear) {
        this(courseCode, courseName, departmentId);
        this.credits = credits;
        this.semester = semester;
        this.academicYear = academicYear;
    }

    // Getters and Setters

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode != null ? courseCode.toUpperCase() : null;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinAttendancePercentage() {
        return minAttendancePercentage;
    }

    public void setMinAttendancePercentage(BigDecimal minAttendancePercentage) {
        this.minAttendancePercentage = minAttendancePercentage;
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
     * Check if course is active
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    /**
     * Get full course identifier (code - name)
     * @return Course code and name
     */
    public String getFullName() {
        return courseCode + " - " + courseName;
    }

    /**
     * Check if minimum attendance is met
     * @param attendancePercentage Student's attendance percentage
     * @return true if meets minimum requirement
     */
    public boolean meetsMinimumAttendance(BigDecimal attendancePercentage) {
        if (attendancePercentage == null || minAttendancePercentage == null) {
            return false;
        }
        return attendancePercentage.compareTo(minAttendancePercentage) >= 0;
    }

    /**
     * Validate course data
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return courseCode != null && !courseCode.trim().isEmpty() &&
               courseName != null && !courseName.trim().isEmpty() &&
               departmentId > 0 &&
               credits > 0 &&
               semester > 0;
    }

    // Object methods

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return courseId == course.courseId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(courseId);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", semester=" + semester +
                ", credits=" + credits +
                ", status='" + status + '\'' +
                '}';
    }
    /**
     * Get course type (THEORY, PRACTICAL, etc.)
     * @return Course type
     */
    public String getCourseType() {
        return this.description; // Temporary - reuse description
    }

    /**
     * Set course type
     * @param courseType Course type
     */
    public void setCourseType(String courseType) {
        // For now, you can ignore or add new field to database
    }

}
