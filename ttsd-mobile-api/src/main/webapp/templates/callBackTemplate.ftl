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
    <div class="main-layout">
        <h3 class="info-title"><i></i>${message} </h3>
        <ul class="info-detail">
        <#if service == 'ptp_mer_bind_card'>
        <li>
            <span class="title">银行卡名称</span>
            <em class="col-info">${bankName}</em>
        </li>
        <li>
            <span class="title">银行卡号:</span>
            <em class="col-info card">${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")} </em>
        </li>
        <#elseif service == 'mer_recharge_person'>
            <li>
                <span class="title ">所用卡号</span>
                <em class="col-info card">${bankName} <#if cardNumber??></#if>${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}</em>
            </li>
            <li>
                <span class="title">充值金额</span>
                <em class="col-info">${rechargeAmount} </em>
            </li>
            <li>
                <span class="title">订单号</span>
                <em class="col-info">${orderId}</em>
            </li>

        <#elseif service == 'cust_withdrawals'>
        <li>
            <span class="title">到账卡号</span>
            <em class="col-info card">${bankName} <#if cardNumber??></#if>${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}</em>
        </li>
        <li>
            <span class="title">提取金额</span>
            <em class="col-info">${withdrawAmount} </em>
        </li>
            <li>
            <span class="title">订单号</span>
            <em class="col-info">${orderId}</em>
        </li>
        <#elseif service == 'project_transfer_invest'>
        <li><span class="title">投资金额</span><em class="col-info">${investAmount}</em></li>
        <li><span class="title">所投项目</span><em class="col-info">${investName}</em></li>
        <li><span class="title">项目编号</span><em class="col-info">${investId}</em></li>
        <#else>
        </#if>
        </ul>

    </div>
    <a href="${href}" class="btn-success">确认</a>
</body>
</html>