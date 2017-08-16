<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/whitelist/create"
        onsubmit="return doSave(this, '${contextPath }/whitelist/list');">
        <div class="form-group">
            <label for="imeisv" class="col-sm-2 control-label">IMEI/IMEISV *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="imeisv" placeholder="请输入IMEI/IMEISV">
            </div>
        </div>
        <div class="form-group">
            <label for="imsi" class="col-sm-2 control-label">IMSI *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="imsi" placeholder="请输入模板名称">
            </div>
        </div>
        <div class="form-group">
            <label for="listType" class="col-sm-2 control-label">类型*</label>
            <div class="col-sm-10">
                <select class="form-control" name="listType">
                    <option value="0">白名单</option>
                    <option value="1">黑名单</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="submit" class="btn btn-primary">确定</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    function doSave(form, listUrl) {
        $(form).data("bootstrapValidator").validate();
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
                imeisv : {
                    validators : {
                        notEmpty : {},
                        hex : {},
                        stringLength : {
                            max : 16,
                            min : 14
                        }
                    }
                },
                imsi : {
                    validators : {
                        notEmpty : {},
                        hex : {},
                        stringLength : {
                            max : 15
                        }
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
        });
    });
</script>
