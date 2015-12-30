require(['jquery',
    'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect',
    'moment'
], function($) {
    $(function() {
        $('.selectpicker').selectpicker();
        $('#datetimepicker1').datetimepicker({
            format: 'YYYY-MM-DD HH:mm'
        });
        $('#datetimepicker2').datetimepicker({
            format: 'YYYY-MM-DD HH:mm'
        });

        $('form button[type="reset"]').click(function() {
            location.href = "/finance-manage/system-bill";
        });

    });
});