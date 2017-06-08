require("activityStyle/coupon_special_2017.scss");
let browser = globalFun.browserRedirect();
let commonFun= require('publicJs/commonFun');

let $wechatCoupon = $('#wechatCouponSpecial');

//PC页面
if($('#pcCouponSpecial').length) {

    let redBagUrl = require('../images/2017/coupon-special/red-all.png');
    let redBagBottomUrl = require('../images/2017/coupon-special/bg-red.png');
    let topHeaderUrl = require('../images/2017/coupon-special/head-coupon-wap.png');
    document.getElementById('redBag').src = redBagUrl;

    if(browser =='mobile') {
        let  topImg= new Image();
        topImg.src = topHeaderUrl;
        topImg.onload = function() {
            $('#topHeader').append(topImg);
        }

        let  bottomImg= new Image();
        bottomImg.src = redBagBottomUrl;
        bottomImg.onload = function() {
            $('#mobileImg').append(bottomImg);
        }
    }


    let locationUrl = location.href;
    let parseURL = globalFun.parseURL(locationUrl);
    let IsShare = parseURL.params.from;
    if(IsShare=='wechat') {
        $('.header-container,.nav-container,.nav-container,.footer-responsive').hide();
    }

} else if($wechatCoupon.length) {
    $('.btn-receive',$wechatCoupon).on('click',function() {
            location.href='/activity/celebration-coupon/draw';

        $(this).prop('disabled',true);
    });

    window.onload = function() {
        let drew = $wechatCoupon.data('drew');
        if(drew) {
            layer.msg('每个用户只能领取一次哦！');
        }
    }
}

