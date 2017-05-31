<jsp:directive.page contentType="text/html;charset=UTF-8" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/>
<jsp:scriptlet>
<![CDATA[
]]></jsp:scriptlet>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="application.title" /></title>
<%-- <link href="${contextPath}/styles/dwz/themes/default/style.css" rel="stylesheet" type="text/css" /> --%>
<%-- <link href="${contextPath}/styles/dwz/themes/css/core.css" rel="stylesheet" type="text/css" /> --%>
<link href="${contextPath}/styles/dwz/themes/css/login.css" rel="stylesheet" type="text/css" />
<!--可以在收藏夹中显示出图标-->
<link rel="bookmark"  type="image/x-icon"  href="favicon.ico"/>
<!--可以在地址栏中显示出图标-->
<link rel="icon"  type="image/x-icon" href="favicon.ico" />
<link rel="shortcut icon"  type="image/x-icon" href="favicon.ico" />
<!-- form 验证 -->
<link rel="stylesheet" href="${contextPath}/styles/validationEngine/css/validationEngine.jquery.css" type="text/css"/>
<script src="${contextPath}/styles/dwz/js/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="${contextPath}/styles/validationEngine/js/languages/jquery.validationEngine-zh_CN.js" type="text/javascript" charset="utf-8"></script>
<script src="${contextPath}/styles/validationEngine/js/jquery.validationEngine-2.6.4.js" type="text/javascript" charset="utf-8"></script>
<script>
    jQuery(document).ready(function(){
        jQuery("#formID").validationEngine();
    });
    jQuery(document).ready(function(){
    	$("#captcha_img").click(function(){
    		$(this).attr("src", "${contextPath}/Captcha.jpg?time=" + new Date());
    		return false;
    	});
    });

</script>

</head>

<body>
	<div id="login">
		<div id="login_header">
			<h1 class="login_logo">
				<a href="#"><img width="240" height="45" src="${contextPath}/styles/dwz/themes/default/images/login_logo.png" /></a>
			</h1>
			<div class="login_headerContent">
				<div class="navList">
					<ul>
						<li><a href="${contextPath}/login/changeLocale/zh_CN">中文</a></li>
						<li><a href="${contextPath}/login/changeLocale/en_US">English</a></li>
						<li><a href="#" target="_blank"><fmt:message key="info.login.title" /></a></li>
					</ul>
				</div>
				<h2 class="login_title"><img src="${contextPath}/styles/dwz/themes/default/images/login_title.png" /></h2>
			</div>
		</div>
		<div id="login_content">
			<div class="loginForm">
				<form method="post" action="${contextPath}/login" id="formID" >
					<c:if test="${not empty msg}">
						<p style="color: red; margin-left: 10px;">${msg}</p>
					</c:if>
					<p>
						<label><spring:message code="entity.user.username" /></label>
						<input type="text" name="username" style="width: 150px;" class="validate[required] login_input" id="username" value="${username }"/>
					</p>
					<p>
						<label><spring:message code="entity.user.password" /></label>
						<input type="password" name="password" style="width: 150px;" class="validate[required] login_input" id="password" value="${password }"/>
					</p>

					<p>
						<label>验证码:</label>
						<input type="text" style="width: 70px;float:left;" id="captcha" name="captcha" size="6" maxlength="4" />
						<span><img src="${contextPath}/Captcha.jpg" alt="点击刷新验证码" width="75" height="24" id="captcha_img" style="cursor:pointer"/></span>
					</p>
					<!--
					<p>
						<label>记住我:</label>
						<input type="checkbox" id="rememberMe" name="rememberMe"/>
					</p>
					-->
					<div class="login_bar" style="padding-left: 80px;">
						<input class="sub" type="submit" value="" />
					</div>
				</form>
			</div>
			<div class="login_banner"><img src="${contextPath}/styles/dwz/themes/default/images/login_banner.jpg" /></div>
			<div class="login_main">
				<ul class="helpList">
					<li><a href="javascript:toggleBox('forgotPwd')">忘记密码?</a></li>
					<li><a href="${contextPath}/biz/apk/download">点击此链接下载至Android版客户端，或用手机扫描下面的二维码进行下载安装</a></li>
					<img alt="扫描二维码下载应用APP" width="140" height="140" src="${contextPath}/styles/custom/images/qr.png"/>
				</ul>
				<div class="login_inner">
					<p>深圳市贸人科技有限公司</p>
					<p>你的品味，我的品质，好的搭档，你我都需要</p>
					<p>日复一日，精益求精；年复一年，效益满赢</p>
				</div>
			</div>
		</div>
		<div id="login_footer">
			Copyright &copy; 2015 Shenzhen Maoren technology co.,LTD All Rights Reserved. <a href="http://www.miitbeian.gov.cn/" target="_blank">粤ICP备15066967号</a>
		</div>
	</div>
</body>
</html>