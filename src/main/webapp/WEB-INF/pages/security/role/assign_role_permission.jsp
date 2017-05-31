<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/>
<SCRIPT type="text/javascript">
	<!--
	var setting = {
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};

	var zNodes = ${permissionJson};
	
	var code;
	
	function setCheck() {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
		py = $("#py").attr("checked")? "p":"",
		sy = $("#sy").attr("checked")? "s":"",
		pn = $("#pn").attr("checked")? "p":"",
		sn = $("#sn").attr("checked")? "s":"",
		type = { "Y":py + sy, "N":pn + sn};
		zTree.setting.check.chkboxType = type;
		showCode('setting.check.chkboxType = { "Y" : "' + type.Y + '", "N" : "' + type.N + '" };');
	}
	function showCode(str) {
		if (!code) code = $("#code");
		code.empty();
		code.append("<li>"+str+"</li>");
	}
	
	$(document).ready(function(){
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		setCheck();
		$("#py").bind("change", setCheck);
		$("#sy").bind("change", setCheck);
		$("#pn").bind("change", setCheck);
		$("#sn").bind("change", setCheck);
	});
	//-->
	
	function getTree(form)
	{
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.getCheckedNodes(true);
		var treeJson = "[";
		for(var i=0;i<nodes.length;i++) {  
			if(nodes[i].isParent)
			{
				//check_Child_State
				//-1不存在子节点 或 子节点全部设置为 nocheck = true
				//0	无 子节点被勾选
				//1	部分 子节点被勾选
				//2	全部 子节点被勾选
				if(nodes[i].check_Child_State > 0)
				{
					// handle root node
					var parentId = nodes[i].pId;
					if(nodes[i].sn == "Root:*")
					{
						parentId = 0;//parentId不能为null，否则long from String会抛转换异常，设为0
					}
					treeJson += "{\"id\":\"" + nodes[i].id + "\",\"pId\":\""+ parentId + "\",\"name\":\"" + nodes[i].name + "\",\"sn\":\"" + nodes[i].sn + "\"}";
				}
				else
				{
					continue;
				}
			}
			else
			{
				treeJson += "{\"id\":\"" + nodes[i].id + "\",\"pId\":\""+ nodes[i].pId + "\",\"name\":\"" + nodes[i].name + "\",\"sn\":\"" + nodes[i].sn + "\"}";
			}
			if(i < nodes.length -1)
			{
				treeJson += ",";
			}
			//alert(nodes[i].id + nodes[i].name + "--" + nodes[i].check_Child_State + nodes[i].permission + nodes[i].isParent);  
        }  
		treeJson += "]";
		//alert(treeJson);
		document.getElementById("permissionJson").value = treeJson;
		return validateCallback(form, navTabAjaxDone);
	}
</SCRIPT>	
		
<div class="pageContent">
	<form class="form-horizontal" action="${contextPath}/security/role/saveAssignPermission" method="post" onsubmit="return getTree(this);">
	<div id="permissionFormContent" class="pageFormContent" layoutH="58">
		<fieldset>		
		  <input type="hidden" name="id" value="${role.id}"/>
		  <input type="hidden" name="permissionJson" id="permissionJson" value=""/>
		  <ul id="treeDemo" class="ztree"></ul>		  
			<div style="display: none;">
				<input type="checkbox" id="py" checked />
				<input type="checkbox" id="sy" checked />
				<input type="checkbox" id="pn" checked />
				<input type="checkbox" id="sn" checked />
				</div>	
			</fieldset>
	</div>
	
	<div class="formBar">
	<ul>
		<li><div class="button"><div class="buttonContent"><button type="submit">确定</button></div></div></li>
		<li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
	</ul>
	</div>
	</form>
</div>	
		
