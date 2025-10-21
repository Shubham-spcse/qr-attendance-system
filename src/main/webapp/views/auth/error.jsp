<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Error - QR Attendance System" scope="request" />
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="row justify-content-center align-items-center" style="min-height: 80vh;">
        <div class="col-md-6 text-center">
            <!-- Error Icon -->
            <div class="mb-4">
                <i class="fas fa-exclamation-triangle fa-5x text-warning"></i>
            </div>

            <!-- Error Code -->
            <h1 class="display-1 fw-bold text-primary">
                <c:choose>
                    <c:when test="${pageContext.errorData != null}">
                        ${pageContext.errorData.statusCode}
                    </c:when>
                    <c:otherwise>
                        Error
                    </c:otherwise>
                </c:choose>
            </h1>

            <!-- Error Title -->
            <h2 class="mb-3">
                <c:choose>
                    <c:when test="${pageContext.errorData.statusCode == 404}">
                        Page Not Found
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == 403}">
                        Access Denied
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == 500}">
                        Internal Server Error
                    </c:when>
                    <c:otherwise>
                        Oops! Something Went Wrong
                    </c:otherwise>
                </c:choose>
            </h2>

            <!-- Error Message -->
            <p class="text-muted mb-4">
                <c:choose>
                    <c:when test="${pageContext.errorData.statusCode == 404}">
                        The page you are looking for might have been removed, had its name changed, 
                        or is temporarily unavailable.
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == 403}">
                        You don't have permission to access this resource. Please contact your administrator.
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == 500}">
                        We're experiencing some technical difficulties. Our team has been notified and 
                        is working to fix the issue.
                    </c:when>
                    <c:otherwise>
                        An unexpected error has occurred. Please try again later or contact support if the problem persists.
                    </c:otherwise>
                </c:choose>
            </p>

            <!-- Custom Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger d-inline-block" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    ${error}
                </div>
            </c:if>

            <!-- Action Buttons -->
            <div class="mt-4">
                <a href="javascript:history.back()" class="btn btn-outline-primary me-2">
                    <i class="fas fa-arrow-left me-2"></i> Go Back
                </a>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                    <i class="fas fa-home me-2"></i> Go to Home
                </a>
            </div>

            <!-- Support Link -->
            <div class="mt-4">
                <p class="text-muted">
                    Need help? <a href="#" class="text-decoration-none">Contact Support</a>
                </p>
            </div>

            <!-- Technical Details (Only in Development) -->
            <c:if test="${pageContext.errorData != null && param.debug == 'true'}">
                <div class="card mt-4 text-start">
                    <div class="card-header bg-danger text-white">
                        <strong>Technical Details (Debug Mode)</strong>
                    </div>
                    <div class="card-body">
                        <p><strong>Status Code:</strong> ${pageContext.errorData.statusCode}</p>
                        <p><strong>Request URI:</strong> ${pageContext.errorData.requestURI}</p>
                        <p><strong>Servlet Name:</strong> ${pageContext.errorData.servletName}</p>
                        <c:if test="${pageContext.exception != null}">
                            <p><strong>Exception:</strong> ${pageContext.exception}</p>
                            <p><strong>Message:</strong> ${pageContext.exception.message}</p>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
