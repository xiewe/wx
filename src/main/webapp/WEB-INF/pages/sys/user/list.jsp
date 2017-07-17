<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>系统管理</li>
        <li>用户管理</li>
    </ol>
</div>

<div class="row main-content">
    <form user="form" class="form-horizontal" method="post" action="${contextPath }/user/list" id="searchForm" onsubmit="return doSearch(this);">
        <div class="" id="searchDiv">
            <own:paginationHidden pager="${pager}" />
            <div class="form-group form-group-sm">
                <label for="search_LIKE_username" class="control-label col-md-1 col-sm-6">用户:</label>
                <div class="col-md-3 col-sm-6">
                    <input type="text" class="form-control" placeholder="请输入用户名" name="search_LIKE_username" value="${param.search_LIKE_username}" />
                </div>
                <label for="search_LIKE_email" class="control-label col-md-1 col-sm-6">邮箱:</label>
                <div class="col-md-3 col-sm-6">
                    <input type="text" class="form-control" placeholder="请输入邮箱" name="search_LIKE_email" value="${param.search_LIKE_email}" />
                </div>
                <label for="search_LIKE_phone" class="control-label col-md-1 col-sm-6">电话:</label>
                <div class="col-md-3 col-sm-6">
                    <input type="text" class="form-control" placeholder="请输入电话" name="search_LIKE_phone" value="${param.search_LIKE_phone}" />
                </div>
            </div>
            <div class="form-group form-group-sm">
                <label for="search_EQ_sysRole.id" class="control-label col-md-1 col-sm-6">角色:</label>
                <div class="col-md-3 col-sm-6">
                    <input type="text" class="form-control" placeholder="请输入角色" name="search_EQ_sysRole.id" value="${param.search_EQ_sysRole.id}" />
                </div>
                <label for="search_EQ_sysOrganization.id" class="control-label col-md-1 col-sm-6">部门:</label>
                <div class="col-md-3 col-sm-6">
                    <input type="text" class="form-control" placeholder="请输入部门" name="search_EQ_sysOrganization.id" value="${param.search_EQ_sysOrganization.id}" />
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
        <shiro:hasPermission name="SysUser:create">
            <a href="#" class="btn btn-default doCreate">添加</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="SysUser:delete">
            <a href="#" class="btn btn-default doDelete">删除</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="SysUser:update">
            <a href="#" class="btn btn-default doUpdate">修改</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="SysUser:view">
            <a href="#" class="btn btn-default doView">查看</a>
        </shiro:hasPermission>
    </p>

    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover table-condensed" id="tabData">
            <thead>
                <tr>
                    <th>用户名</th>
                    <th>电话</th>
                    <th>邮箱</th>
                    <th>角色</th>
                    <th>组织</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${users}">
                    <tr data-id="${item.id}">
                        <td>${item.username}</td>
                        <td>${item.phone}</td>
                        <td>${item.email}</td>
                        <td>${item.sysRole.name}</td>
                        <td>${item.sysOrganization.name}</td>
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
            var dataType = "html";
            if ($(this).hasClass('doCreate')) {
                url = "${contextPath }/user/create";
                action = "create";
            } else if ($(this).hasClass('doDelete')) {
                url = "${contextPath }/user/delete/" + id;
                type = "post";
                action = "delete";
                dataType = "json";
            } else if ($(this).hasClass('doUpdate')) {
                url = "${contextPath }/user/update/" + id;
                action = "update";
            } else if ($(this).hasClass('doView')) {
                url = "${contextPath }/user/view/" + id;
                action = "view";
            } else {
                console.log('not supported');
                return;
            }

            $.ajax({
                type : type,
                url : url,
                dataType : dataType
            }).done(function(result) {
                if (action == "create") {
                    $("#indexModal .modal-header h4").text("创建用户");
                    $("#indexModal .modal-body").html(result);
                    $("#indexModal .modal-footer").css('display', 'none');
                    $("#indexModal").modal('show');
                } else if (action == "delete") {
                    if (result.status == 0) {
                        loadContent("${contextPath }/user/list");
                    } else {
                        showAlert('提示', result.errMsg);
                    }
                } else if (action == "update") {
                    $("#indexModal .modal-header h4").text("修改用户");
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
