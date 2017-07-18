<jsp:directive.page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp" />

<link href="${contextPath}/styles/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<div class="row main-content">
    <form id="saveForm" class="form-horizontal" role="form" method="post" action="${contextPath }/role/create" onsubmit="return doSave(this, '${contextPath }/role/list');">
        <div class="form-group">
            <label for="name" class="col-sm-2 control-label">角色名称 *</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="name" placeholder="请输入角色名字">
            </div>
        </div>
        <div class="form-group">
            <label for="description" class="col-sm-2 control-label">描述 *</label>
            <div class="col-sm-10">
                <textarea class="form-control" rows="3" name="description" placeholder="请输入描述"></textarea>
            </div>
        </div>
        <div class="form-group">
            <label for="description" class="col-sm-2 control-label">请选择权限 *</label>
            <div class="col-sm-10">
                <div class="zTreeDemoBackground" style="width: 400px; overflow: auto; height: 330px;">
                    <div style="display: none" class="alert alert-danger">权限不能为空，请选择权限！</div>
                    <ul id="treeDemo" class="ztree"></ul>
                    <input type="hidden" name="ids" value="">
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
<script src="${contextPath}/styles/zTree/js/jquery.ztree.core.min.js"></script>
<script src="${contextPath}/styles/zTree/js/jquery.ztree.excheck.min.js"></script>
<script type="text/javascript">
    function doSave(form, listUrl) {
        $(form).data("bootstrapValidator").validate();
        var flag = $(form).data("bootstrapValidator").isValid();

        // tree 
        var zTree = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = zTree.getCheckedNodes();
        var ids = '';
        if (nodes.length == 0) {
            $('.alert-danger').css('display', 'block');
            return false;
        } else {
            $('.alert-danger').css('display', 'none');
            for (var i = 0; i < nodes.length; i++) {
                ids += nodes[i].id + '-';
            }
            if (ids.length > 0) {
                ids = ids.substring(0, ids.length - 1);
                $("input[name=ids]").val(ids);
            }
        }

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
                name : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            min : 1,
                            max : 45
                        }
                    }
                },
                description : {
                    validators : {
                        notEmpty : {},
                        stringLength : {
                            max : 128
                        }
                    }
                }
            }
        }).on('success.form.bv', function(e) {
            e.preventDefault();
        });

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
            dataType : 'json'
        }).done(function(result, textStatus, jqXHR) {
            var data = result.data;
            for (var i = 0; i < data.length; i++) {
                var tmpObj = new Object();
                tmpObj.id = data[i].sysMenu.id;
                tmpObj.name = data[i].sysMenu.name;
                tmpObj.open = true;
                tmpObj.pId = data[i].sysMenu.parentId;
                zNodes.push(tmpObj);
            }

            initZtreeStatus();
            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log("initZTreeData error");
        }).always(function() {
            console.log("initZTreeData complete");
        });
    }

    function initZtreeStatus() {
        $.ajax({
            type : "get",
            url : "${contextPath }/role/privs"+,
            dataType : 'json'
        }).done(function(result, textStatus, jqXHR) {
            var data = result.data;
            for (var i = 0; i < zNodes.length; i++) {
                for (var j = 0; j < data.length; j++) {
                    if (zNodes[i].id == data[j].id) {
                        zNodes[i].checked = true;
                        break;
                    }
                }
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log("initZtreeStatus error");
        }).always(function() {
            console.log("initZtreeStatus complete");
        });
    }
</script>
