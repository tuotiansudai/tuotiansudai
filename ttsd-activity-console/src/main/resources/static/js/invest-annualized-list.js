require(['jquery', 'underscore', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker', 'csrf'], function ($, layer, _) {

    $('.selectpicker').selectpicker();

    $('#startTime,#endTime').datetimepicker({
        format: 'YYYY-MM-DD'
    });

    $("#startTime").on("dp.change", function (e) {
        $('#endTime').data("DateTimePicker").minDate(e.date);
    });

    $("#endTime").on("dp.change", function (e) {
        $('#startTime').data("DateTimePicker").maxDate(e.date);
    });
});
