//放搜尋結果table的id 
$(document).ready(function() {    
    // 載入資料
    pageLoad();
    // 事件繫結    
    bindEvent();    
});

// 進入頁面預載資料
function pageLoad(){    
	
	$.ajax({
    	url: evg.util.fn.toAction('HBD1LOGIN_DO_ACTION'),
		data:{			
			actionMethod: 'ju'			
		},
		global:true,
        type: 'post',
        dataType: 'json'
    }).done(function(result) {            
    	
    	
    	console.log(result);
        
        if(result.success) {                     
        	
        	$("#txtLoginID").val(result.data.LoginID);
    		$("#txtPassword").val(result.data.Password);
    		$("#btnSubmitLogin").focus();
            return;
        }
        
    });
}

function bindEvent(){	
}

function doLogin(){

	//console.log('do login: ' + evg.util.fn.toAction('HBD1LOGIN_DO_ACTION'));
	
	
	//$('#loginForm').attr("action", evg.util.fn.toAction('HBD1LOGIN_DO_ACTION'));        
    //$('#loginForm').submit();
	
    //return;
	
	if($("#txtLoginID").val() == '' || $("#txtPassword").val() == ''){
		alert("帳號/密碼有誤");
		$("#txtLoginID").focus();
	}else{
		$.ajax({
	    	url: evg.util.fn.toAction('HBD1LOGIN_DO_ACTION'),
			data:{			
				actionMethod: 'jr',
				loginInfo: evg.util.form.toString($("#loginForm"))
			},
			global:true,
	        type: 'post',
	        dataType: 'json'
	    }).done(function(result) {            
	    	
	    	console.log(' @@@ result: ');
	    	console.log(result);
	        
	        if(result.success) {                     
	        	
	        	if (result.data.LoginMark == 'Y'){
	        		        		
	        		if (result.data.LoginGroup == 'C'){        			        			
	        			var okHref = evg.util.fn.toAction('HBD1W010') + '&tabKey=' + result.data.tabKey;
	        			console.log('C: ' + okHref);
	        			//return;
	                	window.location.href = okHref;	
	                }                
	                if (result.data.LoginGroup == 'E'){
	                	console.log('E Group');                	
	                	var okHref = evg.util.fn.toAction('HBD1W320') + '&tabKey=' + result.data.tabKey;
	                	console.log('E: ' + okHref);
	                	//	return;
	                	window.location.href = okHref;
	                	//window.location.href = evg.util.fn.toAction('HBD1W310');
	                }                
	                if (result.data.LoginGroup == 'A'){
	                	console.log('A Group');
	                	var okHref = evg.util.fn.toAction('HBD1W610') + '&tabKey=' + result.data.tabKey;
	                	console.log('A: ' + okHref);                	
	                	window.location.href = okHref;
	                	//window.location.href = evg.util.fn.toAction('HBD1W610');	
	                }
	        	}else{
	        		console.log("帳號/密碼有誤");
	        		$("#txtLoginID").val('').focus();
	        		$("#txtPassword").val('');
	        		//evg.util.fn.showWarning("帳號/密碼有誤");
	        		//evg.util.fn.alert("帳號/密碼有誤");
	        		alert("帳號/密碼有誤");
	        	}
	        	
	        	return;                        
	            
	        }else{
	        	evg.util.fn.alert("帳號/密碼有誤");
	        }
	        
	    });	
	}
    
	
	
	
}

