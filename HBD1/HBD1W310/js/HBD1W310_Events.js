//放搜尋結果table的id 
var resultTableID = "table_queryResult";

//放常用資料JSON
var pageMiscData = {};

//var myTabKey = evg.util.fn.getTabkey();

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
        
		// 初次啟動
        init: function(){
        	//console.log('year: '  + thisYear);
        	//console.log('month: ' + thisMonth);
        	$("#txtYYYY").val(thisYear);
        	$("#txtMM").val(thisMonth);
        	
        	$("#divDayCustomer").html('請先選擇日期');
        },        
        
        // 方法綁定
        bindEvents: function(){
        	
        	// 年月選取時要reset div day
        	$("#txtYYYY, #txtMM").change(function(){
        		$("button[name='btnQuickQuery']").each(function (){
        			var selDay = $("#txtYYYY").val() + $("#txtMM").val() + $(this).attr('data-evg-day');
        			if(selDay == todayStr){
            			$(this).text('今天').removeClass('btn-default').addClass('btn-info');
            		}else{
            			$(this).removeClass('btn-info').addClass('btn-default');
            			$(this).text($(this).data('evg-day'));	
            		}        			
                });
        	});
        	
        	// 找出當天日期
        	$("button[name='btnQuickQuery']").each(function (){
        		var selDay = $("#txtYYYY").val() + $("#txtMM").val() + $(this).data('evg-day');     		
        		if(selDay == todayStr){
        			$(this).text('今天').removeClass('btn-default').addClass('btn-info');
        		}
            });
        	
        	// 綁定 div day事件
        	$("button[name='btnQuickQuery']").click(function (){
        		formEvent.dayQuery($(this).data('evg-day'));
            });        	
        	
        	
        	
        	
        	
        	
        },
        
        
        
        // 頁面載入
        pageLoad: function(){        	
        },
        
        // 新增一筆空白列
        addARow: function(){        	
        	         
        },
        
        dayQuery: function(divDay){        	
        	
        	// 按下日期後查當天有下預訂單的客戶清單        	
        	var selLDay = $("#txtYYYY").val() + $("#txtMM").val() + divDay;
        	var selSDay = $("#txtMM").val() + "/" + divDay;        	
        	
        	
        	// reset color
            $("button[name='btnQuickQuery'].btn-warning").each(function() {
            	$(this).removeClass('btn-warning').addClass('btn-default');
        	});
            
            // highlight 選取的日期
            $("button[data-evg-day='" + divDay + "']").removeClass('btn-default').addClass('btn-warning');
            
            // 把之前列出來的po單關掉
            var containerID = "table_queryResult";    	        	
        	$("#" + containerID + " tbody").replaceWith("<tbody><tr><td colspan='4'>請選擇訂單客戶</td></tr></tbody>");
            
            
            // 載入某天的客戶名單
    		$.ajax({
    	    	url: evg.util.fn.toActionWithTabKey('HBD1W310_ACTION'),
    			data:{
    				//tabKey: myTabKey,
    				actionMethod: 'jr',
    				methodName: 'LOAD_CUSTOMER',
    				selectDay:  selLDay
    			},
    	        type: 'post',
    	        dataType: 'json',
    	        global:false
    	    }).done(function(result) {              
    	        
    	        if(result.success) {
    	        	
    	        	$("#divDayTitle").html(selSDay + ' (' + result.data.WEEKDAY + ') 訂單客戶');
    	        	
    	        	var containerID = "divDayCustomer";
    	        	//console.log('cust_list length: ' + result.data.CUST_LIST.length);
    	        	if(result.data.CUST_LIST.length >0){
    	        		
        	        	var template_obj = Handlebars.compile($('#hbt_QueryResult').html());
    	        		$("#" + containerID).html(template_obj(result));
    	                
    	                // 初始化Grid, 塞 data-evg-orivalue
    	                evg.util.grid.init(containerID, 1);                       
    	                evg.util.form.setOriValue($('#' + containerID));
    	                evg.util.form.markChangeBind($('#' + containerID).find(':input').not('[name="cbChk"]'));
    	                
    	                $("#divQueryResult").hide();
    	                $("#divBidItemList").show();
    	                $("#divCartItemList").hide();
    	                
    	                // 只有當天的單可以作出貨存檔    	                
    	            	if (selLDay == todayStr){
    	    				$("#btnSaveIssue, #btnResetAll, #btnIssueAll").show();
    	    			}else{
    	    				$("#btnSaveIssue, #btnResetAll, #btnIssueAll").hide();
    	    			}
    	                
    	                // 查詢客戶的訂單
    	                $("button[name='btnQueryPO']").click(function (){
    	            		formEvent.poQuery($(this).data('po-buyer'), $(this).data('po-date'), $(this).data('po-seq'));
    	                });
    	        	}else{
    	        		$("#btnSaveIssue, #btnResetAll, #btnIssueAll").hide();
    	        		$("#" + containerID).html(selSDay + ' 沒有訂單!');
    	        	}    	        	    	        	
    	        	
    	        	                        
    	            
    	        }else{
    	            evg.util.fn.showError("Failed to pageLoad !");
    	        }	        
    	    });
            
        },
        
        
        // 查詢資料
        poQuery: function(buyer, poDate, poSeq){			
			
			// highlight
			$("button[name='btnQueryPO']").each(function (){    			
    			if($(this).data('po-date') == poDate && $(this).data('po-seq') == poSeq ){
        			$(this).removeClass('btn-default').addClass('btn-warning');
        		}else{
        			$(this).removeClass('btn-warning').addClass('btn-default');
        		}        			
            });
			
			// 查詢PO資料
			// 載入某天的客戶名單
    		$.ajax({
    	    	url: evg.util.fn.toActionWithTabKey('HBD1W310_ACTION'),
    			data:{
    				//tabKey: myTabKey,
    				actionMethod: 'jr',
    				methodName: 'LOAD_CUST_POS_LIST',
    				selBuyer: buyer,    				
    				selDate: poDate,
    				selSeq: poSeq
    				
    			},
    	        type: 'post',
    	        dataType: 'json',
    	        global:false
    	    }).done(function(result) {              
    	        
    	        if(result.success) {

    	        	//console.log('po item list: ' + result.data.PO_ITEM_LIST.length);
    	        	var containerID = "table_queryResult";   	        	    	        	
    	        	if(result.data.PO_ITEM_LIST.length >0){
    	        		
        	        	var template_obj = Handlebars.compile($('#hbt_POItemList').html());
        	        	//console.log(template_obj(result));
        	        	$("#" + containerID + " tbody").replaceWith(template_obj(result));        	        	
    	                
    	                // 初始化Grid, 塞 data-evg-orivalue
    	                evg.util.grid.init(containerID, 1);                       
    	                evg.util.form.setOriValue($('#' + containerID));
    	                evg.util.form.markChangeBind($('#' + containerID).find(':input').not('[name="cbChk"]'));
    	                
    	    			// 只有當天的po單可以更改出貨數量
    	    			$("button[name='btnPlusItem'], button[name='btnMinusItem']").each(function (){
    	    				console.log('po: ' + poDate + ", to:" +  todayStr);
    	    				console.log('po to: ' + (poDate == todayStr));				
    	    				if (poDate == todayStr){
    	    					console.log('show plus button: ' + $(this).attr('name'));
    	    					$(this).show();
    	    				}else{
    	    					console.log('hide plus button: ' + $(this).attr('name'));
    	    					$(this).hide();
    	    				}        			
    	                });
    	                
    	        	}else{
    	        		$("#" + containerID).html('<tbody>沒有訂單!</tbody>');
    	        	}    	        	    	        	
    	        	
    	        	                        
    	            
    	        }else{
    	            evg.util.fn.showError("Failed to pageLoad !");
    	        }	        
    	    });
			
			
			
        },
        
        // 格式檢查
        chkSaveCond: function(){
            
        },
        
        // 出貨存檔
        submitSave: function(){        	
        	
        	console.log('submit save');      	
        	var info = evg.util.form.toString($("#table_queryResult"));
        	if (info != "{}"){
        		$.ajax({
        			url: evg.util.fn.toActionWithTabKey('HBD1W310_ACTION'),
        			data:{
        				//tabKey: myTabKey,
        				actionMethod: 'ju',
            			methodName: 'SAVE_RCV_INFO',        			
        				revInfo: evg.util.form.toString($("#table_queryResult"))
        			},
        			global: false,
        			type: 'post',
        			dataType: 'json'
        		}).done(function(result) {
        			if(result.success) {
        				evg.util.fn.showSuccess("存檔成功");    				
        			}else{    				
        				evg.util.fn.showError("存檔失敗");    				
        			}                
        		});	
        	}else{
        		evg.util.fn.showWarning("沒有資料可存檔");
        	}        	
        },
        
        IssueAll: function(){        	
        	$("#table_queryResult tbody tr").each(function() {
            	var txtPoddOrdQty = $(this).find("input[name=txtPoddOrdQty]");
            	var txtPoddRcvdQty = $(this).find("input[name=txtPoddRcvdQty]");
            	$(txtPoddRcvdQty).val($(txtPoddOrdQty).val());
        	});        	
        },
        
        ResetAll: function(){      	        	
        	$("#table_queryResult tbody tr").each(function() {            	
            	var txtPoddRcvdQty = $(this).find("input[name=txtPoddRcvdQty]");            	
            	$(txtPoddRcvdQty).val('0');
        	});        	
        	
        	
        	
        },
        
        SaveIssue: function(){
        	formEvent.submitSave();
        },
        
        addItem: function(btnMinus){            
    		var trObj = $(btnMinus).parent().parent();		
    		var txtQty = $(trObj).find("input[name=txtPoddRcvdQty]");
    		var qty = parseInt($(txtQty).val());
    		if (qty < 99){
    			$(txtQty).val(qty+1);	
    		}		
        },
    	minusItem: function(btnMinus){            
    		var trObj = $(btnMinus).parent().parent();
    		var txtQty = $(trObj).find("input[name=txtPoddRcvdQty]");
    		var qty = parseInt($(txtQty).val());
    		if (qty > 0){
    			$(txtQty).val(qty-1);	
    		}		
        },
        
        // db資料的單筆刪除
        deleteThisRow: function(trIndex){        	
        	    	
        },
        
        // db資料的把次刪除
        batchDeleteSample: function(){            
        	       	
        },

        // 開啟  MODAL
        openMoreUseDescModal: function(moreLink){
        	     	
        },
        
        // 帶回修改說明
        modifyUseDesc: function(){
        	
        }
        
};