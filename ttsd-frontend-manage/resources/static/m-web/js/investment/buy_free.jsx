require('mWebStyle/investment/buy_free.scss');
let commonFun = require('publicJs/commonFun');
require('webJs/plugins/autoNumeric');


let $confirmInvest = $('.count-btn');
commonFun.countDownLoan({
    btnDom: $confirmInvest,
    time: 3,
    textCounting: 's',
    isAfterText: '确定'
}, function () {

});
$confirmInvest.on('click', function () {
    location.href = $(this).data('url');
})

$('.money').autoNumeric('init');
commonFun.calculationFun(document,window);



