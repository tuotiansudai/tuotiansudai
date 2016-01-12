define(['swiper'], function (aswiper) {
    var loadCss=function(url) {
        var link = document.createElement("link");
        link.type = "text/css";
        link.rel = "stylesheet";
        link.href = url;
        document.getElementsByTagName("head")[0].appendChild(link);
    }

    var widthScreen=$(window).width();
    if(widthScreen<700) {
        var cssUrl = staticServer+"/style/dest/swiper-3.min.css";
        loadCss(cssUrl);
        var aswiper = new Swiper('.swiper-container', {
            direction : 'horizontal',
            speed:300,
            width : 80
        });
        return aswiper;
    }
    else {
        return false;
    }

});