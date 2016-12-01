require(['jquery', 'layerWrapper', 'template', 'jquery.ajax.extension'], function($, layer, tpl) {
    $(function() {

        var $lotteryList = $('#lotteryList'),
            scrollTimer,
            lottery = {
                click: false, //防止重复点击
                index: -1, //当前转动到哪个位置，起点位置
                count: 0, //总共有多少个位置
                timer: 0, //setTimeout的ID，用clearTimeout清除
                speed: 20, //初始转动速度
                times: 0, //转动次数
                cycle: 50, //转动基本次数：即至少需要转动多少次再进入抽奖环节
                prize: -1, //中奖位置
                init: function(id) {
                    if ($("#" + id).find(".lottery-unit").length > 0) {
                        $lottery = $("#" + id);
                        $units = $lottery.find(".lottery-unit");
                        this.obj = $lottery;
                        this.count = $units.length;
                        $lottery.find(".lottery-unit-" + this.index).addClass("active");
                    };
                },
                roll: function() {
                    var index = this.index,
                        count = this.count,
                        lottery = this.obj;
                    $(lottery).find(".lottery-unit-" + index).removeClass("active");
                    index += 1;
                    if (index > count - 1) {
                        index = 0;
                    };
                    $(lottery).find(".lottery-unit-" + index).addClass("active");
                    this.index = index;
                    return false;
                },
                stop: function(index) {
                    this.prize = index;
                    return false;
                }
            };

        function roll() {
            lottery.times += 1;
            lottery.roll(); //转动过程调用的是lottery的roll方法，这里是第一次调用初始化
            if (lottery.times > lottery.cycle + 10 && lottery.prize == lottery.index) {
                clearTimeout(lottery.timer);
                lottery.prize = -1;
                lottery.times = 0;
                lottery.click = false;

                
                setTimeout(function() {
                    layer.open({
                        type: 1,
                        title: false,
                        closeBtn: 0,
                        content: $('#lotteryTip')
                    });
                }, 1000);

            } else {
                if (lottery.times < lottery.cycle) {
                    lottery.speed -= 10;
                } else if (lottery.times == lottery.cycle) {
                    lottery.prize = lottery.prize;
                } else {
                    if (lottery.times > lottery.cycle + 10 && ((lottery.prize == 0 && lottery.index == 7) || lottery.prize == lottery.index + 1)) {
                        lottery.speed += 110;
                    } else {
                        lottery.speed += 20;
                    }
                }
                if (lottery.speed < 40) {
                    lottery.speed = 40;
                };
                lottery.timer = setTimeout(roll, lottery.speed); //循环调用
            }
            return false;
        }


        lottery.init('lottery');
        //get gift
        $("#lottery .lottery-btn").on('click', function(event) {
            event.preventDefault();
            $.ajax({
                    url: '/activity/isLogin',
                    type: 'GET',
                    dataType: 'json',
                    contentType: 'text/html; charset=UTF-8'
                })
                .done(function(response) {
                    if (response.responseText != "") {
                        $("meta[name='_csrf']").remove();
                        $('head').append($(response.responseText));
                        var token = $("meta[name='_csrf']").attr("content"),
                            header = $("meta[name='_csrf_header']").attr("content");
                        $(document).ajaxSend(function(e, xhr, options) {
                            xhr.setRequestHeader(header, token);
                        });
                        layer.open({
                            type: 1,
                            title: false,
                            closeBtn: 0,
                            area: ['auto', 'auto'],
                            content: $('#loginTip')
                        });
                        
                    } else {
                        if (lottery.click) {
                            return false;
                        } else {
                            $.ajax({
                                    url: '/activity/point-draw/task-draw',
                                    type: 'POST',
                                    dataType: 'json',
                                    data: {
                                        activityCategory: 'CARNIVAL_ACTIVITY'
                                    }
                                })
                                .done(function(data) {

                                    if (data.returnCode == 1) {
                                        $('#lotteryTip').html(tpl('lotteryTipTpl', data));
                                        layer.open({
                                            type: 1,
                                            title: false,
                                            closeBtn: 0,
                                            content: $('#lotteryTip')
                                        });
                                    } else if (data.returnCode == 0) {
                                        $('#lotteryTip').html(tpl('lotteryTipTpl', data));
                                        switch (data.prize) {
                                            case 'M1_PHONE': //锤子M1手机
                                                lottery.prize = 7;
                                                break;
                                            case 'HUMIDIFIER': //小熊加湿器
                                                lottery.prize = 0;
                                                break;
                                            case 'HAIR_DRIER': //飞科电吹风机
                                                lottery.prize = 1;
                                                break;
                                            case 'IQIYI_MEMBERSHIP_REF_CARNIVAL': //爱奇艺会员
                                                lottery.prize = 5;
                                                break;
                                            case 'TELEPHONE_FARE_10_REF_CARNIVAL': //10元话费
                                                lottery.prize = 2;
                                                break;
                                            case 'BAMBOO_CHARCOAL_PACKAGE': //卡通汽车竹炭包
                                                lottery.prize = 6;
                                                break;
                                            case 'INTEREST_COUPON_5_POINT_DRAW_REF_CARNIVAL': //0.5加息券
                                                lottery.prize = 3;
                                                break;
                                            case 'RED_ENVELOPE_50_POINT_DRAW_REF_CARNIVAL': //50元红包
                                                lottery.prize = 4;
                                                break;
                                        }
                                        lottery.speed = 100;
                                        roll();
                                        lottery.click = true;
                                    } else if (data.returnCode == 3) {
                                        $('#lotteryTip').html(tpl('lotteryTipTpl', data));
                                        layer.open({
                                            type: 1,
                                            title: false,
                                            closeBtn: 0,
                                            content: $('#lotteryTip')
                                        });
                                    }
                                })
                                .fail(function() {
                                    layer.msg('抽奖失败，请重试');
                                });

                        }
                    }
                });

        });

        

        //close layer
        $('#lotteryTip').on('click', '.close-item', function(event) {
            event.preventDefault();
            layer.closeAll();
        });


    });
});