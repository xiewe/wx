<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/>
<a id="refreshJbsxBox2organizationTree" rel="jbsxBox2organizationTree" target="ajax" href="${contextPath}/security/organization/tree" style="display:none;"></a>
<own:paginationForm action="${contextPath}/security/organization/list/${parentOrganizationId}" page="${page }" onsubmit="return divSearch(this, 'jbsxBox2organizationList');">
	<input type="hidden" name=search_LIKE_name value="${param.search_LIKE_name }"/>
</own:paginationForm>

<form method="post" action="${contextPath }/security/organization/list/${parentOrganizationId}" onsubmit="return divSearch(this, 'jbsxBox2organizationList');">
	<div class="pageHeader">
		<div class="searchBar" id="commonSearchDiv">
			<ul class="searchContent">
				<li>
					<label>组织名称：</label>
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
		<shiro:hasPermission name="Organization:save">
			<li><a iconClass="group_add" target="dialog" mask="true" width="530" height="260" href="${contextPath}/security/organization/create/${parentOrganizationId}"><span>添加组织</span></a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="Organization:edit">
			<li><a iconClass="group_edit" target="dialog" mask="true" rel="lookupParent2org_edit" width="530" height="260" href="${contextPath}/security/organization/update/{slt_uid}"><span>编辑组织</span></a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="Organization:delete">
			<li><a iconClass="group_delete" target="ajaxTodo" callback="dialogReloadRel2Org" href="${contextPath}/security/organization/delete/{slt_uid}" title="确认要删除该组织?"><span>删除组织</span></a></li>
		</shiro:hasPermission>	
		</ul>
	</div>
	<table class="table" layoutH="142" width="100%" rel="jbsxBox2organizationList" >
		<thead>
			<tr>
				<th>名称</th>
				<th>优先级</th>
				<th>描述</th>
				<th>父组织</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${organizations}">
			<tr target="slt_uid" rel="${item.id}">
				<td>${item.name}</td>				
				<td>${item.priority}</td>
				<td>${item.description}</td>
				<td>${item.parent.name}</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>

	<!-- 分页 -->
	<own:pagination page="${page }" rel="jbsxBox2organizationList" onchange="navTabPageBreak({numPerPage:this.value}, 'jbsxBox2organizationList')"/>
</div>