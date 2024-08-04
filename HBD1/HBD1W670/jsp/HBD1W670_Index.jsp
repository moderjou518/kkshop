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
	<title>HBD1W670 - 預訂及進出貨數量統計</title>
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
               <li class="breadcrumb-item">預訂及進出貨數量統計</li>                              
           </ol>           
          </div>    
      </div>      
      
      <!-- 上半部選單  -->      
      <!-- layout Start page-01-->
      <div class="row">
        <!-- 左邊 sidebar 
        <div title="Expand" id="divOpenBarBtn" class="slideMenu_openBtn"  onclick="HMS2_SlideMenu.slideMenuOpen()"><span class="fa fa-bars"></span></div>-->        
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
                                
                  <div class='card-body' id="" name="">
	                  <form name="qryForm" id="qryForm">
    	              	<div class="form-row">
        	            <!-- Quick Query 區塊, 預設抓全部資料 -->
                    		<div class="form-group">
    						<label for="qryPoDate1">訂單日期</label>    					
    						<input type="text" id="qryPoDate1" name="qryPoDate1" class="form-control" style="width:100%;text-align:center" maxlength="8" placeholder="yyyyMMdd"  />
    						<input type="text" id="qryPoDate2" name="qryPoDate2" class="form-control" style="width:100%;text-align:center" maxlength="8" placeholder="yyyyMMdd"  />
    						<!-- <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small> -->
  						</div>
                    	</div>
                    	<button type="button" id="btnQuery" class="btn btn-primary form-control">查詢</button>
                  	</form>
                    
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
                    <th style="width:2%" nowrap>物料編號</th>                                        
                    <th style="width:2%" nowrap>物料名稱</th>                    
                    <th style="width:3%; text-align:right" nowrap>已進貨</th>
                    <th style="width:3%; text-align:right" nowrap>已生產</th>
                    <th style="width:3%; text-align:right" nowrap>預訂</th>
                    <th style="width:3%; text-align:right" nowrap nowrap>已出貨</th>
                    <th style="width:3%; text-align:right" nowrap nowrap>未出貨</th>
                    <th ></th>                    
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

<div class="modal modal-plus fade" id="moreUseDescModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-lg" style="width:100%;" role="document">
<div class="modal-content">
	<div class="modal-header"><h4 class="modal-title">券種使用說明</h4></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
	          <div class='card-header text-large card-title'>內容</div>                            
	          <div class='card-body' style="overflow-y:auto; height:500px">
	          	<input type="hidden" id="txtAtRow" name="txtAtRow" />
	          	<textarea id="txtMoreUseDesc" style="width:100%;height:470px;" vertical-align="top">
	          	</textarea>
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->
	  
	<div class="modal-footer" style='width:99%;text-align:center;'>
		<input type="button" value="Modify" class="btn btn-plus btn-primary" onclick="formEvent.modifyUseDesc()" />          
		<input type="button" value="Close" class="btn btn-plus btn-default" data-dismiss="modal" /><br/>      
	</div>             
</div>
</div>
</div>

  
<c:import url="/_comm/jsp/hms.js.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>" ></c:import>		
<script src='/HBD1/HBD1W670/js/HBD1W670_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>			
<c:import url="/HBD1/HBD1W670/jsp/HBD1W670_Template.jsp"></c:import>		
</body>	
</html>
