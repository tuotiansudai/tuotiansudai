require('mWebStyle/layout.scss');
require('mWebStyle/layer_diy.scss');
require('mWebStyle/account/account_global.scss');
require('mWebStyle/investment/investment_global.scss');

require('mWebStyle/investment/notice_transfer.scss');

let FastClick = require('fastclick');
FastClick.attach(document.body);

$('.footer-wap-container').on('click', function () {
    let $this = $(this);
    location.href = $this.data('url');
});

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
})(document, window);
