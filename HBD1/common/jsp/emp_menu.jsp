<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%
	String tabKey = UTIL_String.nvl(request.getParameter("tabKey"), "#");
%>
<nav class="navbar navbar-expand-lg navbar-plus">
    <a class="navbar-brand" href="#">呆呆雞肉</a>          
    <button class="navbar-toggler hamburger-icon" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="line line-1"></span>
        <span class="line line-2"></span>
        <span class="line line-3"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li>
                <a class="nav-link" href="/shop.go?webactionID=HBD1LOGIN"><i class="fas fa-sign-out-alt"></i> 登出</a>
            </li>   
        </ul>
    </div>
</nav>