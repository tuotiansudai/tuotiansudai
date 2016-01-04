require(['jquery', 'layerWrapper', 'csrf', 'autoNumeric'], function ($,layer) {
        var $withdraw=$('.withdraw'),
         amountInputElement = $(".amount-display",$withdraw),
         submitElement = $('.withdraw-submit',$withdraw),
         formElement = $('form',$withdraw),
         errorElement = $('.error',$withdraw),
         actualAmountElement = $('.actual-amount',$withdraw),
         withdrawFeeElement = $('.withdraw-fee',$withdraw);

        amountInputElement.autoNumeric("init");

        amountInputElement.keyup(function () {
            var inputAmount = parseFloat(amountInputElement.autoNumeric("get")).toFixed(3),
                amount = parseFloat(inputAmount.substring(0, inputAmount.lastIndexOf('.') + 3)),
                withdrawFee = parseFloat(withdrawFeeElement.html());

            if (isNaN(amount) || amount <= withdrawFee) {
                submitElement.prop('disabled',true);
                errorElement.show();
                actualAmountElement.html('0.00');
            } else {
                submitElement.prop('disabled',false);
                errorElement.hide();
                actualAmountElement.html(Number((amount - withdrawFee)).toFixed(2));
            }
        });

        submitElement.click(function () {
            if (submitElement.hasClass('inactive')) {
                return false;
            }
            var inputAmount = parseFloat(amountInputElement.autoNumeric("get")).toFixed(3),
                amount = parseFloat(inputAmount.substring(0, inputAmount.lastIndexOf('.') + 3));
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
