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


let ListTpl=$('#tplTable').html();
let ListRender = _.template(ListTpl);

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
    if(/heroPre/.test(event.target.id)) {
        currDate=GetDateStr(dateSpilt,-1); //前一天
    }
    else if(/heroNext/.test(event.target.id)){
        currDate=GetDateStr(dateSpilt,1); //后一天
    }
    if(currDate.replace(/-/gi,'')>=20170731) {
        $heroNext.hide();
    }
    else {
        $heroNext.show();
    }
    if(currDate.replace(/-/gi,'')<=20170701) {
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

    console.log('kklll');
    // commonFun.useAjax({
    //     type:'GET',
    //     url: '/activity/hero-ranking/invest/' + date
    // },function(data) {
    //     if(data.status) {
    //         var $nodataInvest=$('.nodata-invest'),
    //             $contentRanking=$('#investRanking-tbody').parents('table');
    //
    //         if(_.isNull(data.records) || data.records.length==0) {
    //             $nodataInvest.show();
    //             $contentRanking.hide();
    //             return;
    //         }
    //         $contentRanking.show();
    //         $nodataInvest.hide();
    //         data.type='invest';
    //         $('#investRanking-tbody').html(ListRender(data));
    //     }
    // });

}

