<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <table class="table table-hover table-condensed" id="tabData">
        <tbody>
            <tr>
                <td class="text-right">名称</td>
                <td>${org.name}</td>
            </tr>
            <tr>
                <td class="text-right">父组织</td>
                <td>${org.parentName}</td>
            </tr>
            <tr>
                <td class="text-right">描述</td>
                <td>${org.description}</td>
            </tr>
            <tr>
                <td class="text-right">创建时间</td>
                <td>${org.createTime}</td>
            </tr>
            <tr>
                <td class="text-right">修改时间</td>
                <td>${org.modifyTime}</td>
            </tr>
        </tbody>
    </table>
</div>