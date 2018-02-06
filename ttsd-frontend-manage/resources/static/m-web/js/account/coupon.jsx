require('mWebStyle/account/coupon.scss');
let commonFun = require('publicJs/commonFun');
let $goBackIcon = $('#goBackIcon');
//领优惠券
$.when(commonFun.isUserLogin())
    .done(function () {
        commonFun.useAjax({
            url: '/assign-coupon',
            type: 'POST',
            contentType: 'application/json; charset=UTF-8'
        });
    })

$goBackIcon.on('click', function (e) {
    history.go(-1);
});