require(['jquery', 'csrf', 'jquery-ui', 'bootstrapDatetimepicker', 'bootstrapSelect', 'bootstrap', 'moment'], function ($) {

    $('.selectpicker').selectpicker();

    var $startTime = $('#startTime');
    var $endTime = $('#endTime');

    $startTime.datetimepicker({format: 'YYYY-MM-DD', locale: 'zh-cn'});
    $endTime.datetimepicker({format: 'YYYY-MM-DD', locale: 'zh-cn'});

    $('form button[type="reset"]').click(function () {
        location.href = "/security-log/audit-log";
    });

    //自动完成提示
    var autoValue = '';
    $("#operator-login-name, #auditor-login-name").autocomplete({
        source: function (query, process) {
            $.get('/user-manage/user/' + query.term + '/search', function (respData) {
                autoValue = respData;
                return process(respData);
            });
        }
    });
    $("#operator-login-name, #auditor-login-name").blur(function () {
        for (var i = 0; i < autoValue.length; i++) {
            if ($(this).val() == autoValue[i]) {
                $(this).removeClass('Validform_error');
                return false;
            } else {
                $(this).addClass('Validform_error');
            }
        }
    });
});