require(['jquery', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment', 'csrf'], function ($) {
    $(function () {
        $('.selectpicker').selectpicker();
        $('#registerStartDatetimepicker').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        $('#registerEndDatetimepicker').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        $('#experienceStartDatetimepicker').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        $('#experienceEndDatetimepicker').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        $('#firstInvestStartDatetimepicker').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        $('#firstInvestEndDatetimepicker').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        $('#secondInvestStartDatetimepicker').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        $('#secondInvestEndDatetimepicker').datetimepicker({format: 'YYYY-MM-DD HH:mm'});

        $('form button[type="reset"]').click(function () {
            location.href = "/user-manage/remain-users";
        });

        $('.down-load').click(function () {
            location.href = "/export/remain-users?" + $('form').serialize();
        });
    });
});