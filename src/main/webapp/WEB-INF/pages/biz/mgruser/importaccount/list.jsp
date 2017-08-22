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
<style type="text/css">
.errorimport {
    color: red;
    padding-left: 10px;
}

.successimport {
    padding-left: 10px;
}
</style>
<div class="row main-content">
    <div class="row">
        <shiro:hasPermission name="IMSIInfo:create">
            <p class="col-md-12 col-sm-12 text-right">
                <a href="${contextPath }/importaccount/download" class="doDownload">模板下载</a>
            </p>
        </shiro:hasPermission>
    </div>
    <hr class="clearfix" style="margin-top: 0px;">
    <div class="row">
        <label for="openaccountfilename" class="col-sm-2 control-label text-right">导入文件 *</label>
        <div class="col-sm-8">
            <input id="openaccountfilename" name="openaccountfilename" type="file" class="file">
        </div>
    </div>

    <hr class="clearfix">
    <div class="form-group" id="importresult" style="display: none;">
        <h3>
            导入详情<small>（若在后台处理数据中离开此页面则只能在系统日志中查看导入日志）</small>：
        </h3>
        <div class="progress parse-progress" style="display: none;">
            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div>
        </div>
        <span class="successimport">row1成功</span><br> <span class="errorimport">row2失败</span><br>
    </div>
</div>

<script src="${contextPath}/styles/fileinput/js/fileinput.min.js"></script>
<script src="${contextPath}/styles/fileinput/js/locales/zh.js"></script>
<script type="text/javascript">
    var parseProgress = 0;
    var parseTimer;
    var logsTimer;
    var filename = "";
    function getParseProgress() {
        $.ajax({
            type : "post",
            url : "${contextPath }/importaccount/import/progress",
            data : {
                "filename" : filename
            },
            dataType : 'json'
        }).done(function(data, textStatus, jqXHR) {
            parseProgress = data.data;
            $('.parse-progress').css('display', 'block');
            $('.parse-progress .progress-bar').attr('aria-valuenow', parseProgress);
            $('.parse-progress .progress-bar').css('width', parseProgress + '%');
            $('.parse-progress .progress-bar').text(parseProgress + '%');
            if (parseProgress >= 100) {
                clearInterval(parseTimer);
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log("error");
        });
    }

    function getParseLogs() {
        $.ajax({
            type : "post",
            url : "${contextPath }/importaccount/import/logs",
            data : {
                "filename" : filename
            },
            dataType : 'json'
        }).done(function(data, textStatus, jqXHR) {
            parseLogs = data.data;
            if (parseLogs == undefined || parseLogs == null || parseLogs == '') {
                clearInterval(logsTimer);
                $('#importresult').append('<span class="successimport">结束</span><br>');
            } else {
                $('#importresult').append(parseLogs);
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log("error");
        });
    }

    $('#openaccountfilename').fileinput({
        language : 'zh',
        uploadUrl : "${contextPath }/importaccount/import",
        enctype : 'multipart/form-data',
        showUpload : true,
        showPreview : false,
        uploadAsync : true,
        //maxFileCount: 1,
        //showCaption: false,
        //dropZoneEnabled: false,
        allowedPreviewTypes : [ 'text' ],
        allowedFileExtensions : [ 'xls', 'xlsx' ],
        maxFileSize : 10240
    });

    //异步上传返回结果处理
    $("#openaccountfilename").on("fileuploaded", function(event, data, previewId, index) {
        var response = data.response;

        $('#importresult').css('display', 'block');
        if (response.status == 0) {
            filename = response.data;
            parseTimer = setInterval(getParseProgress, 1000);
            logsTimer = setInterval(getParseLogs, 1000);
        } else {
            $('#importresult').append('<span class="errorimport">文件导入失败</span><br>');
        }
    });

    $('#openaccountfilename').on('filepreupload', function(event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra, response = data.response, reader = data.reader;
    });
</script>
