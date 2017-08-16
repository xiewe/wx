<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/userinfo/changeimsi"
        onsubmit="return doSave(this, '${contextPath }/userinfo/list');">
        <input type="hidden" name="subNo" value="${useraccount.subNo}">
        <div class="form-group">
            <label for="oldIMSI" class="col-sm-2 control-label">老IMSI</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="oldIMSI" value="${useraccount.imsi }" readonly>
            </div>
        </div>
        <div class="form-group">
            <label for="newIMSI" class="col-sm-2 control-label">新IMSI *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="newIMSI" placeholder="请输入新IMSI">
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
                newIMSI : {
                    validators : {
                        notEmpty : {},
                        hex : {},
                        stringLength : {
                            max : 16
                        }
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
        });
    });
</script>
