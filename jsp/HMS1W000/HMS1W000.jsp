<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="com.hms.web.*, com.hms.util.*" %>
<%

System.out.println("Hello Web");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />      
    <title>首頁</title>
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
	<script type="text/javascript" src="/_ui/js/lov.js?1553743777205"></script>
	<script type="text/javascript" src="/_ui/js/upload.js?1553743777205"></script>
	
	<!-- no exist -->
	<script type="text/javascript" src="/_util_lib/plugin/popper/1.14.6/popper.js"></script>		
	<script type="text/javascript" src="/_util_lib/jquery-chosen/1.6.2/chosen.jquery.min.js"></script>
	<script type="text/javascript" src="/_util_lib/polyfill/es6-promise/4.1.0/es6-promise.auto.min.js" ></script>
	<script type="text/javascript" src="/_util_lib/jquery/bootbox.js" ></script>
	<script type="text/javascript" src="/_util_lib/jquery/jquery.noty.evg.js" ></script>	
	
	<script type="text/javascript" src="/comm/js/lightpicker/moment.min.js?1546935319164"></script>
	<script type="text/javascript" src="/comm/js/lightpicker/hms_lightpick.js?1546935319164"></script>  
	<script type="text/javascript" src="/comm/js/slideMenu/HMS2_SlideMenu.js?1546935319164"></script>  
	<script type="text/javascript" src="/comm/js/HMS2_Util.js?1546935319164"></script>
	
</head>
<body class='body-plus-default hms_style'>
  <div class='app_padding'>
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          
<nav class="navbar navbar-plus" style="border: 0;">
    <div class="navbar-header">
        <a class="navbar-brand" href="#">Purchasing System</a>        
    </div>   
</nav>
        </div>
      </div>
      <div class="row" >
          <div class='col-md-12'>
           <ol class="breadcrumb" style="margin-bottom: 10px;">
               <li class="breadcrumb-item">Purchasing System</li>
               <li class="breadcrumb-item">Material Data Maintain</li>                              
           </ol>           
        </div>    
      </div>      
      
      <!-- 上半部選單  -->      
      <!-- layout Start page-01-->
      <div class="row">
        <!-- 左邊 sidebar -->
        <div title="Expand" id="divOpenBarBtn" class="slideMenu_openBtn" style="display:none" onclick="HMS2_SlideMenu.slideMenuOpen()"><span class="fa fa-bars"></span></div>
        
        <div class="col-md-2" id="hmsSlide_Menu">
          <div class="row">                    
              <div class="col-md-12">
                <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
                  <div class='card-header text-large card-title'>                    
                      <div class="bar-head bar-dark-grey slideMenu_closeBtn">
                        <i class="bar-item bar-button fas fa-angle-left" title="Collapse" onclick="HMS2_SlideMenu.slideMenuClose('Y')"> Search Criteria</i>
                      </div> 
                  </div>                    
                  <div class='card-body'>                  
                        <!-- Search Criteria 區塊 -->
                        <table class='table-plus table-detail width-fit-parent' id='queryCondTab'>
                          <tbody>                            
                            <tr>                              
                              <th rowspan="2"><div style="text-align:left">Code Range</div></th>
                              <td colspan="2">
                                <input type="text" id="qryItemCode1" name="qryItemCode1" value="11011010" maxlength="8" required pattern="^[0-9]{8}$" data-evg-valid-title='Item code must have length 8!' data-evg-valid-msg-pattern='Incorrect item code format' style="width:100%; text-align: center" />
                              </td>
                            </tr>
                            <tr>                              
                              <td colspan="2">
                                <input type="text" id="qryItemCode2" name="qryItemCode2" value="11011234" maxlength="8" required pattern="^[0-9]{8}$" data-evg-valid-title='Item code must have length 8!' data-evg-valid-msg-pattern='Incorrect item code format' style="width:100%; text-align: center" />
                              </td>
                            </tr>                                                        
                            <tr>
                              <th><div style="text-align:left">Description</div></th>
                              <td colspan="2">
                                <input type="text" id="qryDesc" name="qryDesc" style="width:100%" />                                
                              </td>
                            </tr>                            
                            <tr>
                              <th><div style="text-align:left">Store</div></th>
                              <td colspan="2">
                                <select id="qryStore" name="qryStore" style="width:100%">
                                  <option value="*">ALL</option>                                  
                                  <option value="BVS">BVS</option>
                                  <option value="CST">CST</option>
                                  <option value="GST">GST</option>
                                  <option value="GRO">GRO</option>
                                  <option value="CNK">CNK</option>
                                </select>                                
                              </td>
                            </tr>                            
                            <tr>
                              <th><div style="text-align:left">Image Mark</div></th>
                              <td colspan="2">
                                <select id="qryImgMark" name="qryImgMark" style="width:100%">
                                  <option value="*">ALL</option>                                  
                                  <option value="Y">Yes - Exist file.</option>
                                  <option value="N">No - No file.</option>                                  
                                </select>
                              </td>
                            </tr>
                            
                            <tr>
                              <th><div style="text-align:left">Personal<br/>Custody</div></th>
                              <td colspan="2">
                                <select id="qryPersonalMark" name="qryPersonalMark" style="width:100%">
                                  <option value="*">ALL</option>                                  
                                  <option value="Y">Yes</option>
                                  <option value="N">No</option>                                  
                                </select>
                              </td>
                            </tr>
                            
                            <tr>
                              <th><div style="text-align:left">Void Mark</div></th>
                              <td colspan="2">
                                <select id="qryVoidMark" name="qryVoidMark" style="width:100%">
                                  <option value="*">ALL</option>                                  
                                  <option value="Y">Void</option>
                                  <option value="N">Active</option>                                  
                                </select>
                              </td>
                            </tr>
                            <tr>
                              <td colspan="3" align="center">
                                <input data-evg-action="HMP1W020_QueryMaterialList" type="button" id="queryBtn" class="btn btn-primary" value="Query" style="width:100%" />                                                                                        
                               </td>
                            </tr>
                          </tbody>
                        </table>
                        
                  </div>   
                </div>
              </div>                                  
            </div><!-- row end -->                       
        </div><!-- col-md-2  -->
        
        
        <!-- 右邊 Search result -->  
        <div class="col-md-10" id="hmsSlide_Content" >          
          <div class='card card-plus' style="margin-left: 0px; margin-right: 0px;" id="result_dataList">                                   
            <div class='card-header text-large card-title'>Search Result</div>            
            <div id='div_btn_codesetting_list' class='card-func page-line' data-evg-page-target='table_queryResult'>
                <div class='text-left' style='width: 60%; float: left;'>
                  <button type="button" id="showCreateDivBtn" class="btn btn-plus-icon btn-default"   title='Add Data' data-evg-action='HMP1W020_CreateData' ><i class="fas fa-plus"></i></button>                                    
                  <!-- <button type="button" id="undoDatasBtn"     class="btn btn-undo-icon btn-default"   title='Undo' data-evg-action='HMP1W020_CreateData' ><i class="fas fa-undo"></i></button> -->                                    
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
              <table class='table-plus table-list width-fit-parent' id='table_queryResult' style="width:100%">
                <thead>
                  <tr>
                    <th>Item Name</th>
                    <th style="width:5%; text-align:center">Store</th>
                    <th style="width:5%; text-align:center">U/M</th>                    
                    <th style="width:5%; text-align:center">Capacity</th>
                    <th style="width:5%; text-align:center">Void</th>
                    <th style="width:5%; text-align:center">IMG.</th>
                    <th style="width:5%; text-align:center">PER.</th>
                    <th style="width:5%; text-align:center">ACTION</th>
                  </tr>
                </thead>
                <tbody>
					<tr style="display: none;" data-evg-atrow="add">
                        <td align='center'>
                          <button type="button" onclick="$(this).parent().parent().remove()" class="btn btn-plus-icon btn-default btn-delete-icon" title='Delete'><i class="fas fa-trash"></i></button>
                        </td>
                        <td style="text-align: left"><input type="text" name="newQuotItem" onchange="quotItemChange(this, 'new')" style="width:80px" maxlength="8" required value="0" /></td>                        
                        <td style="text-align: left"><input type="text" name="newQuotItemLocDesc" style="width:250px" readonly /></td>
                        <td><input type="text" name="newItemUnit" readonly />
                          <input type="hidden" name="newQuotVend"   value='{{data.quotMast.quotVend}}'  />
                          <input type="hidden" name="newQuotDateB"  value='{{data.quotMast.quotDateB}}' />
                          <input type="hidden" name="newQuotDateE"  value='{{data.quotMast.quotDateE}}' />
                          <input type="hidden" name="newQuotSeq"    value='{{data.quotMast.quotSeq}}'   />
                        </td>
                        <td style="text-align: right">
                          <input type="number" name="newQuotUnitPrice" style="text-align: right; width:110px" maxlength="11" value="0" />
                        </td>                     
                        <td></td>
                        <td></td>                                                
                        <td></td>
                  	</tr>
                
                </tbody>
              </table>              
              <div id='div_nodata_Result'>Ｎｏ　Ｄａｔａ　Ｆｏｕｎｄ</div>              
            </div>    
          </div>
          </div>    
      </div><!-- row -->
      <!-- page01 -->
      <!-- layout End -->      
    </div><!-- container -->    
  </div><!-- appsetting -->


