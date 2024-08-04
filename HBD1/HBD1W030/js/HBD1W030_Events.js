//放搜尋結果table的id 
var mastTblID = "tableQueryResult";
var dtlTblID  = "bidItemResult";
//var myTabKey = evg.util.fn.getTabkey();

$(document).ready(function() {    
    // 載入資料
    pageLoad();
    // 事件繫結    
    bindEvent();    
});

// 進入頁面預載資料
function pageLoad(){
	
	$.ajax({
    	url: evg.util.fn.toActionWithTabKey('HBD1W030_ACTION'),
		data:{			
			//tabKey: myTabKey,
			actionMethod: 'jr',
			methodName: 'LOAD_PROFILE'					
		},
		//global: false,
        type: 'post',
        dataType: 'json'
    }).done(function(result) {              
        
        if(result.success) {                       
        	
        	$("#txtHascBascLoginId").val(result.data.hac0BascLoginId);
        	$("#txtHac0BascAcCode").val(result.data.hac0BascAcCode);
        	$("#txtHac0BascName").val(result.data.hac0BascName);
        	$("#txtHac0BascPwd").val(result.data.hac0BascPwd);
        	$("#txtHac0BascNote").val(result.data.hac0BascNote);           
                        
            
        }else{
            evg.util.fn.showError("Failed to pageLoad !");
        }
        
    });
	
	return;
    
}

function bindEvent(){
  
}

function goBookmark(linkID){
	var target_top = $("#" + linkID).offset().top;
	$("html,body").animate({scrollTop: target_top}, 1000);  //帶滑動效果的跳轉
	$("html,body").scrollTop(target_top);
}

var formEvent = {	
    
    
    submitSaveOne: function(){
    	
    	// 儲存匯款資料
    	
    	console.log('monForm: ' + evg.util.form.toString($("#moneyForm")));
    	
    	$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W030_ACTION'),
			data:{			
				//tabKey: myTabKey,
				actionMethod: 'ju',
				methodName: 'SAVE_PROFILE',
				info: evg.util.form.toString($('#divQueryResult'))				
			},
	        type: 'post',
	        dataType: 'json'
	    }).done(function(result) {    		    	
	        if(result.success) {          	
	        	evg.util.fn.showSuccess("存檔成功");
	        	$("#txtHascBascLoginId").val(result.data.hac0BascLoginId);
	        	$("#txtHac0BascAcCode").val(result.data.hac0BascAcCode);
	        	$("#txtHac0BascName").val(result.data.hac0BascName);
	        	$("#txtHac0BascPwd").val(result.data.hac0BascPwd);
	        	$("#txtHac0BascNote").val(result.data.hac0BascNote);	        	
	        	$("#txtNewPassword").val('');
	        }else{
	            evg.util.fn.showError("無法儲存資料，系統忙錄中!");
	        }	        
	    });

    	
    },   
 
    
    showLogin: function(){    	
    	$('#loginModal').modal('show');
    },
    
    
    gotoTop: function(){
    	goBookmark("topLink");
    }
    
};