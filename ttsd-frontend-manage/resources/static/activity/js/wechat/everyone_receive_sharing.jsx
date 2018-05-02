require('activityStyle/wechat/everyone_receive_sharing.scss');
let commonFun = require('publicJs/commonFun');
let sourceKind = globalFun.parseURL(location.href);
commonFun.calculationFun(document,window);
let timer;

$('.invite_friends_btn').on('click',() => {
    if(is_wechat()) {
        $('.wechat_share_tip').show();
    }
    else {
        alert('请在微信中分享');
    }
});

$('.withdraw_cash').on('click',function () {
    location.href = '/activity/invite-help/true/wechat/'+ $("#helpId").data("helpId") +'/withdraw';
});

$('.wechat_share_tip').on('click',function () {
    $(this).hide();
});

$('.rules').on('click',() => {
    $('.flex_rules').show();
    $('body').css('overflow','hidden');
});

$('.close_rules').on('click',() => {
    $('.flex_rules').hide();
    $('body').css('overflow','auto');
});

function countTimePop(str) {
    $('.pic_wrapper').show();
    $('.time_over').hide();
    let end = new Date(str).getTime();
    let now = new Date().getTime();
    let leftTime = (end-now)/1000;
    timerCount();
    timer = setInterval(() => {
        timerCount();
    },1000);
    function timerCount() {
        let h,m,s;
        if (leftTime > 0) {
            if (leftTime <= 1) {
                clearInterval(timer);
                $('.pic_wrapper').hide();
                $('.time_over').show();
                return;
            }
            h = Math.floor(leftTime/60/60%24);
            m = Math.floor(leftTime/60%60);
            s = Math.floor(leftTime%60);
            h = h < '10' ? '0' + h : h + '';
            m = m < '10' ? '0' + m : m + '';
            s = s < '10' ? '0' + s : s + '';

            $('.hour1').html(h.charAt(0));
            $('.hour2').html(h.charAt(1));
            $('.minutes1').html(m.charAt(0));
            $('.minutes2').html(m.charAt(1));
            $('.seconds1').html(s.charAt(0));
            $('.seconds2').html(s.charAt(1));
            --leftTime;
        }
        else {
            clearInterval(timer);
            $('.pic_wrapper').hide();
            $('.time_over').show();
        }
    }
}
countTimePop($("#countDown").data("countdown").replace(/-/g,'/'));

function is_wechat(){
    return navigator.userAgent.toLowerCase().match(/MicroMessenger/i)=="micromessenger";
}







