<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>正在处理中...</title>
    <link rel="stylesheet" type="text/css" href="${css.index!}" charset="utf-8"/>
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="yes" name="apple-touch-fullscreen">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta charset="UTF-8" name="viewport" content="width=device-width,initial-scale=1,target-density dpi=high-dpi,user-scalable=no">
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>

<div class="my-account-content personal-profile loading-page">

    <div class="gif-container">
        <img id="gifImg" src="" alt="">
    </div>
    <div class="progress-container">
        正在处理中：<span id="secondsCountDown">5</span>
    </div>
    <form id="isPaySuccess"
          action="${requestContext.getContextPath()}/callback/${bankCallbackType.name()}/order-no/${orderNo}/is-success"
          method="post" style="display: none">
    </form>
</div>

<script>
    countDown($('#secondsCountDown'),5,function () {
        $("#isPaySuccess").submit();
    });
    function countDown(dom,time,callback){
        var downtimer;
        var countDownStart=function(){

            if(time==0) {
                //结束倒计时
                clearInterval(downtimer);
                callback && callback();
            } else {
                time = time-1;
                dom.text(time);
            }

        }
        if(time>0) {
            downtimer=setInterval( function() {
                countDownStart();
            },1000);
        }
    }
</script>

</body>
</html>