require('mWebStyle/account/my_account.scss');
let commonFun = require('publicJs/commonFun');
let $myAccount = $('#myAccount'),
    $summaryBox = $('.summary-box',$myAccount);

$('.footer-wap-container').find('a').removeClass('current').eq(2).addClass('current');
let $myMenu = $('.menu-my');
if($myMenu.length){
    $myMenu.on('click',function (e) {
        e.preventDefault();
        $.when(commonFun.isUserLogin())
            .done(function () {
                location.href = '/m/account'
            }).fail(function () {
            location.href = '/m/account-anonymous'
        })

    })
}

