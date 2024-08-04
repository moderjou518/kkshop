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
    <title>呆呆雞肉-Google登入系統</title>		
    <style>
            
html,
body {
  height: 100%;
  font-size: 1.25rem;
}

body {
padding-top: 40px;
  padding-bottom: 40px;
  display: -ms-flexbox;  
  -ms-flex-align: center;
  align-items: center;  
  
}



html,
body {
  height: 100%;
}

body {
  display: -ms-flexbox;
  display: flex;
  -ms-flex-align: center;
  align-items: center;
  padding-top: 40px;
  padding-bottom: 40px;
  background-color: #f5f5f5;
}

.form-signin {
  width: 100%;
  max-width: 330px;
  padding: 15px;
  margin: auto;
}
.form-signin .checkbox {
  font-weight: 400;
}
.form-signin .form-control {
  position: relative;
  box-sizing: border-box;
  height: auto;
  padding: 10px;
  font-size: 16px;
}
.form-signin .form-control:focus {
  z-index: 2;
}
.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

</style>
</head>
<body>

<script src="https://apis.google.com/js/platform.js" async="async"></script>
<meta name="google-signin-client_id" content="183358413146-79of4mps144kc78dek9tq2994d2njfrh.apps.googleusercontent.com"/>
<div class="g-signin2" data-onsuccess="onSignIn"></div>

目前狀態：
<span id="GOOGLE_STATUS_1"></span>

<script>
// 登入之後
function onSignIn(googleUser) {
var profile = googleUser.getBasicProfile(),
target = document.getElementById("GOOGLE_STATUS_1"),
html = "";

html += "ID: " + profile.getId() + "<br/>";
html += "會員暱稱： " + profile.getName() + "<br/>";
html += "會員頭像：" + profile.getImageUrl() + "<br/>";
html += "會員 email：" + profile.getEmail() + "<br/>";
target.innerHTML = html;
}
</script>
</body>
</html>