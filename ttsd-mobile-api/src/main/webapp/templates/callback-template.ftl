<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <title>拓天速贷</title>
    <link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}base.css" charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}ttsd-mobile.css" charset="utf-8"/>
</head>
<body>
<div class="main-layout">
    <h3 class="info-title"><i></i>${message}</h3>
    <ul class="info-detail">
        <li>
            <span class="title">您已成为V5会员！投资时将享受服务费7折优惠。</span>
        </li>

    </ul>

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