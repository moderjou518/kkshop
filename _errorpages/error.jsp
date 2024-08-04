<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">   
    <title>登入成功</title>
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
<script>
function goHome(){
	location.reload();
}
// 1秒後自動執行
setInterval("goHome()", 1000);

//每隔3秒就執行一次函式
setTimeout("goHome()", 3000);   
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
</style>
</head>
<body class="d-flex flex-column h-100">
<body class="hms_style">  
  <div class='app_padding'>
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <c:import url="/_errorpages/errorHeader.jsp" />          
        </div>
      </div>
      <div class="row" >
          <div class='col-md-12'>
           <ol class="breadcrumb" style="margin-bottom: 10px;">
               <li class="breadcrumb-item">登入成功</li>                                             
           </ol>           
          </div>    
      </div>      
      
      <!-- 上半部選單  -->      
      <!-- layout Start page-01-->
      <div class="row">       
          
        <div class="col-md-12" id="hmsSlide_Content" >          
          <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;" id="result_dataList">                                   
            <!-- <div class='card-header text-large card-title'>登入成功</div> -->                                   
            <!-- 搜尋結果 Search Result 區塊 -->
            <div class='card-body' style="overflow-y:auto">                           
              	3秒後自動進入系統，頁面若沒反應，請<a href="#" onclick='location.reload()'>點這邊</a>進入
            </div>    
          </div>
          </div>    
      </div><!-- row -->
      <!-- page01 -->
      <!-- layout End -->      
    </div><!-- container -->    
  </div><!-- appsetting -->
</body>
</html>