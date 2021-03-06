<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/imsi/create" onsubmit="return doSave(this, '${contextPath }/imsi/list');">
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
            <label for="imsi" class="col-sm-4 control-label">IMSI *</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="imsi" placeholder="请输入imsi">
            </div>
        </div>
        <div class="form-group">
            <label for="k" class="col-sm-4 control-label">密钥 *</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="k" placeholder="请输入密钥">
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
                imsi : {
                    validators : {
                        notEmpty : {},
                        hex : {},
                        stringLength : {
                            max : 16
                        }
                    }
                },
                k : {
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
