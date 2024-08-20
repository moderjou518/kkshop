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

// 進入頁面載入目前有開放的預訂單
function pageLoad(){
	
	$.ajax({
    	url: evg.util.fn.toActionWithTabKey('HBD1W010_ACTION'),
		data:{			
			actionMethod: 'jr',
			methodName: 'LOAD_BID_LIST'					
		},
		//global: false,
        type: 'post',
        dataType: 'json'
    }).done(function(result) {              
        
        if(result.success) {                       
        	
            template_obj = Handlebars.compile($('#hbt_bidList').html());
            $("#" + mastTblID + " tbody").replaceWith(template_obj(result));
            
            if(result.data.BIDLIST.length == 0){
            	$("#divQueryResult").show();
            	var msg = "【周一至周四開放預訂】";           	
            	$("#" + mastTblID + " tbody").replaceWith("<tbody><tr><th style='text-align: left' colspan='3'>" + msg + "</th></tr></tbody>");
            	//evg.util.fn.showWarning(msg);
            	evg.util.fn.alert(msg);
            }else{
            	$("#divQueryResult").show();
            }
            
        }else{
            evg.util.fn.showError("無法載入初始資料!");
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
	
	// 從item List 回到 BID LIST
	backBidList: function(){
		$("#divQueryResult").show();
        $("#divBidItemList").hide();
        $("#divCartItemList").hide();	                
        goBookmark("queryResultTop");
	},
	
	// 從 cart 回到 item list
	backBidItemList: function(){
		$("#divQueryResult").hide();
        $("#divBidItemList").show();
        $("#divCartItemList").hide();	                
        goBookmark("bidItemListTop");
	},
	
	confirmRemoveCartItem: function(btnRemove){		
		var trObj = $(btnRemove).parent().parent();		
		var txtQty = $(trObj).find("input[name=txtPoddQty]");
		if($(txtQty).val().trim() != ''){
			var msg = "確定刪除此品項?";        	
	        evg.util.fn.confirm(msg, function(isConfirm) {            	
	            if(isConfirm){$(trObj).remove();}
	        });// end confirm	
		}else{
			$(trObj).remove();
		}		
	},
	
	
	loadBidItem: function(bidUUID){
		
		console.log('bidUUID: ' + bidUUID);
		
		// 載入某一周的預訂單商品
		$.ajax({
	    	url: evg.util.fn.toActionWithTabKey('HBD1W010_ACTION'),
			data:{			
				//tabKey: myTabKey,
				actionMethod: 'jr',
				methodName: 'LOAD_BID_ITEM_LIST',
				queryBidUUID: bidUUID
			},
	        type: 'post',
	        dataType: 'json'
	    }).done(function(result) {              
	        
	        if(result.success) {           
	            
	        	var containerID = "divBidItemList";
	        	
	        	if(result.data.BID_DAY_COUNT > 0){
	        		
	        		var template_obj = Handlebars.compile($('#hbt_BidItemList').html());
	        		$("#" + containerID).html(template_obj(result));
	                
	                // 初始化Grid, 塞 data-evg-orivalue
	                evg.util.grid.init(containerID, 1);                       
	                evg.util.form.setOriValue($('#' + containerID));
	                evg.util.form.markChangeBind($('#' + containerID).find(':input').not('[name="cbChk"]'));
	                
	                $("#divQueryResult").hide();
	                $("#divBidItemList").show();
	                $("#divCartItemList").hide();	                
	                // 自動捲到商品清單
	                goBookmark("bidItemListTop");
	                
	        	}else{	        		

	        		$("#" + dtlTblID + "tbody" ).replaceWith("<tbody><tr><td colspan='3'>沒有商品<br/>返回</td></tr></tbody>");
	        	}	        	
	        	
	        	return;
	        	
	        	var target_top = $("#itemListLink").offset().top;
	        	$("html,body").animate({scrollTop: target_top}, 1000);  //帶滑動效果的跳轉
	        	$("html,body").scrollTop(target_top);                        
	            
	        }else{
	            evg.util.fn.showError("LOAD BID ITEM LIST 失敗");
	        }	        
	    });
		
		
	},
	addItem: function(btnPlus){            
		var trObj = $(btnPlus).parent().parent();		
		var txtQty = $(trObj).find("input[name=txtPoddQty]");
		var qty = parseInt($(txtQty).val());
		if (qty < 1000){
			$(txtQty).val(qty+1);	
			$(txtQty).addClass("text-danger").addClass("font-weight-bold");
		}
		
    },
    
	minusItem: function(btnMinus){            
	
		let trObj = $(btnMinus).parent().parent();
		let txtQty = $(trObj).find("input[name=txtPoddQty]");
		let qty = parseInt($(txtQty).val());		
		if (qty > 0){
			qty = qty -1;
			$(txtQty).val(qty);
			if (qty > 0){				
				$(txtQty).addClass("text-danger").addClass("font-weight-bold");
			}else{
				$(txtQty).removeClass("text-danger").removeClass("font-weight-bold");
			}
		}
		
		/*
		if($(txtQty).val().trim() == '0'){
			var msg = "要移除此品項?";        	
	        evg.util.fn.confirm(msg, function(isConfirm) {            	
	            if(isConfirm){
	            	$(trObj).remove();
	            }
	        });// end confirm
		}
		*/
    },
    
    
    bidItem2CartItem: function(){
    	
    	console.log('---bidItem2CartItem---');
    	
    	/* 收集有訂購的物料 */    	
		var dayAry 	= new Array($("#divBidItemList").find('table').length);
		var dataAry = new Array($("#divBidItemList").find('table').length);
		
		$("#divBidItemList").find('table').each(function (i, el) {			
			dayAry[i]	= $(el).attr("data-order-date");
			dataAry[i] 	= evg.util.form.toString($(el));
		});		
		
		
		/* 將訂購的物料轉到購物車(want list)中 */
		$.ajax({
			url: evg.util.fn.toActionWithTabKey('HBD1W010_ACTION'),
			data:{
				//tabKey: myTabKey,
				actionMethod: 'jr',
    			methodName: 'BID_ITEM_TO_CART_ITEM',
				dayInfo: dayAry.toString(),
				dataInfo: dataAry.toString()
			},
			global: false,
			type: 'post',
			dataType: 'json'
		}).done(function(result) {
			
			if(result.success) {				
				
				var containerID = "divCartItemList";
        		var template_obj = Handlebars.compile($('#hbt_CartItemList').html());
        		$("#" + containerID).html(template_obj(result));                
        		
                // 初始化Grid, 塞 data-evg-orivalue
                evg.util.grid.init(containerID, 1);                       
                evg.util.form.setOriValue($('#' + containerID));
                evg.util.form.markChangeBind($('#' + containerID).find(':input').not('[name="cbChk"]'));
                
                // 20220823: 預覽訂單時，隱藏沒有訂購的商品，以免畫面太亂
                $(".poItemRow").each(function(index) {
                    let txtPoddQty = $(this).find("input[name=txtPoddQty]");
                    if ($(txtPoddQty).val() == '0'){
                        $(this).hide();
                    }                    
                });
                
                $("#divQueryResult").hide();
                $("#divBidItemList").hide();
                $("#divCartItemList").show();
                
                // 自動捲到確認清單
                goBookmark("cartItemListTop");
                console.log('END divCartItemList');
                
				
			}else{
				evg.util.fn.showError("ITEM TO CART 失敗!");
			}                
		});       	    	
    	
    	return;
    	
    	
    },
    
    showLogin: function(){   	   
    	
    	console.log('showLogin ..');   	    	
    	
    	$.ajax({
        	url: evg.util.fn.toActionWithTabKey('HBD1LOGIN_ACTION'),
    		data:{			
    			//tabKey: myTabKey,
    			actionMethod: 'jc'    			
    		},
    		global:true,
            type: 'post',
            dataType: 'json'
        }).done(function(result) {            
        	
            if(result.success) {            	            	
            	$("#txtLoginID").val(result.data.hasc0BascLoginId);                
            }else{
            	$("#txtLoginID").val('');            	
            }           
            
        });
    	
    },
    
    
    
    cartItem2OrderItem: function(){
    	
    	console.log('cart2Order');
    	
    	$("#btnCreateOrder").hide();
    	$("#btnModifyOrder").hide();
    	$("#txgMsg").show();
    	
    	
    	/* 收集有訂購的物料 */
    	//var table	= $("#divCartItemList");
		var dayAry 	= new Array($("#divCartItemList").find('table').length);
		var dataAry = new Array($("#divCartItemList").find('table').length);
		
		//console.log('010 ajax : @@@cartItem2OrderItem');
		//console.log('010 dayInfo :' + dayAry.toString());
		//console.log('010 dataInfo :' + dataAry.toString());
		
		$("#divCartItemList").find('table').each(function (i, el) {
			//console.log('--orderDate--' + $(el).attr("data-order-date"));
			//console.log('--dataAry--' + evg.util.form.toString($(el)));
			dayAry[i]	= $(el).attr("data-order-date");
			dataAry[i] 	= evg.util.form.toString($(el));
		});


		/* 將訂購的物料轉到購物車(want list)中 */
		$.ajax({
			url: evg.util.fn.toActionWithTabKey('HBD1W010_ACTION'),
			data:{
				//tabKey: myTabKey,
				actionMethod: 'ju',
    			methodName: 'CART_ITEM_TO_ORDER_ITEM',
    			loginInfo: evg.util.form.toString($("#loginForm")),
				dayInfo: dayAry.toString(),
				dataInfo: dataAry.toString()
			},
			global: false,
			type: 'post',
			dataType: 'json'
		}).done(function(result) {
			if(result.success) {
				//console.log('login ok & save order');
				//evg.util.fn.showSuccess('訂單完成');
				$("#btnSaveOK").show();
				$("#divQueryResult").hide();
                $("#divBidItemList").hide();
                $("#divCartItemList").hide();
                //$('#loginModal').modal('hide');
                $("#btnCreateOrder").show();
            	$("#btnModifyOrder").show();
            	$("#txgMsg").hide();
			}else{
				//console.log('login error');
				//console.log(result);				
				evg.util.fn.showError(result.msg);
			}                
		});
    	
    	
    },
    gotoTop: function(){
    	goBookmark("topLink");
    },
    showProfile: function(){
    	alert('Profile');
    }
    
};