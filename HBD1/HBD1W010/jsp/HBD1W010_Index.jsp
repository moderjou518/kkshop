<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*, org.apache.log4j.Logger" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%
	String tabKey = UTIL_String.nvl(request.getParameter("tabKey"), "#");
	String cID = (String) request.getSession().getAttribute("COMP_ID");
	String actionUrl = "shop.go?tabKey=" + tabKey + "&webactionID=HBD1W010";
	
	Logger logger = Logger.getLogger("KKSHOP_JSP_LOG");
	logger.info("HBD1W010.jsp");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">   
    <title>HBD1W010-每周預訂單</title>
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
	<c:import url="/_ui/jsp/basic.js.jsp" context="/wlhbmg"></c:import>
	<c:import url="/_ui/jsp/basic.css.jsp" context="/wlhbmg">
		<c:param name="basic" value="3.0.0" />   
  		<c:param name="fa" value="5.7.2" />
	</c:import>
	<c:import url="/_ui/jsp/util/date.js.jsp"></c:import>
	<c:import url="/_ui/jsp/util/grid.js.jsp?v=2" context="/wlhbmg"></c:import>
	<c:import url="/_comm/jsp/hms.css.jsp?ver=2.0.0" context="/wlhbmg" ></c:import>
	<c:import url="/_ui/jsp/util/validator.js.jsp" context="/wlhbmg"></c:import>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-N8SMN6LXBV"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'G-N8SMN6LXBV');
</script>	
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

.sticky {
    position: sticky;
    top: 0;
}

table {
    border-collapse: collapse; /* 這將會消除預設的 table 邊框間距 */
}

td {
    padding: 0; /* 這將會消除 td 內的間距 */
}

.flex-container {
    display: flex;
    align-items: stretch;
}
.flex-container > * {
    align-self: center;
}

input{
	font-weight: bold
}
</style>
</head>
<!-- <body class="d-flex flex-column h-100"> -->
<body class="hms_style">
<c:import url="/HBD1/HBD1W010/common/jsp/menu.jsp" />  
<form>
<div class='card card-plus' style="margin-top: 0px"></div>
<div id="queryResultTop"></div>
<div id="bidListLink"></div>
<div class="col-md-12">
	<div class='card card-plus' id="divQueryResult">                  
		<div class='card-header card-title text-large'>預訂單</div>                     
		<div class='card-body'>       
			<table class='table' id='tableQueryResult' style="width:100%">	        
		        <tbody></tbody>
		      </table>        
		</div>   
	</div>
</div>
	  		
<!-- 賣場每日商品 -->
<div id="bidItemListTop" style="height:0px"></div>
<div class="container" id="divBidItemList"></div>

<!-- 購物車內的商品 -->
<div id="cartItemListTop" style="height:0px"></div> 
<div class="container" id="divCartItemList"></div>
<div class="text-center" id="btnSaveOK" style="display:none">
	<div style="height:50px"></div>	
	<i class="far fa-smile" style="zoom:200%">
	<p></p>預訂成功<p></p>感謝訂購</i>
	<button class="btn btn-lg btn-block"></button>	
</div>
<div style="height:80px"></div	>
</form>
<c:import url="/HBD1/common/jsp/cus_footer.jsp" />
<c:import url="/HBD1/HBD1W010/jsp/HBD1W010_Template.jsp" context="/wlhbmg"></c:import>
<script src='/HBD1/HBD1W010/js/HBD1W010_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>
</body>
</html>