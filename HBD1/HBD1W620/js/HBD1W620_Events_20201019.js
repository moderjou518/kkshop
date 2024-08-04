//放搜尋結果table的id 
var resultTableID = "table_queryResult";

//放常用資料JSON
var hmp1MiscData = {};

$(document).ready(function() {    

    formEvent.init();
    formEvent.bindEvents();    
    formEvent.pageLoad();
    templateHelper.register();
    
});

// 主程式
var formEvent = {
        
        init: function(){            
            
            
            
        },
        
        bindEvents: function(){        
        	
            $("#queryBtn").on("click", formEvent.queryData);
            $("#btnAddRow").on("click", formEvent.addARow);            
            $("#btnSave").on("click", formEvent.saveGridData);
            
            // 按下 enter 就自動 query
            $("#qryTketName").keypress(function (event) {
                if (event.keyCode == 13) {
                    $("#queryBtn").trigger("click");
                }
            });
            
        },       
        
        pageLoad: function(){
        	
        	var compCode = $("#txtCompCode").val();
        	//console.log('pageload js cid: ' + compCode);       	
        	
        	// load misc
            $.ajax({
            	url: evg.util.fn.toActionWithTabKey('HBD1W620_ACTION'),
				data:{					
					actionMethod: 'jr',
					methodName: 'PAGE_LOAD'					
				},
				//global: false,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {
                
                
                if(result.success) {                       

                	/*
                    var template_obj = Handlebars.compile($('#hbt_QueryResult').html());
                    $("#" + resultTableID + " tbody").replaceWith(template_obj(result));
                    
                    // 初始化Grid, 塞 data-evg-orivalue
                    evg.util.grid.init(resultTableID, 1);                       
                    evg.util.form.setOriValue($('#' + resultTableID));
                    evg.util.form.markChangeBind($('#' + resultTableID).find(':input').not('[name="cbChk"]'));
                    
                    */                                  
                }else{
                    evg.util.fn.showError("Failed to pageLoad !");
                }
                
            });
        	
        },
        
        addARow: function(){            
        	
        	// 自動 focus 在新增的 input
            evg.util.grid.addOne(resultTableID);            
            var newRow = $("#" + resultTableID + " tbody tr:visible:first");
            //$(newRow).find("input[name='txtTketLmtMon']").val(hmp1MiscData.contractMonth);
            $(newRow).find("input[name='txtBidName']").focus();
            //$(newRow).find("input[name='txtTketTypeData']").bind('change', );
            
            // 初始化月曆            
            $(newRow).find("[name='txtBidSaleSdate'],[name='txtBidSaleEdate']").datepicker({
                changeMonth: true,
                changeYear: true,
                showButtonPanel: true,                    
                dateFormat: 'yymmdd'
            });
            
            
            
        },
        
        changeType: function(dlType){       	        	
        	var selVal = $(dlType).val().split('-');        	
        	var newRow = $(dlType).parent().parent();
        	$(newRow).find("input[name='txtTketCode']").focus();
        	
        	if (selVal.length == 3){        		
        		// var newTketType = selVal[0];
        		// var newTketMpMark = selVal[2]; 市價註記            	
        		var newTketEtMark = selVal[1]; // 出券註記
        		$(newRow).find("span[name='txtTketEtMark']").html(newTketEtMark);
        	}else{
        		$(newRow).find("span[name='txtTketEtMark']").html('');
        	}        			
        },     

        
        form2String: function(){
        	console.log('form2String ok');
        },
        
        quickQuery: function(btn){
        	// reset value & set query type
        	
        	//var qryYear = $("#qryYear").val();
        	//var qryMonth = qryYera + parmMonth;
        	$("button[name='btnQuickQuery']").removeClass('btn-warning').addClass('btn-info');
        	$(btn).removeClass('btn-info').addClass('btn-warning');
        	
        	let parmMonth = $(btn).attr("data-month");
        	
        	$("#qryMonth").val(parmMonth);
        	
        	//console.log('quickYear: ' + qryYear);
        	//console.log('quickMonth: ' + parmMonth);
        	
            formEvent.queryData();
        },
        
        queryData: function(){
        	
			$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W620_ACTION'),
				data:{					
					actionMethod: 'jr',					
					methodName: 'QUERY_DATA_LIST',
					qryInfo: evg.util.form.toString($('#queryCondTab'))
				},				
				//global: false,
                type: 'post',
                dataType: 'json'                
            }).done(function(result) {
            	
                if(result.success) {
                	
                	
                	
                	var template_obj = Handlebars.compile($('#hbt_QueryResult').html());
                    $("#" + resultTableID + " tbody").replaceWith(template_obj(result));
                    
                    // 初始化Grid, 塞 data-evg-orivalue
                    evg.util.grid.init(resultTableID, 1);                       
                    evg.util.form.setOriValue($('#' + resultTableID));
                    evg.util.form.markChangeBind($('#' + resultTableID).find(':input').not('[name="cbChk"]'));
                    
                    
                    // 設定月曆
                    $("#" + resultTableID + " tbody tr:visible").find("[name='txtBidSaleSdate'],[name='txtBidSaleEdate']").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,                    
                        dateFormat: 'yymmdd'
                    });                    
                    
                }else{
                	//evg.util.fn.showInfo("查無資料")
                	//evg.util.fn.showSuccess("查無資料")
                	//evg.util.fn.showError("查無資料")
                	evg.util.fn.showWarning("查無資料");
                }
                
            });
        },
        
        // 格式檢查
        chkSaveCond: function(){
        	
        	
            // 進行檢查，有錯誤就提示 & 停止          	
        	if(evg.util.validator.validate($("#" + resultTableID + " tbody tr:visible")).getErrorNum()!==0){        		
        		evg.util.fn.showError("資料不完整，請檢查欄位是否正確");
                return false;
            }

        	// 券種類別不可為空
        	chk = 0;
        	$('#' + resultTableID + " tbody tr:visible").find('input:visible[name=txtBidOrdSdate],input:visible[name=txtBidOrdEdate],input:visible[name=txtBidSaleSdate],input:visible[name=txtSaleOrdEdate]').each(function(i, el) {
        		if($(el).val() == ''){       			
        			chk ++;
        			//$(el).focus();
        			evg.util.fn.showError("日期不可為空");
        			return;
        		}
        	});        	
        	
        	if (chk > 0){
        		
        		return false;
        	}else{
        		return true;	
        	}        	
        	
        	
            
        },
        
        submitSave: function(){
        	
        	// TODO: VALIDATOR
        	if(!formEvent.chkSaveCond()){
        		return false;
        	}else{
        		
        		var trAddGroup  = evg.util.grid.getTrAddArr(resultTableID);            
                var trUpdGroup  = evg.util.grid.getTrFilterArr(resultTableID, {recordType:'now', update:'Y'});
                var totCnt = trAddGroup.length + trUpdGroup.length;
                
                if (totCnt > 0){
                	
                	$.ajax({
                		url: evg.util.fn.toActionWithTabKey('HBD1W620_ACTION'),
        				data:{        					
        					actionMethod: 'ju',        					
        					methodName: 'SAVE_DATA_LIST',
        					addInfo: evg.util.grid.toString(trAddGroup), 
                            updInfo: evg.util.grid.toString(trUpdGroup) 
        				},			                          	
                        global:false,
                        type: 'post',
                        dataType: 'json'
                    }).done(function(result) {
                        
                        if(result.success) {
                        	formEvent.queryData();        
                        	console.log(' update ok');                        	
                        	evg.util.fn.showSuccess("完成更新");
                        }else{
                        	console.log(' update fail');
                        	console.log(result.msg);
                        	//alert('存檔失敗');
                        	evg.util.fn.showError("資料新增/更新失敗，請檢查資料長度是否過長");
                        }
                        
                    });
                	
                }else{
                	evg.util.fn.showInfo("No changes save.");
                }
        		
        	}
        	
        },
        
        loadBidPrice: function(trIndex){
        	
        	var trObj = $("#" + resultTableID + " tr[data-rowidx=" + trIndex + "]");       	        	
        	var sDate = $(trObj).find("input[name='txtBidOrdSdate']").val();
        	var eDate = $(trObj).find("input[name='txtBidOrdEdate']").val();
        	var bidUuid = $(trObj).find("input[name='txtBidUuid']").val();
        	
        	$('#setPriceModal').modal('show');        	
        	$("#modalTitle").html('[' + sDate + ' ~ ' + eDate + '] 商品價格設定');     
        	
        	console.log('bidUUID: ' + bidUuid);
        	
        	$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W620_ACTION'),
				data:{					
					actionMethod: 'jr',					
					methodName: 'QUERY_SUB_DATA_LIST',
					qryBidUuid: bidUuid
				},				
				global: false,
                type: 'post',
                dataType: 'json'                
            }).done(function(result) {
            	
                if(result.success) {
                	
                	var template_obj = Handlebars.compile($('#hbt_QuerySubResult').html());
                    $("#subTableQueryResult tbody").replaceWith(template_obj(result));                    
                    
                    evg.util.grid.init('subTableQueryResult', 1);                       
                    evg.util.form.setOriValue($('#subTableQueryResult'));
                    evg.util.form.markChangeBind($('#subTableQueryResult').find(':input').not('[name="cbChk"]'));
                    
                    
                }else{
                	//evg.util.fn.showInfo("查無資料")
                	//evg.util.fn.showSuccess("查無資料")
                	//evg.util.fn.showError("查無資料")
                	evg.util.fn.showWarning("查無資料");
                }
                
            });
        	
        },              
        
        deleteThisRow: function(btnDelete){
        	
        	var trObj = $(btnDelete).parent().parent();        	
        	var rid = $(trObj).find("input[name='txtBidUuid']").val();
        	console.log('uuid: ' + rid);
        	var msg = "確定要刪除此筆資料嗎?";
        	
            evg.util.fn.confirm(msg, function(isConfirm) {            	
                if(isConfirm){                	
                	$.ajax({
                		url: evg.util.fn.toActionWithTabKey('HBD1W620_ACTION'),
        				data:{        					
        					actionMethod: 'jd',       					
        					//methodName: 'DELETE_THIS_DATA',
        					delID: rid 
        				},	                                    	
                        global:false,
                        type: 'post',
                        dataType: 'json'
                    }).done(function(result) {                        
                        if(result.success) {
                        	$(trObj).remove();
                        	evg.util.fn.showSuccess("完成刪除");
                        }else{
                        	evg.util.fn.showError("資料刪除失敗");
                        }                        
                    });                	
                }else{
                	evg.util.fn.showError("取消刪除");
                }
            });// end confirm
        	
        	
        },
        
        saveModifyPrice: function(){
        	
        	var subResultTable = "subTableQueryResult";        	            
            var trUpdGroup  = evg.util.grid.getTrFilterArr(subResultTable, {recordType:'now', update:'Y'});
            var totCnt = trUpdGroup.length;
            
            if (totCnt > 0){
            	
            	$.ajax({
            		url: evg.util.fn.toActionWithTabKey('HBD1W620_ACTION'),
    				data:{				
    					actionMethod: 'ju',        					
    					methodName: 'SAVE_SUB_DATA_LIST',    					 
                        updInfo: evg.util.grid.toString(trUpdGroup) 
    				},			                          	
                    global:false,
                    type: 'post',
                    dataType: 'json'
                }).done(function(result) {
                    
                    if(result.success) {
                    	//formEvent.loadBidPrice();        
                    	//console.log(' update ok');                        	
                    	evg.util.fn.showSuccess("完成更新");
                    }else{
                    	console.log(' update fail');
                    	console.log(result.msg);
                    	//alert('存檔失敗');
                    	evg.util.fn.showError("資料新增/更新失敗，請檢查資料長度是否過長");
                    }
                    
                });
            	
            }else{
            	evg.util.fn.showInfo("No changes save.");
            }

        }
        
                
       
        
};
