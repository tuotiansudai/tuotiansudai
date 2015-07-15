<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link rel="stylesheet" type="text/css" href="/mobile/style/style.css"/>
    <link rel="stylesheet" type="text/css" href="/mobile/style/global.css"/>
    <link rel="stylesheet" type="text/css" href="/mobile/style/copyright.css"/>
    <script src="/mobile/js/libs/require.js" defer async="true" data-main="/mobile/js/realName"></script>
    <title>手机认证</title>
</head>
<body>
<div class="cerWrap"><!--cerWrap begin-->
    <h3>恭喜您注册成功</h3>

    <p>拓天速贷与第三方支付平台（联动优势）合作，您的资金由联动优势平台进行托管，充分保障了安全，让您安心理财.</p>
    <ul>
        <form class="realName">
            <li><input type="text"name="yourName" required placeholder="请输入您的姓名" class="yourName"/> </li>
            <li><input type="text" name="yourId" minLength="15" maxLength="18" required placeholder="请输入您的身份证号" class="yourId"/> </li>
            <input type="button" class="realName realName_submit" value="实名认证" />
        </form>
    </ul>

    <a href="/user/center" class="skip">跳过</a>
    <div class="copyright">
        ©拓天速贷 京ICP备14008676号<br/><a href="javascript:;">手机版</a> <a href="javascript:;">PC版</a> 电话：400-169-1188
    </div>
</div>
<!--cerWrap end-->
<div class="tipMask"><!--tipMask begin-->
    <p>实名认证失败！</p>
</div><!--tipMask end-->
</body>
</html>