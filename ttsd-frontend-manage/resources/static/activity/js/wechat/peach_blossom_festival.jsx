require('activityStyle/wechat/peach_blossom_festival.scss');
//var commonFun = require('publicJs/commonFun');

function calculationFun(doc, win) {
    let docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            let clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
             let fSize = 100 * (clientWidth /750);
           // let fSize = 20 * (clientWidth /375);
             fSize > 100 && (fSize = 98.4);
            docEl.style.fontSize = fSize + 'px';
        };
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
};
calculationFun(document, window);

let isGet = $('.container').data('get');
let isSuc = $('.container').data('success');
let startTime = $('.container').data('startTime');
let overTime = $('.container').data('overTime');
let activityTime = new Date(startTime.replace(/-/g, "/")).getTime(); // 活动开始时间
let activityOverTime = new Date(overTime.replace(/-/g, "/")).getTime();  // 活动结束时间

if (!isGet) {
    $('.get_it_btn').show();
}
else {
    $('.got_it_btn').show();
}

if (isSuc) {
    $('.pop_modal_container').show();
}
else if (isSuc === false) {
    $('.pop_modal_container_again').show();
}

$('.get-button').on('click',function () {
    let currentTime = new Date().getTime();
    if (currentTime < activityTime) {
        layer.msg('活动未开始');
    }
    else if (currentTime > activityOverTime) {
        layer.msg('活动已结束');
    }
    else {
        // location.href = '/activity/start-work/draw';
    }

});







