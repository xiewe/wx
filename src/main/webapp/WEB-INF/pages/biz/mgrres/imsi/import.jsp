<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" enctype="multipart/form-data" method="post" action="${contextPath }/imsi/import"
        onsubmit="return doSave(this, '${contextPath }/imsi/list');">
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
                <div class="input-group">
                    <input type="text" class="form-control" name="imsifilename" id="imsifilename"> <span class="input-group-btn">
                        <button type="button" class="btn btn-primary">上传</button>
                    </span>
                </div>
                <input type="file" class="form-control" name="imsifile" id="imsifile"
                    style="position: absolute; right: 0px !important; width: 100%; top: 0; opacity: 0; z-index: 999;">
                <div class="clearfix progress upload-progress" style="display: none;">
                    <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div>
                </div>
            </div>
        </div>
        <div class="form-group progress parse-progress" style="display: none;">
            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-8">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="submit" class="btn btn-primary">确定</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="${contextPath}/styles/utils/ajaxfileupload.js"></script>
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
            url : "${contextPath }/imsi/import/progress",
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

    function doSave(form, listUrl) {
        var $form = $(form);
        var flag = $form.data("bootstrapValidator").isValid();
        if (!flag) {
            return false;
        }

        var timer = setInterval(updateProgress, 1000);
        var url = $form.attr('action');
        $.ajaxFileUpload({
            url : url,
            secureuri : false,
            fileElementId : 'imsifile',
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

        $('#imsifile').on('change', function(e) {
            $('#imsifilename').val(this.value);
        });

        $('#saveForm').bootstrapValidator({
            feedbackIcons : {
                valid : 'glyphicon glyphicon-ok',
                invalid : 'glyphicon glyphicon-remove',
                validating : 'glyphicon glyphicon-refresh'
            },
            fields : {
                imsifile : {
                    validators : {
                        notEmpty : {}
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
        });
    });

    asdf;
</script>
