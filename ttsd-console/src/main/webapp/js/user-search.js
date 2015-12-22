require(['jquery', 'jquery-ui', 'bootstrap', 'moment', 'csrf'], function ($) {

    $(function () {
        $('form button[type="reset"]').click(function () {
            location.href = "/user-manage/users-search";
        });

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

    });
});
