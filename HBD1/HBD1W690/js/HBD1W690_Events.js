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
            
        	$('#mdlUnitPrice').on('shown.bs.modal', function() {
        		$("#txtSetUnitPrice").val('').focus();
        	});
            
            
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
        	
        	$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W690_ACTION'),
				data:{					
					actionMethod: 'jr',					
					methodName: 'PAGE_LOAD',
					qryInfo: evg.util.form.toString($('#queryCondTab'))
				},				
				global: false,
                type: 'post',
                dataType: 'json'                
            }).done(function(result) {
            	
                if(result.success) {                	
                	var template_obj = Handlebars.compile($('#hbt_ItemList').html());               	   
                    $("#qryItem").html(template_obj(result));    
                    
                    template_obj = Handlebars.compile($('#hbt_YearList').html());               	   
                    $("#qryYear").html(template_obj(result));
                    
                }else{
                	evg.util.fn.showWarning("查無資料");
                }
                
            });
        	
        },
        
                
        quickQuery: function(btn){        	
        	/**
        	 * 速查某個月的賣場資料
        	 */
        	$(btn).removeClass('btn-info').addClass('btn-warning');        	
        	let parmMonth = $(btn).attr("data-month");        	
        	$("#qryMonth").val(parmMonth);
        	$("button[name='btnQuickQuery']").removeClass('btn-warning').addClass('btn-info');        	
            formEvent.queryData();
        },
        
        queryData: function(){
        	
			$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W690_ACTION'),
				data:{					
					actionMethod: 'jr',					
					methodName: 'QUERY_DATA_LIST',
					qryInfo: evg.util.form.toString($('#queryCondTab'))
				},				
				global: false,
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
                    
                    //console.log('size: ' + result.data.ITEM_PRICE_LIST.length);
                    if(result.data.ITEM_PRICE_LIST.length ==0){
                    	$("#btnSave").hide();
                    	$("#btnDelete").hide();
                    	$("#btnCreatePrice").show();
                    	$("#btnSyncPrice").hide();
                    	let itemText = $("#qryItem option:selected").text();
                    	let pricYear = $("#qryYear").val();
                    	let title = '設定[' + itemText + ']-' + pricYear + '年價格';
                    	$("#btnCreatePrice").text(title);
                    	$("#modalTitle").text(title);
                    }else{
                    	$("#btnSave").show();
                    	$("#btnDelete").show();
                    	$("#btnSyncPrice").hide();
                    	$("#btnCreatePrice").hide();                    	
                    }                     
                    
                }else{
                	//evg.util.fn.showInfo("查無資料")
                	//evg.util.fn.showSuccess("查無資料")
                	//evg.util.fn.showError("查無資料")
                	evg.util.fn.showWarning("查無資料");
                }
                
            });
        },
        
        btnSubmitCreate_Click: function(){        	        	
        	
        	if($("#txtSetUnitPrice").val() == ''){
        		evg.util.fn.showWarning("請輸入商品價格");
        		$("#txtSetUnitPrice").focus();
        		return false;
        	}else{
        		$("#btnSubmitCreate").hide();        		
        	}
        	
        	$.ajax({
        		url: evg.util.fn.toActionWithTabKey('HBD1W690_ACTION'),
				data:{        					
					actionMethod: 'jc',       					
					methodName: 'CREATE_PRICE',
					info: evg.util.form.toString($('#queryCondTab')),
					pInfo: evg.util.form.toString($('#unitPriceTbl'))
				},	                                    	
                global:true,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {                   	
                if(result.success) {
                	//console.log('submit create click');
                	$("#mdlUnitPrice").modal('hide');
                	$("#btnCreatePrice").hide();
                	$("#btnSyncPrice").hide();                	                	
                	//formEvents.btnSyncPrice_Click();                	
                	$("#btnSubmitCreate").show();
                	evg.util.fn.showSuccess("價格設定成功");
                	let btnJan = $("#divQuickSearch button[name='btnQuickQuery']:first");
                	$(btnJan).trigger('click');
                }else{                	
                	evg.util.fn.showError("設定失敗");
                	$("#btnSubmitCreate").show();
                }                        
            });
        },
        
        btnCreatePrice_Click: function(){       	        	
        	$("#mdlUnitPrice").modal('show');
        },
        
        btnResetPrice_Click: function(){        	
        	$("#mdlUnitPrice").modal('show');        	
        },
        
        btnSyncPrice_Click: function(){
        	
        	evg.util.fn.showWarning("功能尚未開放");
        	return false;
        	
        	$.ajax({
        		url: evg.util.fn.toActionWithTabKey('HBD1W690_ACTION'),
				data:{        					
					actionMethod: 'jd',       					
					methodName: 'SYNC_PRICE',
					info: evg.util.form.toString($('#queryCondTab'))					
				},	                                    	
                global:true,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {                        
                if(result.success) {
                	console.log('submit SYNC click');                	
                	$("#btnSyncPrice").hide();
                	evg.util.fn.showSuccess("價格同步成功");
                }else{
                	evg.util.fn.showError("同步失敗");
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
        	
            return true;
        },
        
        btnDelete_Click: function(){
        	
        	let msg = "確定刪除商品價格(整年)?";        	
	        
        	evg.util.fn.confirm(msg, function(isConfirm) {            	
	            
        		if(isConfirm){	            	
	            	$.ajax({
                		url: evg.util.fn.toActionWithTabKey('HBD1W690_ACTION'),
        				data:{        					
        					actionMethod: 'jd',        					
        					methodName: 'DELETE_ITEM_PRICE',
        					qryInfo: evg.util.form.toString($('#queryCondTab'))
        					
        				},			                          	
                        global:true,
                        type: 'post',
                        dataType: 'json'
                    }).done(function(result) {
                        
                        if(result.success) {
                        	formEvent.queryData();        
                        	console.log(' delete ok');       
                        	$("#btnDelete").hide();
                        	evg.util.fn.showSuccess("刪除成功");
                        }else{
                        	console.log(' update fail');
                        	console.log(result.msg);
                        	//alert('存檔失敗');
                        	evg.util.fn.showError("刪除失敗!");
                        }
                        
                    });
	            	
	            }
	        });// end confirm

        	
        },
        
        submitSave: function(){
        	
        	// TODO: VALIDATOR
        	if(!formEvent.chkSaveCond()){
        		return false;
        	}else{
        		
        		//var trAddGroup  = evg.util.grid.getTrAddArr(resultTableID);            
                let trUpdGroup  = evg.util.grid.getTrFilterArr(resultTableID, {recordType:'now', update:'Y'});
                let totCnt = trUpdGroup.length;
                
                if (totCnt > 0){
                	
                	$.ajax({
                		url: evg.util.fn.toActionWithTabKey('HBD1W690_ACTION'),
        				data:{        					
        					actionMethod: 'ju',        					
        					methodName: 'SAVE_DATA_LIST',        					 
                            updInfo: evg.util.grid.toString(trUpdGroup) 
        				},			                          	
                        global:false,
                        type: 'post',
                        dataType: 'json'
                    }).done(function(result) {
                        
                        if(result.success) {
                        	formEvent.queryData();        
                        	console.log(' update ok');                        	
                        	evg.util.fn.showSuccess("已存檔(" + result.data.count + "筆)");
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
        	
        }
        
};
