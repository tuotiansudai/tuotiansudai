require('mWebStyle/layout.scss');
require('mWebStyle/layer_diy.scss');
require('mWebStyle/account/account_global.scss');
require('mWebStyle/investment/investment_global.scss');

require('mWebStyle/investment/notice_transfer.scss');

let FastClick = require('fastclick');
FastClick.attach(document.body);

$('.footer-wap-container').on('click', function () {
    let $this = $(this);
    location.href = $this.data('url');
});
$('.menu-invest').on('click',function () {
    alert(9)
})


