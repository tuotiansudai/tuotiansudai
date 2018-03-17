require("activityStyle/peach_blossom_festival_2018.scss");
let commonFun = require('publicJs/commonFun');




let $activityPageFrame = $('#activityPageFrame');
//投资按钮，前一天后一天按钮
let $investRankingButton = $('#investRanking-button'),
    $heroNext = $('#rankingNext'),
    $heroPre = $('#rankingPre'),
    sourceKind = globalFun.parseURL(location.href),
    $contentRanking = $('#investRanking-tbody'),
    $toInvestBtn = $('#toInvest');
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

});

function loadData(nowDay) {
    let activityStatusStr = activityStatus(startTime,endTime,todayDay,nowDay);
    if(activityStatusStr.status == 'noStart'){
        //活动未开始
        $heroPre.css({'visibility':'hidden'});
        $heroNext.css({'visibility':'hidden'});
        $contentRanking.html(`<div class="noData">不在活动时间范围内</div>`);
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
            $toInvestBtn.css('marginTOp','0')

        }else if(activityStatusStr.isLastDay == true){
            //活动最后一天
            $heroNext.css({'visibility':'hidden'});

        }
        $('#isToday').text(function () {
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
    $bigPrize.html(prizeImg);
    $wapBigPrize.html(wapBigPrize);
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
