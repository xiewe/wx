<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/biz/create" onsubmit="return doSave(this, '${contextPath }/biz/list');">
        <div class="form-group">
            <label for="bizTplId" class="col-sm-2 control-label">业务模板ID *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="bizTplId" placeholder="请输入业务模板ID">
            </div>
        </div>
        <div class="form-group">
            <label for="bizTplName" class="col-sm-2 control-label">业务模板名称 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="bizTplName" placeholder="请输入业务模板名称">
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
            <label for="bizIdList" class="col-sm-2 control-label">请选择业务 *</label>
            <div class="col-sm-10">
                <input type="hidden" name="bizIdList" value="">
                <div class="checkbox">
                    <label><input type="checkbox" name="bizSwitcher" value="">集群业务（开关）</label>
                </div>
                <div class="checkbox">
                    <label><input type="checkbox" value="100100">语音单呼</label>
                    <label><input type="checkbox" value="100200">视频单呼</label>
                    <label><input type="checkbox" value="100301" class="level_low">本地呼出</label>
                    <label><input type="checkbox" value="100302" class="level_mid">国内呼出</label>
                    <label><input type="checkbox" value="100303" class="level_high">国际呼出</label>
                    <label><input type="checkbox" value="100400">短数据</label>
                    <label><input type="checkbox" value="100500">短信</label>
                    <label><input type="checkbox" value="100600">即时消息</label>
                    <label><input type="checkbox" value="100700">录音</label>
                    <label><input type="checkbox" value="100800">录像</label>
                    <label><input type="checkbox" value="100900">语音组呼</label>
                    <label><input type="checkbox" value="101000">可视组呼</label>
                </div>
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
                bizTplId : {
                    validators : {
                        notEmpty : {},
                        digits : {}
                    }
                },
                bizTplName : {
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
