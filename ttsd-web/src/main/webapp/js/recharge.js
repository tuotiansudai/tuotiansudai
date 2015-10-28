require(['jquery', 'csrf', 'autoNumeric','commonFun'], function ($) {
    $(function () {
        var amountInputElement = $(".e-bank-amount");
        var amountElement = $(".e-bank-recharge .recharge-form input[name='amount']");
        var submitElement = $('.recharge-submit');

        amountInputElement.autoNumeric("init");

        amountInputElement.keyup(function () {
            var amount = parseFloat(amountInputElement.autoNumeric("get"));
            if (isNaN(amount) || amount === 0) {
                submitElement.addClass('grey').attr('disabled', true);
            } else {
                submitElement.removeClass('grey').attr('disabled', false);
            }
        });

        //select bank
        var bankElement = $('.e-bank-recharge ol li');

        bankElement.click(function () {
            var selectedBankElement = $(this).find('input');
            var bankCode = selectedBankElement.data('name');
            $('.selected-bank').val(bankCode);
        });

        //tab切换
        var _li = $('.banking ul li');
        _li.click(function () {
            var _this = $(this);
            var index = _this.index();
            var boxBanking = $('.box-banking .tab-box');
            if (_this.hasClass('tab-fast-pay')) {
                $.get(API_FAST_PAY, function (data) {
                    /*optional stuff to do after success */
                    if (data.bindCardStatus == "unbindCard") {
                        //未绑卡
                        $('.bind-card-layer').find('p').text("您还未绑定银行卡，请您绑定银行卡！");
                        $('.btn-box-layer').find('.now').attr('href', "/bind-card");
                        $('.bind-card-layer').show();
                        //location.href = 'http://localhost:8080/bind-card';
                    } else if (data.bindCardStatus == "commonBindCard") {
                        $('.bind-card-layer').find('p').text("您绑定的银行卡不支持快捷支付，是否换卡？");
                        $('.btn-box-layer').find('.now').attr('href', "/bind-card");
                        $('.bind-card-layer').show();
                        //  普卡绑
                        //location.href = 'http://localhost:8080/bind-card';

                    } else if (data.bindCardStatus == "specialBindCard") {
                        //  7卡未开
                        _this.addClass('active').siblings().removeClass('active');
                        boxBanking.eq(index).find('.recharge-bank').hide();
                        boxBanking.eq(index).find('.jq-open-fast-pay').show();
                        boxBanking.eq(index).show().siblings().hide();
                    } else if (data.bindCardStatus == "openFastPay") {
                        //  开通快捷支付
                        _this.addClass('active').siblings().removeClass('active');
                        boxBanking.eq(index).find('.recharge-bank').show()
                        boxBanking.eq(index).find('.jq-open-fast-pay').hide()
                        boxBanking.eq(index).show().siblings().hide();

                    }
                });

            } else {
                _this.addClass('active').siblings().removeClass('active');
                boxBanking.eq(index).show().siblings().hide();
            }

        });

        $('.close2,.cancel').click(function () {
            $('.bind-card-layer').hide();
        });

        //充值提交
        submitElement.click(function () {
            var amount = amountInputElement.autoNumeric("get");
            amountElement.val(amount);
            var content=$('#popRecharge');

            commonFun.popWindow('登录到联动优势支付平台充值',content,{width:'560px'});

        });

    });
});