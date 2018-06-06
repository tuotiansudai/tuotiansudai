<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>正在处理中...</title>
    <link rel="stylesheet" type="text/css" href="${css.index!}" charset="utf-8"/>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>

<div class="my-account-content personal-profile loading-page">

    <div class="info-container">
        <div class="status-container">
            <div class="icon-status icon-failure"></div>
            <p class="desc">银行卡绑定失败</p>
            <p class="reason-error">身份证信息和真实姓名不匹配</p>
        </div>

    </div>

<#--失败时重新尝试-->
    <div class="btn-container">
        <a href="/" class="btn-confirm">再次尝试</a>
    </div>
    <div class="contact"><p>客服电话：400-169-1188（服务时间：9:00-20:00）</p></div>
</div>
</div>


</body>
</html>