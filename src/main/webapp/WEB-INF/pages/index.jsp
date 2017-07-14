<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="${contextPath}/favicon.ico">
<title>UC</title>

<!-- Bootstrap core CSS -->
<link href="${contextPath}/styles/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/styles/bootstrapvalidator/css/bootstrapValidator.min.css" rel="stylesheet">

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<link href="${contextPath}/styles/bootstrap/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="${contextPath}/styles/custom/css/dashboard.css?v=${date}" rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
<script src="${contextPath}/styles/bootstrap/js/html5shiv.min.js"></script>
<script src="${contextPath}/styles/bootstrap/js/respond.min.js"></script>
<![endif]-->
<style type="text/css">
</style>
</head>
<body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container-fruid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">TCN2000用户中心</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${contextPath}/updateBase"><span>欢迎您，${login_user.username}</span></a></li>
                    <li><a href="${contextPath}/updatePwd"><span>修改密码</span></a></li>
                    <li><a href="${contextPath}/logout"><span style="padding-right: 50px; border-right: 0px">退出</span></a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container-fruid">
        <div class="row index-row">
            <div class="col-md-2 col-sm-3 sideMenu">
                <a href="#" class="btn btn-defaut" role="button"> <!-- <span class="glyphicon glyphicon-menu-left"></span> --><< TCN2000网管
                </a>
                <div class="panel-group" id="accordion">
                    <c:set var="menuParentId" value="0" />
                    <c:forEach var="item" items="${menu}">
                        <c:if test="${item.parentId == null || item.parentId == 0}">
                            <c:if test="${menuParentId != 0 }">
                </div>
            </div>
        </div>
        </c:if>
        <c:set var="menuParentId" value="${item.id }" />
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">
                    <span class="glyphicon-plus glyphicon"></span> <a data-toggle="collapse" data-parent="#accordion" href="#collapse${menuParentId}"> ${item.name} </a>
                </h4>
            </div>
            <div id="collapse${menuParentId}" class="panel-collapse collapse">
                <div class="list-group">
                    </c:if>
                    <c:if test="${item.parentId != null && item.parentId == menuParentId}">
                        <a href="#" class="list-group-item" data-url="${contextPath}${item.url }">${item.name}</a>
                    </c:if>
                    </c:forEach>
                    <c:if test="${menuParentId != 0 }">
                </div>
            </div>
        </div>
        </c:if>
    </div>
    </div>

    <div class="col-sm-9 col-md-10 main">
        <!-- 可变内容开始 -->

        <div class="row">
            <p>欢迎您！</p>
        </div>

        <!-- 可变内容结束 -->
    </div>
    </div>
    </div>

    <footer class="footer">
        <div class="container">
            <p class="text-center">Copyright © 2017-2017 XINWEI All Rights Reserved.</p>
        </div>
    </footer>
    <!-- Modal begin -->
    <div class="modal fade" id="indexModal" tabindex="-1" role="dialog" aria-labelledby="indexModalLabel" aria-hidden="true" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
                    <h4 class="modal-title" id="indexModalLabel">模态框（Modal）标题</h4>
                </div>
                <div class="modal-body" id="indexModalContent">在这里添加一些文本</div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary doConfirm">确定</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- Modal end -->
    <script src="${contextPath}/styles/jquery-3.2.1.min.js"></script>
    <script src="${contextPath}/styles/bootstrap/js/bootstrap.min.js"></script>
    <script src="${contextPath}/styles/bootstrapvalidator/js/bootstrapValidator.min.js"></script>
    <script src="${contextPath}/styles/bootstrapvalidator/js/language/zh_CN.js"></script>

    <script src="${contextPath}/styles/utils/common.js?v=${date}"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <%-- <script src="${contextPath}/styles/bootstrap/js/ie10-viewport-bug-workaround.js"></script> --%>
    <script type="text/javascript">
                    
                </script>

</body>
</html>
