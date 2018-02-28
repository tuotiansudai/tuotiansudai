let commonFun= require('publicJs/commonFun');

let $successBox= $('#successBox');

if ($successBox.length) {
    let $countTime = $('.count-time',$successBox);

    commonFun.countDownLoan({
        btnDom:$countTime,
        time:5
    },function() {
        window.location.href = '/personal-info';
    });
}

$('.see_my_account').on('click',() => {
    location.href = '/account';
});

$('.see_other_project').on('click',() => {
    location.href = '/loan-list';
});

$('.go_to_recharge').on('click',() => {
    location.href = '/recharge';
});

$('.go_to_invest').on('click',() => {
    location.href = '/loan-list';
});

$('.my_personal-info').on('click',() => {
    location.href = '/personal-info';
});
