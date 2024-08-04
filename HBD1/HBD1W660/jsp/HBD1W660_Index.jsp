<%@page contentType="text/html;charset=utf-8" language="java" import="java.util.*,java.lang.*,java.text.*,java.sql.*,java.text.*,java.math.*,com.evergreen.web.security.*"%>
<%@page import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%
	
	
	//System.out.println("jsp compCode: " + compCode);
	//System.out.println("jsp compName: " + compName);
%>
<html>
<head>
	<title>HBD1W660-每日訂單管理</title>
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
	<c:import url="/_ui/jsp/basic.js.jsp"></c:import>
	<c:import url="/_ui/jsp/basic.css.jsp">
		<c:param name="basic" value="3.0.0" />   
  		<c:param name="fa" value="5.7.2" />
	</c:import>
	<c:import url="/_ui/jsp/util/money.js.jsp">
    	<c:param name="sep" value="," />
    	<c:param name="point" value="." />
    	<c:param name="validate" value="Y" />
    	<c:param name="serial" value="Y" />
	</c:import> 
	<c:import url="/_ui/jsp/util/date.js.jsp"></c:import>
	<c:import url="/_ui/jsp/util/grid.js.jsp?v=2"></c:import>
	<c:import url="/_comm/jsp/hms.css.jsp?ver=2.0.0" ></c:import>
	<c:import url="/_ui/jsp/util/validator.js.jsp"></c:import>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-N8SMN6LXBV"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'G-N8SMN6LXBV');
</script>		
</head>
<body class="hms_style">  
  <div class='app_padding'>
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <c:import url="/HBD1/common/jsp/menu.jsp" />          
        </div>
      </div>
      <div class="row" >
          <div class='col-md-12'>
           <ol class="breadcrumb" style="margin-bottom: 10px;">
               <li class="breadcrumb-item">管理者</li>
               <li class="breadcrumb-item">每日訂單管理</li>                              
           </ol>           
          </div>    
      </div>      
      
      <!-- 上半部選單  -->      
      <!-- layout Start page-01-->
      <div class="row">
        <!-- 左邊 sidebar
        <div title="Expand" id="divOpenBarBtn" class="slideMenu_openBtn"  onclick="HMS2_SlideMenu.slideMenuOpen()"><span class="fa fa-bars"></span></div> -->        
        <div class="col-md-2" id="hmsSlide_Menu">
          <div class="row" >                    
              <div class="col-md-12">
                <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
                  <div class='card-header text-large card-title' style="display:none">
                    	快速搜尋                    
                      <div class="btn btn-plus bar-head bar-dark-grey slideMenu_closeBtn" onclick="HMS2_SlideMenu.slideMenuClose('Y')">                        
                        	<i id="hmsSlider_collapseBtn" class="fas fa-angle-double-left"></i>
                      </div> 
                  </div>      
                                
                  <div class='card-body' id="" name="">
	                  <form name="qryForm" id="qryForm">
    	              	<div class="form-row">
        	            <!-- Quick Query 區塊, 預設抓全部資料 -->
                    		<div class="form-group">
    						<label for="qryPoDate1">訂單日期</label>    					
    						<input type="text" id="qryPoDate1" name="qryPoDate1" class="form-control" style="text-align:center" maxlength="8" placeholder="yyyyMMdd"  />    						
    						<!-- <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small> -->
  							</div>
  						<button type="button" id="btnQuery" class="btn btn-primary form-control">查詢</button>
                    	</div>                    	
                  	</form>
                    
                  </div>   
                </div>
              </div>                                  
            </div><!-- row end -->
                              

        </div><!-- col-md-3  -->
        
        
        <!-- 右邊 Search result -->  
        <div class="col-md-10" id="hmsSlide_Content" >
          <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;" id="result_dataMast">                                   
            <div class='card-header text-large card-title'>
            	當日訂單
            </div>            
            <div class='card-body'>                                        
              <table id='table_queryMasterResult' class='table-plus table-list'>                            
                <thead>
                  <tr>                 	                          
                  	<th nowrap>日期</th>
                    <th style="text-align:right" nowrap>訂單總金額</th>
                    <th style="text-align:right" nowrap>已出貨總金額</th>                    
                    <th style="text-align:right" nowrap>差額</th>                    
                  </tr>
                </thead>
                <tbody>
                 <tr>
                 	<td></td>
                 	<td style="text-align:right"><span id="txtOrdAmtSum" class="money"></span></td>
                    <td style="text-align:right" nowrap><span id="txtRcvdAmtSum" class="money"></span></td>                    
                    <td style="text-align:right" nowrap><span id="txtDiffAmt"></span></td>
                 </tr>
                </tbody>
              </table>
            </div>    
          </div>
                    
          <!-- 查詢結果  -->
          <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;" id="result_dataList">                                   
            <div class='card-header text-large card-title'>
            	查詢結果<!-- 
              <div class='row'>
                <div class="col-md-6"></div>
                <div class="col-md-6" style="text-align:right">                  
                	<input id="btnAddReciItem" value="Add Recipe Item" class="btn btn-default" onclick="modalEvent.showRecipeItemModal()" type="button"  />
                </div>
              </div> -->
            </div>            
            
            <!-- 搜尋結果 Search Result 區塊 -->
            <div class='card-body' style="overflow-y:auto; height:600px">                           
              <!-- 搜尋結果 Search Result 區塊 -->
              <table id='table_queryResult' class='table-plus table-list'>                            
                <thead>
                  <tr>                 
                  	<th class="text-center">訂單日期</th> 	
                    <th class="text-center" nowrap>會員</th>                    
                    <th class="text-right" nowrap>訂單金額</th>
                    <th class="text-right" nowrap>已出貨金額</th>
                    <!-- <th style="width:15%; text-align:right" nowrap>狀態</th> -->                                        
                    <th style="text-align:center">明細</th>
                  </tr>
                </thead>
                <tbody></tbody>
              </table>
            </div>    
          </div>
          </div>    
      </div><!-- row -->
      <!-- page01 -->
      <!-- layout End -->      
    </div><!-- container -->    
  </div><!-- appsetting -->	
  
  
