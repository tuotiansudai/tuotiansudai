require(['jquery', 'csrf', 'jquery-ui'], function ($) {

    function showErrorMessage(msg){
        $('.web-error-message').show();
        $('.console-error-message').hide();
        $('.message').html(msg);
    }

    function hideErrorMessage(){
        $('.web-error-message').hide();
    }

    $('.btn-submit').click(function(){
        hideErrorMessage();

        if ($('#loginName').val().trim().length == 0){
            showErrorMessage('登录名不能为空!');
            return false;
        }
        if ($('#userName').val().trim().length == 0){
            showErrorMessage('姓名不能为空!');
            return false;
        }
        if ($('#identityNumber').val().trim().length == 0){
            showErrorMessage('身份证不能为空!');
            return false;
        }
        if(!/^\d{11}$/.test($('#mobile').val())){
            showErrorMessage('手机号码应为11位数字！');
            return false;
        }
        if ($('#email').val().trim().length == 0){
            showErrorMessage('电子邮件不能为空!');
            return false;
        }
    });

    $('#referrer').autocomplete({
        minLength: 0,
        source: function (query, process) {
            //var matchCount = this.options.items;//返回结果集最大数量
            $.get('/user-manage/user/' + query.term + '/search', function (respData) {
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