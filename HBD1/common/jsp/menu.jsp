<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*, net.sf.json.JSONObject" %>
<%
	String tabKey = UTIL_String.nvl(request.getParameter("tabKey"), "#");
	String compCode = (String) request.getSession().getAttribute("LoginComp");
	String loginGroup = (String) request.getSession().getAttribute("LoginGroup");
	String actionUrl = "shop.go?webactionID=" + compCode + "&=";
	
    // 讀取成功登入過的 COOKIE(公司別、角色、登入帳號)
    JSONObject cookieJo = new JSONObject();
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
        if("LoginID".equals(cookie.getName())) {
            //cookie.setValue("");
            cookieJo.put("LoginID", cookie.getValue());                
        }
        if("Password".equals(cookie.getName())) {
            cookieJo.put("Password", cookie.getValue());                
        }        
        if("CompCode".equals(cookie.getName())) {
            cookieJo.put("CompCode", cookie.getValue());              
            compCode = cookie.getValue();
        }
        
        if("UserRole".equals(cookie.getName())) {
            cookieJo.put("UserRole", cookie.getValue());
            loginGroup = cookie.getValue();
        }        
        //System.out.println(cookie.getName() + " : " + cookie.getValue());
    }
	
    // 取得各角色被授權的程式列表
    String authPgmID = UTIL_GlobalConfig.getAuthPgmID(compCode, loginGroup);
    
    //System.out.println("authPgmID: " + authPgmID);
	
	
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
<script>
	
$(document).ready(function() {
    
	let authActions = '<%=authPgmID%>';
	let menuList = $("#navbarSupportedContent").find("[data-evg-action]");        
	$.each(menuList, function() {
		let menuAction = $(this).data('evg-action');
		console.log(menuAction);
		if (authActions.indexOf(menuAction) >= 0){
			console.log(menuAction + ': Y');
			$(this).show();
		}else{
			console.log(menuAction + ': N');
			$(this).hide();
		}
	});
    
});
				

</script>
<nav class="navbar navbar-expand-lg navbar-plus">
    <a class="navbar-brand" href="#">呆呆雞肉管理系統</a>          
    <button class="navbar-toggler hamburger-icon" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="line line-1"></span>
        <span class="line line-2"></span>
        <span class="line line-3"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a data-evg-action="HBD1W610" class="nav-link" href="/shop.go?webactionID=HBD1W610&tabKey=<%=tabKey%>">商品維護</a>       
            </li>
            <li>
				<a data-evg-action="HBD1W690" class="nav-link" href="/shop.go?webactionID=HBD1W690&tabKey=<%=tabKey%>">商品價格設定</a>
            </li>
            <li>
				<a data-evg-action="HBD1W620" class="nav-link" href="/shop.go?webactionID=HBD1W620&tabKey=<%=tabKey%>">預訂單管理</a>
            </li>
            <li>
                <a data-evg-action="HBD1W660" class="nav-link" href="/shop.go?webactionID=HBD1W660&tabKey=<%=tabKey%>">每日訂單管理</a>
            </li>
            <li>
                <a data-evg-action="HBD1W661" class="nav-link" href="/shop.go?webactionID=HBD1W661&tabKey=<%=tabKey%>">每月訂單統計</a>
            </li>
            <li>
                <a data-evg-action="HBD1W662" class="nav-link" href="/shop.go?webactionID=HBD1W662&tabKey=<%=tabKey%>">每年訂單統計</a>
            </li>
            <li>
                <a data-evg-action="HBD1W670" class="nav-link" href="/shop.go?webactionID=HBD1W670&tabKey=<%=tabKey%>">預訂及出貨統計</a>
            </li>
            <li>
            	<a data-evg-action="HBD1W510" class="nav-link" href="/shop.go?webactionID=HBD1W510&tabKey=<%=tabKey%>">進貨登記</a>
            </li>                                   
            <li>
                <a data-evg-action="HBD1W680" class="nav-link" href="/shop.go?webactionID=HBD1W680&tabKey=<%=tabKey%>">應收帳款</a>
            </li>           
            
            <li>
                <a data-evg-action="HBD1W310" class="nav-link" href="/shop.go?webactionID=HBD1W320&tabKey=<%=tabKey%>">現場-出貨</a>
            </li>
            
            <li>
                <a data-evg-action="HBD1W330" class="nav-link" href="/shop.go?webactionID=HBD1W330&tabKey=<%=tabKey%>">現場-生產</a>
            </li>
            
            
            
            <li class="nav-item dropdown" data-evg-action="HBD1W610" >
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    其他作業
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                	<a class="dropdown-item" data-evg-action="HBD1W630" href="/shop.go?webactionID=HBD1W630&tabKey=<%=tabKey%>">會員管理</a>                                        
                    <!-- <a class="dropdown-item" href="/shop.go?webactionID=HBD1W680&tabKey=<%=tabKey%>">應收帳款(周結)</a> -->                        
                    <!-- <a class="dropdown-item" href="action">應收帳後(月結)</a> -->                                            
                    <a class="dropdown-item" href="action" data-evg-action="HBD1W700" >月損益報表</a>                        
                    <a class="dropdown-item" href="action" data-evg-action="HBD1W710" >年度統計圖</a>                    
                </div>
            </li>
            <li>
                <a class="nav-link" href="/shop.go?webactionID=HBD1LOGIN"><i class="fas fa-sign-out-alt"></i> 登出</a>
            </li>   
        </ul>
    </div>
</nav>
<!--  Google AD 
<script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-9352666354860569" crossorigin="anonymous"></script>
-->
<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-043BBX22SH"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'G-043BBX22SH');
</script>