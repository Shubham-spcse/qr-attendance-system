package com.attendance.models;

import com.attendance.enums.SessionStatus;
import com.attendance.enums.SessionType;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * AttendanceSession - Represents an attendance session for a course
 * Maps to: attendance_sessions table in database
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class AttendanceSession implements Serializable {

    private static final long serialVersionUID = 1L;

    // Primary fields from database
    private int sessionId;
    private String sessionCode;
    private int courseId;
    private int staffId;
    private Date sessionDate;
    private Time startTime;
    private Time endTime;
    private String location;
    private SessionType sessionType;
    private SessionStatus status;
    private String qrCodeData;
    private String qrCodeChecksum;
    private int totalStudents;
    private int presentCount;
    private int lateCount;
    private int absentCount;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp activatedAt;
    private Timestamp endedAt;

    // Joined fields (not in database)
    private String courseName;
    private String courseCode;
    private String staffName;
    private String departmentName;

    /**
     * Default constructor
     */
    public AttendanceSession() {
        this.status = SessionStatus.SCHEDULED;
        this.sessionType = SessionType.LECTURE;
        this.totalStudents = 0;
        this.presentCount = 0;
        this.lateCount = 0;
        this.absentCount = 0;
    }

    /**
     * Constructor with essential fields
     * @param sessionCode Unique session code
     * @param courseId Course ID
     * @param staffId Staff ID
     * @param sessionDate Session date
     */
    public AttendanceSession(String sessionCode, int courseId, int staffId, Date sessionDate) {
        this();
        this.sessionCode = sessionCode;
        this.courseId = courseId;
        this.staffId = staffId;
        this.sessionDate = sessionDate;
    }

    /**
     * Full constructor
     * @param sessionCode Unique session code
     * @param courseId Course ID
     * @param staffId Staff ID
     * @param sessionDate Session date
     * @param startTime Start time
     * @param endTime End time
     * @param location Location/room
     * @param sessionType Type of session
     */
    public AttendanceSession(String sessionCode, int courseId, int staffId, Date sessionDate,
                            Time startTime, Time endTime, String location, SessionType sessionType) {
        this(sessionCode, courseId, staffId, sessionDate);
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.sessionType = sessionType;
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
        this.sessionCode = sessionCode != null ? sessionCode.toUpperCase() : null;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public String getQrCodeChecksum() {
        return qrCodeChecksum;
    }

    public void setQrCodeChecksum(String qrCodeChecksum) {
        this.qrCodeChecksum = qrCodeChecksum;
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

    public int getLateCount() {
        return lateCount;
    }

    public void setLateCount(int lateCount) {
        this.lateCount = lateCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(int absentCount) {
        this.absentCount = absentCount;
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

    public Timestamp getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(Timestamp activatedAt) {
        this.activatedAt = activatedAt;
    }

    public Timestamp getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Timestamp endedAt) {
        this.endedAt = endedAt;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    // Business logic methods

    /**
     * Check if session is currently active
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return status == SessionStatus.ACTIVE;
    }

    /**
     * Check if session is scheduled
     * @return true if status is SCHEDULED
     */
    public boolean isScheduled() {
        return status == SessionStatus.SCHEDULED;
    }

    /**
     * Check if session has expired
     * @return true if status is EXPIRED
     */
    public boolean isExpired() {
        return status == SessionStatus.EXPIRED;
    }

    /**
     * Check if session is cancelled
     * @return true if status is CANCELLED
     */
    public boolean isCancelled() {
        return status == SessionStatus.CANCELLED;
    }

    /**
     * Activate the session and set activation time
     */
    public void activate() {
        this.status = SessionStatus.ACTIVE;
        this.activatedAt = new Timestamp(System.currentTimeMillis());
    }

    /**
     * End the session and set end time
     */
    public void end() {
        this.status = SessionStatus.EXPIRED;
        this.endedAt = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Cancel the session
     */
    public void cancel() {
        this.status = SessionStatus.CANCELLED;
        this.endedAt = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Calculate attendance percentage
     * @return Percentage of students present (including late)
     */
    public double getAttendancePercentage() {
        if (totalStudents == 0) return 0.0;
        return ((double)(presentCount + lateCount) / totalStudents) * 100.0;
    }

    /**
     * Get full session display name
     * @return Course code - Session type - Date
     */
    public String getDisplayName() {
        if (courseCode != null && sessionType != null && sessionDate != null) {
            return courseCode + " - " + sessionType.getDisplayName() + " - " + sessionDate;
        }
        return sessionCode;
    }

    /**
     * Get session time range
     * @return Start time - End time
     */
    public String getTimeRange() {
        if (startTime != null && endTime != null) {
            return startTime + " - " + endTime;
        }
        return "N/A";
    }

    /**
     * Update attendance counts
     * @param present Present count
     * @param late Late count
     * @param absent Absent count
     */
    public void updateCounts(int present, int late, int absent) {
        this.presentCount = present;
        this.lateCount = late;
        this.absentCount = absent;
    }

    /**
     * Validate session data
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return sessionCode != null && !sessionCode.trim().isEmpty() &&
               courseId > 0 &&
               staffId > 0 &&
               sessionDate != null &&
               startTime != null &&
               endTime != null;
    }

    // Object methods

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AttendanceSession that = (AttendanceSession) obj;
        return sessionId == that.sessionId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(sessionId);
    }

    @Override
    public String toString() {
        return "AttendanceSession{" +
                "sessionId=" + sessionId +
                ", sessionCode='" + sessionCode + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", sessionDate=" + sessionDate +
                ", sessionType=" + sessionType +
                ", status=" + status +
                ", presentCount=" + presentCount +
                ", totalStudents=" + totalStudents +
                '}';
    }
}
