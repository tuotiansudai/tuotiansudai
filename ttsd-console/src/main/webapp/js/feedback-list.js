require(['jquery', 'jquery-ui', 'bootstrap'], function ($) {
    $(function () {
        //自动完成提示
        var autoValue = '';
        $(".jq-loginName").autocomplete({
            source: function (query, process) {
                $.get('/user-manage/user/' + query.term + '/search', function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        }).blur(function () {
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