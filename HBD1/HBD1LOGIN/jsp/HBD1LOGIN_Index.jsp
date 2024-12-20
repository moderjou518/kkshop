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
	
	Logger logger = Logger.getLogger("STDOUT");
	logger.debug(" ~~ IN Login JSP ~~ ");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">   
    <title>呆呆雞肉-登入系統</title>
    <c:import url="/_ui/jsp/util/lib.js.jsp" >
		<c:param name='esapi4js' value='0.1.3' />
		<c:param name='jq' value='3.1.0' />
		<c:param name='ui' value='1.12.1' />
		<c:param name="popper" value="1.14.6" />
		<c:param name='bs' value='4.3.1' />		
		<c:param name='handlebars' value='4.0.5' />
		<c:param name='chosen' value='1.6.2' />			    		
	</c:import>		
	<c:import url='/_ui/jsp/util/lib.css.jsp'>
		<c:param name="bs" value="4.3.1" />   
		<c:param name="ui" value="1.12.1" />
		<c:param name="chosen" value="1.6.2" />
	</c:import>
	<c:import url="/_ui/jsp/basic.js.jsp" ></c:import>
	<c:import url="/_ui/jsp/basic.css.jsp" >
		<c:param name="basic" value="3.0.0" />   
  		<c:param name="fa" value="5.7.2" />
	</c:import>
	<c:import url="/_ui/jsp/util/date.js.jsp"></c:import>
	<c:import url="/_ui/jsp/util/grid.js.jsp?v=2" ></c:import>
	<c:import url="/_comm/jsp/hms.css.jsp?ver=2.0.0"  ></c:import>
	<c:import url="/_ui/jsp/util/validator.js.jsp" ></c:import>
		
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
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'G-043BBX22SH');
</script>
<script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-9352666354860569"
     crossorigin="anonymous"></script>
<!-- <body class="d-flex flex-column h-100"> -->
<body class="hms_style">
<form class="form-signin" id="loginForm" action="/shop.go?webactionID=HBD1LOGIN&actionMethod=jr" method="post">
  <div style="text-align:center"> <img class="mb-4" src="/HBD1/images/logoText.png" width="100%" height="100%">
  <!--  
  <a href="###" onclick="javascript:$('#txtLoginID').val('0900333333');$('#txtPassword').val('123456');">家成</a> |  
  <a href="###" onclick="javascript:$('#txtLoginID').val('0900555555');$('#txtPassword').val('123456');">阿志</a> |
  <a href="###" onclick="javascript:$('#txtLoginID').val('0900111111');$('#txtPassword').val('123456');">文洲</a> |
  <a href="###" onclick="javascript:$('#txtLoginID').val('0900222222');$('#txtPassword').val('123456');">金龍</a> |
  <a href="###" onclick="javascript:$('#txtLoginID').val('0956565656');$('#txtPassword').val('123456');">小潘</a> |
  <a href="###" onclick="javascript:$('#txtLoginID').val('0912121212');$('#txtPassword').val('123456');">紅毛</a> |
  <a href="###" onclick="javascript:$('#txtLoginID').val('0934343434');$('#txtPassword').val('123456');">忠霖</a> |
  <a href="###" onclick="javascript:$('#txtLoginID').val('0900666666');$('#txtPassword').val('123456');">維漢</a>
  -->
   
  </div>
  
  <label for="txtLoginID" class="sr-only">手機號碼</label>
  <input type="number" id="txtLoginID" name="txtLoginID" class="form-control" placeholder="輸入手機號碼" required="" autofocus="" maxlength="10" autocomplete="off">
  <label for="txtPassword" class="sr-only">Password</label>
  <input type="password" id="txtPassword" name="txtPassword" class="form-control" placeholder="請輸入密碼" required="" maxlength="20"  autocomplete="off">
  <label for="txtCompCode" class="sr-only">商家代碼</label>
  <div style="text-align:center">
  <!-- 
  <a href="###" onclick="javascript:$('#txtLoginID').val('0900123123');$('#txtPassword').val('123456');">現場出貨</a> |
  <a href="###" onclick="javascript:$('#txtLoginID').val('0900789789');$('#txtPassword').val('123456');">管理者</a>
   -->
  </div>
  <input type="hidden" id="txtCompCode" name="txtCompCode" class="readonly-noBG form-control" readonly placeholder="商家代碼" value="1122334455" />
  <div class="checkbox mb-3" style="display:none">
    <label>
      <input type="checkbox" value="remember-me"> 記住我的手機號碼
    </label>
  </div>
  <button id="btnSubmitLogin" class="btn btn-lg btn-primary btn-block" type="button" onclick="doLogin()">登入</button>
  <br/>
  <p class="mt-5 mb-3 text-muted">© 2021 牙語資訊</p>
</form>  


<c:import url="/HBD1/HBD1LOGIN/jsp/HBD1LOGIN_Template.jsp"></c:import>
<script src='/HBD1/HBD1LOGIN/js/HBD1LOGIN_Index.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>
</body>
</html>