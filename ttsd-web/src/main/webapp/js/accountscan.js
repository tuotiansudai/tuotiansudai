require(['jquery', 'daterangepicker', 'moment', 'csrf'], function ($) {
    $('#daterangepicker').dateRangePicker({separator: ' ~ '});


    //还款计划
    $('.plan').click(function () {
        $('.layer-box').show();
        return false;
    });
    $('.layer-box .close').click(function () {
        $('.layer-box').hide();
        return false;
    });
    $('.layer-fix').click(function () {
        $('.layer-box').hide();
    });


});