require('mWebStyle/investment/buy_free.scss');
let commonFun = require('publicJs/commonFun');
require('webJs/plugins/autoNumeric');


let $confirmInvest = $('#countDownBtn');
commonFun.countDownLoan({
    btnDom:$confirmInvest,
    time:3,
    textCounting:'S',
    isAfterText:'确定'
},function () {
    $confirmInvest.on('click',function () {
        location.href='/m'
    })
})




