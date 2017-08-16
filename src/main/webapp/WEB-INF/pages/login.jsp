<jsp:directive.page contentType="text/html;charset=UTF-8" />
<jsp:directive.include file="/WEB-INF/pages/include.inc.jsp"/>
<c:if test="${inoms == '1' }">
<%
    response.sendRedirect("/tcn2000/security/tologinpage.do?userName=");
%>
</c:if>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="bookmark"  type="image/x-icon"  href="favicon.ico"/>
    <link rel="icon"  type="image/x-icon" href="favicon.ico" />
    <link rel="shortcut icon"  type="image/x-icon" href="favicon.ico" />

    <title>登陆</title>

    <!-- Bootstrap core CSS -->
    <link href="${contextPath}/styles/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="${contextPath}/styles/bootstrap/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="${contextPath}/styles/custom/css/signin.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
        <script src="${contextPath}/styles/bootstrap/js/html5shiv.min.js"></script>
        <script src="${contextPath}/styles/bootstrap/js/respond.min.js"></script>
        <![endif]-->
  </head>

  <body>

    <div class="container">

      <form class="form-signin" method="post" action="${contextPath}/login" id="formID">
        <h2 class="form-signin-heading">请输入</h2>
        <c:if test="${not empty msg}">
            <p style="color: red; margin-left: 10px;">${msg}</p>
        </c:if>
        <label for="username" class="sr-only">用户名</label>
        <input type="text" id="username" name="username" value="${username }" class="form-control" placeholder="用户名" required autofocus>
        <label for="password" class="sr-only">密码</label>
        <input type="password" id="password" name="password" value="${password }" class="form-control" placeholder="密码" required>
        <div class="input-group form-group">
        <label for="captcha" class="sr-only">验证码</label>
        <input type="text" id="captcha" name="captcha" class="form-control" placeholder="验证码" required>
        <span class="input-group-addon">
        <img src="${contextPath}/Captcha.jpg" alt="点击刷新验证码" id="captcha_img" style="cursor:pointer;height:30px;width:100px;"/>
        </span>
        </div>
<!--         <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> Remember me
          </label>
        </div> -->
        <div class="form-group">
            <button class="btn btn-lg btn-primary btn-block form-control" type="submit">登陆</button>
        </div>
      </form>

    </div> <!-- /container -->

    <script src="${contextPath}/styles/jquery-3.2.1.min.js"></script>
    <script src="${contextPath}/styles/bootstrap/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <%-- <script src="${contextPath}/assets/js/ie10-viewport-bug-workaround.js"></script> --%>
    <script>
        $("#captcha_img").click(function(){
            $(this).attr("src", "${contextPath}/Captcha.jpg?time=" + new Date());
            return false;
        });
    </script>
  </body>
</html>
