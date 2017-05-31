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
  return str.replace(/(^\s+)|(\s+$)/g,'');
}

function comStr(arrStr, split) {
  if (arrStr.length == 0) {
    return "";
  } else if (arrStr.length == 1) {
    return arrStr[0];
  } else if (arrStr.length > 1) {
    var str ="";
    for (var i in arrStr) {
      str += arrStr[i] + split;
    }
    return trimT(str, split.length);
  }
}

function isEmpty(str) {
  if (str==undefined || str == "" || str.trim() == "") {
    return true;
  } else {
    return false;
  }
}

function isWholeNumber(str) {
  if (str == undefined) {
    return false;
  }

  /**正整数匹配表达式*/
  var pattern=/^\d+$/;
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