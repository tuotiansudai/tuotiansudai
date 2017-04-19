<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <title>拓天财富登录授权</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <link href="${commonStaticServer}/images/favicon.ico" id="icoFavicon" rel="shortcut icon" type="image/x-icon"/>
    <link rel="stylesheet" type="text/css" href="${css.globalFun_page!}" charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="${css.we_start}" charset="utf-8"/>

</head>
<body>
<div class="weChat-container" id="weChatStartContainer">
    <h2 class="note">请填写手机号码，以补全您的拓天速贷账户信息。</h2>

    <form id="formStart">
        <input validate class="login-name" type="text" value="" name="username" placeholder="请输入账号/手机号" maxlength="25"/>
        <div class="error-box">sdsd</div>
        <button type="submit" class="btn-normal">下一步</button>
    </form>

    <div class="notice-tip">
        <b>温馨提示:</b>
        <p>若您输入的手机号码已被注册，需输入相应的登录密码补全账户信息；若未被注册需要设置密码补全信息。</p>
    </div>
</div>

<script>
    window.staticServer = '${commonStaticServer}';
</script>

<script src="${js.jquerydll}" type="text/javascript" defer></script>
<script src="${js.globalFun_page!}" type="text/javascript" defer></script>
<script src="${js.we_start}" type="text/javascript" defer></script>

</body>
</html>