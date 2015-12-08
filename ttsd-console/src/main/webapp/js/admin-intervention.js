require(['jquery', 'csrf', 'jquery-ui', 'autoNumeric', 'bootstrapSelect', 'bootstrap'], function ($) {
    var errorMessage = $('.console-error-message');

    $('select[name="operationType"]').selectpicker();

    $('#login-name').autocomplete({
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

    $('form input[type="submit"]').click(function () {
        if (!$('#login-name').val()) {
            $('.alert', errorMessage).html('用户名不能为空');
            errorMessage.show();
            return false;
        }

        var amount = $("#amount").val();
        if (!amount || parseFloat(amount) === 0) {
            $('.alert', errorMessage).html('金额不能为空');
            errorMessage.show();
            return false;
        }

        if (!$("#description").val()) {
            $('.alert', errorMessage).html('详情不能为空');
            errorMessage.show();
            return false;
        }
        $('#confirm-modal').modal('show');
        return false;
    });

    $('#confirm-modal .btn-submit').click(function () {
        $("form").submit();
    });

    $("#amount").autoNumeric("init");

});