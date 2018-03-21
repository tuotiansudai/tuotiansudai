require("activityStyle/peach_blossom_festival_2018.scss");
require('publicJs/login_tip');
let commonFun = require('publicJs/commonFun');

let $activityPageFrame = $('#activityPageFrame');
//投资按钮，前一天后一天按钮
let $investRankingButton = $('#investRanking-button'),
    $heroNext = $('#rankingNext'),
    $heroPre = $('#rankingPre'),
    sourceKind = globalFun.parseURL(location.href),
    $contentRanking = $('#investRanking-tbody'),
    $toInvestBtn = $('#toInvest'),
    $rankingTable = $('#rankingTable'),
    $rankingList = $('#rankingList');

let $myRank = $('#myRank'),
    $totalAmount = $('#totalAmount'),
    $lookMore = $('#lookMore'),
    $lookLess = $('#lookLess'),
    $lookBtn = $('.look-btn');
/*
activityStatus 参数说明
startTime 活动开始时间,字符串
endTime 活动结束时间
nowDay 当天的时间
todayDay 显示数据的时间
* */
let $date = $('#dateTime');
let startTime = $date.data('starttime'),//开始时间
    endTime = $date.data('endtime'),//结束时间
    todayDay = $.trim($date.text());//显示的日期

//二维码
let qrCodeUrl = require('../images/2018/peach-blossom-festival/qr_code.png');
let  qrCodeImg = $(`<img src="${qrCodeUrl}"/>`);
$('#qrCode').append(qrCodeImg);
console.log(startTime,endTime,todayDay);

console.log(activityStatus(startTime,endTime,todayDay,todayDay))
loadData(todayDay);
$investRankingButton.find('.look-btn').on('click', function (event) {
    var dateSpilt = $.trim($date.text()),
        currDate;
    if (/rankingPre/.test(event.target.id)) {
        currDate = commonFun.GetDateStr(dateSpilt, -1); //前一天
    }
    else if (/rankingNext/.test(event.target.id)) {
        currDate = commonFun.GetDateStr(dateSpilt, 1); //后一天
    }
    $date.text(currDate);
    loadData($date.text())
    heroRank($date.text())

});
$('#loginTipBtn').on('click',function () {
    loginTip();
});
$('#loginTipBtnInvest').on('click',function () {
    loginTip();
});
heroRank($('#dateTime').text());


let bigBgUrl = require('../images/2018/peach-blossom-festival/big_part_three_bg.png');
let smallBgUrl = require('../images/2018/peach-blossom-festival/part_three_bg.png');

let smallBgUrlWap = require('../images/2018/peach-blossom-festival/wap_part_three_bg.png');
let bigBgUrlWap = require('../images/2018/peach-blossom-festival/wap_big_part_three.png');
function showMoreData(num) {
     $lookLess.hide();
    if (num == 0) {
        $lookMore.hide();
    }
    let $seenArea = $('#rankingList').find('thead').height();
    let $height = $contentRanking.find('tr').height();
    if (num < 6) {
        $rankingList.css('height',$seenArea + num * $height);
        $lookMore.hide();
    } else {
        $rankingList.css('height',$seenArea + 5 * $height);
        $lookMore.show();
    }

    let wrapTop = (num - 5)*$height+$('#rankBg')[0].offsetTop;
    $rankingList.css('overflow','hidden');
    $lookMore.on('click',() => {
        $rankingList.css('height',$seenArea + num * $height);
        $rankingList.css('overflow','visible');
        if($(document).width()>1024){
            $('.part-three-bg').css({
                'backgroundImage':'url('+bigBgUrl+')'
            })
            $('#rankBg').css({
                'top':wrapTop
            })
        }else {
            $('.part-three-bg').css({
                'backgroundImage':'url('+bigBgUrlWap+')'
            })
            $('#rankBg').css({
                'top':wrapTop
            })
        }

        $lookMore.hide();
        $lookLess.show();
    });
    $lookLess.on('click', () => {
        $rankingList.css('height',$seenArea + 5 * $height);
        $rankingList.css('overflow','hidden');
        if($(document).width()>1024){
            $('.part-three-bg').css({
                'backgroundImage':'url('+smallBgUrl+')'
            });
        }else {
            $('.part-three-bg').css({
                'backgroundImage':'url('+smallBgUrlWap+')'
            });
        }

        $lookLess.hide();
        $lookMore.show();
    })
}
// 排行榜数据列表
function heroRank(date) {
    commonFun.useAjax({
        type: 'GET',
        url: '/activity/spring-breeze/ranking/' + date
    }, function (data) {
        console.log(data)

        if (data.status) {
            if (_.isNull(data.records) || data.records.length == 0) {

                $contentRanking.html(`<tr> <td colspan="4" class="noData">暂时没有记录哦~</td> </tr>`);
                $lookLess.hide();
                    $lookMore.hide();
                return;
            }
            //获取模版内容
            let ListTpl = $('#tplTable').html();
            // 解析模板, 返回解析后的内容
            let render = _.template(ListTpl);
            let html = render(data);
            $contentRanking.html(html);
            showMoreData(data.records.length)
        }
    });

    commonFun.useAjax({
        type: 'GET',
        url: '/activity/spring-breeze/my-ranking/' + date
    }, function (data) {
        console.log(data)
        //今日投资总额 和 排名
        let investRanking = data.investRanking;
        $totalAmount.text(data.investAmount / 100+'元');
        if(investRanking==0) {
            $myRank.text('未上榜');
        } else {
            $myRank.text(data.investRanking);
        }

    })
}

