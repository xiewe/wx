<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/> 
<a id="refreshJbsxBox2moduleTree" rel="jbsxBox2moduleTree" target="ajax" href="${contextPath}/security/permission/tree" style="display:none;"></a>
<own:paginationForm action="${contextPath}/security/permission/list/${parentModuleId}" page="${page }" onsubmit="return divSearch(this, 'jbsxBox2moduleList');">
	<input type="hidden" name=search_LIKE_name value="${param.search_LIKE_name }"/>
</own:paginationForm>

<form method="post" action="${contextPath }/security/permission/list/${parentModuleId}" onsubmit="return divSearch(this, 'jbsxBox2moduleList');">
	<div class="pageHeader">
		<div class="searchBar" id="commonSearchDiv">
			<ul class="searchContent">
				<li>
					<label>模块名称：</label>
					<input type="text" name=search_LIKE_name value="${param.search_LIKE_name }"/>
				</li>
			</ul>
			<div class="subBar">
				<ul>	
					<li><div class="button"><div class="buttonContent"><button type="button" onclick="clearAllSearchContent('commonSearchDiv');">清空</button></div></div></li>					
					<li><div class="button"><div class="buttonContent"><button type="submit">搜索</button></div></div></li>
				</ul>
			</div>
		</div>
	</div>
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
		<shiro:hasPermission name="Permission:view">
			<li><a iconClass="magnifier" target="dialog" width="540" height="560" mask="true" href="${contextPath }/security/permission/view/{slt_uid}"><span>查看模块</span></a></li>
		</shiro:hasPermission>		
		<shiro:hasPermission name="Permission:save">
			<li><a class="add" target="dialog" width="540" height="560" mask="true" href="${contextPath }/security/permission/create/${parentModuleId}"><span>添加模块</span></a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="Permission:edit">
			<li><a class="edit" target="dialog" rel="lookupParent2module_edit" width="540" height="560" mask="true" href="${contextPath }/security/permission/update/{slt_uid}"><span>编辑模块</span></a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="Permission:delete">
			<li><a class="delete" target="ajaxTodo" callback="dialogReloadRel2Module" href="${contextPath }/security/permission/delete/{slt_uid}" title="确认要删除该模块?"><span>删除模块</span></a></li>
		</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" layoutH="142" width="100%" rel="jbsxBox2moduleList" >
		<thead>
			<tr>
				<th>名称</th>
				<th>类名</th>
				<th>授权名称</th>
				<th>父模块</th>
				<th>模块地址</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${modules}">
			<tr target="slt_uid" rel="${item.id}">
				<td>${item.name}</td>
				<td>${item.className}</td>
				<td>${item.sn}</td>
				<td>${item.parent.name}</td>
				<td>${item.url}</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>

	<!-- 分页 -->
	<own:pagination page="${page }" rel="jbsxBox2moduleList" onchange="navTabPageBreak({numPerPage:this.value}, 'jbsxBox2moduleList')"/>
</div>