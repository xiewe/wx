<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/no/create" onsubmit="return doSave(this, '${contextPath }/no/list');">
        <div class="form-group">
            <label for="orgId" class="col-sm-4 control-label">组织名称 *</label>
            <div class="col-sm-8">
                 <select class="form-control" name="orgId">
                    <c:forEach var="item" items="${resorgs}">
                        <option value="${item.orgId }">${item.orgName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="phoneNoType" class="col-sm-4 control-label">号码类型 *</label>
            <div class="col-sm-8">
                 <select class="form-control" name="phoneNoType">
                    <option value="1">用户号码</option>
                    <option value="2">组号码</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="phoneNoStart" class="col-sm-4 control-label">起始号码 *</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="phoneNoStart" placeholder="请输入起始号码">
            </div>
        </div>
        <div class="form-group">
            <label for="numbers" class="col-sm-4 control-label">号码个数 *</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="numbers" placeholder="请输入号码个数">
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
                orgId : {
                    validators : {
                        notEmpty : {}
                    }
                },
                phoneNoType : {
                    validators : {
                        notEmpty : {}
                    }
                },
                phoneNoStart : {
                    validators : {
                        notEmpty : {},
                        digits : {},
                        stringLength: {
                        	min:1,
                        	max:10
                        }
                    }
                },
                numbers : {
                    validators : {
                        notEmpty : {},
                        digits : {}
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
        });
    });
</script>
