<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/>
<style>

.priority_input {
	border:none;border-bottom:1px solid gray;background:none;width:50px;height:22px;padding: 0px;float: left;margin-right: 10px;
}

</style>
<script type="text/javascript">

jQuery(document).ready(function(){
     
    $(".assignRole").click(function(){
    	var roleId = $(this).attr("id").split("submit_")[1];
    	var $roleRow = $("#userRoleRow_" + roleId);
    	    
    	jQuery.ajax({
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
            url: '${contextPath}/security/user/create/userRole?user.id=${userId}&role.id=' + roleId ,
            error: function() { 
            	 alertMsg.error('分配角色失败！');
            },
            success: function() { 
            	// 删除已分配
				var $remove = $roleRow.remove();
	        	var roleName = $remove.find("td").eq(0).text();
		    	// 添加分配
				$("#hasRoles").append("<tr><td>" + roleName + "</td></tr>");
				$('tr[class="selected"]', getCurrentNavtabRel()).find("td").eq(5).find("div").append(roleName + "&nbsp;&nbsp;&nbsp;");
    		}		
        });	
    });
    
});

</script>
<div class="pageContent" layoutH="0" >

	<fieldset>
		<legend>已分配角色</legend>
		<table class="list" width="100%">
			<thead>
				<tr>
					<th>角色名称</th>
				</tr>
			</thead>
			<tbody id="hasRoles">
				<c:forEach var="item" items="${userRoles}">
				<tr>
					<td>${item.role.name}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</fieldset>
	<fieldset>
		<legend>可分配角色</legend>
		<table class="list" width="100%">
			<thead>
				<tr>
					<th width="60%">角色名称</th>
					<th width="40%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${roles}">
				<tr id="userRoleRow_${item.id}">
					<td>${item.name}</td>
					<td>
						<div class="button"><div class="buttonContent"><button id="submit_${item.id}" class="assignRole">分配</button></div></div>
					</td>
				</tr>	
				</c:forEach>
			</tbody>
		</table>
	</fieldset>
</div>