define([], function () {
    let commonFun = require('publicJs/commonFun');

    let $errorContainer = $('#errorContainer');
    if ($errorContainer.length) {
        var $jumpTip = $('.jump-tip i', $errorContainer);
        commonFun.countDownLoan({
            btnDom: $jumpTip,
            time: 10,
            textCounting: ''
        }, function () {
            window.location = "/";
        });
    };

    var browser={
        versions:function(){
            var u = navigator.userAgent, app = navigator.appVersion;
            return {
                mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
            };
        }()
    };

//判断是否移动端
    if(browser.versions.mobile||browser.versions.android||browser.versions.ios){
        let commonFun = require('publicJs/commonFun');
        let meta="<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\">";
        $("head").prepend(meta);
        // M站隐藏一系列头部
        $('.header-download').hide();
        $('.header-top').hide();
        $('.nav-container').hide();
        $('.app-container').show();
        $('#errorContainer').css('marginTop','70px');

        $('.close-app').on('click',() => {
            $('.app-container').hide();
            $('#errorContainer').css('marginTop',0);
        });

        $('.open-app').on('click',function () {
            if(commonFun.phoneModal() == 'android'){
                location.href = 'http://tuotiansudai.com/app/tuotiansudai.apk';
            }else if(commonFun.phoneModal() == 'ios'){
                location.href = 'https://itunes.apple.com/cn/app/id1039233966';
            }
        });
    }
});
