//放搜尋結果table的id 
var resultTableID = "table_queryResult";

//放常用資料JSON
var hmp1MiscData = {};

var currRowIdx = '';

$(document).ready(function() {    

    formEvent.init();
    formEvent.bindEvents();    
    formEvent.pageLoad();
    templateHelper.register();
    
});


var qryEvent = {
		
		submitQueryMonth: function(){
			
			//alert('query month: ' + $("#qryMonth").val());
			$.ajax({
            	url: evg.util.fn.toActionWithTabKey('HBD1W680_ACTION'),
				data:{
					actionMethod: 'jr',
					methodName: 'LOAD_WEEK',
					qryInfo: evg.util.form.toString($('#queryCondTab'))
				},
				global: false,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {                
                
                if(result.success) {
                    var template_obj = Handlebars.compile($('#hbt_WEEK_LIST').html());
                    $("#divQuickSearch").html(template_obj(result));
                }else{
                    evg.util.fn.showError("Failed to query !");
                }                
            });
			
		},
		
	    showDetailModal: function(buyer, bidmUUID){
	    	
	    	//console.log('showDetailModal: ' + bidUUID);   	    	
	    	
	    	// 載入某一周預訂單商品
			$.ajax({
		    	url: evg.util.fn.toActionWithTabKey('HBD1W680_ACTION'),
				data:{			
					//tabKey: myTabKey,
					actionMethod: 'jr',
					methodName: 'LOAD_PODD_BY_WEEK_BUYER',
					qryBuyer: buyer,
					qryBidUUID: bidmUUID				
				},
		        type: 'post',
		        dataType: 'json'
		    }).done(function(result) {    		    	
		        if(result.success) {            
		            
		        	var containerID = "tblWeekPOList";	        	
		        	
		        	if(result.data.WEEK_PO.length > 0){
		        		
		        		var template_obj = Handlebars.compile($('#hbt_WeekPoItemList').html());	        		
		        		$("#" + containerID + " tbody").replaceWith(template_obj(result));
		        		
		        		//template_obj = Handlebars.compile($('#hbt_WeekPoTotal').html());
		        		//$("#" + containerID + " tfoot").replaceWith(template_obj(result));
		        		
		        		//console.log('111111111111:' + template_obj(result));
		                
		                // 初始化Grid, 塞 data-evg-orivalue
		                evg.util.grid.init(containerID, 1);                       
		                evg.util.form.setOriValue($('#' + containerID));
		                evg.util.form.markChangeBind($('#' + containerID).find(':input').not('[name="cbChk"]'));
		                $('#detailModal').modal('show');
		                
		                // 本周出貨金額
		                let sum = 0;
		                $("span[name='txtDayAmount']").each(function() {		                	
		                    sum += Number($(this).text());
		                });
		                $("#txtWeekTotalAmount").text(sum);		                

		        	}else{       			        		
		        		evg.util.fn.showInfo("沒有本周的預訂單資料");	        		
		        	}                        
		            
		        }else{
		            evg.util.fn.showError("Failed to load list !");
		        }	        
		    });
			
	    	
	    },
		
	    // 查詢指定周的會員應付、已付金額
		queryOffsLists: function(btn){
			//alert('bidUUID: ' + bidUUID);
			
			$("button[name='btnQuickQuery']").removeClass('btn-warning').addClass('btn-info');
        	$(btn).removeClass('btn-info').addClass('btn-warning');
        	
        	let bidUUID = $(btn).attr("data-uuid");
			
			$.ajax({
            	url: evg.util.fn.toActionWithTabKey('HBD1W680_ACTION'),
				data:{
					actionMethod: 'jr',
					methodName: 'LOAD_OFFS',
					qryUUID: bidUUID
				},
				global: true,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {                
                
                if(result.success) {                    
                    var template_obj = Handlebars.compile($('#hbt_QueryResult').html());
                    $("#table_queryResult tbody").replaceWith(template_obj(result));
                    
                    template_obj = Handlebars.compile($('#hbt_WeekPoTotal').html());
	        		$("#table_queryResult tfoot").replaceWith(template_obj(result));
	        		
	        		
                    //console.log("length: " + result.data.OFFS_LIST.length);
                    //console.log("size: " + result.data.OFFS_LIST.size);
                    if (result.data.OFFS_LIST.length == 0){
                    	evg.util.fn.showInfo("目前沒有資料 !");
                    }
                }else{
                    evg.util.fn.showError("無法查詢資料 ! ");
                }
                
            });
		}
		
};

// 主程式
var formEvent = {
        
        init: function(){            
            
            
            
        },
        
        bindEvents: function(){        
        	
            $("#queryBtn").on("click", formEvent.queryData);
            $("#btnAddRow").on("click", formEvent.addARow);            
            $("#btnSave").on("click", formEvent.saveGridData);
            
            $("#txtDate").datepicker({
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
        	
        	var d = new Date();

        	var month = d.getMonth()+1;
        	var day = d.getDate();

        	var qryMonth = d.getFullYear() + (month<10 ? '0' : '') + month; 
        	
        	$("#qryMonth").val(qryMonth);
        	
        },
        
        
        
        
        
        
        quickQuery: function(parmMonth){
        	// reset value & set query type
        	
        	//var qryYear = $("#qryYear").val();
        	//var qryMonth = qryYera + parmMonth;
        	
        	$("#qryMonth").val(parmMonth);
        	
        	//console.log('quickYear: ' + qryYear);
        	//console.log('quickMonth: ' + parmMonth);
        	
            formEvent.queryData();
        },
        
        queryData: function(){
        	
			$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W680_ACTION'),
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
                    $("#" + resultTableID + " tbody tr:visible").find("[name='txtBidSaleSdate'],[name='txtBidSaleEdate'],[name='txtBidOrdSdate'],[name='txtBidOrdEdate']").datepicker({
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
        
        showMoneyModal: function(buyer, bidmUUID, btn){
        	
        	let trObj = $(btn).parent().parent();
        	
        	console.log('trIdx: ' + $(trObj).attr("data-rowidx"));
        	
        	currRowIdx = $(trObj).attr("data-rowidx");
        	
        	var txtOffsPayAmt = $(trObj).find("input[name='']")
        	
        	
        	// 先載入匯款資訊        	
        	$('#moneyModal').modal('show');
        	
        	// ajax load_One_Offs
        	
        	//console.log('bidmUUID: ' + $("#txtBidmUUID").val());
        	
        	$("#txtAmount").val('');
        	$("#txtDate").val('');
        	$("#txtAccount").val('');
        	$("#txtBidmUUID").val('');
        	
        	$.ajax({
    	    	url: evg.util.fn.toActionWithTabKey('HBD1W680_ACTION'),
    			data:{			
    				//tabKey: myTabKey,
    				actionMethod: 'jr',
    				methodName: 'LOAD_ONE_OFFS',
    				qryBidUUID: bidmUUID,
    				qryBuyer: buyer
    				
    			},
    	        type: 'post',
    	        global: false,
    	        dataType: 'json'
    	    }).done(function(result) {    		    	
    	        if(result.success) {
    	        	$("#txtAmount").val(result.data.offsPayAmt);
    	        	$("#txtDate").val(result.data.offsPayDate);     
    	        	
    	        	$("#txtAccount").val(result.data.offsBuyer);
    	        	$("#txtBidmUUID").val(result.data.offsBidmUuid);
    	        		            
    	        }else{
    	            evg.util.fn.showError("找不到匯款資料，系統忙錄中!");
    	        }	        
    	    });
        	
        	
        },
        
        submitSaveMoney: function(buyer, bidUUID){
        	
        	// 儲存匯款資料        	
        	$.ajax({
    	    	url: evg.util.fn.toActionWithTabKey('HBD1W680_ACTION'),
    			data:{			
    				//tabKey: myTabKey,
    				actionMethod: 'ju',
    				methodName: 'SAVE_BIDM_MONEY',    				
    				info: evg.util.form.toString($("#moneyForm"))			
    			},
    	        type: 'post',
    	        dataType: 'json'
    	    }).done(function(result) {    		    	
    	        if(result.success) {           
    	        	$("#txtAmount").val('');
    	        	$("#txtDate").val('');	        	
    	        	evg.util.fn.showSuccess("匯款資訊已儲存");     
    	        	$('#moneyModal').modal('hide');
    	        }else{
    	            evg.util.fn.showError("Failed to save money !");
    	        }	        
    	    });

        	
        }
        
                
       
        
};
