<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <title>拓天财富登录授权</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <link href="${commonStaticServer}/images/favicon.ico" id="icoFavicon" rel="shortcut icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${css.globalFun_page!}" charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="${css.we_start}" charset="utf-8" />

</head>
<body>

<div class="weChat-container weChat-login">
    <h2 class="note">您输入的<i class="mobile">186****7529</i>手机号已经注册速贷账户</h2>
    <form id="formLogin" class="form-login">
        <input validate class="password" type="password"  placeholder="请输入登录密码"/>
        <input validate class="captcha" type="text"  placeholder="请输入图形验证码"/>
        <img src="/login/captcha" class="image-captcha" id="imageCaptcha"/>

        <div class="error-box">sdsd</div>
        <button type="submit" class="btn-normal">下一步</button>
    </form>
</div>

<script>
    window.staticServer='${commonStaticServer}';
</script>

<script src="${js.jquerydll}" type="text/javascript" defer></script>
<script src="${js.globalFun_page!}" type="text/javascript" defer></script>
<script src="${js.we_start}" type="text/javascript" defer></script>

</body>
</html>