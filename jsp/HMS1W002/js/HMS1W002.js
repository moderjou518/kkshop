var pageEntity;
//放搜尋結果table的id 
var resultTableID = "table_queryResult";

//today
var fullDate = new Date();
var yyyy = fullDate.getFullYear();
var   MM = (fullDate.getMonth() + 1) >= 10 ? (fullDate.getMonth() + 1) : ("0" + (fullDate.getMonth() + 1));
var   dd = fullDate.getDate() < 10 ? ("0" + fullDate.getDate()) : fullDate.getDate();
var   td = (fullDate.getDate() + 1) < 10 ? ("0" + (fullDate.getDate()+1)) : fullDate.getDate()+1;
var todayStr = yyyy + MM + dd;


// 放常用資料JSON
var hmp1MiscData = {};

$(document).ready(function() {
    
	bindEvent();
	pageLoad();
   
});


function bindEvent(){

    // 查詢
    $("#btnQuery").on("click", submitQuery);
    
    $("#qryUserName").on("change", function(){
    	//$("#txtUserPasswd").focus();
    });
    //$("input[name='txtSignType']").on("click", chkSign);    
    
    // init date input
    $("#qrySignDate").datepicker({
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        dateFormat: 'yymmdd'
    });
    
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
        url: evg.util.fn.toUrl('HMS1W002_ACTION', 'actionMethod=jr&methodName=loadUserList'),        
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
            $("#qryUserName").html(rendered);      
        }else{
        	console.log('ajax call fail');
            evg.util.fn.showError("Failed to load the option records !");
        }
        
    });
    
    //$("#qrySignDate").val(todayStr);
    
    
}

function chkSign(chkBtn){	
	
	$("input[name='txtSignType']").each(function (){        
        $(this).prop("checked", false);
    });
	$(chkBtn).prop("checked", true);
	console.log('chk value: ' + $(chkBtn).val());
	
}


function submitQuery(){
	
    $.ajax({
        url: evg.util.fn.toUrl('HMS1W002_ACTION', 'actionMethod=jr&methodName=querySignList'),        
        type: 'post',
        dataType: 'json',
        data:{                          
            info: evg.util.form.toString($('#queryTab'))            
        },
    }).done(function(result) {
        
        if(result.success) {           
        	//evg.util.fn.showInfo("簽到成功"); 
        	var template_obj = Handlebars.compile($('#hbt_SignRecords').html());
            var rendered = template_obj(result);            
            $("#table_queryResult tbody").replaceWith(rendered);                       
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

