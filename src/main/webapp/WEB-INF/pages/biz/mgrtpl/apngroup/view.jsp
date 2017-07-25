<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <table class="table table-hover table-condensed" id="tabData">
        <tbody>
            <tr>
                <td class="text-right">APN组ID</td>
                <td>${apngrouptpl.apnGroupId}</td>
            </tr>
            <tr>
                <td class="text-right">组名称</td>
                <td>${apngrouptpl.apnGroupName}</td>
            </tr>
            <tr>
                <td class="text-right">上行最大带宽（kbps）</td>
                <td>${apngrouptpl.maxRequestedBwUl}</td>
            </tr>
            <tr>
                <td class="text-right">下行最大带宽（kbps）</td>
                <td>${apngrouptpl.maxRequestedBwDl}</td>
            </tr>
            <tr>
                <td class="text-right">APN模版配置通知类型</td>
                <td>${apngrouptpl.apnNotifiedType}</td>
            </tr>
            <tr>
                <td class="text-right">创建时间</td>
                <td><fmt:formatDate value="${apngrouptpl.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
                <td class="text-right">修改时间</td>
                <td><fmt:formatDate value="${apngrouptpl.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
        </tbody>
    </table>
    <div class="col-sm-offset-2 col-sm-10 text-right" style="margin-bottom: 20px;">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</div>