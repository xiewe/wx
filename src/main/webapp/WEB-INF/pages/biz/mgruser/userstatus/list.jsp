<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>用户管理</li>
        <li>用户状态</li>
    </ol>
</div>

<div class="row main-content">
    <form role="form" class="form-horizontal" method="post" action="${contextPath }/userstatus/list" id="searchForm" onsubmit="return doSearch(this);">
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
                <label for="search_EQ_attachStatus" class="control-label col-md-1 col-sm-6">附着状态*</label>
                <div class="col-md-3 col-sm-6">
                    <select class="form-control" name="search_EQ_attachStatus">
                        <option value=""></option>
                        <option value="0" <c:if test="${param.search_EQ_attachStatus == 0}">selected</c:if>>未附着</option>
                        <option value="1" <c:if test="${param.search_EQ_attachStatus == 1}">selected</c:if>>已附着</option>
                    </select>
                </div>
                <label for="search_EQ_registerStatus" class="control-label col-md-1 col-sm-6">附着状态*</label>
                <div class="col-md-3 col-sm-6">
                    <select class="form-control" name="search_EQ_registerStatus">
                        <option value=""></option>
                        <option value="0" <c:if test="${param.search_EQ_registerStatus == 0}">selected</c:if>>未注册</option>
                        <option value="1" <c:if test="${param.search_EQ_registerStatus == 1}">selected</c:if>>已注册</option>
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
<%--     <p>
        <shiro:hasPermission name="UserStatusInfo:create">
            <a href="#" class="btn btn-default doCreate">添加</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="UserStatusInfo:delete">
            <a href="#" class="btn btn-default doDelete">删除</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="UserStatusInfo:update">
            <a href="#" class="btn btn-default doUpdate">修改</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="UserStatusInfo:view">
            <a href="#" class="btn btn-default doView">查看</a>
        </shiro:hasPermission>
    </p> --%>

    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover table-condensed" id="tabData">
            <thead>
                <tr>
                    <th>组织名称</th>
                    <th>用户号码</th>
                    <th>IMSI</th>
                    <th>用户类型</th>
                    <th>GUTI</th>
                    <th>TAI</th>
                    <th>附着状态</th>
                    <th>注册状态</th>
                    <th>接入方式</th>
                    <th>当前IP</th>
                    <th>当前APN</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${userstatusinfos}">
                    <tr data-id="${item.subNo}">
                        <td>${item.orgName}</td>
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
                        <td>${item.guti}</td>
                        <td>${item.tai}</td>
                        <td>${item.attachStatus == 0?'未附着':'已附着'}</td>
                        <td>${item.registerStatus == 0?'未注册':'已注册'}</td>
                        <td><c:choose>
                                <c:when test="${item.accessWay == 1 }">
                                LTE接入
                            </c:when>
                                <c:when test="${item.accessWay == 2 }">
                                SIP接入
                            </c:when>
                                <c:when test="${item.accessWay == 3 }">
                                POC接入
                            </c:when>
                                <c:otherwise>
                                Unknown
                            </c:otherwise>
                            </c:choose></td>
                        <td>${item.currIp}</td>
                        <td>${item.currAPN}</td>
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
                url = "${contextPath }/userstatus/create";
                action = "create";
            } else if ($(this).hasClass('doDelete')) {
                url = "${contextPath }/userstatus/delete/" + id;
                type = "post";
                action = "delete";
            } else if ($(this).hasClass('doUpdate')) {
                url = "${contextPath }/userstatus/update/" + id;
                action = "update";
            } else if ($(this).hasClass('doView')) {
                url = "${contextPath }/userstatus/view/" + id;
                action = "view";
            } else {
                console.log('not supported');
                return;
            }

            $.ajax({
                type : type,
                url : url
            }).done(function(result) {
                if (action == "create") {
                    $("#indexModal .modal-header h4").text("创建用户状态");
                    $("#indexModal .modal-body").html(result);
                    $("#indexModal .modal-footer").css('display', 'none');
                    $("#indexModal").modal('show');
                } else if (action == "delete") {
                    loadContent("${contextPath }/userstatus/list");
                } else if (action == "update") {
                    $("#indexModal .modal-header h4").text("修改用户状态");
                    $("#indexModal .modal-body").html(result);
                    $("#indexModal .modal-footer").css('display', 'none');
                    $("#indexModal").modal('show');
                } else if (action == "view") {
                    $("#indexModal .modal-header h4").text("用户状态详情");
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
