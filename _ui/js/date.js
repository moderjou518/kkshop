(function(global, factory) {
    if (typeof global.jQuery !== 'undefined') { // jQuery is loaded
        factory(global);
    } else {
        throw new Error( "jQuery is not loaded" );
    }
})(typeof window !== "undefined" ? window : this, function(window, noGlobal) {
    /**
     * 作為中介，會固定呼叫指定Function，並把原本參數都丟進去，回傳iDateObj
     * @returns {Date} Date物件
     */
    var callDateFnsAgentReturnIDate = function(funcName, iDateObj, otherArgArr) {
        var newParmArr = [];//建立預計傳入參數
        if(iDateObj){
            newParmArr.push(iDateObj.date);//固定第一個是Date物件
        }
        //固定把其他參數放在後面
        if(otherArgArr) {
            for (var argIdx = 0; argIdx < otherArgArr.length; argIdx++) {
                newParmArr.push(otherArgArr[argIdx]);
            }    
        }
        
        var returnDateObj = new evg.util.date.iDate();
        returnDateObj.date = dateFns[funcName].apply(null, newParmArr);//直接呼叫目標Function
        return returnDateObj;
    }
    
    /**
     * 作為中介，會固定呼叫指定Function，並把原本Object參數都丟進去，回傳Date 物件
     * @returns {Date} Date物件
     */
    var callDateFnsAgentRetrunOri = function(funcName, iDateObj, otherArgArr) {
        var newParmArr = [];//建立預計傳入參數
        if(iDateObj){
            newParmArr.push(iDateObj.date);//固定第一個是Date物件
        }
        //固定把其他參數放在後面
        if(otherArgArr) {
            for (var argIdx = 0; argIdx < otherArgArr.length; argIdx++) {
                newParmArr.push(otherArgArr[argIdx]);
            }    
        }
        
        return dateFns[funcName].apply(null, newParmArr);//直接呼叫目標Function
    }
    
    // private屬性與方法
    var version = 0.1,
        strundefined = typeof undefined,
        evg = window.evg || {}; // 定義namespace, 名稱固定為evg
 
    evg.util = evg.util || {}; // 定義namespace, 名稱util可自定為其他單字
    evg.util.date = evg.util.date || function() { // 定義物件名稱, 名稱fn可自定為其他單字
    };

    evg.util.date.prototype = { // 定義public屬性與方法

    };
    
    evg.util.date = new evg.util.date(); // 產生evg.util.date物件
    
    evg.util.date.max = evg.util.date.max || function() {
        return callDateFnsAgentReturnIDate("max", null, evg.util.date.toDateArr(arguments));
    };
    evg.util.date.min = evg.util.date.min || function() {
        return callDateFnsAgentReturnIDate("min", null, evg.util.date.toDateArr(arguments));
    };
    evg.util.date.eachDay = evg.util.date.eachDay || function(date1, date2) {
        return callDateFnsAgentRetrunOri("eachDay", null, [evg.util.date.toDate(date1), evg.util.date.toDate(date2)]);
    };
    
    evg.util.date.toDate = evg.util.date.toDate || function(inputDate) {
        if(Object.prototype.toString.call(inputDate) === "[object Date]"){
            return inputDate;
        }
        else if(inputDate instanceof evg.util.date.iDate) {
            return inputDate.date;
        }
        else {
            return null;
        }
    };

    evg.util.date.toDateArr = evg.util.date.toDateArr || function(dateArr) {
        var newDateArr = [];
        for (var argIdx = 0; argIdx < dateArr.length; argIdx++) {
            newDateArr.push(evg.util.date.toDate(dateArr[argIdx]));
        }
        return newDateArr;
    };
    
    evg.util.date.iDate = evg.util.date.iDate ||function () {
        if (!(this instanceof evg.util.date.iDate)) {
            if(arguments.length === 0){
                return new evg.util.date.iDate();
            }
            else {
                return new evg.util.date.iDate(arguments[0]);   
            }
        }
        else {
          //處理傳入類型(無參數，預設new Date())
            if(arguments.length === 0){
                this.date = new Date();
            }
            else {
                var inputDate = arguments[0];
                if (Object.prototype.toString.call(inputDate) === "[object Date]"){
                    this.date = inputDate;
                }
                else if (typeof inputDate === 'string' || inputDate instanceof String) {
                    this.date = dateFns.parse(inputDate, new Date());//只能讀取ISO 8601格式
                }
                else {
                    throw new Error("Invalid date:"+inputDate);
                }
            }    
        }
        
        
    };

    
    
    /*寫法一
    evg.util.date.iDate.prototype = {
        addMonths:function() {
            return callDateFnsAgent('addMonths', this, arguments);
        },
        addDays:function() {
            return callDateFnsAgent('addDays', this, arguments);
        },
        format:function(formatStr) {
            return dateFns.format(this.date, formatStr);
        },
    };
    */
    
    //寫法二
    evg.util.date.iDate.prototype = {};
    evg.util.date.iDate.prototype.equals = function(inputDate) {
        return dateFns.isEqual(this.date, evg.util.date.toDate(inputDate));
    };
    evg.util.date.iDate.prototype.toJSON = function() {
        return this.date.toJSON();
    };
    evg.util.date.iDate.prototype.toString = function() {
        return this.date.toString();
    };
    
     
    
    /*寫法三  分類串Function name list*/
    /* Group 1: ori input, return ori */
    var funcNameStrArr =[ 'format'
                         ]; 
    var GetHelpersArr =[ 
                        'getTime',
                        'getMilliseconds',
                        'getSeconds',
                        'getMinutes',
                        'getHours',
                        'getDate',
                        'getDay',
                        //'getISODay',
                        'getMonth',
                        'getYear'
                        ]; 

    funcNameStrArr = funcNameStrArr.concat(GetHelpersArr);
    
    for (var funcIdx = 0; funcIdx < funcNameStrArr.length; funcIdx++) {
        let funcName = funcNameStrArr[funcIdx];
        evg.util.date.iDate.prototype[funcName] = function() {
            return callDateFnsAgentRetrunOri(funcName, this, arguments);
        };
    }
    
    
    
    
    /*Group 2: date input, return ori*/
    var funcNameParmObjArr =[ 
                        'getDayOfYear',
                        'getDaysInYear',
                        'getDaysInMonth',
                          ]; 
    var IsHelpersArr = [
                        'isAfter',
                        'isBefore',
                     /*   'isToday',
                        'isSameWeek',
                        'isThisWeek',
                        'isSameMonth',
                        'isThisMonth',
                        'isLeapYear',
                        'isSameYear',
                        'isThisYear'*/
                        ];
    
    var DifferenceInHelpersArr = [
                        'differenceInMilliseconds',
                        'differenceInSeconds',
                        'differenceInMinutes',
                        'differenceInHours',
                        'differenceInCalendarDays',
                        'differenceInDays',//完整的天
                        'differenceInWeeks',
                        'differenceInCalendarMonths',
                        'differenceInMonths',//完整的月
                        'differenceInCalendarYears',
                        'differenceInYears'//完整的年
                        ];
    funcNameParmObjArr = funcNameParmObjArr.concat(IsHelpersArr);
    funcNameParmObjArr = funcNameParmObjArr.concat(DifferenceInHelpersArr);

    for (var funcIdx = 0; funcIdx < funcNameParmObjArr.length; funcIdx++) {
        let funcName = funcNameParmObjArr[funcIdx];
        evg.util.date.iDate.prototype[funcName] = function(inputDate) {
            return callDateFnsAgentRetrunOri(funcName, this, evg.util.date.toDateArr(arguments));
        };
    }
    /*Group 3: ori input, return iDate */
    var funcNameObjArr = ['lastDayOfMonth']; 
    var SetHelpersArr = [
                    'setMilliseconds',
                    'setSeconds',
                    'setMinutes',
                    'setHours',
                    'setDate',
                    'setDay',
                   // 'setISODay',
                    'setMonth',//0-11
                    'setYear'
                    ];
    var AddHelpersArr = [
                    'addMilliseconds',
                    'addSeconds',
                    'addMinutes',
                    'addHours',
                    'addDays',
                    'addWeeks',
                    'addMonths',
                    'addYears'
                    ];

    var StartEndHelpersArr =[ 
                    'startOfWeek',
                    'startOfMonth',
                    'startOfYear',
                    'endOfWeek',
                    'endOfMonth',
                    'endOfYear'
                    ]; 
    funcNameObjArr = funcNameObjArr.concat(SetHelpersArr);
    funcNameObjArr = funcNameObjArr.concat(AddHelpersArr);
    funcNameObjArr = funcNameObjArr.concat(StartEndHelpersArr);

    /*function name list END*/
    
    for (var funcIdx = 0; funcIdx < funcNameObjArr.length; funcIdx++) {
        let funcName = funcNameObjArr[funcIdx];
        evg.util.date.iDate.prototype[funcName] = function() {
            return callDateFnsAgentReturnIDate(funcName, this, arguments);
        };
    }
    
    // Self-Executing Anonymous Function 
    (function() {
    	
    })();
     
    // 將evg, evg.util, evg.util.date暴露為全域變數
    if ( typeof noGlobal === strundefined ) {
        window.evg = evg || {};
        window.evg.util = evg.util || {};
        window.evg.util.date = evg.util.date;
    }
    return evg.util.date;
});
