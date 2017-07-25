<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/resorg/create" onsubmit="return doSave(this, '${contextPath }/resorg/list');">
        <div class="form-group">
            <label for="orgName" class="col-sm-2 control-label">组织名称 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="orgName" placeholder="请输入组织名称">
            </div>
        </div>
        <div class="form-group">
            <label for="maxUserNo" class="col-sm-2 control-label">组织最大用户数 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="maxUserNo" placeholder="请输入组织最大用户数">
            </div>
        </div>
        <div class="form-group">
            <label for="maxGroupNo" class="col-sm-2 control-label">最大组数 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="maxGroupNo" placeholder="请输入最大组数">
            </div>
        </div>
        <div class="form-group">
            <label for="admin" class="col-sm-2 control-label">组织管理员 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="admin" placeholder="请输入管理员">
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="col-sm-2 control-label">管理员密码 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="password" placeholder="请输入管理员密码">
            </div>
        </div>
        <div class="form-group">
            <label for="confirmPassword" class="col-sm-2 control-label">确认密码 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="confirmPassword" placeholder="请输入确认密码">
            </div>
        </div>
        <div class="form-group">
            <label for="emgySingleCallNo" class="col-sm-2 control-label">紧急单呼号码</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="emgySingleCallNo" placeholder="请输入紧急单呼号码">
            </div>
        </div>
        <div class="form-group">
            <label for="comments" class="col-sm-2 control-label">备注</label>
            <div class="col-sm-10">
                <textarea class="form-control" rows="3" name="comments" placeholder="请输入备注"></textarea>
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
                orgName : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                maxUserNo : {
                    validators : {
                        notEmpty : {},
                        digits : {}
                    }
                },
                maxGroupNo : {
                    validators : {
                        notEmpty : {},
                        digits : {}
                    }
                },
                admin : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                password : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            max : 32
                        },
                        identical : {
                            field : 'confirmPassword'
                        }
                    }
                },
                confirmPassword : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            max : 32
                        },
                        identical : {
                            field : 'password'
                        }
                    }
                },
                emgySingleCallNo : {
                    validators : {
                        notEmpty : {},
                        digits : {}
                    }
                },
                comments : {
                    validators : {
                        stringLength : {
                            max : 200
                        }
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
        });
    });
</script>
