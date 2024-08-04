<%@page contentType="text/html;charset=utf-8" language="java" import="java.util.*,java.lang.*,java.text.*,java.sql.*,java.text.*,java.math.*,com.evergreen.web.security.*"%>
<%@page import="java.util.*, com.evergreen_hotels.bmg.wuf1.util.*, com.evergreen.web.security.*"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*, org.apache.log4j.Logger" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%
	String compCode = (String) request.getSession().getAttribute("COMP_CODE");
	String compName = (String) request.getSession().getAttribute("COMP_NAME");
	
	Logger logger = Logger.getLogger("KKSHOP_JSP_LOG");
	logger.info("HBD1W310.jsp");
%>
<html>
<head>
	<title>現場出貨管理(HBD1W310)</title>
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
           <c:import url="/HBD1/common/jsp/emp_menu.jsp" />
          </div>    
      </div>
       
      <!-- 上半部選單  -->      
      <!-- layout Start page-01-->
      <div class="row">
        <!-- 右邊 Search result -->  
        <div class="col-md-5">          
          <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;" id="result_dataList">                                   
            <div class='card-header text-large card-title'>
            	<div class='text-left' style='width: 50%; float: left;'>訂單明細</div>
            	<div class='text-right' style='width: 50%; float: right;'>            		
            		<input type="button" id="btnSaveIssue" value="出貨存檔" class="btn btn-secondary" onclick="formEvent.SaveIssue()" style="display:none" />
            		<!-- <input type="button" class="btn btn-warning" value="儲存" onclick="formEvent.submitSave()" /> -->
            	</div>
            </div>                        
            <div id='div_btn_codesetting_list' class='card-func page-line' data-evg-page-target='table_queryResult'>
	                <div class='text-left' style='width: 10%; float: left;'>                 	                  	                                                      
	                </div>
	                <div class='text-right' style='width: 90%; float: right;'>
	                    <input type="button" id="btnResetAll" value="取消出貨" class="btn btn-default" onclick="formEvent.ResetAll()" style="display:none" />  
	                    <input type="button" id="btnIssueAll" value="快速出貨" class="btn btn-default" onclick="formEvent.IssueAll()" style="display:none" />
	                </div>
            </div>
            <!-- 搜尋結果 Search Result 區塊 -->
            <div class='card-body' style="overflow-y:auto; height:600px">            	
            	<div class="row">            		
            		<div class="col-md-12">
	            		<table id='table_queryResult' class='table-plus table-list width-fit-parent' style="width:100%">                            
		                	<thead>
		                  		<tr>
		                    		<th style="width:1%; text-align:right" nowrap>商品名稱</th>                                        
		                    		<th style="width:1%; text-align:right" nowrap>訂購</th>
		                    		<th style="width:1%; text-align:right" nowrap>出貨</th>
		                    		<th></th>
		                  		</tr>
		                	</thead>
		                	<tbody></tbody>
		              	</table>
            		</div>            		
            	</div>                           
              <!-- 搜尋結果 Search Result 區塊 -->                                          
            </div>    
          </div>
          </div>
                
        <!-- 右邊 sidebar
        <div title="Expand" id="divOpenBarBtn" class="slideMenu_openBtn"  onclick="HMS2_SlideMenu.slideMenuOpen()"><span class="fa fa-bars"></span></div>
         -->        
        <div class="col-md-3">
          <div class="row">                    
             <div class="col-md-12">
               <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
                 <div class='card-header text-large card-title' id="divDayTitle">客戶</div>                                
                 <div class='card-body'>
                   <!-- Quick Query 區塊, 預設抓全部資料 -->
                   <div class="row" id="divQuickSearch">                    
                     <div id="divDayCustomer" class="col-md-12" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                       
                     </div>                                                                 
                   </div>                    
                 </div>   
               </div>
             </div>                                  
           </div> <!-- row end -->            
        </div><!-- col-md-1  -->
        
        <div class="col-md-4">
          <div class="row">                    
              <div class="col-md-12">
              	<div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">
              		<div class='card-header text-large card-title text-center'>
              			<select id="txtYYYY" name="txtYYYY">
              				<option value="2018">107</option>
              				<option value="2019">108</option>
              				<option value="2020">109</option>              				
              				<option value="2021">110</option>
              				<option value="2022">111</option>
              			</select>
              			<select id="txtMM" name="txtYYYYMM">
              				<option value="01">1月</option>
              				<option value="02">2月</option>
              				<option value="03">3月</option>
              				<option value="04">4月</option>
              				<option value="05">5月</option>
              				<option value="06">6月</option>
              				<option value="07">7月</option>
              				<option value="08">8月</option>
              				<option value="09">9月</option>
              				<option value="10">10</option>
              				<option value="11">11</option>
              				<option value="12">12</option>
              			</select>
              		</div>		            
		            <div class='card-body' style="overflow-y:auto; height:600px">
		            <!-- 搜尋結果 Search Result 區塊 -->
		            	<div class="row">
		            	<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        	<button type="button" name="btnQuickQuery" data-evg-day="01" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">1</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="02" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">2</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="03" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">3</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="04" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">4</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="05" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">5</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="06" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">6</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="07" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">7</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="08" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">8</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="09" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">9</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="10" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">10</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="11" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">11</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="12" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">12</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="13" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">13</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="14" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">14</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="15" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:3px">15</button>                        	
                      	</div>
                      	<div class="col-md-6" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
                        	<button type="button" name="btnQuickQuery" data-evg-day="16" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">16</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="17" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">17</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="18" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">18</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="19" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">19</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="20" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">20</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="21" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">21</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="22" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">22</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="23" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">23</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="24" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">24</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="25" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">25</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="26" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">26</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="27" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">27</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="28" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">28</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="29" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">29</button>
                        	<button type="button" name="btnQuickQuery" data-evg-day="30" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">30</button>                        	
                        	<button type="button" name="btnQuickQuery" data-evg-day="31" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">31</button>
                      	</div>		     
                      	</div>         		                                           
		            </div>    
              	</div>
              </div>
          </div>
        </div>
        
    
      </div><!-- row -->
      <!-- page01 -->
      <!-- layout End -->      
    </div><!-- container -->    
  </div><!-- appsetting -->	
<c:import url="/HBD1/common/jsp/emp_footer.jsp" />  
<c:import url="/_comm/jsp/hms.js.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>" ></c:import>		
<script src='/HBD1/HBD1W310/js/HBD1W310_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>			
<c:import url="/HBD1/HBD1W310/jsp/HBD1W310_Template.jsp"></c:import>		
</body>	
</html>
