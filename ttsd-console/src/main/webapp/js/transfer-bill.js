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

        $('.search').click(function() {
            var loginName = $('.jq-loginName').val();
            var startDate = $('.jq-startDate').val();
            var endDate = $('.jq-endDate').val();
            window.location.href = "/finance-manage/real-time-status/ump/transfer-bill?loginName="+loginName+"&startDate="+startDate+"&endDate="+endDate;
        });
    });
});