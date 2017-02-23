require(['jquery', 'layerWrapper','template', 'jquery.ajax.extension', 'register_common', 'logintip'], function($, layer,tpl) {
    $(function() {
        var browser = globalFun.browserRedirect(),
            $womenDayContainer = $('#womenDayContainer'),
            timeCount = 0,
            $machineLottery=$('.machine-lottery',$womenDayContainer),
            $pointerImg=$('.sign-btn span',$machineLottery);
        if (browser == 'mobile') {
            var urlObj = globalFun.parseURL(location.href);
            if (urlObj.params.tag == 'yes') {
                $('.reg-tag-current').show();
            }
        }

        var lottery={
            getLottery:function(){
                $.ajax({
                    url: '/activity/point-draw/task-draw',
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        'activityCategory': 'WOMAN_DAY_ACTIVITY'
                    }
                })
                .done(function(data) {
                    if (data.returnCode == 0) {
                        $pointerImg.removeClass('sign-active');

                    } else if (data.returnCode == 1) {
                        //没有抽奖机会
                        layer.open({
                          type: 1,
                          closeBtn:0,
                          move:false,
                          area:['460px','370px'],
                          title:false,
                          content: $('#nochance')
                        });
                    } else if (data.returnCode == 2) {
                        //未登录
                        $('.no-login-text', $womenDayContainer).trigger('click'); //弹框登录

                    } else if (data.returnCode == 3) {
                        //不在活动时间范围内！
                        layer.open({
                          type: 1,
                          closeBtn:0,
                          move:false,
                          area:['460px','370px'],
                          title:false,
                          content: $('#expired')
                        });
                    } else if (data.returnCode == 4) {
                        //实名认证
                        layer.open({
                          type: 1,
                          closeBtn:0,
                          move:false,
                          area:['460px','370px'],
                          title:false,
                          content: $('#authentication')
                        });
                    }
                })
                .fail(function() {
                    layer.msg('请求失败，请重试！');
                });
            },
            giftRecord:function(){
                $.ajax({
                    url: '/activity/point-draw/all-list',
                    type: 'GET',
                    dataType: 'json',
                    data: {
                        'activityCategory': 'WOMAN_DAY_ACTIVITY'
                    }
                })
                .done(function(data) {
                    $('#recordList').html(tpl('recordListTpl',{'recordlist':data}));
                })
                .fail(function() {
                    layer.msg('请求失败，请重试！');
                });
                
            },
            myGift:function(){
                $.ajax({
                    url: '/activity/point-draw/user-list',
                    type: 'GET',
                    dataType: 'json',
                    data: {
                        'activityCategory': 'WOMAN_DAY_ACTIVITY'
                    }
                })
                .done(function(data) {
                    $('#myRecord').html(tpl('myRecordTpl',{'myrecord':data}));
                })
                .fail(function() {
                    layer.msg('请求失败，请重试！');
                });
            },
            scrollList:function(domName,length) {
                var $self=domName;
                var lineHeight = $self.find("li:first").height();
                if ($self.find('li').length > (length!=''?length:10)) {
                    $self.animate({
                        "margin-top": -lineHeight + "px"
                    }, 600, function() {
                        $self.css({
                            "margin-top": "0px"
                        }).find("li:first").appendTo($self);
                    });
                }
            },
            hoverScrollList:function(domName,length) {
                var _this=this,
                    scrollTimer;
                domName.hover(function() {
                    clearInterval(scrollTimer);
                }, function() {
                    scrollTimer = setInterval(function() {
                        _this.scrollList(domName,length);
                    }, 2000);
                }).trigger("mouseout");
            }
        };

        $pointerImg.on('click', function(event) {
            event.preventDefault();
            if ($pointerImg.hasClass('sign-already')) {
                return; //不能重复抽奖
            }
            $pointerImg.addClass('sign-active');
            setTimeout(function(){
                lottery.getLottery();
            },1500);
        });

        //点击切换按钮
        var $menuCls = $machineLottery.find('.menu-switch').find('span');
        $menuCls.on('click', function() {
            var $this = $(this),
                index = $this.index(),
                contentCls = $machineLottery.find('.record-list ul');
            $this.addClass('active').siblings().removeClass('active');
            contentCls.eq(index).show().siblings().hide();
        });

        lottery.giftRecord();
        lottery.myGift();
        lottery.hoverScrollList($('#lotteryList').find('.user-record'),7);
    });
});