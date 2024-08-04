<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%

%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">   
    <title>test</title>
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
<!-- <body class="d-flex flex-column h-100"> -->
<body class="hms_style">
<form class="form-signin" id="loginForm" action="/shop.go?webactionID=HBD1LOGIN&actionMethod=jr" method="post">
  <label for="txtLoginID" class="sr-only">手機號碼</label>
  <input type="number" id="txtLoginID" name="txtLoginID" class="form-control" placeholder="輸入手機號碼" required="" autofocus="" maxlength="10" autocomplete="off">
  <label for="txtPassword" class="sr-only">Password</label>
  <input type="password" id="txtPassword" name="txtPassword" class="form-control" placeholder="請輸入密碼" required="" maxlength="20"  autocomplete="off">
  <label for="txtCompCode" class="sr-only">商家代碼</label>
  
  <input type="hidden" id="txtCompCode" name="txtCompCode" class="readonly-noBG form-control" readonly placeholder="商家代碼" value="1122334455" />
  <div class="checkbox mb-3" style="display:none">
    <label>
      <input type="checkbox" value="remember-me"> 記住我的手機號碼
    </label>
  </div>
  <button id="btnSubmitLogin" class="btn btn-lg btn-primary btn-block" type="button" onclick="doLogin()">登入</button>
  <br/>
  <p class="mt-5 mb-3 text-muted">© 2020 牙語資訊</p>
</form>  


<c:import url="/HBD1/HBD1TEST/jsp/HBD1TEST_Template.jsp"></c:import>
<script src='/HBD1/HBD1TEST/js/HBD1TEST_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>
</body>
</html>