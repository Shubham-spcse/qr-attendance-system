package com.attendance.models;

import java.io.Serializable;

/**
 * SessionData - Helper model for session validation and attendance marking
 * Used for QR code validation and session status checks
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class SessionData implements Serializable {

    private static final long serialVersionUID = 1L;

    // Session validation fields
    private int sessionId;
    private String sessionCode;
    private int courseId;
    private String courseCode;
    private String courseName;
    private String startTime;
    private String endTime;
    private String location;
    private String status;
    private boolean isActive;
    private int totalStudents;
    private int presentCount;
    private String qrCodeChecksum;

    /**
     * Default constructor
     */
    public SessionData() {
        this.isActive = false;
    }

    /**
     * Constructor with essential fields
     * @param sessionId Session ID
     * @param sessionCode Session code
     * @param courseCode Course code
     * @param isActive Active status
     */
    public SessionData(int sessionId, String sessionCode, String courseCode, boolean isActive) {
        this.sessionId = sessionId;
        this.sessionCode = sessionCode;
        this.courseCode = courseCode;
        this.isActive = isActive;
    }

    /**
     * Full constructor for validation
     * @param sessionId Session ID
     * @param sessionCode Session code
     * @param courseCode Course code
     * @param startTime Start time
     * @param endTime End time
     * @param status Session status
     * @param isActive Active flag
     */
    public SessionData(int sessionId, String sessionCode, String courseCode,
                      String startTime, String endTime, String status, boolean isActive) {
        this(sessionId, sessionCode, courseCode, isActive);
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    // Getters and Setters

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

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
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(int presentCount) {
        this.presentCount = presentCount;
    }

    public String getQrCodeChecksum() {
        return qrCodeChecksum;
    }

    public void setQrCodeChecksum(String qrCodeChecksum) {
        this.qrCodeChecksum = qrCodeChecksum;
    }

    // Business logic methods

    /**
     * Check if session is currently active and accepting attendance
     * @return true if active and status is ACTIVE
     */
    public boolean canAcceptAttendance() {
        return isActive && "ACTIVE".equalsIgnoreCase(status);
    }

    /**
     * Check if session has expired
     * @return true if status is EXPIRED
     */
    public boolean isExpired() {
        return "EXPIRED".equalsIgnoreCase(status);
    }

    /**
     * Check if session is scheduled
     * @return true if status is SCHEDULED
     */
    public boolean isScheduled() {
        return "SCHEDULED".equalsIgnoreCase(status);
    }

    /**
     * Check if session is cancelled
     * @return true if status is CANCELLED
     */
    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }

    /**
     * Validate checksum matches expected value
     * @param providedChecksum Checksum from QR code
     * @return true if checksums match
     */
    public boolean validateChecksum(String providedChecksum) {
        if (qrCodeChecksum == null || providedChecksum == null) {
            return false;
        }
        return qrCodeChecksum.equals(providedChecksum);
    }

    /**
     * Get attendance percentage
     * @return Percentage of students marked present
     */
    public double getAttendancePercentage() {
        if (totalStudents == 0) return 0.0;
        return ((double) presentCount / totalStudents) * 100.0;
    }

    /**
     * Get time range display
     * @return Start time - End time
     */
    public String getTimeRange() {
        if (startTime != null && endTime != null) {
            return startTime + " - " + endTime;
        }
        return "N/A";
    }

    /**
     * Get session display name
     * @return Course code - Session code
     */
    public String getDisplayName() {
        if (courseCode != null && sessionCode != null) {
            return courseCode + " - " + sessionCode;
        }
        return sessionCode != null ? sessionCode : "Unknown Session";
    }

    /**
     * Validate session has all required fields
     * @return true if sessionId, sessionCode, and courseCode are set
     */
    public boolean isValid() {
        return sessionId > 0 &&
               sessionCode != null && !sessionCode.trim().isEmpty() &&
               courseCode != null && !courseCode.trim().isEmpty();
    }

    // Object methods

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SessionData that = (SessionData) obj;
        return sessionId == that.sessionId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(sessionId);
    }

    @Override
    public String toString() {
        return "SessionData{" +
                "sessionId=" + sessionId +
                ", sessionCode='" + sessionCode + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", status='" + status + '\'' +
                ", isActive=" + isActive +
                ", presentCount=" + presentCount +
                ", totalStudents=" + totalStudents +
                '}';
    }
}
