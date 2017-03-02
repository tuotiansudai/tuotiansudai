require('publicJs/login_tip');
let drawCircle = require('activityJsModule/gift_circle_draw');
require('activityStyle/lantern_festival.scss');
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


// 是否加载快速注册的功能
let urlObj = globalFun.parseURL(location.href);
if (urlObj.params.tag == 'yes') {
    $lanternFrame.find('.reg-tag-current').show();
}
//未登录
if (sourceKind.params.source == 'app') {
    $.when(commonFun.isUserLogin())
        .done(function () {

        })
        .fail(function () {
            $('.app-list-text', $lanternFrame).html('当前排名：<a href="/login">登录查看</a>');
            $('.app-loan-text', $lanternFrame).text('今日投资额：0.00元');
        });
}
$lanternFrame.find('.tip-list-frame .tip-list').each(function (key, option) {
    let kind = $(option).data('return');
    tipGroupObj[kind] = option;
});


if (todayDate.replace(/-/gi, '') == parseInt($('#activityStartTime').val().substr(0, 10).replace(/-/gi, ''))) {
    $heroPre.hide();
    $heroNext.hide();
} else if (todayDate.replace(/-/gi, '') == parseInt($('#activityEndTime').val().substr(0, 10).replace(/-/gi, ''))) {
    $heroNext.hide();
} else if (todayDate.replace(/-/gi, '') > parseInt($('#activityStartTime').val().substr(0, 10).replace(/-/gi, '')) && todayDate.replace(/-/gi, '') < parseInt($('#activityEndTime').val().substr(0, 10).replace(/-/gi, ''))) {
    $heroPre.show();
    $heroNext.hide();
} else {
    $heroPre.show();
    $heroNext.hide();
}
//获取前一天或者后一天的日期
function GetDateStr(date, AddDayCount) {
    let dd = new Date(date);
    dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
    let y = dd.getFullYear();
    let m = dd.getMonth() + 1;//获取当前月份的日期
    let d = dd.getDate();

    return y + "-" + (m < 10 ? ('0' + m) : m) + "-" + (d < 10 ? ('0' + d) : d);
}
$investRankingButton.find('span').on('click', function (event) {
    let $HistoryAwards = $('#HistoryAwards');
    let dateSpilt = $HistoryAwards.text(),
        currDate;
    if (/heroPre/.test(event.target.id)) {
        currDate = GetDateStr(dateSpilt, -1); //前一天
    }
    else if (/heroNext/.test(event.target.id)) {
        currDate = GetDateStr(dateSpilt, 1); //后一天
    }


    if (currDate.replace(/-/gi, '') == parseInt($('#activityStartTime').val().substr(0, 10).replace(/-/gi, ''))) {
        $heroPre.hide();
        $heroNext.show();
    } else if (currDate.replace(/-/gi, '') == parseInt($('#TodayAwards').val().replace(/-/gi, '')) && currDate.replace(/-/gi, '') > parseInt($('#activityStartTime').val().substr(0, 10).replace(/-/gi, ''))) {
        $heroPre.show();
        $heroNext.hide();
    } else if (currDate.replace(/-/gi, '') == parseInt($('#activityEndTime').val().substr(0, 10).replace(/-/gi, '')) && currDate.replace(/-/gi, '') == parseInt($('#TodayAwards').val().replace(/-/gi, ''))) {
        $heroPre.show();
        $heroNext.hide();
    } else if (currDate.replace(/-/gi, '') > parseInt($('#activityStartTime').val().substr(0, 10).replace(/-/gi, '')) && currDate.replace(/-/gi, '') < parseInt($('#TodayAwards').val().replace(/-/gi, ''))) {
        $heroPre.show();
        $heroNext.show();
    }
    $HistoryAwards.text(currDate);
    heroRank(currDate);
});
//投资排行
function heroRank(date) {
    commonFun.useAjax({
        url: '/activity/lantern-festival/invest/' + date,
        type: 'GET'
    }, function (data) {
        if (data.status) {
            let $contentRanking = $('#investRanking-tbody').parents('table');
            $('#investRanking-tbody').html(tpl('tplTable', data));
        }
    });
}
heroRank($TodayAwards.val());

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