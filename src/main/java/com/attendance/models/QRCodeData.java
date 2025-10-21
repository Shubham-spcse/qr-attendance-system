package com.attendance.models;

import java.io.Serializable;

/**
 * QRCodeData - Represents the encrypted data contained in a QR code
 * Used for QR code generation and validation
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class QRCodeData implements Serializable {

    private static final long serialVersionUID = 1L;

    // QR Code payload fields
    private String sessionId;
    private String courseCode;
    private String staffId;
    private String timestamp;
    private String location;
    private String validUntil;
    private String checksum;

    /**
     * Default constructor
     */
    public QRCodeData() {
    }

    /**
     * Constructor with essential fields
     * @param sessionId Session ID
     * @param courseCode Course code
     * @param validUntil Expiry timestamp
     * @param checksum Security checksum
     */
    public QRCodeData(String sessionId, String courseCode, String validUntil, String checksum) {
        this.sessionId = sessionId;
        this.courseCode = courseCode;
        this.validUntil = validUntil;
        this.checksum = checksum;
        this.timestamp = String.valueOf(System.currentTimeMillis());
    }

    /**
     * Full constructor
     * @param sessionId Session ID
     * @param courseCode Course code
     * @param staffId Staff ID
     * @param timestamp Generation timestamp
     * @param location Session location
     * @param validUntil Expiry timestamp
     * @param checksum Security checksum
     */
    public QRCodeData(String sessionId, String courseCode, String staffId, 
                     String timestamp, String location, String validUntil, String checksum) {
        this.sessionId = sessionId;
        this.courseCode = courseCode;
        this.staffId = staffId;
        this.timestamp = timestamp;
        this.location = location;
        this.validUntil = validUntil;
        this.checksum = checksum;
    }

    // Getters and Setters

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    // Business logic methods

    /**
     * Check if QR code is still valid based on validUntil timestamp
     * @return true if current time is before validUntil
     */
    public boolean isValid() {
        if (validUntil == null || validUntil.trim().isEmpty()) {
            return false;
        }

        try {
            long validUntilTime = Long.parseLong(validUntil);
            long currentTime = System.currentTimeMillis();
            return currentTime < validUntilTime;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if QR code has expired
     * @return true if current time is after validUntil
     */
    public boolean isExpired() {
        return !isValid();
    }

    /**
     * Get remaining validity time in seconds
     * @return Seconds until expiry, or 0 if already expired
     */
    public long getRemainingValiditySeconds() {
        if (validUntil == null || validUntil.trim().isEmpty()) {
            return 0;
        }

        try {
            long validUntilTime = Long.parseLong(validUntil);
            long currentTime = System.currentTimeMillis();
            long remainingMillis = validUntilTime - currentTime;
            return remainingMillis > 0 ? remainingMillis / 1000 : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Validate checksum matches expected value
     * @param expectedChecksum Expected checksum value
     * @return true if checksums match
     */
    public boolean validateChecksum(String expectedChecksum) {
        if (checksum == null || expectedChecksum == null) {
            return false;
        }
        return checksum.equals(expectedChecksum);
    }

    /**
     * Check if all required fields are present
     * @return true if sessionId, courseCode, validUntil, and checksum are set
     */
    public boolean hasAllRequiredFields() {
        return sessionId != null && !sessionId.trim().isEmpty() &&
               courseCode != null && !courseCode.trim().isEmpty() &&
               validUntil != null && !validUntil.trim().isEmpty() &&
               checksum != null && !checksum.trim().isEmpty();
    }

    /**
     * Convert to JSON-like string for QR code encoding
     * @return JSON string representation
     */
    public String toJsonString() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"sessionId\":\"").append(sessionId).append("\",");
        json.append("\"courseCode\":\"").append(courseCode).append("\",");

        if (staffId != null) {
            json.append("\"staffId\":\"").append(staffId).append("\",");
        }

        json.append("\"timestamp\":\"").append(timestamp).append("\",");

        if (location != null) {
            json.append("\"location\":\"").append(location).append("\",");
        }

        json.append("\"validUntil\":\"").append(validUntil).append("\",");
        json.append("\"checksum\":\"").append(checksum).append("\"");
        json.append("}");

        return json.toString();
    }

    /**
     * Get compact identifier for logging
     * @return Session-Course-Checksum format
     */
    public String getIdentifier() {
        return sessionId + "-" + courseCode + "-" + 
               (checksum != null && checksum.length() > 8 ? checksum.substring(0, 8) : checksum);
    }

    // Object methods

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        QRCodeData that = (QRCodeData) obj;
        return sessionId != null && sessionId.equals(that.sessionId) &&
               checksum != null && checksum.equals(that.checksum);
    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (checksum != null ? checksum.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QRCodeData{" +
                "sessionId='" + sessionId + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", location='" + location + '\'' +
                ", validUntil='" + validUntil + '\'' +
                ", checksumPreview='" + (checksum != null && checksum.length() > 8 ? 
                    checksum.substring(0, 8) + "..." : checksum) + '\'' +
                '}';
    }
}
