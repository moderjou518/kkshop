(function(global, factory) {
    if (typeof global.jQuery !== 'undefined') { // jQuery is loaded
        factory(global);
    } else {
        throw new Error( "jQuery is not loaded" );
    }
})(typeof window !== "undefined" ? window : this, function(window, noGlobal) {
    // private屬性與方法
    var version = 0.1,
        strundefined = typeof undefined,
        evg = window.evg || {}; // 定義namespace, 名稱固定為evg
 
    evg.util = evg.util || {}; // 定義namespace, 名稱util可自定為其他單字
    evg.util.money = evg.util.money || function() { // 定義物件名稱, 名稱fn可自定為其他單字
        // your code
    };
    
    var thousandMark = $('script[data-evg-money-sep]').attr('data-evg-money-sep')||',';
    var zeroMark = $('script[data-evg-money-point]').attr('data-evg-money-point')||'.';
    var validate = $('script[data-evg-money-validate]').attr('data-evg-money-validate')||'.';
    var serial = $('script[data-evg-money-serial]').attr('data-evg-money-serial')||'.';
    
    //將字串加回三位一撇
    /**
     * @param money - 目前數值
     * @param i_decimal - 小數點到幾位
     * @param i_round - 四捨五入模式
     * @param i_sep - 是否要加千分位
     * @param i_init - 是否為初始化模式
     */
    var format = function(money, i_decimal, i_round, i_sep, i_init){
        if(money === '') {//20170113 Brad：如果是空字串，則維持空字串
            return money;
        }
        i_init = i_init||false;         
        var negative = "";
        var intArr = [];
                
        var moneyNum;
        if(i_init) {
            moneyNum = Number(money);
        }
        else {
            moneyNum = evg.util.money.toNumber(money);
        }
        
        var moneyStr;
        if(i_decimal===undefined)
        {
            moneyStr = moneyNum.toString();
        }
        else
        {
            if(i_round==='round'){
                moneyNum = Math.round(moneyNum*Math.pow(10, i_decimal))/Math.pow(10, i_decimal);
            }
            else if(i_round==='ceil'){
                moneyNum = Math.ceil(moneyNum*Math.pow(10, i_decimal))/Math.pow(10, i_decimal);
            }
            else if(i_round==='floor'){
                moneyNum = Math.floor(moneyNum*Math.pow(10, i_decimal))/Math.pow(10, i_decimal);
            }
                
            if(false) {
                moneyStr = moneyNum.toFixed(i_decimal).toString();    
            }
            else {
                moneyStr = moneyNum.toString();
                var dot = moneyStr.indexOf(".", 0);
                if(dot===-1) {
                    var current_dig_num = 0;    
                }
                else {
                    var current_dig_num = moneyStr.length - moneyStr.indexOf(".", 0) - 1;
                }
                
                if(dot===-1){
                    moneyStr += '.';
                }
                for (var digIdx = current_dig_num; digIdx < i_decimal; digIdx++) {
                    moneyStr += '0';
                }
            }
        }
        
        if(!i_sep) {
            return moneyStr;
        }    
        else {    
            if (moneyStr < 0) {
                negative = "-";
                moneyStr = moneyStr.substring(1, moneyStr.length);
            }
            
            var dot = moneyStr.indexOf(".", 0);
            var m_int = "";
            var m_decimal = "";
            if (dot !== -1) {
                m_decimal = moneyStr.substring(dot + 1, moneyStr.length);
                m_int = moneyStr.substring(0, dot);
            } else {
                m_int = moneyStr;
            }
            
            
            var count = 0;
            var newMoney = "";
            for (var i = m_int.length - 1; i >= 0; i--) {
                if (count === 3 && i >= 0) {
                    newMoney = thousandMark + newMoney;
                    count = 0;
                }
                newMoney = m_int.charAt(i) + newMoney;
                count++;
            }
            
            if (m_decimal !== "") {
                newMoney = negative + "" + newMoney + zeroMark + m_decimal;
            } else {
                newMoney = negative + "" + newMoney;
            }
            
            return newMoney;
        }        
    };
    //將字串中的千分位移除
    var deformat = function(tmp)                            
    {
        if(thousandMark==='.')
        {
            tmp = tmp.replace(new RegExp('\\.','g'),"");    
        }
        else if(thousandMark===',')
        {
            tmp = tmp.replace(new RegExp('\\,','g'),"");
        }
        else
        {
            tmp = tmp.replace(new RegExp('\\'+thousandMark,'g'),"");
        }
        return tmp;         
    };

    

    
    evg.util.money.prototype = { // 定義public屬性與方法
    	/**
    	 * 回傳千分位符號
    	 * @returns {String} 千分位符號
    	 */
        getThousandMark:function() {
    	    return thousandMark;
    	},
    	/**
    	 * 回傳小數點符號
    	 * @returns {String} 小數點符號
    	 */
    	getZeroMark:function() {
            return zeroMark;
        },
        /**
         * 取得序列化時是否需要轉型
         * @returns {String} 序列化時是否需要轉型
         * @private
         */
        getSerial:function() {
            return serial;
        },
        /**
         * 取得檢查時是否須考慮Money
         * @returns {String} 檢查時是否須考慮Money
         * @private
         */
        getValidate:function() {
            return validate;
        },
        /**
         * 將字串移除千分位並轉number
         * @param oriValue - 原始金額
         * @returns {Number} number物件
         * @example var num = evg.util.money.toNumber('1,000.00');
         */
        toNumber:function(oriValue)                            
        {
            oriValue = deformat(oriValue);
            oriValue = oriValue.replace(new RegExp('\\'+zeroMark,'g'),".");
            return Number(oriValue);         
        },
        /**
         * 處理鍵盤按下去的數字檢查事件，正常不需要手動呼叫
         * @param e
         */
        chkKeyPress:function(e) {
            //var m_decimal = this.getAttribute('data-evg-deci')||'0';
            //var m_int = this.getAttribute('data-evg-int')||'0';
            var code = e.keyCode;
            var char = String.fromCharCode(e.keyCode);
            var value = this.value;
            var dot = value.indexOf(zeroMark, 0);
            var cantkey = false;
            
            if(char === zeroMark && dot !== -1)
            {
                cantkey = true;
            }
            else if(char === thousandMark)
            {
                cantkey = true;
            }               
            //else if((code<48 || code>57) && code!== 46 && code!==45 && code !== 44  && code!==13)
            else if((code<48 || code>57) && code!==45 && char !== zeroMark && code!==13)
            {
                cantkey = true;
            }
            
            if(cantkey)
            {
                event.preventDefault();
                event.stopPropagation();
            }
        },
        /**
         * 處理需要轉成千分位的事件，正常不需要手動呼叫
         */
    	eventFormat:function(){
    			var m_decimal = this.getAttribute('data-evg-deci')||undefined;
    			var m_round = this.getAttribute('data-evg-money-round')||'round';
    			var m_sep = (this.getAttribute('data-evg-money-sep')||'true')=='true';
    			this.value = format(this.value, m_decimal, m_round, m_sep);
    	},
        /**
         * 處理需要移除千分位的事件，正常不需要手動呼叫
         */
        eventFormatSpan:function(){
                var m_decimal = this.getAttribute('data-evg-deci')||undefined;
                var m_round = this.getAttribute('data-evg-money-round')||'round';
                var m_sep = (this.getAttribute('data-evg-money-sep')||'true')=='true';
                this.innerHTML = format(this.innerHTML, m_decimal, m_round, m_sep);
        },
        /**
         * 處理需要轉成千分位的事件，正常不需要手動呼叫
         */
        eventFormatInit:function(){
                var m_decimal = this.getAttribute('data-evg-deci')||undefined;
                var m_round = this.getAttribute('data-evg-money-round')||'round';
                var m_sep = (this.getAttribute('data-evg-money-sep')||'true')=='true';
                this.value = format(this.value, m_decimal, m_round, m_sep, true);
        },
        /**
         * 處理需要移除千分位的事件，正常不需要手動呼叫
         */
        eventFormatSpanInit:function(){
                var m_decimal = this.getAttribute('data-evg-deci')||undefined;
                var m_round = this.getAttribute('data-evg-money-round')||'round';
                var m_sep = (this.getAttribute('data-evg-money-sep')||'true')=='true';
                this.innerHTML = format(this.innerHTML, m_decimal, m_round, m_sep, true);
            },
        /**
         * 手動針對某區塊進行千分位格式轉換
         * @param target 要檢查的區塊
         */
        formatMoney:function(target){
            target.find('input.money').each(evg.util.money.eventFormat);
            target.find('span.money').each(evg.util.money.eventFormatSpan);
        },
        /**
         * 手動針對某區塊進行千分位格式轉換
         * @param target 要檢查的區塊
         */
        formatMoneyInit:function(target){
            target.find('input.money').each(evg.util.money.eventFormatInit);
            target.find('span.money').each(evg.util.money.eventFormatSpanInit);
        }    
    };
     
    evg.util.money = new evg.util.money(); // 產生evg.util.money物件
     
    // Self-Executing Anonymous Function 
    (function() {
    	$(document).on('focus', 'input.money', function (e) {
    		this.value = deformat(this.value);
    	});
    	$(document).on('blur', 'input.money', evg.util.money.eventFormat);
    	//$(document).on('DOMNodeInserted', 'input.money', evg.util.money.eventFormat);
    	$(document).on('keypress', 'input.money', evg.util.money.chkKeyPress);
    	$('input.money').each(evg.util.money.eventFormatInit);
    	$('span.money').each(evg.util.money.eventFormatSpanInit);
    	
    	/*
    	$(document).on("paste", 'input.money', function(e){
    	    var m_decimal = this.getAttribute('data-evg-deci')||undefined;            
    	    var valueOri, valueNew;
    	    if (window.clipboardData && window.clipboardData.getData) {
    	        valueOri = window.clipboardData.getData('Text');
            } else if (original.clipboardData && original.clipboardData.getData) {
                valueOri = original.clipboardData.getData('text/plain');
            }
    	    
    	    valueOri = deformat(valueOri);
    	    
    	    var canPaste = true;
    	    for(var i=0;i<valueOri.length;i++)
    	    {
    	        var char = valueOri.charAt(i);
    	        var code = valueOri.charCodeAt(i);
    	        if((code<48 || code>57) && char !== zeroMark)
    	        {
    	            canPaste = false;
    	            break;
    	        }
    	    }
    	    
    	    if(canPaste)
    	    {
    	        this.value = valueNew;   
    	    }    	    
    	    
    	    e.preventDefault();
            return false;
    	} );
    	*/
    })();
     
    // 將evg, evg.util, evg.util.money暴露為全域變數
    if ( typeof noGlobal === strundefined ) {
        window.evg = evg || {};
        window.evg.util = evg.util || {};
        window.evg.util.money = evg.util.money;
    }
    return evg.util.money;
});