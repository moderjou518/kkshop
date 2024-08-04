<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script id='hbt_ItemClassOptions4Select' type='text/x-handlebars-template'>  
  <option value="*">全部</option>
    {{#data.ITEMCLASS}}
      <option value="{{miscCode}}">{{miscDesc}}</option>
    {{/data.ITEMCLASS}}
</script>

<script id='hbt_ItemClassOptions' type='text/x-handlebars-template'>
<div class="col-md-12" style="margin-bottom: 10px;margin-left: 0px; margin-right: 0px;">
	<button type="button" name="btnQuickQuery" onclick="formEvent.quickQuery('C')" class="btn btn-plus btn-info form-control">
	客戶
	</button>
<button type="button" name="btnQuickQuery" onclick="formEvent.quickQuery('E')" class="btn btn-plus btn-info form-control">
	員工
	</button>
</div>  
</script>

<script id='hbt_QueryResult' type='text/x-handlebars-template'>
  <tbody>
    {{#data.EMP_LIST}}
	<tr data-rowidx="trIndex{{@index}}">
		<td nowrap>
			<input type="text" name="txtHac0BascNote" value='{{hac0BascNote}}' autocomplete="off" class="form-control readonly-noBG" readonly />			
		</td>			
		<td nowrap>
			<input type="text" name="txtHac0BascLoginId" value='{{hac0BascLoginId}}' autocomplete="off" class="form-control readonly-noBG" readonly />			
		</td>
		<td nowrap>								
			<input type="text" name="txtHac0BascName" value="{{hac0BascName}}" maxlength="30" autocomplete="off" style="width:100%" class="form-control readonly-noBG" readonly />
		</td>
		<td class="text-center" nowrap>{{hac0VoidMark}}</td>
		<td nowrap style="width:1%">
			<button type="button" title="修改" onclick="formEvent.showData(this)" class="btn btn-plus-icon btn-secondary btn-save-icon"><i class="fas fa-pencil-alt"></i></button>
			<input type="hidden" name="txtHac0BascAcCode" value='{{hac0BascAcCode}}' />
			<input type="hidden" name="txtHac0BascPwd" value="{{hac0BascPwd}}" />
			<input type="hidden" name="txtHac0BascGroup" value="{{hac0BascGroup}}" />
			<input type="hidden" name="txtHac0BascNote" value="{{hac0BascNote}}" />
			<input type="hidden" name="txtVoidMark" value="{{hac0VoidMark}}"  />
		</td>
		<td></td>
	</tr>
    {{/data.EMP_LIST}}
  </tbody>
</script>