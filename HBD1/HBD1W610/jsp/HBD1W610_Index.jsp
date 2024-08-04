<%@page contentType="text/html;charset=utf-8" language="java" import="java.util.*,java.lang.*,java.text.*,java.sql.*,java.text.*,java.math.*,com.evergreen.web.security.*"%>
<%@page import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*, org.apache.log4j.Logger" %>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%
	String compCode = (String) request.getSession().getAttribute("COMP_CODE");
	String compName = (String) request.getSession().getAttribute("COMP_NAME");
	
	Logger logger = Logger.getLogger("KKSHOP_JSP_LOG");
	logger.info("HBD1W610.jsp");
%>
<html>
<head>
	<title>HBD1W610-商品維護</title>
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
               <li class="breadcrumb-item">商品維護</li>                              
           </ol>           
          </div>    
      </div>      
      
      <!-- 上半部選單  -->      
      <!-- layout Start page-01-->
      <div class="row">
        <!-- 左邊 sidebar 
        <div title="Expand" id="divOpenBarBtn" class="slideMenu_openBtn"  onclick="HMS2_SlideMenu.slideMenuOpen()"><span class="fa fa-bars"></span></div>
        -->        
        <div class="col-md-2" id="hmsSlide_Menu">
          <div class="row" >                    
              <div class="col-md-12">
                <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
                  <div class='card-header text-large card-title'>
                    	快速搜尋                    
                      <!-- <div class="btn btn-plus bar-head bar-dark-grey slideMenu_closeBtn" onclick="HMS2_SlideMenu.slideMenuClose('Y')">                        
                        	<i id="hmsSlider_collapseBtn" class="fas fa-angle-double-left"></i>
                      </div> --> 
                  </div>      
                                
                  <div class='card-body'>
                    <!-- Quick Query 區塊, 預設抓全部資料 -->
                    <div class="row" id="divQuickSearch">                    
                    
                                            
                    </div>
                    
                  </div>   
                </div>
              </div>                                  
            </div><!-- row end -->
                              
          <div class="row" style="display:none">
              <div class="col-md-12">
                <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
                  <div class='card-header text-large card-title'>進階搜尋</div>                    
                  <div class='card-body'>                  
                        <!-- Search Criteria 區塊 -->
                        <table class='table-plus table-detail width-fit-parent' id='queryCondTab' style="width:100%">
                          <tbody>
                            <tr>
                              <th colspan="2" style="text-align:center">商品名稱</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <input type="text" id="qryItemName" name="qryItemName" maxlength="10" style="width:96%" />                                
                              </td>
                            </tr>
                                                        
                            <tr>
                              <th colspan="2" style="text-align:center">商品類型</th>
                            </tr>
                            <tr>
                              <td colspan="2">                                                               
                                <select id="qryItemType" name="qryItemType" style="width:96%">                                  
                                </select>
                              </td>
                            </tr>
                            
                            <!-- 
							<tr>
                              <th><div style="text-align:right">日期區間</div></th>
                              <td colspan="2">
                                <input type="text" id="qryDate1" name="qryDate1"  maxlength="8" required pattern="^[0-9]{8}$" data-evg-valid-title='Date Format: yyyyMMdd' data-evg-valid-msg-pattern='Incorrect date format' style="width:47%; text-align: center" /> 
                                <input type="text" id="qryDate2" name="qryDate2"  maxlength="8" required pattern="^[0-9]{8}$" data-evg-valid-title='Date Format: yyyyMMdd' data-evg-valid-msg-pattern='Incorrect date format' style="width:47%; text-align: center" />                                
                              </td>
                            </tr>                            
 							-->
 								
                            <tr>
                              <td colspan="3" align="center">
                              	<input type="hidden" id="txtCompCode" name="txtCompCode" value="<%=compCode%>" />
                                <input type="button" id="queryBtn" class="btn btn-primary" value="查詢" style="width:100%" />                                                                                        
                               </td>
                            </tr>
                          </tbody>
                        </table>
                        
                  </div>   
                </div>
              </div>                                  
            </div><!-- row end -->
        </div><!-- col-md-3  -->
        
        
        <!-- 右邊 Search result -->  
        <div class="col-md-10" id="hmsSlide_Content" >          
          <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;" id="result_dataList">                                   
            <div class='card-header text-large card-title'>查詢結果</div>            
            <div id='div_btn_codesetting_list' class='card-func page-line' data-evg-page-target='table_queryResult'>
                <div class='text-left' style='width: 60%; float: left;'>                  
                  <button type="button" id="btnSave"   title="儲存變更" onclick="formEvent.submitSave()" class="btn btn-plus-icon btn-primary btn-save-icon"  ><i class="fas fa-save"></i></button>
                  <button type="button" id="btnAdd"    title="新增資料" onclick="formEvent.addData()" class="btn btn-plus-icon btn-default" ><i class="fas fa-plus"></i></button>                 
                  
                                                      
                </div>
                <div class='text-right' style='width: 40%; float: right;'>
                    <span class='page-change-prev text-right'></span>
                    <span style='padding: 10px;' class='page-select text-left'></span>
                    <span class='page-change-next text-left'></span>
                    <span style='padding: 0px 10px;' class='page-total-rows'></span>
                    <div class="page-row" style="display: none"></div>  
                </div>
            </div>            
            <!-- 搜尋結果 Search Result 區塊 -->
            <div class='card-body' style="overflow-y:auto; height:600px">                           
              <!-- 搜尋結果 Search Result 區塊 -->
              <table id='table_queryResult' class='table-plus table-list'>                            
                <thead>
                  <tr>
                  	<!-- 
                    <th style="width:1%" nowrap>
                      <label><input type="checkbox" name="cbChk" onclick="evg.util.grid.allChk(this,'cbChk')" value="Y" style="zoom:150%" /></label>
                    </th> -->
                    <th style="width:2%" nowrap>筆數</th>                                        
                    <th style="width:5%" nowrap>自編料號</th>                    
                    <th style="width:5%" nowrap>名稱</th>
                    <th style="width:3%" nowrap>單位</th>
                    <!-- <th style="width:10%" nowrap>單價</th> -->
                    <th style="width:10%" nowrap>分類</th>
                    <th style="width:3%" nowrap><a href="#" onclick="alert('數字愈小愈前面\n排列順序為：1.商品分類 2.排序數字')">排序</a></th>
                    <th style="width:1%" nowrap>關閉價格顯示</th>                                                           
                    <th style="width:1%" nowrap>停用</th>
                    <!-- <th style="width:1%" nowrap>移除</th> -->
                    <th style="width:1%" nowrap>資訊</th>
                    <th></th>
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

