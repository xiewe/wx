<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/pages/include.inc.jsp"%>
<script type="text/javascript">

jQuery(document).ready(function(){
	var $fieldset = $("#newPermissonInput");
    
    var $name = $("input[name=_name]",$fieldset);
    var $sn = $("input[name=_sn]",$fieldset);
    var $description = $("input[name=_description]",$fieldset);	
     
    $("#newPermission").click(function(event){
        var nameValidate = !$name.validationEngine('validate');
        var shortNameValidate = !$sn.validationEngine('validate');
        var descriptionValidate = !$description.validationEngine('validate');
        
     	// 验证
        if (!nameValidate || !shortNameValidate || !descriptionValidate) {
        	return false;
        }
        
        var $toNewPermission = $("div.toNewPermission");
        // 判断是否有定义权限
        var maxId = 0;
        if ($("input:last", $toNewPermission).length > 0) {
        	maxId = parseInt($("input:last", $toNewPermission).attr("rel")) + 1;	
        }
        
        $toNewPermission.append($name.val() + '(' + $sn.val() + ')' + '<input type="checkbox" name="children[' + maxId + '].sn" value="' + $sn.val() + '" checked="checked" rel="' + maxId + '"/>&nbsp;&nbsp;'); 
        $toNewPermission.append('<input type="hidden" name="children[' + maxId + '].name" value="' + $name.val() + '" rel="' + maxId + '"/>');
        $toNewPermission.append('<input type="hidden" name="children[' + maxId + '].description" value="' + $description.val() + '" rel="' + maxId + '"/>');
    
    	$name.val("");
    	$sn.val("");
    	$description.val("");
    	
    	event.preventDefault();
    	event.stopPropagation();
    });
    
     $("#permissionForm").submit(function(event){
         event.preventDefault();
    	 event.stopPropagation();
		
    	 var _nameClass = $name.attr("class");
    	 var _snClass = $sn.attr("class");
    	 var _descriptionClass = $description.attr("class");
    	 
    	 $name.attr("class", "");
    	 $sn.attr("class", "");
    	 
     	 var result = validateCallback(this, dialogReloadRel2Module);
     	 if (!result) {
     		$name.attr("class", _nameClass);
     		$sn.attr("class", _snClass);
     		$description.attr("class", _descriptionClass);
     	 }
    	 return result;
     });
});

</script>
<a id="refreshJbsxBox2moduleTree" rel="jbsxBox2moduleTree" target="ajax" href="${contextPath}/security/permission/tree" style="display:none;"></a>
<div class="pageContent">
<form id="permissionForm" method="post" action="${contextPath }/security/permission/update" class="required-validate pageForm" onsubmit="">
	<input type="hidden" name="id" value="${permission.id }"/>
	<input type="hidden" name="priority" value="${permission.priority }"/>
	<div id="permissionFormContent" class="pageFormContent" layoutH="58">
		<fieldset>
		<legend>模块信息</legend>	
		<p>
			<label>名称：</label>
			<input type="text" name="name" class="input-medium validate[required,maxSize[64]] required" maxlength="64" value="${permission.name }"/>
		</p>
		<!--  	
		<p>
			<label>优先级：</label>
			<input type="text" name="priority" class="validate[required,custom[integer],min[1],max[999]] required" maxlength="3" style="width: 80px;" value="${permission.priority }"/>
			<span class="info">（越小越靠前）</span>
		</p>
		-->		
		<p>
			<label>URL：</label>
			<input type="text" name="url" class="input-medium validate[required,maxSize[256]] required" maxlength="256" alt="以#、/或者http开头" value="${permission.url }"/>
		</p>		
		<p>
			<label>授权名称：</label>
			<input type="text" name="sn" class="input-medium validate[required,maxSize[32]] required" maxlength="32" value="${permission.sn }"/>
		</p>
		<p>
			<label>模块类名：</label>
			<input type="text" name="className" class="input-medium" maxlength="256" value="${permission.className }"/>
		</p>				
		<p class="nowrap">
			<label>描述：</label>
			<textarea name="description" cols="29" rows="3" maxlength="256" class="input-medium textarea-scroll">${permission.description }</textarea>
		</p>	
		<p>
			<label>父模块：</label>
			<input name="parent.id" value="${permission.parent.id }" type="hidden"/>
			<input class="required" name="parent.name" type="text" readonly="readonly" value="${permission.parent.name }" class="input-medium"/>
			<a class="btnLook" href="${contextPath}/security/permission/lookupParent/${permission.id}" lookupGroup="parent" mask="true" title="更改父模块" width="400">查找带回</a>			
		</p>			
		</fieldset>	
		
		<c:if test="${permission.category == 1}">	
		<fieldset>
		<legend>自定义授权</legend>
			<div class="toNewPermission">
			<c:forEach var="p" items="${permission.children}" varStatus="s">
			${p.name}(${p.sn})<input type="checkbox" name="children[${s.index}].sn" value="${p.sn}" checked="checked" rel="${s.index}"/>&nbsp;&nbsp;
				     <input type="hidden" name="children[${s.index}].id" value="${p.id}" rel="${s.index}"/>
				     <input type="hidden" name="children[${s.index}].parent.id" value="${p.parent.id}" rel="${s.index}"/>
					 <input type="hidden" name="children[${s.index}].name" value="${p.name}" rel="${s.index}"/>
					 <input type="hidden" name="children[${s.index}].description" value="${p.description}" rel="${s.index}"/>			
			</c:forEach>
			</div>		
		</fieldset>
		<fieldset id="newPermissonInput">
		<legend>动态新增</legend>
			<p>
				<label>名称：</label>
				<input type="text" name="_name" class="input-medium validate[required,maxSize[64]] required" maxlength="64"/>
			</p>
			<p>
				<label>操作名称：</label>
				<input type="text" name="_sn" class="input-medium validate[required,maxSize[32]] required" maxlength="32"/>
			</p>
			<p>
				<label>描述：</label>
				<input type="text" name="_description" class="input-medium" maxlength="256"/>
			</p>				
			<div class="button"><div class="buttonContent"><button id="newPermission">新增</button></div></div>		
		</fieldset>
		</c:if>
	</div>
			
	<div class="formBar">
		<ul>
			<li><div class="button"><div class="buttonContent"><button type="submit">确定</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
		</ul>
	</div>
</form>
</div>