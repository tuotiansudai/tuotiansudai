require(['jquery', 'layerWrapper', 'jquery.ajax.extension', 'autoNumeric'], function ($, layer) {
    var $withdraw = $('.withdraw'),
        amountInputElement = $(".amount-display", $withdraw),
        submitElement = $('.withdraw-submit', $withdraw),
        formElement = $('form', $withdraw),
        errorElement = $('.error', $withdraw),
        actualAmountElement = $('.actual-amount', $withdraw),
        withdrawFeeElement = $('.withdraw-fee', $withdraw);

    amountInputElement.autoNumeric("init");

    amountInputElement.keyup(function () {
        var amount = parseFloat(amountInputElement.autoNumeric("get")),
            withdrawFee = parseFloat(withdrawFeeElement.html());
        if (isNaN(amount) || amount <= withdrawFee) {
            submitElement.prop('disabled', true);
            errorElement.show();
            actualAmountElement.html('0.00');
        } else {
            submitElement.prop('disabled', false);
            errorElement.hide();
            actualAmountElement.html((amount - withdrawFee).toFixed(2));
        }
    });

    submitElement.click(function () {
        var amount = parseFloat(amountInputElement.autoNumeric("get"));
        if (isNaN(amount) || submitElement.hasClass('inactive')) {
            return false;
        }
        $(".withdraw form input[name='amount']").val(amount);
        formElement.submit();
        layer.open({
            type: 1,
            title: '登录到联动优势支付平台充值',
            area: ['560px', '270px'],
            shadeClose: true,
            content: $('#popWithdraw')
        });
    });
});
