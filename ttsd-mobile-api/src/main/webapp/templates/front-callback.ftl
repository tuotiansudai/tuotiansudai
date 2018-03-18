<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <title>${(values.message)!}</title>
    <link rel="stylesheet" type="text/css" href="${css.index!}" charset="utf-8"/>
</head>

<body class="success-body">
<div class="success-info-container">
    <p>
        <i class="success-icon"></i>
    </p>
    <p>${(values.message)!}</p>
<#switch service>
    <#case 'ptp_mer_bind_card'>
        <ul class="info-item">
            <li>
                <span class="info-title">银行卡号:</span>
                <span class="info-text">${values.cardNumber!}</span>
            </li>
        </ul>
        <#break>
    <#case 'ptp_mer_replace_card'>
        <p>换卡申请最快1个工作日内完成</p>
        <#break>
    <#case 'mer_recharge_person'>
        <ul class="info-item">
            <li>
                <span class="info-title">充值卡号</span>
                <span class="info-text">${(values.bankName)!} ${values.cardNumber!}</span>
            </li>
            <li>
                <span class="info-title">充值金额</span>
                <span class="info-text">${(values.rechargeAmount)!}元</span>
            </li>
            <li>
                <span class="info-title">订单号</span>
                <span class="info-text">${(values.orderId)!}</span>
            </li>
        </ul>
        <#break>
    <#case 'cust_withdrawals'>
        <ul class="info-item">
            <li>
                <span class="info-title">到账卡号</span>
                <span class="info-text">${(values.bankName)!} ${values.cardNumber!}</span>
            </li>
            <li>
                <span class="info-title">提取金额</span>
                <span class="info-text">${values.withdrawAmount}元</span>
            </li>
            <li>
                <span class="info-title">订单号</span>
                <span class="info-text">${(values.orderId)!}</span>
            </li>
        </ul>
        <#break>
    <#case 'invest_project_transfer'>
    <#case 'invest_transfer_project_transfer'>
    <#case 'invest_project_transfer_nopwd'>
    <#case 'invest_transfer_project_transfer_nopwd'>
        <ul class="info-item">
            <li>
                <span class="info-title">投资金额</span>
                <span class="info-text">${(values.investAmount)!}元</span>
            </li>
            <li>
                <span class="info-title">所投项目</span>
                <span class="info-text">${(values.loanName)!}</span>
            </li>
            <li>
                <span class="info-title">项目编号</span>
                <span class="info-text">${(values.loanId)!}</span>
            </li>
        </ul>
        <#break>
    <#case 'membership_privilege_purchase_transfer_asyn'>
        <p>投资时将享受服务费7折优惠</p>
        <#break>
</#switch>
    <p class="fix-nav">客服电话：400-169-1188（服务时间：9:00-20:00）</p>
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