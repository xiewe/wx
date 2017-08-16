<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>用户管理</li>
        <li>用户信息</li>
    </ol>
</div>

<div class="row main-content">
    <form role="form" class="form-horizontal" method="post" action="${contextPath }/userinfo/list" id="searchForm" onsubmit="return doSearch(this);">
        <div id="searchDiv">
            <div class="form-group form-group-sm">
                <own:paginationHidden pager="${pager}" />
                <label for="search_EQ_orgId" class="control-label col-md-1 col-sm-6">组织名称:</label>
                <div class="col-md-3 col-sm-6">
                    <select class="form-control" name="search_EQ_orgId">
                        <option value=""></option>
                        <c:forEach var="item" items="${resorgs}">
                            <c:choose>
                                <c:when test="${param.search_EQ_orgId == item.orgId }">
                                    <option value="${item.orgId }" selected>${item.orgName}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${item.orgId }">${item.orgName}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
                <label for="search_EQ_subNo" class="control-label col-md-1 col-sm-6">用户号码:</label>
                <div class="col-md-3 col-sm-6">
                    <input type="text" class="form-control" placeholder="请输入名称" name="search_EQ_subNo" value="${param.search_EQ_subNo}" />
                </div>
                <label for="search_EQ_userType" class="control-label col-md-1 col-sm-6">用户类型 *</label>
                <div class="col-md-3 col-sm-6">
                    <select class="form-control" name="search_EQ_userType">
                        <option value=""></option>
                        <option value="1" <c:if test="${param.search_EQ_userType == 1}">selected</c:if>>SIP</option>
                        <option value="2" <c:if test="${param.search_EQ_userType == 2}">selected</c:if>>DC</option>
                        <option value="3" <c:if test="${param.search_EQ_userType == 3}">selected</c:if>>VoLTE</option>
                        <option value="4" <c:if test="${param.search_EQ_userType == 4}">selected</c:if>>摄像头</option>
                        <option value="5" <c:if test="${param.search_EQ_userType == 5}">selected</c:if>>PDT</option>
                        <option value="6" <c:if test="${param.search_EQ_userType == 6}">selected</c:if>>POC</option>
                        <option value="7" <c:if test="${param.search_EQ_userType == 7}">selected</c:if>>LTE数据终端</option>
                        <option value="8" <c:if test="${param.search_EQ_userType == 8}">selected</c:if>>无线集群终端</option>
                    </select>
                </div>
            </div>
            <div class="form-group form-group-sm">
                <label for="search_EQ_status" class="control-label col-md-1 col-sm-6">用户状态*</label>
                <div class="col-md-3 col-sm-6">
                    <select class="form-control" name="search_EQ_status">
                        <option value=""></option>
                        <option value="1" <c:if test="${param.search_EQ_status == 1}">selected</c:if>>已停机</option>
                        <option value="2" <c:if test="${param.search_EQ_status == 2}">selected</c:if>>正常</option>
                    </select>
                </div>
                <div class="col-md-4 col-sm-6">
                    <button type="submit" class="btn btn-default btn-sm doSearch">查询</button>
                    <button type="submit" class="btn btn-default btn-sm" onclick="javascript:clearAllSearchContent('searchDiv');return false;">清除</button>
                </div>
            </div>
        </div>
    </form>
    <hr class="clearfix">
    <p>
        <shiro:hasPermission name="UserAccount:create">
            <a href="#" class="btn btn-default doCreate">开户</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="UserAccount:delete">
            <a href="#" class="btn btn-default doDelete">销户</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="UserAccount:update">
            <a href="#" class="btn btn-default doUpdate">修改</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="UserAccount:view">
            <a href="#" class="btn btn-default doView">查看</a>
        </shiro:hasPermission>
        <span class="">|</span>
        <shiro:hasPermission name="UserAccount:create">
            <a href="#" class="btn btn-default doResume">复机</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="UserAccount:create">
            <a href="#" class="btn btn-default doTerminate">停机</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="UserAccount:create">
            <a href="#" class="btn btn-default doChangeImsi">换卡</a>
        </shiro:hasPermission>
        <span class="">|</span>
        <shiro:hasPermission name="UserAccount:create">
            <a href="#" class="btn btn-default doRoamTo1000">游牧</a>
        </shiro:hasPermission>
    </p>

    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover table-condensed" id="tabData">
            <thead>
                <tr>
                    <th>组织名称</th>
                    <th>用户号码</th>
                    <th>IMSI</th>
                    <th>用户类型</th>
                    <th>用户状态</th>
                    <th>游牧TCN1000</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${useraccounts}">
                    <tr data-id="${item.subNo}">
                        <td><c:forEach var="item11" items="${resorgs}">
                                <c:if test="${item.orgId == item11.orgId }">
                                   ${item11.orgName}
                                </c:if>
                            </c:forEach></td>
                        <td>${item.subNo}</td>
                        <td>${item.imsi}</td>
                        <td><c:choose>
                                <c:when test="${item.userType == 1 }">
                                SIP
                            </c:when>
                                <c:when test="${item.userType == 2 }">
                                DC
                            </c:when>
                                <c:when test="${item.userType == 3 }">
                                VoLTE
                            </c:when>
                                <c:when test="${item.userType == 4 }">
                                摄像头
                            </c:when>
                                <c:when test="${item.userType == 5 }">
                                PDT
                            </c:when>
                                <c:when test="${item.userType == 6 }">
                                POC
                            </c:when>
                                <c:when test="${item.userType == 7 }">
                                LTE数据终端
                            </c:when>
                                <c:when test="${item.userType == 8 }">
                                无线集群终端
                            </c:when>
                                <c:otherwise>
                                Unknown
                            </c:otherwise>
                            </c:choose></td>
                        <td>${item.status==2?'正常':'已停机'}</td>
                        <td>${item.roamToTcn1000==1?'不互通':'互通'}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- 分页 -->
    <own:pagination pager="${pager}" />
