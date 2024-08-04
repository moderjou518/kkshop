//放搜尋結果table的id 
var mastTblID = "tableQueryResult";
var dtlTblID  = "bidItemResult";
var today = new evg.util.date.iDate();
//var thisMonth = today.format("YYYYMMDD");
var thisYear = today.format("YYYY");
var thisMonth = today.format("MM");
var thisDay = today.format("DD");
var todayStr = today.format("YYYYMMDD");

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
	formEvent.loadWeekList('');
	formEvent.queryData(todayStr);		
	formEvent.pageLoad();
}



function bindEvent(){	
	$('#issueModal').on('shown.bs.modal', function () {
	    $('#btnIssue').focus();
	})    
}

function goBookmark(linkID){
	var target_top = $("#" + linkID).offset().top;
	$("html,body").animate({scrollTop: target_top}, 1000);  //帶滑動效果的跳轉
	$("html,body").scrollTop(target_top);
}

var formEvent = {
		
	quickQuery: function(btnWeekDay){		
		$("#btnWeekDayGroup button[name='btnQuickDay']").removeClass('btn-warning').addClass('btn-default');
    	$(btnWeekDay).removeClass('btn-default').addClass('btn-warning');    	
		var poDate = $(btnWeekDay).data('po-date');		
		formEvent.queryData(poDate);		
	},
	
	loadWeekList: function(ordSdate){
		
		$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W320_ACTION'),
			data:{			
				actionMethod: 'jr',
				methodName: 'LOAD_WEEKDAY_LIST',			
				selOrdSdate: ordSdate
			},
			global: false,
	        type: 'post',
	        dataType: 'json'
	    }).done(function(result) {
	        if(result.success) {        	
	            template_obj = Handlebars.compile($('#hbt_weekDayList').html());
	            $("#btnWeekDayGroup").html(template_obj(result));        
	            
	            ordSdate = result.data.OrdSDate;
	            // lightlight	            
	            $("#btnWeekDayGroup button[name='btnQuickDay']").removeClass('btn-warning').addClass('btn-default');
	            $("#btnWeekDayGroup button[data-po-date='" + ordSdate+ "']").removeClass('btn-default').addClass('btn-warning');
	            console.log('ordDate: ' + ordSdate);
	        }else{
	            evg.util.fn.showError("Failed to pageLoad !");
	        }        
	    });
	},
	
    pageLoad: function(){
    	
    	$.ajax({
			url: evg.util.fn.toActionWithTabKey('HBD1W320_ACTION'),
			data:{					
				actionMethod: 'jr',					
				methodName: 'PAGE_LOAD'
			},				
			global: false,
            type: 'post',
            dataType: 'json'                
        }).done(function(result) {
        	
            if(result.success) {                
                let template_obj = Handlebars.compile($('#hbt_YearList').html());               	   
                $("#txtYYYY").html(template_obj(result));                
            }else{
            	evg.util.fn.showWarning("查無資料");
            }
            
        });
    	
    },
	
	queryData: function(poDate){		
		
		$("#txtPoDate").val(poDate);
		$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W320_ACTION'),
			data:{
				actionMethod: 'jr',
				selDate1: poDate,
				selDate2: poDate,
				methodName: 'LOAD_TODAY_PO_LIST'					
			},
			global: true,
	        type: 'post',
	        dataType: 'json'
	    }).done(function(result) {	        
	        if(result.success) {                
	        	let msg = "";
	            if(result.data.CUST_LIST.length ==0){
	            	msg = "<tbody><tr><td>";
	            	msg += result.data.SHORT_DATE;
	            	msg += " 查無資料(0)</td></tr></tbody>";	            	
	            }else{
	            	template_obj = Handlebars.compile($('#hbt_poList').html());
		            msg = template_obj(result);		            
	            }
	            $("#" + mastTblID + " tbody").replaceWith(msg);	    
	            
	            // 報表時間	            
	            template_obj = Handlebars.compile($('#hbt_reportTime').html());
	            msg = template_obj(result);
	            //console.log(msg);
	            $("#tableMarkTime tbody").replaceWith(msg);
	            
	            
	            
	            
	            let dtlTblID = mastTblID;
	            let colCount = $("#" + dtlTblID +">tbody").find("tr:first th").length;
	            //console.log('col cunt: ' + colCount);
	            let rowCount = $("#" + dtlTblID +">tbody>tr:visible").length;
	            //console.log('row cunt: ' + rowCount);       
	            
	            let tblObj = $('#'+dtlTblID);                                
	            if (rowCount>0){
	            	let hiChk = false;
	            	for(i=2;i<colCount;i++){
	            		hiChk = false;
	            		$("#"+dtlTblID+">tbody>tr").each(function(index, tr) {
	            			let issueQty = $(tr).find("td:nth-child("+i+")").html();
	            			if(i==20){
	            				//console.log('issueQty: ' + issueQty);
		            		}
	            			if (issueQty > 0){
	            				//console.log(issueQty);
	            				hiChk = true;
	            			}
	            		});
	            		
	            		if (hiChk==true){
	            			$("#"+dtlTblID).find("tbody>tr, thead>tr").children(":nth-child("+i+")").show();                                        
	            		}else{
	            			$("#"+dtlTblID).find("tbody>tr, thead>tr").children(":nth-child("+i+")").hide();
	            		}
	            	}
	            }
	            
	            
	        }else{
	            evg.util.fn.showError("Failed to Query Data !");
	        }
	        
	    });

	},
	
	showChangeModal: function(){						
		$('#changeModal').modal('show');		
	},
	
	changeQuery: function(btn){
		
		$("button[name='btnQuickMonth']").removeClass('btn-warning').addClass('btn-default');
    	$(btn).removeClass('btn-default').addClass('btn-warning');    	
    	
		let selMonth = $("#txtYYYY").val() + $(btn).data('po-date');
		//console.log('selMonth: ' + selMonth);
		$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W320_ACTION'),
			data:{			
				actionMethod: 'jr',
				methodName: 'LOAD_BID_LIST',
				queryMonth: selMonth
			},
			global: false,
	        type: 'post',
	        dataType: 'json'
	    }).done(function(result) {
	        if(result.success) {
	            template_obj = Handlebars.compile($('#hbt_bidList').html());
	            $("#weekList tbody").replaceWith(template_obj(result));            
	        }else{
	            evg.util.fn.showError("Failed to pageLoad !");
	        }	        
	    });
		
		
	},
	
	selectWeek: function(bidUUID, poDate1, poDate2){
		
		// todo: 更新當周星期		
		poDate1 = $("#txtYYYY").val() + poDate1;
		poDate2 = $("#txtYYYY").val() + poDate2;				
		$("#txtPoDate").val(poDate1);
		
		$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W320_ACTION'),
			data:{
				actionMethod: 'jr',
				selDate1: poDate1,
				selDate2: poDate1,
				methodName: 'LOAD_TODAY_PO_LIST'					
			},
			global: true,
	        type: 'post',
	        dataType: 'json'
	    }).done(function(result) {	        
	        if(result.success) {                
	        	let msg = "";
	        	$("#txtSelWeek").val(result.data.BID_DATA.bidName);
	            if(result.data.CUST_LIST.length ==0){
	            	msg = "<tbody><tr><td>";
	            	msg += result.data.SHORT_DATE;
	            	msg += " 查無資料(1)</td></tr></tbody>";	            	
	            }else{
	            	template_obj = Handlebars.compile($('#hbt_poList').html());
		            msg = template_obj(result);		            
	            }	            
	            $("#tableQueryResult tbody").replaceWith(msg);
	            
	            // 報表資料	            
	            template_obj = Handlebars.compile($('#hbt_reportTime').html());
	            msg = template_obj(result);
	            $("#tableMarkTime tbody").replaceWith(msg);
	            
	            $('#changeModal').modal('hide');
	            
	            // 更新日期
	            formEvent.loadWeekList(poDate1);
	        }else{
	            evg.util.fn.showError("Failed to Query Data !");
	        }	        
	    });
		
		
	},
	
	showIssueModal: function(btnObj){
		
		let trObj = $(btnObj).parent().parent();
		let bidUUID = $(btnObj).data("biduuid");
		let poDate = $(btnObj).data("pommdate");
		let poSeq = $(btnObj).data("pommseq");
		let buyer = $(btnObj).data("loginid");
		let buyerName = $(btnObj).data("loginname");
		
		console.log('duuid: ' + bidUUID);
		console.log('poDate: ' + poDate);
		console.log('poSeq: ' + poSeq);
		console.log('buyer: ' + buyer + buyerName);
		//alert(buyerName);
		//alert(bidUUID+poDate+poSeq+buyer+buyerName);		
		//return;
		//return false;
		
		
		
		//var containerID = "";
		$("#poddQueryResult>tbody").replaceWith("<tbody></tbody>");		
		$("#poddAddQueryResult>tbody").replaceWith("<tbody></tbody>");
		
		$("#modalTitle").html("出貨維護 [" + buyerName + "]-[" + poDate + "-" + poSeq + "]");					
		
		$("#txtIssueBidmUUID").val(bidUUID);
		$("#txtIssuePoDate").val(poDate);
		$("#txtIssuePoSeq").val(poSeq);
		$("#txtIssueBuyer").val(buyer);
		$('#tableQueryResult>tbody>tr').each(function() {
	        $(this).removeClass('bg-warning');
	    });
		$(trObj).addClass('bg-warning');
		
		$('#issueModal').modal('show');
		
				
		// 查詢PO資料
		$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W320_ACTION'),
			data:{				
				actionMethod: 'jr',
				methodName: 'LOAD_PODD_LIST',		
				selBuyer: buyer,
				selDate: poDate,
				selSeq: poSeq,
				selBidUUID: bidUUID
			},
	        type: 'post',
	        dataType: 'json',
	        global:false
	    }).done(function(result) {              
	        
	        if(result.success) {
	        	
	        	// 加購品項
	        	let template_obj = Handlebars.compile($('#hbt_POAddItemList').html());    	        	
	        	$("#poddAddQueryResult>tbody").replaceWith(template_obj(result));
                evg.util.grid.init('poddAddQueryResult', 1);                       
                evg.util.form.setOriValue($('#poddAddQueryResult'));
                evg.util.form.markChangeBind($('#poddAddQueryResult').find(':input').not('[name="cbChk"]'));

	        	//console.log('po item list: ' + result.data.PO_ITEM_LIST.length);	        	   	        	    	        	
	        	if(result.data.PO_ITEM_LIST.length >0){
	        		
	        		$("#poddQueryResult").show();
    	        	template_obj = Handlebars.compile($('#hbt_POItemList').html());
    	        	$("#poddQueryResult>tbody").replaceWith(template_obj(result));
	                evg.util.grid.init('poddQueryResult', 1);                       
	                evg.util.form.setOriValue($('#poddQueryResult'));
	                evg.util.form.markChangeBind($('#poddQueryResult').find(':input').not('[name="cbChk"]'));	                
	                
	    			// 只有當天的po單可以更改出貨
	                if (poDate == todayStr){           	                	
	                	$("#btnSaveIssue1").show();
	                	$("#btnSaveIssue2").show();
	                	$("#btnAddItem").show();
	                	$("#btnCloseIssue").val('取消');
	                	$("button[name='btnPlusItem'], button[name='btnMinusItem']").each(function (){				
	                		$(this).show();		    					
		                });
	                }else{
	                	$("#btnSaveIssue1").hide();
	                	$("#btnSaveIssue2").hide();
	                	$("#btnAddItem").hide();
	                	$("#btnCloseIssue").val('關閉');
	                	$("button[name='btnPlusItem'], button[name='btnMinusItem']").each(function (){				
	                		$(this).hide();		    					
		                });
	                }
	                
	                
	        	}else{
	        		//$("#" + containerID).html('<tbody>沒有訂單!</tbody>');
	        		$("#poddQueryResult").hide();
	        		//$("#poddQueryResult>tbody").replaceWith('<tbody>沒有訂單!</tbody>');
	        	}
	        	
	        	
	            
	        }else{
	            evg.util.fn.showError("Failed to pageLoad !");
	        }	        
	    });
		
		
	},
	
	txtPoddRcvdQty_blur: function(txt){
		
		if($(txt).val() == ''){
			$(txt).val('0');
		}		
	},
	
	saveIssue: function(){		
		
		let bidUUID = $("#txtIssueBidmUUID").val();
		let poDate = $("#txtIssuePoDate").val();
		let poSeq  = $("#txtIssuePoSeq").val();
		let buyer  = $("#txtIssueBuyer").val();		
		      	
    	var info 	= evg.util.form.toString($("#poddQueryResult"));
    	var addInfo = evg.util.form.toString($("#poddAddQueryResult"));
    	
    	if (info != "{}" || addInfo != "{}"){
    		
    		$("#btnSaveIssue1").hide();
    		$("#btnSaveIssue2").hide();
    		
    		$.ajax({
    			url: evg.util.fn.toActionWithTabKey('HBD1W320_ACTION'),
    			data:{
    				actionMethod: 'ju',
        			methodName: 'SAVE_RCV_INFO',        			
    				revInfo: evg.util.form.toString($("#poddQueryResult")),
    				addInfo: evg.util.form.toString($("#poddAddQueryResult")),
    				parmBuyer: buyer,
    				parmPoDate: poDate,
    				parmPoSeq: poSeq,
    				parmBidmUUID: bidUUID
    			},
    			global: false,
    			type: 'post',
    			dataType: 'json'
    		}).done(function(result) {
    			$("#btnSaveIssue1").show();
    			$("#btnSaveIssue2").show();
    			if(result.success) {
    				evg.util.fn.showSuccess("【出貨成功】" + $("#modalTitle").html());
    				$("#modalTitle").html();
    				$('#issueModal').modal('hide');    				    				
    				let trObj = $("#tableQueryResult>tbody>tr[buyerid=" + buyer + "]"); 
    				$(trObj).find("input[name=txtIssueMark]").val('出').removeClass('text-danger').addClass('font-weight-bold').addClass('font-italic');
    				//$(trObj).find("input[name=txtHac0BascName]").removeClass('text-danger');    				
    				$("#poddQueryResult>tbody").replaceWith("<tbody></tbody>");
    				$("#poddAddQueryResult>tbody").replaceWith("<tbody></tbody>");
    				
    				//$("#tableQueryResult>tbody>tr[buyerid=0918828066]").find("input[name=txtIssueMark]").val('出');
    				
    			}else{    				
    				evg.util.fn.showError("【出貨失敗】");    				
    			}                
    		});	
    	}else{
    		evg.util.fn.showWarning("沒有資料可存檔");
    	}
		
		
    },		

	
	
	addItem: function(btnMinus){            
		let trObj = $(btnMinus).parent().parent();		
		let txtQty = $(trObj).find("input[name=txtPoddRcvdQty]");
		let qty = parseInt($(txtQty).val());
		if (qty < 99){
			$(txtQty).val(qty+1);	
		}
		$(txtQty).trigger("change");
    },
	minusItem: function(btnMinus){            
		var trObj = $(btnMinus).parent().parent();
		var txtQty = $(trObj).find("input[name=txtPoddRcvdQty]");
		var qty = parseInt($(txtQty).val());
		if (qty > 0){
			$(txtQty).val(qty-1);	
		}
		$(txtQty).trigger("change");
    },
    
    IssueAll: function(){    	
    	    	
    	$("#poddQueryResult>tbody>tr").each(function() {
        	var txtPoddOrdQty = $(this).find("input[name=txtPoddOrdQty]");
        	var txtPoddRcvdQty = $(this).find("input[name=txtPoddRcvdQty]");
        	if ($(txtPoddOrdQty).val() != '0'){
        		$(txtPoddRcvdQty).val($(txtPoddOrdQty).val());	
        	}
    	});
    	
    	$("#btnSaveIssue1").focus();
    	
    },

 
    showLogin: function(){  	    	
    	$('#loginModal').modal('show');    	
    },
    

    
    gotoTop: function(){
    	goBookmark("topLink");
    },
    
    createImage: function(){
    	
    	$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W320_ACTION'),
			data:{			
				//tabKey: myTabKey,
				actionMethod: 'jc',
				methodName: 'CREATE_IMAGE'								
			},
	        type: 'post',
	        dataType: 'json'
	    }).done(function(result) {    		    	
	        if(result.success) {
	        	let reportImg = $("#txtPoDate").val() + ".jpg";    	        
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

    	
        
    	
    },
    
};