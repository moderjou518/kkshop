//放搜尋結果table的id 
var resultTableID = "table_queryResult";

//放常用資料JSON
var hmp1MiscData = {};

var today = new evg.util.date.iDate();
//var thisMonth = today.format("YYYYMMDD");
var thisYear = today.format("YYYY");
var thisMonth = today.format("MM");
var thisDay = today.format("DD");
var todayStr = today.format("YYYYMMDD");

$(document).ready(function() {    

    formEvent.init();
    formEvent.bindEvents();    
    formEvent.pageLoad();
    templateHelper.register();
    
});

// 主程式
var formEvent = {
        
        init: function(){            
        	// 設定月曆            
            $("#qryTrnsDate1, #qryTrnsDate2").datepicker({
                changeMonth: true,
                changeYear: true,
                showButtonPanel: true,                    
                dateFormat: 'yymmdd'
            });
            
            // 預設查當天的資料
            $("#qryTrnsDate1").val(todayStr);
            $("#qryTrnsDate2").val(todayStr);
            
            
        },
        
        bindEvents: function(){        
        	
            //$("#queryBtn").on("click", formEvent.queryData);
            $("#btnAddRow").on("click", formEvent.addAData);            
            $("#btnSave").on("click", formEvent.saveGridData);            
            
            // 設定月曆            
            $("#txtTrnsDate").datepicker({
                changeMonth: true,
                changeYear: true,
                showButtonPanel: true,                    
                dateFormat: 'yymmdd'
            });
            
            // 按下 enter 就自動 query
            $("#qryTketName").keypress(function (event) {
                if (event.keyCode == 13) {
                    $("#queryBtn").trigger("click");
                }
            });
            
        },       
        
        pageLoad: function(){
        	
        	$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W330_ACTION'),
				data:{					
					actionMethod: 'jr',					
					methodName: 'PAGE_LOAD'					
				},				
				global: false,
                type: 'post',
                dataType: 'json'                
            }).done(function(result) {
            	
                if(result.success) {
                	var template_obj = Handlebars.compile($('#hbt_ItemList').html());               	   
                    $("#dlItemCode").html(template_obj(result));          
                    
                    template_obj = Handlebars.compile($('#hbt_AddItemList').html());
                    $("#table_addItemList>tbody").replaceWith(template_obj(result));
                    
                    evg.util.grid.init('table_addItemList', 1);                       
                    evg.util.form.setOriValue($('#table_addItemList'));
                    evg.util.form.markChangeBind($('#table_addItemList').find(':input').not('[name="cbChk"]'));                    
                    
                }else{
                	evg.util.fn.showWarning("查無資料");
                }
                
            });
        	
        	
        	
        },
        
        openQuery: function(){
        	$('#showSearchModal').modal('show');
        },
        
        showAddData: function(){
    	   
        },
        
        addAData: function(){        	
            // 新增一筆資料
        	//$('#showDataModal').modal('show');
        	$('#addDataModal').modal('show');
        	
        	$("#btnAddOne").show();
        	$("#btnSaveOne").hide();
        	$("#btnDelOne").hide();
        	
        	$("#dlItemCode").val('*');
        	$("#txtTrnsDate").val(todayStr);
        	$("#txtTrnsQty").val('');
        	$("#txtCacpNotes").val('');
        	$("#dlCard_ID").val('*');
        	$("#txtTrnsAmt").val('');
        	$("#txtTrnsMark").val('');        	
        	//$("#txtVoidMark").prop("checked", false);
        	
        },
        
        submitSaveList: function(){
        	
        	let trUpdGroup  = evg.util.grid.getTrFilterArr('table_addItemList', {recordType:'now', update:'Y'});
        	console.log('tableID: table_addItemList');
        	console.log(trUpdGroup);
        	console.log(evg.util.grid.toString(trUpdGroup));
        	console.log('submitSaveList test');
        	//alert('submitSaveList test');
            //return false;
            
            
            $.ajax({
        		url: evg.util.fn.toActionWithTabKey('HBD1W330_ACTION'),
				data:{				
					actionMethod: 'jc',        					
					methodName: 'ADD_DATA_LIST',
					updInfo: evg.util.grid.toString(trUpdGroup)                     
				},			                          	
                global:false,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {
                
                if(result.success) {
                	formEvent.queryData();       
                	$('#showDataModal').modal('hide');
                	evg.util.fn.showSuccess("完成更新");
                	
                	// reset input
                	$("#table_addItemList>tbody").find("input[name='txtTrnsQty']").each(function(){
                		$(this).val('');
                	});
                	
                }else{                	
                	console.log(result.msg);
                	evg.util.fn.showError("存檔失敗，請檢查資料是否重覆或文字長度過長");
                }
                
            });
        	
        },
     

     
        quickQuery: function(btn){
        },
        
        queryData: function(){
        	
        	console.log('queryData----');
        	console.log('qryInfo: ' + evg.util.form.toString($('#queryCondTab')));
        	
			$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W330_ACTION'),
				data:{				
					actionMethod: 'jr',
					qryInfo: evg.util.form.toString($('#qryForm')),
					methodName: 'QUERY_DATA_LIST'					
				},				
				global: false,
                type: 'post',
                dataType: 'json'                
            }).done(function(result) {
            	
                if(result.success) {
                	
                	result.data.ITEMCLASS = hmp1MiscData.ITEMCLASS;
                	
                	var template_obj = Handlebars.compile($('#hbt_QueryResult').html());
                    $("#" + resultTableID + " tbody").replaceWith(template_obj(result));
                    
                    if (result.data.TRNS_LIST.length == 0){
                    	var range = $("#qryTrnsDate1").val() + '~' + $("#qryTrnsDate2").val(); 
                    	evg.util.fn.showInfo(range + " 查無資料");
                    }else{
                    	// 初始化Grid, 塞 data-evg-orivalue
                        evg.util.grid.init(resultTableID, 1);                       
                        evg.util.form.setOriValue($('#' + resultTableID));
                        evg.util.form.markChangeBind($('#' + resultTableID).find(':input').not('[name="cbChk"]'));
                        $('#showSearchModal').modal('hide');
                    }
                    
                    
                    
                }else{
                	//evg.util.fn.showSuccess("查無資料")
                	//evg.util.fn.showError("查無資料")
                	evg.util.fn.showWarning("查無資料");
                }
                
            });
        },
        
        showData: function(btnShow){
        	
        	var trObj = $(btnShow).parent().parent();
        	var wkCacpdUuid = $(trObj).find("input[name='txtCacpdUuid']").val();
        	var wkCacpRecpCode = $(trObj).find("input[name='txtCacpRecpCode']").val();
        	var wkCacpQty = $(trObj).find("input[name='txtCacpQty']").val();        	
        	var wkCacpNotes = $(trObj).find("input[name='txtCacpNotes']").val();
        	
        	$("#txtCacpdUuid").val(wkCacpdUuid);
        	$("#dlItemCode").val(wkCacpRecpCode);
        	$("#txtTrnsQty").val(wkCacpQty);
        	$("#txtCacpNotes").val(wkCacpNotes);       	
        	        	
        	$('#showDataModal').modal('show');      
        	$("#btnAddOne").hide();
        	$("#btnDelOne").show();
        	$("#btnSaveOne").show();        	
        	
        	        	
        	 	
        	
        },
        
        // 格式檢查
        chkSaveCond: function(){
        	
            // 進行檢查，有錯誤就提示 & 停止    
        	/*
        	if(evg.util.validator.validate($("#" + resultTableID + " tbody tr:visible")).getErrorNum()!==0){
                return false;
            }
            */
        	
        	return true;
            
        },
        
        deleteConfirm: function(){        	
        	
        	var msg = "確定要刪除此筆資料嗎?";
        	
            evg.util.fn.confirm(msg, function(isConfirm) {            	
                if(isConfirm){                	
                	$.ajax({
                		url: evg.util.fn.toActionWithTabKey('HBD1W330_ACTION'),
        				data:{				
        					actionMethod: 'jd',        					
        					methodName: 'DELETE_THIS_DATA',
        					delID: $("#txtCacpdUuid").val() 
        				},	                                    	
                        global:false,
                        type: 'post',
                        dataType: 'json'
                    }).done(function(result) {                        
                        if(result.success) {
                        	$('#showDataModal').modal('hide');                             	        	
                        	
                        	$("#txtTrnsUuid").val('');
                        	$("#txtTrnsDate").val('');
                        	$("#txtTrnsSeq").val('');
                        	$("#dlCard_ID").val('*');
                        	$("#dlItemCode").val('*');        	
                        	$("#txtTrnsQty").val('');        	
                        	$("#txtTrnsAmt").val('');
                        	formEvent.queryData();
                        	evg.util.fn.showSuccess("刪除成功");
                        }else{
                        	evg.util.fn.showError("資料刪除失敗");
                        }                        
                    });                	
                }else{
                	//evg.util.fn.showError("取消刪除");
                }
            });// end confirm
        	
        	
        },
        
        submitSaveOne: function(){
        	
        	console.log('submitSaveOne'); 
        	
        	if(evg.util.validator.validate($("#showDataModal")).getErrorNum()!==0){
                return false;
            }
        	
        	
        	$.ajax({
        		url: evg.util.fn.toActionWithTabKey('HBD1W330_ACTION'),
				data:{				
					actionMethod: 'ju',        					
					methodName: 'SAVE_DATA_ONE',
					info: evg.util.form.toString($('#showDataModal'))                     
				},			                          	
                global:false,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {
                
                if(result.success) {
                	formEvent.queryData();       
                	$('#showDataModal').modal('hide');
                	evg.util.fn.showSuccess("完成更新");
                }else{                	
                	console.log(result.msg);
                	evg.util.fn.showError("存檔失敗，請檢查資料是否重覆或文字長度過長");
                }
                
            });
        	
        },
        
        submitAddOne: function(){
        	
        	console.log('submitAddOne');
        	
        	if(evg.util.validator.validate($("#showDataModal")).getErrorNum()!==0){
                return false;
            }else{
            	if($("#dlItemCode").val() == '*'){
            		evg.util.fn.showWarning("請選擇物料名稱");
            		return false;
            	}
            	
            	if($("#dlCard_ID").val() == '*'){
            		evg.util.fn.showWarning("請選擇物料類別");
            		return false;
            	}
            }      	
        	
        	
        	
        	$.ajax({
        		url: evg.util.fn.toActionWithTabKey('HBD1W330_ACTION'),
				data:{				
					actionMethod: 'jc',        					
					methodName: 'ADD_DATA_ONE',
					info: evg.util.form.toString($('#showDataModal'))                     
				},			                          	
                global:false,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {
                
                if(result.success) {
                	formEvent.queryData();       
                	$('#showDataModal').modal('hide');
                	evg.util.fn.showSuccess("完成新增");
                }else{                	
                	console.log(result.msg);
                	evg.util.fn.showError("新增失敗，請檢查資料是否重覆或文字長度過長");
                }
                
            });
        	
        }
                
        
        
                
       
        
};