<!-- 明細modal -->
<div class="modal modal-plus fade" id="detailModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-lg" style="width:100%;" role="document" style="height:500px">
<div class="modal-content">
	<div class="modal-header" id="poMastForm">
		<h4 class="modal-title">
		<input type="text" id="txtDetailModalTitle" size="15" readonly class="readonly-noBG" />訂單明細		 
		<input type="hidden" id="txtPommBuyer" name="txtPommBuyer" />
		<input type="hidden" id="txtPoDate" name="txtPoDate" />
		<input type="hidden" id="txtPoSeq" name="txtPoSeq" />
		</h4>		
	</div>        
	<div class="modal-body" style='width:100%;'>
		<table class='table' id='tblWeekPOList' style="width:100%">
					<thead>
						<tr>
							<!-- <th style="vertical-align:middle">日期</th> -->
							<th style="text-align:right;width:30%">商品</th>
							<th style="text-align:right;width:10%" nowrap>訂購</th>
							<th style="text-align:right;width:10%" nowrap>出貨</th>
							<th style="text-align:right;width:1%" nowrap>單價</th>
							<th style="text-align:right;width:1%" nowrap>金額</th>							
							<th style="text-align:left"  nowrap>修改資訊</th>
						</tr>						
					</thead>	        
			        <tbody></tbody>
				</table>				  
				<table class='table' id='tblAppendPOList' style="width:100%" class="table-plus">
					<thead>
					<tr class="bg-warning">
							<th style="text-align:right;;width:30%">追加商品</th>							
							<th style="text-align:right;width:20%" nowrap colspan="2"><b>追加出貨</b></th>
							<th style="text-align:right;width:1%" nowrap colspan="2">單價</th>							
							<th></th>							
						</tr>						
					</thead>	        
			        <tbody></tbody>
				</table>	      
	</div><!-- modal body -->
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>
		<input type="button" id="btnSave" value="存檔" class="btn btn-primary form-control" onclick="formEvent.btnSave_Click()" />		          
		<input type="button" value="離開" class="btn btn-default form-control" data-dismiss="modal" />		      
	</div>             
</div>
</div>
</div>

<!-- PO 追加 
<div class="modal modal-plus fade" id="poAppendModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-lg" style="width:100%;" role="document" style="height:500px">
<div class="modal-content">
	<div class="modal-header">
		<h4 class="modal-title">
		<input type="text" size="15" readonly class="readonly-noBG" />訂單追加				
		</h4>		
	</div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">	                                      
	          <div class='card-body' style="overflow-y:auto">          	
				<table class='table' id='tblWeekPOAddList' style="width:100%">
					<thead>
						<tr>							
							<th style="text-align:right">商品</th>							
							<th style="text-align:right">出貨</th>
							<th style="text-align:right">單價</th>														
						</tr>						
					</thead>	        
			        <tbody></tbody>
				</table>				  
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div>
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>
		<input type="button" value="追加" class="btn btn-primary form-control" onclick="formEvent.btnSavedAppend_Click()" />		          
		<input type="button" value="取消" class="btn btn-default form-control" data-dismiss="modal" />		      
	</div>             
</div>
</div>
</div>
-->
  
<c:import url="/_comm/jsp/hms.js.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>" ></c:import>		
<script src='/HBD1/HBD1W660/js/HBD1W660_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>			
<c:import url="/HBD1/HBD1W660/jsp/HBD1W660_Template.jsp"></c:import>		
</body>	
</html>
