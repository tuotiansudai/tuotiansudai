require(['jquery', 'rotate', 'layerWrapper','template', 'jquery.validate', 'jquery.validate.extension', 'jquery.ajax.extension'], function($, rotate, layer,tpl) {
    var bRotateTd = false,
        bRotateCd = false,
        bRotateTdPhone = false,
        bRotateCdPhone = false,
        $beanBtn = $('#beanBtn li'),
        $awardBtn = $('#awardBtn li'),
        $pointerTd = $('#pointerTd'),
        $pointerCd = $('#pointerCd'),
        $rotateTd = $('#rotateTd'),
        $rotateCd = $('#rotateCd'),
        $pointerTdPhone = $('#pointerTdPhone'),
        $pointerCdPhone = $('#pointerCdPhone'),
        $rotateTdPhone = $('#rotateTdPhone'),
        $rotateCdPhone = $('#rotateCdPhone'),
        $tdgiftRecord = $('#tdChangeBtn li'),
        $cdgiftRecord = $('#cdChangeBtn li');

    $('#linePro').height(Math.round($('#linePro').attr('data-totalInvest')) / 1000000 / 12000 * 600);
    $('#lineProPhone').height(Math.round($('#lineProPhone').attr('data-totalInvest')) / 1000000 / 12000 * 300);
    //change rank list
    $beanBtn.on('click', function(event) {
        var $self = $(this),
            index = $self.index();
        $self.addClass('active').siblings('li').removeClass('active');
        $('#beanCom').find('.leader-list:eq(' + index + ')').addClass('active')
            .siblings('.leader-list').removeClass('active');
    });

    //change award list
    $awardBtn.on('click', function(event) {
        var $self = $(this),
            index = $self.index();
        $self.addClass('active').siblings('li').removeClass('active');
        $('#awardCom').find('.leader-list:eq(' + index + ')').addClass('active')
            .siblings('.leader-list').removeClass('active');
    });

    //change award record btn
    $tdgiftRecord.on('click', function(event) {
        var $self = $(this),
            index = $self.index();
        $self.addClass('active').siblings('li').removeClass('active');
        $('#recordList').find('.record-model:eq(' + index + ')').addClass('active')
            .siblings('.record-model').removeClass('active');
    });
    //change award record btn
    $cdgiftRecord.on('click', function(event) {
        var $self = $(this),
            index = $self.index();
        $self.addClass('active').siblings('li').removeClass('active');
        $('#beanList').find('.record-model:eq(' + index + ')').addClass('active')
            .siblings('.record-model').removeClass('active');
    });
    //td click
    $pointerTd.on('click', function(event) {
        event.preventDefault();
        var $self = $(this),
            isLogin = $self.attr('data-is-login');
        if (isLogin != 'true') {
            $('#tipList').show();
            $('#noLogin').show();
        } else {
            if (bRotateTd) return;
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
                                rotateFnTd(0, 56, '20元现金');
                                break;
                            case 'Iphone6s':
                                rotateFnTd(1, 120, 'iPhone 6s Plus');
                                break;
                            case 'JingDong300':
                                rotateFnTd(2, 200, '300元京东购物卡');
                                break;
                            case 'InterestCoupon5':
                                rotateFnTd(3, 260, '0.5%加息券');
                                break;
                            case 'MacBook':
                                rotateFnTd(4, 337, 'MacBook Air');
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

    function rotateFnTd(awards, angles, txt) {
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
                $('.my-td-bean').each(function(index, el) {
                    $(this).text(Math.round($(this).text().replace(/,/gi, '')) - 1000);
                });
            }
        })
    }
    //cd click
    $pointerCd.on('click', function(event) {
        event.preventDefault();
        var $self = $(this),
            isLogin = $self.attr('data-is-login');
        if (isLogin != 'true') {
            $('#tipList').show();
            $('#noLogin').show();
        } else {
            if (bRotateCd) return;
            $.ajax({
                    url: '/activity/point-lottery',
                    type: 'POST',
                    dataType: 'json'
                })
                .done(function(data) {
                    if (bRotateCd) return;
                    switch (data) {
                        case 'PointNotEnough':
                            $('#tipList').show();
                            $('#NoCdbean').show();
                            break;
                        case 'Cash5':
                            rotateFnCd(1, 80, '现金5元');
                            break;
                        case 'AlreadyLotteryNotShare':
                            $('#tipList').show();
                            $('#oneDay').show();
                            break;
                        case 'ThankYou':
                            rotateFnCd(3, 173, '谢谢参与');
                            break;
                        case 'Cash2':
                            rotateFnCd(4, 210, '现金2元');
                            break;
                        case 'InterestCoupon2':
                            rotateFnCd(5, 255, '0.2%加息券');
                            break;
                        case 'AlreadyLotteryShare':
                            $('#tipList').show();
                            $('#onlyTwice').show();
                            break;
                        case 'InvestCoupon3000':
                            rotateFnCd(7, 355, '3000元体验金');
                            break;
                    }
                })
                .fail(function() {
                    layer.msg('请求失败');
                });
        }
    });

    function rotateFnCd(awards, angles, txt) {
        bRotateCd = !bRotateCd;
        $('#rotateCd').stopRotate();
        $('#rotateCd').rotate({
            angle: 0,
            animateTo: angles + 1800,
            duration: 8000,
            callback: function() {
                $('#tipList').show();
                PcDataGet();
                switch (awards) {
                    case 1:
                        $('#cdFive').show();
                        break;
                    case 3:
                        $('#thankYou').show();
                        break;
                    case 4:
                        $('#cdTwo').show();
                        break;
                    case 5:
                        $('#percentCoupon').show();
                        break;
                    case 7:
                        $('#freeMoney').show();
                        break;
                }
                bRotateCd = !bRotateCd;
                $('.my-cd-bean').each(function(index, el) {
                    $(this).text(Math.round($(this).text().replace(/,/gi, '')) - 1000);
                });
            }
        })
    };



    //share event
    window._bd_share_config = {
        "common": {
            "bdSize": "32",
            "bdText": "霸道总裁送你钱！车！房！投资拿排名大奖！还能抽奖！百分百中奖哦！",
            "bdPic": staticServer + "/images/sign/actor/ranklist/share-images.png",
            onAfterClick: function(cmd) {
                $.ajax({
                    url: '/activity/get-lottery-chance',
                    type: 'POST',
                    dataType: 'json'
                });
            }
        },
        "share": {}
    };
    with(document) 0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'https://dn-iyz-file.qbox.me/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5)];

    $("#countForm").validate({
        debug: true,
        rules: {
            money: {
                required: true,
                number: true
            },
            month: {
                required: true,
                number: true
            }
        },
        messages: {
            money: {
                required: '请输入投资金额！',
                number: '请输入有效的数字！'
            },
            month: {
                required: '请输入投资时长！',
                number: '请输入有效的数字！'
            }
        },
        submitHandler: function(form) {
            var moneyNum = Math.round($('#moneyNum').val()),
                monthNum = Math.round($('#monthNum').val()),
                $resultNum = $('#resultNum'),
                resultNum = moneyNum * monthNum / 12;
            $resultNum.text(resultNum.toFixed(0));
        },
        errorPlacement: function(error, element) {
            error.insertAfter(element.parent());
        }
    });
    //close calculator
    $('.close-cal').on('click', function(event) {
        event.preventDefault();
        var $self = $(this),
            $calDom = $self.parents('.td-calculator');
        $calDom.slideUp('fast');
    });
    //calculator show
    $('#calBtn').on('click', function(event) {
        event.preventDefault();
        var $self = $(this),
            $calDom = $self.siblings('.td-calculator');
        $calDom.slideDown('fast');
    });
    //reset form
    $("#resetBtn").on('click', function(event) {
        event.preventDefault();
        $('#countForm').find('.int-text').val('');
        $('#resultNum').text('0');
    });
    //go to my TD
    $("#myTD").on('click', function(event) {
        event.preventDefault();
        var $self = $(this),
            myTdH = $('#awardBtn').offset().top;
        $('body,html').animate({
            scrollTop: myTdH
        }, 'fast', function() {
            $('#awardBtn li:eq(0)').trigger('click');
        });
    });
    //go to CD
    $("#myCD").on('click', function(event) {
        event.preventDefault();
        var $self = $(this),
            myTdH = $('#awardBtn').offset().top;
        $('body,html').animate({
            scrollTop: myTdH
        }, 'fast', function() {
            $('#awardBtn li:eq(1)').trigger('click');
        });
    });
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

    $('.change-btn-list').on('click', 'strong', function(event) {
        event.preventDefault();
        var $self = $(this),
            index = $self.index();
        $self.addClass('active').siblings().removeClass('active');
        $('#changeGift').find('.circle-list:eq(' + index + ')').show().siblings().hide();
    });

    function scrollGift(obj) {
        var $self = obj.find("ul");
        var lineHeight = $self.find("li:first").height();
        if ($self.find('li').length > 5) {
            $self.animate({
                "margin-top": -lineHeight + "px"
            }, 600, function() {
                $self.css({
                    "margin-top": "0px"
                }).find("li:first").appendTo($self);
            })
        }
    }
    setInterval(function() {
        $(".gift-record").each(function(index, el) {
            scrollGift($(this));
        });
    }, 2000);

    $('#TdMyGiftPhone').on('click', function(event) {
        event.preventDefault();
        slideGift($(this));
    });
    $('#CdMyGiftPhone').on('click', function(event) {
        event.preventDefault();
        slideGift($(this));
    });
    function slideGift(dom){
        var $dom = $(this),
            $dd = $dom.find('dd');
        if (!$dom.hasClass('active')) {
            $dom.addClass('active');
            $dd.slideDown('fast');
        } else {
            $dom.removeClass('active');
            $dd.slideUp('fast');
        }
    }


    //td click phone
    $pointerTdPhone.on('click', function(event) {
        event.preventDefault();
        var $self = $(this),
            isLogin = $self.attr('data-is-login');
        if (isLogin != 'true') {
            $('#tipListPhone').show();
            $('#noLoginPhone').show();
        } else {
            if (bRotateTdPhone) return;
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
                                rotateFnTdPhone(0, 56, '20元现金');
                                break;
                            case 'Iphone6s':
                                rotateFnTdPhone(1, 120, 'iPhone 6s Plus');
                                break;
                            case 'JingDong300':
                                rotateFnTdPhone(2, 200, '300元京东购物卡');
                                break;
                            case 'InterestCoupon5':
                                rotateFnTdPhone(3, 260, '0.5%加息券');
                                break;
                            case 'MacBook':
                                rotateFnTdPhone(4, 337, 'MacBook Air');
                                break;
                        }
                    } else if (res.data.returnCode == 1) {
                        $('#tipListPhone').show();
                        $('#TDnoUsePhone').show();
                    } else {
                        $('#tipListPhone').show();
                        $('#noLoginPhone').show();
                    }
                })
                .fail(function() {
                    layer.msg('请求失败');
                });
        }
    });

    function rotateFnTdPhone(awards, angles, txt) {
        bRotateTdPhone = !bRotateTdPhone;
        $('#rotateTdPhone').stopRotate();
        $('#rotateTdPhone').rotate({
            angle: 0,
            animateTo: angles + 1800,
            duration: 8000,
            callback: function() {
                $('#tipListPhone').show();
                PcDataGetPhone();
                switch (awards) {
                    case 0:
                        $('#twentyRMBPhone').show();
                        break;
                    case 1:
                        $('#iphone6sPhone').show();
                        break;
                    case 2:
                        $('#jdCardPhone').show();
                        break;
                    case 3:
                        $('#jiaxiPhone').show();
                        break;
                    case 4:
                        $('#macbookAirPhone').show();
                        break;
                }
                bRotateTdPhone = !bRotateTdPhone;
                $('.myphone-td-bean').each(function(index, el) {
                    $(this).text(Math.round($(this).text().replace(/,/gi, '')) - 1000);
                });
            }
        })
    }
    //cd click Phone
    $pointerCdPhone.on('click', function(event) {
        event.preventDefault();
        var $self = $(this),
            isLogin = $self.attr('data-is-login');
        if (isLogin != 'true') {
            $('#tipListPhone').show();
            $('#noLoginPhone').show();
        } else {
            if (bRotateCdPhone) return;
            $.ajax({
                    url: '/activity/point-lottery',
                    type: 'POST',
                    dataType: 'json'
                })
                .done(function(data) {
                    if (bRotateCdPhone) return;
                    switch (data) {
                        case 'PointNotEnough':
                            $('#tipListPhone').show();
                            $('#NoCdbeanPhone').show();
                            break;
                        case 'Cash5':
                            rotateFnCdPhone(1, 80, '现金5元');
                            break;
                        case 'AlreadyLotteryNotShare':
                            $('#tipListPhone').show();
                            $('#oneDayPhone').show();
                            break;
                        case 'ThankYou':
                            rotateFnCdPhone(3, 173, '谢谢参与');
                            break;
                        case 'Cash2':
                            rotateFnCdPhone(4, 210, '现金2元');
                            break;
                        case 'InterestCoupon2':
                            rotateFnCdPhone(5, 255, '0.2%加息券');
                            break;
                        case 'AlreadyLotteryShare':
                            $('#tipListPhone').show();
                            $('#onlyTwicePhone').show();
                            break;
                        case 'InvestCoupon3000':
                            rotateFnCdPhone(7, 355, '3000元体验金');
                            break;
                    }
                })
                .fail(function() {
                    layer.msg('请求失败');
                });
        }
    });

    function rotateFnCdPhone(awards, angles, txt) {
        bRotateCdPhone = !bRotateCdPhone;
        $('#rotateCdPhone').stopRotate();
        $('#rotateCdPhone').rotate({
            angle: 0,
            animateTo: angles + 1800,
            duration: 8000,
            callback: function() {
                $('#tipListPhone').show();
                PcDataGetPhone();
                switch (awards) {
                    case 1:
                        $('#cdFivePhone').show();
                        break;
                    case 3:
                        $('#thankYouPhone').show();
                        break;
                    case 4:
                        $('#cdTwoPhone').show();
                        break;
                    case 5:
                        $('#percentCouponPhone').show();
                        break;
                    case 7:
                        $('#freeMoneyPhone').show();
                        break;
                }
                bRotateCdPhone = !bRotateCdPhone;
                $('.myphone-cd-bean').each(function(index, el) {
                    $(this).text(Math.round($(this).text().replace(/,/gi, '')) - 1000);
                });
            }
        })
    };
    //go to my TD Phone
    $("#myTDPhone").on('click', function(event) {
        event.preventDefault();
        var $self = $(this),
            myTdH = $('#awardBtnPhone').offset().top;
        $('body,html').animate({
            scrollTop: myTdH
        }, 'fast', function() {
            $('#awardBtnPhone strong:eq(0)').trigger('click');
        });
    });
    //go to CD Phone
    $("#myCDPhone").on('click', function(event) {
        event.preventDefault();
        var $self = $(this),
            myTdH = $('#awardBtnPhone').offset().top;
        $('body,html').animate({
            scrollTop: myTdH
        }, 'fast', function() {
            $('#awardBtnPhone strong:eq(1)').trigger('click');
        });
    });
    function rankList(){
        $.ajax({
            url: '/activity/getTianDouTop15',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            $.each(data, function(index, val) {
                val.loginName=val.loginName.slice(0,3)+'******';
            });
            var list={rank:data};
            $('#rankList').html(tpl('rankListTpl', list));
        });
    }
    function TdGiftRecord(){
        $.ajax({
            url: '/activity/getTianDouPrizeList',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            $.each(data.other, function(index, val) {
                val.loginName=val.loginName.slice(0,3)+'******';
            });
            $('#TdGiftRecord').html(tpl('TdGiftRecordTpl', data));
        });
    }
    
    function TdMyGift(){
        $.ajax({
            url: '/activity/getMyTianDouPrize',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            var list={tdmygift:data};
            $('#TdMyGift').html(tpl('TdMyGiftTpl', list));
        });
    }
    
    function CdGiftRecord(){
        $.ajax({
            url: '/activity/getPointPrizeList',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            $.each(data, function(index, val) {
                val.loginName=val.loginName.slice(0,3)+'******';
            });
            var list={cdgiftrecord:data};
            $('#CdGiftRecord').html(tpl('CdGiftRecordTpl', list));
        });
    }
    
    function CdMyGift(){
        $.ajax({
            url: '/activity/getMyPointPrize',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            var list={cdmygift:data};
            $('#CdMyGift').html(tpl('CdMyGiftTpl', list));
        });
    }
    
    function PcDataGet(){
        rankList();
        TdGiftRecord();
        TdMyGift();
        CdGiftRecord();
        CdMyGift();
    }
    PcDataGet();
    function rankListPhone(){
        $.ajax({
            url: '/activity/getTianDouTop15',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            $.each(data, function(index, val) {
                val.loginName=val.loginName.slice(0,3)+'******';
            });
            var list={rank:data};
            $('#rankListPhone').html(tpl('rankListPhoneTpl', list));
        });
    }
    function TdGiftRecordPhone(){
        $.ajax({
            url: '/activity/getTianDouPrizeList',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            $.each(data.other, function(index, val) {
                val.loginName=val.loginName.slice(0,3)+'******';
            });
            $('#TdGiftRecordPhone').html(tpl('TdGiftRecordPhoneTpl', data));
        });
    }
    
    function TdMyGiftPhone(){
        $.ajax({
            url: '/activity/getMyTianDouPrize',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            var list={tdmygift:data};
            $('#TdMyGiftPhone').length>0?$('#TdMyGiftPhone').html(tpl('TdMyGiftPhoneTpl', list)):false;
        });
    }
    
    function CdGiftRecordPhone(){
        $.ajax({
            url: '/activity/getPointPrizeList',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            $.each(data, function(index, val) {
                val.loginName=val.loginName.slice(0,3)+'******';
            });
            var list={cdgiftrecord:data};
            $('#CdGiftRecordPhone').html(tpl('CdGiftRecordPhoneTpl', list));
        });
    }
    
    function CdMyGiftPhone(){
        $.ajax({
            url: '/activity/getMyPointPrize',
            type: 'POST',
            dataType: 'json'
        })
        .done(function(data) {
            var list={cdmygift:data};
            $('#CdMyGiftPhone').length>0?$('#CdMyGiftPhone').html(tpl('CdMyGiftPhoneTpl', list)):false;
        });
    }
    
    function PcDataGetPhone(){
        rankListPhone();
        TdGiftRecordPhone();
        TdMyGiftPhone();
        CdGiftRecordPhone();
        CdMyGiftPhone();
    }
    PcDataGetPhone();

});