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
            $("#qryYear").val(thisYear);
        },                     
        
        pageLoad: function(){       	
        	$("#qryYear").focus();
        },        
        
        showSearchModal: function(){
        	$('#searchModal').modal('show');
        },
                
        showDetailModal: function(buyer, poDate){
        	
        	//console.log('showDetailModal: ' + bidUUID);   	    	
        	
        	// 載入某一周預訂單商品
    		$.ajax({
    	    	url: evg.util.fn.toActionWithTabKey('HBD1W662_ACTION'),
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
        	
        	if($("#qryYear").val().length != 4){
        		evg.util.fn.showAlert("請輸入4碼西元年(yyyy)");
        		return false;
        	}        	
        	
        	$("#btnQuery").text('報表統計中...年度數據資料龐大, 請稍候約1~2分鐘, 請勿離開此畫面').attr("disabled", true).addClass('btn-warning').removeClass('btn-primary');
        	
        	$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W662_ACTION'),
				data:{				
					actionMethod: 'jr',
					methodName: 'QUERY_DATA_LIST',
					qryInfo: evg.util.form.toString($('#qryForm'))										
				},				
				global: false,
                type: 'post',
                dataType: 'json'                
            }).done(function(result) {	
            	
                if(result.success) {                	
                	
                	var template_obj = Handlebars.compile($('#barTempCustYearReport').html());
                    $("#" + resultTableID + " tbody").replaceWith(template_obj(result));
                    //template_obj = Handlebars.compile($('#hbt_QueryHeader').html());
                    //$("#" + resultTableID + " thead").replaceWith(template_obj(result));
                    //template_obj = Handlebars.compile($('#hbt_QueryFooter').html());
                    //$("#" + resultTableID + " tfoot").replaceWith(template_obj(result));
                    
                    // 初始化Grid, 塞 data-evg-orivalue
                    evg.util.grid.init(resultTableID, 1);                       
                    evg.util.form.setOriValue($('#' + resultTableID));
                    evg.util.form.markChangeBind($('#' + resultTableID).find(':input').not('[name="cbChk"]'));
             
                    // 初始化每个月的总金额数组
                    var monthlyTotalAmts = new Array(12).fill(0);

                    // 遍历每一行（跳过表头）
                    $("#table_queryResult tbody tr").each(function () {
                        var row = $(this);

                        // 获取当前行的第一个单元格（人名）
                        var nameCell = row.find("td:first");

                        // 初始化年度总金额
                        var annualTotalAmt = 0;

                        // 遍历当前行的每个月的金额输入框
                        row.find("input[name^='txtM']").each(function (index) {
                            // 获取输入框的值并将其转换为数字
                            var amt = parseFloat($(this).val());

                            // 如果输入有效，则将其加到年度总金额中
                            if (!isNaN(amt)) {
                                annualTotalAmt += amt;

                                // 更新每月总金额
                                monthlyTotalAmts[index] += amt;
                            }
                        });

                        // 创建一个新的单元格来显示年度总金额
                        var annualTotalCell = $("<td>").text("Annual Total: " + annualTotalAmt);

                        // 将新的单元格添加到当前行的末尾
                        row.append(annualTotalCell);
                    });

                    // 创建表尾行
                    var footerRow = $("<tr>");

                    // 创建一个表尾单元格来显示每月总金额
                    $.each(monthlyTotalAmts, function (index, total) {
                        var monthlyTotalCell = $("<td>").text("Monthly Total: " + total);
                        footerRow.append(monthlyTotalCell);
                    });

                    // 将表尾行添加到表格的tfoot中
                    $("#table_queryResult tfoot").append(footerRow);
                
        	
                    $("#txtYear").val($("#qryYear").val());
                    $("#btnQuery").text('查詢').attr("disabled", false).removeClass('btn-warning').addClass('btn-primary');
	                
                    
                }else{
                	//evg.util.fn.showInfo("查無資料")
                	//evg.util.fn.showSuccess("查無資料")
                	//evg.util.fn.showError("查無資料")
                	$("#btnQuery").text('查詢').attr("disabled", false).removeClass('btn-warning');
                	evg.util.fn.showWarning("查無資料");
                }                
                
            });
        	        	
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
