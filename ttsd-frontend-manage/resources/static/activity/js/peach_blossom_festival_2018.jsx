require("activityStyle/peach_blossom_festival_2018.scss");
let commonFun = require('publicJs/commonFun');




let $activityPageFrame = $('#activityPageFrame');
//投资按钮，前一天后一天按钮
let $investRankingButton = $('#investRanking-button'),
    $heroNext = $('#rankingNext'),
    $heroPre = $('#rankingPre'),
    sourceKind = globalFun.parseURL(location.href),
    $contentRanking = $('#investRanking-tbody');
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
console.log(startTime,endTime,todayDay)

console.log(activityStatus(startTime,endTime,todayDay,todayDay))

function loadData(nowDay) {
    if(activityStatus(startTime,endTime,todayDay,nowDay).status == 'noStart'){
        //活动未开始
        $heroPre.css({'visibility':'hidden'});
        $heroNext.css({'visibility':'hidden'});
        $contentRanking.html(`<div class="noData">不在活动时间范围内</div>`);
        addStaticImg();
    }else if (activityStatus(startTime,endTime,todayDay,nowDay).status == 'end'){
        $heroNext.css({'visibility':'hidden'});
        $heroPre.css({'visibility':'visible'});
        $contentRanking.html(`<tr> <td colspan="4" class="noData">不在活动时间范围内</td> </tr>`);
        //礼物图片静态
        addStaticImg();
    }else if(activityStatus(startTime,endTime,todayDay,nowDay).status == 'activiting'){
        alert(9)
    }
    
}

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
    // activityStatus(currDate);

});
loadData(todayDay);
// if (nowDayStr < startTime) {
//     //活动未开始
//     $heroPre.css({'visibility':'hidden'});
//     $heroNext.css({'visibility':'hidden'});
//
//     $contentRanking.html(`<div class="noData">不在活动时间范围内</div>`);
//     //礼物图片静态
//     let oLotteryUrl = require('../images/2017/mid-national/gift.png');
//     let $gift = $activityPageFrame.find('.big-lottery-con');
//     $gift.append(`<img src="${oLotteryUrl}">`);
//
//
// }
// else if (nowDayStr > endTime) {
//     //活动已经结束
//
//     $heroNext.css({'visibility':'hidden'});
//     $heroPre.css({'visibility':'visible'});
//     $contentRanking.html(`<div class="noData">不在活动时间范围内</div>`);
//     //礼物图片静态
//     let oLotteryUrl = require('../images/2017/mid-national/gift.png');
//     let $gift = $activityPageFrame.find('.big-lottery-con');
//     $gift.html('');
//     $gift.append(`<img src="${oLotteryUrl}">`);
//     $bigLottery.hide();
//
// }  else if(nowDayStr>=startTime && nowDayStr<=endTime){
//     //活动中
//
//     $heroNext.css({'visibility':'visible'});
//     $heroPre.css({'visibility':'visible'});
//     if(isToday){
//         $heroNext.css({'visibility':'hidden'});
//     }
//     $contentRanking.show();
//
//     if(nowDayStr==startTime) {
//         //活动第一天
//         $heroPre.css({'visibility':'hidden'});
//     } else if(nowDayStr==endTime) {
//         //活动最后一天
//         $heroNext.css({'visibility':'hidden'});
//     }
//     heroRank(nowDay);
// }
//
// $('.is-today',$activityPageFrame).text(function() {
//     return isToday ? '今日' : '当日'
// });
//

function addStaticImg() {
    let prizeUrl = require('../images/2018/peach-blossom-festival/gift.png');
    let  prizeImg = $(`<img src="${prizeUrl}" width="103"/>`);
//默认静态图片
    let $bigPrize = $('#bigPrize');
    $bigPrize.html(prizeImg);
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
        //活动中
        return {
            status:'activiting',
            isToday:isToday
        }
    }

}
