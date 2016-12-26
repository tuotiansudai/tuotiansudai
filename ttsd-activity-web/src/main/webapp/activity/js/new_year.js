require(['jquery','drawCircle','commonFun','logintip','register_common'], function ($,drawCircle,commonFun) {
    $(function() {

        var redirect = globalFun.browserRedirect();
        var $newYearDayFrame = $('#newYearDayFrame');
        var $activitySlide=$('#newYearSlide');
        var $loginInBtn=$('#loginIn');
        var locationUrl=location.href;
        var sourceKind=globalFun.parseURL(locationUrl);

        (function() {
            $loginInBtn.on('click',function() {
                if(sourceKind.params.source=='app') {
                    location.href="/login";
                } else {
                    $('.no-login-text',$newYearDayFrame).trigger('click');  //弹框登录
                }
            })

        })();

        //文字连续滚动
        (function() {
            var $slideText=$('.slide-text',$newYearDayFrame);
            var lineHeight = $slideText.find('li').eq(0).height();
            var textTimer;

            $slideText.hover(function() {
                clearInterval(textTimer);
            },function() {
                textTimer = setInterval(function() {
                    var $ulList=$slideText.find('.slide-text-list');
                    $ulList.animate({
                        "margin-top": -lineHeight + "px"
                    }, 2000, function() {
                        $ulList.css({
                            "margin-top": "0px"
                        })
                            .find("li:first")
                            .appendTo($ulList);
                    });
                }, 500);
            }).trigger('mouseout');
        })();

        //为节约手机流量，把pc页面的图片在pc页上显示才增加
        (function() {
            if(redirect=='pc') {
                var HTML='<div class="body-decorate" id="bodyDecorate"> ' +
                    '<div class="bg-left"></div> ' +
                    '<div class="bg-right"></div> ' +
                    '</div>';
                $newYearDayFrame.prepend(HTML);
                $activitySlide.addClass('pc-img');
            }
            else {
                $activitySlide.html('<img src='+staticServer+'/activity/images/new-year/app-top.jpg>');

                // 是否加载快速注册的功能
               var urlObj=globalFun.parseURL(location.href);
                if(urlObj.params.tag=='yes') {
                    $newYearDayFrame.find('.reg-tag-current').show();
                 }
            }
        })();

        (function(drawCircle) {
            //抽奖模块
            var $rewardGiftBox=$('.reward-gift-box',$newYearDayFrame);
            var $rewardTaskStatus=$('#rewardTaskStatus'),
                $rewardListFrame=$('.reward-list-frame',$newYearDayFrame);

            var $MobileNumber=$('#MobileNumber'),
                pointAllList='/activity/point-draw/all-list',  //中奖记录接口地址
                pointUserList='/activity/point-draw/user-list',   //我的奖品接口地址
                drawURL='/activity/point-draw/task-draw',    //抽奖的接口链接
                // drawTime='/activity/christmas/drawTime', //抽奖次数
                $pointerImg=$('.gold-egg',$rewardGiftBox),
                myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号
            var $signToday=$('#signToday');
            var myTimes=$rewardGiftBox.find('.my-times').data('times'); //初始抽奖次数
            var tipMessage={
                info:'',
                button:'',
                area:[]
            };
            var paramData={
                "mobile":myMobileNumber,
                "activityCategory":"ANNUAL_ACTIVITY"
            };

            $rewardListFrame.find('.reward-box').each(function(key,option) {
                var statusObj=$rewardTaskStatus.val().split(',');
                if(statusObj[key]==1) {
                    $(option).addClass('finished');
                }
            });

             $.when(commonFun.isUserLogin())
                 .done(function(){
                     $('.signedIn-status .normal-button',$rewardGiftBox).show();
                 })
                 .fail(function(){
                     console.log('未登陆');
                     $loginInBtn.show();
                 });

            //签到
            drawCircle.prototype.signToday=function(callback,failFun) {

                $signToday.on('click',function(event) {
                    if(event.target.id!='signToday') {
                        return;
                    }
                    $.ajax({
                        url:'/point/sign-in',
                        type:'POST',
                        dataType: 'json'
                    })
                        .done(function(response) {
                            callback && callback(response);
                        })
                        .fail(function() {
                            failFun && failFun()
                        })
                })
            }

            var drawCircle=new drawCircle(pointAllList,pointUserList,drawURL,paramData,$rewardGiftBox);

            //签到成功
            drawCircle.signToday(function() {
                tipMessage.button='<a href="javascript:void(0)" class="go-on go-close">知道了</a>';
                tipMessage.info='<p class="success-text">签到成功！</p>' +
                    '<p class="des-text">恭喜您获得砸金蛋机会一次</p>';
                $signToday.text('已签到');
                var thisTime = Number($rewardGiftBox.find('.my-times').text());
                $rewardGiftBox.find('.my-times').text(thisTime+1);
                $signToday.removeAttr('id');
                drawCircle.tipWindowPop(tipMessage);
            },function() {
                tipMessage.button='';
                tipMessage.info='<p class="login-text">请与客服联系</p>';
                drawCircle.tipWindowPop(tipMessage);
            });

            //渲染中奖记录
            drawCircle.GiftRecord();

            //渲染我的奖品
            drawCircle.MyGift();

            //**********************开始抽奖**********************//
            $pointerImg.on('click',function() {
                //判断是否正在抽奖
                if($pointerImg.hasClass('win-result')) {
                    return;//不能重复抽奖
                }
                $pointerImg.addClass('win-result');
                //延迟1.5秒抽奖
                setTimeout(function() {
                    drawCircle.beginLuckDraw(function(data) {
                        //停止鸡蛋的动画
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
                                tipMessage.button='<a href="/my-treasure" class="double-btn">去查看</a><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>';
                                tipMessage.info='<p class="success-text">恭喜您！</p>' +
                                    '<p class="reward-text">'+data.prizeValue+'！</p>' +
                                    '<p class="des-text">奖品已发放至“我的宝藏”当中。</p>'
                            }
                            drawCircle.noRotateFn(tipMessage);
                            
                            // 抽奖次数
                            $rewardGiftBox.find('.my-times').text(--myTimes);
                        } else if(data.returnCode == 1) {
                            //没有抽奖机会
                            tipMessage.info='<p class="login-text">您暂无抽奖机会啦～</p><p class="des-text">赢取机会后再来抽奖吧！</p>',
                                tipMessage.button='<a href="javascript:void(0)" class="go-close">知道了</a>';
                            drawCircle.tipWindowPop(tipMessage);
                        }
                        else if (data.returnCode == 2) {
                            //未登录

                            if(sourceKind.params.source=='app') {
                                location.href="/login";
                            } else {
                                $('.no-login-text',$newYearDayFrame).trigger('click');  //弹框登录
                            }

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
    })
});


