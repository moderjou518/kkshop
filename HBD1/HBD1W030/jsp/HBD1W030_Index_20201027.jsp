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
    <title>會員資料</title>
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
<!-- <body class="d-flex flex-column h-100"> -->
<body class="hms_style">
<c:import url="/HBD1/HBD1W030/common/jsp/menu.jsp" />  
<form>
<div class='card card-plus' style="margin-top: 0px"></div>
<div class="col-md-12">
	<div class='card card-plus' id="divQueryResult">                  
		<div class='card-header card-title text-large'>會員資料</div>                     
		<div class='card-body'>       
				    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
	          <!-- <div class='card-header text-large card-title'>內容</div> -->                            
	          <div class='card-body'>
	          	<div class="row">
				    <div class="col-md-12 order-md-1">
				      <!-- <h4 class="mb-3">Billing address</h4> -->				      				        
				        <div class="row">
				          <div class="col-md-6 mb-3">
				            <label for="txtHascBascLoginId">登入手機</label>
				          	<input type="text" class="form-control readonly-noBG" readonly id="txtHascBascLoginId" name="txtHascBascLoginId" maxlength="10" autocomplete="off">
				          	<input type="hidden" id="txtHac0BascAcCode" name="txtHac0BascAcCode" />
				          		          
				          </div>
				          <div class="col-md-6 mb-3">
				            <label for="txtHac0BascName">姓名</label>
				          	<input type="text" class="form-control readonly-noBG" readonly id="txtHac0BascName" name="txtHac0BascName" required="" autocomplete="off">				            
				          </div>				          
				        </div>
				        
				        <div class="row">
				          <div class="col-md-6 mb-3">
				            <label for="txtPassword">密碼</label>
				          	<input type="text" class="form-control" id="txtHac0BascPwd" name="txtHac0BascPwd" maxlength="20" autocomplete="off">				            
				          </div>
				          <div class="col-md-6 mb-3">
				            <label for="txtNewPassword">變更密碼</label>
				          	<input type="text" class="form-control" id="txtNewPassword" name="txtNewPassword" maxlength="20" autocomplete="off">				            
				          </div>
				        </div>
				
				        <div class="row">				          
				          <div class="col-md-6 mb-3">
				            <label for="txtHac0BascNote">備註</label>
				          	<input type="text" class="form-control" id="txtHac0BascNote" name="txtHac0BascNote" maxlength="20" autocomplete="off">				            
				          </div>
				        </div>
				    </div>
				  </div>
	          </div>    
	          <div class="modal-footer" style='width:99%;text-align:center;'>					
					<button id="btnSaveOne" class="btn btn-primary btn-lg btn-block" onclick="formEvent.submitSaveOne()" type="button">存檔</button>			      
				</div>    
	        </div>              
	      </div>      
	    </div>    
		</div>   
	</div>
</div>
	  		
<!-- 賣場每日商品 -->
<div id="bidItemListTop" style="height:0px"></div>
<div class="container" id="divBidItemList"></div>

<!-- 購物車內的商品 -->
<div id="cartItemListTop" style="height:0px"></div> 
<div class="container" id="divCartItemList"></div>

<div style="height:80px"></div	>

<!-- 明細modal -->
<div class="modal modal-plus fade" id="detailModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-md" style="width:100%;" role="document" style="height:500px">
<div class="modal-content">
	<div class="modal-header"><h4 class="modal-title">每周訂單明細</h4></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
	          <!-- <div class='card-header text-large card-title'>內容</div> -->                            
	          <div class='card-body' style="overflow-y:auto">          	
				<table class='table' id='tblWeekPOList' style="width:100%">
					<thead>
						<tr>
							<th>日期</th>
							<th>商品</th>
							<th style="text-align:right">數量</th>
							<th style="text-align:center">單價</th>
							<th style="text-align:center">金額</th>
						</tr>
					</thead>	        
			        <tbody></tbody>
				</table>				  
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>		          
		<input type="button" value="離開" class="btn btn-plus btn-default" data-dismiss="modal" />		      
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
				    <input type="text" class="form-control" id="txtAmount" name="txtAmount" required maxlength="6" />
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
		<input type="button" value="儲存" class="btn btn-plus btn-primary" onclick="formEvent.submitSaveMoney()" /><br/>
		<input type="button" value="取消" class="btn btn-plus btn-default" data-dismiss="modal" />		      
	</div>             
</div>
</div>
</div>


</form>
<c:import url="/HBD1/common/jsp/cus_footer.jsp" />
<c:import url="/HBD1/HBD1W030/jsp/HBD1W030_Template.jsp" context="/wlhbmg"></c:import>
<script src='/HBD1/HBD1W030/js/HBD1W030_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>
</body>
</html>