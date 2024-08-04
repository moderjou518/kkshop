<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- 星期 -->
<script id='hbt_weekDayList' type='text/x-handlebars-template'>
{{#data.WEEK_DAY_LIST}}						
	<button type="button" name="btnQuickDay" data-po-date="{{WEEK_DATE}}" 
		class="btn {{#eq 'Y' TODAY_MARK}}btn-warning{{else}}btn-default{{/eq}}" 
		style="{{#eq 'Y' TODAY_MARK}}font-weight: bold{{/eq}} {{#eq '六' WEEK_DAY}}color:red{{/eq}} {{#eq '日' WEEK_DAY}}color:red{{/eq}}" 
		onclick="formEvent.quickQuery(this)">{{WEEK_DAY}}
	</button>			      
{{/data.WEEK_DAY_LIST}}
</script>

<script id='hbt_YearList' type='text/x-handlebars-template'>  
    {{#data.YEAR_LIST}}
		<option value="{{miscCode}}">{{miscCode}}</option>
	{{/data.YEAR_LIST}}
</script>


<script id='hbt_CustPO' type='text/x-handlebars-template'>
<tr buyerID="{{data.CUST_PO.buyer}}">
<td style="text-align: left" nowrap>
		!!!!!<input type="button" value="{{data.CUST_PO.hac0BascName}}" onclick="formEvent.showIssueModal('{{data.CUST_PO.bidUUID}}', '{{data.CUST_PO.pommDate}}', '{{data.CUST_PO.pommSeq}}', '{{data.CUST_PO.hac0BascLoginId}}', '{{data.CUST_PO.hac0BascName}}')" class="btn btn-default" style="width:90px" />
	</td>
	
{{#data.CUST_ITEM_ARY}}
	<td style="text-align: center">{{itemQty}}</td>
	{{/data.CUST_ITEM_ARY}}

	<td style="text-align: left" nowrap>			
		<input type="button" value="{{data.CUST_PO.hac0BascName}}" onclick="formEvent.showIssueModal('{{data.CUST_PO.bidUUID}}', '{{data.CUST_PO.pommDate}}', '{{data.CUST_PO.pommSeq}}', '{{data.CUST_PO.hac0BascLoginId}}', '{{data.CUST_PO.hac0BascName}}')" class="btn btn-default" style="width:90px">
		<!--有預訂&有出貨
		<input type="text" value="出" size="3" readonly class="readonly-noBG" style="text-align:center" />
-->
	</td>	
</tr>
</script>




<script id='hbt_reportTime' type='text/x-handlebars-template'>
<tbody><tr><td>報表時間: {{data.REPORT_TIME}}</td></tr></tbody>
</script>

<script id='hbt_poList' type='text/x-handlebars-template'>
<tbody>

<!-- 表頭: 當周所有物料(金額>0) --> 
	<tr>  
		<th style="text-align: right;width:5%" nowrap>
			<h1 class="text-danger text-bold text-center">{{data.SHORT_DATE}}</h1>
			<h1 class="text-danger text-bold text-center">{{data.PODATEOFWEEK}}</h1>
		</th>

{{#data.BID_ITEM_LIST}}						
		<th style="line-height:1;text-orientation: upright;text-align: top; width:1px;">
			{{itemAbbr}}<br/>{{#eq itemHidePriceMk 'Y'}}{{else}}<label class="text-danger">{{ipUnitPrice}}</label>{{/eq}}
		</th>			      
{{/data.BID_ITEM_LIST}}

		<th style="text-align: left;width:5%" nowrap>
			<h1 class="text-danger text-bold text-center">{{data.SHORT_DATE}}</h1>
			<h1 class="text-danger text-bold text-center">{{data.PODATEOFWEEK}}</h1>
		</th>
	</tr>

<!-- 報表統計 -->
{{#data.CUST_LIST}}
<tr buyerID="{{hac0BascLoginId}}">						
	<td style="text-align: left" nowrap>
		{{#eq poOrdQtySum 0}}
			<!--沒預訂-->					
			{{#eq poddSumRcvdQty 1}}
				<!--沒預訂&有出貨-->				
				<input type="text" name="txtIssueMark" value="出" size="3" readonly class="readonly-noBG" style="text-align:center" />
			{{else}}
				<!--沒預訂&沒出貨-->
				<input type="text" name="txtIssueMark" value="休" size="3" readonly class="readonly-noBG text-danger text-center" style="text-align:center" />
			{{/eq}}			
			<input type="button" name="txtHac0BascName" value="{{hac0BascName}}" data-biduuid="{{bidUUID}}" data-pommDate="{{pommDate}}" data-pommSeq="{{pommSeq}}" data-loginID="{{hac0BascLoginId}}" data-loginName="{{hac0BascName}}" onclick="formEvent.showIssueModal(this)" class="btn btn-default text-danger" style="width:90px" />					
		{{else}}
			<!--有預訂&有出貨-->
			{{#neq poddSumRcvdQty 0}}			
				<input name="txtIssueMark" type="text" value="出" size="3" readonly class="readonly-noBG" style="text-align:center" />
			{{else}}
				<input name="txtIssueMark" type="text" value="" size="3" readonly class="readonly-noBG text-danger" style="text-align:center" />
			{{/neq}}
			<input type="button" name="txtHac0BascName" value="{{hac0BascName}}" data-biduuid="{{bidUUID}}" data-pommDate="{{pommDate}}" data-pommSeq="{{pommSeq}}" data-loginID="{{hac0BascLoginId}}" data-loginName="{{hac0BascName}}" onclick="formEvent.showIssueModal(this)" class="btn btn-default" style="width:90px" />									
		{{/eq}}		
	</td>
	{{#neq poddSumRcvdQty 0}}
		{{#CUST_ITEM_ARY}}
		<td style="text-align: center;text-decoration: underline;">{{#neq itemRcvdQty 0}}{{itemRcvdQty}}{{/neq}}</td>
		{{/CUST_ITEM_ARY}}
	{{else}}
		{{#CUST_ITEM_ARY}}
		<td style="text-align: center">{{#neq itemQty 0}}{{itemQty}}{{/neq}}</td>
		{{/CUST_ITEM_ARY}}
	{{/neq}}
	<td style="text-align: left" nowrap>			
		{{#eq poOrdQtySum 0}}
			<!--沒預訂-->			
			<input type="button" value="{{hac0BascName}}" onclick="formEvent.showIssueModal('{{bidUUID}}', '{{pommDate}}', '{{pommSeq}}', '{{hac0BascLoginId}}', '{{hac0BascName}}')" class="btn btn-default  text-danger" style="width:90px">
			{{#eq poddSumRcvdQty 1}}
				<!--沒預訂&有出貨-->				
				<input type="text" value="出" size="3" readonly class="readonly-noBG" style="text-align:center" />
			{{else}}
				<!--沒預訂&沒出貨-->
				<input type="text" value="休" size="3" readonly class="readonly-noBG text-danger text-center" style="text-align:center" />
			{{/eq}}					
		{{else}}			
			<input type="button" value="{{hac0BascName}}" onclick="formEvent.showIssueModal('{{bidUUID}}', '{{pommDate}}', '{{pommSeq}}', '{{hac0BascLoginId}}', '{{hac0BascName}}')" class="btn btn-default" style="width:90px">
			<!--有預訂&有出貨-->{{#neq poddSumRcvdQty 0}}
				<input type="text" value="出" size="3" readonly class="readonly-noBG" style="text-align:center" />
			{{/neq}}						
		{{/eq}}
	</td>
</tr>
{{/data.CUST_LIST}}

<!-- 表尾: 數量加總 -->
<tr>  
	<td style="text-align: center">		
		<h1 class="text-danger text-bold text-center">{{data.SHORT_DATE}}</h1>
		<h1 class="text-danger text-bold text-center">{{data.PODATEOFWEEK}}</h1>
	</td>
{{#data.BID_ITEM_LIST}}	
	<th style="line-height:1;text-orientation: upright;text-align: top; width:1px;">
		{{itemAbbr}}<br/>{{#eq itemHidePriceMk 'Y'}}{{else}}<label class="text-danger">{{ipUnitPrice}}</label>{{/eq}}
	</th>			      
{{/data.BID_ITEM_LIST}}
	<td style="text-align: center">
		<h1 class="text-danger text-bold text-center">{{data.SHORT_DATE}}</h1>
		<h1 class="text-danger text-bold text-center">{{data.PODATEOFWEEK}}</h1>
	</td>
</tr>
<tr>  
	<td style="text-align: right" nowrap>訂購加總</td>
{{#data.BID_ITEM_LIST}}						
	<td style="text-align: right" nowrap>{{poOrdQtySum}}</td>			      
{{/data.BID_ITEM_LIST}}
	<td style="text-align: left" nowrap>訂購加總</td>
</tr>
<tr>  
	<td style="text-align: right" nowrap>已出貨</td>
{{#data.BID_ITEM_LIST}}						
	<td style="text-align: right;text-decoration: underline" nowrap>{{poRcvdQtySum}}</td>			      
{{/data.BID_ITEM_LIST}}
	<td style="text-align: left" nowrap>已出貨</td>
</tr>
</tbody>
 
</script>

<script id='hbt_POItemList' type='text/x-handlebars-template'>			
<tbody>	
	{{#data.PO_ITEM_LIST}}
		<tr>
			<td nowrap style="text-align:left">
				<input type="text" readonly class="readonly-noBG text-right" value="{{itemAbbr}}" size="10" />
				<input type="hidden" name="txtPoddDate" value="{{poddDate}}" />
				<input type="hidden" name="txtPoddSeq"  value="{{poddSeq}}" />
				<input type="hidden" name="txtPoddItem" value="{{poddItem}}" />
			</td>				              
			<td nowrap style="text-align:right">				
				<input type="text" name="txtPoddOrdQty" maxlength="2" value="{{poddOrdQty}}" style="width:70px;text-align:center" readonly class="readonly-noBG"/>
			</td>
			<td nowrap style="text-align:center">                                                               
				<button type="button" name="btnMinusItem" onclick="formEvent.minusItem(this)" class="btn btn-large btn-default" title='減1'><i class="fas fa-minus"></i></button>
				<input type="number" min="0" max="99" name="txtPoddRcvdQty" onblur="formEvent.txtPoddRcvdQty_blur(this)" maxlength="2" value="{{poddRcvdQty}}" style="width:70px;text-align:center" {{#neq poddOrdQty '0'}}{{#eq poddRcvdQty poddOrdQty}}{{else}}class="has-error"{{/eq}}{{/neq}} />				
				<button type="button" name="btnPlusItem" onclick="formEvent.addItem(this)" class="btn btn-large btn-default" title='加1'><i class="fas fa-plus"></i></button>
			</td>
		</tr>
	{{/data.PO_ITEM_LIST}}
</tbody>
</script>

<script id='hbt_POAddItemList' type='text/x-handlebars-template'>			
<tbody>	
	{{#data.PO_ADD_ITEM_LIST}}
		<tr>
			<td nowrap style="text-align:left">
				<input type="text" readonly class="readonly-noBG text-right" value="{{itemAbbr}}" size="10" />
				<input type="hidden" name="txtPoddDate" value="{{poddDate}}" />
				<input type="hidden" name="txtPoddSeq"  value="{{poddSeq}}" />
				<input type="hidden" name="txtPoddItem" value="{{poddItem}}" />
			</td>				              
			<td nowrap style="text-align:right">				
				<input type="text" name="txtPoddOrdQty" maxlength="2" value="{{poddOrdQty}}" style="width:70px;text-align:center" readonly class="readonly-noBG text-danger"/>
			</td>
			<td nowrap style="text-align:center">                                                               
				<button type="button" name="btnMinusItem" onclick="formEvent.minusItem(this)" class="btn btn-large btn-default" title='減1'><i class="fas fa-minus"></i></button>
				<input type="number" min="0" max="99" name="txtPoddRcvdQty" onblur="formEvent.txtPoddRcvdQty_blur(this)" maxlength="2" value="{{poddRcvdQty}}" style="width:70px;text-align:center" {{#eq poddRcvdQty poddOrdQty}}{{else}}class="has-error"{{/eq}} />				
				<button type="button" name="btnPlusItem" onclick="formEvent.addItem(this)" class="btn btn-large btn-default" title='加1'><i class="fas fa-plus"></i></button>
			</td>
		</tr>
	{{/data.PO_ADD_ITEM_LIST}}
</tbody>
</script>

<!-- 換月份 -->
<script id=hbt_bidList type='text/x-handlebars-template'>
  <tbody>
{{#data.BIDLIST}}
	<tr data-rowidx="trIndex{{@index}}">
		<td nowrap>			
			<a href="#" onclick="formEvent.selectWeek('{{bidUuid}}','{{bidOrdSdate1}}','{{bidOrdEdate1}}');return false;">選擇</a>			
		</td>
		<td nowrap>			
			<input type="text" name="txtBidOrdSdate" value="{{bidOrdSdate1}}" readonly class="readonly-noBG" size="4" /> ~
			<input type="text" name="txtBidOrdEdate" value="{{bidOrdEdate1}}" readonly class="readonly-noBG" size="4" />
		</td>
		<td>
			<input type="hidden" name="txtBidUuid" value="{{bidUuid}}" />
			<input type="hidden" name="txtBidName" value='{{bidName}}' class="form-control readonly-noBG" readonly />			
		</td>		
				
	</tr>
{{/data.BIDLIST}}
</tbody>	
</script>
