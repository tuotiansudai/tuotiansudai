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
    commonFun.CommonLayerTip({
        btn: ['去绑卡','取消'],
        area:['280px', '160px'],
        content: `<div class="record-tip-box"> <b class="pop-title">温馨提示</b> <p style="text-align: center">您还没有绑卡，请先进行绑卡</p></div> `
    },function() {
        $('#bindCardForm').submit();
    })
   return false;
});

$('#noBankCardWithdraw').click(function() {
    commonFun.CommonLayerTip({
        btn: ['去绑卡','取消'],
        area:['280px', '160px'],
        content: `<div class="record-tip-box"> <b class="pop-title">温馨提示</b> <p style="text-align: center">您还没有绑卡，请先进行绑卡</p></div> `
     },function() {
        $('#bindCardForm').submit();
    })

    return false;
});


$('#accountBtn').on('click',function () {
    location.href = '/register/account'
})
$('.btn-close').on('click',function () {
    layer.closeAll();
})


