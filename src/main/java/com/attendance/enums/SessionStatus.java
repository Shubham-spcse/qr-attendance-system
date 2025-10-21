package com.attendance.enums;

/**
 * SessionStatus - Defines the lifecycle status of an attendance session
 * Follows the workflow: SCHEDULED → ACTIVE → EXPIRED/CANCELLED
 */
public enum SessionStatus {
    SCHEDULED("Scheduled", "Session is scheduled but not yet active", "#FFA500"),
    ACTIVE("Active", "Session is currently active and accepting attendance", "#28A745"),
    EXPIRED("Expired", "Session has ended and is no longer accepting attendance", "#6C757D"),
    CANCELLED("Cancelled", "Session was cancelled and did not take place", "#DC3545");

    private final String displayName;
    private final String description;
    private final String colorCode;

    /**
     * Constructor for SessionStatus enum
     * @param displayName Human-readable name
     * @param description Status description
     * @param colorCode Color code for UI display (hex format)
     */
    SessionStatus(String displayName, String description, String colorCode) {
        this.displayName = displayName;
        this.description = description;
        this.colorCode = colorCode;
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
     * Check if session is currently active
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return this == ACTIVE;
    }

    /**
     * Check if session can be modified
     * @return true if status is SCHEDULED or ACTIVE
     */
    public boolean isModifiable() {
        return this == SCHEDULED || this == ACTIVE;
    }

    /**
     * Check if session is closed (expired or cancelled)
     * @return true if status is EXPIRED or CANCELLED
     */
    public boolean isClosed() {
        return this == EXPIRED || this == CANCELLED;
    }

    /**
     * Convert string to SessionStatus enum
     * @param status Status as string
     * @return SessionStatus enum or null if not found
     */
    public static SessionStatus fromString(String status) {
        if (status == null || status.trim().isEmpty()) {
            return null;
        }

        for (SessionStatus sessionStatus : SessionStatus.values()) {
            if (sessionStatus.name().equalsIgnoreCase(status.trim()) || 
                sessionStatus.displayName.equalsIgnoreCase(status.trim())) {
                return sessionStatus;
            }
        }
        return null;
    }

    /**
     * Check if a string is a valid session status
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
