-- =====================================================
-- QR ATTENDANCE SYSTEM - DATABASE SCHEMA
-- Version: 1.0.0
-- Description: Complete database schema for QR-based attendance management
-- =====================================================

-- Drop existing database if exists
DROP DATABASE IF EXISTS qr_attendance_system;

-- Create database
CREATE DATABASE qr_attendance_system 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE qr_attendance_system;

-- =====================================================
-- TABLE: departments
-- Purpose: Store department information
-- =====================================================
CREATE TABLE departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_code VARCHAR(20) NOT NULL UNIQUE,
    department_name VARCHAR(100) NOT NULL,
    description TEXT,
    head_of_department VARCHAR(100),
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_dept_code (department_code),
    INDEX idx_dept_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: courses
-- Purpose: Store course/subject information
-- =====================================================
CREATE TABLE courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(150) NOT NULL,
    department_id INT NOT NULL,
    credits INT DEFAULT 3,
    semester INT,
    academic_year VARCHAR(20),
    description TEXT,
    min_attendance_percentage DECIMAL(5,2) DEFAULT 75.00,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE,
    INDEX idx_course_code (course_code),
    INDEX idx_department (department_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: admin
-- Purpose: Store admin user information
-- =====================================================
CREATE TABLE admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    admin_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    password_hash VARCHAR(255) NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    INDEX idx_admin_code (admin_code),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: staff
-- Purpose: Store staff/faculty information
-- =====================================================
CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    staff_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    department_id INT NOT NULL,
    designation VARCHAR(50),
    password_hash VARCHAR(255) NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE,
    INDEX idx_staff_code (staff_code),
    INDEX idx_email (email),
    INDEX idx_department (department_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: students
-- Purpose: Store student information
-- =====================================================
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    department_id INT NOT NULL,
    year INT NOT NULL,
    section VARCHAR(10),
    semester INT,
    admission_date DATE,
    password_hash VARCHAR(255) NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'GRADUATED', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE,
    INDEX idx_roll_number (roll_number),
    INDEX idx_email (email),
    INDEX idx_department (department_id),
    INDEX idx_year (year),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: staff_courses
-- Purpose: Map staff to courses they teach
-- =====================================================
CREATE TABLE staff_courses (
    staff_course_id INT AUTO_INCREMENT PRIMARY KEY,
    staff_id INT NOT NULL,
    course_id INT NOT NULL,
    academic_year VARCHAR(20),
    semester INT,
    assigned_date DATE DEFAULT (CURRENT_DATE),
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    UNIQUE KEY unique_staff_course (staff_id, course_id, academic_year, semester),
    INDEX idx_staff (staff_id),
    INDEX idx_course (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: enrollments
-- Purpose: Store student course enrollments
-- =====================================================
CREATE TABLE enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    academic_year VARCHAR(20),
    semester INT,
    enrollment_date DATE DEFAULT (CURRENT_DATE),
    status ENUM('ACTIVE', 'DROPPED', 'COMPLETED') DEFAULT 'ACTIVE',
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, course_id, academic_year, semester),
    INDEX idx_student (student_id),
    INDEX idx_course (course_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: attendance_sessions
-- Purpose: Store attendance session information
-- =====================================================
CREATE TABLE attendance_sessions (
    session_id INT AUTO_INCREMENT PRIMARY KEY,
    session_code VARCHAR(50) NOT NULL UNIQUE,
    course_id INT NOT NULL,
    staff_id INT NOT NULL,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    location VARCHAR(100),
    session_type ENUM('LECTURE', 'LAB', 'TUTORIAL', 'PRACTICAL') DEFAULT 'LECTURE',
    status ENUM('SCHEDULED', 'ACTIVE', 'EXPIRED', 'CANCELLED') DEFAULT 'SCHEDULED',
    qr_code_data TEXT,
    qr_code_checksum VARCHAR(255),
    total_students INT DEFAULT 0,
    present_count INT DEFAULT 0,
    late_count INT DEFAULT 0,
    absent_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activated_at TIMESTAMP NULL,
    ended_at TIMESTAMP NULL,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id) ON DELETE CASCADE,
    INDEX idx_session_code (session_code),
    INDEX idx_course (course_id),
    INDEX idx_staff (staff_id),
    INDEX idx_date (session_date),
    INDEX idx_status (status),
    INDEX idx_date_course (session_date, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: attendance
-- Purpose: Store individual attendance records
-- =====================================================
CREATE TABLE attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    session_id INT NOT NULL,
    student_id INT NOT NULL,
    status ENUM('PRESENT', 'LATE', 'ABSENT') DEFAULT 'ABSENT',
    marked_at TIMESTAMP NULL,
    qr_code_used VARCHAR(255),
    location_latitude DECIMAL(10, 8) NULL,
    location_longitude DECIMAL(11, 8) NULL,
    ip_address VARCHAR(45),
    device_info VARCHAR(255),
    marked_by ENUM('SYSTEM', 'STAFF', 'STUDENT') DEFAULT 'STUDENT',
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES attendance_sessions(session_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (session_id, student_id),
    INDEX idx_session (session_id),
    INDEX idx_student (student_id),
    INDEX idx_status (status),
    INDEX idx_marked_at (marked_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: qr_codes
-- Purpose: Store generated QR code history
-- =====================================================
CREATE TABLE qr_codes (
    qr_id INT AUTO_INCREMENT PRIMARY KEY,
    session_id INT NOT NULL,
    qr_data TEXT NOT NULL,
    checksum VARCHAR(255) NOT NULL,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valid_until TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    scan_count INT DEFAULT 0,
    FOREIGN KEY (session_id) REFERENCES attendance_sessions(session_id) ON DELETE CASCADE,
    INDEX idx_session (session_id),
    INDEX idx_checksum (checksum),
    INDEX idx_valid_until (valid_until),
    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: notifications
-- Purpose: Store system notifications
-- =====================================================
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_type ENUM('STUDENT', 'STAFF', 'ADMIN') NOT NULL,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    notification_type ENUM('INFO', 'WARNING', 'ALERT', 'SUCCESS') DEFAULT 'INFO',
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL,
    INDEX idx_user (user_type, user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLE: audit_logs
-- Purpose: Store system audit trail
-- =====================================================
CREATE TABLE audit_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_type ENUM('STUDENT', 'STAFF', 'ADMIN') NOT NULL,
    user_id INT NOT NULL,
    action VARCHAR(100) NOT NULL,
    table_name VARCHAR(50),
    record_id INT,
    old_value TEXT,
    new_value TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_type, user_id),
    INDEX idx_action (action),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- VIEWS: Helpful database views
-- =====================================================

-- View: Student Attendance Summary
CREATE VIEW student_attendance_summary AS
SELECT 
    s.student_id,
    s.roll_number,
    s.name AS student_name,
    c.course_id,
    c.course_code,
    c.course_name,
    COUNT(DISTINCT ats.session_id) AS total_sessions,
    SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) AS present_count,
    SUM(CASE WHEN a.status = 'LATE' THEN 1 ELSE 0 END) AS late_count,
    SUM(CASE WHEN a.status = 'ABSENT' THEN 1 ELSE 0 END) AS absent_count,
    ROUND(
        (SUM(CASE WHEN a.status IN ('PRESENT', 'LATE') THEN 1 ELSE 0 END) * 100.0) / 
        COUNT(DISTINCT ats.session_id), 2
    ) AS attendance_percentage
FROM students s
INNER JOIN enrollments e ON s.student_id = e.student_id
INNER JOIN courses c ON e.course_id = c.course_id
LEFT JOIN attendance_sessions ats ON c.course_id = ats.course_id 
    AND ats.status IN ('ACTIVE', 'EXPIRED')
LEFT JOIN attendance a ON ats.session_id = a.session_id AND s.student_id = a.student_id
WHERE s.status = 'ACTIVE' AND e.status = 'ACTIVE'
GROUP BY s.student_id, c.course_id;

-- View: Course Attendance Statistics
CREATE VIEW course_attendance_stats AS
SELECT 
    c.course_id,
    c.course_code,
    c.course_name,
    COUNT(DISTINCT ats.session_id) AS total_sessions,
    COUNT(DISTINCT e.student_id) AS enrolled_students,
    AVG(ats.present_count + ats.late_count) AS avg_attendance,
    ROUND(
        (AVG(ats.present_count + ats.late_count) * 100.0) / 
        COUNT(DISTINCT e.student_id), 2
    ) AS avg_attendance_percentage
FROM courses c
LEFT JOIN attendance_sessions ats ON c.course_id = ats.course_id 
    AND ats.status IN ('ACTIVE', 'EXPIRED')
LEFT JOIN enrollments e ON c.course_id = e.course_id AND e.status = 'ACTIVE'
WHERE c.status = 'ACTIVE'
GROUP BY c.course_id;

-- =====================================================
-- STORED PROCEDURES
-- =====================================================

-- Procedure: Calculate attendance percentage for a student in a course
DELIMITER //
CREATE PROCEDURE calculate_student_attendance(
    IN p_student_id INT,
    IN p_course_id INT,
    OUT p_percentage DECIMAL(5,2)
)
BEGIN
    SELECT 
        ROUND(
            (SUM(CASE WHEN a.status IN ('PRESENT', 'LATE') THEN 1 ELSE 0 END) * 100.0) / 
            COUNT(*), 2
        ) INTO p_percentage
    FROM attendance a
    INNER JOIN attendance_sessions ats ON a.session_id = ats.session_id
    WHERE a.student_id = p_student_id 
        AND ats.course_id = p_course_id
        AND ats.status IN ('ACTIVE', 'EXPIRED');
END //
DELIMITER ;

-- Procedure: Update session counts
DELIMITER //
CREATE PROCEDURE update_session_counts(IN p_session_id INT)
BEGIN
    UPDATE attendance_sessions 
    SET 
        present_count = (
            SELECT COUNT(*) FROM attendance 
            WHERE session_id = p_session_id AND status = 'PRESENT'
        ),
        late_count = (
            SELECT COUNT(*) FROM attendance 
            WHERE session_id = p_session_id AND status = 'LATE'
        ),
        absent_count = (
            SELECT COUNT(*) FROM attendance 
            WHERE session_id = p_session_id AND status = 'ABSENT'
        )
    WHERE session_id = p_session_id;
END //
DELIMITER ;

-- =====================================================
-- TRIGGERS
-- =====================================================

-- Trigger: Auto-update session counts after attendance insert
DELIMITER //
CREATE TRIGGER after_attendance_insert
AFTER INSERT ON attendance
FOR EACH ROW
BEGIN
    CALL update_session_counts(NEW.session_id);
END //
DELIMITER ;

-- Trigger: Auto-update session counts after attendance update
DELIMITER //
CREATE TRIGGER after_attendance_update
AFTER UPDATE ON attendance
FOR EACH ROW
BEGIN
    CALL update_session_counts(NEW.session_id);
END //
DELIMITER ;

-- =====================================================
-- END OF SCHEMA
-- =====================================================
