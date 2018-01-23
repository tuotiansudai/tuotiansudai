require('mWebStyle/account/coupon.scss');

let $goBackIcon = $('#goBackIcon');


$goBackIcon.on('click', function (e) {
    location.href = "/m/account"
});