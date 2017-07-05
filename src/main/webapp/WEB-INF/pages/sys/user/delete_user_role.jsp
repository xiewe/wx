<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/>
<script type="text/javascript">
jQuery(document).ready(function(){     
    $(".deleteUserRole").click(function(){
    	var userRoleId = $(this).attr("id").split("submit_")[1];
    	jQuery.ajax({
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
            url: '${contextPath}/security/user/delete/userRole/' + userRoleId,
            error: function() { 
    	 		alertMsg.error('撤销角色失败！');
    		},
    		success: function() { 
    	    	// 删除已分配
    	    	var $remove = $("#userRoleRow_" + userRoleId).remove();
	        	var roleName = $remove.find("td").eq(0).text();
		    	// 添加分配
				var	$div = $('tr[class="selected"]', getCurrentNavtabRel()).find("td").eq(5).find("div");
				var text = $div.text();
				$div.text(text.replace(roleName, ""));
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
					<th width="60%">角色名称</th>
					<th width="40%">操作</th>
				</tr>
			</thead>
			<tbody id="hasRoles">
				<c:if test="${empty userRoles }">
				<tr>
					<td colspan="2" align="center" style="color:red;">该用户还没有分配角色。</td>
				</tr>
				</c:if>
				<c:forEach var="item" items="${userRoles}">
				<tr id="userRoleRow_${item.id}">
					<td>${item.role.name}</td>
					<td>
						<div class="button"><div class="buttonContent"><button id="submit_${item.id}" class="deleteUserRole">撤销角色</button></div></div>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</fieldset>
</div>