require("activityStyle/hero_ranking_2017.scss");
let commonFun = require('publicJs/commonFun');
require('publicJs/login_tip');

let $activityPageFrame = $('#activityPageFrame');
let $investRankingButton=$('#investRanking-button'),
    $heroNext=$('#heroNext'),
    $heroPre=$('#heroPre');

let $sortBox = $('#sortBox'),
    $date = $('.date',$sortBox),
    $totalAmount = $('.total',$sortBox),
    $rankingOrder =$('.ranking-order',$sortBox);

let todayDay = $.trim($date.text());
let startTime = 20170601,
    endTime = 20170731,
    todayDayStr=Number(todayDay.replace(/-/gi,''));

let $nodataInvest=$('.nodata-invest'),
    $tableReward = $('table.table-reward');


(function() {
    let $bannerSlide = $('#bannerSlide');
    let redirect = globalFun.browserRedirect();
    if(redirect=='mobile') {
        let topSrc = require('../images/2017/hero-ranking/top-ranking-wap.png');
        let topImage = new Image();
        topImage.src = topSrc;
        topImage.onload=function() {
            $bannerSlide.append(topImage);
        };
    }
})();
//弹框登录
(function() {
    let $isLogin=$('.show-login',$activityPageFrame);
    $isLogin.on('click', function(event) {
        event.preventDefault();
        $.when(commonFun.isUserLogin())
            .fail(function() {
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
//获取前一天或者后一天的日期
function GetDateStr(date,AddDayCount) {
    var dd = new Date(date);
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();

    return y + "-" + (m < 10 ? ('0' + m) : m) + "-" + (d < 10 ? ('0' + d) : d);
}

$investRankingButton.find('.button-small').on('click',function(event) {
    var dateSpilt=$.trim($date.text()),
        currDate;
    endTime = (todayDayStr<endTime)?todayDayStr:endTime;

    if(/heroPre/.test(event.target.id)) {
        currDate=GetDateStr(dateSpilt,-1); //前一天
    }
    else if(/heroNext/.test(event.target.id)){
        currDate=GetDateStr(dateSpilt,1); //后一天
    }
    // $nodataInvest.hide();
    if(currDate.replace(/-/gi,'')>=endTime) {
        $heroNext.hide();
    }
    else {
        $heroNext.show();
    }
    if(currDate.replace(/-/gi,'')<=startTime) {
        $heroPre.hide();
    }
    else {
        $heroPre.show();
    }
    $date.text(currDate);
    heroRank(currDate);

});

//英雄榜排名,今日投资排行
function heroRank(date) {
    commonFun.useAjax({
        type:'GET',
        url: '/activity/hero-ranking/invest/' + date
    },function(data) {
        if(data.status) {
           var $contentRanking=$('#investRanking-tbody');
            if(_.isNull(data.records) || data.records.length==0) {
                $contentRanking.hide();
                return;
            }
            $contentRanking.show();
            //获取模版内容
            let ListTpl=$('#tplTable').html();
            // 解析模板, 返回解析后的内容
            let render = _.template(ListTpl);
            let html = render(data);
            $('#investRanking-tbody').html(html);
        }
    });

    commonFun.useAjax({
        type:'GET',
        url: '/activity/hero-ranking/ranking/' + date
    },function(data) {
        //今日投资总额 和 排名
        $totalAmount.text(data.investAmount/100);
        $rankingOrder.text(data.investRanking);
    })
}

if(todayDayStr<startTime) {
    //活动未开始
    $heroPre.hide();
} else if(todayDayStr>endTime) {
    //活动已经结束
    $nodataInvest.show().html('活动已经结束');
    $tableReward.hide();

} else {
    heroRank(todayDay);
}

$('#toInvest').on('click',function() {

    window.location.href='/loan-list';
});





