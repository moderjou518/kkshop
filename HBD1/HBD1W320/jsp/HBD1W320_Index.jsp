<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*, org.apache.log4j.Logger" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%
Logger logger = Logger.getLogger("KKSHOP_JSP_LOG");
	logger.info("HBD1W320.jsp");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">   
    <title>現場平板統計出貨</title>
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
</style>
</head>
<!-- <body class="d-flex flex-column h-100"> -->
<body class="hms_style">
<div class='app_padding'>
    <div class="container-fluid">     
      <div class="row" >
          <div class='col-md-12'>
           <c:import url="/HBD1/common/jsp/menu.jsp" context="/wlhbmg" />
          </div>    
      </div>
      </div>
</div>  
<form>
<div class='card card-plus' style="margin-top: 0px"></div>
<div id="queryResultTop"></div>
<div id="bidListLink"></div>
<div class="row">
<div class="col-md-1"></div>
<div class="col-md-10">
	<div class='card card-plus' id="divQueryResult">                  
		<div class='card-header card-title text-large'>
			<table style="width:100%">
				<tr>
					<td nowrap>
						<input type="text" id="txtSelWeek" readonly class="form-control readonly-noBG" size="20" />						
						<input type="button" onclick="formEvent.createImage()" value="下載報表" />
					</td>
					<td style="text-align:left"><div class="btn-group" id="btnWeekDayGroup"></div></td>
					<td style="width:10%" nowrap><a href="#" onclick="formEvent.showChangeModal()">其他日期</a></td>
				</tr>
			</table>			
			<input type="hidden" id="txtPoDate" />						
		</div>                     
		<div class='card-body' id="capture">
			<table class='table-plus table-list width-fit-parent' id='tableMarkTime' style="width:100%">
				<tbody></tbody>
			</table>
			<table class='table-plus table-list width-fit-parent' id='tableQueryResult' style="width:100%">
				<tbody></tbody>
			</table>        
		</div>   
	</div>
</div>
<div class="col-md-1"></div>
</div>
<!-- 賣場每日商品 -->
<div id="bidItemListTop" style="height:0px"></div>
<div class="container" id="divBidItemList"></div>


<div class="container" id="divCartItemList"></div>

<div style="height:80px"></div	>
<div class="modal modal-plus fade" id="issueModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-md" style="width:100%;" role="document" style="height:500px">
<div class="modal-content">
	<div class="modal-header">
		<h5 class="modal-title" id="modalTitle">出貨維護</h5>
	</div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
	          <!-- <div class='card-header text-large card-title'>內容</div> -->                            
	          <div class='card-body' style="overflow-y:auto" id="loginForm" name="loginForm">
	          	<table id="poddQueryResult" class="table-plus table-list width-fit-parent" style="width:100%" >                            
	               	<thead>
	                 		<tr>
	                   		<th style="width:20%" nowrap="">商品名稱</th>                                        
	                   		<th style="width:20%; text-align:center">預訂</th>
	                   		<th style="text-align:center">
	                   			<a href="###" onclick="formEvent.IssueAll()" id="btnIssue">預訂提貨</a>                 			 
	                   		</th>
	                 		</tr>
	               	</thead>
	               	<tbody></tbody>
				</table>
				<input type="button" value="出貨存檔" class="btn btn-primary form-control" onclick="formEvent.saveIssue()" id="btnSaveIssue1" />
				<hr>
				<table id="poddAddQueryResult" class="table-plus table-list width-fit-parent" style="width:100%" >                            
	               	<thead>
	                 		<tr>
	                   		<th style="width:20%" nowrap="">加購商品</th>                                        
	                   		<th style="width:20%; text-align:center">預訂</th>
	                   		<th style="text-align:center">現場提貨</th>
	                 		</tr>
	               	</thead>
	               	<tbody></tbody>
				</table>	
				<input type="button" value="出貨存檔" class="btn btn-primary form-control" onclick="formEvent.saveIssue()" id="btnSaveIssue2" />
				<input type="button" value="取消" class="btn btn-default form-control" data-dismiss="modal" id="btnCloseIssue" />	          		
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>
		
		<input type="hidden" name="txtIssueBidmUUID" id="txtIssueBidmUUID" />
		<input type="hidden" name="txtIssuePoDate" id="txtIssuePoDate" />
		<input type="hidden" name="txtIssuePoSeq" id="txtIssuePoSeq" />
		<input type="hidden" name="txtIssueBuyer" id="txtIssueBuyer" />
		<br/>
		      
	</div>             
</div>
</div>
</div><!-- issue modal -->

<!--  change week modal -->
<div class="modal modal-plus fade" id="changeModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-md" style="width:100%;" role="document" style="height:500px">
<div class="modal-content">
<div class="modal-header">
	<h5 class="modal-title" id="modalTitle">選擇其他周</h5>
</div>        
<div class="modal-body" style='width:100%;'>
    <div class="row">      
      <div class="col-md-12">
        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
          <div class='card-header text-large card-title'>
          	年度：<select id="txtYYYY" name="txtYYYY">
          		<option value="2022">111</option>
          		<option value="2021">110</option>				
            	<option value="2020">109</option>            	
            </select>
            <div class="btn-group">
				<button type="button" name="btnQuickMonth" data-po-date="01" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				一月</button>		      						      
				<button type="button" name="btnQuickMonth" data-po-date="02" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				二月</button>
				<button type="button" name="btnQuickMonth" data-po-date="03" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				三月</button>
				<button type="button" name="btnQuickMonth" data-po-date="04" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				四月</button>
				<button type="button" name="btnQuickMonth" data-po-date="05" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				五月</button>
				<button type="button" name="btnQuickMonth" data-po-date="06" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				六月</button>
			</div>
			<div class="btn-group">
				<button type="button" name="btnQuickMonth" data-po-date="07" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				七月</button>		      						      
				<button type="button" name="btnQuickMonth" data-po-date="08" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				八月</button>
				<button type="button" name="btnQuickMonth" data-po-date="09" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				九月</button>
				<button type="button" name="btnQuickMonth" data-po-date="10" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				十月</button>
				<button type="button" name="btnQuickMonth" data-po-date="11" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				十一</button>
				<button type="button" name="btnQuickMonth" data-po-date="12" class="btn btn-default" onclick="formEvent.changeQuery(this)">
				十二</button>
			</div>
          </div>                            
          <div class='card-body' style="overflow-y:auto">
          	<table id="weekList" class="table-plus table-list width-fit-parent" style="width:100%" >                            
               	<thead>
                 	<tr>
                 		<th style="width:10%" nowrap></th>                   		                                        
                   		<th nowrap>日期</th>
                   		<th style="width:10%"></th>
                 	</tr>
               	</thead>
               	<tbody></tbody>
			</table>	          		
          </div>        
        </div>              
      </div>      
    </div>  
</div>  
</div>
</div>
</div><!-- change modal -->
</form>
<c:import url="/_comm/jsp/hms.js.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>" context="/wlhbmg" ></c:import>
<c:import url="/HBD1/HBD1W320/jsp/HBD1W320_Template.jsp" context="/wlhbmg"></c:import>
<script src='/HBD1/HBD1W320/js/HBD1W320_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/_util_lib/html2canvas/html2canvas.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>
</body>
</html>