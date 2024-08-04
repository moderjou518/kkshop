<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script id='hbt_ItemList' type='text/x-handlebars-template'>  
    {{#data.ITEM_LIST}}
		<option value="{{itemCode}}">{{itemAbbr}}</option>
	{{/data.ITEM_LIST}}
</script>

<script id='hbt_YearList' type='text/x-handlebars-template'>  
    {{#data.YEAR_LIST}}
		<option value="{{miscCode}}">{{miscCode}}</option>
	{{/data.YEAR_LIST}}
</script>


<script id='hbt_QueryResult' type='text/x-handlebars-template'>
  <tbody>
    {{#data.ITEM_PRICE_LIST}}
	<tr data-rowidx="trIndex{{@index}}">
		<!--<td nowrap style="width:1%" nowrap>{{inc @index}}</td>-->
		<td nowrap style="width:1%" nowrap {{#eq WEEK_DAY '六'}}class="text-danger font-weight-bold"{{/eq}}{{#eq WEEK_DAY '日'}}class="text-danger font-weight-bold"{{/eq}}>
			
			
			<input type="text" name="txtIpDate" value='{{ipDate}}' readonly class="readonly-noBG" size="10" />{{WEEK_DAY}}			
		</td>		
		<td nowrap style="width:1%" nowrap>								
			<input type="text" name="txtIpUnitPrice" value="{{ipUnitPrice}}" maxlength="5" autocomplete="off" size="5" class="text-right" required />
			<input type="hidden" name="txtIPUUID" value="{{ipUuid}}" />
			<input type="hidden" name="txtIpItemCode" value="{{ipItemCode}}" />			
		</td>		
		<td></td>
	</tr>
    {{/data.ITEM_PRICE_LIST}}
  </tbody>
</script>
