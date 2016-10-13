require(['jquery', 'underscore', 'layerWrapper','drawCircle','template','commonFun', 'register_common'], function ($, _, layer,drawCircle,tpl) {

var $autumnTravelPage=$('#autumnTravelPage'),
    $awardList = $('.award-list',$autumnTravelPage),
    $giftCircleFrame=$('.gift-circle-frame',$autumnTravelPage);
    var scrollTimer, scrollTimer2;
    var $swiperWrapper = $('.swiper-wrapper',$autumnTravelPage),
        $swiperslide = $('.swiper-slide', $swiperWrapper);
    var browser = commonFun.browserRedirect();
    if (browser == 'mobile') {
        var urlObj=commonFun.parseURL(location.href);
        if(urlObj.params.tag=='yes') {
            $autumnTravelPage.find('.reg-tag-current').show();
        }
    }

    var $loginName = $('div.login-name');
    var loginName = $loginName ? $loginName.data('login-name') : '';

    $("a.autumn-travel-invest-channel").click(function () {
        $.ajax({
            url: '/activity/autumn/travel/invest?loginName=' + loginName,
            type: 'POST'
        });
    });

    //以下为抽奖转盘
    var $pointer = $('.pointer-img',$giftCircleFrame);
    var $MobileNumber=$('#MobileNumber'),
        travelAllList='/activity/autumn/travel-all-list',  //中奖记录接口地址
        travelUserList='/activity/autumn/travel-user-list',   //我的奖品接口地址
        drawURL='/activity/autumn/travel-draw',    //抽奖的接口链接
        myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

    var $GiftRecord=$('.user-record',$giftCircleFrame),
        $MyGift=$('.own-record',$giftCircleFrame);

    var tipList=$('.tip-list',$autumnTravelPage);
    var oneData={
        "mobile":myMobileNumber
    };
    var tipMessage={
        info:'',
        button:'',
        area:[]
    };
    //pointAllList:中奖记录接口地址
    //pointUserList:我的奖品接口地址
    //drawURL:抽奖的接口链接
    //oneData:接口参数
    //tipList:弹框dom
    //$oneThousandPoints:抽奖模版dom
    var drawCircle=new drawCircle(travelAllList,travelUserList,drawURL,oneData,tipList,$giftCircleFrame);

    //渲染中奖记录
    drawCircle.GiftRecord();

    //渲染我的奖品
    drawCircle.MyGift();

    //开始抽奖
    $pointer.on('click', function(event) {
        drawCircle.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                var angleNum=0;
                switch (data.prize) {
                    case 'PORCELAIN_CUP':
                        angleNum = 337;
                        break;
                    case 'INTEREST_COUPON_2':
                        angleNum = 56;
                        break;
                    case 'LUXURY':
                        angleNum = 116;
                        break;
                    case 'RED_ENVELOPE_100':
                        angleNum = 160;
                        break;
                    case 'INTEREST_COUPON_5':
                        angleNum = 230;
                        break;
                    case 'RED_ENVELOPE_50':
                        angleNum = 300;
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

                drawCircle.rotateFn(angleNum,tipMessage);
            } else if (data.returnCode == 2) {

                //未登录
                tipMessage.info='<p class="login-text">您还未登录~</p><p class="des-text">请登录后再来抽奖吧！</p>',
                    tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                drawCircle.tipWindowPop(tipMessage);

            } else if(data.returnCode == 3){
                //不在活动时间范围内！
                tipMessage.info='<p class="login-text">不在活动时间内~</p>';
                drawCircle.tipWindowPop(tipMessage);
            }
        });
    });

    //点击切换按钮
    drawCircle.PrizeSwitch();

    //中奖记录,我的奖品超过10条数据滚动
    drawCircle.scrollList($GiftRecord);
    drawCircle.scrollList($MyGift);

    $('body').on('click', '.go-close', function(event) {
        event.preventDefault();
        var $self = $(this);
        if($self.hasClass('go-on')) {
            window.location.reload();
        }
        layer.closeAll();
    });

});