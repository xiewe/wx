<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/op/update" onsubmit="return doSave(this, '${contextPath }/op/list');">
        <div class="form-group">
            <label for="opId" class="col-sm-2 control-label">OP ID *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="opId" value="${optpl.opId}" placeholder="请输入OP ID" disabled>
            </div>
        </div>
        <div class="form-group">
            <label for="opName" class="col-sm-2 control-label">模板名称 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="opName" value="${optpl.opName}" placeholder="请输入模板名称">
            </div>
        </div>
        <div class="form-group">
            <label for="opValue" class="col-sm-2 control-label">运营商可变算法配置域 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="opValue" value="${optpl.opValue}" placeholder="请输入运营商可变算法配置域">
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
                opId : {
                    validators : {
                        notEmpty : {},
                        digits : {}
                    }
                },
                opName : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                opValue : {
                    validators : {
                        notEmpty : {},
                        hex : {},
                        stringLength : {
                            max : 32
                        }
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
        });
    });
</script>