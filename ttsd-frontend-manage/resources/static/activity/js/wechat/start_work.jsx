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

