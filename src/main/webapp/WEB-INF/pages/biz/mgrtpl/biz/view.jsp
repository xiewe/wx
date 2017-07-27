<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <table class="table table-hover table-condensed" id="tabData">
        <tbody>
            <tr>
                <td class="text-right">业务模板ID</td>
                <td>${biztpl.bizTplId}</td>
            </tr>
            <tr>
                <td class="text-right">业务模板名称</td>
                <td>${biztpl.bizTplName}</td>
            </tr>
            <tr>
                <td class="text-right">用户类型</td>
                <td><c:choose>
                        <c:when test="${biztpl.userType == 1 }">
                                SIP
                            </c:when>
                        <c:when test="${biztpl.userType == 2 }">
                                DC
                            </c:when>
                        <c:when test="${biztpl.userType == 3 }">
                                VoLTE
                            </c:when>
                        <c:when test="${biztpl.userType == 4 }">
                                摄像头
                            </c:when>
                        <c:when test="${biztpl.userType == 5 }">
                                PDT
                            </c:when>
                        <c:when test="${biztpl.userType == 6 }">
                                POC
                            </c:when>
                        <c:when test="${biztpl.userType == 7 }">
                                LTE数据终端
                            </c:when>
                        <c:when test="${biztpl.userType == 8 }">
                                无线集群终端
                            </c:when>
                        <c:otherwise>
                                Unknown
                            </c:otherwise>
                    </c:choose></td>
            </tr>
            <tr>
                <td class="text-right">集群业务</td>
                <td>${biztpl.bizSwitcher==0?'关':'开'}</td>
            </tr>
            <tr>
                <td class="text-right">业务清单</td>
                <td><c:set var="sep" value=",${biztpl.bizIdList},"></c:set> <c:forEach var="item" items="${bizs}">
                        <c:set var="tmp" value=",${item.bizId},"></c:set>
                        <c:if test="${fn:contains(sep, tmp)}">${item.bizName }<br>
                        </c:if>
                    </c:forEach></td>
            </tr>
            <tr>
                <td class="text-right">创建时间</td>
                <td><fmt:formatDate value="${biztpl.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
                <td class="text-right">修改时间</td>
                <td><fmt:formatDate value="${biztpl.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
        </tbody>
    </table>
    <div class="col-sm-offset-2 col-sm-10 text-right" style="margin-bottom: 20px;">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</div>