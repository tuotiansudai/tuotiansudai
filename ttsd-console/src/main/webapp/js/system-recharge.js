require(['jquery', 'bootstrap','Validform','Validform_Datatype','jquery-ui','csrf','autoNumeric'], function ($) {
    $(function () {
        var errorMessage = $('.console-error-message');
        $("#amount").autoNumeric("init");
        $("#login-name").autocomplete({
            minLength: 3,
            source: function (query, process) {
                $.get('/user-manage/account/' + query.term + '/query', function (respData) {
                    $('#balance').val('');
                    autoValue = respData;
                    return process(respData);
                });
            },

            change: function (event, ui) {
                if (!ui.item) {
                    this.value = '';
                    $('#balance').val('');
                }
            },
            select:function (event, ui) {
                var loginName = ui.item.value;
                $.get('/account/' + loginName , function (respData) {
                    $('#balance').val(respData);
                })


            }
        });


        $('form input[type="submit"]').click(function () {
            if (!$('#login-name').val()) {
                $('.alert', errorMessage).html('资金来源账户不能为空');
                $('#balance').val('');
                errorMessage.show();
                return false;
            }

            var amount = $("#amount").val();
            if (!amount || parseFloat(amount) === 0) {
                $('.alert', errorMessage).html('充值金额不能为空');

                errorMessage.show();
                return false;
            }

            var balance = $("#balance").val();
            if (!balance || parseFloat(balance) === 0) {
                $('.alert', errorMessage).html('来源账户余额不能为空或零,不能进行平台账户充值');

                errorMessage.show();
                return false;
            }

            if(parseFloat(amount) > parseFloat(balance)){
                $('.alert', errorMessage).html('充值金额不能大于来源账户余额');

                errorMessage.show();
                return false;
            }

            $('#confirm-modal').modal('show');
            return false;
        });

        $('#confirm-modal .btn-submit').click(function () {
            $("form").submit();
        });
    });
})