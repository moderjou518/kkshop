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
	<title>HBD1W662-每年訂單管理</title>
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
      <div class="row"  style="vertical-align:middle">
          <div class='col-md-12' name="qryForm" id="qryForm">
           <ol class="breadcrumb" style="margin-bottom: 10px">
               <li class="breadcrumb-item">管理者</li>
               <li class="breadcrumb-item">每年訂單統計
               	<button type="button" id="btnSave" title="儲存變更" onclick="formEvent.showSearchModal()" class="btn btn-plus-icon btn-default btn-save-icon" style="display:none"><i class="fas fa-search"></i></button>
               </li>
               <li class="breadcrumb-item">
               	<input type="text" id="qryYear" name="qryYear" maxlength="4" placeholder="yyyy" size="6" style="height:30px" class="text-center"/>
				<button type="button" id="btnQuery" class="btn btn-primary">查詢</button>
               </li>
           </ol>           
						           
          </div>    
      </div>      
      
      <!-- 上半部選單  -->      
      <!-- layout Start page-01-->
      <div class="row">        
        <!-- 右邊 Search result -->  
        <div class="col-md-12" id="hmsSlide_Content" >
          <!-- 查詢結果  -->
          <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;" id="result_dataList">                                   
            
            
            <!-- 搜尋結果 Search Result 區塊 -->
            <div class='card-body' style="overflow-y:auto">                           
              <!-- 搜尋結果 Search Result 區塊 -->
              <table id='table_queryResult' class='table-plus table-list width-fit-parent' style="width:100%">                            
                <thead>
                	  <tr>    
					    <th class="text-center" nowrap><input type="text" id="txtYear" name="txtYear" readonly class="readonly-noBG text-center" value="年度" size="6"/></th>
					    <th class="text-center" style="width:8%" nowrap>一</th>                    
					    <th class="text-center" style="width:8%" nowrap>二</th>
					    <th class="text-center" style="width:8%" nowrap>三</th>
					    <th class="text-center" style="width:8%" nowrap>四</th>
					    <th class="text-center" style="width:8%" nowrap>五</th>
					    <th class="text-center" style="width:8%" nowrap>六</th>
					    <th class="text-center" style="width:8%" nowrap>七</th>
					    <th class="text-center" style="width:8%" nowrap>八</th>
					    <th class="text-center" style="width:8%" nowrap>九</th>
					    <th class="text-center" style="width:8%" nowrap>十</th>
					    <th class="text-center" style="width:8%" nowrap>11</th>
					    <th class="text-center" style="width:8%" nowrap>12</th>
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
<div class="modal modal-plus fade" id="searchModal" tabindex="-1" role="dialog">
<div class="modal-dialog modal-sm" style="width:100%;" role="document" style="height:500px">
<div class="modal-content">
	<div class="modal-header"><h4 class="modal-title">統計年度(yyyy)</h4></div>        
	<div class="modal-body" style='width:100%;'>
	    <div class="row">      
	      <div class="col-md-12">
	        <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">	                                      
	          <div class='card-body' style="overflow-y:auto">
	          	         						                  	
	          </div>        
	        </div>              
	      </div>      
	    </div>  
	</div><!-- modal body -->

</div>
</div>
</div>


  
<c:import url="/_comm/jsp/hms.js.jsp?<%=URLUtils.getJsNoCacheParameter(application)%>" ></c:import>		
<script src='/HBD1/HBD1W662/js/HBD1W662_Events.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>		
<script src='/HBD1/common/js/Template.js?<%=URLUtils.getJsNoCacheParameter(application)%>'></script>			
<c:import url="/HBD1/HBD1W662/jsp/HBD1W662_Template.jsp"></c:import>		
</body>	
</html>
