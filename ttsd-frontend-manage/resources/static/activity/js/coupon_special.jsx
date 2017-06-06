require("activityStyle/coupon_special.scss");
let browser = globalFun.browserRedirect();
let commonFun= require('publicJs/commonFun');

let $wechatCoupon = $('#wechatCouponSpecial')
//PC页面
// layer.msg('每个用户只能领取一次哦');
layer.msg('邮箱绑定失败，请重试！', {time: 200000});
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
    //微信端
    $wechatCoupon.find('.btn-receive').on('click',function() {
        commonFun.useAjax({
            url:'',
            type:''
        },function(data) {
            //领取成功
            location.href='success'
            layer.msg('每个用户只能领取一次哦');
        });

    });

}

