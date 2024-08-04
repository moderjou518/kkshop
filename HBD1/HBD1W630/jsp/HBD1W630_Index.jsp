<%@page contentType="text/html;charset=utf-8" language="java" import="java.util.*,java.lang.*,java.text.*,java.sql.*,java.text.*,java.math.*,com.evergreen.web.security.*"%>
<%@page import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
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
	<title>HBD1W630 - 帳號管理</title>
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
               <li class="breadcrumb-item">帳號管理</li>                              
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
                      <div class="btn btn-plus bar-head bar-dark-grey slideMenu_closeBtn" onclick="HMS2_SlideMenu.slideMenuClose('Y')">                        
                        	<i id="hmsSlider_collapseBtn" class="fas fa-angle-double-left"></i>
                      </div> 
                  </div>      
                                
                  <div class='card-body'>
                    <!-- Quick Query 區塊, 預設抓全部資料 -->
                    <div class="row" id="divQuickSearch">
    	                <div class="col-md-12" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
						<button type="button" name="btnQuickQuery" data-group="C" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info form-control">
						客戶	</button>
						<button type="button" name="btnQuickQuery" data-group="E" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info form-control">
						一般員工	</button>
						<button type="button" name="btnQuickQuery" data-group="F" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info form-control">
						財務	</button>
						<button type="button" name="btnQuickQuery" data-group="A" onclick="formEvent.quickQuery(this)" class="btn btn-plus btn-info form-control">
						管理者	</button>
						</div>
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
                              <th colspan="2" style="text-align:center">會員名稱</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <input type="text" id="qryName" name="qryName" maxlength="10" style="width:96%" />                                
                              </td>
                            </tr>
                                                        
                            <tr>
                              <th colspan="2" style="text-align:center">帳號群組</th>
                            </tr>
                            <tr>
                              <td colspan="2">                                                               
                              	<input type="text" id="qryGroup" name="qryGroup" />                                
                              </td>
                            </tr>
                            
 							
                            <tr>
                              <td colspan="3" align="center">                              	
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
                  <button type="button" id="btnAdd"    title="新增資料" onclick="formEvent.addAData()" class="btn btn-plus-icon btn-default" ><i class="fas fa-plus"></i></button>                 
                  
                                                      
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
              <table id='table_queryResult' class='table-plus table-list width-fit-parent'>                            
                <thead>
                  <tr>                  	
                    <th style="width:60px;text-align:center" nowrap>排序</th>                                        
                    <th style="width:100px" nowrap>會員帳號</th>                    
                    <th style="width:100px" nowrap>姓名</th>
                    <th style="width:100px" nowrap>帳號停用(V)</th>
                    <th style="width:5%" nowrap></th>
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

<div class="modal modal-plus fade" id="showDataModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-lg" style="width:100%;" role="document">
<div class="modal-content">
	<div class="modal-header"><h4 class="modal-title">會員資訊</h4></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
	          <!-- <div class='card-header text-large card-title'>內容</div> -->                            
	          <div class='card-body'>
	          	<div class="row">
				    <div class="col-md-12 order-md-1">
				      <!-- <h4 class="mb-3">Billing address</h4> -->
				      <form class="needs-validation" novalidate="" id="detlForm" name="detlForm">				        
				        <div class="row">
				          <div class="col-md-6 mb-3">
				            <label for="txtHascBascLoginId">會員帳號</label>
				          	<input type="text" class="form-control" id="txtHascBascLoginId" name="txtHascBascLoginId" maxlength="10" autocomplete="off" required />			          
				          				            
				          </div>
				          <div class="col-md-6 mb-3">
				            <label for="txtPassword">會員編號</label>
				          	<input type="text" class="form-control readonly-noBG" readonly id="txtHac0BascAcCode" name="txtHac0BascAcCode">				            
				          </div>
				        </div>
				        
				        <div class="row">
				          <div class="col-md-6 mb-3">
				            <label for="txtHac0BascName">姓名</label>
				          	<input type="text" class="form-control" id="txtHac0BascName" name="txtHac0BascName" placeholder="請輸入姓名" autocomplete="off" required />				            
				          </div>
				          <div class="col-md-6 mb-3">
				            <label for="txtHac0BascPwd">密碼</label>
				          	<input type="text" class="form-control" id="txtHac0BascPwd" name="txtHac0BascPwd" maxlength="20" autocomplete="off" required />				            
				          </div>
				        </div>
				
				        <div class="row">
				          <div class="col-md-6 mb-3">
				            <label for="dlHac0BascGroup">系統群組</label>
				          	<select id="dlHac0BascGroup" name="dlHac0BascGroup" class="form-control">
				          		<option value="C">客戶</option>
				          		<option value="E">一般員工</option>
				          		<option value="F">財務</option>
				          		<option value="A">管理者</option>
				          	</select>				            
				          </div>
				          <div class="col-md-6 mb-3">
				            <label for="txtHac0BascNote">排序</label>
				          	<input type="text" class="form-control" id="txtHac0BascNote" name="txtHac0BascNote" maxlength="3" autocomplete="off" size="3">				            
				          </div>
				        </div>
				        
				        
				        <hr class="mb-4">
				        <div class="custom-control custom-checkbox">
				          <input type="checkbox" class="custom-control-input" id="txtVoidMark" name="txtVoidMark" value="V">
				          <label class="custom-control-label" for="txtVoidMark">關閉此帳號</label>
				        </div>
				        <!-- 
				        <div class="custom-control custom-checkbox">
				          <input type="checkbox" class="custom-control-input" id="save-info">
				          <label class="custom-control-label" for="save-info">Save this information for next time</label>
				        </div> -->				        				
					</form>
				    </div>
				  </div>
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>
		<button id="btnAddOne" class="btn btn-primary btn-lg btn-block" onclick="formEvent.submitAddOne()" type="button">存檔</button>
		<button id="btnSaveOne" class="btn btn-primary btn-lg btn-block" onclick="formEvent.submitSaveOne()" type="button">存檔</button>
		<button class="btn btn-default btn-lg btn-block" type="button" data-dismiss="modal">取消</button>
	<!-- 
		<input type="button" value="Modify" class="btn btn-plus btn-primary" onclick="formEvent.modifyUseDesc()" />          
		<input type="button" value="Close" class="btn btn-plus btn-default" data-dismiss="modal" /><br/>
		 -->      
	</div>             
</div>
</div>
</div>

  
<c:import url="/_comm/jsp/hms.js.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>" ></c:import>		
<script src='/HBD1/HBD1W630/js/HBD1W630_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>			
<c:import url="/HBD1/HBD1W630/jsp/HBD1W630_Template.jsp"></c:import>		
</body>	
</html>
