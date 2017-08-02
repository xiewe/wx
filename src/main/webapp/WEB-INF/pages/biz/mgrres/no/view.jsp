<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <table class="table table-hover table-condensed" id="tabData">
        <tbody>
            <tr>
                <td class="text-right">组织名称</td>
                <td><c:forEach var="item" items="${resorgs}">
                        <c:if test="${phonenofinfo.orgId == item.orgId}">${item.orgName }<br>
                        </c:if>
                    </c:forEach></td>
            </tr>
            <tr>
                <td class="text-right">号码类型</td>
                <td>${phonenofinfo.phoneNoType==1?'用户号码':'组号码'}</td>
            </tr>
            <tr>
                <td class="text-right">起始号码</td>
                <td>${phonenofinfo.phoneNoStart}</td>
            </tr>
            <tr>
                <td class="text-right">号码个数</td>
                <td>${phonenofinfo.numbers}</td>
            </tr>
            <tr>
                <td class="text-right">已使用号码数</td>
                <td>${phonenofinfo.usedCount}</td>
            </tr>
            <tr>
                <td class="text-right">创建时间</td>
                <td><fmt:formatDate value="${phonenofinfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
                <td class="text-right">修改时间</td>
                <td><fmt:formatDate value="${phonenofinfo.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
        </tbody>
    </table>
    <div class="col-sm-offset-2 col-sm-10 text-right" style="margin-bottom: 20px;">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</div>