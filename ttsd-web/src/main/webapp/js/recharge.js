require(['jquery', 'layer', 'csrf', 'autoNumeric', 'commonFun'], function ($, layer) {
    $(function () {
        var $rechargeForm = $('.recharge-form'),
            $rechargeCon = $(".recharge"),
            $fastRechargeForm = $(".fast-recharge-form");

        var tabElement = $('.payment-mode li'),
            rechargeInputAmountElement = $(".amount", $rechargeForm),
            rechargeAmountElement = $("input[name='amount']", $rechargeForm),
            rechargeSubmitElement = $('.btn', $rechargeForm),

            fastRechargeInputAmountElement = $(".amount", $fastRechargeForm),
            fastRechargeAmountElement = $("input[name='amount']", $fastRechargeForm),
            fastRechargeSubmitElement = $('.btn', $fastRechargeForm),
            turnOnFastSubmitElement = $(".turn-on-fast-form .submit");

        if (rechargeInputAmountElement) {
            rechargeInputAmountElement.autoNumeric("init");
            rechargeInputAmountElement.keyup(function () {
                var amount = parseFloat(rechargeInputAmountElement.autoNumeric("get"));
                console.log(amount);
                if (isNaN(amount) || amount < 0.01) {
                    rechargeSubmitElement.prop('disabled', true).removeClass('btn-normal');
                } else {
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
                var amount = parseFloat(fastRechargeInputAmountElement.autoNumeric("get"));
                if (isNaN(amount) || amount < 0.01) {
                    fastRechargeSubmitElement.prop('disabled', true).removeClass('btn-normal');

                } else {
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
                    area: ['560px', '270px'],
                    shadeClose: true,
                    content: $('#popRecharge')
                });
            });
        }

        if (turnOnFastSubmitElement) {
            turnOnFastSubmitElement.click(function () {
                $('.ecope-overlay-fast,.ecope-dialog-fast').show();
            });
        }


        if ($(".bind-card-nav")) {
            $(".bind-card-nav .btn", $rechargeCon).click(function () {
                window.location.href = $(this).data('url');
            });

        }

        //tab切换
        tabElement.click(function () {
            tabElement.removeClass("active");
            var self = $(this);
            self.addClass("active");
            $(".recharge-content .fast-recharge").toggleClass("active");
            $(".recharge-content .e-bank-recharge").toggleClass("active");
        });

        //充值提交
        submitElement.click(function () {
            var amount = amountInputElement.autoNumeric("get");
            amountElement.val(amount);
            var content=$('#popRecharge');

            commonFun.popWindow('登录到联动优势支付平台充值',content,{width:'560px'});

        });

        // 充值弹出页面
        $('.ecope-dialog .close').click(function () {
            $('.ecope-overlay,.ecope-dialog').hide();
        });
    });
});