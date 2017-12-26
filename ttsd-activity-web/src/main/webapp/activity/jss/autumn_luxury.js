/**
 * [name]:奢侈品专题页
 * [user]:xuqiang
 * [date]:2016-08-24
 */
require(['jquery', 'layerWrapper', 'template', 'drawCircle', 'jquery.ajax.extension','register_common'], function ($, layer, tpl,drawCircle) {
        if ($(window).width() < 700) {
            $('.product-img').each(function (index, el) {
                $(this).find('img').height($(this).siblings('.product-info').height());
            });
        }
        var browser = globalFun.browserRedirect();
        if (browser == 'mobile') {

            var urlObj=globalFun.parseURL(location.href);
            if(urlObj.params.tag=='yes') {
                $('.reg-tag-current').show();
            }
        }
        var scrollTimer;
        $("#rewardList").hover(function () {
            clearInterval(scrollTimer);
        }, function () {
            scrollTimer = setInterval(function () {
                scrollNews($("#rewardList"));
            }, 1000);
        }).trigger("mouseout");

        function scrollNews(obj) {
            var $self = obj.find(".user-list");
            var lineHeight = $self.find("tr:first").height();
            if ($self.find('tr').length > 5) {
                $self.animate({
                    "margin-top": -lineHeight + "px"
                }, 600, function () {
                    $self.css({
                        "margin-top": "0px"
                    }).find("tr:first").appendTo($self);
                })
            }
        }

        $('.record-list h3 span').on('click', function (event) {
            var $self = $(this),
                $item = $self.parent('h3').siblings('.record-item'),
                index = $self.index();
            $self.addClass('active').siblings('span').removeClass('active');
            $item.find('.record-data:eq(' + index + ')').addClass('active')
                .siblings('.record-data').removeClass('active');
        });

        var $loginName = $('div.login-name');
        var loginName = $loginName ? $loginName.data('login-name') : '';

        $("a.autumn-luxury-invest-channel").click(function () {
            $.ajax({
                url: '/activity/autumn/luxury/invest?loginName=' + loginName,
                type: 'POST'
            });
        });

    //以下为抽奖转盘
    var $pointer = $('.pointer-img');
    var $MobileNumber=$('#MobileNumber'),
        travelAllList='/activity/autumn/luxury-all-list',  //中奖记录接口地址
        travelUserList='/activity/autumn/luxury-user-list',   //我的奖品接口地址
        drawURL='/activity/autumn/luxury-draw',    //抽奖的接口链接
        myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';

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
                        drawCircle.rotateFn(160, '100元投资红包',data.prizeType);
                        break;
                    case 'INTEREST_COUPON_5':
                        drawCircle.rotateFn(230, '0.5%加息券',data.prizeType);
                        break;
                    case 'RED_ENVELOPE_50':
                        drawCircle.rotateFn(300, '50元投资红包',data.prizeType);
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