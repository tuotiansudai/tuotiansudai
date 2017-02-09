require(['jquery', 'jquery.ajax.extension', 'coupon-alert', 'red-envelope-float', 'jquery.form'], function ($) {
    var loanProgress = $('.loan-detail-content').data('loan-progress');

    if (loanProgress <= 50) {
        $('.chart-box .rount').css('transform', "rotate(" + 3.6 * loanProgress + "deg)");
        $('.chart-box .rount2').hide();
    } else {
        $('.chart-box .rount').css('transform', "rotate(180deg)");
        $('.chart-box .rount2').show();
        $('.chart-box .rount2').css('transform', "rotate(" + 3.6 * (loanProgress - 50) + "deg)");
    }

    $.ajax({
        url: '/calculate-expected-coupon-interest/loan/1/amount/0',
        data: $.param([{
            'name': 'couponIds',
            'value': $("input[name='userCouponIds']").data("coupon-id")
        }]),
        type: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8'
    }).done(function (amount) {
        $(".principal-income").text(amount);
    });

    $('#investSubmit').on('click', function (event) {
        var self = $(this);
        $("#investForm").ajaxSubmit({
            dataType: 'json',
            url: '/experience-invest',
            beforeSubmit: function (arr, $form, options) {
                self.addClass("loading");
            },
            success: function (response, statusText, xhr, $form) {
                var data = response.data;
                if (data.status) {

                    layer.open({
                        type: 1,
                        title: '&nbsp',
                        area: ['400px', '205px'],
                        content: $('#freeSuccess')
                    });
                }
                self.removeClass("loading");
            }
        });
        return false;
    });

    $('.close-free').on('click', function (event) {
        event.preventDefault();
        layer.closeAll();
        window.location.reload();
    });
});