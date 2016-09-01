<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <title>拓天速贷</title>
    <#--<link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}base.css" charset="utf-8" />-->
    <#--<link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}ttsd-mobile.css" charset="utf-8" />-->
    <link rel="stylesheet" type="text/css" href="http://localhost:9088/api/style/base.css" charset="utf-8" />
    <link rel="stylesheet" type="text/css" href="http://localhost:9088/api/style/ttsd-mobile.css" charset="utf-8" />
</head>
<body>
    <div class="main">
        <h3 class="info-title">${message} </h3>
        <#if service == 'ptp_mer_bind_card'>
            银行卡名称:${bankName}<br/>
            银行卡号:${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}
        <#elseif service == 'mer_recharge_person'>
            所用卡号:${bankName} <#if cardNumber??></#if>${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}<br/>
            充值金额:${rechargeAmount}<br/>
            订单号:${orderId}
        <#elseif service == 'cust_withdrawals'>
            到账卡号：${bankName} <#if cardNumber??></#if>${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}<br/>
            提取金额：${withdrawAmount}
            订单号：${orderId}
        <#elseif service == 'project_transfer_invest'>
            投资金额：${investAmount}
            所投项目：${investName}
            项目编号：${investId}
        <#else>
        </#if>
        <a href="${href}" class="btn-success">确认</a>
    </div>
</body>
</html>