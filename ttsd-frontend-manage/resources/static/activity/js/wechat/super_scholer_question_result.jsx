require('activityStyle/wechat/super_scholer_question_result.scss');
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

let shareUrl;
let $shareActivity = $('.share-activity'),
    $inviteFriend = $('.invite-friend');

$shareActivity.on('click',function () {
    shareUrl = '${webServer}/activity/super-scholar?come=wechat';
})
$inviteFriend.on('click',function () {
    shareUrl = '${webServer}/activity/super-scholar/share/register?come=wechat';
})
wx.ready(function () {
    wx.onMenuShareAppMessage({
        title: '我在拓天速贷答题赢加薪，邀请你来测一测学霸指数', // 分享标题
        desc: '你是学霸还是学渣？答题见分晓！', // 分享描述
        link: shareUrl, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标
        success: function () {
            // ajax
            commonFun.useAjax({
                dataType: 'json',
                url: '/activity/super-scholar/share/success',
                type: 'post'

            }, function (response) {

            })
        },
        cancel: function () {
        }
    });

    wx.onMenuShareTimeline({
        title: '我在拓天速贷答题赢加薪，邀请你来测一测学霸指数', // 分享标题
        desc: '你是学霸还是学渣？答题见分晓！', // 分享描述
        link: '${webServer}/activity/super-scholar?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标
        success: function () {
            // 用户确认分享后执行的回调函数
        },
        cancel: function () {
            // 用户取消分享后执行的回调函数
        }
    });
});



