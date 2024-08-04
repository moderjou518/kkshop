<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*, org.apache.log4j.Logger" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%
	//String cID = (String) request.getSession().getAttribute("COMP_ID");
	//String actionUrl = "shop.go?cid=" + cID + "&webactionID=";
	//String compCode = (String) request.getSession().getAttribute("COMP_CODE");
	//String compName = (String) request.getSession().getAttribute("COMP_NAME");
	
	//System.out.println("jsp compCode: " + compCode);
	//System.out.println("jsp compName: " + compName);
	
	Logger logger = Logger.getLogger("KKSHOP_JSP_LOG");
	logger.info("HBD1LOGIN_Google.jsp");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
	<!--jQuery-->
	<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
	
	<!--Bootstrap-->
	<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet"></link>
	<script src="https://apis.google.com/js/api:client.js"></script>       
    <title>22Google登入系統22</title>	    
</head>
<body>


<!--登入、登出按鈕-->
<button id="GOOGLE_login" class="btn btn-large btn-primary">GOOGLE 快速登入</button><br/>
<button id="GOOGLE_logout" class="btn btn-large btn-warning" style="display:none">GOOGLE 登出</button>

目前狀態：
<span id="GOOGLE_STATUS_2"></span>
<script>
// 進行登入程序
var startApp = function() {
	gapi.load("auth2", function() {
		auth2 = gapi.auth2.init({
					client_id: 		"183358413146-79of4mps144kc78dek9tq2994d2njfrh.apps.googleusercontent.com", // 用戶端 ID
					cookiepolicy: 	"single_host_origin"
				});
		attachSignin(document.getElementById("GOOGLE_login"));
	});
};

function attachSignin(element) {
	
	auth2.attachClickHandler(element, {},
		// 登入成功
		function(googleUser) {
			var profile = googleUser.getBasicProfile(),
			$target = $("#GOOGLE_STATUS_2"),
			html = "";

			html += "ID: " + profile.getId() + "<br/>";
			html += "會員暱稱： " + profile.getName() + "<br/>";
			html += "會員頭像：" + profile.getImageUrl() + "<br/>";
			html += "會員 email：" + profile.getEmail() + "<br/>";
			$target.html(html);
		},	
		
		
		// 登入失敗
		function(error) {
			$("#GOOGLE_STATUS_2").html("");
			alert(JSON.stringify(error, undefined, 2));
		});
}

// 點擊登入
$("#GOOGLE_login").click(function() {
	// 進行登入程序
	startApp();
});

// 點擊登出
$("#GOOGLE_logout").click(function() {
	var auth2 = gapi.auth2.getAuthInstance();
	auth2.signOut().then(function() {
		// 登出後的動作
		$("#GOOGLE_STATUS_2").html("");
	});
});

startApp();

</script>

</body>
</html>