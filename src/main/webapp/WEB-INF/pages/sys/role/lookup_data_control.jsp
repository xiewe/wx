<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/>

<own:paginationForm action="${contextPath }/security/role/lookup?rp.id=${param['rp.id'] }&prefix=${param.prefix }" page="${page }">
	<input type="hidden" name="search_LIKE_name" value="${param.search_LIKE_name}"/>
</own:paginationForm>

<form method="post" action="${contextPath }/security/role/lookup?rp.id=${param['rp.id'] }&prefix=${param.prefix }" onsubmit="return dialogSearch(this)">
	<div class="pageHeader">
		<div class="searchBar">
			<ul class="searchContent">
				<li>
					<label>名称：</label>
					<input type="text" name="search_LIKE_name" value="${param.search_LIKE_name}"/>			
				</li>
			</ul>
			<div class="subBar">
				<ul>						
					<li><div class="button"><div class="buttonContent"><button type="submit">搜索</button></div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="reset">重置</button></div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="button" multLookup="rolePermissionDataControls[]">选择带回</button></div></div></li>
				</ul>
			</div>
		</div>
	</div>
</form>

<div class="pageContent">	
	<table class="table" layoutH="118" targetType="dialog" width="100%">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="rolePermissionDataControls[]" class="checkboxCtrl"></th>
				<th width="200">名称</th>
				<th width="400">条件</th>
				<th>描述</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${dataControls}">
			<tr>
				<td><input type="checkbox" name="rolePermissionDataControls[]" value="{'dataControl.id':'${item.id }', 'rolePermission.id':'${param['rp.id'] }', 'dataControl.name':'${item.name }'}"></td>
				<td>${item.name}</td>
				<td>${item.operator}</td>
				<td>${item.description}</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	<!-- 分页 -->
	<own:pagination page="${page }" onchange="dialogPageBreak({numPerPage:this.value})" targetType="dialog"/>
</div>