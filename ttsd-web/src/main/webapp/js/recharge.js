require(['jquery', 'layerWrapper', 'csrf', 'autoNumeric', 'commonFun'], function ($, layer) {
    var $rechargeCon = $(".recharge-bind-card"),
        $rechargeForm = $('.recharge-form', $rechargeCon),
        $fastRechargeForm = $(".fast-recharge-form", $rechargeCon),
        $turnOnFast = $(".turn-on-fast-form", $rechargeCon);

    var tabElement = $('.payment-mode li'),
        rechargeInputAmountElement = $('.amount', $rechargeForm),
        rechargeAmountElement = $('input[name="amount"]', $rechargeForm),
        rechargeSubmitElement = $('.btn', $rechargeForm),

        fastRechargeInputAmountElement = $('.amount', $fastRechargeForm),
        fastRechargeAmountElement = $('input[name="amount"]', $fastRechargeForm),
        fastRechargeSubmitElement = $('.btn', $fastRechargeForm),
        turnOnFastSubmitElement = $('input[type="submit"]', $turnOnFast);

    if (rechargeInputAmountElement) {
        rechargeInputAmountElement.autoNumeric("init");
        rechargeInputAmountElement.keyup(function () {
            var amount = parseFloat(rechargeInputAmountElement.autoNumeric("get"));
            var $this = $(this);
            if (isNaN(amount) || amount < 1) {
                $this.parents('form').find('.error').show();
                rechargeSubmitElement.prop('disabled', true).removeClass('btn-normal');
            } else {
                $this.parents('form').find('.error').hide();
                rechargeSubmitElement.prop('disabled', false).addClass('btn-normal');
            }
        });

        //网银充值提交
        rechargeSubmitElement.click(function () {
            var amount = rechargeInputAmountElement.autoNumeric("get");
            rechargeAmountElement.val(amount);

            layer.open({
                type: 1,
                title: '登录到联动优势支付平台充值',
                area: ['560px', '270px'],
                shadeClose: true,
                content: $('#popRecharge')
            });
        });
    }

    if (fastRechargeInputAmountElement) {
        fastRechargeInputAmountElement.autoNumeric("init");
        fastRechargeInputAmountElement.keyup(function () {
            var $this = $(this),
                amount = parseFloat($this.autoNumeric("get"));
            if (isNaN(amount) || amount < 1) {
                $this.parents('form').find('.error').show();
                fastRechargeSubmitElement.prop('disabled', true).removeClass('btn-normal');

            } else {
                $this.parents('form').find('.error').hide();
                fastRechargeSubmitElement.prop('disabled', false).addClass('btn-normal');
            }
        });
        //快捷充值提交
        fastRechargeSubmitElement.click(function () {
            var amount = fastRechargeInputAmountElement.autoNumeric("get");
            fastRechargeAmountElement.val(amount);
            layer.open({
                type: 1,
                title: '登录到联动优势支付平台充值',
                area: ['500px', '290px'],
                shadeClose: true,
                content: $('#popRecharge')
            });
        });
        //开通快捷支付
        turnOnFastSubmitElement.click(function () {
            layer.open({
                type: 1,
                title: '开通快捷支付功能',
                area: ['500px', '180px'],
                shadeClose: true,
                content: $('#openFastRecharge')
            });
        });
    }
    if ($(".bind-card-nav")) {
        $(".bind-card-nav .btn", $rechargeCon).click(function () {
            window.location.href = $(this).data('url');
        });
    }

    //tab切换

    tabElement.click(function (index) {
        tabElement.removeClass("active");
        var self = $(this),
            num = tabElement.index(this),
            $rechargeCon = $('.recharge-content'),
            $fastRecharge = $('.fast-recharge', $rechargeCon),
            $bankRecharge = $('.e-bank-recharge', $rechargeCon);
        self.addClass("active").siblings('li').removeClass('active');
        $rechargeForm.find("form input[name='publicPay']").val($('li.e-bank-public-recharge-tab').hasClass('active') ? 'true' : 'false');
        if (num == 0) {
            $fastRecharge.addClass('active');
            $bankRecharge.removeClass('active');
        } else {
            $fastRecharge.removeClass('active');
            $bankRecharge.addClass('active');
        }
    });
});
