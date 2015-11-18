require(['jquery', 'layer', 'csrf', 'autoNumeric'], function ($, layer) {
    layer.config({
        path: '/js/libs/layer/'
    });
        var $withdraw=$('.withdraw'),
         amountInputElement = $(".amount-display",$withdraw),
         submitElement = $('.withdraw-submit',$withdraw),
         formElement = $('form',$withdraw),
         errorElement = $('.error',$withdraw),
         actualAmountElement = $('.actual-amount',$withdraw);

        amountInputElement.autoNumeric("init");

        amountInputElement.keyup(function () {
            var amount = parseFloat(amountInputElement.autoNumeric("get"));
            if (isNaN(amount) || amount <= 3) {
                submitElement.prop('disabled',true);
                errorElement.show();
                actualAmountElement.html('0.00');
            } else {
                submitElement.prop('disabled',false);
                errorElement.hide();
                actualAmountElement.html(Number((amount - 3)).toFixed(2));
            }
        });

        submitElement.click(function () {
            if (submitElement.hasClass('inactive')) {
                return false;
            }
            var amount = parseFloat(amountInputElement.autoNumeric("get"));
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
