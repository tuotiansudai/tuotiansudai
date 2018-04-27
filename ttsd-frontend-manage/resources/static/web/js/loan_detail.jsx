require('webStyle/investment/loan_detail.scss');
require('webJs/plugins/autoNumeric');
require('publicJs/pagination');
require('webJsModule/coupon_alert');
require('webJsModule/assign_coupon');
//投资计算器和意见反馈
require('webJsModule/red_envelope_float');

//安心签协议
require('webJsModule/anxin_agreement');
//安心签业务
let anxinModule = require('webJsModule/anxin_signed');

require('publicJs/login_tip');
let commonFun= require('publicJs/commonFun');

let $loanDetailContent=$('#loanDetailContent');
let $accountInfo = $('.account-info', $loanDetailContent),
    $btnLookOther = $('.btn-pay', $accountInfo);
let amountInputElement = $(".text-input-amount", $loanDetailContent);
let noPasswordRemind = amountInputElement.data('no-password-remind');
let noPasswordInvest = amountInputElement.data('no-password-invest');
let autoInvestOn = amountInputElement.data('auto-invest-on');
let $ticketList = $('.ticket-list');

let $authorizeAgreement=$('#goAuthorize');
let $authorizeAgreementOptions = $('#authorizeAgreementOptions');

let $investForm = $('#investForm');
let $investSubmit=$('#investSubmit');
let $isAuthenticationRequired=$('#isAuthenticationRequired');

let isInvestor = 'INVESTOR' === $loanDetailContent.data('user-role');
let isAuthentication = 'USER' === $loanDetailContent.data('authentication');
let loanId = $('input[name="loanId"]',$loanDetailContent).val();

var viewport = globalFun.browserRedirect();

function showInputErrorTips(message) {
    layer.tips('<i class="fa fa-times-circle"></i>' + message, '.text-input-amount', {
        tips: [1, '#ff7200'],
        time: 0
    });
}

