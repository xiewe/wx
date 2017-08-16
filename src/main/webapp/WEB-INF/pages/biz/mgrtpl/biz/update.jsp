<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/biz/update" onsubmit="return doSave(this, '${contextPath }/biz/list');">
        <div class="form-group">
            <label for="bizTplId" class="col-sm-2 control-label">业务模板ID *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="bizTplId" value="${biztpl.bizTplId }" placeholder="请输入业务模板ID" readonly>
            </div>
        </div>
        <div class="form-group">
            <label for="bizTplName" class="col-sm-2 control-label">业务模板名称 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="bizTplName" value="${biztpl.bizTplName }" placeholder="请输入业务模板名称">
            </div>
        </div>
        <div class="form-group">
            <label for="userType" class="col-sm-2 control-label">用户类型 *</label>
            <div class="col-sm-10">
                <select class="form-control" name="userType">
                    <option value="1" <c:if test="${biztpl.userType == 1}">selected</c:if>>SIP</option>
                    <option value="2" <c:if test="${biztpl.userType == 2}">selected</c:if>>DC</option>
                    <option value="3" <c:if test="${biztpl.userType == 3}">selected</c:if>>VoLTE</option>
                    <option value="4" <c:if test="${biztpl.userType == 4}">selected</c:if>>摄像头</option>
                    <option value="5" <c:if test="${biztpl.userType == 5}">selected</c:if>>PDT</option>
                    <option value="6" <c:if test="${biztpl.userType == 6}">selected</c:if>>POC</option>
                    <option value="7" <c:if test="${biztpl.userType == 7}">selected</c:if>>LTE数据终端</option>
                    <option value="8" <c:if test="${biztpl.userType == 8}">selected</c:if>>无线集群终端</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="bizIdList" class="col-sm-2 control-label">请选择业务 *</label>
            <div class="col-sm-10">
                <div style="display: none" class="alert alert-danger alert-checkbox">请至少选择一个！</div>
                <input type="hidden" name="bizIdList" value="">
                <div class="checkbox">
                    <label><input type="checkbox" name="bizSwitcher" value="">集群业务（开关）</label>
                </div>
                <div class="checkbox lowlevel">
                    <label><input type="checkbox" value="100100">语音单呼</label> <label><input type="checkbox" value="100200">视频单呼</label> <label><input type="checkbox" value="100301" class="level_low">本地呼出</label> <label><input type="checkbox" value="100302"
                        class="level_mid">国内呼出</label> <label><input type="checkbox" value="100303" class="level_high">国际呼出</label>
                </div>
                <div class="checkbox lowlevel">
                    <label><input type="checkbox" value="100400">短数据</label> <label><input type="checkbox" value="100500">短信</label> <label><input type="checkbox" value="100600">即时消息</label>
                </div>
                <div class="checkbox lowlevel">
                    <label><input type="checkbox" value="100700">录音</label> <label><input type="checkbox" value="100800">录像</label>
                </div>
                <div class="checkbox lowlevel">
                    <label><input type="checkbox" value="100900">语音组呼</label> <label><input type="checkbox" value="101000">可视组呼</label>
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
        if ($('input[type="checkbox"]:checked').length == 0) {
            $('.alert-checkbox').css('display', 'block');
            return false;
        } else {
            $('.alert-checkbox').css('display', 'none');
        }
        
        $(form).data("bootstrapValidator").validate();
        var flag = $(form).data("bootstrapValidator").isValid();
        if (flag) {
            _doSave(form, listUrl);
        }
        return false;
    }
    
    function initData() {
        var bizidlist = '${biztpl.bizIdList}';
        var ids = bizidlist.split(',');
        $('.lowlevel input:checkbox').each(function() {
            for (var i = 0; i < ids.length; i++) {
                if ($(this).val() == ids[i]) {
                    $(this).prop('checked', true);
                }
            }
        });
        
        var bizSwitcher = '${biztpl.bizSwitcher}';
        if (bizSwitcher == 1) {
            $('input[name="bizSwitcher"]').prop('checked', true);
            $('.lowlevel').css('display', 'none');
            $('input[name="bizSwitcher"]').val(1);
        } else {
            $('.lowlevel').css('display', 'block');
            $('input[name="bizSwitcher"]').val(0);
        }

    }

    $(document).ready(function() {

        $('input[name="bizSwitcher"]').on('click', function(e) {
            if (true == $(this).prop('checked')) {
                $('.lowlevel').css('display', 'none');
                $(this).val(1);
            } else {
                $('.lowlevel').css('display', 'block');
                $(this).val(0);
            }
            
            $('#saveForm').data("bootstrapValidator").resetForm();
        });

        $('.level_low').on('click', function(e) {
            if (false == $(this).prop('checked')) {
                $('.level_mid').prop('checked', false);
                $('.level_high').prop('checked', false);
            }
        });

        $('.level_mid').on('click', function(e) {
            if (true == $(this).prop('checked')) {
                $('.level_low').prop('checked', true);
                $('.level_high').prop('checked', false);
            } else {
                $('.level_low').prop('checked', false);
                $('.level_high').prop('checked', false);
            }
        });

        $('.level_high').on('click', function(e) {
            if (true == $(this).prop('checked')) {
                $('.level_mid').prop('checked', true);
                $('.level_low').prop('checked', true);
            }
        });

        $('.lowlevel input:checkbox').on('change', function(e) {
            var bizidlist = '';
            $('.lowlevel input:checkbox:checked').each(function() {
                bizidlist += $(this).val() + ',';
            });
            bizidlist = trimT(bizidlist, 1);
            $('input[name="bizIdList"]').val(bizidlist);

            $('#saveForm').data("bootstrapValidator").resetForm();
        });
        
        initData();

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