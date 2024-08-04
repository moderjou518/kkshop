<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script id='hbt_QueryResult' type='text/x-handlebars-template'>
  <tbody>
    {{#data.POSTAT}}
	<tr data-rowidx="trIndex{{@index}}">		
		<td nowrap>{{itemName}}</td>
		<td nowrap>{{itemAbbr}}</td>                   		
		<td nowrap style="text-align:right" nowrap>{{trnsQtySum}}</td>
		<td nowrap style="text-align:right" nowrap>{{cacpQtySum}}</td>
		<td nowrap style="text-align:right" nowrap>{{poddOrdQtySum}}</td>
		<td nowrap style="text-align:right" nowrap>{{poddRcvdQtySum}}</td>
		<td nowrap style="text-align:right" nowrap>{{poddDiffQty}}</td>
		<td nowrap></td>
	</tr>
    {{/data.POSTAT}}
  </tbody>
</script>