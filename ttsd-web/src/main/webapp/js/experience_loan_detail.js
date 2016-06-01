require(['jquery', 'layerWrapper','underscore', 'jquery.ajax.extension', 'autoNumeric', 'coupon-alert','red-envelope-float', 'jquery.form'], function ($, layer, _) {
    var loanProgress = $('.loan-detail-content').data('loan-progress');

    if (loanProgress <= 50) {
        $('.chart-box .rount').css('webkitTransform', "rotate(" + 3.6 * loanProgress + "deg)");
        $('.chart-box .rount2').hide();
    } else {
        $('.chart-box .rount').css('webkitTransform', "rotate(180deg)");
        $('.chart-box .rount2').show();
        $('.chart-box .rount2').css('webkitTransform', "rotate(" + 3.6 * (loanProgress - 50) + "deg)");
    }

    //submit  form
    function investSubmit(){
        $('#investForm').ajaxSubmit({
            dataType: 'json',
            url: '/no-password-invest',
            beforeSubmit: function (arr, $form, options) {
                $form.addClass("loading");
            },
            success: function (response, statusText, xhr, $form) {
                $form.removeClass("loading");
                var data = response.data;
                if (data.status) {
                    location.href = "/invest-success";
                } else if (data.message == '新手标投资已超上限') {
                    showLayer();
                } else {
                    showInputErrorTips(data.message);
                }
            }
        });

    }

    $('.close-free').on('click', function(event) {
        event.preventDefault();
        $('#freeSuccess').hide();
    });

});