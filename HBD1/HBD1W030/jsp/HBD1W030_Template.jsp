<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script id='hbt_bidList' type='text/x-handlebars-template'>
<tbody>   
    {{#data.BIDLIST}}
		<tr>			
			<!--<th style="text-align: left">{{bidName}}</th>-->
			<th style="text-align: left">{{ordSdate}}~{{ordEdate}}</th>
			<!--<th style="text-align: left">已出貨/待匯款</th>-->
			<th style="text-align: right">{{weekOrdAmt}}</th>
			<td style="text-align:center" nowrap colspan="2">
				<input type="button" class="btn btn-info" onclick="formEvent.showDetailModal('{{bidUuid}}')" value="明細" />
				<input type="button" class="btn btn-info" onclick="formEvent.showMoneyModal('{{bidUuid}}')" value="匯款" />				
			</td>			
		</tr>      
    {{/data.BIDLIST}}
</tbody> 
</script>

<script id='hbt_WeekPoItemList' type='text/x-handlebars-template'>
<tbody>
{{#data.WEEK_PO}}
	<tr>
		<td>{{pommDate}}</td>
		<td>{{itemName}}</td>
		<td>{{poddRcvdQty}}</td>
		<td>{{poddUnitPrice}}</td>
		<td>{{poddAmt}}</td>
	</tr>
{{/data.WEEK_PO}}
</tbody>
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
					{{itemName}}<!--<br/>NT:{{biddPrice}}-->
					<input type="hidden" name="txtBiddItem" value="{{biddItem}}" />
					<input type="hidden" name="txtItemName" value="{{itemName}}" />
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