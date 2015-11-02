require(['jquery', 'csrf', 'autoNumeric','commonFun'], function ($) {
    $(function () {
        var tabElement = $('.recharge-wrapper ul li');

        var rechargeInputAmountElement = $(".recharge-form .amount");
        var rechargeAmountElement = $(".recharge-form input[name='amount']");
        var rechargeSubmitElement = $('.recharge-form .submit');

        var fastRechargeInputAmountElement = $(".fast-recharge-form .amount");
        var fastRechargeAmountElement = $(".fast-recharge-form input[name='amount']");
        var fastRechargeSubmitElement = $('.fast-recharge-form .submit');

        if (rechargeInputAmountElement) {
            rechargeInputAmountElement.autoNumeric("init");
            rechargeInputAmountElement.keyup(function () {
                var amount = parseFloat(rechargeInputAmountElement.autoNumeric("get"));
                if (isNaN(amount) || amount === 0) {
                    rechargeSubmitElement.addClass('grey').attr('disabled', true);
                } else {
                    rechargeSubmitElement.removeClass('grey').attr('disabled', false);
                }
            });
            //网银充值提交
            rechargeSubmitElement.click(function () {
                $('.ecope-overlay,.ecope-dialog').show();
                var amount = rechargeInputAmountElement.autoNumeric("get");
                rechargeAmountElement.val(amount);
            });
        }

        if (fastRechargeInputAmountElement) {
            fastRechargeInputAmountElement.autoNumeric("init");
            fastRechargeInputAmountElement.keyup(function () {
                var amount = parseFloat(fastRechargeInputAmountElement.autoNumeric("get"));
                if (isNaN(amount) || amount === 0) {
                    fastRechargeSubmitElement.addClass('grey').attr('disabled', true);
                } else {
                    fastRechargeSubmitElement.removeClass('grey').attr('disabled', false);
                }
            });
            //快捷充值提交
            fastRechargeSubmitElement.click(function () {
                $('.ecope-overlay,.ecope-dialog').show();
                var amount = fastRechargeInputAmountElement.autoNumeric("get");
                fastRechargeAmountElement.val(amount);
            });
        }

        if ($(".bind-card-nav")) {
            $(".bind-card-nav .submit").click(function () {
                window.location.href = $(this).data('url');
            });
        }

        //select bank
        var bankElement = $('.e-bank-recharge ol li');

        bankElement.click(function () {
            var selectedBankElement = $(this).find('input');
            var bankCode = selectedBankElement.data('name');
            $('.selected-bank').val(bankCode);
        });

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