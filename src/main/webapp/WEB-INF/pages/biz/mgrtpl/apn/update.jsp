<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/apn/update" onsubmit="return doSave(this, '${contextPath }/apn/list');">
        <div class="form-group">
            <label for="apnId" class="col-sm-4 control-label">APN ID *</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="apnId" value="${apntpl.apnId}" placeholder="请输入APN ID" readonly>
            </div>
        </div>
        <div class="form-group">
            <label for="oi" class="col-sm-4 control-label">运营商标识（OI） *</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="oi" value="${apntpl.oi}" placeholder="请输入运营商标识">
            </div>
        </div>
        <div class="form-group">
            <label for="ni" class="col-sm-4 control-label">网络标识（NI） *</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="ni" value="${apntpl.ni}" placeholder="请输入网络标识">
            </div>
        </div>
        <div class="form-group">
            <label for="qci" class="col-sm-4 control-label">QCI *</label>
            <div class="col-sm-8">
                <select class="form-control" name="qci">
                    <c:forEach var="item" begin="5" end="9" step="1">
                        <c:choose>
                            <c:when test="${apntpl.qci == item }">
                                <option value="${item }" selected>${item}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${item }">${item}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="ARPPrio" class="col-sm-4 control-label">ARP优先级 *</label>
            <div class="col-sm-8">
                <select class="form-control" name="ARPPrio">
                    <c:forEach var="item" begin="1" end="15" step="1">
                        <c:choose>
                            <c:when test="${apntpl.ARPPrio == item }">
                                <option value="${item }" selected>${item}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${item }">${item}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="preEmptionCapability" class="col-sm-4 control-label">ARP抢占标识 *</label>
            <div class="col-sm-8">
                <select class="form-control" name="preEmptionCapability">
                    <option value="0" <c:if test="${apntpl.preEmptionCapability == 0}">selected</c:if>>Enabled</option>
                    <option value="1" <c:if test="${apntpl.preEmptionCapability == 1}">selected</c:if>>Disabled</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="preEmptionVulnerablity" class="col-sm-4 control-label">ARP允许抢占标识 *</label>
            <div class="col-sm-8">
                <select class="form-control" name="preEmptionVulnerablity">
                    <option value="0" <c:if test="${apntpl.preEmptionVulnerablity == 0}">selected</c:if>>Enabled</option>
                    <option value="1" <c:if test="${apntpl.preEmptionVulnerablity == 1}">selected</c:if>>Disabled</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="maxBwUl" class="col-sm-4 control-label">上行最大带宽（kbps） *</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="maxBwUl" value="${apntpl.maxBwUl}" placeholder="请输入上行最大带宽">
            </div>
        </div>
        <div class="form-group">
            <label for="maxBwDl" class="col-sm-4 control-label">下行最大带宽（kbps） *</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="maxBwDl" value="${apntpl.maxBwDl}" placeholder="请输入下行最大带宽">
            </div>
        </div>
        <div class="form-group">
            <label for="pgwAllocationType" class="col-sm-4 control-label">PDN GW分配类型 *</label>
            <div class="col-sm-8">
                <div class="radio">
                    <label class="checkbox-inline"> 
                    <input type="radio" name="pgwAllocationType" id="optionsRadios1" value="0" <c:if test="${apntpl.pgwAllocationType == 0}">checked</c:if>> 静态
                    </label> 
                    <label class="checkbox-inline"> 
                    <input type="radio" name="pgwAllocationType" id="optionsRadios2" value="1" <c:if test="${apntpl.pgwAllocationType == 1}">checked</c:if>> 动态
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label for="ipv4" class="col-sm-4 control-label">IPv4</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="ipv4" value="${apntpl.ipv4}" placeholder="请输入ip">
            </div>
        </div>
        <div class="form-group">
            <label for="ipv6" class="col-sm-4 control-label">IPv6</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="ipv6" value="${apntpl.ipv6}" placeholder="请输入ip">
            </div>
        </div>
        <div class="form-group">
            <label for="MIPHomeAgentHost" class="col-sm-4 control-label">目的主机</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="MIPHomeAgentHost" value="${apntpl.MIPHomeAgentHost}" placeholder="请输入目的主机">
            </div>
        </div>
        <div class="form-group">
            <label for="MIPHomeAgentRealm" class="col-sm-4 control-label">目的Realm</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="MIPHomeAgentRealm" value="${apntpl.MIPHomeAgentRealm}" placeholder="请输入目的Realm">
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
                apnId : {
                    validators : {
                        notEmpty : {},
                        digits : {}
                    }
                },
                oi : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                ni : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            max : 32
                        }
                    }
                },
                maxBwUl : {
                    validators : {
                        notEmpty : {},
                        digits : {}
                    }
                },
                maxBwDl : {
                    validators : {
                        notEmpty : {},
                        digits : {}
                    }
                },
                ipv4 : {
                    validators : {
                        ip : {
                            ipv4 : true,
                            ipv6 : false
                        }
                    }
                },
                ipv6 : {
                    validators : {
                        ip : {
                            ipv4 : false,
                            ipv6 : true
                        }
                    }
                },
                MIPHomeAgentRealm : {
                    validators : {
                        stringLength : {
                            max : 128
                        }
                    }
                },
                MIPHomeAgentHost : {
                    validators : {
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