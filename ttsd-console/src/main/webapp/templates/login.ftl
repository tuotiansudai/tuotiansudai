<#assign applicationContext=requestContext.getContextPath() />
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>登录</title>
    <!-- link bootstrap css and js -->
    <link href="${applicationContext}/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
    <link href="${applicationContext}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${applicationContext}/style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <script src="${applicationContext}/js/libs/jquery-1.11.3.min.js"></script> <!-- jquery -->
    <script src="${applicationContext}/js/libs/bootstrap.min.js"></script>
    <script src="${applicationContext}/js/login.js"></script>
    <!-- link bootstrap css and js -->

    <link rel="stylesheet" href="${applicationContext}/style/index.css">

<body class="login-bg">
<!-- header begin -->
<header class="navbar navbar-static-top bs-docs-nav" id="top" role="banner">
    <div class="container">
        <div class="navbar-header">
            <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target="#bs-navbar" aria-controls="bs-navbar" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="i con-bar"></span>
            </button>
            <a href="${applicationContext}/" class="navbar-brand"><img src="${applicationContext}/images/logo.png" alt=""></a>
        </div>
    </div>
</header>
<!-- header end -->

<!-- main begin -->
<div class="main">
    <div class="container-fuild">
        <div class="login">
            <form class="form-login">
                <h2 class="text-center">登录</h2>
                <div class="form-group">
                    <label for="user">用户名：</label>
                    <input type="text" class="form-control" id="user" name="username" placeholder="请输入用户名">
                </div>
                <div class="form-group">
                    <label for="password">密码：</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码">
                </div>
                <div class="form-group">
                    <label for="yzm">验证码：</label>
                    <div class="captcha-column">

                            <input type="text" class="form-control captcha" id="yzm" name="captcha" placeholder="请输入验证码" maxlength="5">

                        <div class="captcha-img">
                            <img class="verification-console-img" src="/login/captcha" alt=""/>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                   <span class="error" style="visibility:hidden; color: #cc0000;">用户名或密码不正确</span>
                </div>
                <div class="form-group">
                    <button class="btn grey btn-primary btn-block btn-lg" type="button">登录</button>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
    </div>
</div>
<!-- main end -->

</body>
</html>