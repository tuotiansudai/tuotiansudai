require('activityStyle/wechat/invite_friends_shared.scss');
let commonFun = require('publicJs/commonFun');
let sourceKind = globalFun.parseURL(location.href);
commonFun.calculationFun(document,window);
let timer;
$('.help_rightNow').on('click',function () {
    if ($(this).hasClass('no_click')) {
        layer.msg('助力已结束');
        return;
    }
    alert(1)
});
$('.help_too').on('click',() => {
    alert(2)
});

$('.withdraw_cash').on('click',() => {
    location.href = '/m/account';
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
            $('.help_rightNow').removeClass('help_rightNow').addClass('no_click');
            $('.pic_wrapper').hide();
            $('.time_over').show();
        }
    }
}

countTimePop('2018-4-17 12:21:30');







