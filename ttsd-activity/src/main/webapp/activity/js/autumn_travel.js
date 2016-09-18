require(['jquery', 'underscore', 'layerWrapper', 'commonFun', 'circle','register_common'], function ($, _, layer) {

var $autumnTravelPage=$('#autumnTravelPage'),
    $awardList = $('.award-list',$autumnTravelPage);
    var scrollTimer, scrollTimer2;
    var $swiperWrapper = $('.swiper-wrapper',$autumnTravelPage),
        $swiperslide = $('.swiper-slide', $swiperWrapper);
    var browser = commonFun.browserRedirect();
    if (browser == 'mobile') {
        var urlObj=commonFun.parseURL(location.href);
        if(urlObj.params.tag=='yes') {
            $autumnTravelPage.find('.reg-tag-current').show();
        }
    }

    function scrollAwardRecords(obj) {
        var lineHeight = obj.find("tr:first").height();
        obj.animate({
            "margin-top": -lineHeight + "px"
        }, 600, function () {
            obj.css({
                "margin-top": "0px"
            }).find("tr:first").appendTo(obj);
        })
    }

    if ($awardList.find('dd').length > 10) {
        $awardList.hover(function () {
            clearInterval(scrollTimer2);
        }, function () {
            scrollTimer2 = setInterval(function () {
                var lineHeight = $awardList.find("dd:first").height();
                $awardList.animate({
                    "margin-top": -lineHeight + "px"
                }, 600, function () {
                    $awardList.css({
                        "margin-top": "0px"
                    }).find("dd:first").appendTo($awardList);
                })
            }, 2000);
        }).trigger("mouseout");
    }


    var $loginName = $('div.login-name');
    var loginName = $loginName ? $loginName.data('login-name') : '';

    $("a.autumn-travel-invest-channel").click(function () {
        $.ajax({
            url: '/activity/autumn/travel/invest?loginName=' + loginName,
            type: 'POST'
        });
    });
});