require(['jquery','bootstrap', 'bootstrapSelect','bootstrapDatetimepicker','jquery-ui'], function($) {
    $(function () {
        $('.selectpicker').selectpicker();

        $('#registerDateBegin,#registerDateEnd').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss'
        });
        $('#registerDateEnd').datetimepicker({
            useCurrent: false
        });
        $("#registerDateBegin").on("dp.change", function (e) {
            $('#registerDateEnd').data("DateTimePicker").minDate(e.date);
        });
        $("#registerDateEnd").on("dp.change", function (e) {
            $('#registerDateBegin').data("DateTimePicker").maxDate(e.date);
        });

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
            var couponId = $('.coupon-id').val();
            window.location.href = "/activity-manage/coupon/"+couponId+"/detail";
        });


    });

});