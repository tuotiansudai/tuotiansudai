require(['jquery', 'csrf', 'jquery-ui', 'bootstrap'], function ($) {
    $('.btn-submit').click(function () {
        var mobile = $('#mobile').val();
        var reg = /^\d{11}$/;

        if (!reg.test(mobile)) {
            $('.web-error-message').show();
            $('.console-error-message').hide();
            $('.message').html('手机号码应为11位数字！');
            return false;
        }

        $('.web-error-message').hide();
        $('#confirm-modal').modal('show');
        return false;
    });

    $('#referrer').autocomplete({
        minLength: 3,
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

    $('#confirm-modal').find('.btn-submit').click(function () {
        $("form").submit();
    });

    $('.btn-refuse').click(function () {
        $.ajax({
            url: '/refuse?taskId='+$('.taskId').val(),
            type: 'GET',
            dataType: 'json',
            data: {}
        })
        .done(function() {
            window.location="/user-manage/users";
        })
        .fail(function() {
            window.location="/";
        });
    });

    $('input[type="reset"]').click(function () {
        location.reload();
        return false;
    });


    $('input[name="roles"]').click(function () {
        var self = $(this);
        if (self.val() === 'AGENT' && self.prop('checked')) {
            $('input[name="roles"][value="STAFF"]').prop('checked', true);
        }
        if (self.val() === 'STAFF' && !self.prop('checked')) {
            $('input[name="roles"][value="AGENT"]').prop('checked', false);
        }
    });
});