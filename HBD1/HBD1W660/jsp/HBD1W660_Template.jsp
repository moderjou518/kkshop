<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script id='hbt_QueryResult' type='text/x-handlebars-template'>
  <tbody>
    {{#data.POSTAT}}
	<tr data-rowidx="trIndex{{@index}}">
		<td nowrap>{{pommDate}} ({{weekDay}})</td>		
		<td nowrap><input type="text" name="txtPommBuyer" value="{{hac0BascName}}" title="{{pommBuyer}}" readonly class="readonly-noBG text-right" size="6" /></td>		                   		
		<td nowrap style="text-align:right">
			<input type="text" name="txtPoOrdAmt" value="{{poOrdAmt}}" readonly class="readonly-noBG text-right {{#neq poOrdAmt poRcvdAmt}}text-danger{{/neq}}" size="6" />
		</td>
		<td nowrap style="text-align:right">
			<input type="text" name="txtPoRcvdAmt" value="{{poRcvdAmt}}" readonly class="readonly-noBG text-right {{#neq poOrdAmt poRcvdAmt}}text-danger{{/neq}}" size="6" />
		</td>
		<!--<td nowrap style="text-align:right">{{pommClosMark}}</td>-->
		<td nowrap style="text-align:right">			
			<input type="button" class="btn btn-default form-control" onclick="formEvent.showDetailModal('{{pommBuyer}}', '{{pommDate}}', '{{pommSeq}}', '{{hac0BascName}}{{pommDate}}({{weekDay}})')" value="檢視訂單明細" />
		</td>
	</tr>
    {{/data.POSTAT}}
  </tbody>
</script>

<script id='hbt_DailyPoItemList' type='text/x-handlebars-template'>
<tbody>
{{#data.DAILY_PO}}
	<tr>
		<td style="text-align:right" nowrap>
			{{#eq poddRcvdQty '0'}}
				<button type="button" title="刪除" onclick="formEvent.confirmDelete(this)" class="btn btn-plus-icon btn-secondary btn-save-icon"><i class="fas fa-trash"></i></button>
			{{/eq}}			
			<input type="text" value="{{itemAbbr}}" readonly class="readonly-noBG text-right" style="width:80%" />
		</td>
		<td style="text-align:right">
			<input type="text" name="txtPoddOrdQty"  value="{{poddOrdQty}}" readonly class="readonly-noBG text-right {{#neq poddOrdQty poddRcvdQty}}text-danger{{/neq}}" size="3" />
		</td>
		<td style="text-align:right">
			<input type="text" name="txtPoddRcvdQty" value="{{poddRcvdQty}}" class="text-right" size="5" maxlength="3" autocomplete="off" />
		</td>
		<td style="text-align:right">
			<input type="text" name="txtPoddUnitPrice" value="{{poddUnitPrice}}" class="text-right" size="4" autocomplete="off" />			
		</td>
		<td style="text-align:right">
			<input type="text" name="txtPoddAmt" value="{{poddAmt}}" readonly class="readonly-noBG text-right" size="5" />
			<input type="hidden" name="txtPoddUuid" value="{{poddUuid}}" />
			<input type="hidden" name="txtPoddItem" value="{{poddItem}}" />			
		</td>
		<td>{{poddModUser}}, {{poddModDateStr}}</td>
	</tr>
{{/data.DAILY_PO}}
</tbody>
</script>

<!-- 20230531: PO 追加 -->
<script id='hbt_AppendDailyPoItemList' type='text/x-handlebars-template'>
<tbody>
{{#data.ADD_PO}}
	<tr>
		<td style="text-align:right">						
			<input type="text" value="{{itemAbbr}}" readonly class="readonly-noBG text-right" />			
		</td>		
		<td style="text-align:right" colspan="2">
			<input type="text" name="txtPoddRcvdQty" value="{{poddRcvdQty}}" class="text-right" size="5"  maxlength="3" autocomplete="off" />
			<input type="hidden" name="txtPoddItem" value="{{itemCode}}" />
		</td>
		<td style="text-align:right" colspan="2">
			<input type="text" name="txtPoddUnitPrice" value="{{ipUnitPrice}}" class="text-right" size="4" autocomplete="off" />			
		</td>
		<td></td>				
	</tr>
{{/data.ADD_PO}}
</tbody>
</script>