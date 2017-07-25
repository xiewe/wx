<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <table class="table table-hover table-condensed" id="tabData">
        <tbody>
            <tr>
                <td class="text-right">组织名称</td>
                <td>${organization.orgName}</td>
            </tr>
            <tr>
                <td class="text-right">组织最大用户数</td>
                <td>${organization.maxUserNo}</td>
            </tr>
            <tr>
                <td class="text-right">最大组数</td>
                <td>${organization.maxGroupNo}</td>
            </tr>
            <tr>
                <td class="text-right">组织管理员</td>
                <td>${organization.admin}</td>
            </tr>
            <tr>
                <td class="text-right">紧急单呼号码</td>
                <td>${organization.emgySingleCallNo}</td>
            </tr>
            <tr>
                <td class="text-right">备注</td>
                <td>${organization.comments}</td>
            </tr>
            <tr>
                <td class="text-right">创建时间</td>
                <td><fmt:formatDate value="${organization.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
                <td class="text-right">修改时间</td>
                <td><fmt:formatDate value="${organization.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
        </tbody>
    </table>
    <div class="col-sm-offset-2 col-sm-10 text-right" style="margin-bottom: 20px;">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</div>