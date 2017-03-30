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
<div class="success-info-container">
    <p>
        <i class="success-icon"></i>
    </p>
    <p>${message}</p>
    <ul class="info-item">
        <#if service == 'ptp_mer_bind_card'>
            <li>
                <span class="info-title">开户银行</span>
                <span class="info-text"><#if bankName??>${bankName}</#if></span>
            </li>
            <li>
                <span class="info-title">银行卡号:</span>
                <span class="info-text"><#if cardNumber??>${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}</#if></span>
            </li>
        <#elseif service == 'mer_recharge_person'>
            <li>
                <span class="info-title">所用卡号</span>
                <span class="info-text"><#if bankName??>${bankName}</#if> <#if cardNumber??>${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}</#if></span>
            </li>
            <li>
                <span class="info-title">充值金额</span>
                <span class="info-text">${rechargeAmount}元</span>
            </li>
            <li>
                <span class="info-title">订单号</span>
                <span class="info-text">${orderId}</span>
            </li>
        <#elseif service == 'cust_withdrawals'>
            <li>
                <span class="info-title">到账卡号</span>
                <span class="info-text"><#if bankName??>${bankName}</#if> <#if cardNumber??>${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}</#if></span>
            </li>
            <li>
                <span class="info-title">提取金额</span>
                <span class="info-text">${withdrawAmount}元</span>
            </li>
            <li>
                <span class="info-title">订单号</span>
                <span class="info-text">${orderId}</span>
            </li>
        <#elseif ["project_transfer_invest","project_transfer_transfer",
        "project_transfer_no_password_invest","project_transfer_no_password_transfer"]?seq_contains(service)>
            <li>
                <span class="info-title">投资金额</span>
                <span class="info-text">${investAmount}元</span>
            </li>
            <li>
                <span class="info-title">所投项目</span>
                <span class="info-text">${investName!}</span>
            </li>
            <li>
                <span class="info-title">项目编号</span>
                <span class="info-text">${loanId}</span>
            </li>
        <#elseif service == 'ptp_mer_replace_card'>
            <li><em class="col-info text-card">${replaceCardContent!}</em></li>
        </#if>
    </ul>
    <#if ["cust_withdrawals","mer_recharge_person","ptp_mer_bind_card"]?seq_contains(service)>
        <p class="fix-nav">客服电话：400-169-1188（服务时间：9:00-20:00）</p>
    </#if>
</div>
<a href="${href}" class="btn-disabled" id="Btn">3s</a>

<script>
    var num = 3,
        href = document.getElementById('Btn').getAttribute('href');
    function countTime() {
        if (num == 0) {
            document.getElementById('Btn').innerHTML = '确定';
            document.getElementById('Btn').setAttribute('href', href);
            document.getElementById('Btn').setAttribute('class', 'btn-success');
            clearInterval(timer);
        } else {
            document.getElementById('Btn').setAttribute('class', 'btn-disabled');
            document.getElementById('Btn').setAttribute('href', 'javascript:void(0)');
            document.getElementById('Btn').innerHTML = num + 's';
            num = num - 1;
        }
    }
    var timer = setInterval('countTime()', 1000);
</script>
</body>
</html>