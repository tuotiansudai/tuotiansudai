require('mWebStyle/operational.scss');

import Swiper from 'swiper';

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

let mySwiper = new Swiper('.swiper-container', {
    direction : 'vertical',
    width: window.screen.width,
    height: document.body.clientHeight - 44,
});


$('#goBack_experienceAmount').on('click',() => {
    history.go(-1);
});


$('.side_to_page').click(function(e){
    let index = e.currentTarget.dataset.index;
    index = index == 4 ? 0 : index;
    mySwiper.slideTo(index, 500, false);//切换到第一个slide，速度为1秒
});

