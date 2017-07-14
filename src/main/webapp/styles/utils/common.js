var browser = {
    versions : function() {
        var u = navigator.userAgent, app = navigator.appVersion;
        return {// 移动终端浏览器版本信息
            trident : u.indexOf('Trident') > -1, // IE内核
            presto : u.indexOf('Presto') > -1, // opera内核
            webKit : u.indexOf('AppleWebKit') > -1, // 苹果、谷歌内核
            gecko : u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, // 火狐内核
            mobile : !!u.match(/AppleWebKit.*Mobile.*/), // 是否为移动终端
            ios : !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), // ios终端
            android : u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, // android终端或uc浏览器
            iPhone : u.indexOf('iPhone') > -1, // 是否为iPhone或者QQHD浏览器
            iPad : u.indexOf('iPad') > -1, // 是否iPad
            webApp : u.indexOf('Safari') == -1
        // 是否web应该程序，没有头部与底部
        };
    }(),
    language : (navigator.browserLanguage || navigator.language).toLowerCase()
}

function stopBubble(e) {
    if (e && e.stopPropagation)
        e.stopPropagation();
    else
        window.event.cancelBubble = true;
}

function stopDefault(e) {
    if (e && e.preventDefault)
        e.preventDefault();
    else
        window.event.returnValue = false;
    return false;
}

// 字符串操作 begin
function trimT(str, len) {
    if (str.length >= len) {
        return str.substring(0, str.length - len);
    } else {
        return "";
    }
}

function trimH(str, len) {
    if (str.length >= len) {
        return str.substring(len, str.length);
    } else {
        return "";
    }
}

function trim(str) {
    return str.replace(/(^\s+)|(\s+$)/g, '');
}

function comStr(arrStr, split) {
    if (arrStr.length == 0) {
        return "";
    } else if (arrStr.length == 1) {
        return arrStr[0];
    } else if (arrStr.length > 1) {
        var str = "";
        for ( var i in arrStr) {
            str += arrStr[i] + split;
        }
        return trimT(str, split.length);
    }
}

function isEmpty(str) {
    if (str == undefined || str == "" || str.trim() == "") {
        return true;
    } else {
        return false;
    }
}

function isWholeNumber(str) {
    if (str == undefined) {
        return false;
    }

    /** 正整数匹配表达式 */
    var pattern = /^\d+$/;
    if (pattern.test(str)) {
        return true;
    } else {
        return false;
    }
}

function getLastBySplit(str, split) {
    var arr = str.split(split);
    return arr[arr.length - 1];
}
// 字符串操作 end

// JSON数组sort()方法的参数，按JSON数组中哪个字段排序
// 例如: var arr = [{"name":"xie", "score":87},{"name":"wen",
// "score":99},{"name":"bao", "score":66}];
// arr.sort(keysrt('score', true));
// key: 排序字段，desc: true - desc; false - asc
function keysrt(key, desc) {
    return function(a, b) {
        return desc ? ~~(a[key] < b[key]) : ~~(a[key] > b[key]);
    }
}

// ajax请求时发生session超时，跳转到登陆页
$(document).ajaxComplete(function(event, xhr, settings) {
    if (xhr.responseText.indexOf('form-signin-heading') != -1) {
        window.location.href = window.location.href;
        return;
    }
});

// 通用模态窗口
function showAlert(title, msg) {
    $("#indexModal .modal-header h4").text(title);
    $("#indexModal .modal-body").html(msg);
    $("#indexModal .modal-footer").css('display', 'block');
    $("#indexModal .modal-footer .btn").css('display', 'block');
    $("#indexModal .modal-footer .doConfirm").css('display', 'none');
    $("#indexModal").modal('show');
}

function showConfirm(title, msg, url) {
    $("#indexModal .modal-header h4").text(title);
    $("#indexModal .modal-body").html(msg);
    $("#indexModal .modal-footer").css('display', 'block');
    $("#indexModal .modal-footer .btn").css('display', 'block');
    $("#indexModal .modal-footer .doConfirm").data('url', url);
    $("#indexModal").modal('show');
}

$("#indexModal").on("hidden.bs.modal", function() {
    // $(this).removeData("bs.modal");
    $(this).find(".modal-body").children().remove();
})
// confirm action
$('#indexModal .modal-footer .btn-primary').on('click', function(e) {
    e.preventDefault();
    var url = $(this).data('url');
    $.ajax({
        type : "get",
        url : url,
        data : {}
    }).done(function(data, textStatus, jqXHR) {
        console.log("done");
        $("#indexModal").modal('hide');
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.log("error");
    }).always(function() {
        console.log("complete");
    });
})

// 导航树的选中，切换图标等效果
$('.list-group').on('click', 'a,li', function(e) {
    stopBubble(e);
    stopDefault(e);

    $('.panel-group .list-group a,li').removeClass('active');
    $(this).addClass('active');

    var url = $(this).data('url');
    loadContent(url);
})

