<%@page contentType="text/html;charset=utf-8" language="java" import="java.util.*,java.lang.*,java.text.*,java.sql.*,java.text.*,java.math.*,com.evergreen.web.security.*"%>
<%@page language="java" import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*, org.apache.log4j.Logger"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%
	String compCode = (String) request.getSession().getAttribute("COMP_CODE");
	String compName = (String) request.getSession().getAttribute("COMP_NAME");
	
	//System.out.println("jsp compCode: " + compCode);
	//System.out.println("jsp compName: " + compName);
	Logger logger = Logger.getLogger("KKSHOP_JSP_LOG");
	logger.debug("HBD1W690_Index.jsp");
%>
<html>
<head>
	<title>HBD1W690-商品價格設定</title>
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
	<c:import url="/_ui/jsp/util/grid.js.jsp?v=2" context="/wlhbmg"></c:import>
	<c:import url="/_comm/jsp/hms.css.jsp?ver=2.0.0" context="/wlhbmg" ></c:import>
	<c:import url="/_ui/jsp/util/validator.js.jsp" context="/wlhbmg"></c:import>		
</head>
<body class="hms_style">  
  <div class='app_padding'>
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <c:import url="/HBD1/common/jsp/menu.jsp" context="/wlhbmg" />          
        </div>
      </div>
      <div class="row" >
          <div class='col-md-12'>
           <ol class="breadcrumb" style="margin-bottom: 10px;">
               <li class="breadcrumb-item">管理者</li>
               <li class="breadcrumb-item">商品價格設定</li>                              
           </ol>           
          </div>    
      </div>      
      
      <!-- 上半部選單  -->      
      <!-- layout Start page-01-->
      <div class="row">
        <!-- 左邊 sidebar 
        <div title="Expand" id="divOpenBarBtn" class="slideMenu_openBtn"  onclick="HMS2_SlideMenu.slideMenuOpen()"><span class="fa fa-bars"></span></div>
        -->        
        <div class="col-md-3" id="hmsSlide_Menu">
          <div class="row"  >                    
              <div class="col-md-12">
                <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
                  <div class='card-header text-large card-title'>
                    	快速搜尋                       
                  </div>      
                                
                  <div class='card-body'>
					<table class='table-plus table-detail width-fit-parent' id='queryCondTab' style="width:100%">
                          <tbody>
                          <tr>
                              <th colspan="2" style="text-align:center">商品</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <select id="qryItem" name="qryItem" style="width:96%">
                                	<option value=""></option>                                	
                                </select>                                                                
                              </td>
                            </tr>
                            
                            <!--  -->
                            <tr>
                              <th colspan="2" style="text-align:center">年度</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <select id="qryYear" name="qryYear" style="width:96%">                                	
                                </select>
                                <input type="hidden" id="qryMonth" name="qryMonth" />                                
                              </td>
                            </tr>
                                                        
							<!-- 月份 -->
                            <tr>
                              <th colspan="2" style="text-align:center">月份</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                              	<div class="row" id="divQuickSearch">        
									<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="01" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					一月
                        				</button>                      
                      				</div>
                      				<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="07" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					七月
                        				</button>                      
                      				</div>                                       
                      				<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="02" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					二月
                        				</button>                      
                      				</div>
                      				<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="08" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					八月
                        				</button>                      
                      				</div>
                      				<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="03" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					三月
                        				</button>                      
                      				</div>
                      				<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="09" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					九月
                        				</button>                      
                      				</div>                      				
                      				<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="04" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					四月
                        				</button>                      
                      				</div>
                      				<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="10" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					十月
                        				</button>                      
                      				</div>
                      				<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="05" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					五月
                        				</button>                      
                      				</div>
                      				<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="11" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					11月
                        				</button>                      
                      				</div>
                      				<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="06" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					六月
                        				</button>                      
                      				</div>                      				
                      				<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				<button type="button" name="btnQuickQuery" data-month="12" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info" style="width: 100%">
                          					12月
                        				</button>                      
                      				</div>                                            
                    			</div>
                              </td>
                            </tr>                
                            
 							
                            
                          </tbody>
                        </table>                  
                    
                    
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
                              <th colspan="2" style="text-align:center">年度</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <select id="qryYear" name="qryYear" style="width:96%">
                                	<option value="2020">2020</option>                                  
                                	<option value="2021">2021</option>
                                	<option value="2022">2022</option>                                	
                                </select>                                
                              </td>
                            </tr>
                                                        
                            <tr>
                              <th colspan="2" style="text-align:center">月份</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                              
                                                                                         
                                <select id="qryMonth" name="qryMonth" style="width:96%">
                                	<option value="01">一月</option>                                  
                                	<option value="02">二月</option>
                                	<option value="03">三月</option>
                                	<option value="04">四月</option>
                                	<option value="05">五月</option>
                                	<option value="06">六月</option>
                                	<option value="07">七月</option>
                                	<option value="08">八月</option>
                                	<option value="09">九月</option>
                                	<option value="10">十月</option>
                                	<option value="11">11月</option>
                                	<option value="12">12月</option>
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
        <div class="col-md-9" id="hmsSlide_Content" >          
          <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;" id="result_dataList">                                   
            <div class='card-header text-large card-title'>
            	<button type="button" id="btnSave"   title="儲存變更" onclick="formEvent.submitSave()"           class="btn btn-primary btn-save-icon"  ><i class="fas fa-save"></i> 存檔</button>
            	<button type="button" id="btnDelete"   title="刪除商品價格" onclick="formEvent.btnDelete_Click()" class="btn btn-secondary btn-delete-icon"  ><i class="fas fa-trash"></i> 刪除價格</button>
            	<button type="button" id="btnCreatePrice" title="新增商品價格" onclick="formEvent.btnCreatePrice_Click()" class="btn btn-plus btn-warning" style="display:none">新增商品價格</button>
            	<button type="button" id="btnSyncPrice" title="同步預訂單價格" onclick="formEvent.btnSyncPrice_Click()" class="btn btn-plus btn-info" style="display:none">同步預訂單價格</button>
            	<button type="button" id="btnResetPrice" title="重設商品價格" onclick="formEvent.btnResetPrice_Click()" class="btn btn-plus btn-info" style="display:none">同步預訂單價格</button>
            </div>                                   
            <!-- 搜尋結果 Search Result 區塊 -->
            <div class='card-body' style="overflow-y:auto; height:600px">                           
              <!-- 搜尋結果 Search Result 區塊 -->
              <table id='table_queryResult' class='table-plus table-list width-fit-parent' style="width:100%">                            
                <thead>
                  <tr>                  	
                    <!-- <th style="width:1%" nowrap>No</th> -->                                        
                    <th style="width:1%" nowrap>日期</th>                                        
                    <th style="width:1%" nowrap>單價</th>
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

