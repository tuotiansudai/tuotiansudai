require(['jquery', 'jquery-ui',
    'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect',
    'moment', 'csrf'], function ($) {
    $(function () {
        $('.selectpicker').selectpicker();
        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD HH:mm'});

        $('form button[type="reset"]').click(function () {
            location.href = "/user-manage/users";
        });
        //自动完成提示
        var autoValue = '';
        $("#loginName, #input-referrer").autocomplete({
            source: function (query, process) {
                //var matchCount = this.options.items;//返回结果集最大数量
                $.get('/user-manage/user/' + query.term + '/search', function (respData) {
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
            location.href = "/user-manage/users?"+$('form').serialize()+"&export=csv";
        });
    });
});
