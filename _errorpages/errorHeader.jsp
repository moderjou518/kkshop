<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%
	String tabKey = UTIL_String.nvl(request.getParameter("tabKey"), "#");
	String cID = (String) request.getSession().getAttribute("COMP_ID");
	String actionUrl = "shop.go?webactionID=" + cID + "&=";
	
	
	
	//WUF1_UserInfo ui    = (WUF1_UserInfo)session.getAttribute("S_WUF1_ROOT_UserInfo");
/*
	System.out.println("loginId: " + ui.getInfo("loginId"));
	System.out.println("userD1BSN: " + ui.getInfo("userD1BSN"));
	System.out.println("userDept: " + ui.getInfo("userDept"));
	System.out.println("userid: " + ui.getInfo("userid"));
	System.out.println("userName: " + ui.getInfo("userName"));
	System.out.println("userEmail: " + ui.getInfo("userEmail"));
	System.out.println("userAdmin: " + ui.getInfo("userAdmin"));
	System.out.println("userVerify: " + ui.getInfo("userVerify"));
	System.out.println("sVerify: " + ui.getInfo("sVerify"));
	System.out.println("locale: " + ui.getInfo("locale"));
	System.out.println("website: " + ui.getInfo("website"));
	System.out.println("userType: " + ui.getInfo("userType"));
	System.out.println("userLang: " + ui.getInfo("userLang"));
	
	
*/

%>
<!-- 
<nav class="navbar navbar-plus" style="border: 0;">
	<div class="navbar-header">
        <a class="navbar-brand" href="#">Shop System</a>(Admin)        
    </div>
    <div class="collapse navbar-collapse" id="#navbarNavDropdown">
        <ul class="navbar-nav">
            <li class="nav-item dropdown"><a class="nav-link dropdown-toggle" href="#" id="templateMenuLink" data-toggle="dropdown" aria-haspopup="true"
                aria-expanded="false">切版 </a>
                <div class="dropdown-menu dropdown-menu-plus" aria-labelledby="templateMenuLink">
                    <a class="dropdown-item" href="">RWD</a> <a class="dropdown-item" href="">Grid System</a> <a class="dropdown-item" href="">樣板參考</a>
                </div></li>
            <li class="nav-item dropdown"><a class="nav-link dropdown-toggle" href="#" id="blockComponentMenuLink" data-toggle="dropdown" aria-haspopup="true"
                aria-expanded="false">區塊元素 </a>
                <div class="dropdown-menu dropdown-menu-plus" aria-labelledby="blockComponentMenuLink1">
                    <a class="dropdown-item" href="">Navbar</a> <a class="dropdown-item" href="">Breadcrumb</a>
                </div></li>
            <li class="nav-item"><a class="nav-link" href="">SOL+平台撰寫的知識庫</a></li>
        </ul>
	</div>
</nav> -->
<nav class="navbar navbar-expand-lg navbar-plus">
    <a class="navbar-brand" href="#">阿呆燒雞預訂單管理系統</a>          
    <button class="navbar-toggler hamburger-icon" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="line line-1"></span>
        <span class="line line-2"></span>
        <span class="line line-3"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/shop.go?webactionID=HBD1LOGIN">登入系統</a>       
            </li>                               
        </ul>
    </div>
</nav>