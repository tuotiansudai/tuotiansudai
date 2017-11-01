require(['jquery', 'bootstrap', 'layer', 'layer-extend', 'layerWrapper', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'autoNumeric', 'moment', 'csrf'], function ($) {

    $(function () {
        $('.selectpicker').selectpicker();

        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker3').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker4').datetimepicker({format: 'YYYY-MM-DD'});

        $('.btnRemark').click(function () {
            var self = $(this);
            $('#id').prop('value', self.data('payroll-id'));
            $('#remarkModal').modal({});
        });

        $('.tooltip-list').on('mouseover', function () {
            var that = this,
                tiptext = $(this).attr('data-original-title');
            layer.tips(tiptext, that, {
                tips: [4, '#000000']
            });
        });

        $('.btnSubmit').on('click', function (e) {
            var remark = $('#remark').val();

            if (remark == '' || remark.trim() == '') {
                alert('备注不能为空');
                return false;
            }
            $.ajax({
                url: '/finance-manage/payroll-manage/update/remark',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8',
                data: JSON.stringify({
                    'id': $('#id').val(),
                    'remark': remark
                }),
            }).done(function (data) {
                $('#remarkModal').modal('hide');
                location.reload();
            })
        });

        $('.primary-audit').click(function () {
            var self = $(this);
            if (confirm("请确认金额无误后再通过该申请?")) {
                $.ajax({
                    url: self.data('url'),
                    type: 'GET',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8',
                }).done(function (result) {
                    if (result.data.status) {
                        window.location.href = "/finance-manage/payroll-manage/payroll-list"
                    } else {
                        alert(result.data.message)
                    }
                })
            }
        });

        $('.advanced-audit').click(function () {
            var self = $(this);

            $.ajax({
                url: self.data('url'),
                type: 'GET',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8',
            }).done(function (result) {
                if (result.data.status) {
                    window.location.href = "/finance-manage/payroll-manage/payroll-list"
                } else {
                    alert(result.data.message)
                }
            })
        });

        $('.reject').click(function () {
            var self = $(this);
            if (confirm("确认驳回该申请")) {
                $.ajax({
                    url: self.data('url'),
                    type: 'GET',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8',
                }).done(function () {

                    window.location.href = "/finance-manage/payroll-manage/payroll-list"

                }).fail(function () {
                    window.location.href = "/finance-manage/payroll-manage/payroll-list"
                })
            }
        });

    })

});
