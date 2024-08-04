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
    evg.util.form = evg.util.form || function() { // 定義物件名稱, 名稱fn可自定為其他單字
        // your code
    };
    
    var getSaveRecordCount = function(obj) {
        var cnt = 0;
        //if(Number.isInteger(obj)) {
        if(parseInt(obj) == obj) {
            cnt = obj;
        }
        else {
            Object.keys(obj).forEach(function(currentName, index, arr){
                cnt += obj[currentName];
            });    
        }
        return cnt;
    }
    
    var showSuccessAct = function(cnt, verb) {
        if(cnt > 1) {            
            evg.util.fn.showSuccess(evg.util.fn.translateOpt(verb +' ' + cnt + ' records successfully.', 'COMMON_006_UPDATE_RECORDS_SUCCESS', verb, cnt));
        }
        else if(cnt === 1) {
            evg.util.fn.showSuccess(evg.util.fn.translateOpt(verb +' ' + cnt + ' record successfully.', 'COMMON_007_UPDATE_RECORD_SUCCESS', verb, cnt)); 
        }
        else {
            evg.util.fn.showWarning(evg.util.fn.translateOpt(verb + ' no record.', 'I008_UPDATE_NO_RECORD', verb));
        }
    }
    
    evg.util.form.prototype = { // 定義public屬性與方法
        /**
    	 * 儲存原始value
    	 * @param target - 含有許多input元素的上層元素，為JQuery物件，通常為div或table或tr
         * @example evg.util.form.setOriValue($('#table-target'))
    	 */
    	setOriValue: function(target)
    	{
    		target.find(':input').not('button, [type=button]').each(function(idx){
    		    var obj = $(this);
    		    if(obj.attr('type')==='checkbox' || obj.attr('type')==='radio') {
    		        obj.attr('data-evg-orivalue',obj.prop('checked'));
    			}
    		    else {
    		        obj.attr('data-evg-orivalue',obj.val());
    		    }
    		});
    	},
    	/**
    	 * 將Target的Value，以data-evg-orivalue還原
    	 * @param target - 含有許多input元素的上層元素，為JQuery物件，通常為div或table或tr
    	 * @example evg.util.form.resetToOriValue($('#table-target'))
    	 */
    	resetToOriValue: function(target) {
            target.find(':input').not('button, [type=button]').each(function(idx){
                var obj = $(this);
                if(obj.attr('data-evg-orivalue')!==undefined) {
                    if(obj.attr('type')==='checkbox' || obj.attr('type')==='radio') {
                        if(obj.attr('data-evg-orivalue')==='true') {
                            obj.prop('checked',true);
                        }   
                        else {
                            obj.prop('checked',false);
                        }
                    }
                    else if(this.tagName==='SELECT' && this.hasAttribute('multiple')) {
                        this.selectedIndex = -1;
                        $.each(obj.attr('data-evg-orivalue').split(","), function(i,e){
                            obj.find("option[value='" + e + "']").prop("selected", true);
                        });
                    }
                    else {
                        obj.val(obj.attr('data-evg-orivalue'));
                    }
                }
            });
    	},
    	/**
    	 * 綁定事件，將針對更新的欄位變更顏色
    	 * @param target - 含有許多input元素的上層元素，為JQuery物件，通常為div或table或tr
    	 * @example evg.util.form.markChangeBind($('#target_table').find(':input').not('[name="chk"]'))
    	 */
        markChangeBind: function(target) {
            target.change(evg.util.form.markChangeHandle);
        },
        /**
         * 處理針對更新的欄位變更顏色的被觸發函數，供手動綁定事件使用，一般不需要使用
         * @example $('#target_table').find(':input').change(evg.util.form.markChangeHandle);
         */
        markChangeHandle: function(){
            var ele = $(this);
            var ori_value = ele.attr('data-evg-orivalue');
            var prev = ele.prev();
            var in_dirty = false;
            if(prev.length!==0 && prev.get(0).tagName==='SPAN' && prev.hasClass('mark-dirty')) {
                in_dirty = true;
            }
            
            if(ori_value===undefined) {
                if(in_dirty) {//其實應該甚麼都不用做，不過還是以防萬一檢查一下
                    clearDirtyPrivate(prev);
                }
            }
            else if(ele.attr('type')==='checkbox') {
                if(ori_value==='true'!=ele.prop('checked')) {
                    markDirtyPrivate(ele);
                }
                else {
                    if(in_dirty) {
                        clearDirtyPrivate(prev);
                    }
                }
            }
            else if(ele.attr('type')==='radio') {
                var ele_name = this.name;
                
                var ele_group_owner = this.form;
                if(ele_group_owner==null) {
                    var ele_group_owner = document.body;
                }
                
                var changed = false;;
                $(ele_group_owner).find('input[type="radio"][name="'+ele_name+'"]').each(function(){
                    var ele_tmp = $(this);
                    var ori_value_tmp = ele_tmp.attr('data-evg-orivalue');
                    
                    if(ori_value_tmp==='true'!=ele_tmp.prop('checked')) {
                        changed = true;;
                    }
                });

                $(ele_group_owner).find('input[type="radio"][name="'+ele_name+'"]').each(function(){
                    var ele_tmp = $(this);
                    var prev_tmp = ele_tmp.prev();
                    var in_dirty_tmp = false;
                    if(prev_tmp.length!==0 && prev_tmp.get(0).tagName==='SPAN' && prev_tmp.hasClass('mark-dirty')) {
                        in_dirty_tmp = true;
                    }
                    
                    if(changed) {
                        if(!in_dirty_tmp) {
                            markDirtyPrivate(ele_tmp);
                        }  
                    }
                    else {
                        if(in_dirty_tmp) {
                            clearDirtyPrivate(prev_tmp);
                        }  
                    }
                });
            }
            else if(this.tagName === 'SELECT' && this.hasAttribute('multiple')) {//20171017 修正多選Select判斷邏輯
                if(ori_value !== $(this).val().toString()) {
                    if(!in_dirty) {
                        markDirtyPrivate(ele);
                    }                
                }
                else {
                    if(in_dirty) {
                        clearDirtyPrivate(prev);
                    }
                }
            }
            else {
                if(ori_value!==this.value) {
                    if(!in_dirty) {
                        markDirtyPrivate(ele);
                    }                
                }
                else {
                    if(in_dirty) {
                        clearDirtyPrivate(prev);
                    }
                }
            }
            

            function markDirtyPrivate(ele) {
                ele.before( "<span class='mark-dirty'></span>" );
                var parent = ele.parent(); 
                if(parent.hasClass('mark-cell-dirty')) {
                    return;
                }
                else {
                    parent.addClass('mark-cell-dirty');
                }
            }
            
            function clearDirtyPrivate(prev) {
                var parent = prev.parent(); 
                prev.remove();
                if(parent.children().filter('mark-dirty').length > 0) {
                    return;
                }
                else {
                    parent.removeClass('mark-cell-dirty');
                }
            }
        },
        markDirty: function(eleGroup) {
            for (var eleIdx = 0; eleIdx < eleGroup.length; eleIdx++) {
                var ele = eleGroup.eq(eleIdx);
                var prev = ele.prev();
                var in_dirty = false;
                if(prev.length!==0 && prev.get(0).tagName==='SPAN' && prev.hasClass('mark-dirty')) {
                    in_dirty = true;
                }
                if(!in_dirty) {//如果現在沒有標注dirty，則加上dirty
                    ele.before( "<span class='mark-dirty'></span>" );
                    var parent = ele.parent(); 
                    if(parent.hasClass('mark-cell-dirty')) {
                        return;
                    }
                    else {
                        parent.addClass('mark-cell-dirty');
                    }   
                }   
            }            
        },
        clearDirty: function(eleGroup) {
            for (var eleIdx = 0; eleIdx < eleGroup.length; eleIdx++) {
                var ele = eleGroup.eq(eleIdx);
                var prev = ele.prev();
                var in_dirty = false;
                if(prev.length!==0 && prev.get(0).tagName==='SPAN' && prev.hasClass('mark-dirty')) {
                    in_dirty = true;
                }
                if(in_dirty) {//如果現在有標注dirty，則移除dirty
                    var parent = prev.parent(); 
                    prev.remove();
                    if(parent.children().filter('mark-dirty').length > 0) {
                        return;
                    }
                    else {
                        parent.removeClass('mark-cell-dirty');
                    }
                }
            }
        },
        /**
         * 將集合內所有的元素，轉成name:value形式的物件
         * 20161102 支援checkbox, radio 而且如果已經有此name的元素，會變成陣列型態
         * 20161206 如果name未定義，則跳過該元素
         * @param target - 含有許多input元素的上層元素，為JQuery物件，通常為DIV或TABLE
         * @param addChange - true代表只傳更改的，false代表只傳keyArr清單，null代表全部都傳
         * @param keyArr - 不管有沒有變動，必定傳送的name清單
         * @Deprecated
         * @returns JS物件，內為name與value的組合
         * @example evg.util.form.serializeArray($('#table-query'))
         */
    	serializeArray: function(target, addChange, keyArr)
    	{
    		/**
    		 * Loop:input
    		 *    if name == null
    		 *        continue;
    		 *    if addChange == null//簡單模式
    		 *        if (is ckeckbox or radio) and not checked
    		 *                continue;
    		 *    else //非簡單模式
    		 *        if 不在key值裡面 
    		 *            if is ckeckbox or radio
    		 *                if addChange == false
    		 *                    continue;
    		 *                else addChange == true
    		 *                    changed = 判斷同name的check or radio是否有改變
    		 *                    if(!changed)
    		 *                        continue;
    		 *                    if(沒有鈎)
    		 *                        加入changed_checkbox清單
    		 *                        continue;
             *            else
             *                if addChange == false
             *                    continue;
             *                else addChange == true
             *                    if value與ori_value一樣，
             *                      continue
             *    if money and money_format need change
             *        轉型
             *    if 同name不在結果中
             *        放進去
             *    else if 同name結果是陣列
             *        放進陣列
             *    else
             *        轉成陣列，放進去
             *    if addChange == true
             *        loop changed_checkbox
             *            if not in result
             *                add 空陣列 in result
    		 *                              
    		 */
    	    //20161216 Brad:針對千分位轉型
    	    var deformatMoney = evg.util.money != undefined && evg.util.money.getSerial() === 'Y';
    		var changed_checkbox = {};
    	    
    	    var result = {};
    		$.each(target.find(':input').not('button, [type=button]'), function(index){
    		    var tmp_ele = $(this);
    		    var tmp_name = tmp_ele.attr('name');
                var tmp_value = tmp_ele.val();
                //20161216 Brad:針對千分位轉型
                if(deformatMoney && tmp_ele.hasClass('money')) {
                    if(tmp_value!=='') {//20170208，如果原本是空的，則保持為空
                        tmp_value = evg.util.money.toNumber(tmp_value);   
                    }
                }
                
                if(tmp_name===undefined) {//簡單模式
                    return;
                }

                if(addChange == null) {//簡單模式
                    if(tmp_ele.attr('type')==='checkbox' || tmp_ele.attr('type')==='radio') {
                        if(!tmp_ele.prop('checked')) {
                            return;
                        }
                    }
                }
                else {
                    if(keyArr==null || keyArr.indexOf(tmp_name) === -1) {//如果不在key值裡面          
                        if(tmp_ele.attr('type')==='checkbox' || tmp_ele.attr('type')==='radio') {
                            if(addChange==false) {//此模式，代表不是key值就不存入
                                return;
                            }
                            else if(addChange===true) {//要檢查
                                var changed = false;
                                //迴圈每一個同名同類物件
                                target.find(':input[name="'+tmp_name+'"][type="'+tmp_ele.attr('type')+'"]').each(function(){
                                    var child = $(this);
                                    var child_check = child.prop('checked').toString();
                                    var check_ori_check = child.attr('data-evg-orivalue');
                                    if(child_check !== check_ori_check) {//如果選擇的情況不一樣，代表有修改
                                        changed = true;
                                    } 
                                });

                                if(!changed) {//如果都沒有修改，代表不用存入
                                    return;
                                }
                                if(!$(this).prop('checked')) {
                                    changed_checkbox[tmp_name] = {};
                                    return;
                                }
                            } 
                        } 
                        else {
                            if(addChange==false) {//此模式，代表不是key值就不存入
                                return;
                            }
                            else if(addChange===true) {//要檢查
                                var ori_value = tmp_ele.attr('data-evg-orivalue');
                                if(ori_value == tmp_value) {//?到底要不要嚴格判斷相等
                                    return;
                                } 
                            } 
                        }                          
                    }
                }
    		    
    			if(result[tmp_name]===undefined) {
    			    result[tmp_name] = tmp_value;
    			}
    			else if(Array.isArray(result[tmp_name])) {
    			    result[tmp_name].push(tmp_value);
    			}
    			else {
    			    result[tmp_name] = [result[tmp_name], tmp_value];
    			}
    		});
    		
    		if(addChange) {
    		    Object.keys(changed_checkbox).forEach(function(currentName, index, arr){
                    if(!result.hasOwnProperty(currentName)){
                        result[currentName] = [];
                    }
                });    
    		}
    		
    		return result;
    	},
    	/**
    	 * 將此區域資料轉成JS物件
         * @param target - 含有許多input元素的上層元素，為JQuery物件，通常為DIV或TABLE
         * @param type - 不傳：代表所有的元素資料都會在裡面。changed：傳遞有改的資料，key：只傳keyArr清單
         * @param keyArr - 不管有沒有變動，必定傳送的name清單
    	 * @returns JS物件，內為name與value的組合
    	 */
    	toObject: function(target, type, keyArr) {
    	    //1.取得所有name
    	    var nameArr = [];
    	    $.each(target.find(':input').not('button, [type=button]'), function(index){
                if(this.name===undefined) {//簡單模式
                    return;
                }
    	        
                if(this.name==='') {
                    return;
                }
                
                if(nameArr.indexOf(this.name)===-1 && !(type === 'key' && keyArr.indexOf(this.name)===-1)) {
                    nameArr.push(this.name);
                }
    	    });
    	    
    	    var result = {};
    	    var eleChanged =  function(ele, value) {
                if($.isArray(value)) {//20171017 Brad:0與空字串，應該要是不同的結果
                    value = value.toString();
                }
    	        if(ele.attr('type')==='checkbox' || ele.attr('type')==='radio') {
                    var check = ele.prop('checked').toString();
                    var check_ori = ele.attr('data-evg-orivalue');
                    if(check !== check_ori) {//如果選擇的情況不一樣，代表有修改
                        return true;
                    } 
                    else {
                        return false;
                    }
                }
                else {
                    value_ori = ele.attr('data-evg-orivalue');
                    if(value !== value_ori) {//如果選擇的情況不一樣，代表有修改  20171017 Brad:0與空字串，應該要是不同的結果
                        return true;
                    } 
                    else {
                        return false;
                    }
                }    	        
            };
            
            var deformatMoney = evg.util.money != undefined && evg.util.money.getSerial() === 'Y';//針對千分位轉型
    	    nameArr.forEach(function(name){
    	        var sameNameArr = target.find('[name='+name+']').not('button, [type=button]');
    	        var value;
    	        var changed;
                if(sameNameArr.length == 1) {
                    var ele = sameNameArr;
                    if(ele.attr('type')==='checkbox' || ele.attr('type')==='radio') {//如果只有一個應該不會有radio，不過還是留著
                        value = [];
                        if(ele.prop('checked')) {
                            //value.push(ele.val());
                            value = [ele.val()];
                        }
                        else {
                            value = [];
                        }
                        changed = eleChanged(ele, value);
                    }
                    else {
                        value = ele.val();
                        
                        //針對千分位轉型
                        if(deformatMoney && ele.hasClass('money')) {
                            if(value!=='') {//20170208，如果原本是空的，則保持為空
                                value = evg.util.money.toNumber(value).toString();   
                            }
                        }    
                        
                        changed = eleChanged(ele, value);
                    }
                    
                }
                else {
                    value = [];
                    changed = false;
                    $.each(sameNameArr, function(index){
                        var ele = $(this);
                        if(ele.attr('type')==='checkbox' || ele.attr('type')==='radio') {
                            if(ele.prop('checked')) {
                                value.push(this.value);
                            }
                            changed = changed || eleChanged(ele, this.value);
                        }
                        else {
                            var ele_value = this.value;
                            //針對千分位轉型
                            if(deformatMoney && ele.hasClass('money')) {
                                if(ele_value!=='') {//20170208，如果原本是空的，則保持為空
                                    ele_value = evg.util.money.toNumber(ele_value).toString();   
                                }
                            }
                            value.push(ele_value);
                            changed = changed || eleChanged(ele, ele_value);
                        }
                    });
                }
                
                if(type === undefined || type === 'key') {//簡單模式
                    result[name] = value;
                }
                else if(type === 'changed') {
                    if(keyArr!=null && keyArr.indexOf(name) !== -1) {
                        result[name] = value;
                    }
                    else if(changed){
                        result[name] = value;
                    }
                }
    	    });
    	    
    	    return result;
    	},
    	/**
    	 * 將此區域資料轉成JSON字串
    	 * @param target - 含有許多input元素的上層元素，為JQuery物件，通常為DIV或TABLE
    	 * @param type - 不傳：代表所有的元素資料都會在裡面。changed：傳遞有改的資料，key：只傳keyArr清單
    	 * @param keyArr - 不管有沒有變動，必定傳送的name清單
    	 * @returns JSON字串
    	 */
    	toString: function(target, type, keyArr) {
    	    return JSON.stringify(evg.util.form.toObject(target, type, keyArr));
        },
        /**
         * (20171017) 將此區域原始資料轉成JS物件
         * @param target - 含有許多input元素的上層元素，為JQuery物件，通常為DIV或TABLE
         * @param type - 不傳：代表所有的元素資料都會在裡面。key：只傳keyArr清單
         * @param keyArr - 不管有沒有變動，必定傳送的name清單
         * @returns JS物件，內為name與value的組合
         */
        toObjectOri: function(target, type, keyArr) {
            //1.取得所有name
            var nameArr = [];
            $.each(target.find(':input').not('button, [type=button]'), function(index){
                if(this.name===undefined) {//簡單模式
                    return;
                }
                
                if(this.name==='') {
                    return;
                }
                
                if(nameArr.indexOf(this.name)===-1 && !(type === 'key' && keyArr.indexOf(this.name)===-1)) {
                    nameArr.push(this.name);
                }
            });
            
            var result = {};
            
            nameArr.forEach(function(name){
                var sameNameArr = target.find('[name='+name+']').not('button, [type=button]');
                var value;
                var changed;
                if(sameNameArr.length == 1) {
                    var ele = sameNameArr;
                    if(ele.attr('type')==='checkbox' || ele.attr('type')==='radio') {//如果只有一個應該不會有radio，不過還是留著
                        value = [];
                        if(ele.attr('data-evg-orivalue')==='true') {
                            value = [ele.val()];
                        }
                        else {
                            value = [];
                        }
                    }
                    else {
                        if(ele[0].tagName === 'SELECT' && ele.attr('multiple')) {
                            value = ele.attr('data-evg-orivalue').split(',');
                        }
                        else {
                            value = ele.attr('data-evg-orivalue');
                        }
                    }
                    
                }
                else {
                    value = [];
                    changed = false;
                    $.each(sameNameArr, function(index){
                        var ele = $(this);
                        if(ele.attr('type')==='checkbox' || ele.attr('type')==='radio') {
                            if(ele.attr('data-evg-orivalue')==='true') {
                                value.push(this.value);
                            }
                        }
                        else {
                            var ele_value = ele.attr('data-evg-orivalue');
                            if(ele[0].tagName === 'SELECT' && ele.attr('multiple')) {
                                value.push(ele_value.split(','));
                            }
                            else {
                                value.push(ele_value);
                            }
                        }
                    });
                }
                
                {//簡單模式
                    result[name] = value;
                }
            });
            
            return result;
        },
        /**
         * (20171017) 將此區域原始資料轉成JSON字串
         * @param target - 含有許多input元素的上層元素，為JQuery物件，通常為DIV或TABLE
         * @param type - 不傳：代表所有的元素資料都會在裡面。key：只傳keyArr清單
         * @param keyArr - 不管有沒有變動，必定傳送的name清單
         * @returns JSON字串
         */
        toStringOri: function(target, type, keyArr) {
            return JSON.stringify(evg.util.form.toObjectOri(target, type, keyArr));
        },
        /**
         * 判斷此區域是否有更改
         * @param target - 含有許多input元素的上層元素，為JQuery物件，通常為DIV或TABLE
         * @param byPassArr - 就算有更新也不管的name清單
         * @returns {Boolean}
         */
        isChanged: function(target, byPassArr) {
            var eleChanged =  function(ele, value) {
                if($.isArray(value)) {//20171017 Brad:0與空字串，應該要是不同的結果
                    value = value.toString();
                }
                if(ele.attr('type')==='checkbox' || ele.attr('type')==='radio') {
                    var check = ele.prop('checked').toString();
                    var check_ori = ele.attr('data-evg-orivalue');
                    if(check !== check_ori) {//如果選擇的情況不一樣，代表有修改
                        return true;
                    } 
                    else {
                        return false;
                    }
                }
                else {
                    value_ori = ele.attr('data-evg-orivalue');
                    if(value !== value_ori) {//如果選擇的情況不一樣，代表有修改 20171017 Brad:0與空字串，應該要是不同的結果
                        return true;
                    } 
                    else {
                        return false;
                    }
                }               
            };
            
            var deformatMoney = evg.util.money != undefined && evg.util.money.getSerial() === 'Y';//針對千分位轉型
            
            //1.取得所有name
            var nameArr = [];
            var updated = false;
            $.each(target.find(':input').not('button, [type=button]'), function(index){
                if(updated) {//如果別的input已經發現有改變，就不用檢查了
                    return;
                }
                var name = this.name;
                if(name===undefined) {//簡單模式
                    return;
                }
                
                if(name==='') {
                    return;
                }
                
                if(nameArr.indexOf(name)!==-1) {
                    return;
                }
                
                if(byPassArr!=null && byPassArr.indexOf(name)!==-1) {
                    return;
                }
                
                nameArr.push(this.name);
                
                var sameNameArr = target.find('[name='+name+']').not('button, [type=button]');
                var value;
                if(sameNameArr.length == 1) {
                    var ele = sameNameArr;
                    if(ele.attr('type')==='checkbox' || ele.attr('type')==='radio') {//如果只有一個應該不會有radio，不過還是留著
                        value = [];
                        if(ele.prop('checked')) {
                            value = [ele.val()];
                        }
                        else {
                            value = [];
                        }
                        if(eleChanged(ele, value)) {
                            updated = true;
                            return;
                        }
                    }
                    else {
                        value = ele.val();
                        
                        //針對千分位轉型
                        if(deformatMoney && ele.hasClass('money')) {
                            if(value!=='') {//20170208，如果原本是空的，則保持為空
                                value = evg.util.money.toNumber(value).toString();   
                            }
                        }    
                        
                        if(eleChanged(ele, value)) {
                            updated = true;
                            return;
                        }
                    }
                    
                }
                else {
                    value = [];
                    value_ori = [];
                    $.each(sameNameArr, function(index){
                        var ele = $(this);
                        if(ele.attr('type')==='checkbox' || ele.attr('type')==='radio') {
                            if(ele.prop('checked')) {
                                value.push(this.value);
                            }
                            if(eleChanged(ele, this.value)) {
                                updated = true;
                                return;
                            }
                        }
                        else {
                            var ele_value = this.value;
                            //針對千分位轉型
                            if(deformatMoney && ele.hasClass('money')) {
                                if(ele_value!=='') {//20170208，如果原本是空的，則保持為空
                                    ele_value = evg.util.money.toNumber(ele_value).toString();   
                                }
                            }
                            value.push(ele_value);
                            if(eleChanged(ele, ele_value)) {
                                updated = true;
                                return;
                            }
                        }
                    });
                }
            });
            
            return updated;
        },
        /**
    	 * 將集合內所有的元素，轉成name:value形式的JSON字串
         * @param target - 含有許多input元素的上層元素，為JQuery物件，通常為div或table或tr
         * @param addChange - true代表只傳更改的，false代表只傳keyArr清單，null代表全部都傳
         * @param keyArr - 不管有沒有變動，必定傳送的name清單
         * @Deprecated
         * @returns JSON字串，內為name與value的組合
    	 * @example evg.util.form.toJSONString($('#table-query'))
    	 */
    	toJSONString: function(target, addChange, keyArr) {
    		return JSON.stringify(evg.util.form.serializeArray(target, addChange, keyArr));
    	},
    	/**
    	 * 處理Ajax成功後的動作，目前會自動進行datepicker註冊動作
    	 * @param event - 包含 event 物件
    	 * @param request - 包含 XMLHttpRequest 物件
    	 * @param settings - 包含 AJAX 請求中使用的選項
    	 */
    	ajaxSuccess: function(event, request, settings) {
    	    evg.util.form.formatDate();
        },
        /**
         * 將物件上記載的action資料轉成url
         * @param obj - JQuery物件或HTMLElement或，可以轉成JQuery物件的字串
         * @returns URL
         */
        getBtnUrl: function(obj) {
            if(obj instanceof jQuery) {
                return evg.util.fn.toUrl(obj.attr('data-evg-action'));    
            }
            else if(typeof obj === "string") {
                return evg.util.fn.toUrl($(obj).attr('data-evg-action'));                
            }
            else if(obj instanceof HTMLElement) {
                return evg.util.fn.toUrl(obj.getAttribute('data-evg-action'));                
            }
            else {
                return null;
            }            
        },
        /**
         * 將物件上記載的action資料轉成url，並串上Tabkey
         * @param obj - JQuery物件或HTMLElement或，可以轉成JQuery物件的字串
         * @returns URL
         */
        getBtnUrlWithTabkey: function(obj) {
            var URL = evg.util.form.getBtnUrl(obj);
            if(URL===null) {
                return URL;
            }
            var tabkey = evg.util.fn.getTabkey();
            URL += "?tabkey=" + tabkey;
            return URL;
        },
        /**
         * 取出物件上記載的action資料
         * @param obj - JQuery物件或HTMLElement或，可以轉成JQuery物件的字串
         * @returns Action
         */
        getBtnAction: function(obj) {
            if(obj instanceof jQuery) {
                return obj.attr('data-evg-action');    
            }
            else if(typeof obj === "string") {
                return $(obj).attr('data-evg-action');                
            }
            else if(obj instanceof HTMLElement) {
                return obj.getAttribute('data-evg-action');                
            }
            else {
                return null;
            }            
        },
        /**
         * 針對指定的name_arr，將from元素中的所有value，指定到to元素
         * @param fm - 包含許多input父元素，格式為JQuery物件，copy的來源
         * @param to - 包含許多input父元素，格式為JQuery物件，copy to 的位置
         * @param name_arr - 記載許多name。ex:['parm1','parm2']
         * @example evg.util.form.copyValue($('#add_table'), $('#table-query'), ['accountNo','customerId']);
         */
        copyValue: function(fm, to, name_arr) {
            for (var i = 0; i < name_arr.length; i++) {
                to.find('[name="'+ name_arr[i] + '"]').val(fm.find('[name="'+ name_arr[i] + '"]').val());
            }
        },
        /**
         * 替指定的input.date格式化
         * @param target 要檢查的區塊
         */
        formatDate: function(target) {
            var searchTarget;
            if(target==null) {
                searchTarget = $('input.date');
            }
            else {
                searchTarget = target.find('input.date');
            }
            
            searchTarget.each(function(){
                var dateFormat = this.getAttribute('data-evg-date-format')||'yymmdd';
                var maxDate = this.getAttribute('data-evg-date-max')||'';
                var minDate = this.getAttribute('data-evg-date-min')||'';
                $(this).datepicker({
                    dateFormat: dateFormat,
                    maxDate : maxDate,
                    minDate : minDate
                  });
            });
        },
        /**
         * 替陣列裡面每個元素加上序號
         * @param ja 陣列
         * @param idx_name index的名稱(可不傳，預設為idx)
         * @returns
         * @example evg.util.form.addJsonArrayIdx({parm_a:1,parm_b:2,parm_c:3});
         */
        addJsonArrayIdx: function(ja, idx_name) {
            idx_name = idx_name || 'i';
            ja.forEach(function(element, index, array){
                element[idx_name] = index;
            });
            return ja;
        },
        showSuccessSave: function(obj) {
            var cnt = getSaveRecordCount(obj);
            showSuccessAct(cnt, evg.util.fn.translateOpt('Save', 'I009_SAVE'));
        },
        showSuccessInsert: function(obj) {
            var cnt = getSaveRecordCount(obj);
            showSuccessAct(cnt, evg.util.fn.translateOpt('Insert', 'I010_INSERT'));
        },
        showSuccessUpdate: function(obj) {
            var cnt = getSaveRecordCount(obj);
            showSuccessAct(cnt, evg.util.fn.translateOpt('Update', 'I011_UPDATE'));
        },
        showSuccessDelete: function(obj) {
            var cnt = getSaveRecordCount(obj);
            showSuccessAct(cnt, evg.util.fn.translateOpt('Delete', 'I012_DELETE'));
        },
        showSuccessVerb: function(verb, obj) {
            var cnt = getSaveRecordCount(obj);
            showSuccessAct(cnt, verb);
        },
        getJqueryObj: function(obj) {
            if(obj instanceof jQuery) {
                return obj;    
            }
            else if(typeof obj === "string") {
                return $(obj);                
            }
            else if(obj instanceof HTMLElement) {
                return $(obj);                
            }
        }
    };

    evg.util.form = new evg.util.form(); // 產生evg.util.form物件

    // Self-Executing Anonymous Function
    (function() {
        evg.util.form.formatDate();
        $(document).ajaxSuccess(evg.util.form.ajaxSuccess);
        //$('[required]').blur(evg.util.form.checkRequired);
    })();

    // 將evg, evg.util, evg.util.form暴露為全域變數
    if ( typeof noGlobal === strundefined ) {
        window.evg = evg || {};
        window.evg.util = evg.util || {};
        window.evg.util.form = evg.util.form;
    }
    return evg.util.form;
});