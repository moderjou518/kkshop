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
		
		bindEvents: function(){                	
            $("#btnQuery").on("click", formEvent.queryData);            
        },
        
        init: function(){            
            $("#qryMonth").val(thisYear + thisMonth);
            
            
            //$("#qryPoDate2").val(todayStr);
        },                     
        
        pageLoad: function(){       	        	        	
        },        
        
        showSearchModal: function(){
        	$('#searchModal').modal('show');
        },
                
        showDetailModal: function(buyer, poDate){
        	
        	//console.log('showDetailModal: ' + bidUUID);   	    	
        	
        	// 載入某一周預訂單商品
    		$.ajax({
    	    	url: evg.util.fn.toActionWithTabKey('HBD1W661_ACTION'),
    			data:{			
    				//tabKey: myTabKey,
    				actionMethod: 'jr',
    				methodName: 'LOAD_BUYER_DAILY_PO',
    				parmBuyer: buyer,
    				parmPoDate: poDate
    			},
    			global: false,
    	        type: 'post',
    	        dataType: 'json'
    	    }).done(function(result) {    		    	
    	        if(result.success) {            
    	            
    	        	var containerID = "tblWeekPOList";	        	
    	        	
    	        	if(result.data.DAILY_PO.length > 0){
    	        		
    	        		var template_obj = Handlebars.compile($('#hbt_DailyPoItemList').html());	        		
    	        		$("#" + containerID + " tbody").replaceWith(template_obj(result));
    	                
    	                // 初始化Grid, 塞 data-evg-orivalue
    	                evg.util.grid.init(containerID, 1);                       
    	                evg.util.form.setOriValue($('#' + containerID));
    	                evg.util.form.markChangeBind($('#' + containerID).find(':input').not('[name="cbChk"]'));
    	                $('#detailModal').modal('show');
    	                
    	                // 統計
    	                

    	        	}else{       			        		
    	        		evg.util.fn.showInfo("沒有訂單資料");	        		
    	        	}                        
    	            
    	        }else{
    	            evg.util.fn.showError("Failed to load list !");
    	        }	        
    	    });
    		
    		console.log('done done');
        	
        },
        
        queryData: function(){
        	
        	
        	console.log('661 queryData');
			$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W661_ACTION'),
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
                	
                	console.log(result.data);
                	
                	var template_obj = Handlebars.compile($('#hbt_QueryResult').html());
                    $("#" + resultTableID + " tbody").replaceWith(template_obj(result));
                    template_obj = Handlebars.compile($('#hbt_QueryHeader').html());
                    $("#" + resultTableID + " thead").replaceWith(template_obj(result));
                    template_obj = Handlebars.compile($('#hbt_QueryFooter').html());
                    $("#" + resultTableID + " tfoot").replaceWith(template_obj(result));
                    
                    // 初始化Grid, 塞 data-evg-orivalue
                    evg.util.grid.init(resultTableID, 1);                       
                    evg.util.form.setOriValue($('#' + resultTableID));
                    evg.util.form.markChangeBind($('#' + resultTableID).find(':input').not('[name="cbChk"]'));
                    
                    
	                
                    
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
        	/*
        	if(evg.util.validator.validate($("#" + resultTableID + " tbody tr:visible")).getErrorNum()!==0){
                return false;
            }
            */
        	
        	return true;
            
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
                		url: evg.util.fn.toActionWithTabKey('HBD1W661_ACTION'),
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
                        	evg.util.fn.showError("資料新增/更新失敗，請檢查代碼是否重覆或資料長度是否過長");
                        }
                        
                    });
                	
                }else{
                	evg.util.fn.showInfo("No changes save.");
                }
        		
        	}
        	
        },       
        

        openMoreUseDescModal: function(moreLink){
        	// 紀錄目前位置
        	var currRow = $(moreLink).parent().parent();
        	$("#txtAtRow").val($(currRow).attr("data-evg-atrow"));
        	$("#txtMoreUseDesc").val($(currRow).find("textarea[name='txtTketUseDesc']").val());
        	$('#moreUseDescModal').modal('show');        	
        },
        
        modifyUseDesc: function(){
        	// srcRow
        	var atRowIdx = $("#txtAtRow").val();
        	var srcRow = $("#" + resultTableID + " tr[data-evg-atrow=" + atRowIdx + "]");        	
        	$(srcRow).find("textarea[name='txtTketUseDesc']").val($("#txtMoreUseDesc").val());
        	$('#moreUseDescModal').modal('hide');
        	$("#txtMoreUseDesc").val('');
        }
        
                
       
        
};
