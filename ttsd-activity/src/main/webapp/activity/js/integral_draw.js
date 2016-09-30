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
        $rotateBtnOne=$('.rotate-btn',$oneThousandPoints);

    var tipList=$('.tip-list',$integralDrawPage);

    var scrollTimer;
    var oneData={
        "mobile":myMobileNumber,
        "activityCategory":"POINT_DRAW_1000"
    };
    var drawCircleOne=new drawCircle(pointAllList,pointUserList,drawURL,oneData,tipList);

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
                    case 'BICYCLE_XM':
                        angleNum=12;
                        break;
                    case 'MASK':
                        angleNum=12;
                        break;
                    case 'LIPSTICK':
                        angleNum=12;
                        break;
                    case 'PORCELAIN_CUP_BY_1000':
                        angleNum=12;
                        break;
                    case 'PHONE_BRACKET':
                        angleNum=12;
                        break;
                    case 'PHONE_CHARGE_10':
                        angleNum=12;
                        break;
                    case 'RED_ENVELOPE_10':
                        angleNum=12;
                        break;
                    case 'INTEREST_COUPON_2_POINT_DRAW':
                        angleNum=12;
                        break;
                }
                data.istype=istype;

                //真实奖品
                if(istype=='CONCRETE') {
                    tipMessage.button='<a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>';
                    tipMessage.info='<p class="success-text">恭喜您！</p>' +
                        '<p class="reward-text">'+data.message+'！</p>' +
                        '<p class="des-text">拓天客服将会在7个工作日内联系您发放奖品</p>';


                }
                else if(istype=='VIRTUAL') {
                    tipMessage.button='<a href="/my-treasure" class="go-on">去查看</a><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>';
                    tipMessage.info='<p class="success-text">恭喜您！</p>' +
                        '<p class="reward-text">'+data.message+'！</p>' +
                        '<p class="des-text">奖品已发放至“我的宝藏”当中。</p>'
                }

                drawCircleOne.rotateFn($rotateBtnOne,angleNum,tipMessage);
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
    var $menuLi=$oneThousandPoints.find('.gift-record li'),
        $contentCls=$oneThousandPoints.find('.record-list ul');
    drawCircleOne.PrizeSwitch($menuLi,$contentCls);

    //中奖记录,我的奖品超过10条数据滚动
    drawCircleOne.scrollList($GiftRecordOne);
    drawCircleOne.scrollList($MyGiftOne);


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
    var drawCircleTen=new drawCircle(pointAllList,pointUserList,drawURL,tenData);

    //渲染中奖记录
    drawCircleTen.GiftRecord();

    //渲染我的奖品
    drawCircleTen.MyGift();

    //开始抽奖
    $pointerTen.on('click', function(event) {
        drawCircleOne.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                var angleNum=0;
                switch (data.prize) {
                    case 'BICYCLE_XM':
                        angleNum=12;
                        break;
                    case 'MASK':
                        angleNum=12;
                        break;
                    case 'LIPSTICK':
                        angleNum=12;
                        break;
                    case 'PORCELAIN_CUP_BY_1000':
                        angleNum=12;
                        break;
                    case 'PHONE_BRACKET':
                        angleNum=12;
                        break;
                    case 'PHONE_CHARGE_10':
                        angleNum=12;
                        break;
                    case 'RED_ENVELOPE_10':
                        angleNum=12;
                        break;
                    case 'INTEREST_COUPON_2_POINT_DRAW':
                        angleNum=12;
                        break;
                }
                data.istype=istype;

                //真实奖品
                if(istype=='CONCRETE') {
                    tipMessage.button='<a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>';
                    tipMessage.info='<p class="success-text">恭喜您！</p>' +
                        '<p class="reward-text">'+data.message+'！</p>' +
                        '<p class="des-text">拓天客服将会在7个工作日内联系您发放奖品</p>';


                }
                else if(istype=='VIRTUAL') {
                    tipMessage.button='<a href="/my-treasure" class="go-on">去查看</a><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>';
                    tipMessage.info='<p class="success-text">恭喜您！</p>' +
                        '<p class="reward-text">'+data.message+'！</p>' +
                        '<p class="des-text">奖品已发放至“我的宝藏”当中。</p>'
                }

                drawCircleOne.rotateFn($rotateBtnTen,angleNum,tipMessage);
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
    var $menuLiTen=$tenThousandPoints.find('.gift-record li'),
        $contentClsTen=$tenThousandPoints.find('.record-list ul');
    drawCircleTen.PrizeSwitch($menuLiTen,$contentClsTen);

    //中奖记录,我的奖品超过10条数据滚动
    drawCircleTen.scrollList($GiftRecordTen);
    drawCircleTen.scrollList($MyGiftTen);

});

