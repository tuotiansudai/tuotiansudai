require(['jquery', 'underscore', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker'], function ($, _) {
    $('#startTime,#endTime').datetimepicker({
        format: 'YYYY-MM-DD'
    });

    $("#startTime").on("dp.change", function (e) {
        $('#endTime').data("DateTimePicker").minDate(e.date);
    });

    $("#endTime").on("dp.change", function (e) {
        $('#startTime').data("DateTimePicker").maxDate(e.date);
    });

    var mobileElement = $('#mobile');

    //自动完成提示
    mobileElement.autocomplete({
        source: function (query, process) {
            //var matchCount = this.options.items;//返回结果集最大数量
            $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
                return process(respData);
            });
        }
    });
});
