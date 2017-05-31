<jsp:directive.page contentType="text/html;charset=UTF-8" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/> 
<div class="pageContent">
<form method="post" action="${contextPath}/security/user/create" class="required-validate pageForm" onsubmit="return validateCallback(this,navTabAjaxDone);">
	<div class="pageFormContent nowrap" layoutH="97">
		<fieldset>
			<legend>必填信息</legend>
			<dl>
				<dt>登录名称：</dt>
				<dd>
					<input type="text" name="username" class="required" minlength="6" maxlength="20" alt="登录名"/>					
					<span class="info">字母、数字、下划线 6-20位</span>
				</dd>
			</dl>
			<dl>
				<dt>登录密码：</dt>
				<dd>
					<input type="password" id="plainPassword" name="plainPassword" class="required" minlength="6" maxlength="20" minlength="6" maxlength="20" alt="6-20位字符"/>					
					<span class="info"> 6-20位字符</span>
				</dd>
			</dl>
			<dl>
				<dt>确认密码：</dt>
				<dd>
					<input type="password" name="repassword" class="required" equalto="#plainPassword"/>					
					<span class="info"> 6-20位字符</span>
				</dd>
			</dl>
		</fieldset>
		
		<fieldset>
			<legend>可选</legend>
			<dl class="nowrap">
				<dt>电话：</dt>
				<dd><input type="text" name="phone" class="phone" maxlength="32"/></dd>
			</dl>
			<dl class="nowrap">
				<dt>邮箱：</dt>
				<dd><input type="text" name="email" class="email" maxlength="128"/></dd>
			</dl>
			<dl class="nowrap">
				<dt>用户状态：</dt>
				<dd>
					<select name="status">
						<option value="0">可用</option>
						<option value="1">不可用</option>
					</select>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>关联组织：</dt>
				<dd>
					<input name="organization.id" type="hidden"/>
					<input name="organization.name" type="text" readonly="readonly" style="width: 140px;"/>
					<a class="btnLook" href="${contextPath}/security/user/lookup2org" lookupGroup="organization" title="关联组织" width="400">查找带回</a>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注信息：</dt>
				<dd><textarea name="textarea5" cols="80" rows="5" maxlength="128"></textarea></dd>
			</dl>
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