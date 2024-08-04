<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%
	String compID = UTIL_String.nvl((String) request.getParameter("COMP_ID"), "");
	String compCode = UTIL_String.nvl((String) request.getAttribute("COMP_CODE"), "");
	String compName = UTIL_String.nvl((String) request.getAttribute("COMP_NAME"), "");
	
%>
<footer class="footer">
<!-- Fixed navbar -->
  <nav class="navbar navbar-expand-md navbar-light fixed-bottom bg-danger" style="text-align:center">
  	<i class="fas fa-home text-white" style="color:blue"><br/>首頁</i>  	
  	<i class="fas fa-list text-white" style="cursor: pointer"><br/>訂單</i>
  	<i class="fas fa-chevron-up text-white" style="cursor: pointer" onclick="formEvent.gotoTop()"><br/>回頂端</i>
    <ul class="navbar-nav mr-auto" style="display:none">
        <!-- <li class="nav-item active">
          <a class="nav-link" onclick="toQuery()">目前進度 <span class="sr-only">(current)</span></a>
        </li> -->
        <li class="nav-item active">
          <i class="fas fa-home"><br/>首頁</i>
        </li>
        <li class="nav-item">          
        </li>
        <li class="nav-item">
          <a href="#"><i class="fas fa-shopping-cart"><br/>購物車</i></a>
        </li>
        <li class="nav-item">
          <a href="#"><i class="fas fa-user"><br/>會員</i></a>
          <!-- 訂單查詢、 -->
        </li>
      </ul>      
  </nav>
</footer>