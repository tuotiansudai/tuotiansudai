define([], function () {
    require('webStyle/module/coupon_alert.scss');
    var couponAlertFrame = globalFun.$('#couponAlertFrame');
    if(couponAlertFrame) {
        var  beginGetFree = globalFun.$('#beginGetFree'),
            couponChild = couponAlertFrame.children,
            btnCoupon = couponChild[0];

        function closeAlert() {
            couponAlertFrame.parentNode.style.display = 'none';
        }
        globalFun.addEventHandler(btnCoupon,'click',closeAlert);
        globalFun.addEventHandler(beginGetFree,'click',closeAlert);
    }
});




