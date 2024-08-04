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
    evg.util.grid = evg.util.grid || function() { // 定義物件名稱, 名稱fn可自定為其他單字
        // your code
    };
    
    var startsWith = function(targetString, searchString) {
        return targetString.substr(0, searchString.length) === searchString;
    };
    
    var getTableGroup = function(table_id){
        if(Array.isArray(table_id)) {
            var selector = '';
            table_id.forEach(function(currentValue,index,arr){
                selector += (selector.length===0?'':', ') + '#' + currentValue;
            });
            return $(selector);
        }
        else {
            return $('#'+table_id);
        }
    };
    
    var getAtrowArr = function(table_id) {
        var atrow_arr = [];
        if(Array.isArray(table_id)) {
            var trArr = $('#'+table_id[0] + ' > tbody > tr').not('[data-evg-atrow=add]');
        }
        else {
            var trArr = $('#'+table_id + ' > tbody > tr').not('[data-evg-atrow=add]');
        }
        for(var i=0;i<trArr.length;i++) {
            var atrow = trArr.eq(i).attr('data-evg-atrow');
            if(atrow_arr.indexOf(atrow)===-1) {
                atrow_arr.push(atrow)
            }
        }
        return atrow_arr;
    };
    
    var getAtrowAddArr = function(table_id) {
        var grid_id = evg.util.grid.getGridId(table_id);
        var atrow_arr_all = getAtrowArr(table_id);
        var atrow_arr = [];
        for(var i=0;i<atrow_arr_all.length;i++) {
            var atrow = atrow_arr_all[i];
            if(atrow!=null && startsWith(atrow, grid_id + '_add_')){
                atrow_arr.push(atrow); 
            }
        }
        return atrow_arr;
    };
    
    var getAtrowNowArr = function(table_id) {
        var grid_id = evg.util.grid.getGridId(table_id);
        var atrow_arr_all = getAtrowArr(table_id);
        var atrow_arr = [];
        for(var i=0;i<atrow_arr_all.length;i++) {
            var atrow = atrow_arr_all[i];
            if(atrow!=null && startsWith(atrow, grid_id + '_now_')){
                atrow_arr.push(atrow); 
            }
        }
        return atrow_arr;
    };
    
    var getDataChkArr = function(type, table_id, chk_name) {
        var $table_group = getTableGroup(table_id);
        var atrow_all_arr = getAtrowArr(table_id);
        var trGroupArr = atrowArrToTrArr($table_group, atrow_all_arr);
        
        var atrow_arr = [];
        var tr_arr = [];
        for (var trIdx = 0; trIdx < trGroupArr.length; trIdx++) {
            var atrow = atrow_all_arr[trIdx];
            var trGroup = trGroupArr[trIdx];       
            
            var chk = trGroup.find("input[name="+chk_name+"]");
            if(chk.length===0) {
                continue;
            }
            if(!chk.prop("checked")) {
                continue;
            }
            
            atrow_arr.push(atrow);
            tr_arr.push(trGroup);
        }
        
        if(type==='A') {
            return atrow_arr;            
        }
        else if(type==='T') {
            return tr_arr;            
        }
    };
    
    var getDataUpdatedArr = function(type, table_id, byPassArr) {
        var $table_group = getTableGroup(table_id);
        var atrow_all_arr = getAtrowArr(table_id);
        var trGroupArr = atrowArrToTrArr($table_group, atrow_all_arr);
        
        var atrow_arr = [];
        var tr_arr = [];
        for (var trIdx = 0; trIdx < trGroupArr.length; trIdx++) {
            var atrow = atrow_all_arr[trIdx];
            var trGroup = trGroupArr[trIdx];            
            if(evg.util.form.isChanged(trGroup, byPassArr)) {
                atrow_arr.push(atrow);
                tr_arr.push(trGroup);
            }
        }

        if(type==='A') {
            return atrow_arr;            
        }
        else if(type==='T') {
            return tr_arr;            
        }
    };
    
    var getDataFilterArr = function(type, table_id, option) {
        var grid_id = evg.util.grid.getGridId(table_id);
        var $table_group = getTableGroup(table_id);
        var atrow_all_arr = getAtrowArr(table_id);
        var trGroupArr = atrowArrToTrArr($table_group, atrow_all_arr);
        
        var atrow_arr = [];
        var tr_arr = [];
        for (var trIdx = 0; trIdx < trGroupArr.length; trIdx++) {
            var atrow = atrow_all_arr[trIdx];
            var trGroup = trGroupArr[trIdx];          
            
            if(option.recordType === 'add' && !startsWith(atrow, grid_id + '_add_')) {
                continue;
            }
            if(option.recordType === 'now' && !startsWith(atrow, grid_id + '_now_')) {
                continue;
            }
            
            if(option.update === 'Y') {
                if(!evg.util.form.isChanged(trGroup, option['byPassArr'])) {
                    continue;
                }
            }
            if(option.update === 'N') {//20161222 Brad：只取得沒有更新的tr
                if(evg.util.form.isChanged(trGroup, option['byPassArr'])) {
                    continue;
                }
            }

            if(option.check !== '' && option.check !== undefined && option.check !== null) {
                var chk = trGroup.find("input[name="+option.check+"]");
                if(chk.length===0) {
                    continue;
                }
                if(!chk.prop("checked")) {
                    continue;
                }
            }

            atrow_arr.push(atrow);
            tr_arr.push(trGroup);
        }

        if(type==='A') {
            return atrow_arr;            
        }
        else if(type==='T') {
            return tr_arr;            
        }
    };
    
    var changeColor = function(table_id){
        var $table_group = getTableGroup(table_id);
        if(!$table_group[0].hasAttribute('data-evg-grid-chgcolor')) {
            return false;
        }
        else {
            return true;
        }
    }
    
    var atrowArrToTrArr = function($table_group, atrow_arr) {
        var resultTmp = {};
        var trAll = $table_group.find('[data-evg-atrow]');
        for (var trIdx = 0; trIdx < trAll.length; trIdx++) {
            var tr = trAll[trIdx];
            var atrow = tr.getAttribute('data-evg-atrow');
            if (atrow_arr.indexOf(atrow) === -1) {
                continue;
            }
            if(!resultTmp.hasOwnProperty(atrow)) {
                resultTmp[atrow] = [tr];
            }
            else {
                resultTmp[atrow].push(tr);    
            }
        }
        
        var result = [];
        for (var atrow_idx = 0; atrow_idx < atrow_arr.length; atrow_idx++) {
            var atrow = atrow_arr[atrow_idx];
            result.push($(resultTmp[atrow]));
        }
        
        return result;
    };
    
    var table_id_key_obj = {};
         
    evg.util.grid.prototype = { // 定義public屬性與方法
        /**
         * 初始化此Grid
         * @param table_id - 作為Grid的Table id
         * @param rowPerGroup - 一筆資料有幾行
         * @example evg.util.grid.init('table_result', 1);
         */
        init: function(table_id, rowPerGroup, option)
        {
            var changeColor;
            if(option!=null) {
                changeColor = option.changeColor;
            }
            
            var notCountNow;
            if(option!=null) {
                notCountNow = option.changeColor;
            }
            
            if(rowPerGroup == undefined) {
                if(Array.isArray(table_id)) {
                    rowPerGroup = [];
                    for (var table_idx = 0; table_idx < table_id.length; table_idx++) {
                        rowPerGroup[table_idx] = 1;
                    }
                }
                else {
                    rowPerGroup = 1;
                }
            }
            
            
            var $table_group = getTableGroup(table_id);
            $table_group.attr('data-evg-grid-chgcolor','');
            var key = JSON.stringify(table_id);
            var grid_id;
            if(table_id_key_obj[key] === undefined) {
                table_id_key_obj[key] = 'g' + Object.keys(table_id_key_obj).length;
            }
            grid_id = table_id_key_obj[key];
            
            for (var table_idx = 0; table_idx < $table_group.length; table_idx++) {
                var $table = $table_group.eq(table_idx);
                var tr_arr = $table.children("tbody").children("tr[data-evg-atrow!='add']").get();//因為可能整個Table都暫時看不到，所以不能用visible 20170418:Brad：因為可能是巢狀Table，所以要確定只往下找一層
                var tr_group_idx = 1;
                if(Array.isArray(table_id)) {
                    var rowPerGroup_Table = rowPerGroup[table_idx];    
                }
                else {
                    var rowPerGroup_Table = rowPerGroup;
                }
                
                $table_group.attr('data-evg-grid-rowpergroup',rowPerGroup_Table);
                for (var tr_idx = 1; tr_idx <= tr_arr.length; tr_idx++) {
                     
                    var tr = tr_arr[tr_idx-1];
                    if(notCountNow!=null && notCountNow==='Y') {
                        tr.setAttribute('data-evg-atrow',grid_id + "_now_" + tr.getAttribute('data-evg-atrow'));    
                    }
                    else {
                        tr.setAttribute('data-evg-atrow',grid_id + "_now_" + tr_group_idx);
                    }
                    
                    if(tr_idx%rowPerGroup_Table===0)
                    {
                        tr_group_idx++;
                    }
                }
            }
            if(evg.util.grid.getRowNumPerGroup(table_id) > 1) {//20170714只有一筆資料大於一行，才不能使用CSS進行自動標顏色
	            evg.util.grid.markColor(table_id);
            }
            
            if(changeColor) {
                $table_group.find('tr').hover(function(){
                    $table_group.find('tr').removeClass('list-td-hover');
                    var atrow = this.getAttribute('data-evg-atrow');
                    $table_group.find('tr[data-evg-atrow="'+ atrow + '"]').addClass('list-td-hover');
                });   
            }
        },
        /**
         * 取得此Grid的編號
         * @param table_id - 作為Grid的Table id
         * @returns gridId
         * @example var grid = evg.util.grid.getGridId('result_table');
         */
        getGridId: function(table_id) {
            var key = JSON.stringify(table_id);
            return table_id_key_obj[key];
        },
        /**
         * 傳入DOM物件，或Jquery物件，回傳其所在的TrGroup
         * @param obj：一個HTMLElement或JQuery物件
         * @returns：tr group
         * @example var grid = evg.util.grid.getTrGroup(document.getElementById('account'));
         */
        getTrGroup: function(obj)
        {
            if(!(obj instanceof jQuery))
            {
                obj = $(obj);
            }
            var tr = obj.closest('tr');
            var atrow = tr.attr('data-evg-atrow');
            return $("[data-evg-atrow='"+atrow+"']");
        },
        /**
         * 傳入DOM物件，或Jquery物件，回傳其所在的TrGroup的序號
         * @param obj：一個HTMLElement或JQuery物件
         * @returns：{area:'now',idx:number}
         * @example var position = evg.util.grid.getTrGroupPosition(document.getElementById('account'));
         */
        getTrGroupPosition: function(obj)
        {
            if(!(obj instanceof jQuery))
            {
                obj = $(obj);
            }
            var tr = obj.closest('tr');
            var atrow = tr.attr('data-evg-atrow');
            if(atrow==='add') {
                return {area:'add', idx:-1};
            }
            
            var atrow_info = atrow.split('_');
            
            return {area:atrow_info[atrow_info.length-2], idx:parseInt(atrow_info[atrow_info.length-1])-1};
        },
        /**
         * 複製一筆Add的區塊
         * @param table_id - 作為Grid的Table id
         * @example evg.util.grid.cloneAddOne('result_table');
         */
        cloneAddOne: function(table_id, template_id, template_input)
        {
            var grid_id = evg.util.grid.getGridId(table_id);
            var addTrNum = evg.util.grid.getTrAddNum(table_id) + 1;
            var new_tr;
            if(Array.isArray(table_id)) {
                new_tr = [];
                
                for (var table_id_idx = 0; table_id_idx < table_id.length; table_id_idx++) {
                    if(template_id===undefined) {//20170209 Brad:提供傳入Template以產生new tr之功能
                        new_tr[table_id_idx] = $('#'+table_id[table_id_idx]+' [data-evg-atrow=add]').clone(true, true);
                        new_tr[table_id_idx].css('display','');    
                    }
                    else {
                        var template = $('#'+template_id[table_id_idx]).html();
                        if(template_input==null) {
                            template_input = {i:addTrNum};
                        }
                        else {
                            template_input.i = addTrNum;    
                        }
                        
                        if (typeof Mustache != 'undefined') {
                            Mustache.parse(template);
                            var rendered = Mustache.render(template, template_input);
                            new_tr[table_id_idx] = $(rendered);    
                        }
                        else if (typeof Handlebars != 'undefined') {
                            var template_obj = Handlebars.compile(template);
                            var rendered = template_obj(template_input);
                            new_tr[table_id_idx] = $(rendered);
                        }
                    }
                    
                    new_tr[table_id_idx].attr('data-evg-atrow',grid_id + "_add_" + addTrNum);
                }  
            }
            else {
                if(template_id===undefined) {//20170209 Brad:提供傳入Template以產生new tr之功能
                    new_tr = $('#'+table_id+' [data-evg-atrow=add]').clone(true, true);
                    new_tr.css('display','');    
                }
                else {
                    var template = $('#'+template_id).html();
                    if(template_input==null) {
                        template_input = {i:addTrNum};
                    }
                    else {
                        template_input.i = addTrNum;    
                    }
                    
                    if (typeof Mustache != 'undefined') {
                        Mustache.parse(template);
                        var rendered = Mustache.render(template, template_input);
                        new_tr = $(rendered);
                    }
                    else if (typeof Handlebars != 'undefined') {
                        var template_obj = Handlebars.compile(template);
                        var rendered = template_obj(template_input);
                        new_tr = $(rendered);
                    }
                    
                }
                
                new_tr.attr('data-evg-atrow',grid_id + "_add_" + addTrNum);
            }
            return new_tr; 
        },
        /**
         * 新增一筆Add的區塊
         * @param table_id - 作為Grid的Table id
         * @example evg.util.grid.addOne('result_table');
         */
        addOne: function(table_id, template_id, template_input)
        {
            var new_tr = evg.util.grid.cloneAddOne(table_id, template_id, template_input);
            if(!Array.isArray(table_id)) {
                {//20170218 Brad:其實這段邏輯應該要放在cloneAddOne，但是也許有人只想複製，而不想轉換，因此只在這邊進行
                    evg.util.form.formatDate(new_tr);
                    if(evg.util.money!==undefined) {
                        evg.util.money.formatMoney(new_tr);   
                    }                
                    evg.util.fn.hideNoActionElement(new_tr);
                }
                if(template_id===undefined) {//20170209 Brad:提供傳入Template以產生new tr之功能
                    $('#'+table_id+' [data-evg-atrow=add]').after(new_tr);   
                }
                else {
                    $('#'+table_id).prepend(new_tr);
                }                
            }
            else {
                for (var table_id_idx = 0; table_id_idx < table_id.length; table_id_idx++) {
                    var table_id_ele = table_id[table_id_idx];
                    var new_tr_ele = new_tr[table_id_idx];
                    {//20170218 Brad:其實這段邏輯應該要放在cloneAddOne，但是也許有人只想複製，而不想轉換，因此只在這邊進行
                        evg.util.form.formatDate(new_tr_ele);
                        if(evg.util.money!==undefined) {
                            evg.util.money.formatMoney(new_tr_ele);   
                        }                
                        evg.util.fn.hideNoActionElement(new_tr_ele);
                    }
                    if(template_id===undefined) {//20170209 Brad:提供傳入Template以產生new tr之功能
                        $('#'+table_id_ele+' [data-evg-atrow=add]').after(new_tr_ele);    
                    }
                    else {
                        $('#'+table_id_ele).prepend(new_tr_ele);
                    }
                }
            }
            if(evg.util.grid.getRowNumPerGroup(table_id) > 1) {//20170714只有一筆資料大於一行，才不能使用CSS進行自動標顏色
                evg.util.grid.markColor(table_id);
            }
            
            return new_tr;
        },
        /**
         * 在最後面新增一筆Add的區塊
         * @param table_id - 作為Grid的Table id
         * @example evg.util.grid.addOneAfter('result_table');
         */
        addOneAfter: function(table_id, template_id, template_input)
        {
            var new_tr = evg.util.grid.cloneAddOne(table_id, template_id, template_input);
            if(!Array.isArray(table_id)) {
                {//20170218 Brad:其實這段邏輯應該要放在cloneAddOne，但是也許有人只想複製，而不想轉換，因此只在這邊進行
                    evg.util.form.formatDate(new_tr);
                    if(evg.util.money!==undefined) {
                        evg.util.money.formatMoney(new_tr);   
                    }                
                    evg.util.fn.hideNoActionElement(new_tr);
                }
                $('#'+table_id).append(new_tr);    
            }
            else {
                for (var table_id_idx = 0; table_id_idx < table_id.length; table_id_idx++) {
                    var table_id_ele = table_id[table_id_idx];
                    var new_tr_ele = new_tr[table_id_idx];
                    {//20170218 Brad:其實這段邏輯應該要放在cloneAddOne，但是也許有人只想複製，而不想轉換，因此只在這邊進行
                        evg.util.form.formatDate(new_tr_ele);
                        if(evg.util.money!==undefined) {
                            evg.util.money.formatMoney(new_tr_ele);   
                        }                
                        evg.util.fn.hideNoActionElement(new_tr_ele);
                    }
                    $('#'+table_id_ele).append(new_tr_ele); 
                }
            }
            if(evg.util.grid.getRowNumPerGroup(table_id) > 1) {//20170714只有一筆資料大於一行，才不能使用CSS進行自動標顏色
                evg.util.grid.markColor(table_id);
            }
            
            return new_tr;
        },
        /**
         * 新增一筆Add的區塊
         * @param table_id - 作為Grid的Table id
         * @example evg.util.grid.addOne('result_table');
         */
        copyMuti: function(table_id, template_id, chk_name, copy_name_arr, copy_function)
        {
            if(evg.util.grid.getTrChkNum(table_id, chk_name) === 0) {
                alert('You need select record to copy!');
                return;
            }

            var chk_tr_arr = evg.util.grid.getTrChkArr(table_id, chk_name);
            var result = [];
            for(var chk_idx = 0; chk_idx<chk_tr_arr.length;chk_idx++) {
                var new_tr = evg.util.grid.addOne(table_id, template_id);    //新增
                if(!Array.isArray(table_id)) {
                    var chk_tr = chk_tr_arr[chk_idx];//取得選擇
                    
                    if(copy_function!=null) {
                        copy_function.call(null, chk_tr, new_tr, copy_name_arr);
                    }
                    else {
                        evg.util.form.copyValue(chk_tr, new_tr, copy_name_arr);
                    }    
                }
                else {
                    for (var table_id_idx = 0; table_id_idx < table_id.length; table_id_idx++) {
                        var table_id_ele = table_id[table_id_idx];
                        var new_tr_ele = new_tr[table_id_idx];
                     
                        var chk_tr_ele = chk_tr_arr[chk_idx][table_id_idx];//取得選擇
                        
                        if(copy_function!=null) {
                            copy_function.call(null, $(chk_tr_ele), new_tr_ele, copy_name_arr);
                        }
                        else {
                            evg.util.form.copyValue($(chk_tr_ele), new_tr_ele, copy_name_arr);
                        }
                    }
                }
                
                result.push(new_tr);
            }
            return result;
        },
        /**
         * 移除這筆Add的區塊，由按鈕事件觸發
         * $("#result_table").find('.btn_delete_row').click(evg.util.grid.delOne);
         */
        delOne: function()
        {
            var trGroup = evg.util.grid.getTrGroup(this);        
            trGroup.remove();
            if(evg.util.grid.getRowNumPerGroup(table_id) > 1) {//20170714只有一筆資料大於一行，才不能使用CSS進行自動標顏色
                evg.util.grid.markColor(table_id);
            }
        },
        /**
         * 
         * @param table_id
         * @param trSet
         * @param recordType
         * @param dir
         * @returns
         */
        initTr: function(table_id, trSet, recordType, dir)
        {
            var grid_id = evg.util.grid.getGridId(table_id);
            var $table_group = getTableGroup(table_id);
            var rowPerGroup = evg.util.grid.getRowPerGroup(table_id);
            
            for (var table_idx = 0; table_idx < $table_group.length; table_idx++) {
                var $table = $table_group.eq(table_idx);
                var tr_arr;
                if(trSet == null) {
                    tr_arr = $table.children("tbody").children("tr:not([data-evg-atrow])").get();//因為可能整個Table都暫時看不到，所以不能用visible 20170418:Brad：因為可能是巢狀Table，所以要確定只往下找一層
                }
                else {
                    tr_arr = trSet[table_idx];
                }
                
                var rowPerGroup_Table;
                if(Array.isArray(table_id)) {
                    rowPerGroup_Table = rowPerGroup[table_idx];    
                }
                else {
                    rowPerGroup_Table = rowPerGroup;
                }
                
                var tr_group_idx;
                if(dir==='asc') {
                    if(recordType === 'add') {
                        tr_group_idx = evg.util.grid.getTrAddNum(table_id) + 1;
                    }
                    else {
                        tr_group_idx = evg.util.grid.getTrNowNum(table_id) + 1;
                    }    
                }
                else {
                    if(recordType === 'add') {
                        tr_group_idx = evg.util.grid.getTrAddNum(table_id) + tr_arr.length / rowPerGroup_Table;
                    }
                    else {
                        tr_group_idx = evg.util.grid.getTrNowNum(table_id) + tr_arr.length / rowPerGroup_Table;
                    }
                }
                
                $table_group.attr('data-evg-grid-rowpergroup',rowPerGroup_Table);
                for (var tr_idx = 1; tr_idx <= tr_arr.length; tr_idx++) {
                     
                    var tr = tr_arr[tr_idx-1];
                    tr.setAttribute('data-evg-atrow',grid_id + "_" + recordType + "_" + tr_group_idx);
                    
                    if(tr_idx%rowPerGroup_Table===0)
                    {
                        if(dir==='asc') {
                            tr_group_idx++;    
                        }
                        else {
                            tr_group_idx--;
                        }
                        
                    }
                }
            }
            
            if(evg.util.grid.getRowNumPerGroup(table_id) > 1) {//20170714只有一筆資料大於一行，才不能使用CSS進行自動標顏色
                evg.util.grid.markColor(table_id);
            }
        },
        /**
         * 取得每筆資料有幾行
         * @param table_id - 作為Grid的Table id
         * @returns {Number} 每筆資料有幾行
         * @example var rowPerGroup = evg.util.grid.getRowPerGroup('result_table');
         */
        getRowPerGroup: function(table_id)
        {
            if(Array.isArray(table_id)) {
                var rowPerGroup = [];
                for (var table_id_idx = 0; table_id_idx < table_id.length; table_id_idx++) {
                    rowPerGroup[table_id_idx] = parseInt($('#'+table_id[table_id_idx]).attr('data-evg-grid-rowpergroup'));   
                }
                return rowPerGroup;
            }
            else {
                var rowPerGroup = $('#'+table_id).attr('data-evg-grid-rowpergroup');
                return parseInt(rowPerGroup);
            }
        },
        /**
         * 取得每筆資料有幾行
         * @param table_id - 作為Grid的Table id
         * @returns {Number} 每筆資料有幾行
         * @example var rowPerGroup = evg.util.grid.getRowPerGroup('result_table');
         */
        getRowNumPerGroup: function(table_id)
        {
            if(Array.isArray(table_id)) {
                var rowPerGroup = 0;
                for (var table_id_idx = 0; table_id_idx < table_id.length; table_id_idx++) {
                    rowPerGroup += parseInt($('#'+table_id[table_id_idx]).attr('data-evg-grid-rowpergroup'));   
                }
                return rowPerGroup;
            }
            else {
                var rowPerGroup = $('#'+table_id).attr('data-evg-grid-rowpergroup');
                return parseInt(rowPerGroup);
            }
        },
        /**
         * 取得有幾筆Add資料
         * @param table_id - 作為Grid的Table id
         * @returns {Number} 有幾筆Add資料
         * @example var trAddNum = evg.util.grid.getTrAddNum('result_table');
         */
        getTrAddNum: function(table_id)
        {
            return getAtrowAddArr(table_id).length;
        },
        /**
         * 取得Add資料tr group陣列
         * @param table_id - 作為Grid的Table id
         * @returns {Array} Add資料tr group陣列
         * @example var trAddArr = evg.util.grid.getTrAddArr('result_table');
         */
        getTrAddArr: function(table_id)
        {
            var $table_group = getTableGroup(table_id);
            var atrow_arr = getAtrowAddArr(table_id);
            
            var result = atrowArrToTrArr($table_group, atrow_arr);
            
            return result;
        },
        /**
         * 取得有幾筆現存資料
         * @param table_id - 作為Grid的Table id
         * @returns {Number} 有幾筆現存資料
         * @example var trNowNum = getTrNowNum('result_table');
         */
        getTrNowNum: function(table_id)
        {
            return getAtrowNowArr(table_id).length;
        },
        /**
         * 取得現存資料tr group陣列
         * @param table_id - 作為Grid的Table id
         * @returns {Number} 現存資料tr group陣列
         * @example var trNowArr = evg.util.grid.getTrNowArr('result_table');
         */
        getTrNowArr: function(table_id)
        {
            var $table_group = getTableGroup(table_id);
            var atrow_arr = getAtrowNowArr(table_id);
            
            var result = atrowArrToTrArr($table_group, atrow_arr);
            
            return result;
        },
        /**
         * 取得Add+現存總共有幾資料
         * @param table_id - 作為Grid的Table id
         * @returns {Number} Add+現存總共有幾資料
         * @example var trNum = evg.util.grid.getTrNum('result_table');
         */
        getTrNum: function(table_id)
        {
            return getAtrowArr(table_id).length;
        },
        /**
         * 取得Add+現存的所有資料tr group陣列
         * @param table_id - 作為Grid的Table id
         * @returns {Array} Add+現存的所有資料tr group陣列
         * @example var trArr = evg.util.grid.getTrArr('result_table');
         */
        getTrArr: function(table_id)
        {
            var $table_group = getTableGroup(table_id);
            var atrow_arr = getAtrowArr(table_id);
            
            var result = atrowArrToTrArr($table_group, atrow_arr);
            
            return result;
        },
        /**
         * 取得勾選幾筆資料
         * @param table_id - 作為Grid的Table id
         * @returns {Number} 勾選幾筆資料
         * @example var trChkNum = evg.util.grid.getTrChkNum('result_table');
         */
        getTrChkNum: function(table_id, chk_name)
        {
            return getDataChkArr('A', table_id, chk_name).length;
        },
        /**
         * 取得勾選tr group陣列
         * @param table_id - 作為Grid的Table id
         * @returns {Array} 勾選tr group陣列
         * @example var trChkArr = evg.util.grid.getTrChkArr('result_table');
         */
        getTrChkArr: function(table_id, chk_name)
        {
            return getDataChkArr('T', table_id, chk_name);
        },
        /**
         * 取得更新幾筆資料
         * @param table_id - 作為Grid的Table id
         * @param byPassArr - 就算有更新也不管的name清單
         * @returns {Number} 取得更新幾筆資料
         * @example var trUpdatedNum = evg.util.grid.getTrUpdatedNum('result_table');
         */
        getTrUpdatedNum: function(table_id, byPassArr)
        {
            return getDataUpdatedArr('A', table_id, byPassArr).length;
        },
        /**
         * 取得更新tr group陣列
         * @param table_id - 作為Grid的Table id
         * @param byPassArr - 就算有更新也不管的name清單
         * @returns {Array} 有更新的tr group陣列
         * @example var trUpdatedArr = evg.util.grid.getTrUpdatedArr('result_table');
         */
        getTrUpdatedArr: function(table_id, byPassArr)
        {
            return getDataUpdatedArr('T', table_id, byPassArr);
        },
        /**
         * 取得篩選出幾筆資料
         * @param table取得篩選後的tr group陣列_id - 作為Grid的Table id
         * @param option.recordType - 'add'代表只抓add資料,'now'代表只抓現有資料，沒有傳代表不考慮此條件
         * @param option.update - 'Y'代表只抓有更新的tr group， 'N'代表只抓有更新的tr group，沒有傳代表不考慮此條件
         * @param check - 代表需勾選的check box的name，沒有傳代表不考慮此條件
         * @returns {Number} 篩選出幾筆資料
         * @example var trFilterNum = evg.util.grid.getTrFilterNum('result_table',{recordType:'now',updated:'Y'});
         */
        getTrFilterNum: function(table_id, option)
        {
            return getDataFilterArr('A', table_id, option).length;
        },
        /**
         * 取得篩選後的tr group陣列
         * @param table取得篩選後的tr group陣列_id - 作為Grid的Table id
         * @param option.recordType - 'add'代表只抓add資料,'now'代表只抓現有資料，沒有傳代表不考慮此條件
         * @param option.update - 'Y'代表只抓有更新的tr group， 'N'代表只抓有更新的tr group，沒有傳代表不考慮此條件
         * @param check - 代表需勾選的check box的name，沒有傳代表不考慮此條件
         * @returns {Array} 取得篩選後的tr group陣列
         * @example var trGroupUpdate = evg.util.grid.getTrFilterArr('result_table',{recordType:'now',updated:'Y'});
         */
        getTrFilterArr: function(table_id, option)
        {
            return getDataFilterArr('T', table_id, option);
        },
        /**
         * 執行全選動作
         * @param obj
         * @example onClick="evg.util.grid.allChk(this)"
         * @example $('#result_table thead').find('[name="chk"]').click(evg.util.grid.chkChk);
         */
        allChk: function(obj)
        {
            if(arguments.length === 0) {
                var cur_check_box = $(this);
            }
            else {
                var cur_check_box = $(obj);
            }
            var chk_name = obj.name;
            var table = cur_check_box.closest('table');
            var table_id = table.attr('id');
            
            var check_box_set = $("#"+table_id+" > tbody > tr input[name="+chk_name+"]");
            check_box_set.prop("checked",cur_check_box.prop("checked"));
        },
        /**
         * 選擇某一筆之後呼叫，如果全部勾選，則會全選check box也會勾選
         * @param obj - HTMLElement，如果不傳則使用this
         * @example onClick="evg.util.grid.chkChk(this)"
         * @example $('#result_table tbody').find('[name="chk"]').click(evg.util.grid.chkChk);
         */
        chkChk: function(obj)
        {
            if(arguments.length === 0) {
                var cur_check_box = $(this);
            }
            else {
                var cur_check_box = $(obj);
            }
            var chk_name = obj.name;
            var table = cur_check_box.closest('table');
            var table_id = table.attr('id');
            
            var check_box_head = $("#"+table_id+" > thead > tr input[name="+chk_name+"]");
            var result = true;
            var check_box_set = $("#"+table_id+" > tbody > tr input[name="+chk_name+"]");
            check_box_set.each(function(index){
                if(!$(this).prop("checked"))
                {
                    result = false;
                    return;
                }
            });
            check_box_head.prop("checked",result);
        },
        /**
         * 每個tr group都呼叫一次evg.util.form.serializeArray，最後組成陣列
         * @param trGroup - 目標的tr group
         * @param addChange - 與 evg.util.form.serializeArray的addChange同義
         * @param keyArr - 與 evg.util.form.serializeArray的keyArr同義
         * @returns JS陣列，內為name與value的組合的物件
         * @example var tr_group = evg.util.grid.getTrNowArr('table_result');
         * var serialObj = evg.util.grid.serializeArray(tr_group);
         */
        serializeArray: function(trGroup, addChange, keyArr){
            var result = [];
            trGroup.forEach(function(element, index, array){
                result.push(evg.util.form.serializeArray(element, addChange, keyArr));
            });
            return result;
        },
        /**
         * 呼叫evg.util.grid.serializeArray，再呼叫JSON.stringify
         * @param trGroup - 目標的tr group
         * @param addChange - 與 evg.util.form.serializeArray的addChange同義
         * @param keyArr - 與 evg.util.form.serializeArray的keyArr同義
         * @returns JS陣列的JSON字串，內為name與value的組合的物件
         * @example var tr_group = evg.util.grid.getTrNowArr('table_result');
         * var jsonStr = evg.util.grid.toJSONString(tr_group);
         */
        toJSONString: function(trGroup, addChange, keyArr){
            return JSON.stringify(evg.util.grid.serializeArray(trGroup, addChange, keyArr));
        },
        /**
         * 每個tr group都呼叫一次evg.util.form.toObject，最後組成陣列
         * @param trGroup - 目標的tr group
         * @param type - 與 evg.util.form.toObject的type同義
         * @param keyArr - 與 evg.util.form.toObject的keyArr同義
         * @returns JS陣列，內為name與value的組合的物件
         * @example var tr_group = evg.util.grid.getTrNowArr('table_result');
         * var serialObj = evg.util.grid.toObject(tr_group);
         */
        toObject: function(trGroup, type, keyArr){
            var result = [];
            trGroup.forEach(function(element, index, array){
                result.push(evg.util.form.toObject(element, type, keyArr));
            });
            return result;
        },
        /**
         * 呼叫evg.util.grid.toObject，再呼叫JSON.stringify
         * @param trGroup - 目標的tr group
         * @param type - 與 evg.util.form.toObject的type同義
         * @param keyArr - 與 evg.util.form.toString的keyArr同義
         * @returns JS陣列的JSON字串，內為name與value的組合的物件
         * @example var tr_group = evg.util.grid.getTrNowArr('table_result');
         * var jsonStr = evg.util.grid.toString(tr_group);
         */
        toString: function(trGroup, type, keyArr){
            return JSON.stringify(evg.util.grid.toObject(trGroup, type, keyArr));
        },
        /**
         * 每個tr group都呼叫一次evg.util.form.toObjectOri，最後組成陣列
         * @param trGroup - 目標的tr group
         * @param type - 與 evg.util.form.toObject的type同義
         * @param keyArr - 與 evg.util.form.toObject的keyArr同義
         * @returns JS陣列，內為name與value的組合的物件
         * @example var tr_group = evg.util.grid.getTrNowArr('table_result');
         * var serialObj = evg.util.grid.toObjectOri(tr_group);
         */
        toObjectOri: function(trGroup, type, keyArr){
            var result = [];
            trGroup.forEach(function(element, index, array){
                result.push(evg.util.form.toObjectOri(element, type, keyArr));
            });
            return result;
        },
        /**
         * 呼叫evg.util.grid.toObjectOri，再呼叫JSON.stringify
         * @param trGroup - 目標的tr group
         * @param type - 與 evg.util.form.toObject的type同義
         * @param keyArr - 與 evg.util.form.toString的keyArr同義
         * @returns JS陣列的JSON字串，內為name與value的組合的物件
         * @example var tr_group = evg.util.grid.getTrNowArr('table_result');
         * var jsonStr = evg.util.grid.toStringOri(tr_group);
         */
        toStringOri: function(trGroup, type, keyArr){
            return JSON.stringify(evg.util.grid.toObjectOri(trGroup, type, keyArr));
        },
        /**
         * 手動針對各tr group進行背景變色
         * @param table_id - 作為Grid的Table id
         * @example evg.util.grid.markColor('table_result');
         */
        markColor: function(table_id) {
            var trArr = evg.util.grid.getTrArr(table_id);
            for (var trIdx = 0; trIdx < trArr.length; trIdx++) {
                trArr[trIdx].removeClass("list-td list-td-even");
                if(trIdx%2===0) {
                    trArr[trIdx].addClass("list-td-even");
                }
                else {
                    trArr[trIdx].addClass("list-td");
                }
            }
        }
    };
     
    // Self-Executing Anonymous Function 
    (function() {
        // your code
    })();
     
    evg.util.grid = new evg.util.grid(); // 產生evg.util.grid物件
     
    // 將evg, evg.util, evg.util.grid暴露為全域變數
    if ( typeof noGlobal === strundefined ) {
        window.evg = evg || {};
        window.evg.util = evg.util || {};
        window.evg.util.grid = evg.util.grid;
    }
    return evg.util.grid;
});