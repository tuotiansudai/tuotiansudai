require(['jquery', 'jquery.ajax.extension', 'coupon-alert', 'autoNumeric', 'red-envelope-float', 'jquery.form'], function ($) {
    var loanProgress = $('.loan-detail-content').data('loan-progress'),
        amountInputElement = $(".text-input-amount", $('.loan-detail-content'));

    if (loanProgress <= 50) {
        $('.chart-box .rount').css('transform', "rotate(" + 3.6 * loanProgress + "deg)");
        $('.chart-box .rount2').hide();
    } else {
        $('.chart-box .rount').css('transform', "rotate(180deg)");
        $('.chart-box .rount2').show();
        $('.chart-box .rount2').css('transform', "rotate(" + 3.6 * (loanProgress - 50) + "deg)");
    }

    if (amountInputElement.length) {
        amountInputElement.autoNumeric("init");
        amountInputElement.focus(function () {
            layer.closeAll('tips');
        });
    }

    var queryParams = [];

    var getInvestAmount = function() {
        var amount = 0;
        if (!isNaN(amountInputElement.autoNumeric("get"))) {
            amount = parseInt((amountInputElement.autoNumeric("get") * 100).toFixed(0));
        }

        return amount;
    };

    $.ajax({
        url: '/calculate-expected-coupon-interest/loan/1/amount/' + getInvestAmount(),
        type: 'GET',
        data: {"loanId":'1',"amount":getInvestAmount(),"couponIds":''},
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8'
    }).done(function (amount) {
        $(".principal-income").text(amount);
    });

    $('#investSubmit').on('click', function (event) {
        var self = $(this);
        $("#investForm").ajaxSubmit({
            dataType: 'json',
            type: 'POST',
            url: '/experience-invest',
            beforeSubmit: function (arr, $form, options) {
                console.log($form);
                console.log(arr);
                self.addClass("loading");
            },
            success: function (response, statusText, xhr, $form) {
                var data = response.data;
                if (data.status) {
                    $("#freeSuccess").show();
                }
                self.removeClass("loading");
            }
        });
        return false;
    });

    $('.close-free').on('click', function (event) {
        event.preventDefault();
        $('#freeSuccess').hide();
        window.location.reload();
    });
});