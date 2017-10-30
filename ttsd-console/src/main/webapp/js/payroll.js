require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment', 'csrf'], function ($) {

    $(function () {
        $('.selectpicker').selectpicker();

        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker3').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker4').datetimepicker({format: 'YYYY-MM-DD'});

        $('.down-load').click(function () {
            window.location.href = "/export/payroll?" + $('form').serialize();
        });

        $('.detail-down-load').click(function () {
            window.location.href = "/export/payroll-detail?" + $('form').serialize();
        });

        $('.btnRemark').click(function () {
            var self = $(this);
            $('#id').prop('value', self.data('payroll-id'));
            $('#remark').prop('value', self.data('remark'));
            $('#remarkModal').modal({});
        });

        $('.btnSubmit').on('click', function (e) {
            $.ajax({
                url: '/payroll-manage/update/remark',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8',
                data: JSON.stringify({
                    'id': $('#id').val(),
                    'remark': $('#remark').val()
                }),
            }).done(function (data) {
                $('#remarkModal').modal('hide');
                location.reload();
            })
        });
    })

});