function loadContent(url) {
    $.ajax({
        type : "get",
        url : url,
        data : {}
    }).done(function(result, textStatus, jqXHR) {
        $('.main').html(result);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.log(textStatus + " - " + errorThrown);
        showAlert('错误', '加载错误');
    }).always(function() {
    });
}

$('.panel-title').on('click', '[data-toggle="collapse"]', function(e) {
    var minus = 0;
    if ($(this).parent().find('span').hasClass('glyphicon-minus')) {
        minus = 1;
    }

    $('.panel-group .panel-heading span').removeClass('glyphicon-minus');
    $('.panel-group .panel-heading span').addClass('glyphicon-plus');

    if (minus == 0) {
        $(this).parent().find('span').addClass('glyphicon-minus');
    }

})

/**
 * 清空列表搜索栏所有内容
 * 
 * @param divid
 */
function clearAllSearchContent(divid) {

    var txts = document.getElementById(divid).getElementsByTagName("input");
    for (var i = 0; i < txts.length; i++) {
        if (txts[i].type == "text") {
            txts[i].value = ""; // input 清空
        }
    }
    var selects = document.getElementById(divid).getElementsByTagName("select");
    for (var i = 0; i < selects.length; i++) {
        selects[i].options[0].selected = true; // select选择第一项
    }

    return false;
}

// save
function doSave(form, listUrl) {
    var $form = $(form);

    var url = $form.attr('action');
    $.ajax({
        type : "post",
        url : url,
        data : $form.serializeArray(),
        dataType : 'json'
    }).done(function(result, textStatus, jqXHR) {
        if (result.status == 0) {
            loadContent(listUrl);
            $("#indexModal").modal('hide');
        } else {
            showAlert('错误', '创建错误');
        }
        $('.main').html(result);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.log(textStatus + " - " + errorThrown);
        showAlert('错误', '创建错误：' + errorThrown);
    }).always(function() {
    });

    return false;
}

// 分页查询
function doSearch(form) {
    var $form = $(form);

    var url = $form.attr('action');
    $.ajax({
        type : "get",
        url : url,
        data : $form.serializeArray()
    }).done(function(result, textStatus, jqXHR) {
        $('.main').html(result);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.log(textStatus + " - " + errorThrown);
        showAlert('错误', '查询错误');
    }).always(function() {
    });

    return false;
}

function _getSearchForm(args) {
    var form = $('#searchForm').get(0);

    if (form) {
        if (args["currPage"])
            form["currPage"].value = args["currPage"];
        if (args["pageSize"])
            form["pageSize"].value = args["pageSize"];
        if (args["orderField"])
            form["orderField"].value = args["orderField"];
        if (args["orderDirection"])
            form["orderDirection"].value = args["orderDirection"];
    }

    return form;
}
function pagerChange(args) {
    var form = _getSearchForm(args);
    doSearch(form);
}
function goToN(e, n) {
    e.preventDefault();
    pagerChange({
        "currPage" : n
    });
}
function goToPre(e) {
    e.preventDefault();
    var curr = $('.pager-list li.active a').text();

    if (curr <= 1) {
        pagerChange({
            "currPage" : 1
        });
    } else {
        pagerChange({
            "currPage" : parseInt(curr) - 1
        });
    }
}
function goToNext(e) {
    e.preventDefault();
    var curr = $('.pager-list li.active a').text();
    var form = $('#searchForm').get(0);
    var totalPage = 1;
    if (form)
        totalPage = form["totalPage"].value;
    if (curr >= totalPage) {
        pagerChange({
            "currPage" : curr
        });
    } else {
        pagerChange({
            "currPage" : parseInt(curr) + 1
        });
    }
}
function goToLast(e) {
    e.preventDefault();
    var form = $('#searchForm').get(0);
    var totalPage = 1;
    if (form)
        totalPage = form["totalPage"].value;
    pagerChange({
        "currPage" : totalPage
    });
}
function goToD(e) {
    e.preventDefault();
    var n = $('.pager-input').val();
    if (n == undefined || n <= 0) {
        return;
    }
    var form = $('#searchForm').get(0);
    var totalPage = 1;
    if (form)
        totalPage = form["totalPage"].value;
    if (n >= totalPage) {
        n = totalPage;
    }
    pagerChange({
        "currPage" : n
    });
}

function loadListener() {

    $('table tbody tr').on('click', function(e) {
        // e.preventDefault();
        $(this).siblings('tr').removeClass('success');

        if ($(this).hasClass('success')) {
            $(this).removeClass('success');
        } else {
            $(this).addClass('success');
        }
    })

}

$(function() {
    changeHeight();
})

function changeHeight() {
    if (window.innerWidth)
        winWidth = window.innerWidth;
    else if ((document.body) && (document.body.clientWidth))
        winWidth = document.body.clientWidth;
    if (window.innerHeight)
        winHeight = window.innerHeight;
    else if ((document.body) && (document.body.clientHeight))
        winHeight = document.body.clientHeight;

    $('.index-row').css('height', winHeight - 100);
    $('.index-row').css('overflow-y', 'auto');
}

window.onresize = function() {
    changeHeight();
}