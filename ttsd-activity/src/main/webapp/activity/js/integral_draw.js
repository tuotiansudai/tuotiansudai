require(['jquery', 'underscore','layerWrapper','drawCircle','commonFun','register_common'], function ($, _,layer,drawCircle) {

    var $integralDrawPage=$('#integralDrawPage');
    //以下为抽奖转盘
    var $pointer = $('.pointer-img',$integralDrawPage);
    var $MobileNumber=$('#MobileNumber'),
        travelAllList='/activity/national/all-list',  //中奖记录接口地址
        travelUserList='/activity/national/user-list',   //我的奖品接口地址
        drawURL='/activity/national/draw',    //抽奖的接口链接
        myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

    var $GiftRecord=$('#GiftRecord'),
        $MyGift=$('#MyGift');

    var drawCircle=new drawCircle(travelAllList,travelUserList,drawURL,myMobileNumber);

    //渲染中奖记录
    drawCircle.GiftRecord();

    //渲染我的奖品
    drawCircle.MyGift(travelUserList,myMobileNumber);

    //开始抽奖
    $pointer.on('click', function(event) {
        drawCircle.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                switch (data.prize) {
                    case 'MANGO_CARD_100':
                        drawCircle.rotateFn(347, '100元芒果卡',data.prizeType);
                        break;
                    case 'RED_INVEST_15':
                        drawCircle.rotateFn(30, '15元投资红包',data.prizeType);
                        break;
                    case 'RED_INVEST_50':
                        drawCircle.rotateFn(120, '50元投资红包',data.prizeType);
                        break;
                    case 'TELEPHONE_FARE_10':
                        drawCircle.rotateFn(265, '10元话费',data.prizeType);
                        break;
                    case 'IQIYI_MEMBERSHIP':
                        drawCircle.rotateFn(80, '1个月爱奇艺会员',data.prizeType);
                        break;
                    case 'CINEMA_TICKET':
                        drawCircle.rotateFn(310, '电影票一张',data.prizeType);
                        break;
                    case 'FLOWER_CUP':
                        drawCircle.rotateFn(170, '青花瓷杯子',data.prizeType);
                        break;
                    case 'MEMBERSHIP_V5':
                        drawCircle.rotateFn(347, '1个月V5会员',data.prizeType);
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

    drawCircle.scrollList($GiftRecord);
    drawCircle.scrollList($MyGift);

    //scroll award record list
    var scrollTimer;

    $GiftRecord.hover(function() {
        clearInterval(scrollTimer);
    }, function() {
        scrollTimer = setInterval(function() {
            drawCircle.scrollList($GiftRecord);
        }, 2000);
    }).trigger("mouseout");

    $MyGift.hover(function() {
        clearInterval(scrollTimer);
    }, function() {
        scrollTimer = setInterval(function() {
            drawCircle.scrollList($MyGift);
        }, 2000);
    }).trigger("mouseout");

});

