<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" enctype="multipart/form-data" method="post" action="${contextPath }/imsi/import" onsubmit="return doSave(this, '${contextPath }/imsi/list');">
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
            <label for="imsi" class="col-sm-4 control-label">导入文件 *</label>
            <div class="col-sm-8">
                <div class="input-group">
                    <input type="text" class="form-control" id="imsifilename"> <span class="input-group-btn">
                        <button type="button" class="btn btn-primary">浏览</button>
                    </span>
                </div>
                <input type="file" class="form-control" name="imsifile" id="imsifile" style="position: absolute; right: 0px !important; width: 100%; top: 0; opacity: 0; z-index: 999;" onchange="document.getElementById('imsifilename').value=this.value;">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-8">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="submit" class="btn btn-primary">确定</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    function doSave(form, listUrl) {
        var flag = $(form).data("bootstrapValidator").isValid();
        if (flag) {
            _doSave(form, listUrl);
        }
        return false;
    }

    $(document).ready(function() {

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
</script>
