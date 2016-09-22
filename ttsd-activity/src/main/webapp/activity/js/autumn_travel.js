require(['jquery', 'underscore', 'layerWrapper','drawCircle','template','commonFun', 'register_common'], function ($, _, layer,drawCircle,tpl) {

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

    //以下为抽奖转盘
    var $pointer = $('#pointer');
    var $MobileNumber=$('#MobileNumber'),
        travelAllList='/activity/autumn/travel-all-list',  //中奖记录接口地址
        travelUserList='/activity/autumn/travel-user-list',   //我的奖品接口地址
        drawURL='/activity/autumn/travel-draw',    //抽奖的接口链接
        myMobileNumber=$MobileNumber ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

    var drawCircle=new drawCircle(travelAllList,travelUserList,drawURL,myMobileNumber);

    //渲染中奖记录
    drawCircle.GiftRecord();

    //渲染我的奖品
    drawCircle.MyGift(travelUserList,myMobileNumber);

    //开始抽奖
    $pointer.on('click', function(event) {
        drawCircle.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                switch (data.prize) {
                    case 'PORCELAIN_CUP':
                        drawCircle.rotateFn(337, '青花瓷杯子',data.prizeType);
                        break;
                    case 'INTEREST_COUPON_2':
                        drawCircle.rotateFn(56, '0.2%加息券',data.prizeType);
                        break;
                    case 'LUXURY':
                        drawCircle.rotateFn(116, '奢侈品大奖',data.prizeType);
                        break;
                    case 'RED_ENVELOPE_100':
                        drawCircle.rotateFn(160, '100元现金红包',data.prizeType);
                        break;
                    case 'INTEREST_COUPON_5':
                        drawCircle.rotateFn(230, '0.5%加息券',data.prizeType);
                        break;
                    case 'RED_ENVELOPE_50':
                        drawCircle.rotateFn(300, '50元现金红包',data.prizeType);
                        break;
                }
            } else if (data.returnCode == 2) {
                $('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'nologin'})).show().find('.tip-dom').show();
            } else if(data.returnCode == 3){
                $('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'timeout'})).show().find('.tip-dom').show();
            } else {
                $('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'notimes'})).show().find('.tip-dom').show();
            }
        });
    });

});