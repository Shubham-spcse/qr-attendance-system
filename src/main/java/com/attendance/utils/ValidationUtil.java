package com.attendance.utils;

import java.util.regex.Pattern;

/**
 * ValidationUtil - Centralized validation methods
 * 
 * Usage:
 *   if (!ValidationUtil.isValidEmail(email)) {
 *       throw new IllegalArgumentException("Invalid email");
 *   }
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class ValidationUtil {

    // Regex patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10}$"
    );

    private static final Pattern ROLL_NUMBER_PATTERN = Pattern.compile(
        "^[0-9]{4}[A-Z]{3}[0-9]{3}$"
    );

    private static final Pattern DEPARTMENT_CODE_PATTERN = Pattern.compile(
        "^[A-Z]{3,5}$"
    );

    private static final Pattern COURSE_CODE_PATTERN = Pattern.compile(
        "^[A-Z]{2}[0-9]{3}$"
    );

    /**
     * Validate email address format
     * 
     * @param email Email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate phone number (10 digits)
     * 
     * @param phone Phone number to validate
     * @return true if valid 10-digit phone
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validate roll number format
     * Format: YYYYDDDNNN (e.g., 2023CSE001)
     * 
     * @param rollNumber Roll number to validate
     * @return true if valid format
     */
    public static boolean isValidRollNumber(String rollNumber) {
        if (rollNumber == null || rollNumber.trim().isEmpty()) {
            return false;
        }
        return ROLL_NUMBER_PATTERN.matcher(rollNumber.toUpperCase()).matches();
    }

    /**
     * Validate department code format
     * Format: 3-5 uppercase letters (e.g., CSE, ECE, MECH)
     * 
     * @param deptCode Department code to validate
     * @return true if valid format
     */
    public static boolean isValidDepartmentCode(String deptCode) {
        if (deptCode == null || deptCode.trim().isEmpty()) {
            return false;
        }
        return DEPARTMENT_CODE_PATTERN.matcher(deptCode.toUpperCase()).matches();
    }

    /**
     * Validate course code format
     * Format: 2 letters + 3 digits (e.g., CS101, MA201)
     * 
     * @param courseCode Course code to validate
     * @return true if valid format
     */
    public static boolean isValidCourseCode(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            return false;
        }
        return COURSE_CODE_PATTERN.matcher(courseCode.toUpperCase()).matches();
    }

    /**
     * Validate string is not null or empty
     * 
     * @param value String to validate
     * @return true if not null and not empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validate string length is within range
     * 
     * @param value String to validate
     * @param minLength Minimum length
     * @param maxLength Maximum length
     * @return true if length is within range
     */
    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (value == null) return false;
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Validate integer is within range
     * 
     * @param value Integer to validate
     * @param min Minimum value
     * @param max Maximum value
     * @return true if within range
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Validate year (1-5 for engineering)
     * 
     * @param year Year to validate
     * @return true if valid year (1-5)
     */
    public static boolean isValidYear(int year) {
        return isInRange(year, 1, 5);
    }

    /**
     * Validate semester (1-10 for engineering)
     * 
     * @param semester Semester to validate
     * @return true if valid semester (1-10)
     */
    public static boolean isValidSemester(int semester) {
        return isInRange(semester, 1, 10);
    }

    /**
     * Validate section (single uppercase letter)
     * 
     * @param section Section to validate
     * @return true if single uppercase letter
     */
    public static boolean isValidSection(String section) {
        if (section == null || section.length() != 1) {
            return false;
        }
        return Character.isUpperCase(section.charAt(0));
    }

    /**
     * Validate credits (1-6 typical range)
     * 
     * @param credits Credits to validate
     * @return true if valid credits (1-6)
     */
    public static boolean isValidCredits(int credits) {
        return isInRange(credits, 1, 6);
    }

    /**
     * Sanitize input string (remove dangerous characters)
     * 
     * @param input Input string to sanitize
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) return null;

        // Remove HTML tags and special characters that could cause issues
        return input.replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;")
                   .replaceAll("\"", "&quot;")
                   .replaceAll("'", "&#x27;")
                   .trim();
    }

    /**
     * Validate academic year format (e.g., "2025-26")
     * 
     * @param academicYear Academic year string
     * @return true if valid format
     */
    public static boolean isValidAcademicYear(String academicYear) {
        if (academicYear == null || academicYear.length() != 7) {
            return false;
        }

        String[] parts = academicYear.split("-");
        if (parts.length != 2) return false;

        try {
            int year1 = Integer.parseInt(parts[0]);
            int year2 = Integer.parseInt(parts[1]);
            return year2 == (year1 + 1) % 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
