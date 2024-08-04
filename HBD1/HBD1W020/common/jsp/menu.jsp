<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%
	String compID = UTIL_String.nvl((String) request.getParameter("COMP_ID"), "");
	String compCode = UTIL_String.nvl((String) request.getAttribute("COMP_CODE"), "");
	String compName = UTIL_String.nvl((String) request.getAttribute("COMP_NAME"), "");	
%>
<script type='tex/javascript'></script>
<header></header>
