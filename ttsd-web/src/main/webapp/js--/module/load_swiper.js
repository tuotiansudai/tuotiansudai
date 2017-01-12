define(['swiper'], function (aswiper) {
    var brower=globalFun.browserRedirect(),
        aswiper=false;
    if(brower=='mobile') {
        var cssUrl = staticServer+"/style/dest/swiper-3.min.css";
        globalFun.loadCss(cssUrl);
        var aswiper = new Swiper('.swiper-container', {
            direction : 'horizontal',
            speed:300,
            width : 80
        });
    }
    return aswiper;
});