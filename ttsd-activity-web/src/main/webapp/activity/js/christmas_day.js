require(['jquery','drawCircle','logintip'], function ($,drawCircle) {

    var redirect = globalFun.browserRedirect();
    var $christmasDayFrame=$('#christmasDayFrame');
    var $activitySlide = $christmasDayFrame.prev();

    //为节约手机流量，把pc页面的图片在pc页上显示才增加
    (function() {
        if(redirect=='pc') {
            var HTML='<div class="body-decorate" id="bodyDecorate"> ' +
                '<div class="bg-left"></div> ' +
                '<div class="bg-right"></div> ' +
                '<div class="bg-bottom"></div> ' +
                '</div>';
            $christmasDayFrame.prepend(HTML);
            $activitySlide.addClass('pc-img');
        }
        else if(redirect=='mobile'){
            $activitySlide.html('<img src='+staticServer+'/activity/images/christmas-day/app-top.jpg>');
        }
    })();

    (function(drawCircle) {
        //抽奖模块
       var $rewardGiftBox=$('.reward-gift-box',$christmasDayFrame);

        var $MobileNumber=$('#MobileNumber'),
            pointAllList='/activity/christmas/all-list',  //中奖记录接口地址
            pointUserList='/activity/christmas/user-list',   //我的奖品接口地址
            drawURL='/activity/christmas/draw',    //抽奖的接口链接
            $pointerImg=$('.pointer-img',$rewardGiftBox),
            myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

        var tipMessage={
            info:'',
            button:'',
            area:[]
        };
        var paramData={
            "mobile":myMobileNumber,
            "activityCategory":"CHRISTMAS_ACTIVITY"
        };

        var drawCircle=new drawCircle(pointAllList,pointUserList,drawURL,paramData,$rewardGiftBox);

        //渲染中奖记录
        drawCircle.GiftRecord();

        //渲染我的奖品
        drawCircle.MyGift();

        //**********************开始抽奖**********************//
        $pointerImg.on('click',function() {
            $pointerImg.addClass('win-result');

            //延迟1.5秒抽奖
            setTimeout(function() {
                drawCircle.beginLuckDraw(function(data) {
                    //停止礼品盒的动画
                    $pointerImg.removeClass('win-result');

                    if (data.returnCode == 0) {
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

                        drawCircle.tipWindowPop(tipMessage);
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
                        tipMessage.info='<p class="login-text">不在活动时间内~</p>';
                        drawCircle.tipWindowPop(tipMessage);

                    } else if(data.returnCode == 4){
                        //实名认证
                        tipMessage.info='<p class="login-text">您还未实名认证~</p><p class="des-text">请实名认证后再来抽奖吧！</p>';
                        tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                        drawCircle.tipWindowPop(tipMessage);
                    }
                });
            },1500);
        });

        //点击切换按钮
        drawCircle.PrizeSwitch();

    })(drawCircle);


});