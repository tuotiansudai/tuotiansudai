require("activityStyle/give_iphonex_2017.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let drawCircle = require('activityJsModule/gift_circle_draw');
let sourceKind = globalFun.parseURL(location.href);
let $iphonex = $('#iphonex'),
    tipGroupObj = {};

var $pointerBtn = $('.draw-btn',$iphonex),
    $investBtn = $('.invest-btn',$iphonex);
var $oneThousandPoints=$('.gift-circle-frame',$iphonex);
var pointAllList='/activity/iphonex/prize-list',  //中奖记录接口地址
    drawURL='/activity/iphonex/iphonex-draw';    //抽奖的接口链接
var $formualCon = $('#formula_box');//计算公式
var $computational = $('#computational');
var oneData={
        'activityCategory':'IPHONEX_ACTIVITY'
    }

$iphonex.find('.tip-list-frame .tip-list').each(function (key, option) {
    let kind = $(option).attr('data-return');
    tipGroupObj[kind] = option;
});

// pointAllList:中奖记录接口地址
// pointUserList:我的奖品接口地址
// drawURL:抽奖的接口链接
// oneData:接口参数
// $oneThousandPoints:抽奖模版dom
// 抽奖机会接口
var drawCircleOne=new drawCircle(pointAllList,null,drawURL,oneData,$oneThousandPoints);
var $leftDrawCount = $('#draw_chance');
//渲染中奖记录
drawCircleOne.GiftRecord();
//中奖纪录滚动
drawCircleOne.scrollUp($iphonex.find('.user-record'),1000);
//计算公式显示隐藏
$computational.mouseover(function(){
    $formualCon.show();
})
$computational.mouseout(function(){
    $formualCon.hide();
})
//显示立即抽奖还是立即投资
function investOrDraw(){
    commonFun.useAjax({
            type:'GET',
            dataType: 'json',
            url:'/activity/iphonex/draw-time'
        }
        ,function(data) {
            $leftDrawCount.text(data);
            if(data == 0){

                $pointerBtn.hide();
                $investBtn.show();
            }else {
                $investBtn.hide();
                $pointerBtn.show();
            }

        })
}

$.when(commonFun.isUserLogin())
    .done(function () {
        investOrDraw();
    });

//开始抽奖

$pointerBtn.on('click', function(event) {

    drawCircleOne.beginLuckDraw(function(data) {
        //抽奖接口成功后奖品指向位置
        if (data.returnCode == 0) {
            var angleNum=0;
            investOrDraw();

            switch (data.prize) {
                case 'IPHONEX_ACTIVITY_ENVELOP_COUPON_5': //0.5%加息券
                    angleNum=45*8;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('0.5%加息券')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'IPHONEX_ACTIVITY_ENVELOP_EXPERIENCE_GOLD_88': //88体验金
                    angleNum=45*7;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('88体验金')
                        .parent().siblings('.des-text').html('您可以在 “APP个人中心－<br />我的体验金” 中进行查看');
                    break;
                case 'IPHONEX_ACTIVITY_ENVELOP_EXPERIENCE_GOLD_888': //888元体验金
                    angleNum=45*6;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('888元体验金')
                        .parent().siblings('.des-text').html('您可以在 “APP个人中心－<br />我的体验金” 中进行查看');
                    break;
                case 'IPHONEX_ACTIVITY_ENVELOP_ENVELOP_18': //18元红包
                    angleNum=45*5;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('18元红包')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'IPHONEX_ACTIVITY_ENVELOP_ENVELOP_188': //188元红包
                    angleNum=45*4;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('188元红包')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'IPHONEX_ACTIVITY_ENVELOP_ENVELOP_288':  //288元红包
                    angleNum=45*3;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('288元红包')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'IPHONEX_ACTIVITY_ENVELOP_ENVELOP_588':  //588元红包
                    angleNum=45*2;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('588元红包')
                        .parent().siblings('.des-text').html('您可以在 “个人中心” 中进行查看');
                    break;
                case 'CELEBRATION_SINGLE_ACTIVITY_EXPERIENCE_GOLD_2888': //iphonex
                    angleNum=45*1;
                    $(tipGroupObj['concrete']).find('.prizeValue').text('iphonex')
                        .parent().siblings('.des-text').html('奖品将于活动结束后发放');
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

$iphonex.find('.to-invest').on('click',function() {
    window.location.href = '/loan-list';
});

