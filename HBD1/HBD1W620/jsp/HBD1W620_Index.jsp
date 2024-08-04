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
	logger.debug("HBD1W620_Index.jsp");
%>
<html>
<head>
	<title>HBD1W620-預訂單管理</title>
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
               <li class="breadcrumb-item">預訂單管理</li>                              
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
                      <div class="btn btn-plus bar-head bar-dark-grey slideMenu_closeBtn" onclick="HMS2_SlideMenu.slideMenuClose('Y')">                        
                        	<i id="hmsSlider_collapseBtn" class="fas fa-angle-double-left"></i>
                      </div> 
                  </div>      
                                
                  <div class='card-body'>
					<table class='table-plus table-detail width-fit-parent' id='queryCondTab' style="width:100%">
                          <tbody>
                            <tr>
                              <th colspan="2" style="text-align:center">年度
                              	<input type="hidden" id="txtCompCode" name="txtCompCode" value="<%=compCode%>" />
                              </th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <select id="qryYear" name="qryYear" style="width:96%">
                                	<option value="2022">2022</option>
                                	<option value="2021">2021</option>
                                	<option value="2020">2020</option>                                	                                	
                                </select>
                                <input type="hidden" id="qryMonth" name="qryMonth" />                                
                              </td>
                            </tr>
                                                        
                            <tr>
                              <th colspan="2" style="text-align:center">訂單月份</th>
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
            	<button type="button" id="btnSave"   title="儲存變更" onclick="formEvent.submitSave()" class="btn btn-plus-icon btn-primary btn-save-icon"  ><i class="fas fa-save"></i></button>
            </div>                                   
            <!-- 搜尋結果 Search Result 區塊 -->
            <div class='card-body' style="overflow-y:auto; height:600px">                           
              <!-- 搜尋結果 Search Result 區塊 -->
              <table id='table_queryResult' class='table-plus table-list width-fit-parent' style="width:100%">                            
                <thead>
                  <tr>                  	
                    <th style="width:1%" nowrap>No</th>                                        
                    <!-- <th >預訂單名稱</th> -->
                    <th style="width:1%" nowrap>訂單起迄 (周一至周日)</th>                                        
                    <th style="width:1%" nowrap>下單起迄 (周一至周四)</th>                                                           
                    <th nowrap>自動上下架</th>     
                    <!-- <th style="width:1%" nowrap>價格</th> -->
                    <!-- <th style="width:1%" nowrap>訂單</th>-->
                    <!-- <th style="width:1%" nowrap>刪</th>
                    <th style="width:1%" nowrap></th> -->
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
<div class="modal modal-plus fade" id="setPriceModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-md" style="width:100%;" role="document" style="height:500px">
<div class="modal-content">
	<div class="modal-header" id="subTableMastResult">
		<h4 class="modal-title" id="modalTitle">
			《<input type="text" id="setBidOrdSdate" name="setBidOrdSdate" readonly class="readonly-noBG" size="6" />~
			<input type="text" id="setBidOrdEdate" name="setBidOrdEdate" readonly class="readonly-noBG" size="6" />》 商品價格設定
			<input type="hidden" id="setBidUUID" name="setBidUUID" readonly/>
		</h4>		
	</div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
	          <!-- <div class='card-header text-large card-title'>內容</div> -->                            
	          <div class='card-body' style="overflow-y:auto; height:350px">
	          	<input type="hidden" id="txtAtRow" name="txtAtRow" />	          	
	          	<table id='subTableQueryResult' class='table-plus table-list width-fit-parent' style="width:100%">                            
	                <thead>
	                  <tr>                  	
	                    <th style="width:10%" nowrap>商品名稱</th>                                        
	                    <th style="width:10%">價格</th>	                    
	                    <th nowrap>自編料號</th>
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
		<input type="button" value="存檔" class="btn btn-primary form-control" onclick="formEvent.saveModifyPrice()" />          
		<input type="button" value="離開" class="btn btn-default form-control" data-dismiss="modal" /><br/>      
	</div>             
</div>
</div>
</div>

<!-- 當周訂單 -->
<div class="modal modal-plus fade" id="weekPoListModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-xl" style="width:100%;" role="document" style="height:500px">
<div class="modal-content">
	<div class="modal-header"><h2 class="modal-title" id="weekPoListModalTitle"></h2></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class="row">
				<div class="col-md-1"></div>
				<div class="col-md-10">
					<div class='card card-plus' id="divQueryResult">                  
						<div class='card-header card-title text-large'>
							<table style="width:100%" style="font-size:16px">
								<tr>
									<td nowrap>
										<input type="text" id="txtSelWeek" readonly class="form-control readonly-noBG" size="20" />
									</td>
									<td style="text-align:left"><div class="btn-group" id="btnWeekDayGroup"></div></td>
									<td style="width:10%" nowrap></td>
								</tr>
							</table>			
							<input type="hidden" id="txtPoDate" />						
						</div>                     
						<div class='card-body'>       
							<table class='table-plus table-list width-fit-parent' id='tableQueryResult' style="width:100%">
								<tbody></tbody>
							</table>        
						</div>   
					</div>
				</div>
				<div class="col-md-1"></div>
				</div>        
	      </div>      
	    </div>  
	</div><!-- modal body -->
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>		          
		<input type="button" value="離開" class="btn btn-default form-control" data-dismiss="modal" /><br/>      
	</div>             
</div>
</div>
</div>

  
<c:import url="/_comm/jsp/hms.js.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>" context="/wlhbmg" ></c:import>		
<script src='/HBD1/HBD1W620/js/HBD1W620_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>			
<c:import url="/HBD1/HBD1W620/jsp/HBD1W620_Template.jsp" context="/wlhbmg"></c:import>		
</body>	
</html>
