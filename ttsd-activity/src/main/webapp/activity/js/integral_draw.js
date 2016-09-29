require(['jquery', 'underscore','layerWrapper','drawCircle','commonFun','register_common'], function ($, _,layer,drawCircle) {

    var $integralDrawPage=$('#integralDrawPage'),
        $oneThousandPoints=$('.one-thousand-points',$integralDrawPage),
        $tenThousandPoints=$('.ten-thousand-points',$integralDrawPage);
    //以下为抽奖转盘
    var $MobileNumber=$('#MobileNumber'),
        pointAllList='/activity/point-draw/all-list',  //中奖记录接口地址
        pointUserList='/activity/point-draw/user-list',   //我的奖品接口地址
        drawURL='/activity/point-draw/draw',    //抽奖的接口链接
        myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号


    var tipMessage={
        info:'',
        button:'',
        area:[]
    };
    $('body').on('click', '.go-close', function(event) {
        event.preventDefault();
        var $self = $(this);
        if($self.hasClass('go-on')) {
            window.location.reload();
        }
        layer.closeAll();
    });

    //************************1000积分抽好礼*****************************/
    var $GiftRecordOne=$('.user-record',$oneThousandPoints),
        $MyGiftOne=$('.own-record',$oneThousandPoints),
        $pointerOne = $('.pointer-img',$oneThousandPoints),
        $textTipOne=$('.text-tip',$oneThousandPoints);

    var tipList=$('.tip-list',$integralDrawPage);

    var scrollTimer;
    var oneData={
        "mobile":myMobileNumber,
        "activityCategory":"POINT_DRAW_1000"
    };
    var drawCircleOne=new drawCircle(pointAllList,pointUserList,drawURL,oneData);

    //渲染中奖记录
    drawCircleOne.GiftRecord();

    //渲染我的奖品
    drawCircleOne.MyGift();

    //开始抽奖
    $pointerOne.on('click', function(event) {
        drawCircleOne.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                switch (data.prize) {
                    case 'BICYCLE_XM':
                        drawCircleOne.rotateFn(347, '小米（MI）九号平衡车',data.prizeType);
                        break;

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
            } else if(data.returnCode == 1) {
                //积分不足
                tipMessage.info='<p class="login-text">您的积分不足~</p><p class="des-text">投资赚取更多积分再来抽奖吧！</p>',
                    tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                    drawCircleOne.tipWindowPop(tipMessage,tipList);
            }
            else if (data.returnCode == 2) {
                //未登录
                tipMessage.info='<p class="login-text">您还未登录~</p><p class="des-text">请登录后再来抽奖吧！</p>',
                tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                drawCircleOne.tipWindowPop(tipMessage,tipList);

            } else if(data.returnCode == 3){
                //不在活动时间范围内！
                tipMessage.info='<p class="login-text">不在活动时间内~</p>';
                drawCircleOne.tipWindowPop(tipMessage,tipList);

            } else if(data.returnCode == 4){
                //实名认证
                tipMessage.info='<p class="login-text">您还未实名认证~</p><p class="des-text">请实名认证后再来抽奖吧！</p>';
                tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                drawCircleOne.tipWindowPop(tipMessage,tipList);
            }
        });
    });

    //中奖记录,我的奖品超过10条数据滚动

    drawCircleOne.scrollList($GiftRecordOne);
    drawCircleOne.scrollList($MyGiftOne);

    //scroll award record list

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


    //************************10000积分抽好礼*****************************/

   var $GiftRecordTen=$('.user-record',$tenThousandPoints),
        $MyGiftTen=$('.own-record',$tenThousandPoints),
        $pointerTen = $('.pointer-img',$tenThousandPoints),
        $textTipTen=$('.text-tip',$tenThousandPoints);
    var scrollTimer2;
    var tenData={
        "mobile":myMobileNumber,
        "activityCategory":"POINT_DRAW_10000"
    };
    var drawCircleTen=new drawCircle(pointAllList,pointUserList,drawURL,tenData);

    //渲染中奖记录
    drawCircleTen.GiftRecord();

    //渲染我的奖品
    drawCircleTen.MyGift();

    //开始抽奖
    $pointerTen.on('click', function(event) {
        drawCircleTen.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                switch (data.prize) {
                    case 'MANGO_CARD_100':
                        drawCircleTen.rotateFn(347, '100元芒果卡',data.prizeType);
                        break;
                    case 'RED_INVEST_15':
                        drawCircleTen.rotateFn(30, '15元投资红包',data.prizeType);
                        break;
                    case 'RED_INVEST_50':
                        drawCircleTen.rotateFn(120, '50元投资红包',data.prizeType);
                        break;
                    case 'TELEPHONE_FARE_10':
                        drawCircleTen.rotateFn(265, '10元话费',data.prizeType);
                        break;
                    case 'IQIYI_MEMBERSHIP':
                        drawCircleTen.rotateFn(80, '1个月爱奇艺会员',data.prizeType);
                        break;
                    case 'CINEMA_TICKET':
                        drawCircleTen.rotateFn(310, '电影票一张',data.prizeType);
                        break;
                    case 'FLOWER_CUP':
                        drawCircleTen.rotateFn(170, '青花瓷杯子',data.prizeType);
                        break;
                    case 'MEMBERSHIP_V5':
                        drawCircleTen.rotateFn(347, '1个月V5会员',data.prizeType);
                        break;
                }
            } else if (data.returnCode == 2) {
                //未登录


                //$('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'nologin'})).show().find('.tip-dom').show();
            } else if(data.returnCode == 3){
                //$('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'timeout'})).show().find('.tip-dom').show();
            } else {
                //$('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'notimes'})).show().find('.tip-dom').show();
            }
        });
    });

    //中奖记录,我的奖品超过10条数据滚动

    drawCircleTen.scrollList($GiftRecordTen);
    drawCircleTen.scrollList($MyGiftTen);

    //scroll award record list

    $GiftRecordTen.hover(function() {
        clearInterval(scrollTimer);
    }, function() {
        scrollTimer2 = setInterval(function() {
            drawCircleTen.scrollList($GiftRecordTen);
        }, 2000);
    }).trigger("mouseout");

    $MyGiftTen.hover(function() {
        clearInterval(scrollTimer2);
    }, function() {
        scrollTimer2 = setInterval(function() {
            drawCircleTen.scrollList($MyGiftTen);
        }, 2000);
    }).trigger("mouseout");

});

