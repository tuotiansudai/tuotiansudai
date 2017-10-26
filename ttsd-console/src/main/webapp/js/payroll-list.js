require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment'], function ($) {

    $(function () {
        $('.selectpicker').selectpicker();

        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker3').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker4').datetimepicker({format: 'YYYY-MM-DD'});

        $('.down-load').click(function () {
            window.location.href = "/export/payroll?" + $('form').serialize();
        });

        $('.btnRemark').click(function () {
            var self = $(this);
            $('#id').prop('value', self.data('payroll-id'));
            $('#remark').prop('value', self.data('remark'));
            $('#remarkModal').modal({});
        });

        $('.btnSubmit').click(function () {
            $('#remarkForm').submit();
        })

    })

});
