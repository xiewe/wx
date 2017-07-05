<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/>

<script type="text/javascript">

var setting = {
	check: {
		enable: false
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};

var zNodes = ${permissionJson};

$(document).ready(function(){
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
});

</script>
<div class="pageContent">
	<div class="pageFormContent" layoutH="56">
	<dl>
		名称：${role.name }
	</dl>
	<dl>
		描述：${role.description }
	</dl>	
	<div class="divider"></div>
	
	<fieldset>		
	  <input type="hidden" name="id" value="${role.id}"/>
	  <input type="hidden" name="permissionJson" id="permissionJson" value=""/>
	  <ul id="treeDemo" class="ztree"></ul>	
	</fieldset>
	
	</div>
	
	<div class="formBar">
		<ul>
			<li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
		</ul>
	</div>
</div>