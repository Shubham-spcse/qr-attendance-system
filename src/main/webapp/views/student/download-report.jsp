<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    // Since DownloadReportServlet handles the actual download,
    // we'll redirect to it when this page is accessed
    String courseId = request.getParameter("courseId");
    String format = request.getParameter("format");

    if (format == null) {
        format = "CSV";
    }

    String redirectURL = request.getContextPath() + "/student/download-report?format=" + format;
    if (courseId != null && !courseId.isEmpty()) {
        redirectURL += "&courseId=" + courseId;
    }

    response.sendRedirect(redirectURL);
%>
