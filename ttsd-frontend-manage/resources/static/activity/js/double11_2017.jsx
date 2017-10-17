require("activityStyle/double11_2017.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let drawCircle = require('activityJsModule/gift_circle_draw');
let tpl = require('art-template/dist/template');
let sourceKind = globalFun.parseURL(location.href);
//require('activityJsModule/fast_register');

let $double11 = $('#double11'),
    tipGroupObj = {};
var $pointerBtn = $('#draw_btn',$double11);
var $oneThousandPoints=$('.gift-circle-frame',$double11);
var pointAllList='/activity/double-eleven/all-list ',  //中奖记录接口地址
    pointUserList='/activity/double-eleven/user-list',   //我的奖品接口地址
    drawURL='/activity/double-eleven/task-draw';    //抽奖的接口链接


var oneData={
        'activityCategory':'CELEBRATION_DOUBLE_ELEVEN_ACTIVITY'
    },
    $leftDrawCount=$('#leftDrawCount');

$double11.find('.tip-list-frame .tip-list').each(function (key, option) {
    let kind = $(option).attr('data-return');
    tipGroupObj[kind] = option;
});

//pointAllList:中奖记录接口地址
//pointUserList:我的奖品接口地址
//drawURL:抽奖的接口链接
//oneData:接口参数
//$oneThousandPoints:抽奖模版dom
var drawCircleOne=new drawCircle(pointAllList,pointUserList,drawURL,oneData,$oneThousandPoints);

//渲染中奖记录
//drawCircleOne.GiftRecord();

//渲染我的奖品
//drawCircleOne.MyGift();

//drawCircleOne.hoverScrollList($double11.find('.user-record'),10);
//drawCircleOne.hoverScrollList($double11.find('.own-record'),10);
layer.open({
    type: 1,
    title: false,
    closeBtn: 0,
    area: ['auto', 'auto'],
    content: $('#test')
});
//开始抽奖

$pointerBtn.on('click', function(event) {

    drawCircleOne.beginLuckDraw(function(data) {
        console.log(data)
        //抽奖接口成功后奖品指向位置
        if (data.returnCode == 0) {
            var angleNum=0;
            // commonFun.useAjax({
            //     dataType: 'json',
            //     url:'/activity/double-eleven/draw-time'
            // },function(data) {
            //     $leftDrawCount.text(data);
            // });
            switch (data.prize) {
                case 'CELEBRATION_SINGLE_ACTIVITY_EXPERIENCE_GOLD_888': //888元体验金
                    angleNum=45*1-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('888元体验金')
                        .parent().siblings('.des-text').html('您可以在 “APP个人中心－<br />我的体验金” 中进行查看');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_LUGGAGE': //迷彩旅行箱
                    angleNum=45*2-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('迷彩旅行箱')
                        .parent().siblings('.des-text').html('奖品将于活动结束后发放');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_DOLL': //树袋熊玩偶
                    angleNum=45*3-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('树袋熊玩偶')
                        .parent().siblings('.des-text').html('奖品将于活动结束后发放');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_ENVELOP_30': //30元红包
                    angleNum=45*4-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('30元红包')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_COUPON_5': //0.5%加息券
                    angleNum=45*5-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('0.5%加息券')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_ENVELOP_10':  //10元红包
                    angleNum=45*6-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('10元红包')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_ENVELOP_5':  //5元红包
                    angleNum=45*7-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('5元红包')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_EXPERIENCE_GOLD_2888': //2888元体验金
                    angleNum=45*8-20;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('2888元体验金')
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
            //实名认证
            drawCircleOne.tipWindowPop(tipGroupObj['authentication']);
        }
    });
});


//点击切换按钮
drawCircleOne.PrizeSwitch();

