<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/ip/update" onsubmit="return doSave(this, '${contextPath }/ip/list');">
        <div class="form-group">
            <label for="ipFragment" class="col-sm-2 control-label">网段 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="ipFragment" value="${ipfinfo.ipFragment }" placeholder="请输入网段">
            </div>
        </div>
        <div class="form-group">
            <label for="ipMask" class="col-sm-2 control-label">网段掩码 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="ipMask" value="${ipfinfo.ipMask }" placeholder="请输入网段掩码">
            </div>
        </div>
        <div class="form-group">
            <label for="desc" class="col-sm-2 control-label">描述</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="desc" value="${ipfinfo.desc }" placeholder="请输入描述">
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
                ipFragment : {
                    validators : {
                        notEmpty : {},
                        ip : {
                            ipv4 : true,
                            ipv6 : true
                        }
                    }
                },
                ipMask : {
                    validators : {
                        notEmpty : {},
                        ip : {
                            ipv4 : true,
                            ipv6 : true
                        }
                    }
                },
                desc : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            max : 128
                        }
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
        });
    });
</script>