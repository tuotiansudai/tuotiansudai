require(['jquery'], function ($) {

    var redirect = globalFun.browserRedirect();
    var $christmasDayFrame=$('#christmasDayFrame');
    var $activitySlide = $christmasDayFrame.prev();

    //为节约手机流量，把pc页面的图片在pc页上显示才增加
    (function() {
        if(redirect=='pc') {
            var HTML='<div class="body-decorate" id="bodyDecorate"> ' +
                '<div class="bg-left"></div> ' +
                '<div class="bg-right"></div> ' +
                '<div class="bg-bottom"></div> ' +
                '</div>';
            $christmasDayFrame.prepend(HTML);
            $activitySlide.addClass('pc-img');
        }
        else if(redirect=='mobile'){
            $activitySlide.html('<img src='+staticServer+'/activity/images/christmas-day/bg-title.png>');
        }
    })();

    (function() {

       


    })();


});