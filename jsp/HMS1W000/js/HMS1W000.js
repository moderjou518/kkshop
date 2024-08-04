var pageEntity;
//放搜尋結果table的id 
var resultTableID = "table_queryResult";

// 放常用資料JSON
var hmp1MiscData = {};

$(document).ready(function() {
    
	bindEvent();
   
});


function bindEvent(){

    // 查詢
    $("#queryBtn").on("click", showVoidNotice);
    
    // 單筆新增
    $("#showCreateDivBtn").on("click",function(){
        evg.util.grid.addOne(resultTableID);    
    	//console.log(evg.util.form.toString($('#queryCondTab')));
        //$('#createDataModal').modal('show');        
    });
    
    
    
}


// 進入頁面預載資料
function pageLoad(){
    
    // 新增
    $.ajax({
        url: evg.util.fn.toUrl('HMP1W020_PageLoad'),            
        type: 'post',
        dataType: 'json'
    }).done(function(result) {
        
        if(result.success) {
            
            hmp1MiscData.clas2Options   = result.data.clas2Options;
            hmp1MiscData.utOptions      = result.data.utOptions;
            hmp1MiscData.stoOptions     = result.data.stoOptions;
            hmp1MiscData.plaOptions     = result.data.plaOptions;
            hmp1MiscData.HMP0_MGR       = result.data.HMP0_MGR;
            
            // 單位
            var template_obj = Handlebars.compile($('#hbt_UtOptions').html());
            var rendered = template_obj(result);
            $("#newItemUnit").html(rendered);
            $("#newItemUnitC").html(rendered);
            
            // 庫別
            template_obj = Handlebars.compile($('#hbt_StoOptions').html());
            rendered = template_obj(result);
            $("#newItemStore").html(rendered);
            
            // 原產地
            template_obj = Handlebars.compile($('#hbt_plaOptions').html());
            rendered = template_obj(result);
            $("#newItemOriginPla").html(rendered);           
           
            template_obj = Handlebars.compile($('#hbt_Clas2Options').html());
            rendered = template_obj(result);
            $("#newItemClas2").html(rendered);
            //$("#newItemClas2").chosen();
      
        }else{
            evg.util.fn.showError("Failed to load the option records !");
        }
        
    });
    
    
}





function showVoidNotice(){    
	console.log('test1');
    $('#voidNoticeModal').modal('show');
    console.log('test2');
    console.log(evg.util.form.toString($('#queryCondTab')));
    
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

