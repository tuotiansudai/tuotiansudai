<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>正在载入...</title>
    <link rel="stylesheet" type="text/css" href="${css.index!}" charset="utf-8"/>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>
<#if pay??>

<div class="my-account-content personal-profile loading-page">
    <div class="gif-container">
    </div>

</div>
    <#if pay.status>
    <form id="payForm" action="${pay.url}" method="post">
        <input type="hidden" name="reqData" value='${pay.data}'/>
    </form>
    </#if>
</#if>
<script>

    countDown($('#secondsCountDown'), 5, function () {
        $("#isPaySuccess").submit();
    });
    $('#payForm').submit();

    function countDown(dom, time, callback) {
        var downtimer;
        var countDownStart = function () {

            if (time == 0) {
                //结束倒计时
                clearInterval(downtimer);
                callback && callback();
            } else {
                time = time - 1;
                dom.text(time);
            }

        }
        if (time > 0) {
            downtimer = setInterval(function () {
                countDownStart();
            }, 1000);
        }
    }
</script>

</body>
</html>