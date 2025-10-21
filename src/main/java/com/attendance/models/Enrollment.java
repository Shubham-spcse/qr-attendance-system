package com.attendance.models;

import java.io.Serializable;
import java.sql.Date;

/**
 * Enrollment - Represents a student's enrollment in a course
 * Maps to: enrollments table in database
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class Enrollment implements Serializable {

    private static final long serialVersionUID = 1L;

    // Primary fields from database
    private int enrollmentId;
    private int studentId;
    private int courseId;
    private String academicYear;
    private int semester;
    private Date enrollmentDate;
    private String status; // ACTIVE, DROPPED, COMPLETED

    // Joined fields (not in database)
    private String studentName;
    private String rollNumber;
    private String courseName;
    private String courseCode;
    private String departmentName;

    /**
     * Default constructor
     */
    public Enrollment() {
        this.status = "ACTIVE";
        this.enrollmentDate = new Date(System.currentTimeMillis());
    }

    /**
     * Constructor with essential fields
     * @param studentId Student ID
     * @param courseId Course ID
     * @param academicYear Academic year (e.g., "2025-26")
     * @param semester Semester number
     */
    public Enrollment(int studentId, int courseId, String academicYear, int semester) {
        this();
        this.studentId = studentId;
        this.courseId = courseId;
        this.academicYear = academicYear;
        this.semester = semester;
    }

    /**
     * Full constructor
     * @param studentId Student ID
     * @param courseId Course ID
     * @param academicYear Academic year
     * @param semester Semester number
     * @param enrollmentDate Enrollment date
     */
    public Enrollment(int studentId, int courseId, String academicYear, 
                     int semester, Date enrollmentDate) {
        this(studentId, courseId, academicYear, semester);
        this.enrollmentDate = enrollmentDate;
    }

    // Getters and Setters

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    // Business logic methods

    /**
     * Check if enrollment is active
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    /**
     * Check if enrollment is dropped
     * @return true if status is DROPPED
     */
    public boolean isDropped() {
        return "DROPPED".equalsIgnoreCase(this.status);
    }

    /**
     * Check if enrollment is completed
     * @return true if status is COMPLETED
     */
    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(this.status);
    }

    /**
     * Activate the enrollment
     */
    public void activate() {
        this.status = "ACTIVE";
    }

    /**
     * Drop the enrollment
     */
    public void drop() {
        this.status = "DROPPED";
    }

    /**
     * Complete the enrollment
     */
    public void complete() {
        this.status = "COMPLETED";
    }

    /**
     * Validate enrollment data
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return studentId > 0 &&
               courseId > 0 &&
               academicYear != null && !academicYear.trim().isEmpty() &&
               semester > 0 && semester <= 10;
    }

    /**
     * Get full course display name
     * @return Course code - Course name
     */
    public String getFullCourseName() {
        if (courseCode != null && courseName != null) {
            return courseCode + " - " + courseName;
        }
        return courseName != null ? courseName : "N/A";
    }

    /**
     * Get student display info
     * @return Student name (Roll Number)
     */
    public String getStudentDisplay() {
        if (studentName != null && rollNumber != null) {
            return studentName + " (" + rollNumber + ")";
        }
        return studentName != null ? studentName : "N/A";
    }

    /**
     * Get enrollment display info
     * @return Academic year, Semester info
     */
    public String getEnrollmentInfo() {
        return "AY: " + academicYear + ", Sem: " + semester;
    }

    // Object methods

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Enrollment that = (Enrollment) obj;
        return enrollmentId == that.enrollmentId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(enrollmentId);
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", academicYear='" + academicYear + '\'' +
                ", semester=" + semester +
                ", status='" + status + '\'' +
                '}';
    }
}
