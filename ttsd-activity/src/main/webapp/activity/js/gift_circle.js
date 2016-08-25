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
        var $self = $(this),
            isLogin = $self.attr('data-islogin');
        if (isLogin != 'true') {
            $('#tipList').show();
            $('#noLogin').show();
        } else {
            if (bRotate) return;
            $.ajax({
                    url: '/activity/draw-tiandou',
                    type: 'POST',
                    dataType: 'json'
                })
                .done(function(res) {
                    if (res.data.returnCode == 0) {
                        var item = res.data.tianDouPrize;
                        switch (item) {
                            case 'Cash20':
                                rotateFn(0, 56, '20元现金');
                                break;
                            case 'Iphone6s':
                                rotateFn(1, 120, 'iPhone 6s Plus');
                                break;
                            case 'JingDong300':
                                rotateFn(2, 200, '300元京东购物卡');
                                break;
                            case 'InterestCoupon5':
                                rotateFn(3, 260, '0.5%加息券');
                                break;
                            case 'MacBook':
                                rotateFn(4, 337, 'MacBook Air');
                                break;
                        }
                    } else if (res.data.returnCode == 1) {
                        $('#tipList').show();
                        $('#TDnoUse').show();
                    } else {
                        $('#tipList').show();
                        $('#noLogin').show();
                    }
                })
                .fail(function() {
                    layer.msg('请求失败');
                });
        }
    });
    //close btn
    $('body').on('click', '.go-close', function(event) {
        event.preventDefault();
        var $self = $(this),
            $parent = $self.parents('.tip-list'),
            $tipDom = $parent.find('.tip-dom');
        $parent.hide();
        $tipDom.hide();
    });

    function rotateFn(awards, angles, txt) {
        bRotateTd = !bRotateTd;
        $('#rotateTd').stopRotate();
        $('#rotateTd').rotate({
            angle: 0,
            animateTo: angles + 1800,
            duration: 8000,
            callback: function() {
                $('#tipList').show();
                PcDataGet();
                switch (awards) {
                    case 0:
                        $('#twentyRMB').show();
                        break;
                    case 1:
                        $('#iphone6s').show();
                        break;
                    case 2:
                        $('#jdCard').show();
                        break;
                    case 3:
                        $('#jiaxi').show();
                        break;
                    case 4:
                        $('#macbookAir').show();
                        break;
                }
                bRotateTd = !bRotateTd;
            }
        })
    }
    //scroll award record list
    var scrollTimer;
    $(".scroll-record").hover(function() {
        clearInterval(scrollTimer);
    }, function() {
        scrollTimer = setInterval(function() {
            scrollNews($(".scroll-record"));
        }, 2000);
    }).trigger("mouseout");

    function scrollNews(obj) {
        var $self = obj.find("ul.user-record");
        var lineHeight = $self.find("li:first").height();
        if ($self.find('li').length > 15) {
            $self.animate({
                "margin-top": -lineHeight + "px"
            }, 600, function() {
                $self.css({
                    "margin-top": "0px"
                }).find("li:first").appendTo($self);
            })
        }
    }

    function rankList(){
        $.ajax({
            url: '/activity/getTianDouTop15',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            var list={rank:data};
            $('#rankList').html(tpl('rankListTpl', list));
        });
    }
    function GiftRecord(){
        $.ajax({
            url: '/activity/getTianDouPrizeList',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            $('#GiftRecord').html(tpl('GiftRecordTpl', data));
        });
    }
    
    function MyGift(){
        $.ajax({
            url: '/activity/getMyTianDouPrize',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            var list={tdmygift:data};
            $('#MyGift').html(tpl('MyGiftTpl', list));
        });
    }
    function PcDataGet(){
        rankList();
        GiftRecord();
        MyGift();
    }
});