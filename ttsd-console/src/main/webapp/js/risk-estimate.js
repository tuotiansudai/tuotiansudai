require(['jquery', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment', 'csrf'], function ($) {
    $(function () {
        $('.selectpicker').selectpicker();

        $('form button[type="reset"]').click(function () {
            location.href = "/user-manage/risk-estimate";
        });
    });
});
