<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <table class="table table-hover table-condensed" id="tabData">
        <tbody>
            <tr>
                <td class="text-right">组织</td>
                <td><c:forEach var="item" items="${resorgs}">
                        <c:if test="${useraccount.orgId == item.orgId }">
                        ${item.orgName}
                    </c:if>
                    </c:forEach></td>
            </tr>
            <tr>
                <td class="text-right">用戶类型</td>
                <td><c:if test="${useraccount.userType == 1}">SIP
                </c:if> <c:if test="${useraccount.userType == 2}">
                    DC
                </c:if> <c:if test="${useraccount.userType == 3}">
                    VoLTE
                </c:if> <c:if test="${useraccount.userType == 4}">
                    摄像头
                </c:if> <c:if test="${useraccount.userType == 5}">
                    PDT
                </c:if> <c:if test="${useraccount.userType == 6}">
                    POC
                </c:if> <c:if test="${useraccount.userType == 7}">
                    LTE数据终端
                </c:if> <c:if test="${useraccount.userType == 8}">
                    无线集群终端
                </c:if></td>
            </tr>
            <tr>
                <td class="text-right">用户号码</td>
                <td>${useraccount.subNo}</td>
            </tr>
            <tr>
                <td class="text-right">IMSI</td>
                <td>${useraccount.imsi}</td>
            </tr>

            <tr>
                <td class="text-right">K</td>
                <td>${useraccount.k}</td>
            </tr>
            <tr>
                <td class="text-right">状态</td>
                <td>${useraccount.status==2?'正常':'已停机'}</td>
            </tr>
            <tr>
                <td class="text-right">游牧TCN1000</td>
                <td>${useraccount.roamToTcn1000==1?'不互通':'互通'}</td>
            </tr>
            <tr>
                <td class="text-right">用户名</td>
                <td>${useraccount.subName}</td>
            </tr>
            <tr>
                <td class="text-right">密码</td>
                <td>${useraccount.subPassword}</td>
            </tr>
            <tr>
                <td class="text-right">模板</td>
                <td><c:forEach var="item" items="${biztpls}">
                        <c:if test="${useraccount.bizTplId == item.bizTplId }">
                            ${item.bizTplName}
                        </c:if>
                    </c:forEach></td>
            </tr>
            <tr>
                <td class="text-right">无条件呼叫前转号码</td>
                <td>${useraccount.noCondFwdNo}</td>
            </tr>
            <tr>
                <td class="text-right">不可达呼叫前转号码</td>
                <td>${useraccount.noReachFwdNo}</td>
            </tr>
            <tr>
                <td class="text-right">无应答呼叫前转号码</td>
                <td>${useraccount.noReplyFwdNo}</td>
            </tr>
            <tr>
                <td class="text-right">遇忙前转号码</td>
                <td>${useraccount.busyFwdNo}</td>
            </tr>
            <tr>
                <td class="text-right">紧急单呼号码</td>
                <td>${useraccount.emgySingleNo}</td>
            </tr>
            <tr>
                <td class="text-right">紧急组呼号码</td>
                <td>${useraccount.emgyGroupNo}</td>
            </tr>
            <tr>
                <td class="text-right">PNAS注册周期 (s)</td>
                <td>${useraccount.pnasRegCircle}</td>
            </tr>
            <tr>
                <td class="text-right">APN组配置</td>
                <td><c:forEach var="item" items="${apngrouptpls}">
                        <c:if test="${useraccount.apnGroupId == item.apnGroupId }">
${item.apnGroupName}
</c:if>
                    </c:forEach> <br>APN ID: ${useraccount.apnIdList} <br>IP类型:${useraccount.ipAllocationType} <br>IPv4地址:${useraccount.ipv4List}</td>
            </tr>
            <tr>
                <td class="text-right">角色</td>
                <td><c:if test="${useraccount.roleInOrg == 1}">内网用户</c:if> <c:if test="${useraccount.roleInOrg == 2}">组织调度员</c:if> <c:if
                        test="${useraccount.roleInOrg == 3}">组织块调度员</c:if></td>
            </tr>
            <tr>
                <td class="text-right">集群优先级</td>
                <td>${useraccount.truncPrio}</td>
            </tr>
            <tr>
                <td class="text-right">PGIS</td>
                <td><c:if test="${useraccount.pgis == 1}">开通</c:if> <c:if test="${useraccount.pgis == 2}">关闭</c:if></td>
            </tr>
            <tr>
                <td class="text-right">组织呼入呼出权限</td>
                <td><c:if test="${useraccount.callRightInOrg == 1}">只允许组织内出呼</c:if> <c:if test="${useraccount.callRightInOrg == 2}">只允许组织内入呼</c:if>
                    <c:if test="${useraccount.callRightInOrg == 3}">允许组织内出呼和入呼</c:if> <c:if test="${useraccount.callRightInOrg == 4}">允许组织外出呼（含组织内出入呼）</c:if>
                    <c:if test="${useraccount.callRightInOrg == 5}">允许组织外入呼（含组织内出入呼）</c:if> <c:if test="${useraccount.callRightInOrg == 6}">允许组织外出呼和入呼</c:if></td>
            </tr>
            <tr>
                <td class="text-right">授权呼叫</td>
                <td><c:if test="${useraccount.callAuth == 1}">无</c:if> <c:if test="${useraccount.callAuth == 2}">全部呼叫</c:if> <c:if
                        test="${useraccount.callAuth == 3}">国内长途呼叫</c:if> <c:if test="${useraccount.callAuth == 4}">国际长途呼叫</c:if></td>
            </tr>
            <tr>
                <td class="text-right">授权调度台号</td>
                <td>${useraccount.authGDCNo}</td>
            </tr>
            <tr>
                <td class="text-right">短号</td>
                <td>${useraccount.subShortCode}</td>
            </tr>
            <tr>
                <td class="text-right">创建者</td>
                <td>${useraccount.creator}</td>
            </tr>
            <tr>
                <td class="text-right">创建时间</td>
                <td><fmt:formatDate value="${useraccount.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
                <td class="text-right">修改时间</td>
                <td><fmt:formatDate value="${useraccount.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
        </tbody>
    </table>
    <div class="col-sm-offset-2 col-sm-10 text-right" style="margin-bottom: 20px;">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</div>