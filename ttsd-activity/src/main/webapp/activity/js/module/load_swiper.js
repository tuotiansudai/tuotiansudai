define(['swiper','commonFun'], function (aswiper) {
    var brower=commonFun.browserRedirect(),
        aswiper=false;
    if(brower=='mobile') {
        var cssUrl = staticServer+"/activity/style/dest/swiper-3.min.css";
        commonFun.loadCss(cssUrl);
        var aswiper = new Swiper('.swiper-container', {
            direction : 'horizontal',
            speed:300
        });
    }
    return aswiper;
});