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
        <#if bankCallbackType == 'REGISTER'>
            <p class="desc">实名认证失败</p>
        </#if>
        <#if bankCallbackType == 'CARD_BIND'>
            <p class="desc">绑卡失败</p>
        </#if>
        <#--申请提现失败-->
        <#if bankCallbackType == 'WITHDRAW'>
            <p class="desc">申请提现</p>
        </#if>
        <#if bankCallbackType == 'LOAN_INVEST' || bankCallbackType == 'LOAN_FAST_INVEST'>
            <p class="desc">投资失败</p>
        </#if>
            <p class="reason-error">${message!('业务处理失败')}</p>
        </div>

    </div>

<#--失败时重新尝试-->
    <div class="btn-container">
        <a href="${bankCallbackType.getMRetryPath()}" class="btn-confirm">再次尝试</a>
    </div>
    <div class="contact"><p>客服电话：400-169-1188（服务时间：9:00-20:00）</p></div>
</div>
</div>


</body>
</html>