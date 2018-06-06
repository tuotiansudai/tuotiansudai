require('activityStyle/wechat/invite_friends_openWare.scss');
let commonFun= require('publicJs/commonFun');

commonFun.calculationRem(document, window);

commonFun.countTimePop($('.time'),'2018-06-06 18:38:50');

let startTime = $('#inviteContainer').data('startTime');
let overTime = $('#inviteContainer').data('overTime');
let activityTime = new Date(startTime.replace(/-/g, "/")).getTime(); // 活动开始时间
let activityOverTime = new Date(overTime.replace(/-/g, "/")).getTime();  // 活动结束时间
let currentTime = new Date().getTime();
if (currentTime < activityTime) {
    layer.msg('活动未开始');
}
// 点击“邀请好友拆红包”，未登录时跳转到登录页面；
//
// 已登录时点击“邀请好友拆红包”button，页面弹窗提示“活动期间内只有一次机会，是否确认邀请？”点击取消弹窗消失停留在当前页，点击确认，页面弹窗提示右上角分享，点击“我知道了”弹窗消失；
$.when(commonFun.isUserLogin())
    .done(function () {
        if(!dateTime.hasClass('activity-ing')) {
            $('.invest-btn').click(function(e){
                e.preventDefault();
                layer.msg('不在活动时间范围内');

            })
        }
    })