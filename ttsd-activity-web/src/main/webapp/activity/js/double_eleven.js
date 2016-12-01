require(['jquery', 'underscore', 'layerWrapper', 'drawCircle', 'jquery.ajax.extension', 'commonFun', 'register_common', 'logintip'], function($, _, layer,drawCircle) {
    $(function() {
        var browser = commonFun.browserRedirect(),
            timeCount=0;
        if (browser == 'mobile') {
            var urlObj = commonFun.parseURL(location.href);
            if (urlObj.params.tag == 'yes') {
                $('.reg-tag-current').show();
            }
        }

        function getRTime() {
            $('#timeCount li').each(function(index, el) {
                var $self=$(this),
                    _this=$self.find('.time-text span'),
                    endtimeText=_this.attr('data-end'),
                    nowtimeText=_this.attr('data-now'),
                    activityEnd=_this.attr('data-activityEnd'),
                    dateText=_this.attr('data-date').replace('-','/'),
                    EndTime = new Date('2016/'+dateText+' '+endtimeText),
                    NowTime = $('#nowTimeCount').val(),
                    t = EndTime.getTime() - parseInt(NowTime),
                    h = Math.floor(t / 1000 / 60 / 60 % 24),
                    m = Math.floor(t / 1000 / 60 % 60),
                    s = Math.floor(t / 1000 % 60);

                if(parseInt(NowTime)>new Date(activityEnd).getTime()){
                    clearInterval(timer);
                    $self.addClass('end').find('.time-text span').text('活动已过期');
                }else{
                    if(t>0){
                        _this.text((h>=10?h:'0'+h) + ":" + (m>=10?m:'0'+m) + ":" + (s>=10?s:'0'+s));
                    }else{
                        $self.addClass('active') &&_this.html('火热进行中');
                    }
                }
            });
        }
        $('#nowTimeCount').val(new Date($('#nowTimeCount').attr('data-time')).getTime());
        getRTime();
        var timer=setInterval(function(){
            $('#nowTimeCount').val(function(index,num){return parseInt(num)+1000});
            getRTime();
        }, 1000);

    });

    (function(drawCircle) {
        var $christmasDayFrame=$('#doubleElevenContainer');
        //抽奖模块
        var $rewardGiftBox=$('.nine-lottery-group',$christmasDayFrame);

        var $MobileNumber=$('#MobileNumber'),
            pointAllList='/activity/point-draw/all-list',  //中奖记录接口地址
            pointUserList='/activity/point-draw/user-list',   //我的奖品接口地址
            drawURL='/activity/point-draw/draw',    //抽奖的接口链接
            $pointerImg=$('.lottery-btn',$rewardGiftBox),
            myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

        var tipMessage={
            info:'',
            button:'',
            area:[]
        };
        var paramData={
            "mobile":myMobileNumber,
            "activityCategory":"CARNIVAL_ACTIVITY"
        };
        var drawCircle=new drawCircle(pointAllList,pointUserList,drawURL,paramData,$rewardGiftBox);

        //渲染中奖记录
        drawCircle.GiftRecord();

        //渲染我的奖品
        drawCircle.MyGift();

        //**********************开始抽奖**********************//
        $pointerImg.on('click',function() {
            drawCircle.beginLuckDraw(function(data) {
                var prizeKind;

                if (data.returnCode == 0) {

                    switch (data.prize) {
                        case 'M1_PHONE':  //锤子M1手机
                            prizeKind=7;
                            break;
                        case 'HUMIDIFIER': //小熊加湿器
                            prizeKind=0;
                            break;
                        case 'HAIR_DRIER':  //飞科电吹风机
                            prizeKind=1;
                            break;
                        case 'IQIYI_MEMBERSHIP_REF_CARNIVAL':  //爱奇艺会员
                            prizeKind=5;
                            break;
                        case 'TELEPHONE_FARE_10_REF_CARNIVAL':  //10元话费
                            prizeKind=2;
                            break;
                        case 'BAMBOO_CHARCOAL_PACKAGE':  //卡通汽车竹炭包
                            prizeKind=6;
                            break;
                        case 'INTEREST_COUPON_5_POINT_DRAW_REF_CARNIVAL': //0.5加息券
                            prizeKind=3;
                            break;
                        case 'RED_ENVELOPE_50_POINT_DRAW_REF_CARNIVAL':  //50元红包
                            prizeKind=4;
                            break;
                    }

                    //真实奖品
                    if(data.prizeType=='CONCRETE') {
                        tipMessage.button='<a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>';
                        tipMessage.info='<p class="success-text">恭喜您！</p>' +
                            '<p class="reward-text">抽中了'+data.prizeValue+'！</p>' +
                            '<p class="des-text">拓天客服将会在7个工作日内联系您发放奖品</p>';

                    }
                    else if(data.prizeType=='VIRTUAL') {
                        tipMessage.button='<a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>';
                        tipMessage.info='<p class="success-text">恭喜您！</p>' +
                            '<p class="reward-text">'+data.prizeValue+'！</p>' +
                            '<p class="des-text">奖品已发放至“我的宝藏”当中。</p>'
                    }
                    drawCircle.lotteryRoll({
                        elementId:'lotteryBox',
                        speed:100,
                        prize:prizeKind
                    },tipMessage);

                } else if(data.returnCode == 1) {
                    //没有抽奖机会
                    tipMessage.info='<p class="login-text">您暂无抽奖机会啦～</p><p class="des-text">赢取机会后再来抽奖吧！</p>',
                        tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                    drawCircle.tipWindowPop(tipMessage);
                }
                else if (data.returnCode == 2) {
                    //未登录
                    $('.no-login-text',$christmasDayFrame).trigger('click');  //弹框登录

                } else if(data.returnCode == 3){
                    //不在活动时间范围内！
                    tipMessage.info='<p class="login-text">活动已过期～</p><p class="des-text">更多活动即将登场！</p> ';
                    tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                    drawCircle.tipWindowPop(tipMessage);

                } else if(data.returnCode == 4){
                    //实名认证
                    tipMessage.info='<p class="login-text">您还未实名认证~</p><p class="des-text">请实名认证后再来抽奖吧！</p>';
                    tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                    drawCircle.tipWindowPop(tipMessage);
                }
            });
        });

        //点击切换按钮
        var menuCls=$rewardGiftBox.find('.lottery-right-group').find('h3 span');
        menuCls.on('click',function() {
            var $this=$(this),
                index=$this.index(),
                contentCls=$rewardGiftBox.find('.record-item');
            $this.addClass('active').siblings().removeClass('active');
            contentCls.eq(index).show().siblings().hide();

        });

    })(drawCircle);
});