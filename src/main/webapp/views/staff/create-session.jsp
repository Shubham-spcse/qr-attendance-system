<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Create Session - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/navbar.jsp" %>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0"><i class="fas fa-plus-circle me-2"></i> Create Attendance Session</h4>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show">
                            <i class="fas fa-exclamation-circle me-2"></i> ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/staff/create-session" method="post">
                        <div class="mb-3">
                            <label for="courseId" class="form-label fw-semibold">
                                <i class="fas fa-book me-1"></i> Course *
                            </label>
                            <select class="form-select" id="courseId" name="courseId" required>
                                <option value="">-- Select Course --</option>
                                <c:forEach items="${courses}" var="course">
                                    <option value="${course.courseId}">
                                        ${course.courseCode} - ${course.courseName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="sessionDate" class="form-label fw-semibold">
                                    <i class="fas fa-calendar me-1"></i> Date *
                                </label>
                                <input type="date" class="form-control" id="sessionDate" name="sessionDate" required>
                            </div>
                            <div class="col-md-6">
                                <label for="sessionType" class="form-label fw-semibold">
                                    <i class="fas fa-tag me-1"></i> Session Type *
                                </label>
                                <select class="form-select" id="sessionType" name="sessionType" required>
                                    <option value="LECTURE">Lecture</option>
                                    <option value="LAB">Lab</option>
                                    <option value="TUTORIAL">Tutorial</option>
                                    <option value="PRACTICAL">Practical</option>
                                </select>
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="startTime" class="form-label fw-semibold">
                                    <i class="fas fa-clock me-1"></i> Start Time *
                                </label>
                                <input type="time" class="form-control" id="startTime" name="startTime" required>
                            </div>
                            <div class="col-md-6">
                                <label for="endTime" class="form-label fw-semibold">
                                    <i class="fas fa-clock me-1"></i> End Time *
                                </label>
                                <input type="time" class="form-control" id="endTime" name="endTime" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="location" class="form-label fw-semibold">
                                <i class="fas fa-map-marker-alt me-1"></i> Location *
                            </label>
                            <input type="text" class="form-control" id="location" name="location" 
                                   placeholder="e.g., Room 301, Lab 2" required>
                        </div>

                        <div class="mb-3">
                            <label for="notes" class="form-label fw-semibold">
                                <i class="fas fa-sticky-note me-1"></i> Notes (Optional)
                            </label>
                            <textarea class="form-control" id="notes" name="notes" rows="3" 
                                      placeholder="Additional information about this session"></textarea>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="fas fa-plus-circle me-2"></i> Create Session
                            </button>
                            <a href="${pageContext.request.contextPath}/staff/dashboard" class="btn btn-outline-secondary">
                                <i class="fas fa-times me-2"></i> Cancel
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.getElementById('sessionDate').min = new Date().toISOString().split('T')[0];
</script>

<%@ include file="../common/footer.jsp" %>
