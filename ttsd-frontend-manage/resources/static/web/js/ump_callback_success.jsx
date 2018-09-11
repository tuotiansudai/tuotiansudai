require('webStyle/ump_callback_success.scss');
let commonFun= require('publicJs/commonFun');

let $successBox= $('#successBox');
let $countTime = $('.count-time',$successBox);
let accountUrl = $successBox.data('accounturl');

commonFun.countDownLoan({
    btnDom:$countTime,
    time:5
},function() {
    window.location.href = accountUrl;
});

