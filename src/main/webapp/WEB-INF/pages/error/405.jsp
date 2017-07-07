<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%response.setStatus(200);%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>哎呀…您访问的页面不存在</title>
<link rel="stylesheet" type="text/css" />

<style>

html { height: 100%; width: 100%; background: url(${contextPath}/styles/custom/404/bj.png) repeat; }
*{margin:0;padding:0}
body{font-family:"微软雅黑";}
img{border:none}
a *{cursor:pointer}
ul,li{list-style:none}
table{table-layout:fixed;}
table tr td{word-break:break-all; word-wrap:break-word;}

a{text-decoration:none;outline:none}
a:hover{text-decoration:underline}
.cf:after{content: ".";display: block;height: 0;font-size: 0;clear:both;visibility: hidden;}
.cf{zoom: 1;clear:both}

.cont{margin:0 auto;width:500px;line-height:20px;}
.c1{height:360px;text-align:center}
.c1 .img1{margin-top:180px}
.c1 .img2{margin-top:165px}
.cont h2{text-align:center;color:#555;font-size:18px;font-weight:normal;height:35px}
.c2{height:35px;text-align:center}
.c2 a{display:inline-block;margin:0 4px;font-size:14px;height:23px;color:#626262;padding-top:1px;text-decoration:none;text-align:left}
.c2 a:hover{color:#626262;text-decoration:none;}
.c2 a.home{width:66px;background:url("${contextPath}/styles/custom/404/02.png");padding-left:30px}
.c2 a.home:hover{background:url("${contextPath}/styles/custom/404/02.png") 0 -24px}
.c2 a.home:active{background:url("${contextPath}/styles/custom/404/02.png") 0 -48px}
.c2 a.re{width:66px;background:url("${contextPath}/styles/custom/404/03.png");padding-left:30px}
.c2 a.re:hover{background:url("${contextPath}/styles/custom/404/03.png") 0 -24px}
.c2 a.re:active{background:url("${contextPath}/styles/custom/404/03.png") 0 -48px}
.c2 a.sr{width:153px;background:url("${contextPath}/styles/custom/404/04.png");padding-left:28px}
.c2 a.sr:hover{background:url("${contextPath}/styles/custom/404/04.png") 0 -24px}
.c2 a.sr:active{background:url("${contextPath}/styles/custom/404/04.png") 0 -48px}
.c3{height:180px;text-align:center;color:#999;font-size:12px}
#bf{position:absolute;top:269px;left:0;width:100%}
.bf1{margin:0 auto;width:99px;padding-left:32px}
.bd{height:600px;overflow:hidden}
#box{position:absolute;top:165px;left:0;width:100%;text-align:center}
.bf1{margin:0 auto;width:99px;padding-left:32px}
</style>

</head>
<body>
<div>
	<div class="cont">
		<div class="c1"><img src="${contextPath}/styles/custom/404/01.png" class="img1" /></div>
		<h2>哎呀…您访问的页面不存在</h2>
		<div class="c2"><a href="javascript:history.back();" class="re">返回</a><a href="<c:url value="/index"/>" class="home">首页</a></div>
		<div class="c3">温馨提醒您 - 您可能输入了错误的网址，或者该网页已删除或移动</div>
	</div>
</div>
</body>
</html>