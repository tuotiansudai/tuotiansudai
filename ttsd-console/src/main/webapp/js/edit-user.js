require(['jquery', 'csrf', 'jquery-ui'], function ($) {

    $('.btn-submit').click(function(){
        var mobile = $('#mobile').val();
        var reg = /^\d{11}$/;

        if(!reg.test(mobile)){
            $('.web-error-message').show();
            $('.console-error-message').hide();
            $('.message').html('手机号码应为11位数字！');
            return false;
        }else{
            $('.web-error-message').hide();
        }
    });

    $('#referrer').autocomplete({
        minLength: 0,
        source: function (query, process) {
            //var matchCount = this.options.items;//返回结果集最大数量
            $.get('/user/' + query.term + '/search', function (respData) {
                return process(respData);
            });
        },
        change: function (event, ui) {
            if (!ui.item) {
                this.value = '';
            }
        }
    });

    $("input[type='reset']").click(function() {
        location.reload();
        return false;
    });
});