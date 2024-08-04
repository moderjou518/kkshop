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
  <div class="row text-center">	  		  	
  	<a href="/shop.go?webactionID=HBD1W010&tabKey=<%=tabKey%>" class="w-25"><i class="fas fa-shopping-cart text-white" style="color:blue"><br/>訂購</i></a>
	<a href="/shop.go?webactionID=HBD1W020&tabKey=<%=tabKey%>"  class="w-25"><i class="fas fa-search-dollar text-white" style="cursor: pointer"><br/>查詢</i></a>
  	<a href="/shop.go?webactionID=HBD1W030&tabKey=<%=tabKey%>" class="w-25"><i class="fas fa-user-alt text-white" style="cursor: pointer"><br/>會員</i></a>
    <ul class="navbar-nav mr-auto" style="display:none">        
        <li class="nav-item active">
          <i class="fas fa-home"><br/>首頁</i>
        </li>
        <li class="nav-item">          
        </li>
        <li class="nav-item">
          <a href="#"><i class="fas fa-shopping-cart"><br/>購物車</i></a>
        </li>
        <li class="nav-item">
          <a href="###" onclick=""><i class="fas fa-user"><br/>會員</i></a>
          <!-- 訂單查詢、 -->
        </li>
      </ul>    
  </div>  
  </nav>
</footer>
<!--  Google AD -->
<script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-9352666354860569" crossorigin="anonymous"></script>

<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-043BBX22SH"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'G-043BBX22SH');
</script>