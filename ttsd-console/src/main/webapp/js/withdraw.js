require(['jquery', 'jquery-ui',
    'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect',
    'moment'], function ($) {
    $(function () {
        $('.selectpicker').selectpicker();
        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});
        $('#datetimepicker2').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss'
        }).on('dp.change', function (ev) {
            if ($(this).find('.form-control').val().split(' ')[1] == '00:00:00') {
                $(this).find('.form-control').val($(this).find('.form-control').val().split(' ')[0] + ' 23:59:59')
            }
        });

        $('form button[type="reset"]').click(function () {
            location.href = "/finance-manage/withdraw";
        });

        //自动完成提示
        var autoValue = '';
        $('#loginName').autocomplete({
            source: function (query, process) {
                //var matchCount = this.options.items;//返回结果集最大数量
                $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        });
        $('#loginName').blur(function () {
            for (var i = 0; i < autoValue.length; i++) {
                if ($(this).val() == autoValue[i]) {
                    $(this).removeClass('Validform_error');
                    return false;
                } else {
                    $(this).addClass('Validform_error');
                }
            }
        });

        $('.down-load').click(function () {
            location.href = "/export/withdraw?" + $('form').serialize();
        });
    });
});
