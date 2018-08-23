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

$('#toProject').on('click',() => {
    location.href = '/loan-list';
});

$('.toRecharge').on('click',() => {
    location.href = '/recharge';
});

$('.investBtn').on('click',() => {
    location.href = '/loan-list';
});

$('.retry').on('click',() => {
    $('#retry-form').submit();
    return false;
});
$('.personal-info').on('click',() => {
    location.href = '/personal-info';
});
if($('#registerSuccess').length){
    let referParam = globalFun.parseURL(location.href);
    let referrer = referParam.params.referrer;
    if(referrer &&referrer == 'loan'){
        $('.toLocationBtn').text('去借款');
    }
    
    $('.toLocationBtn').click(function () {
        if(referrer &&referrer == 'loan'){
            location.href = '/loan-application';
        }else {
            location.href = '/bind-card';
        }

    })
}

$('.my_personal-info').on('click',() => {
    location.href = '/personal-info';
});

let $bankResult = $('#bankResult');
if($('#bankResult').length){
    let $countTime = $('.count-down',$bankResult);

    commonFun.countDownLoan({
        btnDom:$countTime,
        time:5,
        textCounting:'',
        isAfterText:'1'
    },function() {
        $("#isPaySuccess").submit();
    });
}



