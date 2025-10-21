package com.attendance.enums;

/**
 * UserRole - Defines the types of users in the system
 * Used for authentication and authorization
 */
public enum UserRole {
    STUDENT("Student", "student", "Students can mark attendance and view their records"),
    STAFF("Staff", "staff", "Staff can create sessions and manage attendance"),
    ADMIN("Admin", "admin", "Administrators have full system access");

    private final String displayName;
    private final String urlPrefix;
    private final String description;

    /**
     * Constructor for UserRole enum
     * @param displayName Human-readable name
     * @param urlPrefix URL prefix for role-based routing
     * @param description Role description
     */
    UserRole(String displayName, String urlPrefix, String description) {
        this.displayName = displayName;
        this.urlPrefix = urlPrefix;
        this.description = description;
    }

    /**
     * Get the display name of the role
     * @return Display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the URL prefix for this role
     * @return URL prefix (e.g., "student", "staff", "admin")
     */
    public String getUrlPrefix() {
        return urlPrefix;
    }

    /**
     * Get the description of this role
     * @return Role description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Convert string to UserRole enum
     * @param role Role as string
     * @return UserRole enum or null if not found
     */
    public static UserRole fromString(String role) {
        if (role == null || role.trim().isEmpty()) {
            return null;
        }

        for (UserRole userRole : UserRole.values()) {
            if (userRole.name().equalsIgnoreCase(role.trim()) || 
                userRole.displayName.equalsIgnoreCase(role.trim())) {
                return userRole;
            }
        }
        return null;
    }

    /**
     * Check if a string is a valid user role
     * @param role Role to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String role) {
        return fromString(role) != null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
