require('mWebStyle/investment/buy_free.scss');
let commonFun = require('publicJs/commonFun');
require('webJs/plugins/autoNumeric');

$amountInputElement.autoNumeric('init');



function getInvestAmount() {
    var amount = 0;
    if (!isNaN($amountInputElement.autoNumeric("get"))) {
        amount = parseInt(($amountInputElement.autoNumeric("get") * 100).toFixed(0));
    }

    return amount;
}

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




