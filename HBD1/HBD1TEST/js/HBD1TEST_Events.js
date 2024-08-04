//放搜尋結果table的id 
$(document).ready(function() {    
    
});



function doLogin(){	
	
	console.log('do Login()');
    
	$.ajax({
    	url: evg.util.fn.toAction('HBD1TEST_ACTION'),
		data:{			
			actionMethod: 'ju'			
		},
		global:true,
        type: 'post',
        dataType: 'json'
    }).done(function(result) {            
    	
    	console.log(' @@@ result: ');
    	console.log(result);
        
        if(result.success) {                     
        	
        	
        	console.log('result success');
            
        }else{
            evg.util.fn.showWarning("帳號/密碼有誤");
        }
        
    });
	
	console.log('end !!! login');
}

