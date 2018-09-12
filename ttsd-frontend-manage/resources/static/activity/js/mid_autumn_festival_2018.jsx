require("activityStyle/mid_autumn_festival_2018.scss");

let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let sourceKind = globalFun.parseURL(location.href);

let imgUrl = require('../images/2018/mid-autumn-festival/phone.png');
$('.phone').find('img').attr('src',imgUrl)
let $contentList = $('#contentList'),
    $changeBtn = $('.change-btn'),
    $currentDate = $('#currentDate'),
    $heroPre = $('#rankingPre'),
    $heroNext = $('#rankingNext');
let $date = $('#dateTime');
let $activityStatus = $('#activity_status')

let nowDate = getNowDate();
$currentDate.val(nowDate);
$date.text(nowDate.substr(0,10))//当日日期
heroRank(nowDate);
showBtns();


function cutDownTime() {
    let nowDate = getNowDate();
    let nowDateTime = new Date(nowDate.replace(/-/g, "/")).getTime();
    let todayOverTime = new Date(nowDate.substr(0,10)+' 22:00:00'.replace(/-/g, "/")).getTime();
    let distance = todayOverTime-nowDateTime;
    var second = Math.floor(distance / 1000);//未来时间距离现在的秒数
    var day = Math.floor(second / 86400);//整数部分代表的是天；一天有24*60*60=86400秒 ；
    second = second % 86400;//余数代表剩下的秒数；
    var hour = Math.floor(second / 3600);//整数部分代表小时；
    second %= 3600; //余数代表 剩下的秒数；
    var minute = Math.floor(second / 60);
    second %= 60;
    $('#hourDOM').text(hour<10?'0'+hour:hour);
    $('#minutesDOM').text(minute<10?'0'+minute:minute);
    $('#secondDOM').text(second<10?'0'+second:second);

}
setInterval(cutDownTime, 1000);


//不在活动时间范围内的提示
// if(commonFun.activityStatus($activityStatus) == 'activity-noStarted'){
//     $('.no-record').text('不在活动时间范围内！');
// }else {
//     $('.no-record').text('目前还没有人获得现金奖励，快去投资吧！');
// }

$changeBtn.on('click', function (event) {
    let currDate = getNowDate();
     var dateSpilt = $currentDate.val();
    if (/rankingPre/.test(event.target.id)) {
        currDate = GetDateStr(dateSpilt, -1); //前一天
    }
    else if (/rankingNext/.test(event.target.id)) {
        currDate = GetDateStr(dateSpilt, 1); //后一天
    }
    $currentDate.val(currDate);
    $date.text(currDate.substr(0,10))


    showBtns();
   if(activityStatus($activityStatus).status == 'activiting'){

        heroRank($currentDate.val());

    }

});


function getNowDate() {
    let dd = new Date();
    return getHMS(dd);
}

//获取前一天或者后一天的日期
 function GetDateStr(date,AddDayCount) {
    var dd = new Date(date);
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    return getHMS(dd)
}
function getHMS(dd){
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();
    var h = dd.getHours();
    var minites = dd.getMinutes();
    var s = dd.getSeconds();
    return y + "-" + (m < 10 ? ('0' + m) : m) + "-" + (d < 10 ? ('0' + d) : d) + ' ' + (h < 10 ? ('0' + h) : h) + ':' + (minites < 10 ? ('0' + minites) : minites) + ':' + (s < 10 ? ('0' + s) : s);
}

function heroRank(date) {
    commonFun.useAjax({
        type: 'GET',
        url: '/activity/middleautum-nationalday/records?tradingTime=' + date
    }, function (data) {

        if (data.status) {
            //获取模版内容
            let ListTpl = $('#tplTable').html();
            // 解析模板, 返回解析后的内容
            let render = _.template(ListTpl);
            let html = render(data);
            $contentList.html(html);
        }
    })
}

$('#loginTipBtn').on('click',function () {
    loginTip();
});
$('#loginTipBtnInvest').on('click',function () {
    loginTip();
});
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

