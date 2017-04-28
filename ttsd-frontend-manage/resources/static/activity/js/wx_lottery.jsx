require('publicJs/login_tip');
let drawCircle = require('activityJsModule/gift_circle_draw');
require('activityStyle/wx_lottery.scss');
let tpl = require('art-template/dist/template');
let commonFun = require('publicJs/commonFun');


let $lanternFrame = $('#lanternFrame'),
    tipGroupObj = {};
var $pointerBtn = $('.pointer-img',$lanternFrame);
var $oneThousandPoints=$('.gift-item',$lanternFrame);
var $MobileNumber=$('#MobileNumber'),
    pointAllList='/activity/point-draw/all-list',  //中奖记录接口地址
    pointUserList='/activity/point-draw/user-list',   //我的奖品接口地址
    drawURL='/wechat/lottery/draw';    //抽奖的接口链接

var oneData={
    "activityCategory":"POINT_DRAW_1000"
};

$lanternFrame.find('.tip-list-frame .tip-list').each(function (key, option) {
    let kind = $(option).data('return');
    tipGroupObj[kind] = option;
});



    //pointAllList:中奖记录接口地址
    //pointUserList:我的奖品接口地址
    //drawURL:抽奖的接口链接
    //oneData:接口参数
    //$oneThousandPoints:抽奖模版dom
    var drawCircleOne=new drawCircle(pointAllList,pointUserList,drawURL,oneData,$oneThousandPoints);

    //渲染中奖记录
    drawCircleOne.GiftRecord(6);

    //渲染我的奖品
    drawCircleOne.MyGift(6);

    //开始抽奖

    $pointerBtn.on('click', function(event) {
        drawCircleOne.rotateFn(52,tipGroupObj['nochance']);
        drawCircleOne.beginLuckDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                var angleNum=0;
                $myPropertyPoint.text(data.myPoint);
                switch (data.prize) {
                    case 'BICYCLE_XM':  
                        angleNum=72*1-20;
                        break;
                    case 'MASK': 
                        angleNum=72*2-20;
                        break;
                    case 'LIPSTICK':  
                        angleNum=72*3-20;
                        break;
                    case 'PORCELAIN_CUP_BY_1000':  
                        angleNum=72*4-20;
                        break;
                    case 'PHONE_BRACKET': 
                        angleNum=72*5-20;
                        break;
                }
                var prizeType=data.prizeType.toLowerCase();
                $(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);

                drawCircleOne.rotateFn(angleNum,tipGroupObj[prizeType]);

            } else if(data.returnCode == 1) {
                //积分不足
                drawCircleOne.tipWindowPop(tipGroupObj['nopoint']);
            }
            else if (data.returnCode == 2) {
                //未登录

                $('.no-login-text',$integralDrawPage).trigger('click');  //弹框登录

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

