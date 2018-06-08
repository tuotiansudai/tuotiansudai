require('mWebStyle/bank_callback.scss');
let commonFun= require('publicJs/commonFun');
if($('.loading-page').length){
    let $countTime = $('#secondsCountDown');

    commonFun.countDownLoan({
        btnDom:$countTime,
        time:5,
        textCounting:'',
        isAfterText:'1'
    },function() {
        $("#isPaySuccess").submit();
    });
}
