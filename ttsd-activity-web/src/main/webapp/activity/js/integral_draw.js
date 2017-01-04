require(['jquery', 'underscore','drawCircle','register_common','logintip'], function ($, _,drawCircle) {

    var $integralDrawPage=$('#integralDrawPage'),
        $oneThousandPoints=$('.one-thousand-points',$integralDrawPage),
        $tenThousandPoints=$('.ten-thousand-points',$integralDrawPage),
        $myPropertyPoint=$('.gift-circle-detail .my-property',$integralDrawPage),
        tipGroupObj={};
    //以下为抽奖转盘
    var $pointerOne = $('.pointer-img',$oneThousandPoints);
    var $pointerTen = $('.pointer-img',$tenThousandPoints);
    var $MobileNumber=$('#MobileNumber'),
        pointAllList='/activity/point-draw/all-list',  //中奖记录接口地址
        pointUserList='/activity/point-draw/user-list',   //我的奖品接口地址
        drawURL='/activity/point-draw/draw',    //抽奖的接口链接
        myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

    $integralDrawPage.find('.tip-list-frame .tip-list').each(function(key,option) {
        var kind=$(option).data('return');
        tipGroupObj[kind]=option;
    });

    $oneThousandPoints.find('.gift-circle-out').after($('.draw-instructions',$oneThousandPoints));
    $tenThousandPoints.find('.gift-circle-out').after($('.draw-instructions',$tenThousandPoints));

    $myPropertyPoint.text(myPoint);

    var browser = globalFun.browserRedirect();
    var urlObj=globalFun.parseURL(location.href);
    if (browser == 'mobile') {
        if(urlObj.params.tag=='yes') {
            $('.reg-tag-current').show();
        }
    }


    //************************1000积分抽好礼*****************************/
    var oneData={
        "mobile":myMobileNumber,
        "activityCategory":"POINT_DRAW_1000"
    };

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

    //开始抽奖

    $pointerOne.on('click', function(event) {
        drawCircleOne.beginLuckDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                var angleNum=0;
                $myPropertyPoint.text(data.myPoint);
                switch (data.prize) {
                    case 'BICYCLE_XM':  //平衡车
                        angleNum=45*1-20;
                        break;
                    case 'MASK': //口罩
                        angleNum=45*7-20;
                        break;
                    case 'LIPSTICK':  //唇膏
                        angleNum=45*5-20;
                        break;
                    case 'PORCELAIN_CUP_BY_1000':  //杯子
                        angleNum=45*2-20;
                        break;
                    case 'PHONE_BRACKET':  //手机支架
                        angleNum=45*8-20;
                        break;
                    case 'PHONE_CHARGE_10':  //10元话费
                        angleNum=45*6-20;
                        break;
                    case 'RED_ENVELOPE_10': //10元红包
                        angleNum=45*4-20;
                        break;
                    case 'INTEREST_COUPON_2_POINT_DRAW':  //加息卷
                        angleNum=45*3-20;
                        break;
                }
                var prizeType=data.prizeType.toLowerCase();
                $(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);

                drawCircle.rotateFn(angleNum,tipGroupObj[prizeType]);

            } else if(data.returnCode == 1) {
                //积分不足
                drawCircle.tipWindowPop(tipGroupObj['nopoint']);
            }
            else if (data.returnCode == 2) {
                //未登录

                $('.no-login-text',$integralDrawPage).trigger('click');  //弹框登录

            } else if(data.returnCode == 3){
                //不在活动时间范围内！
                drawCircle.tipWindowPop(tipGroupObj['expired']);

            } else if(data.returnCode == 4){
                //实名认证
                drawCircle.tipWindowPop(tipGroupObj['authentication']);
            }
        });
    });


    //点击切换按钮
    drawCircleOne.PrizeSwitch();

    //************************10000积分抽好礼*****************************/

    var tenData={
        "mobile":myMobileNumber,
        "activityCategory":"POINT_DRAW_10000"
    };
    //pointAllList:中奖记录接口地址
    //pointUserList:我的奖品接口地址
    //drawURL:抽奖的接口链接
    //oneData:接口参数
    //$oneThousandPoints:抽奖模版dom
    var drawCircleTen=new drawCircle(pointAllList,pointUserList,drawURL,tenData,$tenThousandPoints);

    //渲染中奖记录
    drawCircleTen.GiftRecord();

    //渲染我的奖品
    drawCircleTen.MyGift();

    //开始抽奖
    $pointerTen.on('click', function(event) {
        drawCircleTen.beginLuckDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                var angleNum=0;
                $myPropertyPoint.text(data.myPoint);
                switch (data.prize) {
                    case 'IPHONE7_128G':  //iPhone 7手机128G
                        angleNum=45*1-20;
                        break;
                    case 'DELAYED_ACTION':    //通用自拍杆
                        angleNum=45*7-20;
                        break;
                    case 'U_DISH':  //拓天速贷U盘
                        angleNum=45*5-20;
                        break;
                    case 'PHONE_CHARGE_20':  //20元话费
                        angleNum=45*2-20;
                        break;
                    case 'HEADREST':  //车家两用U型头枕
                        angleNum=45*8-20;
                        break;
                    case 'IQIYI_MEMBERSHIP_30':  //爱奇艺会员月卡
                        angleNum=45*6-20;
                        break;
                    case 'RED_ENVELOPE_50_POINT_DRAW':  //50元投资红包
                        angleNum=45*3-20;
                        break;
                    case 'INTEREST_COUPON_5_POINT_DRAW':  //0.5%加息券
                        angleNum=45*4-20;
                        break;
                }

                var prizeType=data.prizeType.toLowerCase();
                $(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);

                drawCircle.rotateFn(angleNum,tipGroupObj[prizeType]);
            } else if(data.returnCode == 1) {
                //积分不足
                drawCircle.tipWindowPop(tipGroupObj['nopoint']);
            }
            else if (data.returnCode == 2) {
                //未登录
                $('.no-login-text',$integralDrawPage).trigger('click');  //弹框登录

            } else if(data.returnCode == 3){
                //不在活动时间范围内！
                tdrawCircle.tipWindowPop(tipGroupObj['expired']);

            } else if(data.returnCode == 4){
                //实名认证
                drawCircle.tipWindowPop(tipGroupObj['authentication']);
            }
        });
    });

    //点击切换按钮
    drawCircleTen.PrizeSwitch();


});

