﻿
<%@page contentType="text/html;charset=utf-8" language="java" import="java.util.*,java.lang.*,java.text.*,java.sql.*,java.text.*,java.math.*,com.evergreen.web.security.*"%>
<%@page import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*, org.apache.log4j.Logger" %>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%
	String compCode = (String) request.getSession().getAttribute("COMP_CODE");
	String compName = (String) request.getSession().getAttribute("COMP_NAME");
	
	Logger logger = Logger.getLogger("KKSHOP_JSP_LOG");
	logger.info("HBD1W330.jsp");
%>
<html>
<head>
	<title>HBD1W330 - 成品製作</title>
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
<style>
            
html,
body {
  height: 100%;
  font-size: 1.1rem;
}
</style>	
</head>
<body class="hms_style">  
  <div class='app_padding'>
    <div class="container-fluid">
      <div class="row" >
          <div class='col-md-12'>           
           <c:import url="/HBD1/common/jsp/menu.jsp" context="/wlhbmg" />
          </div>    
      </div>
      
      
      <!-- 上半部選單  -->      
      <!-- layout Start page-01-->
      <div class="row">
        <!-- 左邊 sidebar
        
                
        <div class="col-md-2" id="hmsSlide_Menu">
          <div class="row" >                    
              <div class="col-md-12">
                <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
                  <div class='card-header text-large card-title'>
                    	查詢資料                    
                      <div class="btn btn-plus bar-head bar-dark-grey slideMenu_closeBtn" onclick="HMS2_SlideMenu.slideMenuClose('Y')">                        
                        	<i id="hmsSlider_collapseBtn" class="fas fa-angle-double-left"></i>
                      </div> 
                  </div>      
                                
                  <div class='card-body'>
                    <form name="qryForm" id="qryForm">
    	              	<div class="form-row">        	            
                    		<div class="form-group">
    						<label for="qryTrnsDate">日期區間</label>    					
    						<input type="text" id="qryTrnsDate1" name="qryTrnsDate1" class="form-control" style="width:100%;text-align:center" maxlength="8" placeholder="yyyyMMdd">   						
    						<input type="text" id="qryTrnsDate2" name="qryTrnsDate2" class="form-control" style="width:100%;text-align:center" maxlength="8" placeholder="yyyyMMdd">
    						
  						</div>
                    	</div>
                    	<button type="button" id="btnQuery" class="btn btn-primary form-control" onclick="formEvent.queryData()">查詢</button>
                  	</form>                  
                  </div>   
                </div>
              </div>                                  
            </div>          
        </div><
         -->
        
        
        <!-- 右邊 Search result -->  
        <div class="col-md-12" id="hmsSlide_Content" >          
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
            <div id='div_btn_codesetting_list' class='card-func page-line' data-evg-page-target='table_queryResult'>
                <div class='text-left' style='width: 60%; float: left;'>
                  <button type="button" id="btnOpenQuery"    title="查詢資料" onclick="formEvent.openQuery()" class="btn btn-plus-icon btn-primary" ><i class="fas fa-search"></i></button>
                  <button type="button" id="btnAdd"    title="新增資料" onclick="formEvent.addAData()" class="btn btn-plus-icon btn-default" ><i class="fas fa-plus"></i></button>                                                     
                </div>
                <div class='text-right' style='width: 40%; float: right;'>                </div>
            </div>            
            <!-- 搜尋結果 Search Result 區塊 -->
            <div class='card-body' style="overflow-y:auto; height:600px">                           
              <!-- 搜尋結果 Search Result 區塊 -->
              <table id='table_queryResult' class='table-plus table-list width-fit-parent' style="width:100%">                            
                <thead>
                  <tr>                  	
                    <th style="text-align:center; width:1%" nowrap>日期單號</th>                                       
                    <th nowrap style="text-align:right; width:1%">名稱</th>               
                    <th nowrap style="text-align:right; width:1%">製作數量</th>
                    <th nowrap style="text-align:left; width:1%">單位</th>
                    <th nowrap style="text-align:left; width:1%">備註</th>
                    <th nowrap style="text-align:left">修改</th>                    
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


<!-- 搜尋條件 -->
<div class="modal modal-plus fade" id="showSearchModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-lg" style="width:100%;" role="document">
<div class="modal-content">
	<div class="modal-header"><h4 class="modal-title">成品製作</h4></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">	                                      
	          <div class='card-body'>
	          	<div class="row">
	          		<div class="col-md-12">
                <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
                  <!-- <div class='card-header text-large card-title'>查詢資料 </div> -->      
                                
                  <div class='card-body'>
                    <form name="qryForm" id="qryForm">
                    	<div class="row">
				          <div class="col-md-6 mb-3">
				            <label for="qryTrnsDate1">生產起日</label>
				          	<input type="text" class="form-control" id="qryTrnsDate1" name="qryTrnsDate1" maxlength="8" autocomplete="off">				            
				          </div>
				          <div class="col-md-6 mb-3">
				            <label for="qryTrnsDate2">生產起日</label>
				          	<input type="text" class="form-control" id="qryTrnsDate2" name="qryTrnsDate2" maxlength="8" autocomplete="off">				            
				          </div>				        
				        </div>
				        <!--				        
    	              	<div class="form-row">        	            
                    		<div class="form-group">
    						<label for="qryTrnsDate">日期區間</label>    					
    						<input type="text" id="qryTrnsDate1" name="qryTrnsDate1" class="form-control" style="width:45%;text-align:center" maxlength="8" placeholder="yyyyMMdd">   						
    						<input type="text" id="qryTrnsDate2" name="qryTrnsDate2" class="form-control" style="width:45%;text-align:center" maxlength="8" placeholder="yyyyMMdd">
    						
  						</div>
                    	</div>
                    	-->                    	
                  	</form>                  
                  </div>   
                </div>
              </div>  				    
				</div>
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>
		<button type="button" id="btnQuery" class="btn btn-primary form-control" onclick="formEvent.queryData()">查詢</button>
      
	</div>             
