require('mWebStyle/account/coupon.scss');
let commonFun = require('publicJs/commonFun');
let $goBackIcon = $('#goBackIcon');
//领优惠券

$goBackIcon.on('click', function (e) {
    location.href = '/m/account';
});