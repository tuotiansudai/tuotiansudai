require("activityStyle/single_rank.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let drawCircle = require('activityJsModule/gift_circle_draw');
let tpl = require('art-template/dist/template');
let sourceKind = globalFun.parseURL(location.href);


let topimg=require('../images/single-rank/top-image.jpg');
let topimgPhone=require('../images/single-rank/top-image-phone.jpg');
$('#topImg').find('.media-pc').attr('src',topimg).siblings('.media-phone').attr('src',topimgPhone);

let introimg=require('../images/single-rank/free-money.png');
let introimgPhone=require('../images/single-rank/free-money-phone.png');
$('#introImg').find('.media-pc').attr('src',introimg).siblings('.media-phone').attr('src',introimgPhone);


let $singleRank = $('#singleRank'),
    tipGroupObj = {};
var $pointerBtn = $('.pointer-img',$singleRank);
var $oneThousandPoints=$('.gift-circle-frame',$singleRank);
var pointAllList='/activity/point-draw/all-list',  //中奖记录接口地址
    pointUserList='/activity/point-draw/user-list',   //我的奖品接口地址
    drawURL='/activity/point-draw/single-draw';    //抽奖的接口链接

var oneData='',
$leftDrawCount=$('#leftDrawCount');

$singleRank.find('.tip-list-frame .tip-list').each(function (key, option) {
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
    drawCircleOne.GiftRecord();

    //渲染我的奖品
    drawCircleOne.MyGift();

    drawCircleOne.hoverScrollList($singleRank.find('.user-record'),6);
    drawCircleOne.hoverScrollList($singleRank.find('.own-record'),6);

    //开始抽奖

    $pointerBtn.on('click', function(event) {
        
        drawCircleOne.beginLuckDraw(function(data) {
            console.log(data)
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                var angleNum=0;
                $leftDrawCount.text(data.myPoint);
                switch (data.data.wechatLotteryPrize) {
                    case 'WECHAT_LOTTERY_BEDCLOTHES': //欧式奢华贡缎床品四件套
                        angleNum=72*5-20;
                        $(tipGroupObj['concrete']).find('.prizeValue').text('一等奖')
                        .parent().siblings('.des-text').text('欧式奢华贡缎床品四件套')
                        .siblings('.reward-text').attr('class','reward-text gift-one');
                        break;
                    case 'WECHAT_LOTTERY_BAG': //时尚百搭真皮子母包
                        angleNum=72*4-20;
                        $(tipGroupObj['concrete']).find('.prizeValue').text('二等奖')
                        .parent().siblings('.des-text').text('时尚百搭真皮子母包')
                        .siblings('.reward-text').attr('class','reward-text gift-two');
                        break;
                    case 'WECHAT_LOTTERY_HEADGEAR':  //简约吊坠百搭锁骨链
                        angleNum=72*3-20;
                         $(tipGroupObj['concrete']).find('.prizeValue').text('三等奖')
                         .parent().siblings('.des-text').text('简约吊坠百搭锁骨链')
                        .siblings('.reward-text').attr('class','reward-text gift-three');
                        break;
                    case 'WECHAT_LOTTERY_TOWEL':  //精品定制毛巾礼盒
                        angleNum=72*2-20;
                         $(tipGroupObj['concrete']).find('.prizeValue').text('四等奖')
                         .parent().siblings('.des-text').text('精品定制毛巾礼盒')
                        .siblings('.reward-text').attr('class','reward-text gift-four');
                        break;
                    case 'WECHAT_LOTTERY_RED_ENVELOP_20': //20元红包
                        angleNum=72*1-20;
                         $(tipGroupObj['concrete']).find('.prizeValue').text('五等奖')
                         .parent().siblings('.des-text').text('20元红包')
                        .siblings('.reward-text').attr('class','reward-text gift-five');
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