<div class="modal modal-plus fade" id="voidNoticeModal" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-lg" style="width:100%;" role="document">
    <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Void Notice Mail</h4>
        </div>
        <div class="modal-body" style='width:100%;'>          
          <table class='table-plus table-detail' id='voidNoticeTab'>                                    
            <tr>
                <th>Current Item Code</th>
                <td>
                  <input type="text" id="voidItemCode" name="voidItemCode" readonly />                                            
                </td>
                <td>
                  <input type="text" id="voidItemLocDesc" name="voidItemLocDesc" readonly />
                </td>
                <td>                  
                  <input type="button" id="submitVoidNoticeBtn" name="submitVoidNoticeBtn" value="Void Notice" class="btn btn-plus btn-default" data-evg-action="HMP1W020_SendVoidNotice" style="width:150px" />
                </td>                
            </tr>
            <tr>
                <th>Replace by Item Code</th>
                <td id="itemCodeContainer">                  
                  <input type="text" id="qryItemCode" name="qryItemCode" maxlength="8" pattern="^[0-9]{8}$" data-evg-valid-title='Item code must have length 8!' data-evg-valid-msg-pattern='Incorrect item code format' data-evg-lov-id="itemLov" />                                                              
                </td>
                <td>
                  <input type="text" id="qryDesc" name="qryDesc" />
                </td>
                <td>
                  <input type="button" id="submitReplaceNoticeBtn" name="submitReplaceNoticeBtn" value="Replace Notice"  class="btn btn-plus btn-default" data-evg-action="HMP1W020_SendReplaceNotice" style="width:150px" />
                </td>                
            </tr>            
          </table>
        </div>  
        <div class="modal-foot" style='width:99%;text-align:center;'>
          <input type="button" value="Close" class="btn btn-plus btn-default" data-dismiss="modal" /><br/>          
        </div>        
        <span style="height:10px"></span>
    </div>
  </div>
</div>

<script src='/jsp/HMS1W000/js/HMS1W000.js?2557456898072'></script>        
</body>
</html>