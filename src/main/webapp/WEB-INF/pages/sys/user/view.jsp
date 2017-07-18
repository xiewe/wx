<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <table class="table table-hover table-condensed" id="tabData">
        <tbody>
            <tr>
                <td class="text-right">用户名</td>
                <td>${user.username}</td>
            </tr>
            <tr>
                <td class="text-right">电话</td>
                <td>${user.phone}</td>
            </tr>
            <tr>
                <td class="text-right">邮箱</td>
                <td>${user.email}</td>
            </tr>
            <tr>
                <td class="text-right">角色</td>
                <td>${user.sysRole.name}</td>
            </tr>
            <tr>
                <td class="text-right">组织</td>
                <td>${user.sysOrganization.name}</td>
            </tr>
            <tr>
                <td class="text-right">创建时间</td>
                <td><fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
                <td class="text-right">修改时间</td>
                <td><fmt:formatDate value="${user.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
        </tbody>
    </table>
    <div class="col-sm-offset-2 col-sm-10 text-right" style="margin-bottom: 20px;">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</div>