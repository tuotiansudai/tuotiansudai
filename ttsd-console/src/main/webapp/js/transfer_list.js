require(['jquery','bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment'], function ($) {
    $(function() {
        $('.transfer-list-box ul li').on('click', function(event) {
            event.preventDefault();
            var $self=$(this),
                urldata=$self.attr('data-url');
            console.log('1');
            location.href=urldata;
        });
    });

    $('.selectpicker').selectpicker();

    $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
    $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
    $('form button[type="reset"]').click(function () {
        location.href = "transfer-list";
    });

    //自动完成提示
    var autoValue = '';
    $(".ui-autocomplete-input").autocomplete({
        source: function (query, process) {
            $.get('/user-manage/account/' + query.term + '/query', function (respData) {
                autoValue = respData;
                return process(respData);
            });
        }
    });
    $(".ui-autocomplete-input").blur(function () {
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
