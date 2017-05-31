<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/> 
<script type="text/javascript">

jQuery(document).ready(function(){
	var $fieldset = $("#newPermissonInput");
    
    var $name = $("input[name=_name]",$fieldset);
    var $sn = $("input[name=_sn]",$fieldset);
    var $description = $("input[name=_description]",$fieldset);
    
    $("#newPermission").click(function(event){
        var nameValidate = !$name.validationEngine('validate');
        var snValidate = !$sn.validationEngine('validate');
        var descriptionValidate = !$description.validationEngine('validate');
        
     	// 验证
        if (!nameValidate || !snValidate || !descriptionValidate) {
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

function hideExtendInfo(isHide)
{
	if(isHide)
	{
		document.getElementById("extendInfo").style.display = "none";		
	}else
	{
		document.getElementById("extendInfo").style.display = "inline";
	}
}

</script>
<a id="refreshJbsxBox2moduleTree" rel="jbsxBox2moduleTree" target="ajax" href="${contextPath}/security/permission/tree" style="display:none;"></a>
<div class="pageContent">
<form id="permissionForm" method="post" action="${contextPath }/security/permission/create" class="required-validate pageForm" onsubmit="">
	<input type="hidden" name="parent.id" value="${parentModuleId }"/>
	<div id="permissionFormContent" class="pageFormContent" layoutH="58">
		<fieldset>
		<legend>类型</legend>
			<div>		
			功能模块<input type="radio" name="category" value="1" checked="checked" onclick="hideExtendInfo(false);"/>&nbsp;&nbsp;
			文件夹<input type="radio" name="category" value="0" onclick="hideExtendInfo(true);"/>&nbsp;&nbsp;			
			</div>		
		</fieldset>
		<fieldset>
		<legend>模块信息</legend>	
		<p>
			<label>名称：</label>
			<input type="text" name="name" class="input-medium validate[required,maxSize[64]] required" maxlength="64"/>
		</p>
		<!-- 
		<p>
			<label>优先级：</label>
			<input type="text" name="priority" class="validate[required,custom[integer],min[1],max[999]] required" value="999" maxlength="3" style="width: 80px;"/>
			<span class="info">（越小越靠前）</span>
		</p>
		-->			
		<p>
			<label>URL：</label>
			<input type="text" name="url" class="input-medium validate[required,maxSize[256]] required" maxlength="256" alt="以#、/或者http开头"/>
		</p>		
		<p>
			<label>授权KEY：</label>
			<input type="text" name="sn" class="input-medium validate[required,maxSize[32]] required" maxlength="32" alt="Example:xxxx:*"/>
		</p>
		<p>
			<label>类名：</label>
			<input type="text" name="className" class="required" minlength="6" maxlength="100" alt="包名+类名"/><span class="info">
		</p>				
		<p class="nowrap">
			<label>描述：</label>
			<textarea name="description" cols="29" rows="3" maxlength="256" class="input-medium textarea-scroll"></textarea>
		</p>		
		</fieldset>
		
		<div id="extendInfo">
		<fieldset>
		<legend>基本操作</legend>
			<div class="toNewPermission">		
			增(save)<input type="checkbox" name="children[0].sn" value="save" checked="checked" rel="0"/>&nbsp;&nbsp;
	         <input type="hidden" name="children[0].name" value="增" rel="0"/>
			删(delete)<input type="checkbox" name="children[1].sn" value="delete" checked="checked" rel="1"/>&nbsp;&nbsp;
			<input type="hidden" name="children[1].name" value="删" rel="1"/>
			改(edit)<input type="checkbox" name="children[2].sn" value="edit" checked="checked" rel="2"/>&nbsp;&nbsp;
	         <input type="hidden" name="children[2].name" value="改" rel="2"/>
			查(view)<input type="checkbox" name="children[3].sn" value="view" checked="checked" rel="3"/>&nbsp;&nbsp;
			<input type="hidden" name="children[3].name" value="查" rel="3"/>			
			</div>		
		</fieldset>
		<fieldset id="newPermissonInput">
		<legend>自定义扩展</legend>
			<p>
				<label>名称：</label>
				<input type="text" name="_name" class="input-medium validate[required,maxSize[64]] required" maxlength="64"/>
			</p>
			<p>
				<label>授权KEY：</label>
				<input type="text" name="_sn" class="input-medium validate[required,maxSize[32]] required" maxlength="32"/>
			</p>
			<p>
				<label>描述：</label>
				<input type="text" name="_description" class="input-medium" maxlength="256"/>
			</p>				
			<div class="button"><div class="buttonContent"><button id="newPermission">新增</button></div></div>		
		</fieldset>
		</div>
		
	</div>	
				
	<div class="formBar">
		<ul>
			<li><div class="button"><div class="buttonContent"><button type="submit">确定</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
		</ul>
	</div>
</form>
</div>