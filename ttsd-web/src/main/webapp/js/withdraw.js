require(['jquery', 'csrf', 'autoNumeric'], function ($) {
    $(function () {
        var amountInputElement = $(".withdraw .amount-display");
        var submitElement = $('.withdraw-submit');
        var formElement = $('.withdraw form');
        var errorElement = $('.withdraw .error');
        var actualAmountElement = $('.withdraw .actual-amount');

        amountInputElement.autoNumeric("init");

        amountInputElement.keyup(function () {
            var amount = parseFloat(amountInputElement.autoNumeric("get"));
            if (isNaN(amount) || amount <= 3) {
                submitElement.addClass('inactive').attr('disabled', true);
                errorElement.show();
                actualAmountElement.html('0.00');
            } else {
                submitElement.removeClass('inactive').attr('disabled', false);
                errorElement.hide();
                actualAmountElement.html(Number((amount - 3).toFixed(2)));
            }
        });

        submitElement.click(function () {
            if (submitElement.hasClass('inactive')) {
                return false;
            }

            var amount = parseFloat(amountInputElement.autoNumeric("get"));
            $(".withdraw form input[name='amount']").val(amount);
            $('.ecope-overlay,.ecope-dialog').show();
            formElement.submit();
        });
    });
});