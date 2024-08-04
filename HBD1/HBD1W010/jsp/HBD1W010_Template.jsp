<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script id='hbt_bidList' type='text/x-handlebars-template'>
<tbody>   
    {{#data.BIDLIST}}
		<tr>			
			<th style="text-align: left" colspan="2">{{bidName}}</th>
			<!--<td>{{ordSdate}}-{{ordEdate}}</td>-->
			<td style="text-align:right; width:1%" nowrap>
				<input type="button" class="btn btn-info form-control" onclick="formEvent.loadBidItem('{{bidUuid}}')" value="預訂" />				
			</td>
		</tr>      
    {{/data.BIDLIST}}
</tbody> 
</script>

<script id='hbt_BidItemList' type='text/x-handlebars-template'>
{{#data.WEEK_LIST}}
	<div class="col-md-12">
	<div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
	  <!--<div class='card-header text-large card-title'>{{PO_DATE}}({{WEEKDAY}}) 預訂單</div>-->                    
	  <div class='card-body'>
			<table class='table-plus table-detail width-fit-parent' id='bidIItemDay{{@index}}' data-order-date="{{ORDER_DATE}}" style="width:100%">
			  <thead class="sticky"><th colspan="2" class=" text-center bg-warning">{{PO_DATE}}({{WEEKDAY}}) 預訂單</th></thead>			
			  <tbody>	
		{{#ITEM_LIST}}
				<tr>
				  <th>
					<input type="text" name="txtItemAbbr" value="{{itemAbbr}}" readonly class="readonly-noBG text-right form-control font-weight-bold" style="width:100%" />
					<input type="hidden" name="txtBiddItem" value="{{itemCode}}" />					
					<input type="hidden" name="txtBiddPrice" value="{{ipUnitPrice}}" />
				  </th>				              
				  <td style="width:1%" nowrap>
					<div class="flex-container">                                                               
					<button type="button" onclick="formEvent.minusItem(this)" class="btn btn-default text-center" title='減1'><i class="fas fa-minus"></i></button>
					<input type="number" name="txtPoddQty" value="{{poddQty}}" min="0" max="99" style="text-align:center;height:45px" />
					<button type="button" onclick="formEvent.addItem(this)" class="btn btn-default text-center" title='加1'><i class="fas fa-plus"></i></button>
					<span style="color:white;display:none">{{@index}}</span>
					</div>										
				  </td>
				</tr>
		{{/ITEM_LIST}}
			  </tbody>
			</table>				        
	  </div>   
	</div>
	</div>
{{/data.WEEK_LIST}}
<input type="button" class="btn btn-default form-control font-weight-bold" value="回上頁" onclick="formEvent.backBidList()" /><p/>
<input type="button" class="btn btn-warning form-control font-weight-bold" value="下一步" onclick="formEvent.bidItem2CartItem()" />
</script>

<script id='hbt_CartItemList' type='text/x-handlebars-template'>
{{#data.WEEK_LIST}}
	<div class="col-md-12">
	<div class='card card-plus' style="margin-left: 0px; margin-right: 0px;">                  
	  <!--<div class='card-header text-large card-title text-danger'>{{ORDER_DATE}} ({{ORDER_WEEKDAY}}) 訂單預覽</div>-->                    
	  <div class='card-body'>
			<table class='table-plus table-detail width-fit-parent' id='bidIItemDay{{@index}}' data-order-date="{{ORDER_DATE}}" style="width:100%">
		     <thead class="sticky text-center"><th colspan="2" class=" text-center bg-warning">{{ORDER_DATE}} ({{ORDER_WEEKDAY}}) 訂單預覽</th></thead>
			  <tbody>	
		{{#ITEM_LIST}}
				<tr class="poItemRow">
				  <th>
					<!--<button type="button" onclick="formEvent.confirmRemoveCartItem(this)"><i class="fas fa-trash"></i></button>-->					                            
					<input type="text" name="txtItemAbbr" value="{{itemAbbr}}" size="6" readonly class="readonly-noBG text-right form-control font-weight-bold" />
					<input type="hidden" name="txtBiddItem" value="{{itemCode}}" />					
					<input type="hidden" name="txtBiddPrice" value="{{biddPrice}}" />
				  </th>				              
				  <td style="width:1%" nowrap>
					<div class="flex-container">                                                               
					<button type="button" onclick="formEvent.minusItem(this)" class="btn btn-default text-center" title='減1'><i class="fas fa-minus"></i></button>				    
					<input type="number" name="txtPoddQty" value="{{poddQty}}" min="0" max="99" style="text-align:center;height:45px" />				
					<button type="button" onclick="formEvent.addItem(this)" class="btn btn-default text-center" title='加1'><i class="fas fa-plus"></i></button>
					</div>
				  </td>
				</tr>
		{{/ITEM_LIST}}
			  </tbody>
			</table>				        
	  </div>   
	</div>
	</div>


{{/data.WEEK_LIST}}
<input type="button" id="btnModifyOrder" class="btn btn-default form-control font-weight-bold" value="增購品項" onclick="formEvent.backBidItemList()" /><p/>
<input type="button" id="btnCreateOrder" class="btn bg-warning text-dark form-control font-weight-bold" value="成立訂單" onclick="formEvent.cartItem2OrderItem()" />
<span id="txgMsg" style="display:none" class="text-center form-control bg-dark text-white font-weight-bold">...請稍候，訂單成立中...<br/></span>
</script>