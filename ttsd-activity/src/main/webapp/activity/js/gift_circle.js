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
        var $mobile = $('div.mobile');
        var mobile = $mobile ? $mobile.data('mobile') : '';
        var url = '/activity/autumn/luxury-draw?mobile='+mobile;
        if($('#themeType').val() == 'travel'){
            url = '/activity/autumn/travel-draw?mobile='+mobile;
        }else if($('#themeType').val() == 'national'){
            url = '/activity/national/draw?mobile='+mobile;
        }

        if (bRotate) return;
        $.ajax({
            url: url,
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            if (data.returnCode == 0) {
                switch (data.prize) {
                    case 'PORCELAIN_CUP':
                        rotateFn(0, 337, '青花瓷杯子',data.prizeType);
                        break;
                    case 'INTEREST_COUPON_2':
                        rotateFn(1, 56, '0.2%加息券',data.prizeType);
                        break;
                    case 'LUXURY':
                        rotateFn(2, 116, '奢侈品大奖',data.prizeType);
                        break;
                    case 'RED_ENVELOPE_100':
                        rotateFn(3, 160, '100元现金红包',data.prizeType);
                        break;
                    case 'INTEREST_COUPON_5':
                        rotateFn(4, 230, '0.5%加息券',data.prizeType);
                        break;
                    case 'RED_ENVELOPE_50':
                        rotateFn(5, 300, '50元现金红包',data.prizeType);
                        break;
                    case 'MANGO_CARD_100':
                        rotateFn(6, 347, '100元芒果卡',data.prizeType);
                        break;
                    case 'RED_INVEST_15':
                        rotateFn(7, 30, '15元投资红包',data.prizeType);
                        break;
                    case 'RED_INVEST_50':
                        rotateFn(8, 120, '50元投资红包',data.prizeType);
                        break;
                    case 'TELEPHONE_FARE_10':
                        rotateFn(9, 265, '10元话费',data.prizeType);
                        break;
                    case 'IQIYI_MEMBERSHIP':
                        rotateFn(10, 80, '1个月爱奇艺会员',data.prizeType);
                        break;
                    case 'CINEMA_TICKET':
                        rotateFn(11, 310, '电影票一张',data.prizeType);
                        break;
                    case 'FLOWER_CUP':
                        rotateFn(12, 170, '青花瓷杯子',data.prizeType);
                        break;
                    case 'MEMBERSHIP_V5':
                        rotateFn(13, 347, '1个月V5会员',data.prizeType);
                        break;
                }
            } else if (data.returnCode == 2) {
                $('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'nologin'})).show().find('.tip-dom').show();
            } else if(data.returnCode == 3){
                $('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'timeout'})).show().find('.tip-dom').show();
            } else {
                $('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'notimes'})).show().find('.tip-dom').show();
            }
        })
        .fail(function() {
            layer.msg('请求失败');
        });
    });

    function rotateFn(awards, angles, txt,type) {
        bRotate = !bRotate;
        $('#rotate').stopRotate();
        $('#rotate').rotate({
            angle: 0,
            animateTo: angles + 1800,
            duration: 8000,
            callback: function() {
                console.log(type);
                $('#tipList').html(tpl('tipListTpl', {tiptext:'抽中了'+txt,istype:type})).show().find('.tip-dom').show();
                bRotate = !bRotate;
                $('.lottery-time').each(function(index,el){
                    $(this).text()>1?$(this).text(function(index,num){return parseInt(num)-1}):$(this).text('0');
                });
                GiftRecord();
                MyGift();
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

    function GiftRecord (){
        var url = '/activity/autumn/luxury-all-list';
        if($('#themeType').val() == 'travel'){
            url = '/activity/autumn/travel-all-list';
        }else if($('#themeType').val() == 'national'){
            url = '/activity/national/all-list';
        }
        $.ajax({
            url: url,
            type: 'GET',
            dataType: 'json'
        })
        .done(function(data) {
            $('#GiftRecord').html(tpl('GiftRecordTpl', {record:data}));
        });
    }

    function MyGift(){
        var $mobile = $('div.mobile');
        var mobile = $mobile ? $mobile.data('mobile') : '';
        var url = '/activity/autumn/luxury-user-list?mobile='+mobile;
        if($('#themeType').val() == 'travel'){
            url = '/activity/autumn/travel-user-list?mobile='+mobile;
        }else if($('#themeType').val() == 'national'){
            url = '/activity/national/user-list?mobile='+mobile;
        }
        $.ajax({
            url: url,
            type: 'GET',
            dataType: 'json'
        })
        .done(function(data) {
            $('#MyGift').html(tpl('MyGiftTpl', {gift:data}));
        });
    }
    GiftRecord ();
    MyGift();
});