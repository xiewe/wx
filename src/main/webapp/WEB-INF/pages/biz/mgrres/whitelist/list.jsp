<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>资源管理</li>
        <li>黑白名单管理</li>
    </ol>
</div>

<div class="row main-content">
    <form role="form" class="form-horizontal" method="post" action="${contextPath }/whitelist/list" id="searchForm" onsubmit="return doSearch(this);">
        <div class="form-group form-group-sm" id="searchDiv">
            <own:paginationHidden pager="${pager}" />
            <label for="search_EQ_imeisv" class="control-label col-md-1 col-sm-6">IMSI/IMEISV:</label>
            <div class="col-md-3 col-sm-6">
                <input type="text" class="form-control" placeholder="请输入名称" name="search_EQ_imeisv" value="${param.search_EQ_imeisv}" />
            </div>
            <label for="search_EQ_listType" class="control-label col-md-1 col-sm-6">类型:</label>
            <div class="col-md-3 col-sm-6">
                <select class="form-control" name="search_EQ_listType">
                    <option value=""></option>
                    <c:choose>
                        <c:when test="${param.search_EQ_listType == 0 }">
                            <option value="0" selected>白名单</option>
                        </c:when>
                        <c:otherwise>
                            <option value="0">白名单</option>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${param.search_EQ_listType == 1 }">
                            <option value="1" selected>黑名单</option>
                        </c:when>
                        <c:otherwise>
                            <option value="1">黑名单</option>
                        </c:otherwise>
                    </c:choose>
                </select>
            </div>
            <div class="col-md-4 col-sm-6">
                <button type="submit" class="btn btn-default btn-sm doSearch">查询</button>
                <button type="submit" class="btn btn-default btn-sm" onclick="javascript:clearAllSearchContent('searchDiv');return false;">清除</button>
            </div>
        </div>
    </form>
    <hr class="clearfix">
    <div class="row">
        <p class="col-md-6 col-sm-12">
            <shiro:hasPermission name="BlackWhiteList:create">
                <a href="#" class="btn btn-default doCreate">添加</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="BlackWhiteList:delete">
                <a href="#" class="btn btn-default doDelete">删除</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="BlackWhiteList:update">
                <a href="#" class="btn btn-default doUpdate">修改</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="BlackWhiteList:create">
                <a href="#" class="btn btn-default doImport">导入</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="IMSIInfo:create">
                <p class="col-md-6 col-sm-12 text-right">
                    <a href="${contextPath }/whitelist/download" class="doDownload">模板下载</a>
                </p>
            </shiro:hasPermission>
        </p>
    </div>
    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover table-condensed" id="tabData">
            <thead>
                <tr>
                    <th>IMEI/IMEISV</th>
                    <th>IMSI</th>
                    <th>类型</th>
                    <th>创建时间</th>
                    <th>修改时间</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${blackwhitelists}">
                    <tr data-id="${item.createTime.time}" data-listtype="${item.listType}">
                        <td>${item.imeisv}</td>
                        <td>${item.imsi}</td>
                        <td>${item.listType==1?'黑名单':'白名单'}</td>
                        <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                        <td><fmt:formatDate value="${item.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
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
        loadListenerMulitSelect();

        $('a.btn').on('click', function(e) {
            stopBubble(e);
            stopDefault(e);

            var id = "";

            if (!$(this).hasClass('doCreate') && !$(this).hasClass('doDelete') && !$(this).hasClass('doImport')) {
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
                url = "${contextPath }/whitelist/create";
                action = "create";
            } else if ($(this).hasClass('doDelete')) {
                url = "${contextPath }/whitelist/delete";
                type = "post";
                action = "delete";
                
                var ids = "";
                var imeis = "";
                var imsis = "";
                var listtypes = "";
                
                $('table tbody').find('tr.success').each(function(){
                            ids += $(this).data("id");
                            imeis += $(this).children('td').eq(0).text() + '-';
                            imsis += $(this).children('td').eq(1).text() + '-';
                            listtypes += $(this).data("listtype") + '-';
                        }
                )
                
                
                $.ajax({
                    type : type,
                    url : url,
                    data : {"ids":ids, "imeis": imeis, "imsis":imsis, "listtypes":listtypes}
                }).done(function(result) {
                    loadContent("${contextPath }/whitelist/list");
                }).fail(function(result) {
                    showAlert('错误', '失败原因：' + result);
                }).always(function() {
                });
                return;
            } else if ($(this).hasClass('doUpdate')) {
                url = "${contextPath }/whitelist/update/" + id;
                action = "update";
            } else if ($(this).hasClass('doView')) {
                url = "${contextPath }/whitelist/view/" + id;
                action = "view";
            } else if ($(this).hasClass('doImport')) {
                url = "${contextPath }/whitelist/importpage";
                action = "import";
                loadContent(url);
                return;
            } else {
                console.log('not supported');
                return;
            }

            $.ajax({
                type : type,
                url : url
            }).done(function(result) {
                if (action == "create") {
                    $("#indexModal .modal-header h4").text("创建白名单");
                    $("#indexModal .modal-body").html(result);
                    $("#indexModal .modal-footer").css('display', 'none');
                    $("#indexModal").modal('show');
                } else if (action == "delete") {
                    loadContent("${contextPath }/whitelist/list");
                } else if (action == "update") {
                    $("#indexModal .modal-header h4").text("修改白名单");
                    $("#indexModal .modal-body").html(result);
                    $("#indexModal .modal-footer").css('display', 'none');
                    $("#indexModal").modal('show');
                } else if (action == "view") {
                    $("#indexModal .modal-header h4").text("白名单详情");
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
