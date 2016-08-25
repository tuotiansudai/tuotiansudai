/**
 * [name]:抽奖转盘组件
 * [user]:xuqiang
 * [date]:2016-08-24
 */
define(['jquery', 'rotate', 'layerWrapper','template', 'jquery.validate', 'jquery.validate.extension', 'jquery.ajax.extension'], function($, rotate, layer,tpl) {
    var bRotate = false,
        $pointer = $('#pointer'),
        $rotate = $('#rotate'),
        $RecordBtn = $('.gift-record li');

    
    //change award record btn
    $RecordBtn.on('click', function(event) {
        var $self = $(this),
            index = $self.index();
        $self.addClass('active').siblings('li').removeClass('active');
        $('#recordList').find('.record-model:eq(' + index + ')').addClass('active')
            .siblings('.record-model').removeClass('active');
    });

    //td click
    $pointer.on('click', function(event) {
        event.preventDefault();

            if (bRotate) return;
            $.ajax({
                    url: '/activity/draw-lottery',
                    type: 'POST',
                    dataType: 'json'
                })
                .done(function(data) {
                    console.log(data);
                    if (data.returnCode == 0) {
                        var item = data.lotteryPrize;
                        switch (item) {
                            case 'INTEREST_COUPON_2':
                                rotateFn(0, 56, '0.2加息券');
                                break;
                            case 'LUXURY':
                                rotateFn(0, 56, '奢侈品大奖');
                                break;
                            case 'RED_ENVELOPE_100':
                                rotateFn(1, 120, '100元现金红包');
                                break;
                            case 'INTEREST_COUPON_5':
                                rotateFn(2, 200, '0.5加息券');
                                break;
                            case 'RED_ENVELOPE_50':
                                rotateFn(3, 260, '50元现金红包');
                                break;
                            case 'PORCELAIN_CUP':
                                rotateFn(4, 337, '青花瓷杯子');
                                break;
                        }
                    } else if (data.returnCode == 2) {
                        $('#tipList').html(tpl('tipListTpl', {tiptext:data.message})).show().find('.tip-dom').show();
                    } else {
                        $('#tipList').html(tpl('tipListTpl', {tiptext:data.message})).show().find('.tip-dom').show();
                    }
                })
                .fail(function() {
                    layer.msg('请求失败');
                });
    });

    function rotateFn(awards, angles, txt) {
        bRotate = !bRotate;
        $('#rotate').stopRotate();
        $('#rotate').rotate({
            angle: 0,
            animateTo: angles + 1800,
            duration: 8000,
            callback: function() {
                $('#tipList').html(tpl('tipListTpl', {tiptext:'恭喜你抽中了'+txt})).show().find('.tip-dom').show();
                bRotate = !bRotate;
            }
        })
    }
    //close btn
    $('body').on('click', '.go-close', function(event) {
        event.preventDefault();
        var $self = $(this),
            $parent = $self.parents('.tip-list'),
            $tipDom = $parent.find('.tip-dom');
        $parent.hide();
        $tipDom.hide();
    });


    //scroll award record list
    var scrollTimer;
    $(".user-record").hover(function() {
        clearInterval(scrollTimer);
    }, function() {
        scrollTimer = setInterval(function() {
            scrollNews($("#recordList"));
        }, 2000);
    }).trigger("mouseout");

    function scrollNews(obj) {
        var $self = obj.find("ul.user-record");
        var lineHeight = $self.find("li:first").height();
        if ($self.find('li').length > 10) {
            $self.animate({
                "margin-top": -lineHeight + "px"
            }, 600, function() {
                $self.css({
                    "margin-top": "0px"
                }).find("li:first").appendTo($self);
            })
        }
    }

    var rankList=function (){
        $.ajax({
            url: '/activity/getTianDouTop15',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            $('#rankList').html(tpl('rankListTpl', data));
        });
    };
    var GiftRecord=function (){
        $.ajax({
            url: '/activity/lottery-record-list',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            $('#GiftRecord').html(tpl('GiftRecordTpl', {record:data}));
        });
    }();
    
    var MyGift=function (){
        $.ajax({
            url: '/activity/lottery-record-list',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            $('#MyGift').html(tpl('MyGiftTpl', {gift:data}));
        });
    }();
});