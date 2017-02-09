require(['jquery', 'pagination', 'mustache', 'text!/tpl/loan-invest-list.mustache', 'layerWrapper', 'underscore', 'fancybox', 'jquery.ajax.extension', 'autoNumeric', 'coupon-alert', 'red-envelope-float', 'jquery.form', 'commonFun','logintip','assign_coupon','anxin_qian'], function ($, pagination, Mustache, investListTemplate, layer, _) {
    var $loanDetail = $('.loan-detail-content'),
        loanId = $('.hid-loan').val(),
        amountInputElement = $(".text-input-amount", $loanDetail),
        $accountInfo = $('.account-info', $loanDetail),
        $btnLookOther = $('.btn-pay', $accountInfo),
        tabs = $('.loan-nav li'),
        $loanList = $('.loan-list', $loanDetail),
        paginationElement = $('.pagination', $loanDetail),
        $errorTip = $('.errorTip'),
        $errorType = $('.errorType'),
        $investSubmit=$('#investSubmit'),
        $noPasswordTips=$('#noPasswordTips'),
        $authorizeAgreement=$('#goAuthorize'),
        $failGoOnBtnInvest=$('.fail_go_on_invest'),
        $successGoOnBtnInvest=$('.success_go_on_invest'),
        $againBtn=$('.again-btn'),
        $authorizeAgreementOptions = $('#authorizeAgreementOptions'),
        noPasswordRemind = amountInputElement.data('no-password-remind'),
        noPasswordInvest = amountInputElement.data('no-password-invest'),
        autoInvestOn = amountInputElement.data('auto-invest-on'),
        $minInvestAmount = $('.text-input-amount').data('min-invest-amount'),
        $isAuthenticationRequired=$('#isAuthenticationRequired'),
        $investForm = $('#investForm');

    var viewport = globalFun.browserRedirect();

    $('.extra-rate .fa-mobile').on('mouseover', function(event) {
        event.preventDefault();
        var $self=$(this);
        layer.tips('APP投资该项目享受最高0.8%年化收益奖励', $self, {
            tips: 3
        });
    });
    function showInputErrorTips(message) {
        layer.tips('<i class="fa fa-times-circle"></i>' + message, '.text-input-amount', {
            tips: [1, '#ff7200'],
            time: 0
        });
    }

    if ($errorTip.length > 0 && $errorTip.text() != '' && $errorType.val() != 'OUT_OF_NOVICE_INVEST_LIMIT') {
        showInputErrorTips($errorTip.text());
    }

    var showLayer = function() {
        layer.open({
            shadeClose: false,
            title: '新手体验特权',
            btn: ['确认'],
            type: 1,
            area: ['500px', 'auto'],
            content: '<p class="pad-m-tb tc">抱歉，您已购买过新手专享产品，无法再次参加该活动。</p>',
            btn1: function () {
                layer.closeAll();
            }
        });
    };

    if ($errorType.val() == 'OUT_OF_NOVICE_INVEST_LIMIT') {
        showLayer();
    }

    var loadInvestData = function() {
        paginationElement.loadPagination({
            index: 1
        }, function(data) {
            if (data.status) {
                data.achievementClass = function () {
                    return function (text, render) {
                        var classMapping = { 'FIRST_INVEST': 'first-icon', 'MAX_AMOUNT': 'max-icon', 'LAST_INVEST': 'last-icon' };
                        return "<i class='" + classMapping[render(text)] + "'></i>";
                    }
                };
                var html = Mustache.render(investListTemplate, data);
                $('.loan-list-con table').html(html);
            }
        });
    };

    // tab select
    $loanList.find('.loan-list-con').eq(0).show();
    tabs.click(function() {
        var self = $(this);
        self.addClass('active').siblings('li').removeClass('active');
        var index = self.index();
        if (index === 1) {
            loadInvestData();
        }
        $loanList.find('.loan-list-con').eq(index).show().siblings('.loan-list-con').hide();
    });

    if ($loanDetail.data("loan-status") === 'PREHEAT') {
        var countdown = $loanDetail.data('loan-countdown');
        window.setInterval(function() {
            var day = 0,
                hour = 0,
                minute = 0,
                second = 0;
            if (countdown <= 1800) {
                if (countdown > 0) {
                    minute = Math.floor(countdown / 60) - (day * 24 * 60) - (hour * 60);
                    second = Math.floor(countdown) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
                } else {
                    $btnLookOther.prop('disabled', false);
                    $btnLookOther.html('马上投资');
                    $accountInfo.find('.time-item').remove();
                    $accountInfo.find('.invest-amount').show();
                    $accountInfo.find('.experience-ticket').show();
                    $accountInfo.find('.expected-interest-dd').show();
                }
                if (minute <= 9) minute = '0' + minute;
                if (second <= 9) second = '0' + second;
                $('#minute_show').html(minute + '分');
                $('#second_show').html(second + '秒');
                countdown--;
            }
        }, 1000);
    }

    if (amountInputElement.length) {
        amountInputElement.autoNumeric("init");
        amountInputElement.focus(function() {
            layer.closeAll('tips');
        });

        var isInvestor = 'INVESTOR' === $loanDetail.data('user-role');
        var isAuthentication = 'USER' === $loanDetail.data('authentication');
        var $ticketList = $('.ticket-list');
        var $useExperienceTicket = $('#use-experience-ticket');
        var $couponExpectedInterest = $(".experience-income");

        var getInvestAmount = function() {
            var amount = 0;
            if (!isNaN(amountInputElement.autoNumeric("get"))) {
                amount = parseInt((amountInputElement.autoNumeric("get") * 100).toFixed(0));
            }

            return amount;
        };

        var refreshCouponStatus = function() {
            var investAmount = getInvestAmount();
            $.each($ticketList.find("li"), function(index, ticket) {
                var self = $(ticket);
                var input = $(self.find("input"));
                var productTypeEnable = self.data("product-type-usable");
                var investLowerLimit = $(self.find(".ticket-term.lower-limit")).data('invest-lower-limit') || 0;
                var disabled = !productTypeEnable || (investLowerLimit > 0 && investLowerLimit > investAmount);
                input.prop("disabled", disabled);
                disabled ? self.addClass('disabled') : self.removeClass('disabled');
            });

            var notSharedRedEnvelopes = _.groupBy($ticketList.find("li[data-coupon-type='RED_ENVELOPE'][data-product-type-usable='true']"), function(ticket) {
                return $(ticket).hasClass('disabled') ? "disabled" : "enabled";
            });

            var birthdayCoupon = $ticketList.find("li[data-coupon-type='BIRTHDAY_COUPON'][data-product-type-usable='true']");

            var newbieCoupons = _.groupBy($ticketList.find("li[data-coupon-type='NEWBIE_COUPON'][data-product-type-usable='true']"), function(ticket) {
                return $(ticket).hasClass('disabled') ? "disabled" : "enabled";
            });

            var investCoupons = _.groupBy($ticketList.find("li[data-coupon-type='INVEST_COUPON'][data-product-type-usable='true']"), function(ticket) {
                return $(ticket).hasClass('disabled') ? "disabled" : "enabled";
            });

            var interestCoupons = _.groupBy($ticketList.find("li[data-coupon-type='INTEREST_COUPON'][data-product-type-usable='true']"), function(ticket) {
                return $(ticket).hasClass('disabled') ? "disabled" : "enabled";
            });

            var productTypeDisableCoupons = $ticketList.find("li[data-product-type-usable='false']");

            $ticketList.empty();

            if (notSharedRedEnvelopes['enabled']) {
                $ticketList.append(_.sortBy(notSharedRedEnvelopes['enabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-end-time")).getTime();
                }));
            }

            if (birthdayCoupon.length > 0) {
                $ticketList.append(birthdayCoupon);
            }

            if (newbieCoupons['enabled']) {
                $ticketList.append(_.sortBy(newbieCoupons['enabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-end-time")).getTime();
                }));
            }

            if (investCoupons['enabled']) {
                $ticketList.append(_.sortBy(investCoupons['enabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-end-time")).getTime();
                }));
            }

            if (interestCoupons['enabled']) {
                $ticketList.append(_.sortBy(interestCoupons['enabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-end-time")).getTime();
                }));
            }

            if (notSharedRedEnvelopes['disabled']) {
                $ticketList.append(_.sortBy(notSharedRedEnvelopes['disabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-end-time")).getTime();
                }));
            }

            if (newbieCoupons['disabled']) {
                $ticketList.append(_.sortBy(newbieCoupons['disabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-end-time")).getTime();
                }));
            }

            if (investCoupons['disabled']) {
                $ticketList.append(_.sortBy(investCoupons['disabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-end-time")).getTime();
                }));
            }

            if (interestCoupons['disabled']) {
                $ticketList.append(_.sortBy(interestCoupons['disabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-end-time")).getTime();
                }));
            }

            if (productTypeDisableCoupons.length > 0) {
                $ticketList.append(_.sortBy(productTypeDisableCoupons, function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-end-time")).getTime();
                }));
            }

            $ticketList.find('li').click(function(event) {
                var couponItem = $(event.currentTarget);
                if (couponItem.hasClass("disabled")) {
                    return false;
                }

                var couponTitle = $.trim(couponItem.find('.ticket-title').text());
                $useExperienceTicket.find('span').text(couponTitle);
                couponItem.find("input").prop('checked', true);
                $couponExpectedInterest.text("");

                calExpectedCouponInterest();
                $ticketList.addClass('hide');
            });
        };

        var validateInvestAmount = function() {
            var amount = getInvestAmount();
            var amountNeedRaised = parseInt($('#investForm').find('input[name=amount]').data("amount-need-raised")) || 0;
            return amount > 0 && amountNeedRaised >= amount;
        };

        var calExpectedCouponInterest = function() {
            var queryParams = [];

            $.each($('input[type="hidden"][name="userCouponIds"]'), function(index, item) {
                queryParams.push({
                    'name': 'couponIds',
                    'value': $(item).data("coupon-id")
                })
            });

            $ticketList.find('li').each(function(index, item) {
                if ($(item).find('input[type="radio"]:checked').length > 0) {
                    queryParams.push({
                        'name': 'couponIds',
                        'value': $(item).data("coupon-id")
                    });
                }
            });

            if (queryParams.length == 0) {
                $couponExpectedInterest.text("");
                return;
            }

            $.ajax({
                url: '/calculate-expected-coupon-interest/loan/' + loanId + '/amount/' + getInvestAmount(),
                data: $.param(queryParams),
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function(amount) {
                $couponExpectedInterest.text("+" + amount);
                $btnLookOther.prop('disabled', false);
            });
        };

        var calExpectedInterest = function() {
            $.ajax({
                url: '/calculate-expected-interest/loan/' + loanId + '/amount/' + getInvestAmount(),
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function(amount) {
                $(".principal-income").text(amount);
            });
        };

        if (isInvestor) {
            calExpectedInterest();
            calExpectedCouponInterest();
        }

        amountInputElement.blur(function() {
            calExpectedInterest();
            $.ajax({
                url: '/loan/' + loanId + '/amount/' + getInvestAmount() + "/max-benefit-user-coupon",
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function(maxBenefitUserCouponId) {
                $ticketList.find('input[type="radio"]:checked').prop('checked', false);
                console.log("calculate max coupon init");
                if (!isNaN(parseInt(maxBenefitUserCouponId))) {
                    var maxBenefitUserCoupon = $("#" + maxBenefitUserCouponId);
                    var couponTitle = $.trim($ticketList.find('li[data-user-coupon-id="' + maxBenefitUserCouponId +'"]').find(".ticket-info .ticket-title").text());
                    $useExperienceTicket.find('span').text(couponTitle);
                    maxBenefitUserCoupon.prop('checked', true);
                    console.log("calculate max coupon finished");
                } else {
                    if (!$useExperienceTicket.hasClass("disabled")) {
                        $useExperienceTicket.find('span').text('请选择优惠券');
                    }
                }
                calExpectedCouponInterest();
            });
        });

        //click invest submit btn
        $investSubmit.on('click', function(event) {
            event.preventDefault();

            $.ajax({
                url: '/isLogin',
                //data:data,
                type: 'GET',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).fail(function (response) {
                    if (response.responseText != "") {
                        $("meta[name='_csrf']").remove();
                        $('head').append($(response.responseText));
                        var token = $("meta[name='_csrf']").attr("content");
                        var header = $("meta[name='_csrf_header']").attr("content");
                        $(document).ajaxSend(function (e, xhr, options) {
                            xhr.setRequestHeader(header, token);
                        });
                        layer.open({
                            type: 1,
                            title: false,
                            closeBtn: 0,
                            area: ['auto', 'auto'],
                            content: $('#loginTip')
                        });
                        $('.image-captcha img').trigger('click');
                    } else {
                        if (isInvestor) {
                            noPasswordRemind || noPasswordInvest ? investSubmit() : markNoPasswordRemind();
                            return;
                        }
                        if (isAuthentication) {
                            location.href = '/register/account';
                        }
                    }

                }
            );
        });

        $useExperienceTicket.click(function(event) {
            var $this = $(this);
            if ($this.hasClass('disabled')) {
                return false;
            }
            $ticketList.toggleClass('hide');
            $this.find('.fa-sort-down').toggleClass('hide').siblings('.fa-sort-up').toggleClass('hide');

            if (!$ticketList.hasClass('hide')) {
                refreshCouponStatus();
            }

            if (event.stopPropagation) {
                event.stopPropagation();
            } else if (window.event) {
                window.event.cancelBubsuble = true;
            }
        });

        amountInputElement.focus(function(event) {
            $ticketList.addClass('hide');
        });

        $('body').click(function(e) {
            var event = e || window.event,
                target = event.srcElement || event.target;

            if (!$ticketList.hasClass('hide') && $(target).parents('.ticket-list').length == 0) {
                $ticketList.addClass('hide');
            }
        });
    }


    //click tip red text
    $noPasswordTips.on('click', function() {
        layer.open({
            type: 1,
            closeBtn: 0,
            skin: 'layer-tip-loanDetail',
            shadeClose: false,
            title: '免密投资',
            btn: ['不开启', '开启'],
            area: ['500px', '160px'],
            content: '<p class="pad-m-tb tc">您可直接开启免密投资，简化投资过程，理财快人一步，是否开启？</p>',
            btn1: function () {
                cnzzPush.trackClick("标的详情页", "推荐免密弹框", "不开启");
                layer.closeAll();
            },
            btn2: function () {
                cnzzPush.trackClick("标的详情页", "推荐免密弹框", "开启");
                if (autoInvestOn) { // 如果开启过免密支付
                    $.ajax({
                            url: '/no-password-invest/enabled',
                            type: 'POST',
                            dataType: 'json'
                        })
                        .done(function () {
                            noPasswordInvest = true;
                            $noPasswordTips.hide();
                            layer.msg('开启成功！');
                        })
                        .fail(function () {
                            layer.msg('开启失败，请重试！');
                        })
                        .complete(function () {
                            layer.closeAll();
                        });
                } else {
                    showAuthorizeAgreementOptions();
                    $authorizeAgreement.submit();
                }
            }
        });
        return false;
    });

    $authorizeAgreementOptions.on('click', '.go-on-btn', function(event) {
        event.preventDefault();
        layer.closeAll();
        location.reload();
    }).on('click', '.again-btn', function(event) {
        event.preventDefault();
        $authorizeAgreement.submit();
    });

    //is tip B1 or tip B2?
    function markNoPasswordRemind(){
        if (!noPasswordRemind) {
            $.ajax({
                    url: '/no-password-invest/mark-remind',
                    type: 'POST',
                    dataType: 'json'
                })
                .done(function () {
                    noPasswordRemind = true;
                });
        }
        layer.open({
            type: 1,
            closeBtn: 0,
            skin: 'layer-tip-loanDetail',
            title: '免密投资',
            shadeClose: false,
            btn: autoInvestOn ? ['继续投资', '开启免密投资'] : ['继续投资', '前往联动优势授权'],
            area: ['500px', '160px'],
            content: '<p class="pad-m-tb tc">推荐您开通免密投资功能，简化投资过程，理财快人一步！</p>',
            btn1: function () {
                cnzzPush.trackClick("标的详情页", "马上投资弹框", "继续投资B");
                investSubmit();
                layer.closeAll();
            },
            btn2: function () {
                cnzzPush.trackClick("标的详情页", "马上投资弹框", autoInvestOn ? "开启免密投资" : "前往联动优势授权");
                if (autoInvestOn) {
                    $.ajax({
                            url: '/no-password-invest/enabled',
                            type: 'POST',
                            dataType: 'json'
                        })
                        .done(function () {
                            noPasswordInvest = true;
                            layer.closeAll();
                            layer.msg('开启成功！', function () {
                                investSubmit();
                            });
                        })
                        .fail(function () {
                            layer.closeAll();
                            layer.msg('开启失败，请重试！');
                        })
                } else {
                    showAuthorizeAgreementOptions();
                    $authorizeAgreement.submit();
                }
            }
        });
    }

    $againBtn.on('click',function(){
        cnzzPush.trackClick("标的详情页","免密异步弹框","重新授权");
    });

    $failGoOnBtnInvest.on('click',function(){
        cnzzPush.trackClick("标的详情页","免密异步弹框","继续投资C1");
    });

    $successGoOnBtnInvest.on('click',function(){
        cnzzPush.trackClick("标的详情页","免密异步弹框","继续投资C2");
    });

    //is tip C
    function showAuthorizeAgreementOptions(){
        layer.closeAll();
        layer.open({
            type: 1,
            skin: 'layer-tip-loanDetail',
            shadeClose:false,
            title: '登录到联动优势支付平台开通免密投资',
            area: ['500px', '290px'],
            content: $authorizeAgreementOptions,
            end:function(){
                location.reload();
            }
        });
    }
    //投资表单请求以及校验
    function investSubmit(){

        if ($investForm.attr('action') === '/invest') {
            if (!isInvestor) {
                location.href = '/login?redirect=' + encodeURIComponent(location.href);
                return false;
            }

            var investAmount = getInvestAmount();

            if (!validateInvestAmount()) {
                showInputErrorTips(investAmount === 0 ? '投资金额不能为0元！' : '投资金额不能大于可投金额！');
                return false;
            }
            var minInvestAmount = parseInt(($minInvestAmount * 100).toFixed(0));
            if(investAmount < minInvestAmount){
                var tipContent = '投资金额小于标的最小投资金额！';
                layer.tips('<i class="fa fa-times-circle"></i>' + tipContent, '.text-input-amount', {
                    tips: [1, '#ff7200'],
                    time: 0,
                    maxWidth : 220
                });
                return false;
            }

            var accountAmount = parseInt($investForm.find('.account-amount').data("user-balance")) || 0;
            if (investAmount > accountAmount) {
                location.href = '/recharge';
                return false;
            }
        }
        amountInputElement.val(amountInputElement.autoNumeric("get"));
        if (noPasswordInvest) {//判断是否开启免密投资
            layer.open({
                type: 1,
                closeBtn: 0,
                skin: 'layer-tip-loanDetail',
                title: '免密投资',
                shadeClose:false,
                btn:['取消', '确认'],
                area: ['300px', '160px'],
                content: '<p class="pad-m-tb tc">确认投资？</p>',
                btn1: function(){
                    cnzzPush.trackClick("67标的详情页","马上投资确认框","取消");
                    layer.closeAll();
                },
                btn2:function(){
                    cnzzPush.trackClick("68标的详情页","马上投资确认框","确认");
                    if($isAuthenticationRequired.val()==='false'){//判断是否开启安心签免验
                        sendSubmitRequest();
                    }else{
                        getSkipPhoneTip();
                        return false;
                    }
                }
            });
            return;
        }
        //正常投资
        if($isAuthenticationRequired.val()=='false'){//判断是否开启安心签免验
            $investForm.submit();
        }else{
            getSkipPhoneTip();
            return false;
        }
    }

    //发送投资提交请求
    function sendSubmitRequest(){
        $investForm.ajaxSubmit({
            dataType: 'json',
            url: '/no-password-invest',
            beforeSubmit: function () {
                console.log("invest start");
                $investSubmit.addClass("loading");
            },
            success: function (response) {
                layer.closeAll();
                $investSubmit.removeClass("loading");
                var data = response.data;
                if (data.status) {
                    location.href = "/invest-success";
                } else if (data.message == '新手标投资已超上限') {
                    showLayer();
                } else {
                    showInputErrorTips(data.message);
                }
            }
        });
    }
    // 投资加息
    (function () {
        var $extraRate = $('#extra-rate');
        if (!$extraRate.length) {
            return false;
        }

        var utils = {
            getSize: function (element, type) {
                return element[type]();
            },
            getOffset: function (element) {
                return element.offset();
            },
            removeElement: function (element) {
                if (element.length) {
                    element.remove();
                }
            },
            getRelativeRate: function (arr, num) {
                var index = _.findLastIndex(__extraRate, function (value) {
                    if (num >= value.minInvestAmount && (value.maxInvestAmount > num || value.maxInvestAmount === 0)) {
                        return true;
                    }
                });
                return index !== -1 ? arr[index].rate : '';
            },
            replace: function (str) {
                return str.replace(/,/g, '');
            }
        };

        var tplFn = _.compose(_.template, function () {
            return $('#extra-rate-popup-tpl').html();
        })();
        var getOffset = _.partial(utils.getOffset, $extraRate);
        var getSize = _.partial(utils.getSize, $extraRate);
        var extraRateWidth = getSize('width');
        var extraRateHeight = getSize('height');
        var css = _.compose(_.partial(function (offset, extraRateHeight) {
            return {
                left: offset.left - 5,
                top: offset.top + extraRateHeight - 10
            }
        }, _, extraRateHeight), getOffset);
        var createPopup = _.partial(function (tpl, css) {
            return $(tpl).css(css).appendTo('body');
        }, _, css());
        var showPopup = _.compose(createPopup, tplFn);
        var removePopup = _.partial(_.compose(utils.removeElement, $), '#extra-rate-popup');

        $extraRate.find('.fa').on({
            mouseover: _.partial(showPopup, {__extraRate: __extraRate}),
            mouseout: function () {
                removePopup();
            }
        });

        var getRelativeRate = _.partial(utils.getRelativeRate, __extraRate);
        var changeHTML = function () {
            var $element = $('[data-extra-rate]');
            return function (rate) {
                $element.html(rate);
            }
        }();
        var addSign = function (rate) {
            if (!rate) {
                return ''
            }
            return '+' + rate;
        };

        $('#investForm').find('.text-input-amount').on('change', _.compose(changeHTML, addSign, getRelativeRate, parseInt, utils.replace, function () {
            return $(this).val()
        })).trigger('change');
    })();

    $.fn.carousel = function () {
        return this.each(function () {
            var $ele = $(this);
            var $leftBtn = $ele.find('.left-button');
            var $rightBtn = $ele.find('.right-button');
            var $scrollEle = $ele.find('.scroll-content > .row');
            var $col = $scrollEle.find('.col');
            var len = $col.length;
            var record = 0;
            var eachShowAmount = $(window).width() > 700 ? 4 : 1;
            var imgNum = $scrollEle.find('img').length,
                moveWid;
            switch (viewport) {
                case 'pc':
                    if (imgNum <= 4) {
                        $rightBtn.addClass('disabled');
                    }
                    moveWid = 200;
                    break;
                case 'mobile':
                    if (imgNum <= 1) {
                        $rightBtn.addClass('disabled');
                    }
                    moveWid = $ele.find('.scroll-content').width();
                    $scrollEle.find('img').width(moveWid);
                    $col.width(moveWid);
                    break;
            }

            $rightBtn.on('click', function () {
                if ($rightBtn.hasClass('disabled')) {
                    return false;
                }
                $rightBtn.add($leftBtn).removeClass('disabled');
                record++;
                if (record >= (len - eachShowAmount)) {
                    $rightBtn.addClass('disabled');
                }
                $scrollEle.stop().animate({
                    marginLeft: (-moveWid - 10) * record
                });
            });
            $leftBtn.on('click', function () {
                if ($leftBtn.hasClass('disabled')) {
                    return false;
                }
                $leftBtn.add($rightBtn).removeClass('disabled');
                record--;
                if (record == 0) {
                    $leftBtn.addClass('disabled');
                }
                $scrollEle.stop().animate({
                    marginLeft: (-moveWid - 10) * record
                });
            });
        });
    };
    $('[scroll-carousel]').carousel().find('.col').fancybox({
        'titlePosition': 'over',
        'cyclic': false,
        'showCloseButton': true,
        'showNavArrows': true,
        'titleFormat': function (title, currentArray, currentIndex, currentOpts) {
            return '';
        }
    });

    (function () {
        var maybe = function (value) {
            return value ? value : 0;
        };
        var getElementBindData = function (key) {
            return $investInput.data(key);
        };
        var $investInput = $('#investForm').find('.text-input-amount');
        var maxInvestAmount = _.compose(parseFloat, maybe, getElementBindData)('max-invest-amount');
        var minInvestAmount = _.compose(parseFloat, maybe, getElementBindData)('min-invest-amount');
        var replace = function (str) {
            return str.replace(/,/g, '');
        };
        var keyupHandler = _.debounce(function (event) {
            var $this = $(this);
            var value = _.compose(parseFloat, replace)($this.val());
            layer.closeAll('tips');
            if (value > maxInvestAmount) {
                showInputErrorTips('该项目每人限投' + maxInvestAmount + '元');
            } else if (value < minInvestAmount) {
                showInputErrorTips('单笔最低投资' + minInvestAmount + '元');
            }
        }, 300);

        $investInput.on('keyup', keyupHandler);

        $accountInfo.find('.icon-graded').on('mouseover',function() {
            layer.closeAll('tips');
            var value = _.compose(replace)($investInput.val()),
                $expected=$accountInfo.find('.expected-interest-dd');

            var queryParams = [];
            $.each($('input[type="hidden"][name="userCouponIds"]'), function(index, item) {
                queryParams.push({
                    'name': 'couponIds',
                    'value': $(item).data("coupon-id")
                })
            });

            $ticketList.find('li').each(function(index, item) {
                if ($(item).find('input[type="radio"]:checked').length > 0) {
                    queryParams.push({
                        'name': 'couponIds',
                        'value': $(item).data("coupon-id")
                    });
                }
            });
            var couponIds = queryParams.length == 0 ? 0: queryParams[0].value;

            $.ajax({
                url: '/get-membership-preference',
                type: 'GET',
                dataType: 'json',
                data:{"loanId":loanId,"investAmount":value,"couponIds":couponIds},
                contentType: 'application/json; charset=UTF-8'
            })
                .done(function(response) {
                    var data=response.data;
                    if (data.status) {

                        var info='V'+data.level+'会员，专享服务费'+data.rate+'折优惠，已多赚'+data.amount+'元';
                        if(data.membershipPrivilege){
                            info= '增值特权，专享服务费7折优惠，已多赚'+ data.amount +'元';
                        }

                        layer.tips(info, $expected, {
                            tips: [1, '#ff7200'],
                            time: 3000,
                            skin: 'level-layer-tips',
                            tipsMore: true,
                            area: 'auto',
                            maxWidth: '400'
                        });
                    }
                })
                .fail(function() {

                });

        });

    })();

    //勾选马上投资下方 协议复选框
    $('.skip-group .skip-icon').on('click', function(event) {
        event.preventDefault();

        $(this).hasClass('active')?$(this).removeClass('active') && $('#skipCheck').val('false') && $('#checkTip').show()&& $investSubmit.prop('disabled',true):$(this).addClass('active')&& $('#skipCheck').val('true') && $('#checkTip').hide() && $investSubmit.prop('disabled',false);
    });

    //勾选 安心签弹框中的复选框
    $('.tip-item .skip-icon').on('click', function(event) {
        event.preventDefault();
        $(this).hasClass('active')?$(this).removeClass('active') && $('#tipCheck').val('false'):$(this).addClass('active')&& $('#tipCheck').val('true');
    });

    //弹出安心签弹框
    function getSkipPhoneTip(){
        layer.open({
            shadeClose: false,
            title: '安心签代签署授权',
            btn: 0,
            type: 1,
            area: $(window).width()>700?['400px', 'auto']:['320px','auto'],
            content: $('#getSkipPhone')
        });
    }

    var num = 60,Down;

    //获取短信验证码
    $('#getSkipCode').on('click', function(event) {
        event.preventDefault();
        getCode(false);
    });

    //获取语音验证码
    $('#microPhone').on('click', function(event) {
        event.preventDefault();
        getCode(true);
    });
    //安心签弹框中获取短信验证码请求
    function getCode(type){
        $('#getSkipCode').prop('disabled',true);
        $('#microPhone').css('visibility', 'hidden');
        $.ajax({
            url: '/anxinSign/sendCaptcha',
            type: 'POST',
            dataType: 'json',
            data:{
                isVoice:type
            }
        })
        .done(function(data) {
            $('#getSkipCode').prop('disabled',false);
            $('#microPhone').css('visibility', 'visible');
            if(data.success) {
                countDown();
                Down = setInterval(countDown, 1000);
            }
            else {
                layer.msg('请求失败，请重试或联系客服！');
            }

        })
        .fail(function() {
            $('#getSkipCode').prop('disabled',false);
            $('#microPhone').css('visibility', 'visible');
            layer.msg('请求失败，请重试或联系客服！');
        });
    }
    //安心签弹框中获取短信验证码倒计时效果
    function countDown() {
        $('#getSkipCode').val(num + '秒后重新获取').prop('disabled',true);
        $('#microPhone').css('visibility', 'hidden');
        if (num == 0) {
            clearInterval(Down);
            $('#getSkipCode').val('重新获取验证码').prop('disabled',false);
            $('#microPhone').css('visibility', 'visible');
            num=60;
        }else{
            num--;
        }
    }
    //安心签授权弹框表单提交
    $('#getSkipBtn').on('click',  function(event) {
        event.preventDefault();
        var $self=$(this);
        if($('#skipPhoneCode').val()!=''){
            $.ajax({
                url: '/anxinSign/verifyCaptcha',
                type: 'POST',
                dataType: 'json',
                data: {
                    captcha: $('#skipPhoneCode').val(),
                    skipAuth:$('#tipCheck').val()
                }
            })
            .done(function(data) {
                $self.removeClass('active').val('立即授权').prop('disabled', false);
                if(data.success){
                    $('#isAnxinUser').val('true') && $('.skip-group').hide();
                    if(data.skipAuth=='true'){
                        $isAuthenticationRequired.val('false');
                    }
                    skipSuccess();
                }else{
                    $('#skipError').text('验证码不正确').show();
                }
            })
            .fail(function() {
                $self.removeClass('active').val('立即授权').prop('disabled', false);
                layer.msg('请求失败，请重试！');
            })
            .always(function() {
                $self.addClass('active').val('授权中...').prop('disabled', true);
            });
        }else{
            $('#skipError').text('验证码不能为空').show();
        }
    });

    //安心签授权成功弹框
    function skipSuccess(){
        layer.closeAll();
        $('#skipSuccess').show();
        setTimeout(function(){
            $('#skipSuccess').hide();
            $('#skipPhoneCode').val('');
            num=0;
            noPasswordInvest?sendSubmitRequest():$investForm.submit();

        },3000)
    }

});