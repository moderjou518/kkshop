var pageEntity;
//放搜尋結果table的id 
var resultTableID = "table_queryResult";

// 放常用資料JSON
var hmp1MiscData = {};

$(document).ready(function() {
    
	bindEvent();
	pageLoad();
   
});


function bindEvent(){

    // 查詢
    $("#loginBtn").on("click", submitLogin);
    
    $("#txtUserName").on("change", function(){
    	//$("#txtUserPasswd").focus();
    });
    //$("input[name='txtSignType']").on("click", chkSign);
    
    // 單筆新增
    $("#showCreateDivBtn").on("click",function(){
        evg.util.grid.addOne(resultTableID);    
    	//console.log(evg.util.form.toString($('#loginTab')));
        //$('#createDataModal').modal('show');        
    });
    
}

// 進入頁面預載資料
function pageLoad(){
    
    $.ajax({
        url: evg.util.fn.toUrl('HMS1W001_ACTION', 'actionMethod=jr&methodName=loadUserList'),        
        //url: '<%=UTIL_GlobalConfig.getWebActionUrl("HMS_SIGNIN_LIST", "jr")%>',
        type: 'post',
        dataType: 'json',
        data:{                          
            info: evg.util.form.toString($('#loginTab'))            
        },
    }).done(function(result) {
        
        if(result.success) {        
            
        	console.log('ajax call ok');
        	//return;
            // 單位
            var template_obj = Handlebars.compile($('#hbt_UserOptions').html());
            var rendered = template_obj(result);
            $("#txtUserName").html(rendered);      
        }else{
        	console.log('ajax call fail');
            evg.util.fn.showError("Failed to load the option records !");
        }
        
    });
    
    
}

function chkSign(chkBtn){	
	
	$("input[name='txtSignType']").each(function (){        
        $(this).prop("checked", false);
    });
	$(chkBtn).prop("checked", true);
	console.log('chk value: ' + $(chkBtn).val());
	
}


function submitLogin(){
	
	if ($("#txtUserName").val() == ''){		
		alert('請選擇帳號');
		$("#txtUserName").focus();
		return false;
	}
	
	if ($("#txtUserPasswd").val() == ''){
		alert('請輸入密碼');
		$("#txtUserPasswd").focus();
		return false;
	}
	
	var i = 0;
	$("input[name='txtSignType']").each(function (){
		if($(this).prop("checked") == true){
			i = i + 1;	
		}        
    });
	
	if (i==0){
		alert('請選擇【上班/下班】');
		return false;
	}
	
    $.ajax({
        url: evg.util.fn.toUrl('HMS1W001_ACTION', 'actionMethod=jr&methodName=loginCheck'),        
        type: 'post',
        dataType: 'json',
        data:{                          
            info: evg.util.form.toString($('#loginTab'))            
        },
    }).done(function(result) {
        
        if(result.success) {           
        	//evg.util.fn.showInfo("簽到成功");
        	// reset input
        	$("#txtUserName").val('');
        	$("#txtUserPasswd").val('');
        	$("input[name='txtSignType']").each(function (){        
                $(this).prop("checked", false);
            });
        	showInfo("簽到成功");        	
        }else{
        	//evg.util.fn.showError("帳密不正確，簽到失敗");
        	showError("簽到失敗: " + result.msg);

        }
        
    });
	
    
}

function submitData(){    
	console.log('test1');
    $('#voidNoticeModal').modal('show');
    console.log('test2');
    console.log(evg.util.form.toString($('#loginTab')));
    
}


function registerHelper(){
    
    Handlebars.registerHelper("eq", function(v1, v2, options){
        if(v1==v2){
            return options.fn(this);
        }else{
          return options.inverse(this);
        }
    }); 
    
    Handlebars.registerHelper("isSel",function(v1, v2, showTxt){
        var reStr = "";
        if(v1==v2){
            reStr = "<option value='" + v1 + "' selected>" + showTxt + "</option>";
        }else{
            reStr = "<option value='" + v1 + "'>" + showTxt + "</option>";
        }       
        return new Handlebars.SafeString(reStr);
    });
}

