require(['jquery', 'underscore','layerWrapper','drawCircle','commonFun','register_common'], function ($, _,layer,drawCircle) {

    var $integralDrawPage=$('#integralDrawPage');
    //以下为抽奖转盘
    var $pointer = $('.pointer-img',$integralDrawPage);
    var $MobileNumber=$('#MobileNumber'),
        travelAllList='/activity/national/all-list',  //中奖记录接口地址
        travelUserList='/activity/national/user-list',   //我的奖品接口地址
        drawURL='/activity/national/draw',    //抽奖的接口链接
        myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

    var $GiftRecordOne=$('.one-thousand-points .user-record',$integralDrawPage),
        $MyGiftOne=$('.one-thousand-points .own-record',$integralDrawPage),

        $GiftRecordTen=$('.ten-thousand-points .user-record',$integralDrawPage),
        $MyGiftTen=$('.ten-thousand-points .own-record',$integralDrawPage);

    var drawCircleOne=new drawCircle(travelAllList,travelUserList,drawURL,myMobileNumber);

    //渲染中奖记录
    drawCircleOne.GiftRecord();

    //渲染我的奖品
    drawCircleOne.MyGift(travelUserList,myMobileNumber);

    //开始抽奖
    $pointer.on('click', function(event) {
        drawCircleOne.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                switch (data.prize) {
                    case 'MANGO_CARD_100':
                        drawCircleOne.rotateFn(347, '100元芒果卡',data.prizeType);
                        break;
                    case 'RED_INVEST_15':
                        drawCircleOne.rotateFn(30, '15元投资红包',data.prizeType);
                        break;
                    case 'RED_INVEST_50':
                        drawCircleOne.rotateFn(120, '50元投资红包',data.prizeType);
                        break;
                    case 'TELEPHONE_FARE_10':
                        drawCircleOne.rotateFn(265, '10元话费',data.prizeType);
                        break;
                    case 'IQIYI_MEMBERSHIP':
                        drawCircleOne.rotateFn(80, '1个月爱奇艺会员',data.prizeType);
                        break;
                    case 'CINEMA_TICKET':
                        drawCircleOne.rotateFn(310, '电影票一张',data.prizeType);
                        break;
                    case 'FLOWER_CUP':
                        drawCircleOne.rotateFn(170, '青花瓷杯子',data.prizeType);
                        break;
                    case 'MEMBERSHIP_V5':
                        drawCircleOne.rotateFn(347, '1个月V5会员',data.prizeType);
                        break;
                }
            } else if (data.returnCode == 2) {
                //$('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'nologin'})).show().find('.tip-dom').show();
            } else if(data.returnCode == 3){
                //$('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'timeout'})).show().find('.tip-dom').show();
            } else {
                //$('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'notimes'})).show().find('.tip-dom').show();
            }
        });
    });

    //中奖记录,我的奖品超过10条数据滚动

    drawCircleOne.scrollList($GiftRecordOne);
    drawCircleOne.scrollList($MyGiftOne);

    //scroll award record list
    var scrollTimer;

    $GiftRecordOne.hover(function() {
        clearInterval(scrollTimer);
    }, function() {
        scrollTimer = setInterval(function() {
            drawCircleOne.scrollList($GiftRecordOne);
        }, 2000);
    }).trigger("mouseout");

    $MyGiftOne.hover(function() {
        clearInterval(scrollTimer);
    }, function() {
        scrollTimer = setInterval(function() {
            drawCircleOne.scrollList($MyGiftOne);
        }, 2000);
    }).trigger("mouseout");

});

