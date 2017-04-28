require('publicJs/login_tip');
let drawCircle = require('activityJsModule/gift_circle_draw');
require('activityStyle/wx_lottery.scss');
let tpl = require('art-template/dist/template');
let commonFun = require('publicJs/commonFun');


let $lanternFrame = $('#lanternFrame'),
    tipGroupObj = {},
    $TodayAwards = $('#TodayAwards'),
    $investRankingButton = $('#investRanking-button'),
    $heroNext = $('#heroNext'),
    $heroPre = $('#heroPre'),
    todayDate = $.trim($TodayAwards.val()),
    sourceKind = globalFun.parseURL(location.href);


$lanternFrame.find('.tip-list-frame .tip-list').each(function (key, option) {
    let kind = $(option).data('return');
    tipGroupObj[kind] = option;
});




(function (drawCircle) {
    //抽奖模块
    let $rewardGiftBox = $('.reward-gift-box', $lanternFrame);

    let $MobileNumber = $('#MobileNumber'),
        pointAllList = '/activity/lantern-festival/all-prize-list',  //中奖记录接口地址
        pointUserList = '/activity/lantern-festival/user-prize-list',   //我的奖品接口地址
        drawURL = '/activity/lantern-festival/prize',    //抽奖的接口链接
        drawTime = '/activity/lantern-festival/drawTime', //抽奖次数
        $pointerImg = $('.pointer-img', $rewardGiftBox),
        myMobileNumber = $MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

    let paramData = {
        "mobile": myMobileNumber,
        "activityCategory": "LANTERN_FESTIVAL_ACTIVITY"
    };

    drawCircle.prototype.showDrawTime = function () {
        commonFun.useAjax({
            url: drawTime,
            type: 'GET'
        }, function (data) {
            $('.draw-time', $rewardGiftBox).text(data);
        });
    };

    var drawCircle = new drawCircle(pointAllList, pointUserList, drawURL, paramData, $rewardGiftBox, 3);

    //渲染中奖记录
    drawCircle.GiftRecord(3);

    //渲染我的奖品
    drawCircle.MyGift(3);

    //渲染我的抽奖次数
    drawCircle.showDrawTime();

    //**********************开始抽奖**********************//
    $pointerImg.on('click', function () {
        //未登录
        if (sourceKind.params.source == 'app') {
            $.when(commonFun.isUserLogin())
                .done(function () {
                    getGift();
                })
                .fail(function () {
                    location.href = "/login";
                });
        } else {
            getGift();
        }
    });

    function getGift() {
        //判断是否正在抽奖
        if ($pointerImg.hasClass('win-result')) {
            return;//不能重复抽奖
        }
        $pointerImg.addClass('win-result');
        //延迟1秒抽奖
        setTimeout(function () {
            drawCircle.beginLuckDraw(function (data) {
                //停止礼品盒的动画
                $pointerImg.removeClass('win-result');
                drawCircle.showDrawTime();

                if (data.returnCode == 0) {
                    var prizeType = data.prizeType.toLowerCase();
                    $(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);
                    drawCircle.noRotateFn(tipGroupObj[prizeType]);

                } else if (data.returnCode == 1) {
                    //没有抽奖机会
                    drawCircle.tipWindowPop(tipGroupObj['nochance']);
                }
                else if (data.returnCode == 2) {
                    //判断是否需要弹框登陆
                    layer.open({
                        type: 1,
                        title: false,
                        closeBtn: 0,
                        area: ['auto', 'auto'],
                        content: $('#loginTip')
                    });  //弹框登录
                } else if (data.returnCode == 3) {
                    //不在活动时间范围内！
                    drawCircle.tipWindowPop(tipGroupObj['expired']);

                } else if (data.returnCode == 4) {
                    //实名认证
                    drawCircle.tipWindowPop(tipGroupObj['authentication']);
                }
            });
        }, 1000);
    }

    //点击切换按钮
    let menuCls = $rewardGiftBox.find('.menu-switch span');
    menuCls.on('click', function () {
        let $this = $(this),
            index = $this.index(),
            contentCls = $rewardGiftBox.find('.record-list ul');
        $this.addClass('active').siblings().removeClass('active');
        contentCls.eq(index).show().siblings('ul').hide();

    });

})(drawCircle);

if ($(window).width() < 700) {
    $('.bg-two .wp', $lanternFrame).append($('.bg-two .rule-model', $lanternFrame));
}