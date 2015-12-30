require(['jquery', 'jquery-ui',
    'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect',
    'moment'], function ($) {
    $(function () {
        $('.selectpicker').selectpicker();
        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD HH:mm', maxDate: 'now'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD HH:mm', maxDate: 'now'});
        var dpicker2 = $('#datetimepicker2').data("DateTimePicker");
        $('#datetimepicker1').on('dp.change', function (e) {
            dpicker2.minDate(e.date);
        });

        $('form button[type="reset"]').click(function () {
            location.href = "/finance-manage/withdraw";
        });

        //自动完成提示
        var autoValue = '';
        $('#loginName').autocomplete({
            source: function (query, process) {
                //var matchCount = this.options.items;//返回结果集最大数量
                $.get('/user-manage/user/' + query.term + '/search', function (respData) {
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
            location.href = "/finance-manage/withdraw?"+$('form').serialize()+"&export=csv";
        });
    });
});
