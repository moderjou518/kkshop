<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<script id='hbt_WEEK_LIST' type='text/x-handlebars-template'>
{{#data.WEEK_LIST}}  
	<div class="col-md-12" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
		<button type="button" name="btnQuickQuery" data-uuid="{{bidUuid}}" onclick="qryEvent.queryOffsLists(this)" class="btn btn-plus btn-info"style="width: 100%">
			{{bidOrdSdate}} - {{bidOrdEdate}}
		</button>
	</div>
{{/data.WEEK_LIST}}     
</script>

<script id='hbt_QueryResult' type='text/x-handlebars-template'>
  <tbody>
	{{#data.OFFS_LIST}}
	<tr data-rowidx="trIndex{{@index}}">
		<td nowrap>{{hac0BascName}}</td>
		<td nowrap style="text-align:right">{{poddRcvdAmtSum}}</td>		
		<td nowrap style="text-align:right">
			<input type="text" name="txtOffsPayAmt" value="{{offsPayAmt}}" style="text-align:right" class="readonly-noBG" readonly size="8" />
		</td>
		<td nowrap style="text-align:center" {{#neq poddRcvdAmtSum offsPayAmt}}class="text-danger"{{/neq}}>			
			<input type="text" name="txtDiffAmt" value="{{diffAmt}}" style="text-align:right" class="readonly-noBG" readonly size="8" />
		</td>
		<td nowrap style="text-align:center">			
			<input type="text" name="txtOffsPayDate" value="{{offsPayDate}}" style="text-align:center" class="readonly-noBG" readonly size="8" />
		</td>
		<td nowrap>
			<input type="button" class="btn btn-default form-control" onclick="qryEvent.showDetailModal('{{offsBuyer}}','{{offsBidmUuid}}', this)" value="明細" />			
		</td>
		<td nowrap>
			<input type="button" class="btn btn-default form-control" onclick="formEvent.showMoneyModal('{{offsBuyer}}','{{offsBidmUuid}}', this)" value="匯款" />
		</td>
	</tr>
    {{/data.OFFS_LIST}}
  </tbody>
</script>


<script id='hbt_WeekPoItemList11' type='text/x-handlebars-template'>
<tbody>
{{#data.WEEK_PO}}
	<tr>
		<td>{{poDate}} ({{weekDay}})</td>
		<td>{{itemName}}</td>
		<td style="text-align:right">{{poddOrdQty}}</td>
		<td style="text-align:right">{{poddRcvdQty}}</td>
		<td style="text-align:right">{{poddAmt}}</td>
	</tr>
{{/data.WEEK_PO}}
</tbody>
</script>

<script id='hbt_WeekPoItemList' type='text/x-handlebars-template'>
<tbody>
{{#data.WEEK_PO}}
	<tr class="{{#eq dayType 9}}bg-warning text-dark{{else}}text-dark{{/eq}} font-weight-bold">
		{{#eq poddRcvdQty '小計'}}
		<td></td>    		
		<td colspan="3" style="text-align:right" nowrap {{#neq dayType 9}}class="{{#neq poddOrdQty poddRcvdQty}}text-danger{{/neq}}"{{/neq}}>{{poddRcvdQty}}</td>		
		<td colspan="1"  style="text-align:right">			
			<span name="txtDayAmount" class="txtDayAmount">{{poddAmt}}</span>
		</td>
		{{else}}
			<td>{{poDate}}{{#if weekDay}}({{weekDay}}){{/if}}</td>	    
			<td class="text-right">{{itemAbbr}}</td>			
			<!--<td style="text-align:right">{{poddOrdQty}}</td>-->	
			<td style="text-align:right" nowrap {{#neq dayType 9}} {{/neq}}>{{poddRcvdQty}}</td>
			<td style="text-align:right">{{poddUnitPrice}}</td>		
			<td style="text-align:right">{{poddAmt}}</td>
		{{/eq}}
	</tr>
{{/data.WEEK_PO}}
</tbody>
</script>

<script id='hbt_WeekPoTotal' type='text/x-handlebars-template'>
<tfoot>
{{#data.WEEK_TOTAL}}
	<tr class="text-white bg-dark">
		<td colspan="3" class="text-right">本周累計</td>	    
		<td class="text-right"></td>
		<td class="text-right"></td>
		<td class="text-right">{{TotalAmt}}</td>
	</tr>
{{/data.WEEK_TOTAL}}
</tfoot>
</script>

<script id='hbt_QuerySubResult' type='text/x-handlebars-template'>
  <tbody>
    {{#data.BIDDLIST}}
	<tr data-rowidx="trIndex{{@index}}">
		<td nowrap>{{itemName}}</td>
		<td nowrap style="text-align:center">			
			<input type="text" name="txtBiddPrice" value="{{biddPrice}}" maxlength="8" required autocomplete="off" style="width:80px" />			
		</td>
		<td nowrap>{{bidUuid}}
			<input type="hidden" name="txtBiddItem" value="{{biddItem}}" />
			<input type="hidden" name="txtBiddUuid" value="{{biddUuid}}" />
			<input type="hidden" name="txtBidmUuid" value="{{bidmUuid}}" />
			<input type="hidden" name="txtBiddComp" value="{{biddComp}}" />
		</td>
	</tr>
    {{/data.BIDDLIST}}
  </tbody>
</script>