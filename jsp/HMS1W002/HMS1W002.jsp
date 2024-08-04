<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%
	
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />      
    <title>員工簽到退作業</title>
    <link rel="stylesheet" href="/_ui/css/3.0.0/Basic.css?v=1553743777205" type="text/css" />       
	<link rel="stylesheet" href="/_ui/css/3.0.0/Color.css?v=1553743777205" type="text/css" />    
	<link rel="stylesheet" href="/_ui/css/3.0.0/Fit.css?v=1553743777205" type="text/css" />

	<link rel="stylesheet" href="/_util_lib/bootstrap/4.3.1/css/bootstrap.min.css" type="text/css" >
	<link rel="stylesheet" href="/_util_lib/jquery-ui/1.12.1/css/jquery-ui.min.css" type="text/css" >
	<link rel="stylesheet" href="/_util_lib/jquery-chosen/1.6.2/chosen.min.css" type="text/css" >
	<link rel="stylesheet" href="/_util_lib/fontawesome/5.6.3/css/all.min.css" type="text/css" />
	
	
	<link rel="stylesheet" href="/comm/css/2.0.0/hmsStyle2.css?1546935319164" type="text/css" />    
	<link rel="stylesheet" href="/comm/css/2.0.0/hmsDataGrid.css?1546935319164" type="text/css" />
	<link rel="stylesheet" href="/comm/css/2.0.0/hmsDataGrid_thin.css?1546935319164" type="text/css" />    
	<link rel="stylesheet" href="/comm/css/2.0.0/hmsColorSet2.css?1546935319164" type="text/css" />
	<link rel="stylesheet" href="/comm/css/2.0.0/hmsStyle_FIT.css?1546935319164" type="text/css" />
	<!-- 
	<link rel="stylesheet" href="/comm/js/lightpicker/lightpick.css?1546935319164"  type="text/css">
	<link rel="stylesheet" href="/comm/js/lightpicker/lightpick_hmsClr.css?1546935319164"  type="text/css">  
	<link rel="stylesheet" href="/comm/js/slideMenu/HMS2_SlideMenuSylte.css?1546935319164"  type="text/css">  
	<link rel="stylesheet" href="/hmp1/css/HMP1.css" type="text/css">
	 -->
	<!-- javascript -->
	<!-- hms <script type="text/javascript" src="/_util_lib/jquery/3.1.0/jquery.min.js" ></script> -->
	<script type="text/javascript" src="/_util_lib/jquery/3.4.0/jquery.min.js" ></script>
	
	<script type="text/javascript" src="/_util_lib/jquery-ui/1.12.1/js/jquery-ui.min.js"></script>
	<script type="text/javascript" src="/_util_lib/bootstrap/4.3.1/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/_util_lib/js-template/handlebars/4.0.5/handlebars.js"></script>
	
	<script type="text/javascript" src="/_ui/js/fn.js?1553743777205"></script>	
	<script type="text/javascript" src="/_ui/js/batch.js?1553743777205" ></script>
	<script type="text/javascript" src="/_ui/js/evg_noty_theme.js?1553743777205" ></script>
	<script type="text/javascript" src="/_ui/js/form.js?1553743777205" ></script>
	<script type="text/javascript" src="/_ui/js/ui.js?1553743777205" ></script>
	<script type="text/javascript" src="/_ui/js/page2.js?1553743777205"></script>
	<script type="text/javascript" src="/_ui/js/grid2.js?1553743777205"></script>
	<script type="text/javascript" src="/_ui/js/validator.js?1553743777205"></script>
	<!-- <script type="text/javascript" src="/_ui/js/lov.js?1553743777205"></script> -->
	<!-- <script type="text/javascript" src="/_ui/js/upload.js?1553743777205"></script> -->
	
	<!-- no exist -->
	<script type="text/javascript" src="/_util_lib/plugin/popper/1.14.6/popper.js"></script>		
	<script type="text/javascript" src="/_util_lib/jquery-chosen/1.6.2/chosen.jquery.min.js"></script>
	<script type="text/javascript" src="/_util_lib/polyfill/es6-promise/4.1.0/es6-promise.auto.min.js" ></script>
	<script type="text/javascript" src="/_util_lib/jquery/bootbox.js" ></script>
	<!-- <script type="text/javascript" src="/_util_lib/jquery/jquery.noty.evg.js" ></script> -->	
	
	<script type="text/javascript" src="/comm/js/lightpicker/moment.min.js?1546935319164"></script>
	<script type="text/javascript" src="/comm/js/lightpicker/hms_lightpick.js?1546935319164"></script>  
	<script type="text/javascript" src="/comm/js/slideMenu/HMS2_SlideMenu.js?1546935319164"></script>  
	<script type="text/javascript" src="/comm/js/HMS2_Util.js?1546935319164"></script>

