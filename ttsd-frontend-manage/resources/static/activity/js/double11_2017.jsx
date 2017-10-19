require("activityStyle/double11_2017.scss");
require("activityStyle/media.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let drawCircle = require('activityJsModule/gift_circle_draw');
let tpl = require('art-template/dist/template');
let sourceKind = globalFun.parseURL(location.href);
//require('activityJsModule/fast_register');
//pointAllList:中奖记录接口地址
//pointUserList:我的奖品接口地址
//drawURL:抽奖的接口链接
//oneData:接口参数
//$oneThousandPoints:抽奖模版dom

let $double11 = $('#double11'),
    tipGroupObj = {};
var $pointerBtn = $('#draw_btn',$double11);
var $oneThousandPoints=$('.gift-circle-frame',$double11);
var $toLogin = $('#to_login_DOM'),
    $leftDrawDOM = $('#left_draw_DOM'),
    $loginBtn = $('.to-login-btn',$double11),
    $prizeLoginDOM = $('#prize_login_DOM');
var pointAllList='/activity/double-eleven/all-list ',  //中奖记录接口地址
    pointUserList='/activity/double-eleven/user-list',   //我的奖品接口地址
    drawURL='/activity/double-eleven/task-draw',//抽奖接口
    leftDraw = '/activity/double-eleven/left-times';    //剩余抽奖次数接口


var oneData={
        'activityCategory':'CELEBRATION_DOUBLE_ELEVEN_ACTIVITY'
    },
    $leftDrawCount=$('#leftDrawCount');

$double11.find('.tip-list-frame .tip-list').each(function (key, option) {
    let kind = $(option).attr('data-return');
    tipGroupObj[kind] = option;
});
function drawTimes(){
    commonFun.useAjax({
        dataType: 'json',
        type:'get',
        url:'/activity/double-eleven/left-times'
    },function(data) {
        $leftDrawCount.text(data);
    });
}
//通过判断是否登录显示隐藏相应的按钮
$.when(commonFun.isUserLogin())
    .done(function () {
        $toLogin.hide();
        $leftDrawDOM.show();
        $prizeLoginDOM.hide();
        drawTimes();
    })
    .fail(function(){
        $leftDrawDOM.hide();
        $toLogin.show();
        $prizeLoginDOM.show();
    });
//点击登录弹框登录
$loginBtn.on('click',function(event){
    event.preventDefault();
    //判断是否需要弹框登陆
    if (sourceKind.params.source == 'app') {
        location.href = "/login";
    }else {
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['auto', 'auto'],
            content: $('#loginTip')
        });
    }

})
var drawCircleOne=new drawCircle(pointAllList,pointUserList,drawURL,oneData,$oneThousandPoints);

//渲染中奖记录
drawCircleOne.GiftRecord();

//渲染我的奖品
drawCircleOne.MyGift();

drawCircleOne.hoverScrollList($double11.find('.user-record'),10);
//drawCircleOne.hoverScrollList($double11.find('.own-record'),10);
// layer.open({
//     type: 1,
//     title: false,
//     closeBtn: 0,
//     // scrollbar: false,
//     area: ['auto', 'auto'],
//     content: $('#test')
// });
//开始抽奖
//layer.msg('今天没有抽奖机会了哦~，明天再来吧');
$pointerBtn.on('click', function(event) {
    drawCircleOne.beginLuckDraw(function(data) {
        console.log(data)
        //抽奖接口成功后奖品指向位置
        if (data.returnCode == 0) {
            var angleNum=0;
            drawTimes();
            switch (data.prize) {
                case 'CELEBRATION_SINGLE_ACTIVITY_EXPERIENCE_GOLD_888': //0.5%加息券
                    angleNum=45*1-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('0.5%加息券')
                        .parent().siblings('.des-text').html('您可以在 “APP个人中心－<br />我的体验金” 中进行查看');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_LUGGAGE': //200元京东E卡
                    angleNum=45*2-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('200元京东E卡')
                        .parent().siblings('.des-text').html('奖品将于活动结束后发放');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_DOLL': //200元红包
                    angleNum=45*3-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('200元红包')
                        .parent().siblings('.des-text').html('奖品将于活动结束后发放');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_ENVELOP_30': //0.2%加息券
                    angleNum=45*4-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('0.2%加息券')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_COUPON_5': //100元红包
                    angleNum=45*5-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('100元红包')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_ENVELOP_10':  //20元红包
                    angleNum=45*6-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('20元红包')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_ENVELOP_5':  //1000元体验金
                    angleNum=45*7-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('1000元体验金')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_EXPERIENCE_GOLD_2888': //50元红包
                    angleNum=45*8-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('50元红包')
                        .parent().siblings('.des-text').html('您可以在 “APP个人中心－<br />我的体验金” 中进行查看');
                    break;
            }
            drawCircleOne.rotateFn(angleNum,tipGroupObj['concrete']);

        } else if(data.returnCode == 1) {
            //抽奖次数不足
            drawCircleOne.tipWindowPop(tipGroupObj['nochance']);
        }
        else if (data.returnCode == 2) {
            //未登录
            if (sourceKind.params.source == 'app') {
                location.href = "app/tuotian/login";
            }else{
                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: ['auto', 'auto'],
                    content: $('#loginTip')
                });
            }

        } else if(data.returnCode == 3){
            //不在活动时间范围内！
            drawCircleOne.tipWindowPop(tipGroupObj['expired']);

        } else if(data.returnCode == 4){
            //今日没有抽奖机会了
            drawCircleOne.tipWindowPop(tipGroupObj['authentication']);
        }
    });
});


//点击切换按钮
drawCircleOne.PrizeSwitch();

