﻿//放搜尋結果table的id 
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
        	
        	// 設定月曆            
            $("#qryPoDate1").datepicker({
                changeMonth: true,
                changeYear: true,
                showButtonPanel: true,                    
                dateFormat: 'yymmdd'
            });
            
            $("#qryPoDate1").val(todayStr);
            //$("#qryPoDate2").val(todayStr);
        },                     
        
        pageLoad: function(){       	        	        	
        },        
                
        showDetailModal: function(buyer, poDate, poSeq, modalTitle){
        	
        	//console.log('showDetailModal: ' + bidUUID);   	    	
        	$("#txtPommBuyer").val(buyer);
        	$("#txtPoDate").val(poDate);
        	$("#txtPoSeq").val(poSeq);
        	
        	// 載入某一周預訂單商品
    		$.ajax({
    	    	url: evg.util.fn.toActionWithTabKey('HBD1W660_ACTION'),
    			data:{		
    				actionMethod: 'jr',
    				methodName: 'LOAD_BUYER_DAILY_PO',
    				parmBuyer: buyer,
    				parmPoDate: poDate,
    				parmPoSeq: poSeq
    			},
    			global: true,
    	        type: 'post',
    	        dataType: 'json'
    	    }).done(function(result) {    		    	
    	        if(result.success) {            
    	            
    	        	var containerID = "tblWeekPOList";	        	
    	        	
    	        	if(result.data.DAILY_PO.length > 0){
    	        		
    	        		var template_obj = Handlebars.compile($('#hbt_DailyPoItemList').html());	        		
    	        		$("#" + containerID + " tbody").replaceWith(template_obj(result));
    	                evg.util.grid.init(containerID, 1);                       
    	                evg.util.form.setOriValue($('#' + containerID));
    	                evg.util.form.markChangeBind($('#' + containerID).find(':input').not('[name="cbChk"]'));
    	                
    	                
    	                // 20230531: po追加
    	                containerID = "tblAppendPOList";
    	                template_obj = Handlebars.compile($('#hbt_AppendDailyPoItemList').html());
    	                $("#" + containerID + " tbody").replaceWith(template_obj(result));
    	                evg.util.grid.init(containerID, 1);                       
    	                evg.util.form.setOriValue($('#' + containerID));
    	                evg.util.form.markChangeBind($('#' + containerID).find(':input').not('[name="cbChk"]'));
    	                
    	                
    	                $('#detailModal').modal('show');
    	                $("#txtDetailModalTitle").val(modalTitle);
    	                // 統計
    	                

    	        	}else{       			        		
    	        		evg.util.fn.showInfo("沒有訂單資料");	        		
    	        	}                        
    	            
    	        }else{
    	            evg.util.fn.showError("Failed to load list !");
    	        }	        
    	    });
    		
    		//console.log('done done');
        	
        },
        
        queryData: function(){
        	
        	
        	console.log('660 queryData');
			$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W660_ACTION'),
				data:{				
					actionMethod: 'jr',
					qryInfo: evg.util.form.toString($('#qryForm')),
					methodName: 'QUERY_DATA_LIST'					
				},				
				global: true,
                type: 'post',
                dataType: 'json'                
            }).done(function(result) {
            	
                if(result.success) {
                	
                	console.log(result.data);
                	
                	var template_obj = Handlebars.compile($('#hbt_QueryResult').html());
                    $("#" + resultTableID + " tbody").replaceWith(template_obj(result));
                    
                    // 初始化Grid, 塞 data-evg-orivalue
                    evg.util.grid.init(resultTableID, 1);                       
                    evg.util.form.setOriValue($('#' + resultTableID));
                    evg.util.form.markChangeBind($('#' + resultTableID).find(':input').not('[name="cbChk"]'));
                    
                    $("#txtPODate").val($("#qryPoDate1").val());
	                $("#txtOrdAmtSum").html(result.data.DAILY_STAT.ordAmtSum);
	                $("#txtRcvdAmtSum").html(result.data.DAILY_STAT.rcvdAmtSum);
	                $("#txtDiffAmt").html(result.data.DAILY_STAT.diffAmt);
	                
                    
                }else{
                	//evg.util.fn.showInfo("查無資料")
                	//evg.util.fn.showSuccess("查無資料")
                	//evg.util.fn.showError("查無資料")
                	evg.util.fn.showWarning("查無資料");
                }
                
            });
        },
        
        confirmDelete: function(btnDelete){        	
        	
        	var trObj = $(btnDelete).parent().parent();        	
        	var poddUuid = $(trObj).find("input[name='txtPoddUuid']").val();
        	
        	var msg = "確定要刪除此筆資料嗎?";        	
            evg.util.fn.confirm(msg, function(isConfirm) {            	
                if(isConfirm){                	
                	$.ajax({
                		url: evg.util.fn.toActionWithTabKey('HBD1W660_ACTION'),
        				data:{        					
        					actionMethod: 'jd',       					
        					methodName: 'DELETE_THIS_DATA',
        					delUUID: poddUuid  
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
        
        // 格式檢查
        chkSaveCond: function(){
        	
            // 進行檢查，有錯誤就提示 & 停止   
        	
        	if(evg.util.validator.validate($("#tblWeekPOList tbody tr:visible")).getErrorNum()!==0){
                return false;
            }            
        	
        	return true;
            
        },
        
        btnSave_Click: function(){
        	
        	// TODO: VALIDATOR
        	if(!formEvent.chkSaveCond()){
        		return false;
        	}else{        		
        		            
                var trUpdGroup  = evg.util.grid.getTrFilterArr("tblWeekPOList", {recordType:'now', update:'Y'});
                var trAddGroup  = evg.util.grid.getTrFilterArr("tblAppendPOList", {recordType:'now', update:'Y'});
                var totCnt = trAddGroup.length + trUpdGroup.length;
                
                //console.log('totCnt: ' + totCnt);
                //console.log(evg.util.grid.toString(trUpdGroup));
                //return false;
                
                if (totCnt > 0){
                	
                	$.ajax({
                		url: evg.util.fn.toActionWithTabKey('HBD1W660_ACTION'),
        				data:{				
        					actionMethod: 'ju',        					
        					methodName: 'SAVE_DATA_LIST',
        					poMastData: evg.util.form.toString($('#poMastForm')),
        					updInfo: evg.util.grid.toString(trUpdGroup),
        					addInfo: evg.util.grid.toString(trAddGroup)                             
        				},			                          	
                        global:false,
                        type: 'post',
                        dataType: 'json'
                    }).done(function(result) {
                        
                        if(result.success) {
                        	formEvent.showDetailModal($("#txtPommBuyer").val(), $("#txtPoDate").val(), $("#txtPoSeq").val(), $("#txtDetailModalTitle").val());
                        	evg.util.fn.showSuccess("完成更新11");
                        	//formEvent.queryData();                        	
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
        
        showPoAppendModal: function(){
        	$("#poAppendModal").modal('show');
        },
        
        btnSaveAppend_Click: function(){
        	
        	// 追加出貨
        	if(!formEvent.chkSaveCond()){
        		return false;
        	}else{
        		
        		var trAddGroup  = evg.util.grid.getTrAddArr("tblWeekPOList");            
                var trUpdGroup  = evg.util.grid.getTrFilterArr("tblWeekPOList", {recordType:'now', update:'Y'});
                var totCnt = trAddGroup.length + trUpdGroup.length;
                
                console.log('totCnt: ' + totCnt);
                console.log(evg.util.grid.toString(trUpdGroup));
                //return false;
                
                if (totCnt > 0){
                	
                	$.ajax({
                		url: evg.util.fn.toActionWithTabKey('HBD1W660_ACTION'),
        				data:{				
        					actionMethod: 'ju',        					
        					methodName: 'SAVE_ADD_DATA_LIST',
        					updInfo: evg.util.grid.toString(trUpdGroup),
        					addInfo: evg.util.grid.toString(trAddGroup)                             
        				},			                          	
                        global:false,
                        type: 'post',
                        dataType: 'json'
                    }).done(function(result) {
                        
                        if(result.success) {
                        	formEvent.showDetailModal($("#txtPommBuyer").val(), $("#txtPoDate").val(), $("#txtPoSeq").val(), $("#txtDetailModalTitle").val());
                        	evg.util.fn.showSuccess("完成更新");
                        	//formEvent.queryData();                        	
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
        	
        }
        
};
