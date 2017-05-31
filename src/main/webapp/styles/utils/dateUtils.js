//对Date的扩展，将 Date 转化为指定格式的String 
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd HH:mm:ss.S") ==> 2006-07-02 18:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.format = function(fmt) { //author: meizz 
	var o = {
		"M+" : this.getMonth() + 1, //月份 
		"d+" : this.getDate(), //日 
		"h+" : this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时           
		"H+" : this.getHours(), //小时 
		"m+" : this.getMinutes(), //分 
		"s+" : this.getSeconds(), //秒 
		"q+" : Math.floor((this.getMonth() + 3) / 3), //季度 
		"S" : this.getMilliseconds()
	//毫秒 
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

function str2Milliseconds(fmt) {
	fmt = fmt.replace(new RegExp("-","gm"),"/");
    return (new Date(fmt)).getTime();
}

function milliseconds2Str(s) {
    return (new Date(s)).format("yyyy-MM-dd HH:mm:ss");
}

function milliseconds2FormatStr(s,f) {
    return (new Date(s)).format(f);
}

function getday(afterDays) {
	return milliseconds2FormatStr(new Date().getTime()+afterDays * 24 * 60 * 60 * 1000, 'yyyy-MM-dd');
}

function getday2(s, afterDays) {
	return milliseconds2FormatStr(new Date(s).getTime()+afterDays * 24 * 60 * 60 * 1000, 'yyyy-MM-dd');
}

function leftTimeStr(endtime, starttime) {
  var aday = 86400000;
  var ahour = 3600000;
  var amin = 60000;
  var ams = 1000;

  if (endtime < starttime) {
  	return "0";
  }

  var diff = endtime - starttime;
  var days = parseInt(diff / aday);
  var hours = parseInt((diff % aday) / ahour);
  var mins = parseInt((diff % ahour) / amin);
  var second = parseInt(diff % amin / ams);

  var ret = '';
  if (days != undefined && days > 0) {
  	ret += days + '天';
  }
  if (hours != undefined && hours > 0) {
  	ret += hours + '小时';
  }
  if (mins != undefined && mins > 0) {
  	ret += mins + '分';
  }
  if (second != undefined && second > 0) {
  	ret += second + '秒';
  }

  return ret;
}

function countdownTimeStr(endtime) {
	return leftTimeStr(endtime, new Date().getTime());
}

function countdownTimeToDaysLater(time, day) {
	if (time == undefined) {
		return "0";
	}
	return leftTimeStr(time + day * 86400000, new Date().getTime());
}
