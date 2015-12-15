require(['jquery',
    'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect',
    'moment'
], function($) {
    $(function() {
        $('.selectpicker').selectpicker();
        $('#datetimepicker1').datetimepicker({
            format: 'YYYY-MM-DD HH:mm',
            maxDate: 'now'
        });
        $('#datetimepicker2').datetimepicker({
            format: 'YYYY-MM-DD HH:mm',
            maxDate: 'now'
        });

        var dpicker2 = $('#datetimepicker2').data("DateTimePicker");
        $('#datetimepicker1').on('dp.change', function(e) {
            dpicker2.minDate(e.date);
        });

        $('form button[type="reset"]').click(function() {
            location.href = "/finance-manage/system-bill";
        });

    });
});