function showBtns() {
    let activityStatusStr = activityStatus();
    if(activityStatusStr.status == 'noStart'){
        //活动未开始
        if(activityStatusStr.isToday == true){
            $heroPre.hide()
            $heroNext.hide()

        }else if(!activityStatusStr.isToday == true){
            $heroPre.css({'visibility':'hidden'});
        }

        // $contentRanking.html(`<tr> <td colspan="4" class="noData">不在活动时间范围内</td> </tr>`);
        // addStaticImg();

    }else if (activityStatusStr.status == 'end'){
        //活动已结束
        $heroNext.css({'visibility':'hidden'});
        $heroPre.css({'visibility':'visible'});
        // $contentRanking.html(`<tr> <td colspan="4" class="noData">不在活动时间范围内</td> </tr>`);
        //礼物图片静态
        // addStaticImg();

    }else if(activityStatusStr.status == 'activiting'){
        // addStaticImg();
        $heroNext.css({'visibility':'visible'});
        $heroPre.css({'visibility':'visible'});
console.log(activityStatusStr)
        if(activityStatusStr.isToday){

            $heroNext.css({'visibility':'hidden'});
        }

        if(activityStatusStr.isFirstDay == true&&activityStatusStr.isToday == true){
            //活动第一天
            $heroPre.hide();
            $heroNext.hide()

        }
        if(activityStatusStr.isFirstDay == true&&activityStatusStr.isToday == false){
            $heroPre.css({'visibility':'hidden'});
        }

    }

}

function activityStatus() {
    let start = $activityStatus.data('starttime');
    let over = $activityStatus.data('overtime');
    let nowDayStr =$date.text(),//当前天的日期
        todayDayStr = nowDate.substr(0,10),
        startTime = new Date(start.replace(/-/g, "/")).getTime(),
        endTime = new Date(over.replace(/-/g, "/")).getTime();

    let isToday = nowDayStr==todayDayStr;
    let currentTime = new Date().getTime();

    if (currentTime < startTime) {
        //活动未开始
        return {
            status:'noStart',
            isToday:isToday
        }
    }else if(currentTime > endTime){
        //活动结束
        return {
            status:'end',
            isToday:isToday
        }
    }else {
        let isFirstDay = false;
        let isLastDay = false;
        //活动中
        if($date.text().substr(0,10)==start.substr(0,10)){
            //活动第一天
            isFirstDay = true;

        }else if($date.text().substr(0,10)==over.substr(0,10)){
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

function loadData(nowDay) {
    let activityStatusStr = activityStatus();
    if(activityStatusStr.status == 'activity-noStarted'){
        //活动未开始
        if(activityStatusStr.isToday == true){
            $heroPre.hide()
            $heroNext.hide()
            // $toInvestBtn.css('marginTop','0')
        }else if(!activityStatusStr.isToday == true){
            $heroPre.css({'visibility':'hidden'});
        }

        // $contentRanking.html(`<tr> <td colspan="4" class="noData">不在活动时间范围内</td> </tr>`);
        // addStaticImg();

    }else if (activityStatusStr.status == 'activity-end'){
        //活动已结束
        $heroNext.css({'visibility':'hidden'});
        $heroPre.css({'visibility':'visible'});
        // $contentRanking.html(`<tr> <td colspan="4" class="noData">不在活动时间范围内</td> </tr>`);
        //礼物图片静态
        // addStaticImg();

    }else if(activityStatusStr.status == 'activity-ing'){
        // addStaticImg();
        $heroNext.css({'visibility':'visible'});
        $heroPre.css({'visibility':'visible'});

        if(activityStatusStr.isToday){
            $heroNext.css({'visibility':'hidden'});
        }

        if(activityStatusStr.isFirstDay == true&&activityStatusStr.isToday == true){
            //活动第一天
            $heroPre.hide();
            $heroNext.hide()

        }
        if(activityStatusStr.isFirstDay == true&&activityStatusStr.isToday == false){
            $heroPre.css({'visibility':'hidden'});
        }



    }

}