function showLayer() {
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

function getInvestAmount() {
    var amount = 0;
    if (!isNaN(amountInputElement.autoNumeric("get"))) {
        amount = parseInt((amountInputElement.autoNumeric("get") * 100).toFixed(0));
    }

    return amount;
}

function validateInvestAmount() {
    var amount = getInvestAmount();
    var amountNeedRaised = parseInt($('#investForm').find('input[name=amount]').data("amount-need-raised")) || 0;
    return amount > 0 && amountNeedRaised >= amount;
};
//投资表单请求以及校验
function investSubmit(){
    let $minInvestAmount = amountInputElement.data('min-invest-amount')
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
            area: ['300px'],
            content: '<p class="pad-m-tb tc">确认投资？</p>',
            btn1: function(){
                layer.closeAll();
            },
            btn2:function(){
                if($isAuthenticationRequired.val()==='false'){
                    sendSubmitRequest();
                }else{
                    anxinModule.getSkipPhoneTip();
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
        anxinModule.getSkipPhoneTip();
        return false;
    }
}

//发送投资提交请求
function sendSubmitRequest(){

    commonFun.useAjax({
        url: '/no-password-invest',
        data: $investForm.serialize(),
        type: 'POST',
        beforeSubmit: function () {
            $investSubmit.addClass("loading");
        },
    },function(response) {
        layer.closeAll();
        $investSubmit.removeClass("loading");
        let data = response.data;
        if (data.status) {
            location.href = "/callback/invest_project_transfer_nopwd?" + $.param(data.extraValues);
        } else if (data.message == '新手标投资已超上限') {
            showLayer();
        } else {
            showInputErrorTips(data.message);
        }
    });
}

//is tip B1 or tip B2?
function markNoPasswordRemind(){
    if (!noPasswordRemind) {
        commonFun.useAjax({
            url: '/no-password-invest/mark-remind',
            type: 'POST'
        },function() {
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
        area: ['500px'],
        content: '<p class="pad-m-tb tc">推荐您开通免密投资功能，简化投资过程，理财快人一步！</p>',
        btn1: function () {
            investSubmit();
            layer.closeAll();
        },
        btn2: function () {
            if (autoInvestOn) {

                commonFun.useAjax({
                    url: '/no-password-invest/enabled',
                    type: 'POST'
                },function() {
                    noPasswordInvest = true;
                    layer.closeAll();
                    layer.msg('开启成功！', function () {
                        investSubmit();
                    });
                });
            } else {
                showAuthorizeAgreementOptions();
                $authorizeAgreement.submit();
            }
        }
    });
}

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

//投资显示错误信息
(function() {
    let $errorTip = $('.errorTip',$loanDetailContent),
        $errorType = $('.errorType',$loanDetailContent);

    $('.extra-rate .fa-mobile',$loanDetailContent).on('mouseover', function(event) {
        event.preventDefault();
        var $self=$(this);
        layer.tips('APP投资该项目享受最高0.8%年化收益奖励', $self, {
            tips: 3
        });
    });

    if ($errorTip.length > 0 && $errorTip.text() != '' && $errorType.val() != 'OUT_OF_NOVICE_INVEST_LIMIT') {
        showInputErrorTips($errorTip.text());
    }
    if ($errorType.val() == 'OUT_OF_NOVICE_INVEST_LIMIT') {
        showLayer();
    }

})();

//如果标的状态是PREHEAT，30分钟以内开始倒计时
(function() {

    if ($loanDetailContent.data("loan-status") === 'PREHEAT') {
        var countdown = $loanDetailContent.data('loan-countdown');
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
})();

// 如果标的状态为PREHEAT或者RAISING
(function() {

    let $investSubmit=$('#investSubmit');
    if (amountInputElement.length) {
        amountInputElement.autoNumeric("init");
        amountInputElement.focus(function() {
            layer.closeAll('tips');
        });

        var $useExperienceTicket = $('#use-experience-ticket');
        var $couponExpectedInterest = $(".experience-income");

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
            commonFun.useAjax({
                url: '/calculate-expected-coupon-interest/loan/' + loanId + '/amount/' + getInvestAmount(),
                data: $.param(queryParams),
                type: 'GET'
            },function(amount) {
                $couponExpectedInterest.text("+" + amount);
                $btnLookOther.prop('disabled', false);
            });
        };

        var calExpectedInterest = function() {
            commonFun.useAjax({
                url: '/calculate-expected-interest/loan/' + loanId + '/amount/' + getInvestAmount(),
                type: 'GET',
            },function(amount) {
                $(".principal-income").text(amount);
            });
        };

        if (isInvestor) {
            calExpectedInterest();
            calExpectedCouponInterest();
        }

        amountInputElement.blur(function() {
            calExpectedInterest();
            commonFun.useAjax({
                url: '/loan/' + loanId + '/amount/' + getInvestAmount() + "/max-benefit-user-coupon",
                type: 'GET',
            },function(maxBenefitUserCouponId) {
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
            })
        });

        //click invest submit btn
        $investSubmit.on('click', function(event) {
            event.preventDefault();
            if ($('.header-login').data('wechat-login-name')) {
                location.href = '/login?redirect=' + location.href;
                return;
            }
            $.when(commonFun.isUserLogin())
                .done(function() {
                    if (isInvestor) {
                        noPasswordRemind || noPasswordInvest ? investSubmit() : markNoPasswordRemind();
                        return;
                    }
                    if (isAuthentication) {
                        location.href = '/register/account';
                    }
                })
                .fail(function() {
                    //判断是否需要弹框登陆
                    layer.open({
                        type: 1,
                        title: false,
                        closeBtn: 0,
                        area: ['auto', 'auto'],
                        content: $('#loginTip')
                    });
                });
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
})();

//借款详情和出借记录

(function() {
    let $loanDetailSwitch=$('#loanDetailSwitch')
    let menuTab=$('.loan-nav li',$loanDetailSwitch);
    let $loanList = $('.loan-list', $loanDetailSwitch);
    let paginationElement = $('.pagination', $loanDetailSwitch);

    let loadInvestData = function() {
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
                let LendTpl=$('#LendTemplate').html();
                let ListRender = _.template(LendTpl);

                $('.loan-list-con .table-box',$loanDetailSwitch).html(ListRender(data));
            }
        });
    };
    //默认显示借款详情
    $loanList.find('.loan-list-con').eq(0).show();

    menuTab.click(function() {
        var self = $(this);
        self.addClass('active').siblings('li').removeClass('active');
        var index = self.index();
        if (index === 1) {
            loadInvestData();
        }
        $loanList.find('.loan-list-con').eq(index).show().siblings('.loan-list-con').hide();
    });
})();

//免密投资
(function() {
    let $noPasswordTips=$('#noPasswordTips');
    $noPasswordTips.on('click', function() {
        layer.open({
            type: 1,
            closeBtn: 0,
            skin: 'layer-tip-loanDetail',
            shadeClose: false,
            title: '免密投资',
            btn: ['不开启', '开启'],
            area: ['500px'],
            content: '<p class="pad-m-tb tc">您可直接开启免密投资，简化投资过程，理财快人一步，是否开启？</p>',
            btn1: function () {
                layer.closeAll();
            },
            btn2: function () {
                if (autoInvestOn) {
                    // 如果开启过免密支付
                    commonFun.useAjax({
                        url: '/no-password-invest/enabled',
                        type: 'POST'
                    },function () {
                        noPasswordInvest = true;
                        $noPasswordTips.hide();
                        layer.msg('开启成功！');
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

})();

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

//图片效果
(function() {
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
    let fancybox = require('publicJs/fancybox');
    fancybox(function() {
        $('[scroll-carousel]').carousel().find('.col').fancybox({
            'titlePosition': 'over',
            'cyclic': false,
            'showCloseButton': true,
            'showNavArrows': true,
            'titleFormat': function (title, currentArray, currentIndex, currentOpts) {
                return '';
            }
        });
    });

})();
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

        commonFun.useAjax({
            url: '/get-membership-preference',
            type: 'GET',
            data:{"loanId":loanId,"investAmount":value,"couponIds":couponIds},
        },function (response) {
            var data=response.data;
            if (data.status) {
                var info='V'+data.level+'会员，专享服务费'+data.rate+'折优惠，已多赚'+data.amount+'元';

                layer.tips(info, $expected, {
                    tips: [1, '#ff7200'],
                    time: 3000,
                    skin: 'level-layer-tips',
                    tipsMore: true,
                    area: 'auto',
                    maxWidth: '400'
                });
            }
        });

    });

})();


//**************************安心签*******************************
//勾选马上投资下方 协议复选框
$('.init-checkbox-style').initCheckbox(function(event) {
    //如果安心签协议未勾选，马上投资按钮需要置灰
    let checkboxBtn = event.children[0];
    let checkBool = $(checkboxBtn).prop('checked');
    if(checkboxBtn.id=='skipCheck') {
        $investSubmit.prop('disabled',!checkBool);
        $('#checkTip').toggle();
    }
});

anxinModule.toAuthorForAnxin(function(data) {
    $('#isAnxinUser').val('true');
    $('.skip-group').hide();
    if(data.skipAuth=='true'){
        $isAuthenticationRequired.val('false');
    }
    noPasswordInvest?sendSubmitRequest():$investForm.submit();
});





