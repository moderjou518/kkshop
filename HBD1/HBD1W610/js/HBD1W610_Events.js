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
            
        	
        	$('#showDataModal').on('shown.bs.modal', function () {
        	    $('#txtItemName').focus();
        	})  
            
        },
        
        bindEvents: function(){        
        	
            $("#queryBtn").on("click", formEvent.queryData);
            $("#btnAddRow").on("click", formEvent.addARow);            
            $("#btnSave").on("click", formEvent.saveGridData);            
            $("#btnAddOne").on("click", formEvent.submitAddOne);            
            $("#btnSaveOne").on("click", formEvent.submitSaveOne);
            
            
            
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
        	
        	// load misc
            $.ajax({
            	url: evg.util.fn.toActionWithTabKey('HBD1W610_ACTION'),
				data:{					
					actionMethod: 'jr',
					methodName: 'PAGE_LOAD'					
				},
				global: false,
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
                    
                    // Quick Search
                    template_obj = Handlebars.compile($('#hbt_ItemClassOptions').html());
                    $("#divQuickSearch").html(template_obj(result));
                    
                    // type select
                    template_obj = Handlebars.compile($('#hbt_ItemClassOptions4Select').html());
                    $("#qryItemType").html(template_obj(result)).chosen();           
                    hmp1MiscData.ITEMCLASS   = result.data.ITEMCLASS;
                    
                    formEvent.quickQuery('*');
              
                }else{
                    evg.util.fn.showError("Failed to pageLoad !");
                }
                
            });
            
            
        	
        },
        
        addData: function(){
        	
        	$('#showDataModal').modal('show');
        	$("#btnAddOne").show();
        	$("#btnSaveOne").hide();
        	
        },
        
        addARow: function(){            
        	
        	// 自動 focus 在新增的 input
            evg.util.grid.addOne(resultTableID);            
            var newRow = $("#" + resultTableID + " tbody tr:visible:first");
            $(newRow).find("input[name='txtTketLmtMon']").val(hmp1MiscData.contractMonth);
            $(newRow).find("input[name='txtItemName']").focus();
            //$(newRow).find("input[name='txtTketTypeData']").bind('change', );
            
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
        
        quickQuery: function(qryType){
        	// highlight
        	//$("button[name='btnQuickQuery']").removeClass('btn-warning').addClass('btn-info');
        	//$(btn).removeClass('btn-info').addClass('btn-warning');
        	
        	//let qryType = $(btn).attr("data-type");
        	
        	// reset value & set query type
        	//console.log('quickQuery: ' + qryType);
        	$("#qryItemName").val('');
            $("#qryItemType").val(qryType);
            formEvent.queryData();
        },
        
        queryData: function(){
        	
			$.ajax({
				url: evg.util.fn.toActionWithTabKey('HBD1W610_ACTION'),
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
                	evg.util.fn.showWarning("查無資料");
                }
                
            });
        },
        
        // 格式檢查
        chkSaveCond: function(){
        	
            // 進行檢查，有錯誤就提示 & 停止    
        	/*
        	if(evg.util.validator.validate($("#" + resultTableID + " tbody tr:visible")).getErrorNum()!==0){
                return false;
            }
            */
        	
        	return true;
            
        },
        
        submitAddOne: function(){
        	console.log('submitAddOne');
        	
        	$.ajax({
        		url: evg.util.fn.toActionWithTabKey('HBD1W610_ACTION'),
				data:{        					
					actionMethod: 'jc',        					
					methodName: 'ADD_DATA',
					info: evg.util.form.toString($('#showDataModal'))                    
				},			                          	
                global:false,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {
                
                if(result.success) {
                	evg.util.fn.showSuccess("新增成功");
                	$("#showDataModal").modal('hide');               	                       	                	
                	formEvent.queryData();
                }else{
                	console.log('insert fail');
                	console.log(result.msg);
                	evg.util.fn.showError("資料新增失敗，請檢查代碼是否重覆或資料長度是否過長");
                }
                
            });
        	
        	
        	
        },
        
        submitSaveOne: function(){
        	console.log('submitSaveOne');
        },
        
        submitSave: function(){
        	
        	// TODO: VALIDATOR
        	if(!formEvent.chkSaveCond()){
        		return false;
        	}else{
        		
        		var trAddGroup  = evg.util.grid.getTrAddArr(resultTableID);            
                var trUpdGroup  = evg.util.grid.getTrFilterArr(resultTableID, {recordType:'now', update:'Y'});
                var totCnt = trAddGroup.length + trUpdGroup.length;
                
                if (totCnt > 0){
                	
                	$.ajax({
                		url: evg.util.fn.toActionWithTabKey('HBD1W610_ACTION'),
        				data:{        					
        					actionMethod: 'ju',        					
        					methodName: 'SAVE_DATA_LIST',
        					addInfo: evg.util.grid.toString(trAddGroup), 
                            updInfo: evg.util.grid.toString(trUpdGroup) 
        				},			                          	
                        global:false,
                        type: 'post',
                        dataType: 'json'
                    }).done(function(result) {
                        
                        if(result.success) {
                        	formEvent.queryData();        
                        	console.log(' update ok');
                        	evg.util.fn.showSuccess("完成更新");
                        }else{
                        	console.log(' update fail');
                        	console.log(result.msg);
                        	evg.util.fn.showError("資料新增/更新失敗，請檢查代碼是否重覆或資料長度是否過長");
                        }
                        
                    });
                	
                }else{
                	evg.util.fn.showInfo("No changes save.");
                }
        		
        	}
        	
        },
        
        deleteTempRow: function(btnDelete){        	
        	$(btnDelete).parent().parent().remove();
        },
        
        deleteThisRow: function(btnDelete){
        	
        	var trObj = $(btnDelete).parent().parent();        	
        	var rid = $(trObj).find("input[name='txtItemUuid']").val();
        	console.log('uuid: ' + rid);
        	var msg = "確定要刪除此筆資料嗎?";
        	
            evg.util.fn.confirm(msg, function(isConfirm) {            	
                if(isConfirm){                	
                	$.ajax({
                		url: evg.util.fn.toActionWithTabKey('HBD1W610_ACTION'),
        				data:{        					
        					actionMethod: 'jd',        					
        					methodName: 'DELETE_THIS_DATA',
        					delID: rid 
        				},	                                    	
                        global:false,
                        type: 'post',
                        dataType: 'json'
                    }).done(function(result) {                        
                        if(result.success) {
                        	$(trObj).remove();
                        	evg.util.fn.showSuccess("完成刪除");
                        }else{
                        	evg.util.fn.showError("資料刪除失敗");
                        }                        
                    });                	
                }else{
                	evg.util.fn.showError("取消刪除");
                }
            });// end confirm
        	
        	
        },                
        
        batchDeleteSample: function(){
            
        	var trDelGroup  = evg.util.grid.getTrFilterArr(resultTableID, {recordType:'now', update:'Y'});
            console.log('submitDelete: ' +  evg.util.grid.toString(trDelGroup));
            
            $.ajax({
                url: evg.util.fn.toUrl('SET_MAIN'),
                data:{                	
                	methodName: 'DELETE_CHECKED_DATA',                         
                    delInfo: evg.util.grid.toString(trDelGroup)                    
                },    	
                global:false,
                type: 'post',
                dataType: 'json'
            }).done(function(result) {
                
                if(result.success) {
                	formEvent.queryData();           
                	evg.util.fn.showSuccess("完成刪除");
                }else{
                	evg.util.fn.showError("資料刪除失敗");
                }
                
            });
        	
        }        


                
       
        
};
