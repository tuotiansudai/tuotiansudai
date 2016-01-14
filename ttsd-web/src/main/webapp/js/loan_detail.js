require(['jquery', 'pagination', 'mustache', 'text!/tpl/loan-invest-list.mustache', 'layerWrapper', 'underscore', 'csrf', 'autoNumeric'], function ($, pagination, Mustache, investListTemplate, layer, _) {

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
            $ticketList.find("li").each(function (ticket) {
                var self = $(this);
                var radio = self.find("input[name='userCouponId']");
                self.removeClass('disabled');
                radio.prop("disabled", false);
                var ticketTerm = $(self.find(".ticket-term"));
                var investLowerLimit = ticketTerm.data('invest-lower-limit') || 0;
                if (investLowerLimit > getInvestAmount()) {
                    self.addClass('disabled');
                    radio.prop("disabled", true);
                }
            });

            var tickets = _.sortBy($ticketList.find("li"), function(ticket) {
                var $ticket = $(ticket);
                return new Date($ticket.data("coupon-created-time")).getTime() * ($ticket.hasClass('disabled') ? 2 : 1);
            });

            $ticketList.empty().append(tickets);

            $ticketList.find('li').click(function (event) {
                var couponItem = $($(event.target).parents("li")).length > 0 ? $($(event.target).parents("li")) : $(event.target);
                if (couponItem.hasClass("disabled")) {
                    return false;
                }
                var text = $.trim(couponItem.find('.ticket-title').text());
                $useExperienceTicket.find('span').text(text);
                $ticketList.addClass('hide');

                var couponAmount = couponItem.data('coupon-amount');
                $couponExpectedInterest.text("");
                if (!isNaN(couponAmount)) {
                    $.ajax({
                        url: '/calculate-expected-interest/loan/' + loanId + '/amount/' + couponAmount,
                        type: 'get',
                        dataType: 'json',
                        contentType: 'application/json; charset=UTF-8'
                    }).done(function (amount) {
                        $couponExpectedInterest.text("+" + (amount/100).toFixed(2));
                        $btnLookOther.prop('disabled', false);
                    });
                }
            });
        };

        var validateInvestAmount = function () {
            var amount = getInvestAmount();
            var amountNeedRaised = parseInt($('form .amountNeedRaised-i').data("amount-need-raised")) || 0;
            return amount > 0 && amountNeedRaised >= amount;
        };

        var calExpectedInterest = function () {
            $.ajax({
                url: '/calculate-expected-interest/loan/' + loanId + '/amount/' + getInvestAmount(),
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function (amount) {
                $(".principal-income").text((amount/100).toFixed(2));
            });
        };

        if (isInvestor) {
            calExpectedInterest();
        }

        amountInputElement.blur(function () {
            calExpectedInterest();
        });

        amountInputElement.keyup(function (event) {
            $couponExpectedInterest.text("");
            if (isInvestor && !$('#noCouponSelected').prop('checked')) {
                $ticketList.find('input').prop('checked', false);
                $useExperienceTicket.find('span').text('请点击选择优惠券');
            }
        });

        $('form').submit(function () {
            var frm = $(this);
            if (frm.attr('action') === '/invest') {
                if (!isInvestor) {
                    location.href = '/login?redirect=' + encodeURIComponent(location.href);
                    return false;
                }

                if (!validateInvestAmount()) {
                    var tipContent = isNaN(amountInputElement.autoNumeric("get")) || parseInt((amountInputElement.autoNumeric("get") * 100).toFixed(0)) === 0 ? '投资金额不能为0元！' : '投资金额不能大于可投金额！';
                    layer.tips('<i class="fa fa-times-circle"></i>' + tipContent, '.text-input-amount', {
                        tips: [1, '#ff7200'],
                        time: 0
                    });
                    return false;
                }

                var investAmount = getInvestAmount();
                var accountAmount = parseInt($('form .account-amount').data("user-balance")) || 0;
                if (investAmount > accountAmount) {
                    location.href = '/recharge';
                    return false;
                }
            }
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