<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>成功</title>
    <link rel="stylesheet" type="text/css" href="${css.index!}" charset="utf-8"/>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>

<div class="my-account-content personal-profile">
<#if bankCallbackType == 'REGISTER'>
    <div class="m-header">开通存管账号</div>
    <div class="info-container">
        <div class="status-container">
            <div class="icon-status icon-success"></div>
            <p class="desc"> ${bankCallbackType.getTitle()}</p>
        </div>

    </div>
<#--实名认证成功后下一步 去绑卡-->
    <form action="${requestContext.getContextPath()}/bank-card/bind/source/M" method="post" style="display: inline-block;float:right">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="btn-container">
            <input type="submit" class="btn-confirm"  value="下一步"/>
        </div>
    </form>
</#if>
<#--银行卡绑定成功-->
<#if bankCallbackType == 'CARD_BIND'>
    <div class="m-header">开通存管账号</div>
    <div class="info-container">
        <div class="status-container">
            <div class="icon-status icon-success"></div>
            <p class="desc"> ${bankCallbackType.getTitle()}</p>
        </div>

    </div>
    <div class="btn-container">
        <a href="/" class="btn-confirm">确定</a>
    </div>
</#if>
<#if bankCallbackType == 'RECHARGE'>
    <div class="m-header">充值成功</div>
    <div class="info-container">
        <div class="status-container">
            <div class="icon-status icon-success"></div>
            <p class="desc"> ${bankCallbackType.getTitle()}</p>
        </div>

    </div>
    <div class="btn-container">
        <a href="/" class="btn-confirm">确定</a>
    </div>

</#if>

<#--申请提现成功-->
<#if bankCallbackType == 'WITHDRAW'>
    <div class="m-header">提现成功</div>
    <div class="info-container">
        <div class="status-container">
            <div class="icon-status icon-success"></div>
            <p class="desc"> ${bankCallbackType.getTitle()}</p>
        </div>

    </div>
    <div class="btn-container">
        <a href="/" class="btn-confirm">确定</a>
    </div>
</#if>

<#--投资成功-->
<#if bankCallbackType == 'LOAN_INVEST' || bankCallbackType == 'LOAN_FAST_INVEST'>
    <div class="m-header">投资成功</div>
    <div class="info-container">
        <div class="status-container">
            <div class="icon-status icon-success"></div>
            <p class="desc"> ${bankCallbackType.getTitle()}</p>
        </div>

    </div>
    <div class="btn-container">
        <a href="/" class="btn-confirm">确定</a>
    </div>
</#if>


    <div class="contact"><p>客服电话：400-169-1188（服务时间：9:00-20:00）</p></div>
</div>


</body>
</html>