//登录按钮
function loginTip() {
        if (sourceKind.params.source == 'app') {
            location.href = "/login";
        }else {
            $.when(commonFun.isUserLogin())
                .fail(function () {
                    //判断是否需要弹框登陆
                    layer.open({
                        type: 1,
                        title: false,
                        closeBtn: 0,
                        area: ['auto', 'auto'],
                        content: $('#loginTip')
                    });
                });
        }

}
function loadData(nowDay) {
    let activityStatusStr = activityStatus(startTime,endTime,todayDay,nowDay);
    if(activityStatusStr.status == 'noStart'){
        //活动未开始
        $heroPre.hide()
        $heroNext.hide()
        $toInvestBtn.css('marginTop','0')
        $contentRanking.html(`<tr> <td colspan="4" class="noData">不在活动时间范围内</td> </tr>`);
        addStaticImg();
    }else if (activityStatusStr.status == 'end'){
        //活动已结束
        $heroNext.css({'visibility':'hidden'});
        $heroPre.css({'visibility':'visible'});
        $contentRanking.html(`<tr> <td colspan="4" class="noData">不在活动时间范围内</td> </tr>`);
        //礼物图片静态
        addStaticImg();
    }else if(activityStatusStr.status == 'activiting'){
        addStaticImg();
        $heroNext.css({'visibility':'visible'});
        $heroPre.css({'visibility':'visible'});

        if(activityStatusStr.isToday){
            $heroNext.css({'visibility':'hidden'});
        }

        if(activityStatusStr.isFirstDay == true){
            //活动第一天
            $heroPre.hide()
            $heroNext.hide()
            $toInvestBtn.css('marginTop','0')

        }else if(activityStatusStr.isLastDay == true){
            //活动最后一天
            $heroNext.css({'visibility':'hidden'});

        }
        $('.isToday').text(function () {
            return activityStatusStr.isToday ? '今日' : '当日'
        })


    }

}



function addStaticImg() {
    let prizeUrl = require('../images/2018/peach-blossom-festival/gift.png');
    let  prizeImg = $(`<img src="${prizeUrl}"/>`);
//默认静态图片
    let $bigPrize = $('#bigPrize');
    let wapBigPrizeUrl = require('../images/2018/peach-blossom-festival/gift.png');
    let  wapBigPrize = $(`<img src="${wapBigPrizeUrl}"/>`);
    let $wapBigPrize = $('#wapBigPrize');
    if($('.heroes-list').data('awardsrc')){
        $bigPrize.html(`<img src=${$('.heroes-list').data('awardsrc')}/>`)
        $wapBigPrize.html(`<img src=${$('.heroes-list').data('awardsrc')}/>`)
    }else {
        $bigPrize.html(prizeImg);
        $wapBigPrize.html(wapBigPrize);
    }

}

function activityStatus(startTime,endTime,todayDay,nowDay) {
    let nowDayStr = Number(nowDay.replace(/-/gi, '')),//当前天的日期
        todayDayStr = Number(todayDay.replace(/-/gi, ''));
        startTime = Number(startTime.substring(0, 10).replace(/-/gi, ''));
        endTime = Number(endTime.substring(0, 10).replace(/-/gi, ''));
    let isToday = nowDayStr==todayDayStr;

    if (nowDayStr < startTime) {
        //活动未开始
        return {
            status:'noStart',
            isToday:isToday
        }
    }else if(nowDayStr > endTime){
        //活动结束
        return {
            status:'end',
            isToday:isToday
        }
    }else {
        let isFirstDay = false;
        let isLastDay = false;
        //活动中
        if(nowDayStr==startTime){
            //活动第一天
            isFirstDay = true;

        }else if(nowDayStr==endTime){
            //活动最后一天
            isLastDay = true;

        }

        return {
            status:'activiting',
            isToday:isToday,
            isFirstDay:isFirstDay,
            isLastDay:isLastDay
        }
    }

}
