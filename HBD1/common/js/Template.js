var templateHelper = {
    register: function(){
        Handlebars.registerHelper("eq", function(v1, v2, options){
            if(v1==v2){
                return options.fn(this);
            }else{
              return options.inverse(this);
            }
        });
        
        Handlebars.registerHelper("neq", function(v1, v2, options){
            if(v1!=v2){
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
        
        Handlebars.registerHelper("inc", function(value, options){
        	return parseInt(value) + 1;
        });
    }
};