</div>
</div>
</div>


<!-- 成品製作開窗 -->
<div class="modal modal-plus fade" id="showDataModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-lg" style="width:100%;" role="document">
<div class="modal-content">
	<div class="modal-header"><h4 class="modal-title"><input type="text" id="txtTrnsDate" name="txtTrnsDate" readonly class="readonly-noBG" size="8" />-成品生產</h4></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
	          <!-- <div class='card-header text-large card-title'>內容</div> -->                            
	          <div class='card-body'>
	          	<div class="row">
				    <div class="col-md-12 order-md-1">
				      <!-- <h4 class="mb-3">Billing address</h4> -->
				      <form class="needs-validation">				      					        
				        <div class="row">
				          <div class="col-md-6 mb-3">
				            <input type="hidden" id="txtCacpdUuid" name="txtCacpdUuid" />
				            <label for="dlItemCode">物料名稱</label>
				          		<select id="dlItemCode" name="dlItemCode" class="form-control">				          							          			
				          		</select>				          				            
				          </div>
				          <div class="col-md-6 mb-3">
				            <label for="txtTrnsQty">數量</label>
				          	<input type="number" class="form-control" id="txtTrnsQty" name="txtTrnsQty" required autocomplete="off">				            
				          </div>
				          <div class="col-md-12 mb-6">
				            <label for="txtTrnsQty">備註</label>
				          	<input type="text" class="form-control" id="txtCacpNotes" name="txtCacpNotes" required autocomplete="off">				            
				          </div>				     				          
				        </div>
				
				        
				        <!-- 
				        <hr class="mb-4">
				        <div class="custom-control custom-checkbox">				          
				        </div>
				         -->
				        				        				
					</form>
				    </div>
				  </div>
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->
	
	
	<div class="modal-footer">
		<button id="btnAddOne" class="btn btn-primary btn-lg btn-block" onclick="formEvent.submitAddOne()" type="button">存檔</button>
		<button id="btnSaveOne" class="btn btn-primary btn-lg btn-block" onclick="formEvent.submitSaveOne()" type="button">存檔</button>
		<button id="btnDelOne" class="btn btn-danger btn-lg btn-block" onclick="formEvent.deleteConfirm()" type="button">刪除</button>		
		<button class="btn btn-default btn-lg btn-block" type="button" data-dismiss="modal">取消</button>
	<!--   
		<input type="button" value="Modify" class="btn btn-primary" onclick="formEvent.modifyUseDesc()" />          
		<input type="button" value="Close" class="btn btn-plus btn-default" data-dismiss="modal" /><br/>
       -->
	</div>
			             
</div>
</div>
</div>

<!-- 成確製作 LIST版 -->
<div class="modal modal-plus fade" id="addDataModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-lg" style="width:100%;" role="document">
<div class="modal-content">
	<div class="modal-header"><h4 class="modal-title"><input type="text" id="txtTrnsDate" name="txtTrnsDate" readonly class="readonly-noBG" size="8" />-成品生產</h4></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
	          <!-- <div class='card-header text-large card-title'>內容</div> -->                            
	          <div class='card-body'>
	          <!-- -->
	          <table id="table_addItemList" class='table-plus table-list width-fit-parent' style="width:100%">                            
                <thead>
                  <tr>                  	
                    <th style="text-align:center; width:1%" nowrap>No.</th>                                       
                    <th nowrap style="text-align:right; width:1%">名稱</th>
                    <th nowrap style="text-align:left; width:1%">生產數量</th>               
                    <th nowrap style="text-align:right; width:1%">單位</th>
                    <th nowrap style="text-align:left">備註</th>                    
                  </tr>
                </thead>
                <tbody></tbody>
              </table>	          	
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->
	
	
	<div class="modal-footer">		
		<button id="btnSaveList" class="btn btn-primary btn-lg btn-block" onclick="formEvent.submitSaveList()" type="button">存檔</button>				
		<button class="btn btn-default btn-lg btn-block" type="button" data-dismiss="modal">取消</button>
	<!--   
		<input type="button" value="Modify" class="btn btn-primary" onclick="formEvent.modifyUseDesc()" />          
		<input type="button" value="Close" class="btn btn-plus btn-default" data-dismiss="modal" /><br/>
       -->
	</div>
			             
</div>
</div>
</div>
  
<c:import url="/_comm/jsp/hms.js.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>" ></c:import>		
<script src='/HBD1/HBD1W330/js/HBD1W330_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>			
<c:import url="/HBD1/HBD1W330/jsp/HBD1W330_Template.jsp"></c:import>		
</body>	
</html>
