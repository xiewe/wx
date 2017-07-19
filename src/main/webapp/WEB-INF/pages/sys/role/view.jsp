<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<link href="${contextPath}/styles/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<div class="row main-content">
    <table class="table table-hover table-condensed" id="tabData">
        <tbody>
            <tr>
                <td class="text-right">名称</td>
                <td>${role.name}</td>
            </tr>
            <tr>
                <td class="text-right">描述</td>
                <td>${role.description}</td>
            </tr>
            <tr>
                <td class="text-right">权限</td>
                <td>
                <div class="zTreeDemoBackground" style="overflow: auto; height: 330px;">
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
                </td>
            </tr>
        </tbody>
    </table>
    <div class="col-sm-offset-2 col-sm-10 text-right" style="margin-bottom: 20px;">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</div>
<script src="${contextPath}/styles/zTree/js/jquery.ztree.core.min.js"></script>
<script src="${contextPath}/styles/zTree/js/jquery.ztree.excheck.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        initZTreeData();
    });

    var setting = {
        check : {
            enable : true,
            chkboxType : {
                "Y" : "ps",
                "N" : "ps"
            }
        },
        data : {
            simpleData : {
                enable : true
            }
        }
    };

    var zNodes = new Array();

    function initZTreeData() {
        $.ajax({
            type : "get",
            url : "${contextPath }/role/privs/1",
            dataType : 'json',
            async : false
        }).done(function(result, textStatus, jqXHR) {
            var data = result.data;
            for (var i = 0; i < data.length; i++) {
                var tmpObj = new Object();
                tmpObj.id = data[i].id;
                tmpObj.name = data[i].name;
                tmpObj.open = true;
                tmpObj.pId = data[i].parentId;
                zNodes.push(tmpObj);

                for (var j = 0; j < data[i].sysMenuClasses.length; j++) {
                    var classObj = new Object();
                    classObj.id = data[i].sysMenuClasses[j].id;
                    classObj.name = data[i].sysMenuClasses[j].name;
                    classObj.open = true;
                    classObj.pId = data[i].id;
                    zNodes.push(classObj);
                }
            }

            initZtreeStatus();
             
            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log("initZTreeData error");
        }).always(function() {
        });
    }

    function initZtreeStatus() {
        var rid = ${role.id};
        $.ajax({
            type : "get",
            url : "${contextPath }/role/privs/" + rid,
            dataType : 'json',
            async : false
        }).done(function(result, textStatus, jqXHR) {
            var data = result.data;
            for (var j = 0; j < data.length; j++) {
                for (var i = 0; i < zNodes.length; i++) {
                    if (zNodes[i].id == data[j].id) {
                        zNodes[i].checked = true;
                    }

                    for (var k = 0; k < data[j].sysMenuClasses.length; k++) {
                        if (zNodes[i].id == data[j].sysMenuClasses[k].id) {
                            zNodes[i].checked = true;
                            break;
                        }
                    }
                }
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log("initZtreeStatus error");
        }).always(function() {
        });
    }
</script>