require(['jquery', 'underscore','layerWrapper','drawCircle','commonFun','register_common'], function ($, _,layer,drawCircle) {

    var $integralDrawPage=$('#integralDrawPage'),
        $oneThousandPoints=$('.one-thousand-points',$integralDrawPage),
        $tenThousandPoints=$('.ten-thousand-points',$integralDrawPage),
        $myPropertyPoint=$('.gift-circle-detail .my-property',$integralDrawPage);
    //以下为抽奖转盘
    var $MobileNumber=$('#MobileNumber'),
        pointAllList='/activity/point-draw/all-list',  //中奖记录接口地址
        pointUserList='/activity/point-draw/user-list',   //我的奖品接口地址
        drawURL='/activity/point-draw/draw',    //抽奖的接口链接
        myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

    $oneThousandPoints.find('.gift-circle-out').after($('.draw-instructions',$oneThousandPoints));
    $tenThousandPoints.find('.gift-circle-out').after($('.draw-instructions',$tenThousandPoints));

    $myPropertyPoint.text(myPoint);
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
    var $pointerOne = $('.pointer-img',$oneThousandPoints);

    var tipList=$('.tip-list',$integralDrawPage);

    var scrollTimer;
    var oneData={
        "mobile":myMobileNumber,
        "activityCategory":"POINT_DRAW_1000"
    };

    //pointAllList:中奖记录接口地址
    //pointUserList:我的奖品接口地址
    //drawURL:抽奖的接口链接
    //oneData:接口参数
    //tipList:弹框dom
    //$oneThousandPoints:抽奖模版dom
    var drawCircleOne=new drawCircle(pointAllList,pointUserList,drawURL,oneData,tipList,$oneThousandPoints);

    //渲染中奖记录
    drawCircleOne.GiftRecord();

    //渲染我的奖品
    drawCircleOne.MyGift();

    //开始抽奖
    $pointerOne.on('click', function(event) {
        drawCircleOne.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                var angleNum=0;
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
                //真实奖品
                if(data.prizeType=='CONCRETE') {
                    tipMessage.button='<a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>';
                    tipMessage.info='<p class="success-text">恭喜您！</p>' +
                        '<p class="reward-text">'+data.message+'！</p>' +
                        '<p class="des-text">拓天客服将会在7个工作日内联系您发放奖品</p>';

                }
                else if(data.prizeType=='VIRTUAL') {
                    tipMessage.button='<a href="/my-treasure" class="go-on">去查看</a><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>';
                    tipMessage.info='<p class="success-text">恭喜您！</p>' +
                        '<p class="reward-text">'+data.message+'！</p>' +
                        '<p class="des-text">奖品已发放至“我的宝藏”当中。</p>'
                }

                drawCircleOne.rotateFn(angleNum,tipMessage);
            } else if(data.returnCode == 1) {
                //积分不足
                tipMessage.info='<p class="login-text">您的积分不足~</p><p class="des-text">投资赚取更多积分再来抽奖吧！</p>',
                    tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                    drawCircleOne.tipWindowPop(tipMessage);
            }
            else if (data.returnCode == 2) {
                //未登录
                tipMessage.info='<p class="login-text">您还未登录~</p><p class="des-text">请登录后再来抽奖吧！</p>',
                tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                drawCircleOne.tipWindowPop(tipMessage);

            } else if(data.returnCode == 3){
                //不在活动时间范围内！
                tipMessage.info='<p class="login-text">不在活动时间内~</p>';
                drawCircleOne.tipWindowPop(tipMessage);

            } else if(data.returnCode == 4){
                //实名认证
                tipMessage.info='<p class="login-text">您还未实名认证~</p><p class="des-text">请实名认证后再来抽奖吧！</p>';
                tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                drawCircleOne.tipWindowPop(tipMessage);
            }
        });
    });

    //点击切换按钮
    drawCircleOne.PrizeSwitch();

    //************************10000积分抽好礼*****************************/

   var $GiftRecordTen=$('.user-record',$tenThousandPoints),
        $MyGiftTen=$('.own-record',$tenThousandPoints),
        $pointerTen = $('.pointer-img',$tenThousandPoints),
        $rotateBtnTen=$('.rotate-btn',$tenThousandPoints);
    var scrollTimer2;
    var tenData={
        "mobile":myMobileNumber,
        "activityCategory":"POINT_DRAW_10000"
    };
    //pointAllList:中奖记录接口地址
    //pointUserList:我的奖品接口地址
    //drawURL:抽奖的接口链接
    //oneData:接口参数
    //tipList:弹框dom
    //$oneThousandPoints:抽奖模版dom
    var drawCircleTen=new drawCircle(pointAllList,pointUserList,drawURL,tenData,tipList,$tenThousandPoints);

    //渲染中奖记录
    drawCircleTen.GiftRecord();

    //渲染我的奖品
    drawCircleTen.MyGift();

    //开始抽奖
    $pointerTen.on('click', function(event) {
        drawCircleTen.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                var angleNum=0;
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

                //真实奖品
                if(data.prizeType=='CONCRETE') {
                    tipMessage.button='<a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>';
                    tipMessage.info='<p class="success-text">恭喜您！</p>' +
                        '<p class="reward-text">'+data.message+'！</p>' +
                        '<p class="des-text">拓天客服将会在7个工作日内联系您发放奖品</p>';


                }
                else if(data.prizeType=='VIRTUAL') {
                    tipMessage.button='<a href="/my-treasure" class="go-on">去查看</a><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>';
                    tipMessage.info='<p class="success-text">恭喜您！</p>' +
                        '<p class="reward-text">'+data.message+'！</p>' +
                        '<p class="des-text">奖品已发放至“我的宝藏”当中。</p>'
                }

                drawCircleTen.rotateFn(angleNum,tipMessage);
            } else if(data.returnCode == 1) {
                //积分不足
                tipMessage.info='<p class="login-text">您的积分不足~</p><p class="des-text">投资赚取更多积分再来抽奖吧！</p>',
                    tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                drawCircleTen.tipWindowPop(tipMessage);
            }
            else if (data.returnCode == 2) {
                //未登录
                tipMessage.info='<p class="login-text">您还未登录~</p><p class="des-text">请登录后再来抽奖吧！</p>',
                    tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                drawCircleTen.tipWindowPop(tipMessage);

            } else if(data.returnCode == 3){
                //不在活动时间范围内！
                tipMessage.info='<p class="login-text">不在活动时间内~</p>';
                drawCircleTen.tipWindowPop(tipMessage);

            } else if(data.returnCode == 4){
                //实名认证
                tipMessage.info='<p class="login-text">您还未实名认证~</p><p class="des-text">请实名认证后再来抽奖吧！</p>';
                tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                drawCircleTen.tipWindowPop(tipMessage);
            }
        });
    });

    //点击切换按钮
    drawCircleTen.PrizeSwitch();


});

