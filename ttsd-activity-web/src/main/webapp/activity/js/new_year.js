require(['jquery','drawCircle'], function ($,drawCircle) {
    $(function() {

        var redirect = globalFun.browserRedirect();
        var $newYearDayFrame = $('#newYearDayFrame');
        var $activitySlide=$('#newYearSlide');

        //为节约手机流量，把pc页面的图片在pc页上显示才增加
        (function() {
            if(redirect=='pc') {
                $activitySlide.addClass('pc-img');
            }
            else {
                $activitySlide.html('<img src='+staticServer+'/activity/images/christmas-day/app-top.jpg>');

                // 是否加载快速注册的功能
               var urlObj=globalFun.parseURL(location.href);
                if(urlObj.params.tag=='yes') {
                    $newYearDayFrame.find('.reg-tag-current').show();
                 }
            }
        })();




    })
});


