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
    <form role="form" class="form-horizontal" method="post" action="${contextPath }/org/list" id="searchForm" onsubmit="return doSearch(this)">
        <div class="form-group" id="searchDiv">
            <own:paginationHidden pager="${pager}" />
            <label for="name" class="control-label col-xs-3 col-sm-1">组织名称:</label>
            <div class="col-xs-9 col-sm-3">
                <input type="text" class="form-control" placeholder="请输入名称" name="search_LIKE_name" value="${param.search_EQ_name}" />
            </div>
            <div class="col-xs-6 col-sm-2">
                <button type="submit" class="btn btn-default doSearch">查询</button>
                <button type="submit" class="btn btn-default" onclick="javascript:clearAllSearchContent('searchDiv');return false;">清除</button>
            </div>
        </div>
    </form>
    <hr class="clearfix">
    <p>
        <shiro:hasPermission name="SysOrganization:create">
            <a href="#" data-toggle="modal" data-target="#indexModal" class="btn btn-default doCreate">添加</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="SysOrganization:delete">
            <a href="#" data-toggle="modal" data-target="#indexModal" class="btn btn-default doDelete">删除</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="SysOrganization:update">
            <a href="#" data-toggle="modal" data-target="#indexModal" class="btn btn-default doUpdate">修改</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="SysOrganization:view">
            <a href="#" data-toggle="modal" data-target="#indexModal" class="btn btn-default doView">查看</a>
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
                <%-- <c:choose> --%>
                <%-- <c:when test="${orgs.size() == 0}">
                        <tr colspan="3"><td>没有记录</td></tr>
                    </c:when>
                    <c:otherwise> --%>
                <c:forEach var="item" items="${orgs}">
                    <tr data-id="${item.id}">
                        <td>${item.name}</td>
                        <td>${item.parentId}</td>
                        <td>${item.description}</td>
                    </tr>
                </c:forEach>
                <%-- </c:otherwise> --%>
                <%-- </c:choose> --%>
            </tbody>
        </table>
    </div>

    <!-- 分页 -->
    <own:pagination pager="${pager}" />
</div>
