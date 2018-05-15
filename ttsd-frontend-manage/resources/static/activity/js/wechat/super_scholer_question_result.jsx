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

var $shareActivity = $('.share-activity'),
    $inviteFriend = $('.invite-friend');

$shareActivity.on('click',function () {
    location.href = '/activity/super-scholar/view/result?shareType=activityHome';
});

$inviteFriend.on('click',function () {
    location.href = '/activity/super-scholar/view/result?shareType=activityRegister';
});



