<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <title>拓天速贷</title>
    <link rel="stylesheet" type="text/css" href="${css.index!}" charset="utf-8"/>

</head>
<body class="success-body">
<div class="success-container">
    <p>
        <i class="success-icon"></i>
    </p>

    <p>${message}</p>
    <#if "membership-purchase" == service>
        <p>您已成为<span class="v5-icon"></span>会员！投资时将享受服务费7折优惠。</p>
    </#if>

    <#if "ptp_mer_replace_card" == service>
        <p>换卡申请最快两小时处理完成</p>
    </#if>

    <#if "membership_privilege_purchase" == service>
        <p>您已成功购买增值特权</p>
        <p>投资时将享受服务费7折优惠</p>
    </#if>

    <p>
        <a href="${href}" class="btn-success">确定</a>
    </p>

    <#if "ptp_mer_replace_card" == service>
        <p class="fix-nav">客服电话：400-169-1188（服务时间：9:00-20:00）</p>
    </#if>
</div>
</body>
</html>