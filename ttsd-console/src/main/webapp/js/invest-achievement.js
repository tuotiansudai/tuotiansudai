require(['jquery', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'moment'], function ($) {
    $(function () {

        $('.selectpicker').selectpicker();

        var autoValue = '';
        $("#loginName").autocomplete({
            source: function (query, process) {
                //var matchCount = this.options.items;//返回结果集最大数量
                $.get('/user-manage/user/' + query.term + '/search', function (respData) {
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

        $('.down-load').click(function () {
            location.href = "/activity-manage/invest-achievement?"+$('form').serialize()+"&export=csv";
        });

    });
});