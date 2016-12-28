require(['jquery','drawCircle','logintip','register_common'], function ($,drawCircle) {

    var redirect = globalFun.browserRedirect();
    var $christmasDayFrame=$('#christmasDayFrame');
    var $activitySlide = $christmasDayFrame.prev(),
        tipGroupObj={};

    $christmasDayFrame.find('.tip-list-frame .tip-list').each(function(key,option) {
        var kind=$(option).data('return');
        tipGroupObj[kind]=option;
    });

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

            // 是否加载快速注册的功能
            var urlObj=globalFun.parseURL(location.href);
            if(urlObj.params.tag=='yes') {
                $christmasDayFrame.find('.reg-tag-current').show();
            }
        }
    })();

    (function(drawCircle) {
        //抽奖模块
       var $rewardGiftBox=$('.reward-gift-box',$christmasDayFrame);

        var $MobileNumber=$('#MobileNumber'),
            pointAllList='/activity/christmas/all-list',  //中奖记录接口地址
            pointUserList='/activity/christmas/user-list',   //我的奖品接口地址
            drawURL='/activity/christmas/draw',    //抽奖的接口链接
            drawTime='/activity/christmas/drawTime', //抽奖次数
            $pointerImg=$('.pointer-img',$rewardGiftBox),
            myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

        var paramData={
            "mobile":myMobileNumber,
            "activityCategory":"CHRISTMAS_ACTIVITY"
        };

        drawCircle.prototype.showDrawTime=function() {
            $.ajax({
                url: drawTime,
                type: 'GET',
                dataType: 'json'
            }).done(function(data) {
                $('.draw-time',$rewardGiftBox).text(data);
            })
        }
        var drawCircle=new drawCircle(pointAllList,pointUserList,drawURL,paramData,$rewardGiftBox);

        //渲染中奖记录
        drawCircle.GiftRecord();

        //渲染我的奖品
        drawCircle.MyGift();

        //渲染我的抽奖次数
        drawCircle.showDrawTime();

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
                    //停止礼品盒的动画
                    $pointerImg.removeClass('win-result');
                    drawCircle.showDrawTime();

                    if (data.returnCode == 0) {
                        var prizeType=data.prizeType.toLowerCase();
                        $(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);
                        drawCircle.noRotateFn(tipGroupObj[prizeType]);

                    } else if(data.returnCode == 1) {
                        //没有抽奖机会
                        drawCircle.tipWindowPop(tipGroupObj['nochance']);
                    }
                    else if (data.returnCode == 2) {
                        //未登录
                        $('.no-login-text',$christmasDayFrame).trigger('click');  //弹框登录

                    } else if(data.returnCode == 3){
                        //不在活动时间范围内！
                        drawCircle.tipWindowPop(tipGroupObj['expired']);

                    } else if(data.returnCode == 4){
                        //实名认证
                        drawCircle.tipWindowPop(tipGroupObj['authentication']);
                    }
                });
            },1500);
        });

        //点击切换按钮
        var menuCls=$rewardGiftBox.find('.menu-switch').find('span');
        menuCls.on('click',function() {
            var $this=$(this),
                index=$this.index(),
                contentCls=$rewardGiftBox.find('.record-list ul');
                $this.addClass('active').siblings().removeClass('active');
                contentCls.eq(index).show().siblings().hide();

        });

    })(drawCircle);


});