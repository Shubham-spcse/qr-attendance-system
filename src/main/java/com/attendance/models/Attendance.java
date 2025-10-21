package com.attendance.models;

import com.attendance.enums.AttendanceStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Attendance - Represents an individual attendance record
 * Maps to: attendance table in database
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    // Primary fields from database
    private int attendanceId;
    private int sessionId;
    private int studentId;
    private AttendanceStatus status;
    private Timestamp markedAt;
    private String qrCodeUsed;
    private BigDecimal locationLatitude;
    private BigDecimal locationLongitude;
    private String ipAddress;
    private String deviceInfo;
    private String markedBy; // SYSTEM, STAFF, STUDENT
    private String remarks;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Joined fields (not in database)
    private String studentName;
    private String rollNumber;
    private String sessionCode;
    private String courseName;
    private String courseCode;

    /**
     * Default constructor
     */
    public Attendance() {
        this.status = AttendanceStatus.ABSENT;
        this.markedBy = "SYSTEM";
    }

    /**
     * Constructor with essential fields
     * @param sessionId Session ID
     * @param studentId Student ID
     * @param status Attendance status
     */
    public Attendance(int sessionId, int studentId, AttendanceStatus status) {
        this();
        this.sessionId = sessionId;
        this.studentId = studentId;
        this.status = status;
    }

    /**
     * Full constructor for marking attendance
     * @param sessionId Session ID
     * @param studentId Student ID
     * @param status Attendance status
     * @param qrCodeUsed QR code data used
     * @param markedBy Who marked (SYSTEM/STAFF/STUDENT)
     */
    public Attendance(int sessionId, int studentId, AttendanceStatus status,
                     String qrCodeUsed, String markedBy) {
        this(sessionId, studentId, status);
        this.qrCodeUsed = qrCodeUsed;
        this.markedBy = markedBy;
        this.markedAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public Timestamp getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(Timestamp markedAt) {
        this.markedAt = markedAt;
    }

    public String getQrCodeUsed() {
        return qrCodeUsed;
    }

    public void setQrCodeUsed(String qrCodeUsed) {
        this.qrCodeUsed = qrCodeUsed;
    }

    public BigDecimal getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(BigDecimal locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public BigDecimal getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(BigDecimal locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getMarkedBy() {
        return markedBy;
    }

    public void setMarkedBy(String markedBy) {
        this.markedBy = markedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
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

    // Business logic methods

    /**
     * Check if attendance is marked as present
     * @return true if status is PRESENT
     */
    public boolean isPresent() {
        return status == AttendanceStatus.PRESENT;
    }

    /**
     * Check if attendance is marked as late
     * @return true if status is LATE
     */
    public boolean isLate() {
        return status == AttendanceStatus.LATE;
    }

    /**
     * Check if attendance is marked as absent
     * @return true if status is ABSENT
     */
    public boolean isAbsent() {
        return status == AttendanceStatus.ABSENT;
    }

    /**
     * Check if student attended (present or late)
     * @return true if status is PRESENT or LATE
     */
    public boolean hasAttended() {
        return status == AttendanceStatus.PRESENT || status == AttendanceStatus.LATE;
    }

    /**
     * Mark as present
     */
    public void markPresent() {
        this.status = AttendanceStatus.PRESENT;
        this.markedAt = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Mark as late
     */
    public void markLate() {
        this.status = AttendanceStatus.LATE;
        this.markedAt = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Mark as absent
     */
    public void markAbsent() {
        this.status = AttendanceStatus.ABSENT;
        this.markedAt = null;
    }

    /**
     * Set location coordinates
     * @param latitude Latitude
     * @param longitude Longitude
     */
    public void setLocation(BigDecimal latitude, BigDecimal longitude) {
        this.locationLatitude = latitude;
        this.locationLongitude = longitude;
    }

    /**
     * Check if location data is available
     * @return true if latitude and longitude are set
     */
    public boolean hasLocationData() {
        return locationLatitude != null && locationLongitude != null;
    }

    /**
     * Get attendance weightage for calculation
     * @return Weightage from enum (1.0 for PRESENT, 0.5 for LATE, 0.0 for ABSENT)
     */
    public double getWeightage() {
        return status.getWeightage();
    }

    /**
     * Get display status with color
     * @return Status display name
     */
    public String getDisplayStatus() {
        return status.getDisplayName();
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
     * Validate attendance data
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return sessionId > 0 &&
               studentId > 0 &&
               status != null;
    }

    // Object methods

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Attendance that = (Attendance) obj;
        return attendanceId == that.attendanceId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(attendanceId);
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId=" + attendanceId +
                ", sessionId=" + sessionId +
                ", studentId=" + studentId +
                ", status=" + status +
                ", markedAt=" + markedAt +
                ", markedBy='" + markedBy + '\'' +
                '}';
    }
}
