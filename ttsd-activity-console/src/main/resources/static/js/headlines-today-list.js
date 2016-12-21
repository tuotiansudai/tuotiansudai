require(['jquery', 'underscore', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker'], function ($, _) {
    $(function () {
        $('.selectpicker').selectpicker();

        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});

        $("select[name='prizeType']").change(function () {
            var self = $(this);
            $.ajax({
                url: '/activity-console/activity-manage/category?activityCategory=' + self.val(),
                type: 'GET',
                dataType: 'json'
            }).done(function (data) {

            });
        });

        $('#activityHeadlinesTodayExport').click(function () {
            location.href = "/activity-console/activity-manage/export-headlines-today-record?" + $('#prizeFrom').serialize();
        });
    });
});
