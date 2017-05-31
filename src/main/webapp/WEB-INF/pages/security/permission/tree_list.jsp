<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/>   
<div class="pageContent">
	<div class="tabs">
		<div class="tabsContent">
			<div>
				<div layoutH="5" id="jbsxBox2moduleTree" style="float:left; display:block; overflow:auto; width:300px; border:solid 1px #CCC; line-height:21px; background:#fff">
					<c:import url="/security/permission/tree"></c:import>
				</div>
				
				<div layoutH="0" id="jbsxBox2moduleList" class="unitBox" style="margin-left:306px;">
					<c:import url="/security/permission/list/1"></c:import>
				</div>
			</div>
		</div>
	</div>
</div>