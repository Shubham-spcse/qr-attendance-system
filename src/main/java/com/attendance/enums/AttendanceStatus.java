package com.attendance.enums;

/**
 * AttendanceStatus - Defines the attendance marking status for students
 * PRESENT: On time attendance
 * LATE: Arrived after threshold time
 * ABSENT: Did not attend
 */
public enum AttendanceStatus {
    PRESENT("Present", "Student attended on time", "#28A745", 1.0),
    LATE("Late", "Student arrived late", "#FFC107", 0.5),
    ABSENT("Absent", "Student did not attend", "#DC3545", 0.0);

    private final String displayName;
    private final String description;
    private final String colorCode;
    private final double weightage;

    /**
     * Constructor for AttendanceStatus enum
     * @param displayName Human-readable name
     * @param description Status description
     * @param colorCode Color code for UI display (hex format)
     * @param weightage Weightage for attendance calculation (0.0 to 1.0)
     */
    AttendanceStatus(String displayName, String description, String colorCode, double weightage) {
        this.displayName = displayName;
        this.description = description;
        this.colorCode = colorCode;
        this.weightage = weightage;
    }

    /**
     * Get the display name of the status
     * @return Display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the description of this status
     * @return Status description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the color code for UI display
     * @return Hex color code (e.g., "#28A745")
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     * Get the weightage for attendance calculation
     * PRESENT = 1.0 (100%), LATE = 0.5 (50%), ABSENT = 0.0 (0%)
     * @return Weightage value between 0.0 and 1.0
     */
    public double getWeightage() {
        return weightage;
    }

    /**
     * Check if this status counts as attended
     * @return true if PRESENT or LATE
     */
    public boolean isAttended() {
        return this == PRESENT || this == LATE;
    }

    /**
     * Check if this status is considered present
     * @return true if PRESENT
     */
    public boolean isPresent() {
        return this == PRESENT;
    }

    /**
     * Check if this status is considered late
     * @return true if LATE
     */
    public boolean isLate() {
        return this == LATE;
    }

    /**
     * Check if this status is considered absent
     * @return true if ABSENT
     */
    public boolean isAbsent() {
        return this == ABSENT;
    }

    /**
     * Convert string to AttendanceStatus enum
     * @param status Status as string
     * @return AttendanceStatus enum or null if not found
     */
    public static AttendanceStatus fromString(String status) {
        if (status == null || status.trim().isEmpty()) {
            return null;
        }

        for (AttendanceStatus attendanceStatus : AttendanceStatus.values()) {
            if (attendanceStatus.name().equalsIgnoreCase(status.trim()) || 
                attendanceStatus.displayName.equalsIgnoreCase(status.trim())) {
                return attendanceStatus;
            }
        }
        return null;
    }

    /**
     * Check if a string is a valid attendance status
     * @param status Status to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String status) {
        return fromString(status) != null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
