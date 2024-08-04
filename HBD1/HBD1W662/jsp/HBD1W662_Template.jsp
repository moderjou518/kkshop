<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script id='barTempCustYearReport' type='text/x-handlebars-template'>
<tbody>
{{#data.CUST_YEAR_REPORT}}
  <tr>    
    <td class="text-right" nowrap>{{buyerName}}</td>
    <td class="text-right"><input type="text" value="{{M01}}" name="txtM01Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>                    
    <td class="text-right"><input type="text" value="{{M02}}" name="txtM02Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>
    <td class="text-right"><input type="text" value="{{M03}}" name="txtM03Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>
    <td class="text-right"><input type="text" value="{{M04}}" name="txtM04Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>
    <td class="text-right"><input type="text" value="{{M05}}" name="txtM05Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>
    <td class="text-right"><input type="text" value="{{M06}}" name="txtM06Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>
    <td class="text-right"><input type="text" value="{{M07}}" name="txtM07Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>
    <td class="text-right"><input type="text" value="{{M08}}" name="txtM08Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>
    <td class="text-right"><input type="text" value="{{M09}}" name="txtM09Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>
    <td class="text-right"><input type="text" value="{{M10}}" name="txtM10Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>
    <td class="text-right"><input type="text" value="{{M11}}" name="txtM11Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>
    <td class="text-right"><input type="text" value="{{M12}}" name="txtM12Amt" readonly class="readonly-noBG txtAmt" size="7" /></td>
  </tr>
{{/data.CUST_YEAR_REPORT}}
</tbody>
</script>



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

