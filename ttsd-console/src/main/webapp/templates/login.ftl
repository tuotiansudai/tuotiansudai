<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>登录</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- link bootstrap css and js -->
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <script src="${requestContext.getContextPath()}/js/libs/jquery-1.10.1.min.js"></script> <!-- jquery -->
    <script src="${requestContext.getContextPath()}/js/libs/bootstrap.min.js"></script>
    <script src="${requestContext.getContextPath()}/js/login.js"></script>
    <!-- link bootstrap css and js -->

    <link rel="stylesheet" href="../style/index.css">

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
            <a href="${requestContext.getContextPath()}/" class="navbar-brand"><img src="${requestContext.getContextPath()}/images/logo.jpg" alt=""></a>
        </div>
    </div>
</header>
<!-- header end -->

<!-- main begin -->
<div class="main">
    <div class="container-fuild">
        <div class="login">
            <form class="form-login">
                <h1 class="text-center">登录</h1>
                <div class="form-group">
                    <label for="user">账号：</label>
                    <input type="text" class="form-control" id="user" name="username" placeholder="user name">
                </div>
                <div class="form-group">
                    <label for="password">密码：</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                </div>
                <div class="form-group">
                    <label for="yzm">验证码：</label>
                    <div class="row">
                        <div class="col-md-7">
                            <input type="text" class="form-control captcha" id="yzm" name="captcha" placeholder="验证码" maxlength="5">
                        </div>
                        <div class="captcha-img col-md-5">
                            <img class="verification-console-img" src="/login/captcha" alt=""/>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                   <span class="error" style="display: none; color: #cc0000;"></span>
                </div>
                <div class="form-group" style="margin-top: 20px;">
                    <button class="btn grey btn-primary btn-block btn-lg" type="button" disabled="disabled">登录</button>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
    </div>
</div>
<!-- main end -->

</body>
</html>