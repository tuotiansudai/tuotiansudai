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
    <script src="/mobile/js/libs/require.js" defer async="true" data-main="/mobile/js/logUp"></script>
</head>
<body>
<div class="wrap"><!--wrap begin-->
    <div class="step">
        <form class="cmxForm" action="" method="">
            <ul>
                <li><input type="text" placeholder="5-25位数字字母组合,请勿使用手机号" class="userName" name="username"/></li>
                <li><input type="password" value="" placeholder="请输入密码,至少6位" class="passWord" name="password"
                           minLength="6" required/></li>
                <li><input type="text" value="" placeholder="请输入手机号" class="phoneNumber" maxLength="11" name="phoneNumber"
                           required/><span class="tip">手机号码不能为空</span></li>
                <li><input type="text" value="" placeholder="请输入验证码" class="vCode" name="vCode" required/></li>
            </ul>
            <i class="send_vCode">获取验证码</i>
            <input type="submit" class="logUp" value="立即注册">
            <#--<a href="javascript:;" class="logUp">立即注册</a>-->
        </form>
        <p>已有账号 | <a href="" class="landNow">立即登录</a></p>
    </div>

    <div class="check"><input type="checkbox" checked="checked" class="check_input" id="agreementList"><label for="agreementList">我已阅读并同意<a href="">《网站服务协议》</a></label></div>
    <div class="copyright">
        ©拓天速贷 京ICP备14008676号<br/><a href="javascript:;">手机版</a> <a href="javascript:;">PC版</a> 电话：400-169-1188
    </div>
</div>
<!--wrap end-->
</body>
</html>