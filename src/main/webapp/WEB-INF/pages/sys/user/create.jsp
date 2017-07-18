<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/user/create" onsubmit="return doSave(this, '${contextPath }/user/list');">
        <div class="form-group">
            <label for="username" class="col-sm-2 control-label">用户名 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="username" placeholder="请输入用户名">
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="col-sm-2 control-label">密码 *</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" name="password" placeholder="请输入密码">
            </div>
        </div>
        <div class="form-group">
            <label for="confirmPassword" class="col-sm-2 control-label">重复密码 *</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" name="confirmPassword" placeholder="请重复输入密码">
            </div>
        </div>
        <div class="form-group">
            <label for="phone" class="col-sm-2 control-label">电话</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="phone" placeholder="请输入电话">
            </div>
        </div>
        <div class="form-group">
            <label for="email" class="col-sm-2 control-label">邮箱</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="email" placeholder="请输入邮箱">
            </div>
        </div>
        <div class="form-group">
            <label for="sysRole.id" class="col-sm-2 control-label">角色 *</label>
            <div class="col-sm-10">
                <select class="form-control" name="sysRole.id">
                    <option value=""></option>
                    <c:forEach var="item" items="${roles}">
                        <option value="${item.id }">${item.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="sysOrganization.id" class="col-sm-2 control-label">组织</label>
            <div class="col-sm-10">
                <select class="form-control" name="sysOrganization.id">
                    <option value=""></option>
                    <c:forEach var="item" items="${orgs}">
                        <option value="${item.id }">${item.name}</option>
                    </c:forEach>
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
                username : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            min : 1,
                            max : 32
                        }
                    }
                },
                password : {
                    validators : {
                        notEmpty : {},
                        identical : {
                            field : 'confirmPassword'
                        }
                    }
                },
                confirmPassword : {
                    validators : {
                        notEmpty : {},
                        identical : {
                            field : 'password'
                        }
                    }
                },
                phone : {
                    validators : {
                        phone : {
                            country : 'CN'
                        }
                    }
                },
                email : {
                    validators : {
                        emailAddress : {}
                    }
                },
                'sysRole.id' : {
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