require(['jquery', 'underscore','layerWrapper','commonFun'], function ($, _, layer) {

    var $awardRecordsFrame=$('#awardRecordsFrame');
    var $slideBody=$('.table tbody',$awardRecordsFrame);
    var $awardList=$('.award-list');
    var scrollTimer,scrollTimer2;
    var browser=commonFun.browserRedirect();

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

    if($awardList.find('dd').length>10) {
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

    $slideBody.hover(function () {
        clearInterval(scrollTimer);
    }, function () {
        scrollTimer = setInterval(function () {
            scrollAwardRecords($slideBody);
        }, 2000);
    }).trigger("mouseout");

});