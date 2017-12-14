define([], function () {
    require('pointStyle/module/_nine_lottery.scss');
    require('pointStyle/module/_gift_circle_tip.scss');
    let tpl = require('art-template/dist/template');
    let commonFun = require('publicJs/commonFun');
    var $pointContainerFrame = $('#pointContainer'),
        tipGroupObj = {};
    //点击切换按钮
    $('.lottery-right-group').find('h3 span').on('click', function () {
        var $this = $(this),
            index = $this.index(),
            contentCls = $(this).parent().siblings('.record-list').find('.record-item');
        $this.addClass('active').siblings().removeClass('active');
        contentCls.eq(index).show().siblings('.record-item').hide();
    });
    //遍历弹框dom节点
    $pointContainerFrame.find('.tip-list-frame .tip-list').each(function (key, option) {
        var kind = $(option).attr('id');
        tipGroupObj[kind] = option;
    });
    var lottery = {
        prizeType: '',
        prizeKind: 0,
        click: false,
        index: -1,    //当前转动到哪个位置，起点位置
        count: 0,    //总共有多少个位置
        timer: 0,    //setTimeout的ID，用clearTimeout清除
        speed: 20,    //初始转动速度
        times: 0,    //转动次数
        cycle: 50,    //转动基本次数：即至少需要转动多少次再进入抽奖环节
        prize: -1,    //中奖位置
        init: function (id) {
            if ($("#" + id).find(".lottery-unit").length > 0) {
                $lottery = $("#" + id);
                $units = $lottery.find(".lottery-unit");
                this.obj = $lottery;
                this.count = $units.length;
                $lottery.find(".lottery-unit-" + this.index).addClass("active");
            }
            ;
        },
        roll: function () {
            var index = this.index;
            var count = this.count;
            var lottery = this.obj;
            $(lottery).find(".lottery-unit-" + index).removeClass("active");
            index += 1;
            if (index > count - 1) {
                index = 0;
            }
            ;
            $(lottery).find(".lottery-unit-" + index).addClass("active");
            this.index = index;
            return false;
        },
        stop: function () {
            lottery.times += 1;
            lottery.roll();//转动过程调用的是lottery的roll方法，这里是第一次调用初始化
            if (lottery.times > lottery.cycle + 10 && lottery.prize == lottery.index) {
                clearTimeout(lottery.timer);
                lottery.prize = -1;
                lottery.times = 0;
                lottery.click = false;
                layer.open({
                    type: 1,
                    closeBtn: 0,
                    move: false,
                    area: ['460px', '370px'],
                    title: false,
                    content: $(lottery.prizeType)
                });
                lottery.giftRecord();
                lottery.myGift();
            } else {
                if (lottery.times < lottery.cycle) {
                    lottery.speed -= 10;
                } else if (lottery.times == lottery.cycle) {
                    lottery.prize = lottery.prizeKind;
                } else {
                    if (lottery.times > lottery.cycle + 10 && ((lottery.prize == 0 && lottery.index == 7) || lottery.prize == lottery.index + 1)) {
                        lottery.speed += 110;
                    } else {
                        lottery.speed += 20;
                    }
                }
                if (lottery.speed < 40) {
                    lottery.speed = 40;
                }
                ;
                lottery.timer = setTimeout(lottery.stop, lottery.speed);//循环调用
            }
            return false;
        },
        getLottery: function () {
            commonFun.useAjax({
                url: '/activity/point-draw/draw',
                data: {'activityCategory': 'POINT_SHOP_DRAW_1000'},
                type: 'POST'
            }, function (data) {
                if (data.returnCode == 0) {

                    switch (data.prize) {
                        case 'POINT_SHOP_INTEREST_COUPON_2': //0.2加息券
                            lottery.prizeKind = 7;
                            break;
                        case 'POINT_SHOP_RED_ENVELOPE_10': //10元投资红包
                            lottery.prizeKind = 0;
                            break;
                        case 'POINT_SHOP_POINT_500': //500积分
                            lottery.prizeKind = 1;
                            break;
                        case 'POINT_SHOP_PHONE_CHARGE_10': //10元话费
                            lottery.prizeKind = 5;
                            break;
                        case 'OINT_SHOP_JD_100': //100元京东卡
                            lottery.prizeKind = 2;
                            break;
                        case 'POINT_SHOP_POINT_3000': //3000积分
                            lottery.prizeKind = 6;
                            break;
                        case 'POINT_SHOP_INTEREST_COUPON_5': //0.5加息券
                            lottery.prizeKind = 4;
                            break;
                        case 'POINT_SHOP_RED_ENVELOPE_50': //50元投资红包
                            lottery.prizeKind = 3;
                            break;
                    }

                    var prizeType = data.prizeType.toLowerCase();
                    $(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);
                    lottery.speed = 100;
                    lottery.stop();
                    lottery.click = true;
                    lottery.prizeType = $(tipGroupObj[prizeType]);

                } else if (data.returnCode == 1) {
                    //没有抽奖机会
                    layer.open({
                        type: 1,
                        closeBtn: 0,
                        move: false,
                        area: ['460px', '370px'],
                        title: false,
                        content: $('#nochance')
                    });
                } else if (data.returnCode == 2) {
                    //未登录
                    location.href = '/login';

                } else if (data.returnCode == 3) {
                    //不在活动时间范围内！
                    layer.open({
                        type: 1,
                        closeBtn: 0,
                        move: false,
                        area: ['460px', '370px'],
                        title: false,
                        content: $('#expired')
                    });
                } else if (data.returnCode == 4) {
                    //实名认证
                    layer.open({
                        type: 1,
                        closeBtn: 0,
                        move: false,
                        area: ['460px', '370px'],
                        title: false,
                        content: $('#authentication')
                    });
                } else if (data.returnCode == 5) {
                    layer.open({
                        type: 1,
                        closeBtn: 0,
                        move: false,
                        area: ['460px', '370px'],
                        title: false,
                        content: $('#frequentOperation')
                    });
                } else if (data.returnCode == 6) {
                    layer.open({
                        type: 1,
                        closeBtn: 0,
                        move: false,
                        area: ['460px', '370px'],
                        title: false,
                        content: $('#pointChangingFail')
                    });
                }
            });
        },
        giftRecord: function () {
            commonFun.useAjax({
                url: '/activity/point-draw/all-list',
                data: {
                    'activityCategory': 'POINT_SHOP_DRAW_1000'
                },
                type: 'GET',
            }, function (data) {
                $('#recordList').html(tpl('recordListTpl', {'recordlist': data}));
            });
        },
        myGift: function () {
            commonFun.useAjax({
                url: '/activity/point-draw/user-list',
                data: {
                    'activityCategory': 'POINT_SHOP_DRAW_1000'
                },
                type: 'GET',
            }, function (data) {
                $('#myRecord').html(tpl('myRecordTpl', {'myrecord': data}));
            });
        },
        scrollList: function (domName, length) {
            var $self = domName;
            var lineHeight = $self.find("li:first").height();
            if ($self.find('li').length > (length != '' ? length : 10)) {
                $self.animate({
                    "margin-top": -lineHeight + "px"
                }, 600, function () {
                    $self.css({
                        "margin-top": "0px"
                    }).find("li:first").appendTo($self);
                });
            }
        },
        hoverScrollList: function (domName, length) {
            var _this = this,
                scrollTimer;
            domName.hover(function () {
                clearInterval(scrollTimer);
            }, function () {
                scrollTimer = setInterval(function () {
                    _this.scrollList(domName, length);
                }, 2000);
            }).trigger("mouseout");
        }
    };

    $('#lotteryBox .lottery-btn').on('click', function (event) {
        event.preventDefault();
        lottery.init('lotteryBox');
        if (lottery.click) {
            return false;
        } else {
            lottery.getLottery();
            return false;
        }
    });
    //关闭弹框
    $('.tip-list').on('click', '.go-close', function (event) {
        event.preventDefault();
        layer.closeAll();
        location.reload();
    });

    lottery.giftRecord();
    lottery.myGift();
    lottery.hoverScrollList($('#lotteryList').find('.user-record'), 10);

});



