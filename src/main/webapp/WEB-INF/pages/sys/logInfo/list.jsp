<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>系统管理</li>
        <li>系统日志</li>
    </ol>
</div>

<div class="row main-content">
    <form role="form" class="form-horizontal" method="post" action="${contextPath }/log/list" id="searchForm" onsubmit="return doSearch(this);">
        <div class="" id="searchDiv">
            <own:paginationHidden pager="${pager}" />
            <div class="form-group form-group-sm">
                <label for="search_LIKE_username" class="control-label col-md-1 col-sm-6">用户:</label>
                <div class="col-md-3 col-sm-6">
                    <input type="text" class="form-control" placeholder="请输入用户名" name="search_LIKE_username" value="${param.search_LIKE_username}" />
                </div>
                <label for="search_LIKE_ip" class="control-label col-md-1 col-sm-6">IP:</label>
                <div class="col-md-3 col-sm-6">
                    <input type="text" class="form-control" placeholder="请输入IP" name="search_LIKE_ip" value="${param.search_LIKE_ip}" />
                </div>
                <label for="search_LIKE_category" class="control-label col-md-1 col-sm-6">类别:</label>
                <div class="col-md-3 col-sm-6">
                    <input type="text" class="form-control" placeholder="请输入日志类别" name="search_LIKE_category" value="${param.search_LIKE_category}" />
                </div>
            </div>
            <div class="form-group form-group-sm">
                <label for="search_LIKE_message" class="control-label col-md-1 col-sm-6">日志:</label>
                <div class="col-md-3 col-sm-6">
                    <input type="text" class="form-control" placeholder="请输入日志关键字" name="search_LIKE_message" value="${param.search_LIKE_message}" />
                </div>
                <div class="col-md-4 col-sm-6">
                    <button type="submit" class="btn btn-default btn-sm doSearch">查询</button>
                    <button type="submit" class="btn btn-default btn-sm" onclick="javascript:clearAllSearchContent('searchDiv');return false;">清除</button>
                </div>
            </div>
        </div>
    </form>
    <hr class="clearfix">

    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover table-condensed" id="tabData">
            <thead>
                <tr>
                    <th>类别</th>
                    <th>用户</th>
                    <th>IP</th>
                    <th>Agent</th>
                    <th>日志</th>
                    <th>时间</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${logs}">
                    <tr>
                        <td>${item.category}</td>
                        <td>${item.username}</td>
                        <td>${item.ip}</td>
                        <td><a href="#" style="width: 150px; text-overflow: ellipsis; white-space: nowrap; overflow: hidden; display: block;" data-toggle="tooltip" title="${item.userAgent}">${item.userAgent}</a></td>
                        <td>${item.message}</td>
                        <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- 分页 -->
    <own:pagination pager="${pager}" />
</div>

<script type="text/javascript">
    $(function() {
        $("[data-toggle='tooltip']").tooltip();
    });
</script>
