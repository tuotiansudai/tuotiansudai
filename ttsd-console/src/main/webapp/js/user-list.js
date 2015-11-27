require(['jquery', 'jquery-ui',
    'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect',
    'moment', 'csrf'], function ($) {
    $(function () {
        $('.selectpicker').selectpicker();
        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD HH:mm', maxDate: 'now'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD HH:mm', maxDate: 'now'});
        var dpicker2 = $('#datetimepicker2').data("DateTimePicker");
        $('#datetimepicker1').on('dp.change', function (e) {
            dpicker2.minDate(e.date);
        });
        $('form button[type="reset"]').click(function () {
            location.href = "users";
        });
        //自动完成提示
        var autoValue = '';
        $("#loginName, #input-referrer").autocomplete({
            source: function (query, process) {
                $.get('/user/' + query.term + '/search', function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        });
        $("#loginName, #input-referrer").blur(function () {
            for (var i = 0; i < autoValue.length; i++) {
                if ($(this).val() == autoValue[i]) {
                    $(this).removeClass('Validform_error');
                    return false;
                } else {
                    $(this).addClass('Validform_error');
                }
            }
        });

        $('.user-status-modifier').click(function () {
            var _this = $(this);
            $.ajax({
                url: _this.data('url'),
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function (data) {
                if (data == 'OK') {
                    location.reload();
                } else {
                    alert('操作失败：' + data);
                }
            }).fail(function (data) {
                alert('操作失败');
            });
            return false;
        });

        $('.down-load').click(function () {
            location.href = "/users?"+$('form').serialize()+"&export=csv";
        });
    });
});
