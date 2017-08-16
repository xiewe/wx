<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>资源管理</li>
        <li>号段管理</li>
    </ol>
</div>

<div class="row main-content">
    <form role="form" class="form-horizontal" method="post" action="${contextPath }/no/list" id="searchForm" onsubmit="return doSearch(this);">
        <div class="form-group form-group-sm" id="searchDiv">
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
            <label for="search_EQ_phoneNoType" class="control-label col-md-1 col-sm-6">号码类型:</label>
            <div class="col-md-3 col-sm-6">
                <select class="form-control" name="search_EQ_phoneNoType">
                    <option value=""></option>
                    <option value="1" <c:if test="${param.search_EQ_phoneNoType == 1}">selected</c:if>>用户号码</option>
                    <option value="2" <c:if test="${param.search_EQ_phoneNoType == 2}">selected</c:if>>组号码</option>
                </select>
            </div>
            <div class="col-md-4 col-sm-6">
                <button type="submit" class="btn btn-default btn-sm doSearch">查询</button>
                <button type="submit" class="btn btn-default btn-sm" onclick="javascript:clearAllSearchContent('searchDiv');return false;">清除</button>
            </div>
        </div>
    </form>
    <hr class="clearfix">
    <p>
        <shiro:hasPermission name="PhoneNoFInfo:create">
            <a href="#" class="btn btn-default doCreate">添加</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="PhoneNoFInfo:delete">
            <a href="#" class="btn btn-default doDelete">删除</a>
        </shiro:hasPermission>
        <%-- <shiro:hasPermission name="PhoneNoFInfo:update">
            <a href="#" class="btn btn-default doUpdate">修改</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="PhoneNoFInfo:view">
            <a href="#" class="btn btn-default doView">查看</a>
        </shiro:hasPermission> --%>
    </p>

    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover table-condensed" id="tabData">
            <thead>
                <tr>
                    <th>组织名称</th>
                    <th>起始号码</th>
                    <th>号码个数</th>
                    <th>已使用号码数</th>
                    <th>号码类型</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${phonenofinfos}">
                    <tr data-id="${item.createTime.time}" data-startno="${item.phoneNoStart}">
                        <td><c:forEach var="op" items="${resorgs}">
                                <c:if test="${item.orgId == op.orgId}">${op.orgName }
                                </c:if>
                            </c:forEach></td>
                        <td>${item.phoneNoStart}</td>
                        <td>${item.numbers}</td>
                        <td>${item.usedCount}</td>
                        <td>${item.phoneNoType==1?'用户号码':'组号码'}</td>
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
            var startno = "";

            if (!$(this).hasClass('doCreate')) {
                if ($('table tbody').find('tr.success').length == 0 || $('table tbody').find('tr.success').length > 1) {
                    showAlert('提示', '请选择并仅选择一条记录');
                    return;
                } else {
                    id = $('table tbody').find('tr.success').data("id");
                    startno = $('table tbody').find('tr.success').data("startno");
                }
            }

            var url = "";
            var type = "get";
            var action = "";
            if ($(this).hasClass('doCreate')) {
                url = "${contextPath }/no/create";
                action = "create";
            } else if ($(this).hasClass('doDelete')) {
                url = "${contextPath }/no/delete/" + id + "/" + startno;
                type = "post";
                action = "delete";
            } else if ($(this).hasClass('doUpdate')) {
                url = "${contextPath }/no/update/" + id;
                action = "update";
            } else if ($(this).hasClass('doView')) {
                url = "${contextPath }/no/view/" + id;
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
                    $("#indexModal .modal-header h4").text("创建号段");
                    $("#indexModal .modal-body").html(result);
                    $("#indexModal .modal-footer").css('display', 'none');
                    $("#indexModal").modal('show');
                } else if (action == "delete") {
                    loadContent("${contextPath }/no/list");
                } else if (action == "update") {
                    $("#indexModal .modal-header h4").text("修改号段");
                    $("#indexModal .modal-body").html(result);
                    $("#indexModal .modal-footer").css('display', 'none');
                    $("#indexModal").modal('show');
                } else if (action == "view") {
                    $("#indexModal .modal-header h4").text("号段详情");
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