<!-- Notify js -->
<script src='/js/jquery/noty/noty.js'></script>
<link type="text/css" href="/js/jquery/noty/noty.css" media="screen" rel="stylesheet" />

<!-- Browser Detect -->
<script src='/js/jquery/jquery.browser.detect.js'></script>	
	
</head>
<body class='body-plus-default hms_style'>
  <div class='app_padding'>
    <div class="container-fluid">
      <div class="row" style="display:none">
        <div class="col-md-12">          
			<nav class="navbar navbar-plus" style="border: 0;">
    			<div class="navbar-header">
        			<a class="navbar-brand" href="#">員工簽到退報表</a>        
			    </div>   	
			</nav>
        </div>
      </div>
      <div class="row" >
          <div class='col-md-12'>
           <ol class="breadcrumb" style="margin-bottom: 10px;">
               <li class="breadcrumb-item">牙語系統</li>
               <li class="breadcrumb-item">員工簽到退報表</li>                              
           </ol>           
        </div>    
      </div>
      
     
      
      <!-- 上半部選單  -->      
      <!-- layout Start page-01-->
      <div class="row">
        <!-- 左邊 sidebar -->
        <div title="Expand" id="divOpenBarBtn" class="slideMenu_openBtn" style="display:none" onclick="HMS2_SlideMenu.slideMenuOpen()"><span class="fa fa-bars"></span></div>
        
        <div class="col-md-12" id="hmsSlide_Menu">
          <div class="row">          
			<div class="col-md-2">
                <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
                  <div class='card-header text-large card-title'>                    
                      <div class="bar-head bar-dark-grey slideMenu_closeBtn">查詢條件 </div> 
                  </div>                    
                  <div class='card-body'>                  
                        <!-- Search Criteria 區塊 -->                        
                        <table class='table-plus table-detail width-fit-parent' id='queryTab' style="width:100%">
                          <tbody>                                                
                            <tr>
                              <th><div style="text-align:right">帳號</div></th>
                              <td colspan="2">
                                <select id="qryUserName" name="qryUserName" style="width:100%">                                                                    
                                </select>
                              </td>
                            </tr>                            
                            <tr>
                              <th><div style="text-align:right">日期</div></th>
                              <td colspan="2">                                
                                <input type="text" id="qrySignDate" name="qrySignDate" required maxlength="8" style="width:100%">
                              </td>
                            </tr>
							<tr>                              
                              <td colspan="3">                                
                                <input type="button" id="btnQuery" class="btn btn-primary" value="查詢" style="width:100%" />
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        
                  </div>   
                </div>			
          	</div>          
             <div class="col-md-10">
          <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;" id="result_dataList">                                   
            <div class='card-header text-large card-title'>查詢結果</div>                        
            <!-- 搜尋結果 Search Result 區塊 -->
            <div class='card-body' style="overflow-y:auto; height:600px">                           
              <!-- 搜尋結果 Search Result 區塊 -->
              <table class='table-plus table-list width-fit-parent' id='table_queryResult' style="width:100%">
                <thead>
                  <tr>                    
                    <th style="width:15%; text-align:center">日期</th>
                    <th style="width:15%; text-align:center">姓名</th>                    
                    <th style="width:15%; text-align:center">簽到時間</th>
                    <th style="width:15%; text-align:center">簽退時間</th>                    
                    <th style="text-align:center">ACTION</th>
                  </tr>
                </thead>
                <tbody>                
                </tbody>
              </table>                            
            </div>    
          </div>             
             </div>                                               
            </div><!-- row end -->                       
        </div><!-- col-md-12  -->        
            
      </div><!-- row -->
      <!-- page01 -->
      <!-- layout End -->      
    </div><!-- container -->    
  </div><!-- appsetting -->

<script src='/jsp/HMS1W002/js/HMS1W002.js?2557456898072'></script> 
<script id='hbt_SignRecords' type='text/x-handlebars-template'>
<tbody>
  {{#data}}
	<tr>
		<td>{{SLOG_DATE}}</td>
		<td>{{SLOG_USER_NAME}}</td>		
		<td>{{SLOG_ACT_ON_TIME}}</td>
		<td>{{SLOG_ACT_OFF_TIME}}</td>
		<td></td>
	</tr>
{{/data}}
</tbody>
</script>       
<script id='hbt_UserOptions' type='text/x-handlebars-template'>
    <option value="">Select</option>  
    {{#data}}
      <option value="{{USER_NAME}}">{{USER_NAME}}</option>
    {{/data}}
</script>
</body>
</html>