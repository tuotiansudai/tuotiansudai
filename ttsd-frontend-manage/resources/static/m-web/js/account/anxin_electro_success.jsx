require('mWebStyle/account/anxin_electro_success.scss');


$('#openAuthorization').on('click',function() {

    location.href = 'anxin-authorization.ftl'
});


(function (doc, win) {
    let docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            let clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            let fSize = 20 * (clientWidth /375);
            fSize > 40 && (fSize = 39.36);
            docEl.style.fontSize = fSize + 'px';
        };

    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);


// 点击返回btn
$('.go-back-container').on('click',() => {
    history.go(-1);
});
