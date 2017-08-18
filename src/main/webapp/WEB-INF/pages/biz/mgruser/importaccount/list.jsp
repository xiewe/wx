<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>用户管理</li>
        <li>批量开户</li>
    </ol>
</div>

<link href="${contextPath}/styles/fileinput/css/fileinput.min.css" rel="stylesheet">
<div class="row main-content">
    <div class="row">
        <shiro:hasPermission name="IMSIInfo:create">
            <p class="col-md-12 col-sm-12 text-right">
                <a href="${contextPath }/importaccount/download" class="doDownload">模板下载</a>
            </p>
        </shiro:hasPermission>
    </div>
    <hr class="clearfix" style="margin-top: 0px;">
    <form id="saveForm" class="form-horizontal" role="form" enctype="multipart/form-data" method="post" action="${contextPath }/importaccount/import" onsubmit="return doImport(this);">
        <div class="form-group">
            <label for="openaccountfilename" class="col-sm-2 control-label">导入文件 *</label>
            <div class="col-sm-8">
                <input id="openaccountfilename" type="file" class="file">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-8">
                <button type="submit" class="btn btn-primary">确定</button>
            </div>
        </div>
    </form>

    <hr class="clearfix" style="margin-top: 0px;">
    <div class="form-group" style="display: block;"></div>
</div>

<script src="${contextPath}/styles/fileinput/js/fileinput.min.js"></script>
<script src="${contextPath}/styles/fileinput/js/locales/zh.js"></script>
<script type="text/javascript">
    //$.getScript("${contextPath}/styles/fileinput/js/locales/zh.js");

    function doImport(form) {
        var $form = $(form);

        var url = $form.attr('action');

        return false;
    }

    $('#openaccountfilename').fileinput({
        language : 'zh',
        showUpload : false,
        showPreview : true,
        allowedPreviewTypes : [ 'text' ],
        allowedFileExtensions : [ 'xls', 'xlsx' ],
        maxFileSize : 10240
    });

    $(document).ready(function() {

    })
</script>
