require('activityStyle/wechat/start_work.scss');
let commonFun = require('publicJs/commonFun');

(function (doc, win) {
    var docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            var clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            var fSize = 20 * (clientWidth /375);
            fSize > 40 && (fSize = 39.36);
            docEl.style.fontSize = fSize + 'px';
        };

    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
    $('body').css('visibility', 'visible');
})(document, window);

let isGet = $('.container').data('get');
if (!isGet) {
    $('.get_it_btn').show();
}
else {
    $('.got_it_btn').show();
}

$('.get_it_btn').on('click',function () {
    $('.pop_modal_container').show();
    // layer.msg('活动未开始');
    // layer.msg('活动已结束');
});

$('.closeBtn').on('click',function () {
    $('.pop_modal_container').hide();
});

$('.see_my_redPocket').on('click',function () {
   location.href = '/m/my-treasure';
});


