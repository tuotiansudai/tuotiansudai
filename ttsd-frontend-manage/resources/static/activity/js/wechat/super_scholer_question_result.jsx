require('activityStyle/wechat/super_scholer_question_result.scss');
let commonFun= require('publicJs/commonFun');
function calculationFun(doc, win) {
    let docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            let clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
             let fSize = 100 * (clientWidth /750);
             fSize > 100 && (fSize = 98.4);
            docEl.style.fontSize = fSize + 'px';
        };
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
};
calculationFun(document, window);

var $shareActivity = $('.share-activity'),
    $inviteFriend = $('.invite-friend');

$shareActivity.on('click',function () {
    $('#inviteBox').hide();
    $('#shareBox').show();


});

$inviteFriend.on('click',function () {
    $('#shareBox').hide();
    $('#inviteBox').show();

});
$('.share-tip-btn').on('click',function () {
    location.href = '/activity/super-scholar/view/result?shareType=activityHome';
})
$('.invite-tip-btn').on('click',function () {
    location.href = '/activity/super-scholar/view/result?shareType=activityRegister';
})

var link = webServer + '/activity/super-scholar?come=wechat';
var titleShare = '我在拓天速贷答题赢加薪，邀请你来测一测学霸指数';
var descShare = '你是学霸还是学渣？答题见分晓！';
let mobileStr = mobile.substring(0,3)+'****'+mobile.substring(8,11);

if (shareType === 'activityHome'){
    link = webServer + '/activity/super-scholar?come=wechat';
    titleShare = '我在拓天速贷答题赢加薪，邀请你来测一测学霸指数';
    descShare = '你是学霸还是学渣？答题见分晓！';

}else{
    link = webServer + '/activity/super-scholar/share/register?referrerMobile=' + mobile + '&come=wechat';
    titleShare = '明明可以自己偷偷赚钱，但我还是想叫上你';
    descShare = '友谊的小船变巨轮，'+mobileStr+'送您6888元体验金，邀您一起来赚钱';
}

wx.ready(function () {
    wx.onMenuShareAppMessage({
        title: titleShare, // 分享标题
        desc: descShare, // 分享描述
        link: link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: commonStaticServer + '/images/icons/red_ware_money.png', // 分享图标
        success: function () {
            commonFun.useAjax({
                dataType: 'json',
                url: '/activity/super-scholar/share/success',
                type: 'GET'

            }, function (response) {

            })
        },
        cancel: function () {
        }
    });

    wx.onMenuShareTimeline({
        title: '我在拓天速贷答题赢加薪，邀请你来测一测学霸指数', // 分享标题
        desc: '你是学霸还是学渣？答题见分晓！', // 分享描述
        link: link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: commonStaticServer + '/images/icons/red_ware_money.png', // 分享图标
        success: function () {
            // 用户确认分享后执行的回调函数
            commonFun.useAjax({
                dataType: 'json',
                url: '/activity/super-scholar/share/success',
                type: 'GET'

            }, function (response) {

            })
        },
        cancel: function () {
            // 用户取消分享后执行的回调函数
        }
    });
});



