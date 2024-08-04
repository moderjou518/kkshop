<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script id='hbt_ItemClassOptions4Select' type='text/x-handlebars-template'>  
  <option value="*">全部</option>
    {{#data.ITEMCLASS}}
      <option value="{{miscCode}}">{{miscDesc}}</option>
    {{/data.ITEMCLASS}}
</script>

<script id='hbt_ItemClassOptions' type='text/x-handlebars-template'>
<div class="col-md-12" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
		<button type="button" name="btnQuickQuery" data-type="*" onclick="formEvent.quickQuery('*')" class="btn btn-plus btn-info"style="width: 100%">
			全部 
		</button>
</div>
<!--
{{#data.ITEMCLASS}}  
	<div class="col-md-12" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
		<button type="button" name="btnQuickQuery" onclick="formEvent.quickQuery('{{miscCode}}')" class="btn btn-plus btn-info"style="width: 100%">
			{{miscDesc}} 
		</button>
	</div>
{{/data.ITEMCLASS}}
-->     
</script>

<script id='hbt_QueryResult' type='text/x-handlebars-template'>
  <tbody>
    <tr style="display: none;" data-evg-atrow="add">
		<td nowrap>自動取號
			<!--<input type="text" name="txtItemCode" style="width:110px" maxlength="10" required autocomplete="off" placeholder="自動取號" style="display:none" />-->
		</td>
		<td nowrap>
			<input type="text" name="txtItemName" value="" maxlength="4" autocomplete="off" style="width:100%" title="自編料號" />
		</td>                    
		<td nowrap>
			<input type="text" name="txtItemAbbr" value="" maxlength="30" autocomplete="off" size="20" />
		</td>
		<td nowrap style="text-align:center">
			<input type="text" name="txtItemUnit" value="" maxlength="2" autocomplete="off" style="width:100%" />
			<input type="hidden" name="txtItemPrice" value="" maxlength="5" autocomplete="off" style="width:100%" />
		</td>
		<td nowrap>
				<select id="txtItemType" name="txtItemType" style="width:96%">
				<option value="*">請選擇</option>
    			{{#data.ITEMCLASS}}
      				<option value="{{miscCode}}">{{miscDesc}}</option>
    			{{/data.ITEMCLASS}}
				</select>
		</td>		
		<td nowrap>
			<input type="text" name="txtItemSort" value='' style="width:50px" maxlength="4" min="1" max="9999" autocomplete="off" />
		</td>
		<td nowrap style="text-align:center">
			<input type="checkbox" name="txtItemHidePriceMk" style="zoom:150%" value="Y" />
		</td>		
		<td nowrap style="text-align:center">
			<input type="checkbox" name="txtItemVoidMk" style="zoom:150%" value="Y" />
		</td>		
		<td style="text-align:center">			
			<button type="button" onclick="formEvent.deleteTempRow(this)" class="btn btn-plus-icon btn-default btn-delete-icon" title="Remove"><i class="fas fa-trash"></i></button>
		</td>
		<td nowrap></td>
		<td nowrap></td>
    </tr>
    {{#data.ITEMLIST}}
	<tr data-rowidx="trIndex{{@index}}">		
		<td nowrap>			
			<input type="text" value="{{inc @index}}" readonly class="readonly-noBG text-center" size="2" />
			<input type="hidden" name="txtItemUuid" value="{{itemUuid}}" readonly class="readonly-noBG text-center" size="4" />
			<input type="hidden" name="txtItemCode" value="{{itemCode}}" />
		</td>
		<td nowrap>
			<input type="text" name="txtItemName" value='{{itemName}}' maxlength="4" autocomplete="off" size="4" title="{{itemCode}}" />			
		</td>                   		
		<td nowrap>								
			<input type="text" name="txtItemAbbr" value="{{itemAbbr}}" maxlength="30" autocomplete="off" size="20" />
		</td>
		<td nowrap style="text-align:center">			
			<input type="text" name="txtItemUnit" value="{{itemUnit}}" maxlength="2" autocomplete="off" style="width:100%" />
			<input type="hidden" name="txtItemPrice" value="{{itemPrice}}" maxlength="5" autocomplete="off" style="width:100%" />
		</td>
		<td nowrap>
			<!--<input type="text" name="txtItemType" value='{{itemType}}' style="width:70px" maxlength="4" autocomplete="off" />-->
			<select id="txtItemType" name="txtItemType" style="width:96%">
				<option value="*">請選擇</option>
    			{{#../data.ITEMCLASS}}      				
					<option value="{{miscCode}}" {{#eq miscCode ../itemType}}selected{{/eq}}>{{miscDesc}}</option>        
    			{{/../data.ITEMCLASS}}
			</select>
		</td>		
		<td nowrap>
			<input type="text" name="txtItemSort" value='{{itemSort}}' style="width:50px" maxlength="4" min="1" max="9999" autocomplete="off" />
		</td>		
		<td nowrap style="text-align:center">
			<input type="checkbox" name="txtItemHidePriceMk" style="zoom:150%" value="Y" {{#eq "Y" itemHidePriceMk}}checked{{/eq}}/>
		</td>		
		<td nowrap style="text-align:center">
			<input type="checkbox" name="txtItemVoidMk" style="zoom:150%" value="Y" {{#eq "Y" itemVoidMk}}checked{{/eq}}/>
		</td>
<!--
		<td style="text-align:center">
			<button type="button" onclick="formEvent.deleteThisRow(this)" class="btn btn-plus-icon btn-default btn-delete-icon" title="Remove"><i class="fas fa-trash"></i></button>
		</td>
-->
		<td style="text-align:center">
			<i class="fas fa-info-circle" title="{{itemModUser}}-{{itemModTime}}"></i>
		</td>
		<td nowrap></td>
	</tr>
    {{/data.ITEMLIST}}
  </tbody>
</script>