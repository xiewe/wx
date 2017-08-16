<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/userstatus/create" onsubmit="return doSave(this, '${contextPath }/userstatus/list');">
        <div class="form-group">
            <label for="orgId" class="col-sm-2 control-label">组织ID *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="orgId" placeholder="请输入orgName">
            </div>
        </div>
        <div class="form-group">
            <label for="orgName" class="col-sm-2 control-label">组织名称 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="orgName" placeholder="请输入orgName">
            </div>
        </div>
        <div class="form-group">
            <label for="userType" class="col-sm-2 control-label">用户类型 *</label>
            <div class="col-sm-10">
                <select class="form-control" name="userType">
                    <option value="1">SIP</option>
                    <option value="2">DC</option>
                    <option value="3">VoLTE</option>
                    <option value="4">摄像头</option>
                    <option value="5">PDT</option>
                    <option value="6">POC</option>
                    <option value="7">LTE数据终端</option>
                    <option value="8">无线集群终端</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="subNo" class="col-sm-2 control-label">用户号码 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="subNo" placeholder="请输入用户号码">
            </div>
        </div>
        <div class="form-group">
            <label for="imsi" class="col-sm-2 control-label">IMSI *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="imsi" placeholder="请输入imsi">
            </div>
        </div>
        <div class="form-group">
            <label for="attachStatus" class="col-sm-2 control-label">附着状态 *</label>
            <div class="col-sm-10">
                <select class="form-control" name="attachStatus">
                        <option value=""></option>
                        <option value="0" >未附着</option>
                        <option value="1" >已附着</option>
                    </select>
            </div>
        </div>
        <div class="form-group">
            <label for="registerStatus" class="col-sm-2 control-label">注册状态 *</label>
            <div class="col-sm-10">
                <select class="form-control" name="registerStatus">
                        <option value=""></option>
                        <option value="0" >未注册</option>
                        <option value="1" >已注册</option>
                    </select>
            </div>
        </div>
        <div class="form-group">
            <label for="guti" class="col-sm-2 control-label">GUTI *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="guti" placeholder="请输入guti">
            </div>
        </div>
        <div class="form-group">
            <label for="tai" class="col-sm-2 control-label">TAI *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="tai" placeholder="请输入tai">
            </div>
        </div>
        <div class="form-group">
            <label for="accessWay" class="col-sm-2 control-label">接入方式*</label>
            <div class="col-sm-10">
                <select class="form-control" name="accessWay">
                        <option value="1">LTE接入</option>
                        <option value="2">SIP接入</option>
                        <option value="3">POC接入</option>
                    </select>
            </div>
        </div>
        <div class="form-group">
            <label for="currIp" class="col-sm-2 control-label">当前IP *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="currIp" placeholder="请输入当前IP">
            </div>
        </div>
        <div class="form-group">
            <label for="currAPN" class="col-sm-2 control-label">当前APN *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="currAPN" placeholder="请输入当前APN">
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
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
        });
    });
</script>
