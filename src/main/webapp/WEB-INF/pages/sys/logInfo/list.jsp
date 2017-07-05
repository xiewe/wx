<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/> 

<own:paginationForm action="${contextPath }/security/logInfo/list" page="${page }">
	<input type="hidden" name="search_EQ_username" value="${param.search_EQ_username }"/>
	<input type="hidden" name="search_EQ_ipAddress" value="${param.search_EQ_ipAddress }"/>
	<input type="hidden" name="search_EQ_logLevel" value="${param.search_EQ_logLevel }"/>
	<input type="hidden" name="search_GTE_createTime" value="${param.search_GTE_createTime}"/>
	<input type="hidden" name="search_LTE_createTime" value="${param.search_LTE_createTime}"/>
</own:paginationForm>

<form method="post" action="${contextPath}/security/logInfo/list" onsubmit="return navTabSearch(this)">
	<div class="pageHeader">
		<div class="searchBar" id="commonSearchDiv">
			<ul class="searchContent">
				<li>
					<label style="width: 100px;">登录名称：</label>
					<input type="text" name="search_EQ_username" value="${param.search_EQ_username }"/>
				</li>	
				<li>
					<label style="width: 100px;">登录IP：</label>
					<input type="text" name="search_EQ_ipAddress" value="${param.search_EQ_ipAddress }"/>
				</li>
				<li>
					<label style="width: 100px;">日志等级：</label>
					<select name="search_EQ_logLevel">
						<option value="">所有</option>
						<c:forEach var="logLevel" items="${logLevels }"> 
							<option value="${logLevel}" ${param.search_EQ_logLevel == logLevel ? 'selected="selected"':'' }>${logLevel}</option>
						</c:forEach>
					</select>
				</li>																
			</ul>
			<ul class="searchContent">	
				<li>
					<label style="width: 100px;">日志开始时间：</label>
					<input type="text" name="search_GTE_createTime" class="date" readonly="readonly" style="float:left;" value="${param.search_GTE_createTime}"/>
					<a class="inputDateButton" href="javascript:;" style="float:left;">选择</a>
				</li>			
				<li>
					<label style="width: 100px;">日志结束时间：</label>
					<input type="text" name="search_LTE_createTime" class="date" readonly="readonly" style="float:left;" value="${param.search_LTE_createTime}"/>
					<a class="inputDateButton" href="javascript:;" style="float:left;">选择</a>
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
			<shiro:hasPermission name="LogInfo:delete">
				<li><a class="delete" target="selectedTodo" rel="ids" href="${contextPath}/security/logInfo/delete" title="确认要删除?"><span>删除日志</span></a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	
	<table class="table" layoutH="162" width="100%">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
				<th orderField="username" class="${page.orderField eq 'username' ? page.orderDirection : ''}">登录名称</th>
				<th orderField="logLevel" class="${page.orderField eq 'logLevel' ? page.orderDirection : ''}">日志等级</th>
				<th orderField="message" class="${page.orderField eq 'message' ? page.orderDirection : ''}">日志内容</th>
				<th orderField="ipAddress" class="${page.orderField eq 'ipAddress' ? page.orderDirection : ''}">登录IP</th>
				<th orderField="createTime" class="${page.orderField eq 'createTime' ? page.orderDirection : ''}">创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${logInfos}">
			<tr target="slt_uid" rel="${item.id}">
				<td><input name="ids" value="${item.id}" type="checkbox"></td>
				<td>${item.username}</td>
				<td>${item.logLevel}</td>
				<td>${item.message}</td>
				<td>${item.ipAddress}</td>
				<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>			
			</c:forEach>
		</tbody>
	</table>
	<!-- 分页 -->
	<own:pagination page="${page }"/>
</div>