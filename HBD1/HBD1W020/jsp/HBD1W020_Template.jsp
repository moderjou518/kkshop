<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script id='hbt_bidList' type='text/x-handlebars-template'>
<tbody>   
    {{#data.BIDLIST}}
		<tr>
			<th colspan="3" style="vertical-align:middle;color:red">日期: {{ordSdate}} - {{ordEdate}}</th>
			<th colspan="1" nowrap>
				<input type="button" class="btn btn-default form-control" onclick="formEvent.showDetailModal('{{bidUuid}}')" value="打開明細" />
			</th>
		</tr>
		<tr>			
			<!--<th style="text-align: left">{{bidName}}</th>	-->
			<th style="text-align: right; width:1%;" nowrap></th>		
			<th style="text-align: left; width:1%" nowrap>
				周結金額：$ {{#if weekOrdAmt}}{{weekOrdAmt}}{{else}}0{{/if}}
			</th>
			<th style="text-align: left" colspan="2">
				<!--						
					{{#eq offsPayAmt 0}}
						{{#eq PAST_WEEK 'Y'}}
							<input type="button" class="btn btn-danger form-control" onclick="formEvent.showMoneyModal('{{bidUuid}}','{{bidSaleSdate}}')" value="待匯款" />
						{{/eq}}			
					{{else}}
						<u><a href="#" onclick="formEvent.showMoneyModal('{{bidUuid}}','{{bidSaleSdate}}')">已匯款:{{offsPayAmt}}</a></u>
					{{/eq}}
				-->							
			</th>						
		</tr>      
    {{/data.BIDLIST}}
</tbody> 
</script>

<script id='hbt_reportTime' type='text/x-handlebars-template'>
<tbody>
<tr>
	<td class="text-right"><input type="button" onclick="formEvent.createImage()" value="下載報表" class="btn btn-plus btn-primary" /></td>
</tr>
<tr>
	<td>訂單起迄: {{data.PO_DATE}}<br/>會員名稱: {{data.PO_BUYER}}<br/>報表時間: {{data.REPORT_TIME}}</td>	
</tr></tbody>
</script>

<script id='hbt_WeekPoItemList' type='text/x-handlebars-template'>
<tbody>
{{#data.WEEK_PO}}
	<tr class="{{#eq dayType 9}}bg-warning text-dark{{else}}text-dark{{/eq}} font-weight-bold">
		{{#eq poDate ''}}
			<td colspan="2" class="text-right">{{itemAbbr}}</td>
		{{else}}
			<td colspan="2" class="text-right">{{poDate}} {{#if weekDay}}({{weekDay}}){{/if}}<br/>{{itemAbbr}}</td>			
		{{/eq}}		
		<td style="text-align:right">{{poddUnitPrice}}</td>
		<td style="text-align:right">{{poddOrdQty}}</td>
		<td style="text-align:right" {{#neq dayType 9}}class="{{#neq poddOrdQty poddRcvdQty}}text-danger{{/neq}}"{{/neq}}>{{poddRcvdQty}}</td>		
		<td style="text-align:right">{{poddAmt}}</td>
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


<script id='hbt_CartItemList' type='text/x-handlebars-template'>
{{#data.WEEK_LIST}}
	<div class="col-md-12">
	<div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
	  <div class='card-header text-large card-title text-danger'>{{ORDER_DATE}} 訂單確認</div>                    
	  <div class='card-body'>
			<table class='table-plus table-detail width-fit-parent' id='bidIItemDay{{@index}}' data-order-date="{{ORDER_DATE}}" style="width:100%">
			  <tbody>	
		{{#ITEM_LIST}}
				<tr>
				  <th>
					<input type="text" name="txtItemAbbr" value="{{itemAbbr}}" size="6" readonly class="readonly-noBG" />
					<input type="hidden" name="txtBiddItem" value="{{biddItem}}" />					
				  </th>				              
				  <td colspan="2">                                                               
					<button type="button" onclick="formEvent.minusItem(this)" class="btn btn-plus-icon btn-default" title='減1'><i class="fas fa-minus"></i></button>
					<input type="text" name="txtPoddQty" maxlength="2" value="{{poddQty}}" style="width:70px;text-align:center" />				
					<button type="button" onclick="formEvent.addItem(this)" class="btn btn-plus-icon btn-default" title='加1'><i class="fas fa-plus"></i></button>
				  </td>
				</tr>
		{{/ITEM_LIST}}
			  </tbody>
			</table>				        
	  </div>   
	</div>
	</div>


{{/data.WEEK_LIST}}
<input type="button" class="btn btn-default" value="更改品項" onclick="formEvent.backBidItemList()" />
<input type="button" class="btn btn-danger" value="成立訂單" onclick="formEvent.showLogin()" />
</script>