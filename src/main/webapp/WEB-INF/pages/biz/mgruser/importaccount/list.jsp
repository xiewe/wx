<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>用户管理</li>
        <li>批量开户</li>
    </ol>
</div>

<div class="row main-content">
    <div class="row">
        <shiro:hasPermission name="IMSIInfo:create">
            <p class="col-md-12 col-sm-12 text-right">
                <a href="${contextPath }/importaccount/download" class="doDownload">模板下载</a>
            </p>
        </shiro:hasPermission>
    </div>
    <hr class="clearfix" style="margin-top: 0px;">
    <form id="saveForm" class="form-horizontal" role="form" enctype="multipart/form-data" method="post" action="${contextPath }/importaccount/import"
        onsubmit="return doImport(this);">
        <div class="form-group">
            <label for="openaccountfilename" class="col-sm-2 control-label">导入文件 *</label>
            <div class="col-sm-4">
                <div class="input-group">
                    <input type="text" class="form-control" name="openaccountfilename" id="openaccountfilename"> <span class="input-group-btn">
                        <button type="button" class="btn btn-primary">浏览</button>
                    </span>
                </div>
                <input type="file" class="form-control" name="openaccountfile" id="openaccountfile"
                    style="position: absolute; right: 0px !important; width: 100%; top: 0; opacity: 0; z-index: 999;">
                <div class="clearfix progress upload-progress" style="display: none;">
                    <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-8">
                <button type="submit" class="btn btn-primary">确定</button>
            </div>
        </div>
    </form>

    <hr class="clearfix" style="margin-top: 0px;">
    <div class="form-group progress parse-progress" style="display: block;">
        <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div>
    </div>
</div>

<script type="text/javascript">
    var progress = 0;
    var filename = "";
    function updateProgress() {
        progress += 1;
        if (progress > 0 && progress <= 100) {
            $('.upload-progress').css('display', 'block');
            $('.upload-progress .progress-bar').attr('aria-valuenow', progress);
            $('.upload-progress .progress-bar').css('width', progress + '%');
            $('.upload-progress .progress-bar').text(progress + '%');
        }
    }

    var parseProgress = 0;
    var parseTimer;
    function getParseProgress() {
        $.ajax({
            type : "post",
            url : "${contextPath }/importaccount/import/progress",
            data : {
                "filename" : filename
            }
        }).done(function(data, textStatus, jqXHR) {
            parseProgress = data.data;
            $('.parse-progress').css('display', 'block');
            $('.parse-progress .progress-bar').attr('aria-valuenow', parseProgress);
            $('.parse-progress .progress-bar').css('width', parseProgress + '%');
            $('.parse-progress .progress-bar').text(parseProgress + '%');
            if (parseProgress == 100) {
                clearInterval(parseTimer);
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log("error");
        }).always(function() {
            console.log("complete");
        });
    }

    function doImport(form) {
        var $form = $(form);

        var timer = setInterval(updateProgress, 1000);
        var url = $form.attr('action');
        $.ajaxFileUpload({
            url : url,
            secureuri : false,
            fileElementId : 'openaccountfile',
            dataType : 'json'
        }).done(function(result, textStatus, jqXHR) {
            console.log(result.data);
            filename = result.data;
            clearInterval(timer);
            parseTimer = setInterval(getParseProgress, 1000);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log('failed');
        });

        return false;
    }

    $(document).ready(function() {
        $('#openaccountfile').on('change', function(e) {
            $('#openaccountfilename').val(this.value);
        });
    })
</script>
