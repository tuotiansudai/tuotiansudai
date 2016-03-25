require(['jquery', 'pagination', 'mustache', 'text!/tpl/loan-invest-list.mustache', 'layerWrapper','cnzz-statistics', 'underscore', 'jquery.ajax.extension', 'autoNumeric', 'coupon-alert','red-envelope-float'], function ($, pagination, Mustache, investListTemplate, layer,cnzzPush, _) {
    var $loanDetail = $('.loan-detail-content'),
        loanId = $('.hid-loan').val(),
        amountInputElement = $(".text-input-amount", $loanDetail),
        $accountInfo = $('.account-info', $loanDetail),
        $btnLookOther = $('.btn-pay', $accountInfo),
        tabs = $('.loan-nav li'),
        $loanList = $('.loan-list', $loanDetail),
        paginationElement = $('.pagination', $loanDetail),
        $error = $('.errorTip'),
        $investNoPassword=$('#investNoPassword'),
        $loanInvest=$('#loanInvest'),
        $freeSecret=$('#freeSecret'),
        $hasRemindInvestNoPassword=$('#hasRemindInvestNoPassword'),
        $goAuthorize=$('#goAuthorize'),
        $failGoOnBtnInvest=$('.fail_go_on_invest'),
        $successGoOnBtnInvest=$('.success_go_on_invest'),
        $againBtn=$('.again-btn'),
        cnzzPush = new cnzzPush();


    layer.ready(function() {
        layer.photos({
            photos: '#picListBox'
        });
    });

    var loanProgress = $loanDetail.data('loan-progress');
    if (loanProgress <= 50) {
        $('.chart-box .rount').css('webkitTransform', "rotate(" + 3.6 * loanProgress + "deg)");
        $('.chart-box .rount2').hide();
    } else {
        $('.chart-box .rount').css('webkitTransform', "rotate(180deg)");
        $('.chart-box .rount2').show();
        $('.chart-box .rount2').css('webkitTransform', "rotate(" + 3.6 * (loanProgress - 50) + "deg)");
    }

    var loadInvestData = function() {
        paginationElement.loadPagination({
            index: 1
        }, function(data) {
            if (data.status) {
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
                var investLowerLimit = $(self.find(".ticket-term.lower-limit")).data('invest-lower-limit') || 0;
                var investUpperLimit = $(self.find(".ticket-term.upper-limit")).data('invest-upper-limit') || 0;
                var disabled = (investLowerLimit > 0 && investLowerLimit > investAmount) || (investUpperLimit > 0 && investUpperLimit < investAmount);
                input.prop("disabled", disabled);
                disabled ? self.addClass('disabled') : self.removeClass('disabled');
            });

            var notSharedRedEnvelopes = _.groupBy($ticketList.find("li[data-coupon-type='RED_ENVELOPE']"), function(ticket) {
                return $(ticket).hasClass('disabled') ? "disabled" : "enabled";
            });

            var notSharedCoupons = _.groupBy($ticketList.find("li[data-coupon-type!='RED_ENVELOPE'][data-coupon-type!='BIRTHDAY_COUPON']"), function(ticket) {
                return $(ticket).hasClass('disabled') ? "disabled" : "enabled";
            });

            var birthdayCoupon = $ticketList.find("li[data-coupon-type='BIRTHDAY_COUPON']");

            $ticketList.empty();

            if (birthdayCoupon.length > 0) {
                $ticketList.append(birthdayCoupon);
            }

            if (notSharedRedEnvelopes['enabled']) {
                $ticketList.append(_.sortBy(notSharedRedEnvelopes['enabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-created-time")).getTime();
                }));
            }

            if (notSharedCoupons['enabled']) {
                $ticketList.append(_.sortBy(notSharedCoupons['enabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-created-time")).getTime();
                }));
            }

            if (notSharedRedEnvelopes['disabled']) {
                $ticketList.append(_.sortBy(notSharedRedEnvelopes['disabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-created-time")).getTime();
                }));
            }

            if (notSharedCoupons['disabled']) {
                $ticketList.append(_.sortBy(notSharedCoupons['disabled'], function(ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-created-time")).getTime();
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
            var amountNeedRaised = parseInt($('form .amountNeedRaised-i').data("amount-need-raised")) || 0;
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
            calExpectedCouponInterest();
        });

        amountInputElement.keyup(function(event) {
            if (isInvestor) {
                var flag = true;
                $ticketList.find('li').each(function(index, item) {
                    if ($(item).attr("data-coupon-type") == 'BIRTHDAY_COUPON') {
                        flag = false;
                        $(item).find('input[type="radio"]').prop('checked', true);
                    } else {
                        $(item).find('input[type="radio"]').prop('checked', false);
                    }
                });
                if (flag) {
                    $useExperienceTicket.find('span').text('请选择优惠券');
                } else {
                    $useExperienceTicket.find('span').text('生日福利');
                }
            }
        });
        //click touzi btn
        $loanInvest.on('click', function(event) {
            event.preventDefault();
            if (!isInvestor) {
                location.href = '/login?redirect=' + encodeURIComponent(location.href);
            }else{
                if ($hasRemindInvestNoPassword.val()=='true' || $investNoPassword.val()=='true') {
                    formSubmit();
                } else {
                    isAuthorize();
                }
            }
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

    if ($error.length) {
        layer.tips('<i class="fa fa-times-circle"></i>' + $error.text(), '.text-input-amount', {
            tips: [1, '#ff7200'],
            time: 0
        });
    }
    //click tip red text
    $freeSecret.on('click', function(event) {
        event.preventDefault();
        openBtn();
    });
    $('#isAuthorizeSuccess').on('click', '.go-on-btn', function(event) {
        event.preventDefault();
        layer.closeAll();
        location.reload();
    }).on('click', '.again-btn', function(event) {
        event.preventDefault();
        $goAuthorize.submit();
    });

    //is tip A
    function openBtn(){
        layer.open({
            type: 1,
            closeBtn: 0,
            skin: 'demo-class',
            shadeClose:false,
            btn:['不开启','开启'],
            title: '免密投资',
            area: ['500px', '160px'],
            content: '<p class="pad-m-tb tc">您可直接开启免密投资，简化投资过程，理财快人一步，是否开启？</p>',
            btn1:function(){
                cnzzPush.trackClick("标的详情页","推荐免密弹框","不开启");
                layer.closeAll();
            },
            btn2: function(index){
                cnzzPush.trackClick("标的详情页","推荐免密弹框","开启");
                if ($freeSecret.attr('data-open-agreement')=='true') { // 如果开启过免密支付
                    $.ajax({
                        url: '/no-password-invest/enabled',
                        type: 'POST',
                        dataType: 'json'
                    })
                    .done(function() {
                        layer.closeAll();
                        $freeSecret.hide();
                        $investNoPassword.val('true');
                        layer.msg('开通成功！');
                    })
                    .fail(function() {
                        layer.closeAll();
                        layer.msg('开通失败，请重试！');
                    });
                } else {
                    layer.closeAll();
                    isAuthorizeSuccess();
                    $goAuthorize.submit();
                }
            }
        });
    }
 
    //is tip B1 or tip B2?
    function isAuthorize(){
        $.ajax({
            url: '/no-password-invest/writeRemindFlag',
            type: 'GET',
            dataType: 'json'
        })
        .done(function() {
            $hasRemindInvestNoPassword.val('true');
            if($freeSecret.attr('data-open-agreement')=='true'){
                layer.open({
                    type: 1,
                    closeBtn: 0,
                    skin: 'demo-class',
                    title: '免密投资',
                    shadeClose:false,
                    btn:['继续投资','开启免密投资'],
                    area: ['500px', '160px'],
                    content: '<p class="pad-m-tb tc">推荐您开通免密投资功能，简化投资过程，理财快人一步。</p>',
                    btn1:function(){
                        cnzzPush.trackClick("标的详情页","马上投资弹框","继续投资B");
                        formSubmit();
                        layer.closeAll();
                    },
                    btn2: function(index){
                        cnzzPush.trackClick("标的详情页","马上投资弹框","开启免密投资");
                        $.ajax({
                            url: '/no-password-invest/enabled',
                            type: 'POST',
                            dataType: 'json'
                        })
                        .done(function() {
                            layer.closeAll();
                            layer.msg('开通成功！', function(){
                                formSubmit();
                            });
                        })
                        .fail(function() {
                            layer.closeAll();
                            layer.msg('开通失败，请重试！');
                        });
                    }
                });
            }else{
                layer.open({
                    type: 1,
                    closeBtn: 0,
                    skin: 'demo-class',
                    title: '免密投资',
                    shadeClose:false,
                    btn:['继续投资','去联动优势授权'],
                    area: ['500px', '160px'],
                    content: '<p class="pad-m-tb tc">推荐您开通免密投资功能，简化投资过程，理财快人一步。</p>',
                    btn1:function(){
                        cnzzPush.trackClick("标的详情页","马上投资弹框","继续投资B");
                        formSubmit();
                        layer.closeAll();
                    },
                    btn2: function(index){
                        cnzzPush.trackClick("标的详情页","马上投资弹框","去联动优势授权");
                        layer.closeAll();
                        isAuthorizeSuccess();
                        $goAuthorize.submit();
                    }
                });
            }
        })
        .fail(function() {
            layer.msg('请求失败，请重试！');
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
    function isAuthorizeSuccess(){
        layer.open({
            type: 1,
            skin: 'demo-class',
            shadeClose:false,
            title: '登录到联动优势支付平台开通免密投资',
            area: ['500px', '290px'],
            content: $('#isAuthorizeSuccess'),
            end:function(){
                location.reload();
            }
        });
    }
    //submit  form
    function formSubmit(){
        if ($('#investForm').attr('action') === '/invest') {
            if (!isInvestor) {
                location.href = '/login?redirect=' + encodeURIComponent(location.href);
                return false;
            }

            var investAmount = getInvestAmount();

            if (!validateInvestAmount()) {
                var tipContent = investAmount === 0 ? '投资金额不能为0元！' : '投资金额不能大于可投金额！';
                layer.tips('<i class="fa fa-times-circle"></i>' + tipContent, '.text-input-amount', {
                    tips: [1, '#ff7200'],
                    time: 0
                });
                return false;
            }

            var accountAmount = parseInt($('#investForm .account-amount').data("user-balance")) || 0;
            if (investAmount > accountAmount) {
                location.href = '/recharge';
                return false;
            }
        }
        amountInputElement.val(amountInputElement.autoNumeric("get"));
        $('#investForm').submit();
    }
});