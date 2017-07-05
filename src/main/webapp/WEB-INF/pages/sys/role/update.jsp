<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/> 

<div class="pageContent">
<form method="post" action="${contextPath }/security/role/update" class="required-validate pageForm" onsubmit="return validateCallback(this, dialogAjaxDone);">
	<input type="hidden" name="id" value="${role.id}"/>
	<div class="pageFormContent" layoutH="58">		
		<p>
			<label>名称：</label>
			<input type="text" name="name" readonly class="input-medium validate[required,maxSize[64]] required" maxlength="64" value="${role.name}"/>
		</p>			
		<p class="nowrap">
			<label>描述：</label>
			<textarea name="description" cols="29" rows="3" maxlength="256" class="input-medium textarea-scroll">${role.description}</textarea>
		</p>			
	</div>
			
	<div class="formBar">
		<ul>
			<li><div class="button"><div class="buttonContent"><button type="submit">确定</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
		</ul>
	</div>
</form>
</div>