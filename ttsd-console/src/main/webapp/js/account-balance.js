require(['jquery', 'bootstrap','jquery-ui'], function ($) {

    var autoValue = '';
    $(".jq-loginName").autocomplete({
        source: function (query, process) {
            $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
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
        window.location.href = "/finance-manage/account-balance?balanceMin=50";
    });

    $('.down-load').click(function(){
        var mobile = $('.jq-loginName').val();
        window.location.href = "/finance-manage/account-balance?mobile="+mobile+"&export=csv";
    });

})