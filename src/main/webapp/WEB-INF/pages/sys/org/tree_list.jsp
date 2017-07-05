<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/>  
<div class="pageContent">
	<div class="tabs">
		<div class="tabsContent">
			<div>
				<div layoutH="5" id="jbsxBox2organizationTree" style="float:left; display:block; overflow:auto; width:300px; border:solid 1px #CCC; line-height:21px; background:#fff">
					<c:import url="/security/organization/tree"/>
				</div>
				
				<div layoutH="0" id="jbsxBox2organizationList" class="unitBox" style="margin-left:306px;">
					<c:import url="/security/organization/list/1"/>
				</div>
			</div>
		</div>
	</div>
</div>