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
    <form role="form" class="form-horizontal" method="post" action="${contextPath }/openaccount/create" id="searchForm"
        onsubmit="return doSave(this);">
        <div class="form-group">
            <span class="label" style="margin-left: 20px; font-size: 1.8rem; font-weight: 100; border-radius: 0; background-color: #4d9e4f;">新建用户</span>
        </div>
        <div class="form-group">
            <label for="orgId" class="control-label col-md-1 col-sm-6">组织:</label>
            <div class="col-md-3 col-sm-6">
                <select class="form-control" name="orgId">
                    <option value=""></option>
                    <c:forEach var="item" items="${resorgs}">
                        <c:choose>
                            <c:when test="${param.search_EQ_orgId == item.orgId }">
                                <option value="${item.orgId }" selected>${item.orgName}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${item.orgId }">${item.orgName}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
            <label for="userType" class="control-label col-md-1 col-sm-6">用户类型 *</label>
            <div class="col-md-3 col-sm-6">
                <select class="form-control" name="userType">
                    <option value=""></option>
                    <option value="1" <c:if test="${param.search_EQ_userType == 1}">selected</c:if>>SIP</option>
                    <option value="2" <c:if test="${param.search_EQ_userType == 2}">selected</c:if>>DC</option>
                    <option value="3" <c:if test="${param.search_EQ_userType == 3}">selected</c:if>>VoLTE</option>
                    <option value="4" <c:if test="${param.search_EQ_userType == 4}">selected</c:if>>摄像头</option>
                    <option value="5" <c:if test="${param.search_EQ_userType == 5}">selected</c:if>>PDT</option>
                    <option value="6" <c:if test="${param.search_EQ_userType == 6}">selected</c:if>>POC</option>
                    <option value="7" <c:if test="${param.search_EQ_userType == 7}">selected</c:if>>LTE数据终端</option>
                    <option value="8" <c:if test="${param.search_EQ_userType == 8}">selected</c:if>>无线集群终端</option>
                </select>
            </div>
        </div>

        <div class="form-group col-md-10 col-sm-12">
            <div class="panel panel-primary" style="margin: 0 0 0 15px;">
                <div class="panel-heading">
                    <h4 class="panel-title">选择号码</h4>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <label for="imsi" class="control-label col-md-2 col-sm-6">您喜欢的号码</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
                        </div>
                        <button type="submit" class="btn btn-primary">查询</button>
                    </div>
                    <table class="table table-bordered table-condensed" id="tabData">
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
                    <own:pagination pager="${pager}" />
                </div>
            </div>
        </div>

        <div class="form-group col-md-10 col-sm-12">
            <div class="panel panel-primary" style="margin: 0 0 0 15px;">
                <div class="panel-heading">
                    <h4 class="panel-title">选择IMSI</h4>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <label for="imsi" class="control-label col-md-2 col-sm-6">您喜欢的号码</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
                        </div>
                        <button type="submit" class="btn btn-primary">查询</button>
                    </div>
                    <table class="table table-bordered table-condensed" id="tabData">
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
                    <own:pagination pager="${pager}" />
                </div>
            </div>
        </div>
        <div class="form-group col-md-12 col-sm-12" style="margin-left: 15px;">
            <label for="imsi" class="control-label col-md-1 col-sm-6">用户名 *</label>
            <div class="col-md-3 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
            </div>
            <label for="imsi" class="control-label col-md-1 col-sm-6">密码 *</label>
            <div class="col-md-3 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入密码">
            </div>
        </div>
        <div class="form-group" style="margin-left: 15px;">
            <label for="imsi" class="control-label col-md-1 col-sm-6">业务模板 *</label>
            <div class="col-md-3 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
            </div>
        </div>
        <div class="form-group" style="margin-left: 15px;">
            <label for="imsi" class="control-label col-md-1 col-sm-6">APN组 *</label>
            <div class="col-md-3 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
            </div>
        </div>
        <div class="form-group" style="margin-left: 15px;">
            <label for="imsi" class="control-label col-md-1 col-sm-6">IP地址分配方法 *</label>
            <div class="col-md-3 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
            </div>
        </div>
        <div class="form-group" style="margin-left: 15px;">
            <label for="imsi" class="control-label col-md-1 col-sm-6">无条件呼叫前转号码 *</label>
            <div class="col-md-2 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
            </div>
            <label for="imsi" class="control-label col-md-1 col-sm-6">不可达呼叫前转号码 *</label>
            <div class="col-md-2 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
            </div>
            <label for="imsi" class="control-label col-md-1 col-sm-6">无应答呼叫前转号码 *</label>
            <div class="col-md-2 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
            </div>
            <label for="imsi" class="control-label col-md-1 col-sm-6">遇忙前转号码 *</label>
            <div class="col-md-2 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
            </div>
        </div>

        <div class="form-group" style="margin-left: 15px;">
            <label for="imsi" class="control-label col-md-1 col-sm-6">紧急单呼号码 *</label>
            <div class="col-md-2 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
            </div>
            <label for="imsi" class="control-label col-md-1 col-sm-6">紧急组呼号码 *</label>
            <div class="col-md-2 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
            </div>
            <label for="imsi" class="control-label col-md-1 col-sm-6">PNAS注册周期 *</label>
            <div class="col-md-2 col-sm-6">
                <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
            </div>
        </div>

        <div class="form-group col-md-10 col-sm-12">
            <div class="panel panel-primary" style="margin: 0 0 0 15px;">
                <div class="panel-heading">
                    <h4 class="panel-title">组织属性</h4>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <label for="imsi" class="control-label col-md-1 col-sm-6">角色*</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
                        </div>
                        <label for="imsi" class="control-label col-md-1 col-sm-6">集群优先级 *</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
                        </div>
                        <label for="imsi" class="control-label col-md-1 col-sm-6">PGIS *</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="imsi" class="control-label col-md-1 col-sm-6">组织呼入呼出权限 *</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
                        </div>
                        <label for="imsi" class="control-label col-md-1 col-sm-6">授权呼叫 *</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
                        </div>
                        <label for="imsi" class="control-label col-md-1 col-sm-6">短号 *</label>
                        <div class="col-md-3 col-sm-6">
                            <input type="text" class="form-control" name="imsi" placeholder="请输入用户名">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-group" style="margin-left: 15px;">
            <div class="col-sm-offset-2 col-sm-4">
                <button type="submit" class="btn btn-primary" style="width:180px;">提交</button>
            </div>
            <div class="col-sm-3 text-right">
                <button type="submit" class="btn btn-default" style="margin-left:50px;">返回列表</button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(document).ready(function() {

    })
</script>
