<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/> 
<div class="pageContent">
	<div class="pageFormContent" layoutH="68">
		<fieldset>
		<legend>模块信息</legend>	
		<p>
			<label>名称：</label>${permission.name }
		</p>	
		<p>
			<label>URL：</label>${permission.url }
		</p>		
		<p>
			<label>授权名称：</label>${permission.sn }
		</p>
		<p class="nowrap">
			<label>描述：</label>${permission.description }
		</p>				
		</fieldset>
		<c:if test="${permission.category == 1}">
		<fieldset>
		<legend>自定义授权</legend>
			<c:forEach var="p" items="${permission.children }" varStatus="s">
				<fieldset>
				<legend>${s.count }.${p.name }</legend>
					<p>
						<label style="width: 150px;">授权名称：${p.sn }</label>描述：${p.description }
					</p>
				</fieldset>			
			</c:forEach>		
		</fieldset>
		</c:if>
	</div>
			
	<div class="formBar">
		<ul>
			<li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
		</ul>
	</div>
</div>