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
    <p>${(values.message)!}</p>
<#switch service>
    <#case 'project_transfer_nopwd'>
    <#case 'project_transfer'>
        <p>账户激活成功！</p>
        <#break>
    <#case 'hui_zu_password_repay_project_transfer'>
        <p>租金还款成功！</p>
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