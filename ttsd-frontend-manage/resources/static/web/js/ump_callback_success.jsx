let commonFun= require('publicJs/commonFun');

let $successBox= $('#successBox');
let $countTime = $('.count-time',$successBox);

commonFun.countDownLoan({
    btnDom:$countTime,
    time:5
},function() {
    window.location.href = '/personal-info';
});

