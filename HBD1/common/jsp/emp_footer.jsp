<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%
	String tabKey = UTIL_String.nvl(request.getParameter("tabKey"), "#");
/*
	String compID = UTIL_String.nvl((String) request.getParameter("COMP_ID"), "");
	String compCode = UTIL_String.nvl((String) request.getAttribute("COMP_CODE"), "");
	String compName = UTIL_String.nvl((String) request.getAttribute("COMP_NAME"), "");
	*/
	
%>
<footer class="footer">
<!-- Fixed navbar -->
  <nav class="navbar navbar-expand-md navbar-light fixed-bottom bg-danger" style="text-align:center">	  	
  	<a href="/shop.go?webactionID=HBD1W320&tabKey=<%=tabKey%>"><i class="fas fa-people-carry text-white" style="color:blue"><br/>出貨</i></a>
	<a href="/shop.go?webactionID=HBD1W310&tabKey=<%=tabKey%>"><i class="fas fa-search text-white" style="cursor: pointer"><br/>查詢</i></a>
	<a href="/shop.go?webactionID=HBD1W330&tabKey=<%=tabKey%>"><i class="fas fa-fire-alt text-white" style="cursor: pointer"><br/>生產</i></a>
	  	
    <ul class="navbar-nav mr-auto" style="display:none">        
        <li class="nav-item active">
          <i class="fas fa-home"><br/>首頁</i>
        </li>
        <li class="nav-item">          
        </li>
        <li class="nav-item">
          <a href="#"><i class="fas fa-shipping-fast"><br/>購物車</i></a>
        </li>
        <li class="nav-item">
          <a href="###" onclick=""><i class="fas fa-user"><br/>會員</i></a>
          <!-- 訂單查詢、 -->
        </li>
      </ul>      
  </nav>
</footer>