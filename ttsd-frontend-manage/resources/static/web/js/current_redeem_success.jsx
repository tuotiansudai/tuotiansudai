require('webStyle/investment/current_redeem_success.scss');
let commonFun= require('publicJs/commonFun');

let $successBox= $('#successBox');
let $countTime = $('.count-time',$successBox);

commonFun.countDownLoan({
    btnDom:$countTime,
    time:5
},function() {
    window.location.href = '/account';
});