<!-- item modal -->
<div class="modal modal-plus fade" id="showDataModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-lg" style="width:100%;" role="document">
<div class="modal-content">
	<div class="modal-header"><h4 class="modal-title">物料資訊</h4></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">	                                      
	          <div class='card-body'>
	          	<div class="row">
				    <div class="col-md-12 order-md-1">				      
				      <form class="needs-validation" novalidate="">				        
				        <div class="row">
				          <div class="col-md-6 mb-3">
				            <label for="txtHascBascLoginId">自編料號</label>
				          	<input type="text" class="form-control" id="txtItemName" name="txtItemName" autocomplete="off" placeholder="自編料號" >			          
				          				            
				          </div>
				          <div class="col-md-6 mb-3">
				          	<label for="txtItemPrice">價格</label>
				          	<input type="number" class="form-control" id="txtItemPrice" name="txtItemPrice" autocomplete="off">				            				            
				          </div>
				        </div>
				        
				        <div class="row">
				          <div class="col-md-6 mb-3">
				            <label for="txtHac0BascName">名稱</label>
				          	<input type="text" class="form-control" id="txtItemAbbr" name="txtItemAbbr" placeholder="請輸入名稱" required="" autocomplete="off">				            
				          </div>
				          <div class="col-md-6 mb-3">
				            <label for="txtPassword">單位</label>
				          	<input type="text" class="form-control" id="txtItemUnit" name="txtItemUnit" maxlength="20" autocomplete="off">				            
				          </div>
				        </div>
				
				        <div class="row">
				        	<div class="col-md-6 mb-3">
				            	<label for="txtPassword">排序</label>
				          		<input type="text" class="form-control" id="txtItemSort" name="txtItemSort" maxlength="20" autocomplete="off" placeholder="排序號碼愈小排愈前面">				            
				          	</div>
				          <div class="col-md-6 mb-3">
				            <label for="txtHac0BascName">分類</label>
				          	<select id="txtItemType" name="txtItemType" style="width:96%" data-evg-orivalue="1101" class="form-control">
								<option value="*">請選擇</option>
								<option value="1101" selected="">主要商品</option>        
								<option value="2101">次要商品</option>        
							</select>  
				          </div>				          
				        </div>				        
				        
				        
				        <div class="row" style="display:none">
				          <div class="col-md-6 mb-3">
				          	<div class="custom-control custom-checkbox">
				          		<input type="checkbox" class="custom-control-input" id="txtItemVoidMk" name="txtItemVoidMk" value="V">
				          		<label class="custom-control-label" for="txtItemVoidMk">停用</label>
				        	</div>
				            				          				            
				          </div>
				          <div class="col-md-6 mb-3">
				          	<label for="txtPassword">物料編號</label>
				          	<input type="text" class="form-control readonly-noBG" readonly id="txtItemUuid" name="txtItemUuid">				          					            				            
				          </div>
				        </div>
				        
				        
				        				        				
					</form>
				    </div>
				  </div>
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>
		<button id="btnAddOne" class="btn btn-primary btn-lg btn-block" type="button">存檔</button>
		<button id="btnSaveOne" class="btn btn-primary btn-lg btn-block" type="button">存檔</button>
		<button class="btn btn-default btn-lg btn-block" type="button" data-dismiss="modal">取消</button>
	</div>             
</div>
</div>
</div>


  
<c:import url="/_comm/jsp/hms.js.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>" ></c:import>		
<script src='/HBD1/HBD1W610/js/HBD1W610_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>			
<c:import url="/HBD1/HBD1W610/jsp/HBD1W610_Template.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>"></c:import>		
</body>	
</html>
