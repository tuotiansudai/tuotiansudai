require("activityStyle/coupon_special_2017.scss");
let browser = globalFun.browserRedirect();
let commonFun= require('publicJs/commonFun');

let $wechatCoupon = $('#wechatCouponSpecial');

//PC页面
if($('#pcCouponSpecial').length) {

    let redBagUrl = require('../images/2017/coupon-special/red-all.png');
    let topHeaderUrl = require('../images/2017/coupon-special/head-coupon-wap.png');
    document.getElementById('redBag').src = redBagUrl;

    if(browser =='mobile') {
        let  topImg= new Image();
        topImg.src = topHeaderUrl;
        topImg.onload = function() {
            $('#topHeader').append(topImg);
        }
    }
} else if($wechatCoupon.length) {
    $('btn-receive',$wechatCoupon).on('click',function() {
        $(this).prop('disabled',true);
        location.href='/activity/celebration-coupon/draw';
    });
    window.onload = function() {
        layer.msg('每个用户只能领取一次哦！',{type: 1, time: 200000000});
        let drew = $wechatCoupon.data('drew');
        if(drew) {
            layer.msg('每个用户只能领取一次哦！');
        }
    }

}

