require(['jquery', 'pagination', 'mustache', 'text!/tpl/loan-invest-list.mustache', 'layerWrapper', 'underscore', 'csrf', 'autoNumeric', 'coupon-alert'], function ($, pagination, Mustache, investListTemplate, layer, _) {

    var $loanDetail = $('.loan-detail-content'),
        loanId = $('.hid-loan').val(),
        amountInputElement = $(".text-input-amount", $loanDetail),
        $accountInfo = $('.account-info', $loanDetail),
        $btnLookOther = $('.btn-pay', $accountInfo),
        tabs = $('.loan-nav li'),
        $loanList = $('.loan-list', $loanDetail),
        paginationElement = $('.pagination', $loanDetail),
        $error = $('.errorTip');

    layer.ready(function () {
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

    var loadInvestData = function () {
        paginationElement.loadPagination({index: 1}, function (data) {
            if (data.status) {
                var html = Mustache.render(investListTemplate, data);
                $('.loan-list-con table').html(html);
            }
        });
    };

    // tab select
    $loanList.find('.loan-list-con').eq(0).show();
    tabs.click(function () {
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
        window.setInterval(function () {
            var day = 0, hour = 0, minute = 0, second = 0;
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
        amountInputElement.focus(function () {
            layer.closeAll('tips');
        });

        var isInvestor = 'INVESTOR' === $loanDetail.data('user-role');
        var $ticketList = $('.ticket-list');
        var $useExperienceTicket = $('#use-experience-ticket');
        var $couponExpectedInterest = $(".experience-income");

        var getInvestAmount = function () {
            var amount = 0;
            if (!isNaN(amountInputElement.autoNumeric("get"))) {
                amount = parseInt((amountInputElement.autoNumeric("get") * 100).toFixed(0));
            }

            return amount;
        };

        var refreshCouponStatus = function () {
            var investAmount = getInvestAmount();
            $.each($ticketList.find("li"), function (index, ticket) {
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
                $ticketList.append(_.sortBy(notSharedRedEnvelopes['enabled'], function (ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-created-time")).getTime();
                }));
            }

            if (notSharedCoupons['enabled']) {
                $ticketList.append(_.sortBy(notSharedCoupons['enabled'], function (ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-created-time")).getTime();
                }));
            }

            if (notSharedRedEnvelopes['disabled']) {
                $ticketList.append(_.sortBy(notSharedRedEnvelopes['disabled'], function (ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-created-time")).getTime();
                }));
            }

            if (notSharedCoupons['disabled']) {
                $ticketList.append(_.sortBy(notSharedCoupons['disabled'], function (ticket) {
                    var $ticket = $(ticket);
                    return new Date($ticket.data("coupon-created-time")).getTime();
                }));
            }

            $ticketList.find('li').click(function (event) {
                var couponItem = $(event.currentTarget);
                if (couponItem.hasClass("disabled")) {
                    return false;
                }

                var couponTitle = $.trim(couponItem.find('.ticket-title').text());
                $useExperienceTicket.find('span').text(couponTitle);
                couponItem.find("input").prop('checked', true);
                $couponExpectedInterest.text("");

                if (couponItem.data('coupon-id')) {
                    if (couponItem.attr('data-coupon-type') != 'BIRTHDAY_COUPON') {
                        calExpectedCouponInterest(couponItem.data('coupon-id'));
                    } else {
                        calExpectedCouponInterest();
                    }
                }
                $ticketList.addClass('hide');
            });
        };

        var validateInvestAmount = function () {
            var amount = getInvestAmount();
            var amountNeedRaised = parseInt($('form .amountNeedRaised-i').data("amount-need-raised")) || 0;
            return amount > 0 && amountNeedRaised >= amount;
        };

        var calExpectedCouponInterest = function (couponId) {
            var queryParams = [];
            if ($.isNumeric(couponId)) {
                queryParams.push({'name': 'couponIds', 'value': couponId});
            }

            $.each($('input[type="hidden"][name="userCouponIds"]'), function(index, item) {
                queryParams.push({'name': 'couponIds', 'value': $(item).data("coupon-id")})
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
            }).done(function (amount) {
                $couponExpectedInterest.text("+" + amount);
                $btnLookOther.prop('disabled', false);
            });
        };

        var calExpectedInterest = function () {
            $.ajax({
                url: '/calculate-expected-interest/loan/' + loanId + '/amount/' + getInvestAmount(),
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function (amount) {
                $(".principal-income").text(amount);
            });
        };

        if (isInvestor) {
            calExpectedInterest();
            calExpectedCouponInterest();
        }

        amountInputElement.blur(function () {
            calExpectedInterest();
            calExpectedCouponInterest();
        });

        amountInputElement.keyup(function (event) {
            if (isInvestor) {
                var flag = true;
                $ticketList.find('input[type="radio"]').each(function(index,item){
                    if ($(item).attr("data-type") == 'BIRTHDAY_COUPON') {
                        flag = false;
                    } else {
                        $(item).prop('checked', false);
                    }
                });
                if (flag) {
                    $useExperienceTicket.find('span').text('请选择优惠券');
                }
            }
        });

        $('form').submit(function () {
            var frm = $(this);
            if (frm.attr('action') === '/invest') {
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

                var accountAmount = parseInt($('form .account-amount').data("user-balance")) || 0;
                if (investAmount > accountAmount) {
                    location.href = '/recharge';
                    return false;
                }
            }
            amountInputElement.val(amountInputElement.autoNumeric("get"));
            return true;
        });

        $useExperienceTicket.click(function (event) {
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

        amountInputElement.focus(function (event) {
            $ticketList.addClass('hide');
        });

        $('body').click(function (e) {
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
});