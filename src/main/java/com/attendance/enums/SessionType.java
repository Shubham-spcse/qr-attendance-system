package com.attendance.enums;

/**
 * SessionType - Defines the type of academic session
 * Used to categorize different types of classes
 */
public enum SessionType {
    LECTURE("Lecture", "Regular classroom lecture", 60, "#007BFF"),
    LAB("Lab", "Laboratory practical session", 120, "#28A745"),
    TUTORIAL("Tutorial", "Tutorial or discussion session", 60, "#FFC107"),
    PRACTICAL("Practical", "Hands-on practical session", 90, "#17A2B8");

    private final String displayName;
    private final String description;
    private final int defaultDuration; // in minutes
    private final String colorCode;

    /**
     * Constructor for SessionType enum
     * @param displayName Human-readable name
     * @param description Type description
     * @param defaultDuration Default duration in minutes
     * @param colorCode Color code for UI display (hex format)
     */
    SessionType(String displayName, String description, int defaultDuration, String colorCode) {
        this.displayName = displayName;
        this.description = description;
        this.defaultDuration = defaultDuration;
        this.colorCode = colorCode;
    }

    /**
     * Get the display name of the session type
     * @return Display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the description of this session type
     * @return Type description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the default duration for this session type
     * @return Duration in minutes
     */
    public int getDefaultDuration() {
        return defaultDuration;
    }

    /**
     * Get the color code for UI display
     * @return Hex color code (e.g., "#007BFF")
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     * Check if this is a lab session
     * @return true if LAB or PRACTICAL
     */
    public boolean isLabSession() {
        return this == LAB || this == PRACTICAL;
    }

    /**
     * Check if this is a theory session
     * @return true if LECTURE or TUTORIAL
     */
    public boolean isTheorySession() {
        return this == LECTURE || this == TUTORIAL;
    }

    /**
     * Convert string to SessionType enum
     * @param type Type as string
     * @return SessionType enum or null if not found
     */
    public static SessionType fromString(String type) {
        if (type == null || type.trim().isEmpty()) {
            return null;
        }

        for (SessionType sessionType : SessionType.values()) {
            if (sessionType.name().equalsIgnoreCase(type.trim()) || 
                sessionType.displayName.equalsIgnoreCase(type.trim())) {
                return sessionType;
            }
        }
        return null;
    }

    /**
     * Check if a string is a valid session type
     * @param type Type to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String type) {
        return fromString(type) != null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
