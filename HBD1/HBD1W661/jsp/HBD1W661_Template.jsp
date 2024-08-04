<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<script id='hbt_QueryHeader' type='text/x-handlebars-template'>
  <thead>    
	<tr data-rowidx="trIndex{{@index}}">
		<th nowrap>客戶</th>
		{{#data.monthDayList}}
		<th nowrap style="text-align:right">{{shortMonthDay}}</th>
		{{/data.monthDayList}}
		<th style="text-align:right">合計</th>				
	</tr>    
  </thead>
</script>

<script id='hbt_QueryFooter' type='text/x-handlebars-template'>
  <tfoot>
	<tr>
		<th nowrap style="text-align:right">合計</th>
		{{#data.dayTotalList}}
		<th nowrap style="text-align:right">{{dayTotalAmt}}</th>
		{{/data.dayTotalList}}
		<th class="text-danger" style="text-align:right;font-size:20px">{{data.monthTotalAmt}}</th>			
	</tr>
  </tfoot>
</script>

<script id='hbt_QueryResult' type='text/x-handlebars-template'>
  <tbody>
    {{#data.monthCustList}}
	<tr data-rowidx="trIndex{{@index}}">
		<td nowrap class="text-right">{{hac0BascName}}<input type="hidden" value='{{pommBuyer}}' /></td>
		{{#monthAmts}}
		<td nowrap style="text-align:right">{{dayAmt}}</td>
		{{/monthAmts}}
		<td nowrap style="text-align:right">{{custMonthTotalAmt}}</td>				
	</tr>
    {{/data.monthCustList}}
  </tbody>
</script>

<script id='hbt_DailyPoItemList' type='text/x-handlebars-template'>
<tbody>
{{#data.DAILY_PO}}
	<tr>
		<td>{{poDate}}</td>
		<td class="text-right">{{itemName}}</td>
		<td style="text-align:right">{{poddOrdQty}}</td>
		<td style="text-align:right">{{poddRcvdQty}}</td>
		<td style="text-align:right">{{poddAmt}}</td>
	</tr>
{{/data.DAILY_PO}}
</tbody>
</script>