<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1 maximum-scale=1"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>用户注册</title>
    <link rel="stylesheet" type="text/css" href="/mobile/style/style.css"/>
    <link rel="stylesheet" type="text/css" href="/mobile/style/global.css"/>
    <link rel="stylesheet" type="text/css" href="/mobile/style/copyright.css"/>
    <script src="/mobile/js/config.js" type="application/javascript"></script>
    <script src="/mobile/js/libs/require.js" defer async="true" data-main="/mobile/js/logUp.js"></script>
</head>
<body>
<div class="wrap"><!--wrap begin-->
    <div class="step">
        <form class="cmxForm" action="/mobile/register/mobileRegister" method="post">
            <ul>
                <li><input type="text" placeholder="5-25位数字字母组合,请勿使用手机号" class="userName" name="username"/></li>
                <li><input type="password" value="" placeholder="请输入密码,6-20位,不能全为数字" class="passWord" name="password"/></li>
                <li><input type="text" value="" placeholder="请输入手机号" class="phoneNumber" name="phoneNumber"/></li>
                <li><input type="text" value="" placeholder="请输入验证码" class="vCode" name="vCode"/></li>
                <li><input type="text" value="" placeholder="请输入推荐人" class="referrer" name="referrer"/></li>
            </ul>
            <i class="send_vCode">获取验证码</i>
            <div class="submitBtn">
                <a href="javascript:;" class="logUp-a">立即注册</a>
                <input type="submit" class="logUp" value="立即注册"/>
            </div>
        </form>
        <p>已有账号 | <a href="/memberLoginPage" class="landNow">立即登录</a></p>
    </div>

    <div class="check"><input type="checkbox" checked="checked" class="check_input" id="agreementList"><label for="agreementList">我已阅读并同意<a href="/node/spage/registerService">《网站服务协议》</a></label></div>
    <div class="copyright">
        ©拓天速贷 京ICP备14008676号<br/><a href="/mobile/register">手机版</a> <a href="/">PC版</a> 电话：400-169-1188
    </div>
</div>
<!--wrap end-->
</body>
</html>