require('mWebStyle/bank_callback.scss');

if($('.loading-page').length){
    var arrImg = [];
    for(let i = 0; i < 26; i++) {
        arrImg.push(require('../images/gif/gif' +(i+1) + '.png'))

    }
    var count = 0;

    // var timer = setInterval(function () {
    //     if(count == 26){
    //         count = 0;
    //     }
    //     count++;
    //     $('#gifImg').attr('src',require('../images/gif/gif' +count + '.png'));
    //
    // },100);

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
