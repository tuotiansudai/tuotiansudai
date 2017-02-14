require(['jquery', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment', 'csrf'], function ($) {
    $(function () {
        $('.selectpicker').selectpicker();
        $('.date').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});

        $('form button[type="reset"]').click(function () {
            location.href = "/user-manage/user-micro-model";
        });


        $('.down-load').click(function () {
            location.href = "/export/user-micro-model?" + $('form').serialize();
        });
    });
});