<!-- 設定價格 -->
<div class="modal modal-plus fade" id="mdlUnitPrice" tabindex="-1" role="dialog">
<div class='card-header text-large card-title'></div>
<div class="modal-dialog modal-sm" style="width:100%;" role="document">
<div class="modal-content">
	<div class="modal-header">
		<h4 class="modal-title" id="modalTitle">新增商品價格</h4>		
	</div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">	                                      
	          <div class='card-body' style="overflow-y:auto">	          	
	          	<table class='table-plus table-detail width-fit-parent' id='unitPriceTbl' style="width:100%">
                   <tbody>
                     <tr>
                       <th colspan="1" style="text-align:center">價格</th>
                       <td><input type="text" id="txtSetUnitPrice" name="txtSetUnitPrice" size="6" maxlength="10" /></td>
                     </tr>
                     </tbody>
                </table>          	
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>
		<input type="button" id="btnSubmitCreate" value="送出" class="btn btn-primary form-control" onclick="formEvent.btnSubmitCreate_Click()" />          
		<input type="button" value="取消" class="btn btn-default form-control" data-dismiss="modal" /><br/>      
	</div>             
</div>
</div>
</div>



  
<c:import url="/_comm/jsp/hms.js.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>" context="/wlhbmg" ></c:import>		
<script src='/HBD1/HBD1W690/js/HBD1W690_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>			
<c:import url="/HBD1/HBD1W690/jsp/HBD1W690_Template.jsp" context="/wlhbmg"></c:import>		
</body>	
</html>
