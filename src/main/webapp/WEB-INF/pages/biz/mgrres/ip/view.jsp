<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <table class="table table-hover table-condensed" id="tabData">
        <tbody>
            <tr>
                <td class="text-right">网段</td>
                <td>${ipfinfo.ipFragment}</td>
            </tr>
            <tr>
                <td class="text-right">网段掩码</td>
                <td>${ipfinfo.ipMask}</td>
            </tr>
            <tr>
                <td class="text-right">已使用IP个数</td>
                <td>${ipfinfo.usedCount}</td>
            </tr>
            <tr>
                <td class="text-right">描述</td>
                <td>${ipfinfo.desc}</td>
            </tr>
            <tr>
                <td class="text-right">创建时间</td>
                <td><fmt:formatDate value="${ipfinfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
                <td class="text-right">修改时间</td>
                <td><fmt:formatDate value="${ipfinfo.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
        </tbody>
    </table>
    <div class="col-sm-offset-2 col-sm-10 text-right" style="margin-bottom:20px;">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</div>