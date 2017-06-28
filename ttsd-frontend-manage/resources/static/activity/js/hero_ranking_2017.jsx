require("activityStyle/hero_ranking_2017.scss");
require('activityJsModule/fast_register');
let commonFun = require('publicJs/commonFun');
require('publicJs/login_tip');

let $activityPageFrame = $('#activityPageFrame');
let $investRankingButton = $('#investRanking-button'),
    $heroNext = $('#heroNext'),
    $heroPre = $('#heroPre');

let $sortBox = $('#sortBox'),
    $date = $('.date', $sortBox),
    $totalAmount = $('.total', $sortBox),
    $rankingOrder = $('.ranking-order', $sortBox);

let todayDay = $.trim($date.text());
let startTime = Number($date.data('starttime').substring(0, 10).replace(/-/gi, '')),
    endTime = Number($date.data('endtime').substring(0, 10).replace(/-/gi, ''));

let $nodataInvest = $('.nodata-invest'),
    $contentRanking = $('#investRanking-tbody');

function activityStatus(nowDay) {
    let nowDayStr = Number(nowDay.replace(/-/gi, '')),
        todayDayStr = Number(todayDay.replace(/-/gi, '')),
        isToday = nowDayStr==todayDayStr;
    endTime = (todayDayStr < endTime) ? todayDayStr : endTime;
    $nodataInvest.hide();

   if (nowDayStr < startTime) {
        //活动未开始
       $heroPre.css({'visibility':'hidden'});
       $heroNext.css({'visibility':'hidden'});
       $contentRanking.hide();
    }
    else if (nowDayStr > endTime) {
        //活动已经结束
        $heroNext.css({'visibility':'hidden'});
        $heroPre.css({'visibility':'visible'});
        $contentRanking.hide();
        $nodataInvest.show().html('活动已经结束');

    }  else if(nowDayStr>=startTime && nowDayStr<=endTime){
        //活动中
       $heroNext.css({'visibility':'visible'});
       $heroPre.css({'visibility':'visible'});
       $contentRanking.show();
       if(nowDayStr==startTime) {
            //活动第一天
           $heroPre.css({'visibility':'hidden'});
       } else if(nowDayStr==endTime) {
           //活动最后一天
           $heroNext.css({'visibility':'hidden'});
       }
       heroRank(nowDay);
   }

    $('.is-today',$activityPageFrame).text(function() {
        return isToday ? '今日' : '当日'
    });
}

//页面初始
activityStatus(todayDay);

(function () {
    let $bannerSlide = $('#bannerSlide');
    let redirect = globalFun.browserRedirect();
    if (redirect == 'mobile') {
        let topSrc = require('../images/2017/hero-ranking/top-ranking-wap.png');
        let topImage = new Image();
        topImage.src = topSrc;
        topImage.onload = function () {
            $bannerSlide.append(topImage);
        };
    }
})();
//弹框登录
(function () {
    let $isLogin = $('.show-login', $activityPageFrame);
    $isLogin.on('click', function (event) {
        event.preventDefault();
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
    });
})();

$investRankingButton.find('.button-small').on('click', function (event) {
    var dateSpilt = $.trim($date.text()),
        currDate;
    if (/heroPre/.test(event.target.id)) {
        currDate = commonFun.GetDateStr(dateSpilt, -1); //前一天
    }
    else if (/heroNext/.test(event.target.id)) {
        currDate = commonFun.GetDateStr(dateSpilt, 1); //后一天
    }
    $date.text(currDate);
    activityStatus(currDate);

});

//英雄榜排名,今日投资排行
function heroRank(date) {
    commonFun.useAjax({
        type: 'GET',
        url: '/activity/hero-ranking/invest/' + date
    }, function (data) {
        if (data.status) {
            if (_.isNull(data.records) || data.records.length == 0) {
                $('#investRanking-tbody').html('');
                return;
            }
            //获取模版内容
            let ListTpl = $('#tplTable').html();
            // 解析模板, 返回解析后的内容
            let render = _.template(ListTpl);
            let html = render(data);
            $('#investRanking-tbody').html(html);
        }
    });

    commonFun.useAjax({
        type: 'GET',
        url: '/activity/hero-ranking/ranking/' + date
    }, function (data) {
        //今日投资总额 和 排名
        let investRanking = data.investRanking;
        $totalAmount.text(data.investAmount / 100);
        if(investRanking==0) {
            $rankingOrder.text('未上榜');
        } else {
            $rankingOrder.text(data.investRanking);
        }

    })
}

$('#toInvest').on('click',function() {
    window.location.href = '/loan-list';
});

