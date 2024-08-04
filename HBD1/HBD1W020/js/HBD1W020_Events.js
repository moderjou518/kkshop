//放搜尋結果table的id 
var mastTblID = "tableQueryResult";
var dtlTblID  = "bidItemResult";
//var myTabKey = evg.util.fn.getTabkey();

$(document).ready(function() {    
    // 載入資料
    pageLoad();
    // 事件繫結    
    bindEvent();    
    templateHelper.register();
});

// 進入頁面預載資料
function pageLoad(){
	
	$.ajax({
    	url: evg.util.fn.toActionWithTabKey('HBD1W020_ACTION'),
		data:{			
			actionMethod: 'jr',
			methodName: 'LOAD_BID_LIST'					
		},
		global: false,
        type: 'post',
        dataType: 'json'
    }).done(function(result) {              
        
        if(result.success) {
            template_obj = Handlebars.compile($('#hbt_bidList').html());
            $("#" + mastTblID + " tbody").replaceWith(template_obj(result));            
        }else{
            evg.util.fn.showError("Failed to pageLoad !");
        }
        
    });
	
	return;
    
}

function bindEvent(){  
	
	$("#txtDate").datepicker({
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,                    
        dateFormat: 'yymmdd'
    });
	
	$('#moneyModal').on('shown.bs.modal', function() {
		$("#txtAmount").focus();
	});
	
	
}

function goBookmark(linkID){
	var target_top = $("#" + linkID).offset().top;
	$("html,body").animate({scrollTop: target_top}, 1000);  //帶滑動效果的跳轉
	$("html,body").scrollTop(target_top);
}

var formEvent = {
		
    showDetailModal: function(bidUUID){
    	
    	console.log('showDetailModal: ' + bidUUID);   	    	
    	
    	// 載入某一周預訂單商品
		$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W020_ACTION'),
			data:{			
				//tabKey: myTabKey,
				actionMethod: 'jr',
				methodName: 'LOAD_PODD_BY_WEEK_BUYER',
				qryBidUUID: bidUUID				
			},
	        type: 'post',
	        dataType: 'json'
	    }).done(function(result) {    		    	
	        if(result.success) {            
	            
	        	var containerID = "tblWeekPOList";	        	
	        	
	        	if(result.data.WEEK_PO.length > 0){
	        		
	        		var template_obj = Handlebars.compile($('#hbt_WeekPoItemList').html());	        		
	        		$("#" + containerID + " tbody").replaceWith(template_obj(result));
	        		
	        		template_obj = Handlebars.compile($('#hbt_WeekPoTotal').html());
	        		$("#" + containerID + " tfoot").replaceWith(template_obj(result));
	        		
	        		 // 報表時間	            
		            template_obj = Handlebars.compile($('#hbt_reportTime').html());
		            msg = template_obj(result);		            
		            $("#tableMarkTime tbody").replaceWith(msg);
	                
	                // 初始化Grid, 塞 data-evg-orivalue
	                evg.util.grid.init(containerID, 1);                       
	                evg.util.form.setOriValue($('#' + containerID));
	                evg.util.form.markChangeBind($('#' + containerID).find(':input').not('[name="cbChk"]'));
	                $('#detailModal').modal('show');

	        	}else{       			        		
	        		evg.util.fn.showInfo("沒有本周的預訂單資料");	        		
	        	}                        
	            
	        }else{
	            evg.util.fn.showError("Failed to load list !");
	        }	        
	    });
		
    	
    },
		
    
    showMoneyModal: function(bidmUUID){
    	
    	$("#txtBidmUUID").val(bidmUUID);
    	// 先載入匯款資訊
    	
    	$('#moneyModal').modal('show');    	
    	
    	
    	
    	$("#txtAmount").val('');
    	$("#txtDate").val('');	
    	
    	$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W020_ACTION'),
			data:{			
				actionMethod: 'jr',
				methodName: 'LOAD_ONE_OFFS',
				qryBidUUID: bidmUUID				
			},
	        type: 'post',
	        dataType: 'json'
	    }).done(function(result) {    		    	
	        if(result.success) {
	        	if (result.data.offsPayAmt == 0){
	        		$("#txtAmount").val('');	
	        	}else{
	        		$("#txtAmount").val(result.data.offsPayAmt);
	        	}
	        	$("#txtAmount").focus();
	        	$("#txtDate").val(result.data.offsPayDate);        	
	        		            
	        }else{
	            evg.util.fn.showError("找不到匯款資料，系統忙錄中!");
	        }	        
	    });
    	
    	
    },
    
    submitSaveMoney: function(){
    	
    	// 儲存匯款資料
    	
    	//console.log('monForm: ' + evg.util.form.toString($("#moneyForm")));
    	
    	$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W020_ACTION'),
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

    	
    },
    
    createImage: function(){
    	
    	$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W020_ACTION'),
			data:{			
				//tabKey: myTabKey,
				actionMethod: 'jc',
				methodName: 'CREATE_IMAGE'								
			},
	        type: 'post',
	        dataType: 'json'
	    }).done(function(result) {    		    	
	        if(result.success) {
	        	let reportImg = "HBD1W020.jpg";    	        
	            document.getElementById('capture').focus();        
	            html2canvas(document.querySelector("#capture")).then(function (canvas) {              
	                a = document.createElement('a');//add canvas
	                //toDataURL將canvas轉成base64            
	                a.href = canvas.toDataURL("image/jpeg", 1).replace("image/jpeg", "image/octet-stream");
	                a.download = reportImg;
	                a.click();
	            });	        	
	        }else{
	            evg.util.fn.showError("目前無法下載報表 !");
	        }	        
	    });
        
    	
    }
    
    
};