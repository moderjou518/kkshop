<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script id='hbt_bidList' type='text/x-handlebars-template'>
<tbody>   
    {{#data.BIDLIST}}
		<tr>			
			<th style="text-align: left" colspan="2">{{bidName}}</th>
			<!--<td>{{ordSdate}}-{{ordEdate}}</td>-->
			<td style="text-align:right; width:1%" nowrap>
				<input type="button" class="btn btn-info" onclick="formEvent.loadBidItem('{{bidUuid}}')" value="預訂" />				
			</td>
		</tr>      
    {{/data.BIDLIST}}
</tbody> 
</script>

<script id='hbt_BidItemList' type='text/x-handlebars-template'>
{{#data.WEEK_LIST}}
	<div class="col-md-12">
	<div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
	  <div class='card-header text-large card-title'>{{ORDER_DATE}} 預訂單</div>                    
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
					<input type="text" name="txtPoddQty" maxlength="2" value="0" style="width:70px;text-align:center" />				
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
<input type="button" class="btn btn-default" value="回上頁" onclick="formEvent.backBidList()" />
<input type="button" class="btn btn-warning" value="下一步" onclick="formEvent.bidItem2CartItem()" />
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