<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<div class="row main-content">
    <table class="table table-hover table-condensed" id="tabData">
        <tbody>
            <tr>
                <td class="text-right">APN ID</td>
                <td>${apntpl.apnId}</td>
            </tr>
            <tr>
                <td class="text-right">运营商标识（OI）</td>
                <td>${apntpl.oi}</td>
            </tr>
            <tr>
                <td class="text-right">网络标识（NI）</td>
                <td>${apntpl.ni}</td>
            </tr>
            <tr>
                <td class="text-right">QCI</td>
                <td>${apntpl.qci}</td>
            </tr>
            <tr>
                <td class="text-right">ARP优先级</td>
                <td>${apntpl.ARPPrio}</td>
            </tr>
            <tr>
                <td class="text-right">ARP抢占标识</td>
                <td>${apntpl.preEmptionCapability==0? 'Enabled' : 'Disabled'}</td>
            </tr>
            <tr>
                <td class="text-right">ARP允许抢占标识</td>
                <td>${apntpl.preEmptionVulnerablity==0? 'Enabled' : 'Disabled'}</td>
            </tr>
            <tr>
                <td class="text-right">上行最大带宽（kbps）</td>
                <td>${apntpl.maxBwUl}</td>
            </tr>
            <tr>
                <td class="text-right">下行最大带宽（kbps）</td>
                <td>${apntpl.maxBwDl}</td>
            </tr>
            <tr>
                <td class="text-right">PDN GW分配类型</td>
                <td>${apntpl.pgwAllocationType==0? '静态' : '动态'}</td>
            </tr>
            <tr>
                <td class="text-right">IPv4</td>
                <td>${apntpl.ipv4}</td>
            </tr>
            <tr>
                <td class="text-right">IPv6</td>
                <td>${apntpl.ipv6}</td>
            </tr>
            <tr>
                <td class="text-right">目的主机</td>
                <td>${apntpl.MIPHomeAgentHost}</td>
            </tr>
            <tr>
                <td class="text-right">目的Realm</td>
                <td>${apntpl.MIPHomeAgentRealm}</td>
            </tr>
            <tr>
                <td class="text-right">创建时间</td>
                <td><fmt:formatDate value="${apntpl.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
                <td class="text-right">修改时间</td>
                <td><fmt:formatDate value="${apntpl.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
        </tbody>
    </table>
    <div class="col-sm-offset-2 col-sm-10 text-right" style="margin-bottom:20px;">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</div>