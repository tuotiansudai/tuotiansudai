require(['jquery', 'bootstrap','bootstrapSelect','bootstrapDatetimepicker','jquery-ui'], function ($) {
    $(function () {

        $('#startDate,#endDate').datetimepicker({
            format: 'YYYY-MM-DD'
        });
        $('#endDate').datetimepicker({
            useCurrent: false
        });
        $("#startDate").on("dp.change", function (e) {
            $('#endDate').data("DateTimePicker").minDate(e.date);
        });
        $("#endDate").on("dp.change", function (e) {
            $('#startDate').data("DateTimePicker").maxDate(e.date);
        });
        $('.selectpicker').selectpicker();

        //自动完成提示
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

        $('.search').click(function() {
            var loginName = $('.jq-loginName').val();
            var startDate = $('.jq-startDate').val();
            var endDate = $('.jq-endDate').val();
            window.location.href = "/finance-manage/transfer-bill?loginName="+loginName+"&startDate="+startDate+"&endDate="+endDate;
        });
    });
});