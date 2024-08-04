<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script id='hbt_ItemClassOptions4Select' type='text/x-handlebars-template'>  
  <option value="*">全部</option>
    {{#data.ITEMCLASS}}
      <option value="{{miscCode}}">{{miscDesc}}</option>
    {{/data.ITEMCLASS}}
</script>

<script id='hbt_YearList' type='text/x-handlebars-template'>  
    {{#data.YEAR_LIST}}
		<option value="{{miscCode}}">{{miscCode}}</option>
	{{/data.YEAR_LIST}}
</script>

<script id='hbt_poList' type='text/x-handlebars-template'>
<tbody>
<!-- 表頭: 當周所有物料(金額>0) --> 
	<tr>  
		<td style="text-align: center; width:1%" nowrap>
			<!--{{data.SHORT_DATE}}<br/>{{data.PODATEOFWEEK}}-->
			客戶 / 訂單日期
		</td>
			{{#data.BID_ITEM_LIST}}						
		<th style="text-align: center" nowrap>{{itemAbbr}}</th>			      
			{{/data.BID_ITEM_LIST}}
		<td style="text-align: center; width:1%" nowrap>
			
		</td>
	</tr>

<!-- 報表統計 -->
{{#data.CUST_LIST}}
<tr>						
	<td style="text-align: center" nowrap>		
		<input type="text" value="{{#neq poddSumRcvdQty 0}}V{{/neq}}" size="1" readonly class="readonly-noBG" style="text-align:center" />
		<input type="button" value="{{hac0BascName}}" onclick="formEvent.showIssueModal('{{pommDate}}', '{{pommSeq}}', '{{pommBuyer}}', '{{hac0BascName}}')" class="btn btn-default" />
		/ {{pommDate}}
	</td>
	{{#neq poddSumRcvdQty 0}}
		{{#CUST_ITEM_ARY}}
		<td style="text-align: center">{{itemRcvdQty}}</td>
		{{/CUST_ITEM_ARY}}
	{{else}}
		{{#CUST_ITEM_ARY}}
		<td style="text-align: center">{{itemQty}}</td>
		{{/CUST_ITEM_ARY}}
	{{/neq}}
	<td style="text-align: center" nowrap>			
		<input type="button" value="{{hac0BascName}}" onclick="formEvent.showIssueModal('{{pommDate}}', '{{pommSeq}}', '{{pommBuyer}}', '{{hac0BascName}}')" class="btn btn-default">
		<input type="text" value="{{#neq poddSumRcvdQty 0}}V{{/neq}}" size="1" readonly class="readonly-noBG" style="text-align:center" />
	</td>
</tr>
{{/data.CUST_LIST}}

<!-- 表尾: 數量加總 -->
<tr>  
	<td style="text-align: center"></td>
{{#data.BID_ITEM_LIST}}						
	<th style="text-align: center" nowrap>{{itemAbbr}}</th>			      
{{/data.BID_ITEM_LIST}}
	<td style="text-align: center"></td>
</tr>
<tr>  
	<td style="text-align: center" nowrap>訂購加總</td>
{{#data.BID_ITEM_LIST}}						
	<td style="text-align: center" nowrap>{{poOrdQtySum}}</td>			      
{{/data.BID_ITEM_LIST}}
	<td style="text-align: center" nowrap>訂購加總</td>
</tr>
<tr>  
	<td style="text-align: center" nowrap>已出貨</td>
{{#data.BID_ITEM_LIST}}						
	<td style="text-align: center" nowrap><u>{{poRcvdQtySum}}</u></td>			      
{{/data.BID_ITEM_LIST}}
	<td style="text-align: center" nowrap>已出貨</td>
</tr>
</tbody>
 
</script>



<script id='hbt_QueryResult' type='text/x-handlebars-template'>
  <tbody>
	<tr style="display: none;" data-evg-atrow="add">
		<td nowrap>{{bidUuid}}
			自動取號			
		</td>
		<td nowrap>
			<input type="text" name="txtBidName" value='{{bidName}}' maxlength="30" autocomplete="off" style="width:100%" />			
		</td>
		<td nowrap>								
			<input type="text" name="txtBidSaleSdate" maxlength="8" requried autocomplete="off" size="8" />~
			<input type="text" name="txtBidSaleEdate" maxlength="8" requried autocomplete="off" size="8" />
		</td>
		<td nowrap style="text-align:center">			
			<input type="text" name="txtBidOrdSdate" maxlength="8" style="width:80px" readonly class="readonly-noBG" />~
			<input type="text" name="txtBidOrdEdate" maxlength="8" style="width:80px" readonly class="readonly-noBG" />
		</td>
		<td nowrap style="text-align:center">新增</td>		
		<td nowrap>
			<label><input type="checkbox" name="txtBidSaleMark" style="zoom:150%" value="Y" checked/>自動</label>
		</td>		
		<td nowrap style="text-align:center" colspan="2">新增</td>
	</tr>
    {{#data.BIDMLIST}}
	<tr data-rowidx="trIndex{{@index}}">
		<td nowrap>{{bidUuid}}
			<input type="hidden" name="txtBidUuid" value="{{bidUuid}}" />
			<input type="hidden" name="txtBidName" value='{{bidName}}' maxlength="30" autocomplete="off" style="width:100%" />			
		</td>
		<td nowrap style="text-align:center">
			{{bidOrdSdate}}~{{bidOrdEdate}}
			<input type="hidden" name="txtBidOrdSdate" value="{{bidOrdSdate}}" maxlength="8" required autocomplete="off" size="10" readonly class="readonly-noBG"  />
			<input type="hidden" name="txtBidOrdEdate" value="{{bidOrdEdate}}" maxlength="8" required autocomplete="off" size="10" readonly class="readonly-noBG"  />
		</td>		
		<td nowrap>								
			<input type="text" name="txtBidSaleSdate" value="{{bidSaleSdate}}" maxlength="8" autocomplete="off" size="10" />~
			<input type="text" name="txtBidSaleEdate" value="{{bidSaleEdate}}" maxlength="8" autocomplete="off" size="10" />
		</td>
		
		<td nowrap>
			<label><input type="checkbox" name="txtBidSaleMark" style="zoom:150%" value="Y" {{#eq "Y" bidSaleMark}}checked{{/eq}}/>自動</label>
		</td>
		
		<!--
		<td nowrap style="text-align:center" nowrap>		
			<button type="button" id="btnSettingPrice" onclick="formEvent.loadBidPrice('trIndex{{@index}}')" class="btn btn-plus btn-default"   title='設定價格' >設定價格</button>                                                                       
		</td>
		
		<td style="text-align:center" nowrap>	
			<button type="button" onclick="formEvent.selectWeek('{{bidUuid}}', '{{bidOrdSdate}}', '{{bidOrdEdate}}')" class="btn btn-plus btn-default" title="訂單查詢">查詢訂單</button>
		</td>
		
		<td style="text-align:center">			
			<button type="button" onclick="formEvent.deleteThisRow(this)" class="btn btn-plus-icon btn-default btn-delete-icon" title="Remove"><i class="fas fa-trash"></i></button>
		</td>
		<td nowrap style="text-align:center">
			<i class="fas fa-info-circle" title="{{itemModUser}}-{{itemModTime}}"></i>
		</td>
		-->
	</tr>
    {{/data.BIDMLIST}}
  </tbody>
</script>


<script id='hbt_QuerySubResult' type='text/x-handlebars-template'>
  <tbody>
    {{#data.BIDDLIST}}
	<tr data-rowidx="trIndex{{@index}}">
		<td nowrap>
			<input type="text" name="txtItemAbbr" value="{{itemAbbr}}" readonly class="readonly-noBG" size="10" />
		</td>
		<td nowrap>			
			<input type="text" name="txtBiddPrice" value="{{biddPrice}}" maxlength="8" required autocomplete="off" style="width:80px" class="text-right" />			
		</td>
		<td nowrap>
			{{@index}}-<input type="text" name="txtBiddItem" value="{{biddItem}}" readonly class="readonly-noBG" size="7" />
			<input type="hidden" name="txtBiddUuid" value="{{biddUuid}}" />
			<input type="hidden" name="txtBidmUuid" value="{{bidmUuid}}" />
			<input type="hidden" name="txtBiddComp" value="{{biddComp}}" />
		</td>
	</tr>
    {{/data.BIDDLIST}}
  </tbody>
</script>