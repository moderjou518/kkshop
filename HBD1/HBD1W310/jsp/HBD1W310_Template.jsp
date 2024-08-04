<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<script id='hbt_QueryResult' type='text/x-handlebars-template'>
    {{#data.CUST_LIST}}
		<button type="button" name="btnQueryPO" data-po-date="{{pommDate}}" data-po-seq="{{pommSeq}}" data-po-buyer="{{pommBuyer}}" class="btn btn-plus btn-default" style="width: 100%; margin-bottom:1px">{{hac0BascName}}</button>
    {{/data.CUST_LIST}}
  </tbody>
</script>

<script id='hbt_POItemList' type='text/x-handlebars-template'>			
<tbody>	
	{{#data.PO_ITEM_LIST}}
		<tr>
			<td nowrap style="text-align:right">				
				<input type="text" value="{{itemAbbr}}" readonly class="readonly-noBG text-right" size="5" />
			</td>				              
			<td nowrap style="text-align:right">				
				<input type="text" name="txtPoddOrdQty" value="{{poddOrdQty}}" readonly class="readonly-noBG text-right" size="5"/>
				<input type="hidden" name="txtPoddDate" value="{{poddDate}}" />
				<input type="hidden" name="txtPoddSeq"  value="{{poddSeq}}" />
				<input type="hidden" name="txtPoddItem" value="{{poddItem}}" />
			</td>
			<td nowrap style="text-align:center">                                                               
				<button type="button" name="btnMinusItem" onclick="formEvent.minusItem(this)" class="btn btn-large btn-default" title='減1'><i class="fas fa-minus"></i></button>
				<input type="text" name="txtPoddRcvdQty" maxlength="2" value="{{poddRcvdQty}}" style="width:70px;text-align:center" />				
				<button type="button" name="btnPlusItem" onclick="formEvent.addItem(this)" class="btn btn-large btn-default" title='加1'><i class="fas fa-plus"></i></button>
			</td>
			<td></td>
		</tr>
	{{/data.PO_ITEM_LIST}}
</tbody>
<!--
<input type="button" class="btn btn-default" value="回上頁 - 其他預訂單" onclick="formEvent.backBidList()" />

-->
</script>