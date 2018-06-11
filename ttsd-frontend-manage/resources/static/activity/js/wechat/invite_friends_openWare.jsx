require('activityStyle/wechat/invite_friends_openWare.scss');
let commonFun= require('publicJs/commonFun');

commonFun.calculationRem(document, window);



let activityStatus = commonFun.activityStatus($('#inviteContainer'));
let $inviteBtn = $('.invite-btn');
let $wechatTip = $('.wechat-tip');
let $openBtn = $('.open-btn');
let countTime = $('#countdown').val();

if(countTime){
    commonFun.countTimePop($('.time'),countTime);
}else{
    alert("未邀请情况----");
}

// 点击“邀请好友拆红包”，未登录时跳转到登录页面；
//
// 已登录时点击“邀请好友拆红包”button，页面弹窗提示“活动期间内只有一次机会，是否确认邀请？”点击取消弹窗消失停留在当前页，点击确认，页面弹窗提示右上角分享，点击“我知道了”弹窗消失；
$inviteBtn.on('click',function () {
    $.when(commonFun.isUserLogin())
        .done(function () {
            if(activityStatus!== 'activity-ing') {
                    layer.msg('不在活动时间范围内');
            }else {
                $wechatTip.show();
            }
        })
        .fail(function () {
            location.href = '/m/login'
        })
})
$('.tip-btn').on('click',function () {
    $wechatTip.hide();
})
$('.to-join-btn').on('click',function () {
    location.href = '/activity/third-anniversary'
})

$openBtn.on('click',function () {
    $.when(commonFun.isUserLogin())
        .done(function () {
            if(activityStatus!== 'activity-ing') {
                layer.msg('不在活动时间范围内');
            }else {
                $wechatTip.show();
            }
        })
        .fail(function () {
            location.href = '/m/login'
        })
})
