-- =====================================================
-- INITIAL DATA - Sample/Test Data
-- QR Attendance System
-- =====================================================

USE qr_attendance_system;

-- =====================================================
-- DEPARTMENTS
-- =====================================================
INSERT INTO departments (department_code, department_name, description, head_of_department, status) VALUES
('CSE', 'Computer Science & Engineering', 'Department of Computer Science and Engineering', 'Dr. Rajesh Kumar', 'ACTIVE'),
('ECE', 'Electronics & Communication Engineering', 'Department of Electronics and Communication', 'Dr. Priya Sharma', 'ACTIVE'),
('ME', 'Mechanical Engineering', 'Department of Mechanical Engineering', 'Dr. Amit Patel', 'ACTIVE'),
('IT', 'Information Technology', 'Department of Information Technology', 'Dr. Sneha Verma', 'ACTIVE'),
('EEE', 'Electrical & Electronics Engineering', 'Department of Electrical and Electronics', 'Dr. Arun Singh', 'ACTIVE');

-- =====================================================
-- ADMIN USERS
-- Password: Admin@123 (hashed with BCrypt)
-- =====================================================
INSERT INTO admin (admin_code, name, email, phone, password_hash, status) VALUES
('ADM001', 'System Administrator', 'admin@attendance.com', '9876543210', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('ADM002', 'Super Admin', 'superadmin@attendance.com', '9876543211', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE');

-- =====================================================
-- STAFF/FACULTY
-- Password: Staff@123 (hashed with BCrypt)
-- =====================================================
INSERT INTO staff (staff_code, name, email, phone, department_id, designation, password_hash, status) VALUES
('STF001', 'Dr. Ramesh Verma', 'ramesh.verma@college.edu', '9876543220', 1, 'Professor', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('STF002', 'Dr. Sunita Joshi', 'sunita.joshi@college.edu', '9876543221', 1, 'Associate Professor', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('STF003', 'Prof. Vikram Malhotra', 'vikram.m@college.edu', '9876543222', 1, 'Assistant Professor', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('STF004', 'Dr. Anjali Reddy', 'anjali.reddy@college.edu', '9876543223', 2, 'Professor', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('STF005', 'Prof. Karthik Nair', 'karthik.nair@college.edu', '9876543224', 4, 'Assistant Professor', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE');

-- =====================================================
-- COURSES
-- =====================================================
INSERT INTO courses (course_code, course_name, department_id, credits, semester, academic_year, min_attendance_percentage, status) VALUES
('CS101', 'Data Structures', 1, 4, 3, '2025-26', 75.00, 'ACTIVE'),
('CS102', 'Database Management Systems', 1, 4, 3, '2025-26', 75.00, 'ACTIVE'),
('CS201', 'Operating Systems', 1, 4, 5, '2025-26', 75.00, 'ACTIVE'),
('CS202', 'Computer Networks', 1, 4, 5, '2025-26', 75.00, 'ACTIVE'),
('CS301', 'Machine Learning', 1, 4, 7, '2025-26', 75.00, 'ACTIVE'),
('IT101', 'Web Technologies', 4, 3, 3, '2025-26', 75.00, 'ACTIVE'),
('IT201', 'Software Engineering', 4, 4, 5, '2025-26', 75.00, 'ACTIVE'),
('EC101', 'Digital Electronics', 2, 4, 3, '2025-26', 75.00, 'ACTIVE');

-- =====================================================
-- STAFF-COURSE MAPPING
-- =====================================================
INSERT INTO staff_courses (staff_id, course_id, academic_year, semester, status) VALUES
(1, 1, '2025-26', 3, 'ACTIVE'),  -- Dr. Ramesh teaches Data Structures
(1, 2, '2025-26', 3, 'ACTIVE'),  -- Dr. Ramesh teaches DBMS
(2, 3, '2025-26', 5, 'ACTIVE'),  -- Dr. Sunita teaches OS
(2, 4, '2025-26', 5, 'ACTIVE'),  -- Dr. Sunita teaches Networks
(3, 5, '2025-26', 7, 'ACTIVE'),  -- Prof. Vikram teaches ML
(5, 6, '2025-26', 3, 'ACTIVE'),  -- Prof. Karthik teaches Web Tech
(5, 7, '2025-26', 5, 'ACTIVE');  -- Prof. Karthik teaches Software Engg

-- =====================================================
-- STUDENTS
-- Password: Student@123 (hashed with BCrypt)
-- =====================================================
INSERT INTO students (roll_number, name, email, phone, department_id, year, section, semester, admission_date, password_hash, status) VALUES
('2023CSE001', 'Rahul Sharma', 'rahul.2023cse001@student.edu', '8876543210', 1, 2, 'A', 3, '2023-08-01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('2023CSE002', 'Priya Singh', 'priya.2023cse002@student.edu', '8876543211', 1, 2, 'A', 3, '2023-08-01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('2023CSE003', 'Amit Kumar', 'amit.2023cse003@student.edu', '8876543212', 1, 2, 'A', 3, '2023-08-01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('2023CSE004', 'Neha Gupta', 'neha.2023cse004@student.edu', '8876543213', 1, 2, 'A', 3, '2023-08-01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('2023CSE005', 'Ravi Patel', 'ravi.2023cse005@student.edu', '8876543214', 1, 2, 'A', 3, '2023-08-01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('2022CSE001', 'Anjali Mehta', 'anjali.2022cse001@student.edu', '8876543215', 1, 3, 'A', 5, '2022-08-01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('2022CSE002', 'Vikram Reddy', 'vikram.2022cse002@student.edu', '8876543216', 1, 3, 'A', 5, '2022-08-01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('2022CSE003', 'Sneha Iyer', 'sneha.2022cse003@student.edu', '8876543217', 1, 3, 'A', 5, '2022-08-01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('2023IT001', 'Karan Verma', 'karan.2023it001@student.edu', '8876543218', 4, 2, 'A', 3, '2023-08-01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE'),
('2023IT002', 'Divya Nair', 'divya.2023it002@student.edu', '8876543219', 4, 2, 'A', 3, '2023-08-01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE');

-- =====================================================
-- ENROLLMENTS
-- =====================================================
-- Second year CSE students enrolled in CS101, CS102
INSERT INTO enrollments (student_id, course_id, academic_year, semester, status) VALUES
(1, 1, '2025-26', 3, 'ACTIVE'),  -- Rahul -> Data Structures
(1, 2, '2025-26', 3, 'ACTIVE'),  -- Rahul -> DBMS
(2, 1, '2025-26', 3, 'ACTIVE'),  -- Priya -> Data Structures
(2, 2, '2025-26', 3, 'ACTIVE'),  -- Priya -> DBMS
(3, 1, '2025-26', 3, 'ACTIVE'),  -- Amit -> Data Structures
(3, 2, '2025-26', 3, 'ACTIVE'),  -- Amit -> DBMS
(4, 1, '2025-26', 3, 'ACTIVE'),  -- Neha -> Data Structures
(4, 2, '2025-26', 3, 'ACTIVE'),  -- Neha -> DBMS
(5, 1, '2025-26', 3, 'ACTIVE'),  -- Ravi -> Data Structures
(5, 2, '2025-26', 3, 'ACTIVE');  -- Ravi -> DBMS

-- Third year CSE students enrolled in CS201, CS202
INSERT INTO enrollments (student_id, course_id, academic_year, semester, status) VALUES
(6, 3, '2025-26', 5, 'ACTIVE'),  -- Anjali -> OS
(6, 4, '2025-26', 5, 'ACTIVE'),  -- Anjali -> Networks
(7, 3, '2025-26', 5, 'ACTIVE'),  -- Vikram -> OS
(7, 4, '2025-26', 5, 'ACTIVE'),  -- Vikram -> Networks
(8, 3, '2025-26', 5, 'ACTIVE'),  -- Sneha -> OS
(8, 4, '2025-26', 5, 'ACTIVE');  -- Sneha -> Networks

-- IT students
INSERT INTO enrollments (student_id, course_id, academic_year, semester, status) VALUES
(9, 6, '2025-26', 3, 'ACTIVE'),   -- Karan -> Web Tech
(10, 6, '2025-26', 3, 'ACTIVE');  -- Divya -> Web Tech

-- =====================================================
-- SAMPLE ATTENDANCE SESSIONS (for testing)
-- =====================================================
INSERT INTO attendance_sessions 
(session_code, course_id, staff_id, session_date, start_time, end_time, location, session_type, status, total_students) 
VALUES
('ATT_20251017_CS101_001', 1, 1, '2025-10-17', '10:00:00', '11:00:00', 'Room 201', 'LECTURE', 'EXPIRED', 5),
('ATT_20251016_CS102_001', 2, 1, '2025-10-16', '14:00:00', '15:00:00', 'Room 202', 'LECTURE', 'EXPIRED', 5),
('ATT_20251015_CS201_001', 3, 2, '2025-10-15', '09:00:00', '10:00:00', 'Room 301', 'LECTURE', 'EXPIRED', 3);

-- =====================================================
-- SAMPLE ATTENDANCE RECORDS (for testing)
-- =====================================================
INSERT INTO attendance (session_id, student_id, status, marked_at, marked_by) VALUES
-- Session 1 - CS101
(1, 1, 'PRESENT', '2025-10-17 10:05:00', 'STUDENT'),
(1, 2, 'PRESENT', '2025-10-17 10:03:00', 'STUDENT'),
(1, 3, 'LATE', '2025-10-17 10:18:00', 'STUDENT'),
(1, 4, 'PRESENT', '2025-10-17 10:07:00', 'STUDENT'),
(1, 5, 'ABSENT', NULL, 'SYSTEM'),

-- Session 2 - CS102
(2, 1, 'PRESENT', '2025-10-16 14:02:00', 'STUDENT'),
(2, 2, 'PRESENT', '2025-10-16 14:04:00', 'STUDENT'),
(2, 3, 'PRESENT', '2025-10-16 14:01:00', 'STUDENT'),
(2, 4, 'LATE', '2025-10-16 14:20:00', 'STUDENT'),
(2, 5, 'PRESENT', '2025-10-16 14:03:00', 'STUDENT'),

-- Session 3 - CS201
(3, 6, 'PRESENT', '2025-10-15 09:05:00', 'STUDENT'),
(3, 7, 'PRESENT', '2025-10-15 09:02:00', 'STUDENT'),
(3, 8, 'ABSENT', NULL, 'SYSTEM');

-- =====================================================
-- END OF INITIAL DATA
-- =====================================================

-- Display summary
SELECT 'Database initialization completed!' AS status;
SELECT COUNT(*) AS total_departments FROM departments;
SELECT COUNT(*) AS total_staff FROM staff;
SELECT COUNT(*) AS total_students FROM students;
SELECT COUNT(*) AS total_courses FROM courses;
SELECT COUNT(*) AS total_enrollments FROM enrollments;
