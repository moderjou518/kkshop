<%@page contentType="text/html;charset=utf-8" language="java" import="java.util.*,java.lang.*,java.text.*,java.sql.*,java.text.*,java.math.*,com.evergreen.web.security.*"%>
<%@page language="java" import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%
	String compCode = (String) request.getSession().getAttribute("COMP_CODE");
	String compName = (String) request.getSession().getAttribute("COMP_NAME");
	
	//System.out.println("jsp compCode: " + compCode);
	//System.out.println("jsp compName: " + compName);
%>
<html>
<head>
	<title>HBD1W680-應收帳款(周結)</title>
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
	<c:import url="/_ui/jsp/util/date.js.jsp" context="/wlhbmg"></c:import>
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
               <li class="breadcrumb-item">應收帳款(周結)</li>                              
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
          <div class="row"  >                    
              <div class="col-md-12">
                <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
                  <div class='card-header text-large card-title'>搜尋</div>      
                                
                  <div class='card-body'>
					<table class='table-plus table-detail width-fit-parent' id='queryCondTab' style="width:100%">
                          <tbody>
                            <tr>
                            	<th colspan="2" class="text-center">年度月份</th>
                            </tr>
                            <tr>
                              <td colspan="2" class="text-center">
                                <input type="text" id="qryMonth" name="qryMonth" style="width:100%;text-align:center" placeholder="格式: YYYYMM " maxlength="6" value='202010' size="6" />                                                                
                              </td>                              
                            </tr>
                            <tr>
                              <td colspan="2">
                                <input type="button" onclick="qryEvent.submitQueryMonth()" class="btn btn-primary" style="width:100%" value="查詢" />                                                                
                              </td>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <hr/>                                                                
                              </td>
                            </tr>                   
                            <tr>
                              <th colspan="2" style="text-align:center">查詢結果-周預訂單</th>
                            </tr>
                            <tr>
                              <td colspan="2" class="text-center">
                              	<div class="row" id="divQuickSearch">        
									<div class="col-md-12" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        				請先查詢月份                      
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
                              

        </div><!-- col-md-3  -->
        
        
        <!-- 右邊 Search result -->  
        <div class="col-md-10" id="hmsSlide_Content" >          
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
                  <!-- <button type="button" id="btnSave"   title="儲存變更" onclick="formEvent.submitSave()" class="btn btn-plus-icon btn-primary btn-save-icon"  ><i class="fas fa-save"></i></button>
                  <button type="button" id="btnAdd"    title="新增資料" onclick="formEvent.addARow()" class="btn btn-plus-icon btn-default" ><i class="fas fa-plus"></i></button>
                   -->                                    
                </div>
                <div class='text-right' style='width: 40%; float: right;'>                      
                </div>
            </div>            
            <!-- 搜尋結果 Search Result 區塊 -->
            <div class='card-body' style="overflow-y:auto; height:600px">                           
              <!-- 搜尋結果 Search Result 區塊 -->
              <table id='table_queryResult' class='table-plus table-list'>                            
                <thead>
                  <tr>                  	
                    <th  nowrap>會員</th>                                        
                    <th style="text-align:right" nowrap>應收金額</th>
                    <th style="text-align:right" nowrap>匯款金額</th>
                    <th style="text-align:right" nowrap>差額</th>
                    <th style="text-align:center" nowrap>匯款日期</th>
                    <th style="text-align:center">訂單明細</th>
                    <th style="text-align:center">匯款</th>
                  </tr>
                </thead>
                <tbody></tbody>
                <tfoot></tfoot>
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
<div class="modal-dialog modal-md" style="width:100%;" role="document" style="height:500px">
<div class="modal-content">
	<div class="modal-header"><h4 class="modal-title">每周訂單明細</h4></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
	          <!-- <div class='card-header text-large card-title'>內容</div> -->                            
	          <div class='card-body' style="overflow-y:auto">          	
				<table class='table' id='tblWeekPOList' class="table-plus table-detail" style="width:100%">
					<thead>
						<tr>
							<th nowrap>日期</th>
							<th class="text-right">商品</th>
							<!-- <th style="text-align:right">訂購</th> -->
							<th style="text-align:right;width:1%" nowrap>出貨</th>
							<th style="text-align:right;width:1%" nowrap>單價</th>							
							<th style="text-align:right;width:1%" nowrap>金額</th>
						</tr>
					</thead>	        
			        <tbody></tbody>
			        <tfoot>
			        	<tr class="bg-dark text-white font-weight-bold">														
							<th style="text-align:right;width:1%" nowrap></th>
							<th colspan="2" style="text-align:right;width:1%" nowrap>本周出貨總金額</th>							
							<th colspan="2" style="text-align:right;width:1%" nowrap>
								<span id="txtWeekTotalAmount"></span>
							</th>
						</tr>
			        </tfoot>
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
				    <input type="number" class="form-control" id="txtAmount" name="txtAmount" required maxlength="6" />
				  </div>
				  <div class="form-group">
				    <label for="formGroupExampleInput2">匯款日期</label>
				    <input type="text" class="form-control" id="txtDate" name="txtDate" required maxlength="8" />
				  </div>				        	
				  <div class="form-group" style="display:none">
				    <label for="formGroupExampleInput2">帳號末5碼</label>
				    <input type="hidden" id="txtAccount" name="txtAccount" />
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

  
<c:import url="/_comm/jsp/hms.js.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>"></c:import>		
<script src='/HBD1/HBD1W680/js/HBD1W680_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>			
<c:import url="/HBD1/HBD1W680/jsp/HBD1W680_Template.jsp" context="/wlhbmg"></c:import>		
</body>	
</html>
