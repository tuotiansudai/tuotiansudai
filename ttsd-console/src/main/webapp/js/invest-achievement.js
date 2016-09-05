require(['jquery', 'jquery-ui', 'moment'], function ($) {
    $(function () {

        var autoValue = '';
        $("#mobile").autocomplete({
            source: function (query, process) {
                //var matchCount = this.options.items;//返回结果集最大数量
                $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        });
        $("#mobile").blur(function () {
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
            location.href = "/export/invest-achievement?" + $('form').serialize();
        });

    });
});