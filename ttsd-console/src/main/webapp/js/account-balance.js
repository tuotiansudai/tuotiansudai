require(['jquery', 'bootstrap','jquery-ui'], function ($) {

    var autoValue = '';
    $(".jq-loginName").autocomplete({
        source: function (query, process) {
            $.get('/user-manage/user/' + query.term + '/search', function (respData) {
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
        window.location.href = "/finance-manage/account-balance";
    });

    $('.down-load').click(function(){
        var loginName = $('.jq-loginName').val();
        window.location.href = "/finance-manage/account-balance?loginName="+loginName+"&export=csv";
    });

})