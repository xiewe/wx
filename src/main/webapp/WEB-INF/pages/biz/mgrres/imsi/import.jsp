<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />
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

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>资源管理</li>
        <li>IMSI管理</li>
    </ol>
</div>

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" enctype="multipart/form-data" method="post" >
        <div class="form-group">
            <label for="opId" class="col-sm-4 control-label">运用商主密钥模板名称 *</label>
            <div class="col-sm-8">
                <select class="form-control" name="opId">
                    <c:forEach var="item" items="${optpls}">
                        <option value="${item.opId }">${item.opName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="imsifilename" class="col-sm-4 control-label">上传文件 *</label>
            <div class="col-sm-8">
                <input id="imsifilename" name="imsifilename" type="file" class="file">
            </div>
        </div>
    </form>
    
    <hr class="clearfix">
    <div class="form-group" id="importresult" style="display: none;">
        <h3>
            导入详情<small>（若在后台处理数据过程中离开此页面则只能在系统日志中查看该日志）</small>：
        </h3>
        <div class="progress parse-progress" style="display: none;">
            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div>
        </div>
    </div>
</div>
<script src="${contextPath}/styles/fileinput/js/fileinput.min.js"></script>
<script src="${contextPath}/styles/fileinput/js/locales/zh.js"></script>
<script type="text/javascript">
    var parseProgress = 0;
    var parseTimer;
    var filename = "";
    function getParseProgress() {
        $.ajax({
            type : "post",
            url : "${contextPath }/imsi/import/progress",
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
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log("progress error");
        });

        $.ajax({
            type : "post",
            url : "${contextPath }/imsi/import/logs",
            data : {
                "filename" : filename
            },
            dataType : 'json'
        }).done(function(data, textStatus, jqXHR) {
            parseLogs = data.data;
            $('#importresult').append(parseLogs);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log("logs error");
        });

        if (parseProgress >= 100) {
            clearInterval(parseTimer);
            $('#importresult').append('<span class="successimport">结束</span><br>');
        }
    }

    $('#imsifilename').fileinput({
        language : 'zh',
        uploadUrl : "${contextPath }/imsi/import",
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
    $("#imsifilename").on("fileuploaded", function(event, data, previewId, index) {
        var response = data.response;

        $('#importresult').css('display', 'block');
        if (response.status == 0) {
            filename = response.data;
            parseTimer = setInterval(getParseProgress, 1000);
        } else {
            $('#importresult').append('<span class="errorimport">文件导入失败</span><br>');
        }
    });
    $('#imsifilename').on('filecleared', function(event) {
        //console.log("filecleared");
        parseProgress = 0;
        $('#importresult').css('display', 'none');
        $('#importresult').html('<h3>'+
                '导入详情<small>（若在后台处理数据过程中离开此页面则只能在系统日志中查看该日志）</small>：'+
                '</h3>'+
                '<div class="progress parse-progress" style="display: none;">'+
                '    <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div>'+
                '</div>');
    });

</script>
