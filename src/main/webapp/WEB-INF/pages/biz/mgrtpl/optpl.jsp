<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"/>
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/>

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>模板管理</li>
        <li>OP模板</li>
    </ol>
</div>

<!-- 隐藏的form -->
<own:paginationForm action="${contextPath }/uc/optpl/list" pager="${pager}">
    <input type="hidden" name="search_EQ_opId" value="${param.search_EQ_opId}" />
    <input type="hidden" name="search_LIKE_opName" value="${param.search_LIKE_opName}" />
</own:paginationForm>

<div class="row main-content">
    <form role="form" class="form-horizontal" method="post" action="${contextPath }/uc/optpl/list">
        <div class="form-group">
            <label for="opId" class="control-label col-xs-3 col-sm-1">ID:</label>
            <div class="col-xs-9 col-sm-3">
                <input type="text" class="form-control" id="opId" placeholder="请输入OP ID" name="search_EQ_opId" value="${param.search_EQ_opId}" />
            </div>
            <label for="opName" class="control-label col-xs-3 col-sm-1">名称:</label>
            <div class="col-xs-9 col-sm-3">
                <input type="text" class="form-control" id="opName" placeholder="请输入名称"  name="search_EQ_opName" value="${param.search_EQ_opName}" />
            </div>
            <shiro:hasPermission name="OpTpl:view">
            <div class="col-xs-6 col-sm-2">
                <button type="submit" class="btn btn-default doSearch">查询</button>
            </div>
            </shiro:hasPermission>
        </div>
    </form>
    <hr class="clearfix">

    <p>
        <shiro:hasPermission name="OpTpl:create">
        <button type="button" class="btn btn-default doAdd">添加</button>
        </shiro:hasPermission>
    </p>

    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover table-condensed" id="tabData">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>运营商主密钥模板名称</th>
                    <th>运营商可变算法配置域</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${opTpls}">
                <tr>
                    <td>${item.opId}</td>
                    <td>${item.opName}</td>
                    <td>${item.opValue}</td>
                    <td>
                    <button type="button" class="btn btn-link">查看</button>
                    <button type="button" class="btn btn-link">删除</button>
                    </td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- 分页 -->
    <own:pagination pager="${pager}"/>
</div>
<script type="text/javascript">
</script>