<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*, org.apache.log4j.Logger" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%
	String tabKey = UTIL_String.nvl(request.getParameter("tabKey"), "#");
	String cID = (String) request.getSession().getAttribute("COMP_ID");
	String actionUrl = "shop.go?tabKey=" + tabKey + "&webactionID=HBD1W020";
	
	Logger logger = Logger.getLogger("KKSHOP_JSP_LOG");
	logger.info("HBD1W020.jsp");
	
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">   
    <title>客戶每周預訂單列表</title>
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
		
    <style>
            
html,
body {
  height: 100%;
  font-size: 1rem;
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
<!-- <body class="d-flex flex-column h-100"> -->
<body class="hms_style">
<c:import url="/HBD1/HBD1W020/common/jsp/menu.jsp" />  
<form>
<div class='card card-plus' style="margin-top: 0px"></div>
<div id="queryResultTop"></div>
<div id="bidListLink"></div>
<div class="col-md-12">
	<div class='card card-plus' id="divQueryResult">                  
		<div class='card-header card-title text-large'>查詢訂單</div>                     
		<div class='card-body'>       
			<table class='table table-list' id='tableQueryResult' style="width:100%">
				<!-- 
				<thead>
					<tr>
						<th nowrap>日期區間</th>						
						<th nowrap style="text-align:right">周結金額</th>
						<th nowrap style="text-align:right">已匯款</th>
						<th style="text-align:center"></th>						
					</tr>
				</thead>
				 -->	        
		        <tbody></tbody>
			</table>        
		</div>   
	</div>
</div>
	  		
<div style="height:80px"></div	>

<!-- 明細modal -->
<div class="modal modal-plus fade" id="detailModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-md" style="width:100%;" role="document" style="height:500px">
<div class="modal-content">
	<div class="modal-header"><h4 class="modal-title">每周訂單出貨明細</h4></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
	          <!-- <div class='card-header text-large card-title'>內容</div> -->                            
	          <div class='card-body' style="overflow-y:auto" id="capture">
	          	<table class='table' id='tableMarkTime' style="width:100%">
	          	<tbody></tbody>
	          	</table>          	
				<table class='table' id='tblWeekPOList' style="width:100%">
					<thead>
						<tr>
							<th colspan="2" class="text-right">日期 商品</th>
							<th style="text-align:right">單價</th>
							<th style="text-align:center">訂</th>
							<th style="text-align:center">出</th>							
							<th style="text-align:right">金額</th>
						</tr>
					</thead>	        
			        <tbody></tbody>
			        <tfoot></tfoot>
				</table>				  
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>		          
		<input type="button" value="離開" class="btn btn-primary form-control" data-dismiss="modal" />		      
	</div>             
</div>
</div>
</div>

<!-- 匯款modal -->
<div class="modal modal-plus fade" id="moneyModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-md" style="width:100%;" role="document" style="height:500px">
<div class="modal-content">
	<div class="modal-header"><h4 class="modal-title">匯款資訊</h4></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
	          <!-- <div class='card-header text-large card-title'>內容</div> -->                            
	          <div class='card-body' style="overflow-y:auto" id="moneyForm" name="moneyForm">	          	
				  <div class="form-group">
				    <label for="formGroupExampleInput">匯款金額</label>
				    <input type="number" class="form-control" id="txtAmount" name="txtAmount" required maxlength="6" value="" />
				  </div>
				  <div class="form-group">
				    <label for="formGroupExampleInput2">匯款日期</label>
				    <input type="text" class="form-control" id="txtDate" name="txtDate" required maxlength="8" />
				  </div>				        	
				  <div class="form-group" style="display:none">
				    <label for="formGroupExampleInput2">帳號末5碼</label>
				    <input type="hidden" class="form-control" id="txtAccount" name="txtAccount" required maxlength="5" />
				    <input type="hidden" id="txtBidmUUID" name="txtBidmUUID" />
				  </div>
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>		          
		<input type="button" value="儲存" class="btn btn-primary form-control" onclick="formEvent.submitSaveMoney()" /><br/>
		<input type="button" value="取消" class="btn btn-default form-control" data-dismiss="modal" />		      
	</div>             
</div>
</div>
</div>
</form>
<c:import url="/HBD1/common/jsp/cus_footer.jsp" />
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>
<script src='/HBD1/HBD1W020/js/HBD1W020_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>
<script src='/_util_lib/html2canvas/html2canvas.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>
<c:import url="/HBD1/HBD1W020/jsp/HBD1W020_Template.jsp" context="/wlhbmg"></c:import>
</body>
</html>