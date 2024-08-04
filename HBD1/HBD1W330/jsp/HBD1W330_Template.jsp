<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script id='hbt_ItemList' type='text/x-handlebars-template'>
<option value="*">-請選擇-</option>
	{{#data.ITEM_LIST}}
		<option value="{{itemCode}}">{{itemAbbr}}</option>
	{{/data.ITEM_LIST}}
</script>

<script id='hbt_AddItemList' type='text/x-handlebars-template'>
<tbody>
{{#data.ITEM_LIST}}	
	<tr>
		<td>{{@index}}</td>
		<td>
			<input type="text" name="txtItemName" value="{{itemAbbr}}" readonly class="text-right readonly-noBG" size="12" />			
		</td>
		<td>
			<input type="number" name="txtTrnsQty" size="3" maxlength="3" min="0" max="999" autoComplete="Off" />
			<input type="hidden" name="dlItemCode" value="{{itemCode}}" />
			<input type="hidden" name="txtCacpRecpCode" value="{{itemCode}}" />
		</td>
		<td>{{itemUnit}}</td>
		<td><input type="text" name="txtCacpNotes" style="width:100%" /></td>
	</tr>
{{/data.ITEM_LIST}}
</tbody>
</script>


<script id='hbt_QueryResult' type='text/x-handlebars-template'>
  <tbody>
    {{#data.TRNS_LIST}}
	<tr data-rowidx="trIndex{{@index}}">		
		<td nowrap class="text-center" style="width:1%">			
			<input type="hidden" name="txtCacpdUuid" value='{{cacpdUuid}}' />
			<input type="hidden" name="txtCacpRecpCode" value="{{itemCode}}" />
			{{cacpDate}}-{{cacpNo}}
		</td>				
		<td nowrap class="text-right" style="width:1%">
			<input type="hidden" name="txtItemName" value='{{itemName}}' />{{itemAbbr}}						
		</td>
		<td nowrap class="text-right" style="width:1%">
			<input type="hidden" name="txtCacpQty" value='{{cacpQty}}' />{{cacpQty}}			
		</td>		
        <td nowrap style="width:1%">{{itemUnit}}</td>
		<td nowrap style="width:1%"><input type="text" name="txtCacpNotes" value="{{cacpNotes}}" readonly class="readonly-noBG" /></td>
		<td nowrap>
			<button type="button" title="修改" onclick="formEvent.showData(this)" class="btn btn-plus-icon btn-secondary btn-save-icon"><i class="fas fa-pencil-alt"></i></button>
		</td>		
	</tr>
    {{/data.TRNS_LIST}}
  </tbody>
</script>