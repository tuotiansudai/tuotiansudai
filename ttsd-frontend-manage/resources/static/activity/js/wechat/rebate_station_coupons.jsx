require('activityStyle/wechat/rebate_station_coupons.scss');
let commonFun = require('publicJs/commonFun');
let sourceKind = globalFun.parseURL(location.href);
commonFun.calculationFun(document,window);


let isGet = $('.container').data('get');
let isSuc = $('.container').data('success');
let startTime = $('.container').data('startTime');
let overTime = $('.container').data('overTime');
let activityTime = new Date(startTime.replace(/-/g, "/")).getTime(); // 活动开始时间
let activityOverTime = new Date(overTime.replace(/-/g, "/")).getTime();  // 活动结束时间

!isGet ?  $('.handle_btn').addClass('get_it_now') :  $('.handle_btn').addClass('invest_btn');

if (isSuc) {
    $('.tip_text').show();
}

$('.get_it_now').on('click',function () {
    let currentTime = new Date().getTime();
    if (currentTime < activityTime) {
        layer.msg('活动未开始');
    }
    else if (currentTime > activityOverTime) {
        layer.msg('活动已结束');
    }
    else {
        layer.msg('领取成功');
        //location.href = '/activity/start-work/draw';
    }

});

$('.invest_btn').on('click',function () {
    location.href = '/m/loan-list';
});





