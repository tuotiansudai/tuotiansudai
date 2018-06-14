require('activityStyle/wechat/invite_friends_openWare.scss');
let commonFun= require('publicJs/commonFun');

commonFun.calculationRem(document, window);


let $inviteBtn = $('.invite-btn');
let $wechatTipWin = $('#wechatTipWin');
let $inviteTipWin = $('#inviteTipWin');
let $openBtn = $('.open-btn');

if($('#inviteContainer').length){
    let countTime = $('#inviteContainer').data('countdown');
    if(countTime){
        commonFun.countTimePop($('.time'),countTime);
    }else{
        $('.time').text('72:00:00');
    }
}

if($('#openWareContainer').length){
    let countTimeWare = $('#openWareContainer').data('countdown');
    if(countTimeWare){
        commonFun.countTimePop($('.time'),countTimeWare);
    }else{
        $('.time').text('72:00:00');
    }
}

if($('#getWare').find('li').length>0){
    $('#getWare').show();
    $('.bolang2').show();
}else {
    $('#getWare').hide();
    $('.bolang2').hide();
}

// 点击“邀请好友拆红包”，未登录时跳转到登录页面；
//
// 已登录时点击“邀请好友拆红包”button，页面弹窗提示“活动期间内只有一次机会，是否确认邀请？”点击取消弹窗消失停留在当前页，点击确认，页面弹窗提示右上角分享，点击“我知道了”弹窗消失；
$inviteBtn.on('click',function () {
    $.when(commonFun.isUserLogin())
        .done(function () {

            $inviteTipWin.show();
        })
        .fail(function () {
            location.href = '/m/login'
        })
})
$('.open-now').on('click',function () {
    $inviteTipWin.hide();
    $wechatTipWin.show();
})
$('.tip-btn').on('click',function () {
    $wechatTipWin.hide();
})
$('.to-join-btn').on('click',function () {
    location.href = '/activity/third-anniversary'
})

$openBtn.on('click',function () {

})
//先不拆了
$('.no-open').on('click',function () {
    $inviteTipWin.hide();
})
//分享
wx.ready(function () {
    wx.onMenuShareAppMessage({
        title: '我得到了一个神秘生日红包，邀你和我一起拆开', // 分享标题
        desc: '拓天速贷3岁生日豪撒现金红包，老友新朋一起拆拆拆！', // 分享描述
        // title: '哈哈哈哈', // 分享标题
        // desc: '拓天速贷3岁生日！', // 分享描述
        link: webServer+'/activity/third-anniversary/share-page?originator='+originator+'&come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: commonStaticServer+'/images/icons/openWare.png', // 分享图标
        success: function () {
            if($('#inviteContainer').length){
                commonFun.useAjax({
                        url: '/activity/third-anniversary/share-invite',
                        type: 'POST'
                    },function () {
                        location.href="/activity/third-anniversary/invite-page?time=" + ((new Date()).getTime());
                    }
                )
            }

        },

    });

    wx.onMenuShareTimeline({
        title: '我得到了一个神秘生日红包，邀你和我一起拆开', // 分享标题
        desc: '拓天速贷3岁生日豪撒现金红包，老友新朋一起拆拆拆！', // 分享描述
        // title: '哈哈哈哈', // 分享标题
        // desc: '三岁生日！', // 分享描述
        link: webServer+'/activity/third-anniversary/share-page?originator='+originator+'&come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: commonStaticServer+'/images/icons/openWare.png', // 分享图标
        success: function () {
            // 用户确认分享后执行的回调函数
            if($('#inviteContainer').length){
                commonFun.useAjax({
                        url: '/activity/third-anniversary/share-invite',
                        type: 'POST'
                    },function () {
                        location.href="/activity/third-anniversary/invite-page?time=" + ((new Date()).getTime());
                    }
                )
            }
        }
    });
});


