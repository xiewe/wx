<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>模板管理</li>
        <li>APN模板</li>
    </ol>
</div>

<div class="row main-content">
    <form role="form" class="form-horizontal" method="post" action="${contextPath }/apn/list" id="searchForm" onsubmit="return doSearch(this);">
        <div class="form-group form-group-sm" id="searchDiv">
            <own:paginationHidden pager="${pager}" />
            <label for="search_EQ_apnId" class="control-label col-md-1 col-sm-6">APN ID:</label>
            <div class="col-md-3 col-sm-6">
                <input type="text" class="form-control" placeholder="请输入APN ID" name="search_EQ_apnId" value="${param.search_EQ_apnId}" />
            </div>
            <label for="search_EQ_oi" class="control-label col-md-1 col-sm-6">运营商标识:</label>
            <div class="col-md-3 col-sm-6">
                <input type="text" class="form-control" placeholder="请输入运营商标识" name="search_EQ_oi" value="${param.search_EQ_oi}" />
            </div>
            <label for="search_EQ_ni" class="control-label col-md-1 col-sm-6">网络标识:</label>
            <div class="col-md-3 col-sm-6">
                <input type="text" class="form-control" placeholder="请输入网络标识" name="search_EQ_ni" value="${param.search_EQ_ni}" />
            </div>
            <div class="col-md-4 col-sm-6">
                <button type="submit" class="btn btn-default btn-sm doSearch">查询</button>
                <button type="submit" class="btn btn-default btn-sm" onclick="javascript:clearAllSearchContent('searchDiv');return false;">清除</button>
            </div>
        </div>
    </form>
    <hr class="clearfix">
    <p>
        <shiro:hasPermission name="APNTpl:create">
            <a href="#" class="btn btn-default doCreate">添加</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="APNTpl:delete">
            <a href="#" class="btn btn-default doDelete">删除</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="APNTpl:update">
            <a href="#" class="btn btn-default doUpdate">修改</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="APNTpl:view">
            <a href="#" class="btn btn-default doView">查看</a>
        </shiro:hasPermission>
    </p>

    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover table-condensed" id="tabData">
            <thead>
                <tr>
                    <th>APN ID</th>
                    <th>运营商标识</th>
                    <th>网络标识</th>
                    <th>QCI</th>
                    <th>ARP优先级</th>
                    <th>ARP抢占标识</th>
                    <th>上行最大带宽（kbps）</th>
                    <th>下行最大带宽（kbps）</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${apntpls}">
                    <tr data-id="${item.apnId}">
                        <td>${item.apnId}</td>
                        <td>${item.oi}</td>
                        <td>${item.ni}</td>
                        <td>${item.qci}</td>
                        <td>${item.ARPPrio}</td>
                        <td>${item.preEmptionCapability==0? Enabled : Disabled}</td>
                        <td>${item.maxBwUl}</td>
                        <td>${item.maxBwDl}</td>
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
                url = "${contextPath }/apn/create";
                action = "create";
            } else if ($(this).hasClass('doDelete')) {
                url = "${contextPath }/apn/delete/" + id;
                type = "post";
                action = "delete";
            } else if ($(this).hasClass('doUpdate')) {
                url = "${contextPath }/apn/update/" + id;
                action = "update";
            } else if ($(this).hasClass('doView')) {
                url = "${contextPath }/apn/view/" + id;
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
                    $("#indexModal .modal-header h4").text("创建组织");
                    $("#indexModal .modal-body").html(result);
                    $("#indexModal .modal-footer").css('display', 'none');
                    $("#indexModal").modal('show');
                } else if (action == "delete") {
                    loadContent("${contextPath }/apn/list");
                } else if (action == "update") {
                    $("#indexModal .modal-header h4").text("修改组织");
                    $("#indexModal .modal-body").html(result);
                    $("#indexModal .modal-footer").css('display', 'none');
                    $("#indexModal").modal('show');
                } else if (action == "view") {
                    $("#indexModal .modal-header h4").text("组织详情");
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
