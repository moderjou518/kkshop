<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script id='hbt_ItemList' type='text/x-handlebars-template'>
<option value="*">-請選擇-</option>
	{{#data.ITEM_LIST}}
		<option value="{{itemCode}}">{{itemAbbr}}</option>
	{{/data.ITEM_LIST}}
</script>

<script id='hbt_VendorList' type='text/x-handlebars-template'>
<option value="*">-請選擇-</option>
	{{#data.VENDOR_LIST}}
		<option value="{{miscDesc}}">{{miscDesc}}</option>
	{{/data.VENDOR_LIST}}
</script>


<script id='hbt_QueryResult' type='text/x-handlebars-template'>
  <tbody>
    {{#data.TRNS_LIST}}
	<tr data-rowidx="trIndex{{@index}}">		
		<td nowrap>			
			<input type="hidden" name="txtTrnsDate" value='{{trnsDate}}' class="form-control readonly-noBG" style="text-align:center" readonly />{{trnsDate}} ({{weekDay}})
		</td>
		<!--<td nowrap>
			<input type="text" name="txtTrnsSeq" value='{{trnsSeq}}' style="width:100%" class="form-control readonly-noBG" readonly />			
		</td>-->                   		
		<td nowrap>
			<input type="text" name="txtVendNo" value="{{vendNo}}" class="form-control readonly-noBG" readonly />							
			<input type="hidden" name="txtCardId" value="{{cardId}}" class="form-control readonly-noBG" readonly />
			<input type="hidden" name="txtTrnsNote" value="{{trnsNote}}" class="form-control readonly-noBG" readonly />
			<input type="hidden" name="txtTrnsUuid" value="{{trnsUuid}}" />			
		</td>		
		<td nowrap>
			<input type="text" name="txtItemName" value='{{itemAbbr}}' style="width:100%" class="form-control readonly-noBG" readonly />
			<input type="hidden" name="txtTrnsItem" value='{{trnsItem}}' style="width:100%" class="form-control readonly-noBG" readonly />			
		</td>
		<td nowrap>
			<input type="text" name="txtTrnsQty" value='{{trnsQty}}'  style="text-align:right" class="form-control readonly-noBG" readonly />			
		</td>
		<td nowrap>
			<input type="text" name="txtTrnsAmt" value='{{trnsAmt}}'  style="text-align:right" class="form-control readonly-noBG" readonly />			
		</td>
		<td nowrap style="width:1%">
			<button type="button" title="修改" onclick="formEvent.showData(this)" class="btn btn-plus-icon btn-secondary btn-save-icon"><i class="fas fa-pencil-alt"></i></button>
		</td>
	</tr>
    {{/data.TRNS_LIST}}
  </tbody>
</script>