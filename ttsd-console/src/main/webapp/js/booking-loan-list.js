require(['jquery', 'csrf', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker', 'jquery-ui'], function ($) {
    $(function () {
        $('.selectpicker').selectpicker();
        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker3').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker4').datetimepicker({format: 'YYYY-MM-DD'});

    });

    $('.down-load').click(function () {
        location.href = "/booking-loan-manage/export?" + $('form').serialize();
    });
})