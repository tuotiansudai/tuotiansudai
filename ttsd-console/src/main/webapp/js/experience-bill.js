require(['jquery', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker', 'jquery-ui'], function ($) {
    $(function () {

        $('#investDateBegin,#investDateEnd').datetimepicker({
            format: 'YYYY-MM-DD'
        });
        $('.selectpicker').selectpicker();

        //自动完成提示
        var autoValue = '';
        $(".jq-loginName").autocomplete({
            source: function (query, process) {
                $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        });

        $(".jq-loginName").blur(function () {
            for (var i = 0; i < autoValue.length; i++) {
                if ($(this).val() == autoValue[i]) {
                    $(this).removeClass('Validform_error');
                    return false;
                } else {
                    $(this).addClass('Validform_error');
                }
            }
        });

        $('form button[type="reset"]').click(function () {
            window.location.href = "/experience-manage/experience-bill";
        });

    });
})