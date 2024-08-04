//放搜尋結果table的id 
var resultTableID = "table_queryResult";

//放常用資料JSON
var hmp1MiscData = {};

$(document).ready(function() {    

    formEvent.init();
    formEvent.bindEvents();    
    formEvent.pageLoad();
    templateHelper.register();
    
});

// 主程式
var formEvent = {
        
        init: function(){            
            
            
            
        },
        
        bindEvents: function(){        
        	
            //$("#queryBtn").on("click", formEvent.queryData);
            $("#btnAddRow").on("click", formEvent.addAData);            
            $("#btnSave").on("click", formEvent.saveGridData);
            
            // 按下 enter 就自動 query
            $("#qryTketName").keypress(function (event) {
                if (event.keyCode == 13) {
                    $("#queryBtn").trigger("click");
                }
            });
            
        },       
        
        pageLoad: function(){
        	
        	var compCode = $("#txtCompCode").val();
        	//console.log('pageload js cid: ' + compCode);       	
        	
        	/*
            $.ajax({
            	url: evg.util.fn.toActionWithTabKey('HBD1W630_ACTION'),
				data:{
					actionMethod: 'jr'
					,methodName: 'PAGE_LOAD'					
				},
				//global: false,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {              
                
                if(result.success) {
                    console.log('load ok');
                    console.log(result.data);              
                }else{
                    evg.util.fn.showError("Failed to pageLoad !");
                }                
            });
            */
        	
        },
        
        addAData: function(){        	
            // 新增一筆資料
        	$('#showDataModal').modal('show');
        	
        	$("#btnAddOne").show();
        	$("#btnSaveOne").hide();
        	
        	$("#txtHac0BascAcCode").val('');
        	$("#txtHascBascLoginId").val('');
        	$("#txtHac0BascName").val('');
        	$("#txtHac0BascPwd").val('123');
        	$("#dlHac0BascGroup").val('C');
        	$("#txtHac0BascNote").val('');        	
        	$("#txtVoidMark").prop("checked", false);
        	
        },
        
        changeType: function(dlType){       	        	
        	var selVal = $(dlType).val().split('-');        	
        	var newRow = $(dlType).parent().parent();
        	$(newRow).find("input[name='txtTketCode']").focus();
        	
        	if (selVal.length == 3){        		
        		// var newTketType = selVal[0];
        		// var newTketMpMark = selVal[2]; 市價註記            	
        		var newTketEtMark = selVal[1]; // 出券註記
        		$(newRow).find("span[name='txtTketEtMark']").html(newTketEtMark);
        	}else{
        		$(newRow).find("span[name='txtTketEtMark']").html('');
        	}        			
        },     

        
        form2String: function(){
        	console.log('form2String ok');
        },
        
        quickQuery: function(btn){
        	// reset value & set query type
        	
        	$("button[name='btnQuickQuery']").removeClass('btn-warning').addClass('btn-info');
        	$(btn).removeClass('btn-info').addClass('btn-warning');
        	let qryGroup = $(btn).attr("data-group");
        	console.log('quickQuery: ' + qryGroup);        	
            $("#qryGroup").val(qryGroup);
            formEvent.queryData();
        },
        
        queryData: function(){
        	
			$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W630_ACTION'),
				data:{				
					actionMethod: 'jr',
					qryInfo: evg.util.form.toString($('#queryCondTab')),
					methodName: 'QUERY_DATA_LIST'					
				},				
				global: false,
                type: 'post',
                dataType: 'json'                
            }).done(function(result) {
            	
                if(result.success) {
                	
                	result.data.ITEMCLASS = hmp1MiscData.ITEMCLASS;
                	
                	var template_obj = Handlebars.compile($('#hbt_QueryResult').html());
                    $("#" + resultTableID + " tbody").replaceWith(template_obj(result));
                    
                    // 初始化Grid, 塞 data-evg-orivalue
                    evg.util.grid.init(resultTableID, 1);                       
                    evg.util.form.setOriValue($('#' + resultTableID));
                    evg.util.form.markChangeBind($('#' + resultTableID).find(':input').not('[name="cbChk"]'));
                    
                }else{
                	//evg.util.fn.showInfo("查無資料")
                	//evg.util.fn.showSuccess("查無資料")
                	//evg.util.fn.showError("查無資料")
                	evg.util.fn.showWarning("查無資料");
                }
                
            });
        },
        
        showData: function(btnShow){
        	
        	var trObj = $(btnShow).parent().parent();        	
        	var txtAcID = $(trObj).find("input[name='txtHac0BascAcCode']").val();
        	var txtLoginID = $(trObj).find("input[name='txtHac0BascLoginId']").val();
        	var txtAcName = $(trObj).find("input[name='txtHac0BascName']").val();
        	var txtHac0BascPwd = $(trObj).find("input[name='txtHac0BascPwd']").val();
        	var txtGroup = $(trObj).find("input[name='txtHac0BascGroup']").val();
        	var txtNote = $(trObj).find("input[name='txtHac0BascNote']").val();
        	var txtVoid = $(trObj).find("input[name='txtVoidMark']").val();
        	        	
        	$('#showDataModal').modal('show');      
        	$("#btnAddOne").hide();
        	$("#btnSaveOne").show();
        	
        	$("#txtHac0BascAcCode").val(txtAcID);
        	$("#txtHascBascLoginId").val(txtLoginID);
        	$("#txtHac0BascName").val(txtAcName);
        	$("#txtHac0BascPwd").val(txtHac0BascPwd);
        	$("#dlHac0BascGroup").val(txtGroup);
        	$("#txtHac0BascNote").val(txtNote);
        	
        	if (txtVoid == 'V'){
        		$("#txtVoidMark").prop("checked", true);	
        	}else{
        		$("#txtVoidMark").prop("checked", false);
        	}        	
        	
        },
        
        // 格式檢查
        chkSaveCond: function(){
        	
            // 進行檢查，有錯誤就提示 & 停止    
        	
        	if(evg.util.validator.validate($("#showDataModal")).getErrorNum()!==0){
                return false;
            }            
        	
        	return true;
            
        },
        
        submitSaveOne: function(){
        	
        	console.log('submitSaveOne');
        	
        	if(!formEvent.chkSaveCond()){
        		return false;
        	}
        	
        	$.ajax({
        		url: evg.util.fn.toActionWithTabKey('HBD1W630_ACTION'),
				data:{				
					actionMethod: 'ju',        					
					methodName: 'SAVE_DATA_ONE',
					info: evg.util.form.toString($('#showDataModal'))                     
				},			                          	
                global:false,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {
                
                if(result.success) {
                	formEvent.queryData();       
                	$('#showDataModal').modal('hide');
                	evg.util.fn.showSuccess("完成更新");
                }else{                	
                	console.log(result.msg);
                	evg.util.fn.showError("存檔失敗，請檢查資料是否重覆或文字長度過長");
                }
                
            });
        	
        },
        
        submitAddOne: function(){
        	
        	if(!formEvent.chkSaveCond()){
        		return false;
        	}
        	
        	$.ajax({
        		url: evg.util.fn.toActionWithTabKey('HBD1W630_ACTION'),
				data:{				
					actionMethod: 'jd',        					
					methodName: 'ADD_DATA_ONE',
					info: evg.util.form.toString($('#showDataModal'))                     
				},			                          	
                global:false,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {
                
                if(result.success) {
                	formEvent.queryData();       
                	$('#showDataModal').modal('hide');
                	evg.util.fn.showSuccess("完成新增");
                }else{                	
                	console.log(result.msg);
                	evg.util.fn.showError("新增失敗，請檢查資料是否重覆或文字長度過長");
                }
                
            });
        	
        }
                
        
        
                
       
        
};
