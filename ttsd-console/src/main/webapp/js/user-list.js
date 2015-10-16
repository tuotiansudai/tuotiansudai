/**
 <!--[if lt IE 9]>
 <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
 <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
 * Created by zzg on 15/10/16.
 */
require(['jquery', 'jquery-ui',
    'bootstrap','bootstrapDatetimepicker','bootstrapSelect',
    'moment','moment-with-locales', 'csrf' ], function ($) {
    $(function () {
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
        var api_url = '${requestContext.getContextPath()}/user/name-like-query';
        $("#loginName").autocomplete({
            source: function (query, process) {
                //var matchCount = this.options.items;//返回结果集最大数量
                $.get(api_url + '/' + query.term, function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        });
        $("#loginName").blur(function () {
            for (var i = 0; i < autoValue.length; i++) {
                if ($(this).val() == autoValue[i]) {
                    $(this).removeClass('Validform_error');
                    return false;
                } else {
                    $(this).addClass('Validform_error');
                }

            }

        });

        $('.user-status-modifier').click(function(){
            var _this = $(this);
            $.ajax({
                url: _this.attr('href'),
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function (data) {
                location.reload();
            }).fail(function (data) {
                alert('操作失败');
                console.log(data);
            });
            return false;
        });
    });
});
