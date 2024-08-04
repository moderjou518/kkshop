(function(global, factory) {

    if (typeof global.jQuery !== 'undefined') { // jQuery is loaded
        if ( typeof module === "object" && typeof module.exports === "object" ) {
            // For CommonJS and CommonJS-like environments where a proper `window`
            // is present, execute the factory and get jQuery.
            // For environments that do not have a `window` with a `document`
            // (such as Node.js), expose a factory as module.exports.
            // This accentuates the need for the creation of a real `window`.
            // e.g. var jQuery = require("jquery")(window);
            // See ticket #14549 for more info.
            module.exports = global.document ?
                factory( global, true ) :
                function( w ) {
                    if ( !w.document ) {
                        throw new Error( "jQuery requires a window with a document" );
                    }
                    return factory( w );
                };
        } else {
            factory(global);
        }
    } else {
        throw new Error( "jQuery is not loaded" );
    }

})(typeof window !== "undefined" ? window : this, function(window, noGlobal) {
    'use strict';
    // private
    var version = 0.1,
        strundefined = typeof undefined,
        evg = window.evg || {};
    evg.util = evg.util || {};
    evg.util.fn = evg.util.fn || function() {
        var context = this;
        if (typeof this.actionList === 'object' && this.actionList.length === 0) {
            var action_list = scriptTag.attr("data-evg-action-list");
            context.visibleWhenNoAuth = scriptTag.attr("data-visible-whennoauth") === 'true';
            
            var initAjaxArr = [];
            
            /* 20200113: Mark by HMS1
            var getUserInfo = $.ajax({
                url: 'ActionDispatcher',
                type: 'post',
                data: {xctl:'getUserInfo',
                       appid:appid,
                       action_list:action_list}
            }).done(function(data) {
                var jobj = $.parseJSON(data);
                if(jobj.success!==false) {
                    context.actionList = jobj.actionList;
                    context.userInfo.loginId = jobj.loginId;
                    context.userInfo.accode = jobj.accode;
                    context.userInfo.envr = {};
                    if (typeof jobj.envr != strundefined) {
                        context.userInfo.envr.country = jobj.envr.country;
                        context.userInfo.envr.location = jobj.envr.location;
                        context.userInfo.envr.carrier = jobj.envr.carrier;
                    }                   
                }
                else {
                    context.actionList = [];
                    context.userInfo = undefined;
                }

                $(document).ajaxSuccess(function(event, request, settings) {
                    _chkActionExist(null);
                });

                _chkActionExist(null);
                
                //20161017 Brad：提供權限判斷功能，將還沒有判斷權限的清單，進行處理
                for (var i = 0; i < uncheckAuthArr.length; i++) {
                    var action_name = uncheckAuthArr[i].action_name;
                    var callback = uncheckAuthArr[i].callback;
                    handleCheckAuth(action_name, callback);
                }
            });            
            initAjaxArr.push(getUserInfo);
            */
        }
        
        if(!langMulti) {
            langInfoObj = new langInfo();
        }
        else {
            uiLocalStorage = localStorage.getItem(uiStorageKey);
            warLocalStorage = localStorage.getItem(warStorageKey);
            if (uiLocalStorage) {
                uiLocalStorage = JSON.parse(uiLocalStorage);
            }
            else {
                uiLocalStorage = {langMap:{}};
            }
            
            if (warLocalStorage) {
                warLocalStorage = JSON.parse(warLocalStorage);
            }
            else {
                warLocalStorage = {langMap:{}};
            }
            
            var realLangList = '';
            if(!uiLocalStorage.langMap[langPrefix + "common"]) {
                realLangList += (realLangList.length===0?'':',') + "common";
            }
            
            if(!warLocalStorage.langMap[langPrefix + "contextCommon"]) {
                realLangList += (realLangList.length===0?'':',') + currentAction + "#contextCommon";
            }
            
            if(!warLocalStorage.langMap[langPrefix + appid]) {
                realLangList += (realLangList.length===0?'':',') + currentAction;
            }
            
            
            if(langModule!='') {
                for (var mIdx = 0; mIdx < langModuleArr.length; mIdx++) {
                    var moduleEle = langModuleArr[mIdx];
                    if(!warLocalStorage.langMap[langPrefix + appid + "#" + moduleEle]) {
                        realLangList += (realLangList.length===0?'':',') + currentAction + "#" + moduleEle;
                    }
                }
            }
            
            if(realLangList.length===0) {
                langInfoObj = new langInfo();
            }
            else {
            	/* 20200113: Mark by HMS1
                var getLang = $.ajax({
                    url: 'ActionDispatcher',
                    type: 'post',
                    dataType: 'json',
                    data: {xctl:'lang',
                           langlist:realLangList,
                           langAcpt:langAcpt} 
                }).done(function(result) {
                    if(result.success) {
                        var uiLangMapChanged = false;
                        var warLangMapChanged = false;
                        Object.keys(result.data).forEach(function(langItem, index, arr){
                            if(langItem==='common') {
                                uiLocalStorage.langMap[langPrefix + "common"] = result.data[langItem];
                                uiLangMapChanged = true;
                            }
                            else {
                                warLocalStorage.langMap[langPrefix + langItem] = result.data[langItem];
                                warLangMapChanged = true;
                            }
                        });
                        
                        if(uiLangMapChanged) {
                            localStorage.setItem(uiStorageKey, JSON.stringify(uiLocalStorage));    
                        }
                        if(warLangMapChanged) {
                            localStorage.setItem(warStorageKey, JSON.stringify(warLocalStorage));
                        }
                        
                        langInfoObj = new langInfo();
                    }
                    else {
                        evg.util.fn.showError(result.msg);
                    }
                });
                
                initAjaxArr.push(getLang);
                */
            }
        }
        
        /**
         * 2020/01/14: Remark by HMS
         */
        /*
        $.when.apply($, initAjaxArr).then(function() {
                  context.userInfoLoaded = true;//20171103
                  //20171103 增加使用者資訊載入後的Function
                  onUserInfoLoadedFuncArr.forEach(function(func){
                      func();
                  });
        });
        */
    };
    
    var scriptTag = $('script[data-evg-appid]');
    var appid = scriptTag.attr("data-evg-appid");
    var entryContext = scriptTag.attr("data-evg-entry-context");
    //var thisPageAction = location.pathname.substr(entryContext.length + 1);
    var thisPageAction = location.pathname;
    var onUserInfoLoadedFuncArr = [];//20171103
    var commonLangInfo;
    var enableMultiLang;
    var currentAction = scriptTag.attr("data-evg-fn-current-action");
    var currentContext = scriptTag.attr("data-evg-current-context");
    var warModDate = scriptTag.attr("data-evg-fn-warlastmod");
    var uiModDate = scriptTag.attr("data-evg-fn-uilastmod");
    var langAcpt = scriptTag.attr("data-evg-fn-lang-acpt");
    var langMulti = scriptTag.attr("data-evg-fn-lang-multi")==='Y';
    var langModule = scriptTag.attr("data-evg-fn-lang-module")||'';
    var langModuleArr = langModule.split(",");
    var uiStorageKey = "_ui" + uiModDate;
    var warStorageKey = currentContext + warModDate;
    var langPrefix = "app.language.map." + langAcpt + ".";
    var uiLocalStorage, warLocalStorage;
    var langInfoObj;
    
    //20170126 Brad:抽出權限檢查之邏輯，並且提供設定是否隱藏沒有權限的div
    //20170207 Brad:沒有權限的區塊，隱藏時使用class，而非直接display:noe，以供後續判斷有此class的就不可以出現
    //20170217 Brad:提供只檢查指定區域之功能
    var _chkActionExist = function(target) {
        var scanTarget;
        if(evg.util.fn.visibleWhenNoAuth || 'DESKTOP_EnterDesktop' === evg.util.fn.currentAction()) {
            if(target===null) {
                scanTarget = $(':input[data-evg-action]').not('.hide-no-auth');
            }
            else {
                if(!(target instanceof jQuery)) {
                    target = $(target);
                }
                scanTarget = target.find(':input[data-evg-action]').not('.hide-no-auth');
                scanTarget = scanTarget.add(target.filter(':input[data-evg-action]').not('.hide-no-auth'));
            }
            
            scanTarget.each(function(index) {
                var ele = $(this);
                var action = ele.attr("data-evg-action");
                if ($.inArray(action, evg.util.fn.actionList) === -1) {
                    var noAuthAct = ele.attr('data-evg-noauth-act');//20191107 Brad:新增data-evg-noauth-act屬性，供指定無權限時的動作
                    if(noAuthAct === 'disable') {
                        $(this).prop('disabled', true);
                    }
                    else if(noAuthAct === 'hide') {
                        ele.addClass('hide-no-auth');
                    }
                    else {
                        if(this.tagName === 'BUTTON' ||
                           this.tagName === 'INPUT' && (this.type === 'button' || this.type === 'reset' || this.type === 'submit' || this.type === 'image')) {
                            ele.addClass('hide-no-auth');
                        }
                        else {
                            ele.prop('disabled', true);
                            ele.addClass('disable-no-auth');
                        }   
                    }
                }
            });
        }
        else {
            if(target===null) {
                scanTarget = $('[data-evg-action]').not('.hide-no-auth');
            }
            else {
                if(!(target instanceof jQuery)) {
                    target = $(target);
                }
                scanTarget = target.find('[data-evg-action]').not('.hide-no-auth');
                scanTarget = scanTarget.add(target.filter('[data-evg-action]').not('.hide-no-auth'));
            }
            
            scanTarget.each(function(index) {
                var ele = $(this);
                var action = ele.attr("data-evg-action");
                if ($.inArray(action, evg.util.fn.actionList) === -1) {
                    var noAuthAct = ele.attr('data-evg-noauth-act');//20191107 Brad:新增data-evg-noauth-act屬性，供指定無權限時的動作
                    if(noAuthAct === 'disable') {
                        $(this).prop('disabled', true);
                        ele.addClass('disable-no-auth');
                    }
                    else if(noAuthAct === 'hide') {
                        ele.addClass('hide-no-auth');
                    }
                    else {
                        if(this.tagName === 'TEXTAREA' || this.tagName === 'SELECT' ||
                           (this.tagName === 'INPUT' && (this.type !== 'button' && this.type !== 'reset' && this.type !== 'submit' && this.type !== 'image'))) {
                            $(this).prop('disabled', true);
                            ele.addClass('disable-no-auth');
                        }
                        else {
                            ele.addClass('hide-no-auth');
                        }
                    }
                }
            });   
        }
    };
    
    var setMsgDivHeight = function(targetWin, lengthOffset) {
        var height = 0;
        var childList = targetWin.$('#noty_topRight_layout_container').children('li');
        for (var childIdx = 0; childIdx < childList.length - lengthOffset & childIdx < 7; childIdx++) {
            height += childList.eq(childIdx).height() + 6;
        }
        targetWin.$('#noty_topRight_layout_container').css('height',(4 + height)+'px');
    };
    
    var showMsg = function(msg, type, timeout) {
        var targetWin;
        if (typeof top.noty !== strundefined) { // iframe用top的noty
            targetWin = top;            
        } else if (typeof noty !== strundefined) {
            targetWin = window;
        } else {
            throw new Error( "noty.js is not loaded" );
        }
        
        if(targetWin !== undefined) {
            $(function(){
                targetWin.noty({
                    text: msg,
                    type: type,
                    theme: 'evg_noty_theme',
                    layout: 'topRight',
                    closeWith: ['click', 'button', 'backdrop'],
                    timeout: timeout,
                    maxVisible: Number.MAX_VALUE, // you can set max visible notification for dismissQueue true option,
                    callback: {
                        onShow: function() {
                            setMsgDivHeight(targetWin, 0);
                        },
                        afterShow: function() {
                            setMsgDivHeight(targetWin, 0);
                        },
                        afterClose: function() {
                            setMsgDivHeight(targetWin, 1);
                        }
                      }
                });
                
                //targetWin.$('#noty_topRight_layout_container').css('height','300px');
                targetWin.$('#noty_topRight_layout_container').css('overflow-y','hidden');
                
                
                /*
                var noty_li_arr = targetWin.$('#noty_topRight_layout_container li');
                for (var noty_li_idx = 7; noty_li_idx < noty_li_arr.length; noty_li_idx++) {
                    var notyDiv = noty_li_arr.eq(noty_li_idx).children('div');
                    var notyObj = $.noty.get(notyDiv.attr('id'));
                    notyObj.close();
                }
                */
            });
        }
    }
    
    //20161017 Brad：提供權限判斷功能
    var uncheckAuthArr = [];//記載尚未判斷權限的清單
    var handleCheckAuth = function(action_name, callback) {
        if(evg.util.fn.actionList.indexOf(action_name) === -1) {
            callback.call(this, false);
        }
        else {
            callback.call(this, true);
        }
    };
    
    //20170306 Brad:提供Log功能
    var console_mode = 1;    
    {
        if (!window.console) {
            console = {
                log : function() {},
                debug : function() {},
                warn : function() {},
                error : function() {},
                info : function() {}
            }
        }
        else {
            if (!window.console.log) {
                console.log = function(msg) {};
            }
            if (!window.console.debug) {
                console.debug = function(msg) {
                    console.log(msg);
                };
            }
            if (!window.console.warn) {
                console.warn = function(msg) {
                    console.log(msg);
                };
            }
            if (!window.console.error) {
                console.error = function(msg) {
                    console.log(msg);
                };
            }
            if (!window.console.info) {
                console.info = function(msg) {
                    console.log(msg);
                };
            }            
        }
    };

    var langInfo = function() {
        this.joList = [];
        
        if(langMulti) {
            var langJo
            if(langModule!='') {
                for (var mIdx = 0; mIdx < langModuleArr.length; mIdx++) {
                    var moduleEle = langModuleArr[mIdx];
                    langJo = warLocalStorage.langMap[langPrefix + appid + "#" + moduleEle];
                    if(langJo && Object.keys(langJo).length!==0) {
                        this.joList.push(langJo);
                    }
                }
            }
            
            langJo = warLocalStorage.langMap[langPrefix + appid];
            if(langJo && Object.keys(langJo).length!==0) {
                this.joList.push(langJo);
            }
            
            langJo = warLocalStorage.langMap[langPrefix + "contextCommon"];
            if(langJo && Object.keys(langJo).length!==0) {
                this.joList.push(langJo);
            }
            
            langJo = uiLocalStorage.langMap[langPrefix + "common"];
            if(langJo && Object.keys(langJo).length!==0) {
                this.joList.push(langJo);
            }       
        }
        
        var translateCore = function(joList, defaultValue, itemId, parmArr) {
            var result = null;
            for (var langIdx = 0; langIdx < joList.length; langIdx++) {
                var langJo = joList[langIdx];
                if(langJo[itemId]) {
                    result = langJo[itemId];
                    break;
                }
            }
            
            if(result==null) {
                if(defaultValue==null) {
                    return "#" + itemId;
                }
                else {
                    result = defaultValue;
                }
            }
            
            if(parmArr===undefined || parmArr===null) {//如果不用額外處理，直接回傳
                return result;
            }
            
            for(var i=0;i<parmArr.length;i++) {
                result = result.replace(new RegExp("\\{\\{"+i+"\\}\\}", "g"), parmArr[i]);
            }
            return result;
        };
        
        var translateMapCore = function(joList, defaultValue, itemId, parmMap) {
            var result = null;
            for (var langIdx = 0; langIdx < joList.length; langIdx++) {
                var langJo = joList[langIdx];
                if(langJo[itemId]) {
                    result = langJo[itemId];
                    break;
                }
            }
            
            if(result==null) {
                if(defaultValue==null) {
                    return "#" + itemId;
                }
                else {
                    result = defaultValue;
                }
            }
            
            if(parmMap===undefined || parmMap===null) {//如果不用額外處理，直接回傳
                return result;
            }
            
            Object.keys(parmMap).forEach(function(currentName, index, arr){
                if(!result.hasOwnProperty(currentName)){
                    result = result.replace(new RegExp("\\{\\{"+currentName+"\\}\\}", "g"), parmMap[currentName]);
                }
            });
            return result;
        };
        
        /**
         * 進行翻譯
         * @param itemId
         * @param varargs parm
         * @return
         */
        this.translate = function(itemId) {
            if(!langMulti) {
                return '#'+itemId;
            }
            var parmArr = [];
            for(var i=1;i<arguments.length;i++) {
                parmArr.push(arguments[i]);
            }  
            return translateCore(this.joList, null, itemId, parmArr);
        };
        
        /**
         * 進行翻譯
         * @param itemId
         * @param defaultValue
         * @param varargs parm
         * @return
         */
        this.translateOpt = function(defaultValue, itemId) {
            if(!langMulti) {
                return defaultValue;
            }
            var parmArr = [];
            for(var i=2;i<arguments.length;i++) {
                parmArr.push(arguments[i]);
            }  
            return translateCore(this.joList, defaultValue, itemId, parmArr);
        };
     
        /**
         * 進行翻譯
         * @param itemId
         * @param defaultValue
         * @param parmMap
         * @return
         */
        this.translateMap = function(itemId, parmMap) {
            if(!langMulti) {
                return '#'+itemId;
            }
            return translateMapCore(this.joList, null, itemId, parmMap);
        };
     
        /**
         * 進行翻譯
         * @param itemId
         * @param parmMap
         * @return
         */
        this.translateMapOpt = function(defaultValue, itemId, parmMap) {
            if(!langMulti) {
                return defaultValue;
            }
            return translateMapCore(this.joList, defaultValue, itemId, parmMap);
        };
    }
    
    evg.util.fn.prototype = {
        getAppId: function(){
            return appid;
        }, 
        getEntryContext: function(){
            return entryContext;
        }, 
        // public
        actionList: [],
        userInfo: {},
        userInfoLoaded : false,//20171103 紀錄使用者資訊載入了沒有
        getTabkey: function() {
            return $('script[data-evg-tabkey]').attr("data-evg-tabkey");
        },
        /**
         * 將Action轉成URL
         * @param action ： action名稱
         * @returns URL
         * @example evg.util.fn.toUrl('ActionName')
         */
        toUrl: function(action, parm_str) {
        	
            if(action == null) {
                return null;
            }       
            var menu = 'W';
            var func;
            var URL;
            
            URL = '/servlet/WUF1_ControllerServlet.do?lang=zh-TW&menu=WBRN1&func=TRAFFIC_GUIDE&mLang=zh-TW&action=' + action;
            
            return URL;
            
            /**
             * 以下為 plus 原始程式
             
            if (parm_str == null) {
                URL = entryContext + "/" + action;
            }
            else if(typeof parm_str === "string") {//20170112 Brad:用typeof parm_str === "string"是比較準確的字串判斷方式
                URL = entryContext + "/" + action + "?" + encodeURI(parm_str);
            }
            else {
                var parm_str_real = '';
                Object.keys(parm_str).forEach(function(currentValue,index,arr) {
                    parm_str_real += (parm_str_real.length===0?'':'&') + encodeURIComponent(currentValue) + "=" + encodeURIComponent(parm_str[currentValue]);
                });
                URL = entryContext + "/" + action + "?" + parm_str_real;
            }
            return URL;
            */
        },
        /**
         * 將Action轉成URL
         * @param action ： action名稱
         * @param timeout ： 判斷是不是需要import timeout.js // 20191122 Abbie: 如果另開window但也想同步desktop的session timeout，就要設為true，其他情況皆不import
         * @returns URL
         * @example evg.util.fn.toUrl('ActionName')
         */
        toUrlWithTabkey: function(action, parm_str, timeout) {
            if(action == null) {
                return null;
            }            
            var tabkey = evg.util.fn.getTabkey();
            var URL;
            if (parm_str == null) {
                URL = entryContext + "/" + action + "?tabkey=" + tabkey;
            }
            else if(typeof parm_str === "string") {//20170112 Brad:用typeof parm_str === "string"是比較準確的字串判斷方式
                URL = entryContext + "/" + action + "?" + encodeURI(parm_str) + "&" + "tabkey=" + tabkey;
            }
            else {
                var parm_str_real = '';
                Object.keys(parm_str).forEach(function(currentValue,index,arr) {
                    parm_str_real += (parm_str_real.length===0?'':'&') + encodeURIComponent(currentValue) + "=" + encodeURIComponent(parm_str[currentValue]);
                });
                URL = entryContext + "/" + action + "?" + parm_str_real + "&" + "tabkey=" + tabkey;
            }
            // 20191122 Abbie 另開window要同步desktop的session timeout，則在URL後面加上參數方便判斷是否要import timeout.js START
            if(null !== timeout & (timeout == "true" || timeout == true)){
                URL = URL + "&timeout=true";
            }
            // 20191122 Abbie 另開window要同步desktop的session timeout，則在URL後面加上參數方便判斷是否要import timeout.js END
            return URL;
        },
        /**
         * 20170118 Brad:取得目前Action
         * @returns Action
         * @example evg.util.fn.currentAction()
         */
        currentAction: function() {
            return location.pathname.substr(entryContext.length + 1);
        },
        /**
         * 開啟視窗，原本window.open的參數清單為(URL,name,specs,replace)
         * 將action轉url並串上tabkey，與parm_str
         * @param action ： action名稱
         * @param parm_str ： parm字串，也可以是物件，key為parm name。value為parm value
         * @param name ： 原window.open參數
         * @param specs ： 原window.open參數
         * @param replace ： 原window.open參數
         * @param timeout ： 判斷是不是需要import timeout.js // 20191122 Abbie: 如果另開window但也想同步desktop的session timeout，就要設為true，其他情況皆不import
         * @returns 原window.open回傳
         * @example evg.util.fn.openWindow('ActionName','parm1=a&parm2=b','newWin')
         */
        openWindow: function(action, parm_str, name, specs, replace, timeout) {
            return window.open(evg.util.fn.toUrlWithTabkey(action, parm_str, timeout), name, specs, replace);
        },
        /**
         * 以AJAX形式進行檔案下載
         * @param url ： 檔案下載的url
         * @param parm ： parm字串，也可以是物件，key為parm name。value為parm value
         * @param wait ： 是否需要等檔案開始下載User才能繼續動作，true/false
         * @example evg.util.fn.downloadFile(evg.util.form.getBtnUrl($(this)), {parm1:1,parm2:2}, true);
         */
        downloadFile: function(option) {            
            var url, parm, wait, timeout, message, success, successWithTxt, fail, fileNameIn;//20180306 Brad:提供前端指定fileName功能
            if(typeof option === "string") {//原始介面
                url = arguments[0];
                parm = arguments[1];
                wait = arguments[2];
                message = 'Generating File, Please Wait.';
            }
            else {
                url = option.url;
                parm = option.parm;
                wait = option.wait || true;
                timeout = option.timeout;
                message = option.message || 'Generating File, Please Wait.';
                success = option.success;
                successWithTxt = option.successWithTxt;
                fileNameIn = option.fileName
            }
            
            var dialog;
            if(wait===true) {//決定wait時Modal的格式
                dialog = bootbox.dialog({
                    title: 'Hint Message',
                    message: "<span class='ajax-loader-progress'></span><p>"+message+"</p>",
                    onEscape: false,
                    closeButton: false
                });    
            }
            else {
                dialog = bootbox.dialog({
                    size: 'small',
                    title: 'Hint Message',
                    message: "<span class='ajax-loader-progress'></span><p>"+message+"</p>",
                    buttons: {
                        ok: {
                            className: "btn-default"
                        }
                    },
                });
            }
            

            
            var xhr = new XMLHttpRequest(); 
            xhr.open('POST', url, true);
            if(timeout !== undefined) {
                xhr.timeout = timeout;    
            }
            
            xhr.responseType = "blob";
            xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            
            xhr.onreadystatechange = function () { 
                if (xhr.readyState === 4) {
                    if(xhr.status === 200) {//成功
                        var blob = xhr.response;//取得blob
                        var content_disposition = xhr.getResponseHeader('Content-Disposition');//取得Content-Disposition
                        if(content_disposition != null) {
                            var filename;
                            if(fileNameIn == null) {
                                var filename_fm = content_disposition.indexOf("filename=")+9;
                                var filename_to = content_disposition.indexOf(";", filename_fm);//判斷filename後面還有沒有其他屬性，不過應該不會用到。
                                if(filename_to===-1) {
                                    filename = decodeURIComponent(content_disposition.substring(filename_fm));//取得檔名    
                                }
                                else {
                                    filename = decodeURIComponent(content_disposition.substring(filename_fm, filename_to));//取得檔名
                                }
                            }
                            else {
                                filename = fileNameIn;
                            }
                            
                            saveAs(blob, filename);//以blob下載檔案
                            evg.util.fn.hideModal(dialog);
                            
                            if(success!=undefined) {
                                success.call(undefined);    
                            }
                        }
                        else {
                            console.log('Report download failed.');
                            var reader = new FileReader();//將blob轉成text
                            reader.onload = function(event){
                                //reader.result可以取得回傳的字串
                                if(successWithTxt===undefined) {
                                    evg.util.fn.showError(reader.result);
                                }
                                else {
                                    successWithTxt.call(undefined, reader.result);  
                                }
                            };
                            reader.onerror = function(event){
                                //fail.call(undefined, 'readAsTextError');
                                evg.util.fn.showError('Download Fail.');
                            };
                            reader.readAsText(blob);
                            evg.util.fn.hideModal(dialog);
                        }   
                    }
                    else {//失敗
                        var blob = xhr.response;//取得blob
                        if(blob!=null) {
                            var reader = new FileReader();//將blob轉成text
                            reader.onload = function(event){
                                evg.util.fn.showError($(reader.result).find("#plus_err_msg").html());                            
                                //fail.call(undefined, 'httpFailWithMsg');
                            };
                            reader.onerror = function(event){
                                evg.util.fn.showError('Download Fail.');
                                //fail.call(undefined, 'httpFailWithoutMsg');
                            };
                            reader.readAsText(blob);
                            evg.util.fn.hideModal(dialog);    
                        }
                        else {
                            evg.util.fn.showError('Download Fail.');
                            //fail.call(undefined, 'httpFailWithoutMsg');
                            evg.util.fn.hideModal(dialog);    
                        }
                            
                    }
                }
            };
            
            var parm_str;
            if (parm == null) {
                parm_str = '';
            }
            else if(typeof parm === "string") {//這裡邏輯和openWindow的有點不一樣，因為是POST，所以應該不用傳Tabkey
                parm_str = parm;
            }
            else {
                parm_str = $.param(parm);
            }
            
            xhr.send(parm_str);
        },
        /**
         * 20170118 Brad:增加以post形式導頁功能
         * @param url ： 導頁目標url
         * @param parm_obj ： 傳遞之參數物件
         * @example evg.util.fn.redirect($('#url_redirect_target').val(),{parm1:'a',parm2:'b'})
         */
        redirect: function(action, parm_obj, target) {
            var URL = evg.util.fn.toUrl(action);
            
            var content = '<form name="tmp_form" action="'+URL+'" method="post" target="'+target+'">';
            Object.keys(parm_obj).forEach(function(currentValue, index, arr){
                content += '<input type="hidden" name="'+currentValue+' value="'+parm_obj[currentValue]+'>';
            });
            content += '</form>';
            $(document.body).append(content);
            $(document.body).find('form[name="tmp_form"]').last().get(0).submit();
        },
        /**
         * 顯示成功訊息
         * @param msg ： 訊息字串
         * @example evg.util.fn.showSuccess("Success Message")
         */
        showSuccess: function(msg, timeout) {
            timeout = timeout || 5000;
            showMsg(msg, 'success', timeout);
        },
        /**
         * 顯示一般訊息 
         * @param msg ： 訊息字串
         * @example evg.util.fn.showInfo("Info Message")
         */
        showInfo: function(msg, timeout) {
            timeout = timeout || 5000;
            showMsg(msg, 'information', timeout);
        },
        /**
         * 顯示警告訊息
         * @param msg ： 訊息字串
         * @example evg.util.fn.showWarning("Warning Message")
         */
        showWarning: function(msg, timeout) {
            timeout = timeout || 5000;
            showMsg(msg, 'warning', timeout);
        },
        /**
         * 顯示錯誤訊息
         * @param msg ： 訊息字串
         * @example evg.util.fn.showError("Error Message")
         */
        showError: function(msg) {
            showMsg(msg, 'error', false);
        },
        /**
         * 顯示錯誤訊息或寄Email
         * @param msgObj：格式 {"success":false,"msg":"error msg","issueId":"issue id"}
         * @example evg.util.fn.showErrorOrEmail({"success":false,"msg":'Update Fail',"issueId":'123'})
         */
        showErrorOrEmail: function(msgObj) {
            if(msgObj.issueId != null) {
                evg.util.fn.showIssueDialog(msgObj.issueId, msgObj.msg);
            }
            else {
                evg.util.fn.showError(msgObj.msg);
            }
        },
        /**
         * 提示使用者是否將錯誤資料寄給PIC
         * @param issueId
         * @param msg：可以不傳
         * @example showIssueDialog('123', 'Update Fail');
         */
        showIssueDialog: function(issueId, msg) {
            var message = (msg==null?"":"<p>"+msg+"</p>") + "<p>Do you want to send email to PIC?</p>";
            bootbox.dialog({
                title: 'Some Error Happen',
                message: message,
                onEscape: false,
                closeButton: false,
                buttons: {
                          ok: {
                              label: 'Yes',
                              className: 'btn-pramary',
                              callback: function() {
                            	  console.log('showIssueDialog: call back & do nothing');
                            	  /* 20200113: Mark by HMS1
                                  $.ajax({
                                      url: 'ActionDispatcher',
                                      type: 'post',
                                      data: {xctl:'notifyControlTeam',
                                             issueId:issueId}
                                  }).done(function(data) {
                                      
                                  });
                                  */
                              }
                          },
                          nok: {
                              label: 'No',
                              className: 'btn-default'
                          }
                }
                });  

        },
        /**
         * 顯示載入中
         * @param msg ： 訊息字串
         * @example evg.util.fn.showLoading("Hint Message", "Generating File, Please Wait.")
         */
        showLoading: function(title, msg) {
            var dialog = bootbox.dialog({
                title: title,
                message: "<span class='ajax-loader-progress'></span><p>"+msg+"</p>"
            });
            
            return dialog;
        },
        /**
         * 顯示alert
         * @param msg ： 訊息字串
         * @param callback ： alert按下ok後，執行的callback function
         * @example evg.util.fn.alert(msg, function() {
         * 
         * });
         */
        alert: function(msg, callback)
        {
            if(msg===undefined) {
                msg = 'undefined';
            }
            else if(msg===null) {
                msg = 'null';
            }
            else if(msg==='') {
                msg = ' ';
            }
            else{
                msg = msg.toString()
            }
            
            if(typeof top.bootbox !== strundefined)
            {
                $(function(){
                    top.bootbox.alert({
                        message: msg,
                        buttons: {
                            ok: {
                                label: evg.util.fn.translateOpt('OK', 'COMMON_028_OK'),
                                className: "btn-primary btn-plus"
                            }
                        },
                        callback: callback
                    });
                });
            }
            else if (typeof bootbox !== strundefined) {
                $(function(){
                    bootbox.alert({
                        message: msg,
                        buttons: {
                            ok: {
                                label: evg.util.fn.translateOpt('OK', 'COMMON_028_OK'),
                                className: "btn-primary btn-plus"
                            }
                        },
                        callback: callback
                    });
                });
            } else {
                throw new Error( "bootbox.js is not loaded" );
            }
        },
        /**
         * 顯示confirm
         * @param msg - 訊息字串
         * @param callback - confirm按下ok後，執行的callback function
         * @example evg.util.fn.confirm(msg, function(isConfirm) {
         *     if(isConfirm) 
         *         evg.util.fn.alert('doSomethingA()'); 
         *     else 
         *         evg.util.fn.alert('Exist');
         * });
         */
        confirm: function(msg, callback) {
            evg.util.fn.confirmP(msg, callback).then(null, function(){});
        },
        /**
         * 顯示confirmP，會回傳Promise物件
         * @param msg - 訊息字串
         * @param callback - confirm按下ok後，執行的callback function
         * @example evg.util.fn.confirmP(msg)
         *          .then(function() {
         *                  evg.util.fn.alert('doSomethingA()');
         *                },
         *                function() {
         *                  evg.util.fn.alert('Exist');
         *                });
         */
        confirmP: function(msg, callback) {
            return new Promise(function(resolve, reject) {
                if(msg===undefined) {
                    msg = 'undefined';
                }
                else if(msg===null) {
                    msg = 'null';
                }
                else if(msg==='') {
                    msg = ' ';
                }
                else{
                    msg = msg.toString()
                }
                
                var fnCallBack = function(decide) {
                    if(decide) {
                        resolve();
                    }
                    else {
                        reject();
                    }
                    if(callback!=null) {
                        callback.call(this, decide);    
                    }
                }
                
                if(typeof top.bootbox !== strundefined)
                {
                    $(function(){
                        top.bootbox.confirm({
                            //size: 'small',20180306 Brad:讓寬度和Alert一樣
                            message: msg,
                            buttons: {
                                confirm: {
                                    label: evg.util.fn.translateOpt('OK', 'COMMON_028_OK'),
                                    className: "btn-primary btn-plus"
                                },
                                cancel: {
                                    label: evg.util.fn.translateOpt('Cancel', 'COMMON_029_CANCEL'),
                                    className: "btn-default btn-plus pull-right order-1"
                                }
                                },
                            callback: fnCallBack
                        }).find('.btn-primary').css('margin-right','5px');
                    });
                }
                else if (typeof bootbox !== strundefined) {
                    $(function(){
                        bootbox.confirm({
                            //size: 'small',20180306 Brad:讓寬度和Alert一樣
                            message: msg,
                            buttons: {
                                confirm: {
                                    label: evg.util.fn.translateOpt('OK', 'COMMON_028_OK'),
                                    className: "btn-primary btn-plus"
                                },
                                cancel: {
                                    label: evg.util.fn.translateOpt('Cancel', 'COMMON_029_CANCEL'),
                                    className: "btn-default btn-plus pull-right order-1"
                                }
                                },
                            callback: fnCallBack
                        }).find('.btn-primary').css('margin-right','5px');
                    });
                } else {
                    throw new Error( "bootbox.js is not loaded" );
                }                
            });//回傳promise物件
        },
        /**
         * 處理Ajax錯誤後的動作，系統自動綁定，正常不需要呼叫
         * @param event - 包含 event 物件
         * @param jqXHR - 包含 XMLHttpRequest 物件
         * @param ajaxSettings - 包含 AJAX 請求中使用的選項
         * @param thrownError - 包含 JavaScript exception
         */
        ajaxError: function(event, jqXHR, ajaxSettings, thrownError){
            console.log('ajaxError at '+ ajaxSettings.url);//20170222 Brad:ajax錯誤時，在console印出錯誤的url
            if(jqXHR.status === 200) {
                evg.util.fn.showError('Parse Error : ' + ajaxSettings.dataType);
            } else if (jqXHR.status === 440) {
                evg.util.fn.alert('Session Timeout. Please log in again.', function() {
                    top.window.location.href = "DESKTOP_EnterLogin";
                });
            } else if (jqXHR.status !== 0) {
                var returnHTML = jqXHR.responseText;
                var errMsgDivCode = 'id="plus_err_msg"';
                var errMsgIdPos = returnHTML.indexOf(errMsgDivCode);
                var errMsgStartTagTo = returnHTML.indexOf(">", errMsgIdPos) + 1;
                var errMsgEndTagFm = returnHTML.indexOf("</div>", errMsgStartTagTo);
                var errMsg = returnHTML.substring(errMsgStartTagTo, errMsgEndTagFm).trim();
                evg.util.fn.showError(errMsg);                
            } else  {
                evg.util.fn.showError('Ajax Error('+jqXHR.statusText+'):' + ajaxSettings.url);//20171228 Brad：增加顯示Timeout錯誤
            }
        },
        /**
         * 判斷是否有此Action之權限，得到結果後，呼叫callback function，並傳入檢查結果(boolean)
         * 如果有權限的清單尚未取得，將會等到取得後，進行判斷
         * @param action_name - action名稱
         * @param callback - 取得權限資料後，呼叫的callback function。如果有權限則傳入true，反之則傳入false
         * @example evg.util.fn.chkAuth('EXAMPLES_Enter1_1', function(haveAuth) {
         *     if(haveAuth) 
         *         evg.util.fn.alert('have auth'); 
         *     else 
         *         evg.util.fn.alert('have no auth');
         * });
         */
        chkAuth: function(action_name, callback) {//20161017 Brad：提供權限判斷功能
            if(evg.util.fn.userInfo === undefined) {//代表是匿名
                handleCheckAuth(action_name, callback);
            }            
            else if (Object.keys(evg.util.fn.userInfo).length === 0) {//代表還沒有獲得使用者資訊
                uncheckAuthArr.push({action_name:action_name, callback:callback});
            }
            else {
                handleCheckAuth(action_name, callback);
            }
        },
        /**
         * 判斷是否有此Action之權限，回傳檢查結果(boolean)
         * 如果有權限的清單尚未取得，將會檢查失敗
         * @param action_name - action名稱
         * @returns {Boolean} 代表有沒有權限
         * @example if(evg.util.fn.haveAuth('EXAMPLES_Enter1_1')) {
         *     evg.util.fn.alert('有EXAMPLES_Enter1_1的權限');    
         * }
         */
        haveAuth: function(action_name) {//20161209 Brad：提供權限判斷功能
            return evg.util.fn.actionList.indexOf(action_name) != -1;
        },
        /**
         * 註冊後，只會在下一次所有Ajax完成後，被呼叫一次。呼叫後，解除綁定。
         * @param callback - 所有ajax停止後，呼叫的callback function
         * @example $(document).ready(function() {
         *     evg.util.fn.onAjaxStopOnce(function(){
         *         if(evg.util.fn.haveAuth('EXAMPLES_Enter1_1')) {
         *             evg.util.fn.showInfo('有EXAMPLES_Enter1_1的權限');    
         *         }
         *     });
         * });  
         */
        onAjaxStopOnce:function(callback) {
            if($.active!==0) {//如果現在有Ajax在執行，就等到全部結束再呼叫 call back function
                var fin = function(){
                    $(document).off("ajaxStop", fin);
                    callback.call();
                }
                
                $(document).on("ajaxStop", fin);    
            }
            else {//如果現在沒有Ajax在執行，就直接呼叫 call back function
                callback.call();
            }
        },
        hideNoActionElement:function(target) {
            _chkActionExist(target);
        },
        /**
         * 印出error訊息
         * @param msg - 訊息資料
         * @example evg.util.log.logError('error msg');
         */
        logError: function(msg)
        {
            console.error(msg);
        },
        /**
         * 印出warn訊息
         * @param msg - 訊息資料
         * @example evg.util.log.logWarn('warn msg');
         */
        logWarn: function(msg)
        {
            if (console_mode >= 2) {
                console.warn(msg);
            }
        },
        /**
         * 印出info訊息
         * @param msg - 訊息資料
         * @example evg.util.log.logInfo('debug msg');
         */
        logInfo: function(msg)
        {
            if (console_mode >= 3) {
                console.info(msg);   
            }
        },
        /**
         * 印出debug訊息
         * @param msg - 訊息資料
         * @example evg.util.log.logDebug('debug msg');
         */
        logDebug: function(msg)
        {
            if (console_mode >= 4) {
                console.debug(msg);
            }
        },
        /**
         * 設定Log 模式
         * @param mode_str：共有四種層級error/warn/info/debug
         */
        setLogMode: function(mode_str) {
            console.log('setLogMode:'+mode_str);
            //(1)ERROR (2)WARN (3)INFO (4)DEBUG
            if(mode_str === 'error') {
                console_mode = 1;
            }
            else if(mode_str === 'warn') {
                console_mode = 2;
            }
            else if(mode_str === 'info') {
                console_mode = 3;
            }
            else if(mode_str === 'debug') {
                console_mode = 4;
            }
            else {
                console_mode = 1;
            }
        },
        /**
         * 註冊當使用者資訊載入後要執行的動作，如果使用者資訊當時已載入，則會直接觸發
         * @param func
         */
        onUserInfoLoad: function(func) {
            if(!evg.util.fn.userInfoLoaded) {
                onUserInfoLoadedFuncArr.push(func);
            }
            else {
                func();
            }
        },
        /**
         * 供各Function資料交換使用的訊息物件，類似後端MsgObj的格式
         * @param func
         */
        getMsgObj: function(success, msg, data) {
            return {success:true, msg:msg, data:data};
        },
        /**
         * Submit Event
         * @param msg - 訊息資料
         * @param msg - 訊息資料
         * @example 
         */        
        submitEvent: function(action, eventParm) {
            let parm = eventParm;
            $.ajax({
                url: evg.util.fn.toUrl(action),//取得存放在按鈕上的action資料，轉成url
                type: 'post',
                dataType: 'json',
                data: {info:eventParm.toString()},
                success: function(result)
                         {
                            if(result.success) {
                                evg.util.fn.showSuccess('Register Event:' + result.msg);//顯示成功訊息
                            }
                            else {
                                console.log(parm);
                                if((parm instanceof evg.util.fn.PeriodicEventParameter || parm instanceof evg.util.fn.ForeverEventParameter) && (result.msg.indexOf('CronExpression')!=-1 && result.msg.indexOf('is invalid')!=-1)) {
                                    evg.util.fn.showError('Event Frequency Format Error');
                                }
                                else {
                                    evg.util.fn.showErrorOrEmail(result);
                                }
                                //debugger;
                                
                            }
                         }
           });
        },
        submitInstantEvent: function(action, eventId, eventDesc, parmData) {
            var parm = new evg.util.fn.InstantEventParameter();
            parm.setEventId(eventId);
            parm.setEventDesc(eventDesc);
            parm.setCmd("A");
            Object.keys(parmData).forEach(function(currentKey){
                if(parmData.hasOwnProperty(currentKey)){
                    parm.addJobParameter(currentKey, parmData[currentKey]);
                }
            });
            
            evg.util.fn.submitEvent(action, parm);
        },
        InstantEventParameter: function() {
            this.parameter = {cycleType:'I', jobParameter:{}, eventId:null, eventDesc:null, cmd:null, type:null};
            
            this.toString = function() {
                return JSON.stringify(this.parameter);        
            };
            
            this.setEventId = function (eventId) {
                this.parameter.eventId = eventId;
            };
            
            this.setEventDesc = function (eventDesc) {
                this.parameter.eventDesc = eventDesc;
            };
            
            this.setCmd = function (cmd) {
                this.parameter.cmd = cmd;
            };
            
            this.setType = function (type) {
                this.parameter.type = type;
            };
            
            this.addJobParameter = function (key, value) {
                this.parameter.jobParameter[key] = value;
            };
        },
        OneTimeEventParameter: function() {
            this.parameter = {cycleType:'O', jobParameter:{}, eventId:null, eventDesc:null, cmd:null, type:null, 
                              cronSecond:null, cronMinute:null, cronHour:null, cronDay:null, cronMonth:null, cronYear:null};
            
            this.toString = function() {
                return JSON.stringify(this.parameter);        
            };
            
            this.setEventId = function (eventId) {
                this.parameter.eventId = eventId;
            };
            
            this.setEventDesc = function (eventDesc) {
                this.parameter.eventDesc = eventDesc;
            };
            
            this.setCmd = function (cmd) {
                this.parameter.cmd = cmd;
            };
            
            this.setType = function (type) {
                this.parameter.type = type;
            };
            
            this.addJobParameter = function (key, value) {
                this.parameter.jobParameter[key] = value;
            };
            
            this.setCronSecond = function (cronSecond) {
                this.parameter.cronSecond = cronSecond;
            };
            
            this.setCronMinute = function (cronMinute) {
                this.parameter.cronMinute = cronMinute;
            };
            
            this.setCronHour = function (cronHour) {
                this.parameter.cronHour = cronHour;
            };
            
            this.setCronDay = function (cronDay) {
                this.parameter.cronDay = cronDay;
            };
            
            this.setCronMonth = function (cronMonth) {
                this.parameter.cronMonth = cronMonth;
            };
            
            this.setCronYear = function (cronYear) {
                this.parameter.cronYear = cronYear;
            };
        },
        PeriodicEventParameter: function() {
            this.parameter = {cycleType:'P', jobParameter:{}, eventId:null, eventDesc:null, cmd:null, type:null, groupId:null, status:null,
                              cronSecond:null, cronMinute:null, cronHour:null, cronDay:null, cronMonth:null, setCronWeek:null, cronYear:null,
                              startDateTime:null, endDateTime:null};
            
            this.toString = function() {
                return JSON.stringify(this.parameter);        
            };
            
            this.setEventId = function (eventId) {
                this.parameter.eventId = eventId;
            };
            
            this.setEventDesc = function (eventDesc) {
                this.parameter.eventDesc = eventDesc;
            };
            
            this.addJobParameter = function (key, value) {
                this.parameter.jobParameter[key] = value;
            };
            
            this.setCmd = function (cmd) {
                this.parameter.cmd = cmd;
            };
            
            this.setType = function (type) {
                this.parameter.type = type;
            };
            
            this.setGroupId = function (groupId) {
                this.parameter.groupId = groupId;
            };
            
            this.setStatus = function (status) {
                this.parameter.status = status;
            };
            
            this.setCronSecond = function (cronSecond) {
                this.parameter.cronSecond = cronSecond;
            };
            
            this.setCronMinute = function (cronMinute) {
                this.parameter.cronMinute = cronMinute;
            };
            
            this.setCronHour = function (cronHour) {
                this.parameter.cronHour = cronHour;
            };
            
            this.setCronDay = function (cronDay) {
                this.parameter.cronDay = cronDay;
            };
            
            this.setCronMonth = function (cronMonth) {
                this.parameter.cronMonth = cronMonth;
            };
            
            this.setCronWeek = function (cronWeek) {
                this.parameter.cronWeek = cronWeek;
            };
            
            this.setCronYear = function (cronYear) {
                this.parameter.cronYear = cronYear;
            };
            
            this.setStartDateTime = function (startDateTime) {
                this.parameter.startDateTime = startDateTime;
            };
            
            this.setEndDateTime = function (endDateTime) {
                this.parameter.endDateTime = endDateTime;
            };
        },
        ForeverEventParameter: function() {
            this.parameter = {cycleType:'F', jobParameter:{}, eventId:null, eventDesc:null, cmd:null, type:null, groupId:null, status:null,
                              cronSecond:null, cronMinute:null, cronHour:null, cronDay:null, cronMonth:null, setCronWeek:null, cronYear:null,
                              startDateTime:null};
            
            this.toString = function() {
                return JSON.stringify(this.parameter);        
            };
            
            this.setEventId = function (eventId) {
                this.parameter.eventId = eventId;
            };
            
            this.setEventDesc = function (eventDesc) {
                this.parameter.eventDesc = eventDesc;
            };
            
            this.addJobParameter = function (key, value) {
                this.parameter.jobParameter[key] = value;
            };
            
            this.setCmd = function (cmd) {
                this.parameter.cmd = cmd;
            };
            
            this.setType = function (type) {
                this.parameter.type = type;
            };
            
            this.setGroupId = function (groupId) {
                this.parameter.groupId = groupId;
            };
            
            this.setStatus = function (status) {
                this.parameter.status = status;
            };
            
            this.setCronSecond = function (cronSecond) {
                this.parameter.cronSecond = cronSecond;
            };
            
            this.setCronMinute = function (cronMinute) {
                this.parameter.cronMinute = cronMinute;
            };
            
            this.setCronHour = function (cronHour) {
                this.parameter.cronHour = cronHour;
            };
            
            this.setCronDay = function (cronDay) {
                this.parameter.cronDay = cronDay;
            };
            
            this.setCronMonth = function (cronMonth) {
                this.parameter.cronMonth = cronMonth;
            };
            
            this.setCronWeek = function (cronWeek) {
                this.parameter.cronWeek = cronWeek;
            };
            
            this.setCronYear = function (cronYear) {
                this.parameter.cronYear = cronYear;
            };
            
            this.setStartDateTime = function (startDateTime) {
                this.parameter.startDateTime = startDateTime;
            };
        },
        getLangInfo: function() {
            return langInfoObj;
        },
        translate: function() {
            return langInfoObj.translate.apply(langInfoObj, arguments);
        },
        translateOpt: function() {
            return langInfoObj.translateOpt.apply(langInfoObj, arguments);
        },
        translateMap: function(itemId, parmMap) {
            return langInfoObj.translateMap(itemId, parmMap);
        },
        translateMapOpt: function(defaultValue, itemId, parmMap) {
            return langInfoObj.translateMapOpt(defaultValue, itemId, parmMap);
        },
        enableMultiLang: function() {
            return langMulti;
        },

        /**20190322 Willy BS4 Spinners 搭配使用之API
         * 
         * @param $target : 指定要顯示BS4 Spinner的對象、應傳入JQuery DOM  
         * @param option.typeClass : 指定BS4 Spinner類型class
         * @param option.colorClass: 指定BS4 Spinner色彩class
         * 
         */ 
        spinStart: function($target,option) {
            
            var validTypeClass  = ['spinner-border','spinner-grow'];
            var validColorClass = ['text-primary','text-secondary','text-success','text-danger',
                                   'text-warning','text-info'     ,'text-light'  ,'text-dark'   ];
            var option    = option ? option: {};
            var typeClass = option['typeClass']  ? option['typeClass']  :'spinner-border';
            var colorClass= option['colorClass'] ? option['colorClass'] :'text-light';
            
            if($.inArray(typeClass, validTypeClass)===-1){
                throw Error('invalid TypeClass');
            }
            if($.inArray(colorClass, validColorClass)===-1){
                throw Error('invalid colorClass');
            }
            if(!$target){ //如果不指定對象的話，啟動整頁遮罩Spinner
                $('#spinner-overlay').remove();
                $('body').prepend('<div id="spinner-overlay" style="position: fixed;display: none;width: 100%;height: 100%;'+
                        '                             top: 0;left: 0;right: 0;bottom: 0;'+
                        '                             background-color: rgba(0,0,0,0.5);z-index: 9000;">'+
                        '                     <div class="'+typeClass+' '+colorClass+' position-fixed" '+
                        '                          style="width: 3rem; height: 3rem; '+
                        '                                 left: 50%; top: 50%;'+
                        '                                 role="status">'+
                        '                       <span class="sr-only">Loading...</span>'+
                        '                     </div>'+
                        '                 </div>');
                var spinnerWidth  =  $('#spinner-overlay').find('.'+typeClass).outerWidth();
                var spinnerHeight =  $('#spinner-overlay').find('.'+typeClass).outerHeight();
                $('#spinner-overlay').find('.'+typeClass).css('margin-top','calc(-'+spinnerHeight.toString()+'px / 2)');
                $('#spinner-overlay').find('.'+typeClass).css('margin-left','calc(-'+spinnerWidth.toString()+'px / 2)');
                $('#spinner-overlay').show();
            }
            else{//有指定對象
                if($target.is('button') || $target.is('input[type=button]')){//對象為按鈕的話
                    
                    if($target.is('input[type=button]')){//按鈕的tag不可為 input
                        throw Error('Button with input tag');
                    }
                    else{
                        $target.prop('disabled',true);
                        $target.prepend('<span class="'+typeClass+' '+colorClass+' '+typeClass+'-sm" role="status" aria-hidden="true"></span>&nbsp');
                    }

                }
                else{//其他視為局部區塊顯示Spinner 
                    
                    if($target.is('tr,td')){ //tag不可為 tr,td
                        throw Error('Spinner target can\'t be tr or td.');
                    }
                    else{
                        if($target.siblings('.spinner-border.position-absolute').length===0 &&
                           $target.siblings('.spinner-grow.position-absolute').length===0    ){
                                 $target.wrap('<div class="block-spinner-container"></div>'); 
                                 $target.closest('.block-spinner-container')
                                        .prepend('<div class="'+typeClass+' '+colorClass+' position-absolute" '+
                                                 '     style="display:none; left: 50%; top: 50%;z-index:500;"'+
                                                 '      role="status">'+
                                                 '  <span class="sr-only">Loading...</span>'+
                                                 '</div>' );  
                                 var spinnerWidth  =  $target.siblings('.'+typeClass+'.position-absolute').outerWidth();
                                 $target.siblings('.'+typeClass+'.position-absolute').css('margin-left','calc(-'+spinnerWidth.toString()+'px / 2)');
                             }
                             $target.siblings('.'+typeClass+'.position-absolute').show();
                    }
                }
            }
        },
        /**20190322 Willy BS4 Spinners 搭配使用之API
         * 
         * @param $target : 指定要關閉Spinner的對象、應傳入JQuery DOM    
         */      
        spinEnd: function($target) {
                       
            if(!$target){ //如果不指定對象的話，關閉整頁遮罩Spinner
                    if($('#spinner-overlay').is(":visible"))
                        $('#spinner-overlay').hide();     
            }
            else{//有指定對象                                              
                if($target.is('button') || $target.is('input[type=button]')){//對象為按鈕的話
                    
                    if($target.is('input[type=button]')){//按鈕的tag不可為 input
                        throw Error('Button with input tag');
                    }
                    else{
                        var hasValidTypeClass = false;
                        var currentTypeClass;
                        $(['spinner-border','spinner-grow']).each(function(index,item){
                            if($target.find('.'+item).length!==0){
                                hasValidTypeClass = true;
                                currentTypeClass = item;
                                return false;
                            }
                            else{
                                return;
                            }  
                        });
                        if(!hasValidTypeClass)
                            throw Error('invalid Spinner Type Class'); 
                        $target.prop('disabled',false);
                        $target.find('.'+currentTypeClass).remove();
                        $target.html($target.html().replace('&nbsp;','').trim());
                    }
                }
                else{//其他視為局部區塊關閉Spinner
                    var hasValidTypeClass = false;
                    var currentTypeClass;
                    $(['spinner-border','spinner-grow']).each(function(index,item){
                        if($target.siblings('.'+item).length!==0){
                            hasValidTypeClass = true;
                            currentTypeClass = item;
                            return false;
                        }
                        else{
                            return;
                        }  
                    });
                    if(!hasValidTypeClass)
                        throw Error('invalid Spinner Type Class'); 
                    $target.siblings('.'+currentTypeClass+'.position-absolute').hide();
                }
            }
        },
        hideModal:function(modal) {
            modal.on('shown.bs.modal', function(e) {
                modal.modal("hide");//隱藏
            });//因為Bootstrap 4可能會因為太早呼叫，而導致無法順利隱藏，因此在開啟後，才會隱藏，如果已經開啟，就會直接隱藏                                      
            
            if(modal.hasClass('in') || modal.hasClass('show')) {//如果已經出現, bs3用in判斷, bs4用show判斷
                modal.modal("hide");//就直接隱藏
            } 
        }
    };

    evg.util.fn = new evg.util.fn();

    // Self-Executing Anonymous Function
    (function() {
        $("[data-evg-check-unsaved]").each(function(index) {
            var x = $(this);
            x.data("unsaved", false);
            $(x.attr("data-evg-reset-target").split(',')).each(function(idx, element) {
                $(element).on('click', function() {
                    x.data("unsaved", false);
                });
            });
            x.on('change keydown paste input', ":input", function() { //trigers change in all input fields including text type
                x.data("unsaved", true);
            });
        });

        window.onbeforeunload = function () {
            var unsaved = false;
            $("[data-evg-check-unsaved]").each(function(index) {
                unsaved = $(this).data("unsaved");
                if (unsaved === true) {
                    return false; // break;
                }
            });
            if (unsaved === true) {
                return "It looks like you have been editing something. If you leave before saving, your changes will be lost.";
            }
        };

        $(document).ajaxError(evg.util.fn.ajaxError);
    })();

    if ( typeof noGlobal === strundefined ) {
        window.evg = evg || {};
        window.evg.util = evg.util || {};
        window.evg.util.fn = evg.util.fn;
    }
    return evg.util.fn;
});