<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/updatePwd" onsubmit="return doSave(this);">
        <div class="form-group">
            <label for="plainPassword" class="col-sm-2 control-label">老密码 *</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" name="plainPassword" placeholder="请输入老密码">
            </div>
        </div>
        <div class="form-group">
            <label for="newPassword" class="col-sm-2 control-label">新密码 *</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" name="newPassword" placeholder="请输入新密码">
            </div>
        </div>
        <div class="form-group">
            <label for="rPassword" class="col-sm-2 control-label">重复新密码 *</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" name="rPassword" placeholder="请重复输入新密码">
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
    function doSave(form) {
        $(form).data("bootstrapValidator").validate();
        var flag = $(form).data("bootstrapValidator").isValid();
        if (flag) {
            _doSave(form);
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
                plainPassword : {
                    validators : {
                        notEmpty : {}
                    }
                },
                newPassword : {
                    validators : {
                        notEmpty : {},
                        identical : {
                            field : 'rPassword'
                        }
                    }
                },
                rPassword : {
                    validators : {
                        notEmpty : {},
                        identical : {
                            field : 'newPassword'
                        }
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
        });
    });
</script>