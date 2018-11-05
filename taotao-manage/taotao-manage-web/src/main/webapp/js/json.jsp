<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String callback = request.getParameter("callback");
	response.getWriter().write(callback+"({\"abc\":\"123\"})");
%>
