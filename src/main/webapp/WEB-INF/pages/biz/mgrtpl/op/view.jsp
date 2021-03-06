<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <table class="table table-hover table-condensed" id="tabData">
        <tbody>
            <tr>
                <td class="text-right">OP ID</td>
                <td>${optpl.opId}</td>
            </tr>
            <tr>
                <td class="text-right">模板名称</td>
                <td>${optpl.opName}</td>
            </tr>
            <tr>
                <td class="text-right">运营商可变算法配置域</td>
                <td>${optpl.opValue}</td>
            </tr>
            <tr>
                <td class="text-right">创建时间</td>
                <td><fmt:formatDate value="${optpl.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
                <td class="text-right">修改时间</td>
                <td><fmt:formatDate value="${optpl.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
        </tbody>
    </table>
    <div class="col-sm-offset-2 col-sm-10 text-right" style="margin-bottom:20px;">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</div>