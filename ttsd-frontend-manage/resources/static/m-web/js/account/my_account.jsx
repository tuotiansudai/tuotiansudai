require('mWebStyle/account/my_account.scss');
let commonFun = require('publicJs/commonFun');
let $myAccount = $('#myAccount'),
    $summaryBox = $('.summary-box',$myAccount);
commonFun.calculationFun(document,window);

$('.footer-wap-container').find('a').removeClass('current').eq(3).addClass('current');
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
let imgUrl = require('../../images/account/anonymous_bg.png');
let img = new Image();
img.src = imgUrl;
$('#imgContainer').append(img);
function isIphoneX(){
    return /iphone/gi.test(navigator.userAgent) && (screen.height == 812 && screen.width == 375)
}

if(isIphoneX()){
$('.anomous-tip').css({
    'height':'6rem',
    'paddingTop':'1.5rem'

});
}
if($('.anonymous-page').length){
    $('body').css('backgroundColor','#fff8aa')
}

$('#noBankCardRecharge').click(function() {
   $('#bindCardForm').submit();
   return false;
});

$('#noBankCardWithdraw').click(function() {
    $('#bindCardForm').submit();
    return false;
});





