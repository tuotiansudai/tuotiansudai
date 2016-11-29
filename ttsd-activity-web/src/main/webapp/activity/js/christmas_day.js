require(['jquery','drawCircle'], function ($,drawCircle) {

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

    (function() {
        //抽奖模块
       var $rewardGiftBox=$('.reward-gift-box',$christmasDayFrame);

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
        var paramData={
            "mobile":myMobileNumber,
            "activityCategory":"POINT_DRAW_1000"
        };

        var drawCircle=new drawCircle(pointAllList,pointUserList,drawURL,paramData,$rewardGiftBox);

        //渲染中奖记录
        drawCircle.GiftRecord();

        //渲染我的奖品
        drawCircle.MyGift();

        //开始抽奖
        drawCircle.beginLuckDraw(function(data) {
            //抽奖接口成功后奖品指向位置
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
                //积分不足
                tipMessage.info='<p class="login-text">您的积分不足~</p><p class="des-text">投资赚取更多积分再来抽奖吧！</p>',
                    tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                drawCircle.tipWindowPop(tipMessage);
            }
            else if (data.returnCode == 2) {
                //未登录
                //tipMessage.info='<p class="login-text">您还未登录~</p><p class="des-text">请登录后再来抽奖吧！</p>',
                //tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                //drawCircle.tipWindowPop(tipMessage);

                $('.no-login-text',$integralDrawPage).trigger('click');  //弹框登录

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

        //点击切换按钮
        drawCircle.PrizeSwitch();

    })();


});