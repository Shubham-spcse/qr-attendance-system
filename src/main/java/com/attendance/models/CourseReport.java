package com.attendance.models;

import java.util.List;

/**
 * CourseReport - Wrapper class for course attendance reports
 */
public class CourseReport {

    private int courseId;
    private String courseName;
    private String courseCode;
    private List<AttendanceSession> sessions;

    public CourseReport() {
    }

    public CourseReport(int courseId, String courseName, String courseCode, List<AttendanceSession> sessions) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.sessions = sessions;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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

    public List<AttendanceSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<AttendanceSession> sessions) {
        this.sessions = sessions;
    }

    @Override
    public String toString() {
        return "CourseReport{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", sessionsCount=" + (sessions != null ? sessions.size() : 0) +
                '}';
    }
}
