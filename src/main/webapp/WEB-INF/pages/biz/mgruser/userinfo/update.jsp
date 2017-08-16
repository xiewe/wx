<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="divbc">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 主页</li>
        <li>用户管理</li>
        <li>用户开户</li>
    </ol>
</div>

<div class="row main-content" style="font-size: 1.2rem;">
    <form role="form" class="form-horizontal" method="post" action="${contextPath }/userinfo/update" id="saveForm" onsubmit="return doSave(this);">
        <div class="form-group">
            <div class="col-sm-7">
                <span class="label" style="margin-left: 20px; font-size: 1.5rem; font-weight: 100; border-radius: 0; background-color: #4d9e4f;">修改用户</span>
            </div>
            <div class="col-sm-3 text-right">
                <button class="btn btn-default doBackward">返回用户列表</button>
            </div>
        </div>
        <div class="form-group">
            <label for="orgId" class="control-label col-md-1 col-sm-6">组织:</label>
            <div class="col-md-3 col-sm-6">
                <input type="hidden" name="orgId" value="${useraccount.orgId}">
                <c:forEach var="item" items="${resorgs}">
                    <c:if test="${useraccount.orgId == item.orgId }">
                        <input type="text" class="form-control" value="${item.orgName}" readonly>
                    </c:if>
                </c:forEach>
            </div>
            <label for="userType" class="control-label col-md-1 col-sm-6">用户类型 *</label>
            <div class="col-md-3 col-sm-6">
                <input type="hidden" name="userType" id="userType" value="${useraccount.userType}">
                <c:if test="${useraccount.userType == 1}">
                    <input type="text" class="form-control" value="SIP" readonly>
                </c:if>
                <c:if test="${useraccount.userType == 2}">
                    <input type="text" class="form-control" value="DC" readonly>
                </c:if>
                <c:if test="${useraccount.userType == 3}">
                    <input type="text" class="form-control" value="VoLTE" readonly>
                </c:if>
                <c:if test="${useraccount.userType == 4}">
                    <input type="text" class="form-control" value="摄像头" readonly>
                </c:if>
                <c:if test="${useraccount.userType == 5}">
                    <input type="text" class="form-control" value="PDT" readonly>
                </c:if>
                <c:if test="${useraccount.userType == 6}">
                    <input type="text" class="form-control" value="POC" readonly>
                </c:if>
                <c:if test="${useraccount.userType == 7}">
                    <input type="text" class="form-control" value="LTE数据终端" readonly>
                </c:if>
                <c:if test="${useraccount.userType == 8}">
                    <input type="text" class="form-control" value="无线集群终端" readonly>
                </c:if>
            </div>
        </div>

        <div class="form-group col-md-10 col-sm-12" id="chooseNo">
            <div class="panel panel-primary" style="margin: 0 0 0 15px;">
                <div class="panel-heading">
                    <h4 class="panel-title">选择号码</h4>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <!-- <label for="searchNo" class="control-label col-md-2 col-sm-6">您喜欢的号码</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="searchNo" id="searchNo" placeholder="请输入用户名">
                        </div>
                        <div class="col-md-2 col-sm-6">
                            <button type="submit" class="form-control btn btn-primary doSearchNo">查询</button>
                        </div> -->
                        <label for="subNo" class="control-label col-md-2 col-sm-6">已选</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="subNo" id="subNo" value="${useraccount.subNo}" readonly>
                        </div>
                    </div>
                    <!-- <div style="display: none" class="alert alert-danger" id="noMandatory">请选择号码</div>
                    <table class="table table-bordered table-condensed" id="noData" style="margin-bottom: 5px;">
                        <tbody>
                            <tr>
                                <td>1321</td>
                                <td>4665</td>
                                <td>3155</td>
                                <td>9543</td>
                            </tr>
                            <tr>
                                <td>1321</td>
                                <td>4665</td>
                                <td>3155</td>
                                <td>9543</td>
                            </tr>
                            <tr>
                                <td>1321</td>
                                <td>4665</td>
                                <td>3155</td>
                                <td>9543</td>
                            </tr>
                            <tr>
                                <td>1321</td>
                                <td>4665</td>
                                <td>3155</td>
                                <td>9543</td>
                            </tr>
                            <tr>
                                <td>1321</td>
                                <td>4665</td>
                                <td>3155</td>
                                <td>9543</td>
                            </tr>
                        </tbody>
                    </table>
                    <input type="hidden" name="noCurrPage" id="noCurrPage" value="1">
                    <div class="text-right">
                        <button type="submit" class="btn btn-primary doPre4No">上一页</button>
                        <button type="submit" class="btn btn-primary doNext4No">下一页</button>
                    </div> -->
                </div>
            </div>
        </div>

        <div class="form-group col-md-10 col-sm-12" style="display: none;" id="chooseImsi">
            <div class="panel panel-primary" style="margin: 0 0 0 15px;">
                <div class="panel-heading">
                    <h4 class="panel-title">选择IMSI</h4>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <!-- <label for="searchImsi" class="control-label col-md-2 col-sm-6">您喜欢的IMSI</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="searchImsi" id="searchImsi" placeholder="请输入IMSI">
                        </div>
                        <div class="col-md-2 col-sm-6">
                            <button type="submit" class="form-control btn btn-primary doSearchImsi">查询</button>
                        </div> -->
                        <label for="imsi" class="control-label col-md-2 col-sm-6">已选</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="imsi" id="imsi" value="${useraccount.imsi}" readonly>
                        </div>
                    </div>
                    <!-- <div style="display: none" class="alert alert-danger" id="imsiMandatory">请选择IMSI</div>
                    <table class="table table-bordered table-condensed" id="imsiData" style="margin-bottom: 5px;">
                        <tbody>
                            <tr>
                                <td>1321ABCDEFABCDEF</td>
                                <td>4665ABCDEFABCDEF</td>
                                <td>3155ABCDEFABCDEF</td>
                                <td>9543ABCDEFABCDEF</td>
                            </tr>
                            <tr>
                                <td>1321ABCDEFABCDEF</td>
                                <td>4665ABCDEFABCDEF</td>
                                <td>3155ABCDEFABCDEF</td>
                                <td>9543ABCDEFABCDEF</td>
                            </tr>
                            <tr>
                                <td>1321ABCDEFABCDEF</td>
                                <td>4665ABCDEFABCDEF</td>
                                <td>3155ABCDEFABCDEF</td>
                                <td>9543ABCDEFABCDEF</td>
                            </tr>
                            <tr>
                                <td>1321ABCDEFABCDEF</td>
                                <td>4665ABCDEFABCDEF</td>
                                <td>3155ABCDEFABCDEF</td>
                                <td>9543ABCDEFABCDEF</td>
                            </tr>
                            <tr>
                                <td>1321ABCDEFABCDEF</td>
                                <td>4665ABCDEFABCDEF</td>
                                <td>3155ABCDEFABCDEF</td>
                                <td>9543ABCDEFABCDEF</td>
                            </tr>
                        </tbody>
                    </table>
                    <input type="hidden" name="imsiCurrPage" id="imsiCurrPage" value="1">
                    <div class="text-right">
                        <button type="submit" class="btn btn-primary doPre4Imsi">上一页</button>
                        <button type="submit" class="btn btn-primary doNext4Imsi">下一页</button>
                    </div> -->
                </div>
            </div>
        </div>

        <div class="form-group col-md-10 col-sm-12">
            <div class="panel panel-primary" style="margin: 0 0 0 15px;">
                <div class="panel-body">

                    <div class="form-group">
                        <label for="subName" class="control-label col-md-1 col-sm-6">用户名 *</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="subName" value="${useraccount.subName}" placeholder="请输入用户名">
                        </div>
                        <label for="subPassword" class="control-label col-md-1 col-sm-6">密码 *</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="subPassword" value="${useraccount.subPassword}" placeholder="请输入密码">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="bizTplId" class="control-label col-md-1 col-sm-6">业务模板 *</label>
                        <div class="col-md-3 col-sm-6">
                            <select class="form-control" name="bizTplId">
                                <c:forEach var="item" items="${biztpls}">
                                    <c:choose>
                                        <c:when test="${useraccount.bizTplId == item.bizTplId }">
                                            <option value="${item.bizTplId }" selected>${item.bizTplName}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${item.bizTplId }">${item.bizTplName}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3 col-sm-6">
                            <!-- <a class="btn btn-link">详情</a> -->
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="noCondFwdNo" class="control-label col-md-1 col-sm-6">无条件呼叫前转号码 </label>
                        <div class="col-md-2 col-sm-6">
                            <input type="text" class="form-control" name="noCondFwdNo" value="${useraccount.noCondFwdNo}" placeholder="请输入无条件呼叫前转号码">
                        </div>
                        <label for="noReachFwdNo" class="control-label col-md-1 col-sm-6">不可达呼叫前转号码 </label>
                        <div class="col-md-2 col-sm-6">
                            <input type="text" class="form-control" name="noReachFwdNo" value="${useraccount.noReachFwdNo}" placeholder="请输入不可达呼叫前转号码">
                        </div>
                        <label for="noReplyFwdNo" class="control-label col-md-1 col-sm-6">无应答呼叫前转号码 </label>
                        <div class="col-md-2 col-sm-6">
                            <input type="text" class="form-control" name="noReplyFwdNo" value="${useraccount.noReplyFwdNo}" placeholder="请输入无应答呼叫前转号码">
                        </div>
                        <label for="busyFwdNo" class="control-label col-md-1 col-sm-6">遇忙前转号码 </label>
                        <div class="col-md-2 col-sm-6">
                            <input type="text" class="form-control" name="busyFwdNo" value="${useraccount.busyFwdNo}" placeholder="请输入遇忙前转号码">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="emgySingleNo" class="control-label col-md-1 col-sm-6 emgyNoClass" style="display: none;">紧急单呼号码 </label>
                        <div class="col-md-2 col-sm-6 emgyNoClass" style="display: none;">
                            <input type="text" class="form-control" name="emgySingleNo" value="${useraccount.emgySingleNo}" placeholder="请输入紧急单呼号码">
                        </div>
                        <label for="emgyGroupNo" class="control-label col-md-1 col-sm-6 emgyNoClass" style="display: none;">紧急组呼号码 </label>
                        <div class="col-md-2 col-sm-6 emgyNoClass" style="display: none;">
                            <input type="text" class="form-control" name="emgyGroupNo" value="${useraccount.emgyGroupNo}" placeholder="请输入紧急组呼号码">
                        </div>
                        <label for="pnasRegCircle" class="control-label col-md-1 col-sm-6">PNAS注册周期 (s)</label>
                        <div class="col-md-2 col-sm-6">
                            <input type="text" class="form-control" name="pnasRegCircle" value="${useraccount.pnasRegCircle}"
                                placeholder="请输入PNAS注册周期 ">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-group col-md-10 col-sm-12" style="display: none;" id="apngroupcfg">
            <div class="panel panel-primary" style="margin: 0 0 0 15px;">
                <div class="panel-heading">
                    <h4 class="panel-title">APN组配置</h4>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <label for="apnGroupId_apnIdList" class="control-label col-md-1 col-sm-6">APN组 *</label>
                        <div class="col-md-3 col-sm-6">
                            <select class="form-control" name="apnGroupId_apnIdList" id="apnGroupId_apnIdList">
                                <option value=""></option>
                                <c:forEach var="item" items="${apngrouptpls}">
                                    <c:choose>
                                        <c:when test="${useraccount.apnGroupId == item.apnGroupId }">
                                            <option value="${item.apnGroupId }-${item.apnIdList }" selected>${item.apnGroupName}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${item.apnGroupId }-${item.apnIdList }">${item.apnGroupName}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <input type="hidden" name="apnGroupId" value="${useraccount.apnGroupId==null?'0':useraccount.apnGroupId}"> <input
                        type="hidden" name="apnNumbers" value="${useraccount.apnNumbers==null?'0':useraccount.apnNumbers}"> <input
                        type="hidden" name="apnIdList" value="${useraccount.apnIdList}"> <input type="hidden" name="ipAllocationType"
                        value="${useraccount.ipAllocationType}"> <input type="hidden" name="ipv4List" value="${useraccount.ipv4List}">
                    <input type="hidden" name="ipv6List" value="${useraccount.ipv6List}">
                    <div class="apnContent">

                        <c:forEach var="item" items="${apntpls}">
                            <div class="form-group apnClass" style="display: none;" data-apnid="${item.apnId }">
                                <label for="ipAType" class="control-label col-md-3 col-sm-6">${item.apnId } - ${item.oi } - ${item.ni }:</label>
                                <div class="col-md-2 col-sm-6">
                                    <select class="form-control ipAType" name="ipAType">
                                        <option value="1">静态IP</option>
                                        <option value="2">动态IP</option>
                                    </select>
                                </div>
                                <label for="apnIPv4" class="control-label col-md-2 col-sm-6">IPv4地址:</label>
                                <div class="col-md-4 col-sm-6">
                                    <input type="text" class="form-control apnIPv4" name="apnIPv4" placeholder="请输入用户名">
                                </div>
                            </div>
                        </c:forEach>

                    </div>
                </div>
            </div>
        </div>

        <div class="form-group col-md-10 col-sm-12">
            <div class="panel panel-primary" style="margin: 0 0 0 15px;">
                <div class="panel-heading">
                    <h4 class="panel-title">组织属性</h4>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <label for="roleInOrg" class="control-label col-md-1 col-sm-6">角色</label>
                        <div class="col-md-3 col-sm-6">
                            <select class="form-control" name="roleInOrg" id="roleInOrg">
                                <option value="1" <c:if test="${useraccount.roleInOrg == 1}">selected</c:if>>内网用户</option>
                                <option value="2" <c:if test="${useraccount.roleInOrg == 2}">selected</c:if>>组织调度员</option>
                                <option value="3" <c:if test="${useraccount.roleInOrg == 3}">selected</c:if>>组织块调度员</option>
                            </select>
                        </div>
                        <label for="truncPrio" class="control-label col-md-1 col-sm-6">集群优先级 </label>
                        <div class="col-md-3 col-sm-6">
                            <select class="form-control" name="truncPrio">
                                <option value="1" <c:if test="${useraccount.truncPrio == 1}">selected</c:if>>1</option>
                                <option value="2" <c:if test="${useraccount.truncPrio == 2}">selected</c:if>>2</option>
                                <option value="3" <c:if test="${useraccount.truncPrio == 3}">selected</c:if>>3</option>
                                <option value="4" <c:if test="${useraccount.truncPrio == 4}">selected</c:if>>4</option>
                                <option value="5" <c:if test="${useraccount.truncPrio == 5}">selected</c:if>>5</option>
                                <option value="6" <c:if test="${useraccount.truncPrio == 6}">selected</c:if>>6</option>
                                <option value="7" <c:if test="${useraccount.truncPrio == 7}">selected</c:if>>7</option>
                            </select>
                        </div>
                        <label for="pgis" class="control-label col-md-1 col-sm-6 roleInOrgClass" style="display: block;">PGIS</label>
                        <div class="col-md-3 col-sm-6 roleInOrgClass" style="display: block;">
                            <select class="form-control" name="pgis">
                                <option value="1" <c:if test="${useraccount.pgis == 1}">selected</c:if>>开通</option>
                                <option value="2" <c:if test="${useraccount.pgis == 2}">selected</c:if>>关闭</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="callRightInOrg" class="control-label col-md-1 col-sm-6">组织呼入呼出权限 </label>
                        <div class="col-md-3 col-sm-6">
                            <select class="form-control" name="callRightInOrg">
                                <option value="1" <c:if test="${useraccount.callRightInOrg == 1}">selected</c:if>>只允许组织内出呼</option>
                                <option value="2" <c:if test="${useraccount.callRightInOrg == 2}">selected</c:if>>只允许组织内入呼</option>
                                <option value="3" <c:if test="${useraccount.callRightInOrg == 3}">selected</c:if>>允许组织内出呼和入呼</option>
                                <option value="4" <c:if test="${useraccount.callRightInOrg == 4}">selected</c:if>>允许组织外出呼（含组织内出入呼）</option>
                                <option value="5" <c:if test="${useraccount.callRightInOrg == 5}">selected</c:if>>允许组织外入呼（含组织内出入呼）</option>
                                <option value="6" <c:if test="${useraccount.callRightInOrg == 6}">selected</c:if>>允许组织外出呼和入呼</option>
                            </select>
                        </div>
                        <label for="callAuth" class="control-label col-md-1 col-sm-6 roleInOrgClass" style="display: block;">授权呼叫 </label>
                        <div class="col-md-3 col-sm-6 roleInOrgClass" style="display: block;">
                            <select class="form-control" name="callAuth" id="callAuth">
                                <option value="1" <c:if test="${useraccount.callAuth == 1}">selected</c:if>>无</option>
                                <option value="2" <c:if test="${useraccount.callAuth == 2}">selected</c:if>>全部呼叫</option>
                                <option value="3" <c:if test="${useraccount.callAuth == 3}">selected</c:if>>国内长途呼叫</option>
                                <option value="4" <c:if test="${useraccount.callAuth == 4}">selected</c:if>>国际长途呼叫</option>
                            </select>
                        </div>
                        <label for="authGDCNo" class="control-label col-md-1 col-sm-6 authGDCNoClass" style="display: none;">授权调度台号 </label>
                        <div class="col-md-3 col-sm-6 authGDCNoClass" style="display: none;">
                            <input type="text" class="form-control" name="authGDCNo" value="${useraccount.authGDCNo}" placeholder="请输入授权调度台号">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="subShortCode" class="control-label col-md-1 col-sm-6">短号 </label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="subShortCode" value="${useraccount.subShortCode}" placeholder="请输入短号">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-group" style="margin-left: 15px;">
            <div class="col-sm-10 text-center">
                <button type="submit" class="btn btn-primary" style="width: 180px;">提交</button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    function doSave(form) {
        $(form).data("bootstrapValidator").validate();

        if ($('#subNo').val() == undefined || $('#subNo').val() == "") {
            $('#noMandatory').css('display', 'block');
            return false;
        } else {
            $('#noMandatory').css('display', 'none');
        }
        var userType = $('#userType').val();
        if (userType == 3 || userType == 7 || userType == 8) {
            if ($('#imsi').val() == undefined || $('#imsi').val() == "") {
                $('#imsiMandatory').css('display', 'block');
                return false;
            } else {
                $('#imsiMandatory').css('display', 'none');
            }
        }

        var flag = $(form).data("bootstrapValidator").isValid();
        if (flag) {
            setApnCfg();
            var $form = $(form);

            var url = $form.attr('action');
            $.ajax({
                type : "post",
                url : url,
                data : $form.serializeArray(),
                dataType : 'json'
            }).done(function(result, textStatus, jqXHR) {
                if (result.status == 0) {
                    loadContent("${contextPath }/userinfo/list");
                } else {
                    showAlert('错误', '创建错误 - ' + result.errMsg);
                }
            }).fail(function(jqXHR, textStatus, errorThrown) {
                console.log(textStatus + " - " + errorThrown);
                showAlert('错误', '创建错误:' + errorThrown);
            }).always(function() {
            });
        }
        return false;
    }

    function _setApnCfg(gip, idlist) {
        var ids = idlist.split(',');
        var iptypes = "";
        var ipv4s = "";
        var ipv6s = "";

        for ( var i in ids) {

            $('.apnClass').each(function(e) {
                if ($(this).data('apnid') == ids[i]) {
                    $(this).css('display', 'block');
                    iptypes += $(this).find('.ipAType').val() + ',';
                    ipv4s += $(this).find('.apnIPv4').val() + ',';
                    ipv6s += ',';
                }
            })
        }

        $('input[name="apnGroupId"]').val(gip);
        $('input[name="apnNumbers"]').val(ids.length);
        $('input[name="apnIdList"]').val(idlist);
        $('input[name="ipAllocationType"]').val(trimT(iptypes, 1));
        $('input[name="ipv4List"]').val(trimT(ipv4s, 1));
        $('input[name="ipv6List"]').val(trimT(ipv6s, 1));

    }

    function setApnCfg() {
        var apnGroupId_apnIdList = $('#apnGroupId_apnIdList').val();

        if (!isEmpty(apnGroupId_apnIdList)) {
            var arr = apnGroupId_apnIdList.split('-');
            _setApnCfg(arr[0], arr[1]);
        }
    }

    function initApnCfg() {
        var gip = $('input[name="apnGroupId"]').val();
        var apnNumbers = $('input[name="apnNumbers"]').val();
        var idlist = $('input[name="apnIdList"]').val();

        $('.apnClass').css('display', 'none');

        if (!isEmpty(gip) && !isEmpty(idlist)) {
            $('#apnGroupId_apnIdList').val(gip + '-' + idlist);

            var ids = idlist.split(',');
            var iptypes = $('input[name="ipAllocationType"]').val().split(',');
            var ipv4s = $('input[name="ipv4List"]').val().split(',');
            for ( var i in ids) {

                $('.apnClass').each(function(e) {
                    if ($(this).data('apnid') == ids[i]) {
                        $(this).css('display', 'block');
                        $(this).find('.ipAType').val(iptypes[i]);
                        $(this).find('.apnIPv4').val(ipv4s[i]);
                    }
                })
            }

        }
    }

    function initData() {
        var userType = $('#userType').val();
        if (userType == 3 || userType == 7 || userType == 8) {
            $('#chooseImsi').css('display', 'block');
            $('#apngroupcfg').css('display', 'block');
            if (userType == 8) {
                $('.emgyNoClass').css('display', 'block');
            } else {
                $('.emgyNoClass').css('display', 'none');
            }
        } else {
            $('#chooseImsi').css('display', 'none');
            $('#apngroupcfg').css('display', 'none');
        }

        var callAuth = $('#callAuth').val();
        if (callAuth == 1) {
            $('.authGDCNoClass').css('display', 'none');
        } else {
            $('.authGDCNoClass').css('display', 'block');
        }

        var roleInOrg = $('#roleInOrg').val();
        if (roleInOrg == 1) {
            $('.roleInOrgClass').css('display', 'block');
        } else {
            $('.roleInOrgClass').css('display', 'none');
            $('#callAuth').val(1);
            $('.authGDCNoClass').css('display', 'none');
        }

    }

    $(document).ready(function() {
        initApnCfg();
        initData();

        $('.doBackward').on('click', function(e) {
            stopBubble(e);
            stopDefault(e);

            loadContent("${contextPath }/userinfo/list");
        })

        $('#imsiData td').on('click', function(e) {
            stopBubble(e);
            stopDefault(e);

            $('#imsiData td').removeClass('success');
            $(this).addClass('success');
            $('#imsi').val($(this).text());
            console.log($('#imsi').val());
        })

        $('#noData td').on('click', function(e) {
            stopBubble(e);
            stopDefault(e);

            $('#noData td').removeClass('success');
            $(this).addClass('success');
            $('#subNo').val($(this).text());
            console.log($('#subNo').val());
        })

        $('.doSearchImsi').on('click', function(e) {
            stopBubble(e);
            stopDefault(e);

            var vv = $('#searchImsi').val();
            console.log(vv);
        })

        $('.doSearchNo').on('click', function(e) {
            stopBubble(e);
            stopDefault(e);

            var vv = $('#searchNo').val();
            console.log(vv);
        })

        $('.doPre4Imsi').on('click', function(e) {
            stopBubble(e);
            stopDefault(e);

            var vv = parseInt($('#imsiCurrPage').val());
            if (vv > 1) {
                vv = vv - 1;
            }
            $('#imsiCurrPage').val(vv);

            console.log(vv);
        })
        $('.doNext4Imsi').on('click', function(e) {
            stopBubble(e);
            stopDefault(e);

            var vv = parseInt($('#imsiCurrPage').val());
            vv = vv + 1;
            $('#imsiCurrPage').val(vv);

            console.log(vv);
        })

        $('.doPre4No').on('click', function(e) {
            stopBubble(e);
            stopDefault(e);

            var vv = parseInt($('#noCurrPage').val());
            if (vv > 1) {
                vv = vv - 1;
            }
            $('#noCurrPage').val(vv);

            console.log(vv);
        })
        $('.doNext4No').on('click', function(e) {
            stopBubble(e);
            stopDefault(e);

            var vv = parseInt($('#noCurrPage').val());
            vv = vv + 1;
            $('#noCurrPage').val(vv);

            console.log(vv);
        })

        $('#userType').on('change', function(e) {
            var userType = $(this).val();
            if (userType == 3 || userType == 7 || userType == 8) {
                $('#chooseImsi').css('display', 'block');
                $('#apngroupcfg').css('display', 'block');
                if (userType == 8) {
                    $('.emgyNoClass').css('display', 'block');
                } else {
                    $('.emgyNoClass').css('display', 'none');
                }
            } else {
                $('#chooseImsi').css('display', 'none');
                $('#apngroupcfg').css('display', 'none');
            }
        })

        $('#callAuth').on('change', function(e) {
            var callAuth = $(this).val();
            if (callAuth == 1) {
                $('.authGDCNoClass').css('display', 'none');
            } else {
                $('.authGDCNoClass').css('display', 'block');
            }
        })

        $('#roleInOrg').on('change', function(e) {
            var roleInOrg = $(this).val();
            if (roleInOrg == 1) {
                $('.roleInOrgClass').css('display', 'block');
            } else {
                $('.roleInOrgClass').css('display', 'none');
                $('#callAuth').val(1);
                $('.authGDCNoClass').css('display', 'none');
            }
        })

        $('#apnGroupId_apnIdList').on('change', function(e) {
            $('.apnClass').css('display', 'none');
            setApnCfg();
        })

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
                subName : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                subPassword : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                pnasRegCircle : {
                    validators : {
                        numeric : {}
                    }
                },
                noCondFwdNo : {
                    validators : {
                        digits : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                noReachFwdNo : {
                    validators : {
                        digits : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                noReplyFwdNo : {
                    validators : {
                        digits : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                busyFwdNo : {
                    validators : {
                        digits : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                emgyGroupNo : {
                    validators : {
                        digits : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                emgySingleNo : {
                    validators : {
                        digits : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                authGDCNo : {
                    validators : {
                        digits : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                subShortCode : {
                    validators : {
                        digits : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                apnGroupId_apnIdList : {
                    validators : {
                        notEmpty : {}
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
        });

    })
</script>