</div>

<script type="text/javascript">
    $(document).ready(function() {
        loadListener();

        $('a.btn').on('click', function(e) {
            stopBubble(e);
            stopDefault(e);

            var id = "";

            if (!$(this).hasClass('doCreate')) {
                if ($('table tbody').find('tr.success').length == 0 || $('table tbody').find('tr.success').length > 1) {
                    showAlert('提示', '请选择并仅选择一条记录');
                    return;
                } else {
                    id = $('table tbody').find('tr.success').data("id");
                }
            }

            var url = "";
            var type = "get";
            var action = "";
            if ($(this).hasClass('doCreate')) {
                url = "${contextPath }/userinfo/create";
                action = "create";
                loadContent(url);
                return;
            } else if ($(this).hasClass('doDelete')) {
                url = "${contextPath }/userinfo/delete/" + id;
                type = "post";
                action = "delete";
            } else if ($(this).hasClass('doUpdate')) {
                url = "${contextPath }/userinfo/update/" + id;
                action = "update";
                loadContent(url);
                return;
            } else if ($(this).hasClass('doView')) {
                url = "${contextPath }/userinfo/view/" + id;
                action = "view";
            } else if ($(this).hasClass('doResume')) {
                url = "${contextPath }/userinfo/resume/" + id;
                type = "post";
                action = "resume";
            } else if ($(this).hasClass('doTerminate')) {
                url = "${contextPath }/userinfo/terminate/" + id;
                type = "post";
                action = "terminate";
            } else if ($(this).hasClass('doChangeImsi')) {
                url = "${contextPath }/userinfo/changeimsi/" + id;
                action = "changeimsi";
            } else if ($(this).hasClass('doRoamTo1000')) {
                url = "${contextPath }/userinfo/roamTo1000/" + id;
                action = "roamTo1000";
            } else {
                console.log('not supported');
                return;
            }

            $.ajax({
                type : type,
                url : url
            }).done(function(result) {
                if (action == "delete") {
                    loadContent("${contextPath }/userinfo/list");
                } else if (action == "resume") {
                    loadContent("${contextPath }/userinfo/list");
                } else if (action == "terminate") {
                    loadContent("${contextPath }/userinfo/list");
                } else if (action == "roamTo1000") {
                    loadContent("${contextPath }/userinfo/list");
                } else if (action == "changeimsi") {
                    $("#indexModal .modal-header h4").text("换卡");
                    $("#indexModal .modal-body").html(result);
                    $("#indexModal .modal-footer").css('display', 'none');
                    $("#indexModal").modal('show');
                } else if (action == "view") {
                    $("#indexModal .modal-header h4").text("用户详情");
                    $("#indexModal .modal-body").html(result);
                    $("#indexModal .modal-footer").css('display', 'none');
                    $("#indexModal").modal('show');
                }
            }).fail(function(result) {
                showAlert('错误', '失败原因：' + result);
            }).always(function() {
            });

        })

    })
</script>
