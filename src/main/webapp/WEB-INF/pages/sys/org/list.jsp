<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>系统管理</li>
        <li>组织管理</li>
    </ol>
</div>

<div class="row main-content">
    <form role="form" class="form-horizontal" method="post" action="${contextPath }/org/list" id="searchForm" onsubmit="return doSearch(this);">
        <div class="form-group form-group-sm" id="searchDiv">
            <own:paginationHidden pager="${pager}" />
            <label for="search_LIKE_name" class="control-label col-md-1 col-sm-6">组织名称:</label>
            <div class="col-md-3 col-sm-6">
                <input type="text" class="form-control" placeholder="请输入名称" name="search_LIKE_name" value="${param.search_LIKE_name}" />
            </div>
            <div class="col-md-4 col-sm-6">
                <button type="submit" class="btn btn-default btn-sm doSearch">查询</button>
                <button type="submit" class="btn btn-default btn-sm" onclick="javascript:clearAllSearchContent('searchDiv');return false;">清除</button>
            </div>
        </div>
    </form>
    <hr class="clearfix">
    <p>
        <shiro:hasPermission name="SysOrganization:create">
            <a href="#" class="btn btn-default doCreate">添加</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="SysOrganization:delete">
            <a href="#" class="btn btn-default doDelete">删除</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="SysOrganization:update">
            <a href="#" class="btn btn-default doUpdate">修改</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="SysOrganization:view">
            <a href="#" class="btn btn-default doView">查看</a>
        </shiro:hasPermission>
    </p>

    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover table-condensed" id="tabData">
            <thead>
                <tr>
                    <th>组织名称</th>
                    <th>父组织</th>
                    <th>描述</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${orgs}">
                    <tr data-id="${item.id}">
                        <td>${item.name}</td>
                        <td>${item.parentName}</td>
                        <td>${item.description}</td>
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
                url = "${contextPath }/org/create";
                action = "create";
            } else if ($(this).hasClass('doDelete')) {
                url = "${contextPath }/org/delete/" + id;
                type = "post";
                action = "delete";
            } else if ($(this).hasClass('doUpdate')) {
                url = "${contextPath }/org/update/" + id;
                action = "update";
            } else if ($(this).hasClass('doView')) {
                url = "${contextPath }/org/view/" + id;
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
                    loadContent("${contextPath }/org/list");
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
