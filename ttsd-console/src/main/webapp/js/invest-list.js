require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment'], function ($) {
    $('.selectpicker').selectpicker();

    $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD', maxDate: 'now'});
    $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD', maxDate: 'now'});
    var dpicker2 = $('#datetimepicker2').data("DateTimePicker");
    $('#datetimepicker1').on('dp.change', function (e) {
        dpicker2.minDate(e.date);
    });
    $('form button[type="reset"]').click(function () {
        location.href = "invests";
    });
    $('form button[type="submit"]').click(function (event) {
        var queryParams = '';
        if ($('form input[name="loanId"]').val()) {
            queryParams += "loanId=" + $('form input[name="loanId"]').val() + "&";
        }
        if ($('form input[name="loginName"]').val().length > 0) {
            queryParams += "loginName=" + $('form input[name="loginName"]').val() + "&";
        }
        if ($('form input[name="startTime"]').val()) {
            queryParams += "startTime=" + $('form input[name="startTime"]').val() + "&";
        }
        if ($('form input[name="endTime"]').val()) {
            queryParams += "endTime=" + $('form input[name="endTime"]').val() + "&";
        }
        if ($('form select[name="investStatus"]').val()) {
            queryParams += "investStatus=" + $('form select[name="investStatus"]').val() + "&";
        }
        if ($('form select[name="source"]').val()) {
            queryParams += "source=" + $('form select[name="source"]').val() + "&";
        }
        if ($('form select[name="channel"]').val()) {
            queryParams += "channel=" + $('form select[name="channel"]').val() + "&";
        }
        if ($('form select[name="role"]').val()) {
            queryParams += "role=" + $('form select[name="role"]').val() + "&";
        }
        location.href = "?" + queryParams;
        return false;
    });

    //自动完成提示
    var autoValue = '';
    var api_url = '/loan/loaner';
    $("#tags").autocomplete({
        source: function (query, process) {
            //var matchCount = this.options.items;//返回结果集最大数量
            $.get(api_url + '/' + query.term, function (respData) {
                autoValue = respData;
                return process(respData);
            });
        }
    });
    $("#tags").blur(function () {
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
