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
